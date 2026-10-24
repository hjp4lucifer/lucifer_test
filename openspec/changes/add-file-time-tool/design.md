# Design

## Context

动机见 `proposal.md` 的 Why 一节，需求见 `specs/file-time/spec.md`。这里只记录当前状态与约束中会影响做法的部分。

- 全仓库检索 `setLastModified` / `FileTime` / `creationTime` / `BasicFileAttributeView` **零命中**，没有可复用的时间写入代码，从零开始。
- 目标盘（`C:` / `E:` / `F:` / `M:`）经 `Get-Volume` 确认**全部是 NTFS**。NTFS 时间精度为 100ns，不存在 FAT32/exFAT 那种"访问时间只有天精度"的问题；但也意味着本设计只在 NTFS 上验证过。
- 本机 `fsutil behavior query disablelastaccess` 返回 `DisableLastAccess = 2 (System Managed, Disabled)`——系统**不维护**访问时间。这一条直接影响了下面的第一个决策。
- 现有目录遍历风格：`RenameFileByNumberService#leftFileRightFolder` 用 `folder.listFiles(filter)` 把文件与子目录分开，测试侧用 `handleFolder` 递归并传 `excludeFolderList`。新工具沿用同一套划分方式，保持代码风格一致。

## Goals / Non-Goals

**Goals:**

- 把三个时间戳一次性写成同一个值，且写入结果是可预测、可重复的。
- 提供"单个文件"入口，能在不触碰整目录的前提下完成验证。
- 默认不落盘，让"第一次跑"永远是无损的。

**Non-Goals:**

- 不做时间戳的备份与回滚（原值一旦写入即丢失，靠试运行兜底）。
- 不从 EXIF 或文件名推断日期（那是另一种取值模式，不在本次范围）。
- 不把结果持久化到日志文件（只在控制台输出）。
- 不处理目录自身的时间戳，只处理文件。

## Decisions

### 1. 用 `BasicFileAttributeView#setTimes` 写三格，不用 `File#setLastModified`

`java.io.File#setLastModified` 在 Windows 上只能改修改时间。实测对照如下（jshell 在 NTFS 临时文件上跑，跑完已删除）：

```
先用 setTimes 把三格设为 2018-01-02T03:04:05Z
再调用 File.setLastModified(2024-12-31T00:00:00Z)

读回：creation=2018-01-02T03:04:05Z   <- 未变
      modified=2024-12-31T00:00:00Z   <- 只有它变了
      access  =2018-01-02T03:04:05Z   <- 未变
```

改用 `Files.getFileAttributeView(p, BasicFileAttributeView.class).setTimes(m, a, c)` 后，三格都精确落到目标值。Windows 实现内部是单次 `SetFileTime`，三格同时生效，不会出现只写了一半的中间态。

**替代方案**：`Files.setAttribute(p, "creationTime", ft)` 分三次调用。放弃，因为三次独立调用意味着三次出错机会，且中间态可见。

### 2. `setTimes` 的实参顺序是 (修改, 访问, 创建)

这是最容易写错的地方，用三个不同值把顺序钉死（同样实测）：

```
setTimes(ARG1, ARG2, ARG3)  ->  ARG1 落到 modified
                                ARG2 落到 access
                                ARG3 落到 creation
```

**替代方案**：三个参数传同一个值。放弃作为验证手段——顺序错了也看不出来，只有用不同值才能证伪。

### 3. `min` 只用创建时间与修改时间，排除访问时间

原设想是三格取最小，实测环境证据后改为两格：

- 访问时间在本机根本不被维护（`DisableLastAccess = 2`），它保存的可能是任意陈旧值。
- 一旦它恰好比修改时间早很多年，`min` 会把修改时间往回拉若干年，而结果看起来完全正常，极难察觉。
- 它带来的"正确收益"几乎为零：真实场景里访问时间极少是最小的那个，除了陈旧残留。

顺带一个正向效果：既然系统不维护访问时间，我们写进去的访问时间会**原样保留**，不会因为之后"看一眼图片"而被刷新。

**替代方案**：三格参与比较 + 一个 `ignoreAccessTime` 子开关。放弃，因为默认值与"实际可靠的做法"会分裂，多一个容易遗忘的开关。

### 4. 一次 `readAttributes` 拿全三个时间

`Files.readAttributes(p, BasicFileAttributes.class)` 走 `GetFileAttributesEx`，**不打开文件**，因此不会因为"读"而刷新访问时间。分三次调用 `getLastModifiedTime` 之类虽然结果一样，但多两次系统调用，且给未来误用"打开文件读属性"埋下隐患。

### 5. 三格相等即跳过，保证幂等

对齐模式天然收敛：写完后三格相等，`min` 仍等于它自己。故加一条前置判断——三格已相等就不写。好处是重复执行的结果稳定，失败重跑不会反复扰动文件。

### 6. 入口分流靠 `isDirectory()`，两种入口收束到同一个处理函数

```
basePath
  ├─ isDirectory() -> 递归收集 jpg/jpeg/png（应用 exclude 列表）
  └─ isFile()      -> 只取这一个文件，不筛扩展名
            │
            └──> 处理单个文件：readAttributes -> 算目标值 -> 判断 -> setTimes
```

分叉只发生在"**选哪些文件**"，写时间的那段逻辑两种入口完全共用。这样"单文件测试"验证的就是真实路径，不是另一套代码。

**替代方案**：单文件入口也走扩展名筛选。放弃，因为会让"随便拿个 txt 试一下"失效，而单文件是显式点名的目标，意图已经足够明确。

### 7. 时间字符串用 `DateTimeFormatter` + `LocalDateTime`，不用 `SimpleDateFormat`

`SimpleDateFormat` 默认宽容解析，`2020-13-45` 这类输入会被"顺延"成合法日期而不报错；本项目 `DateUtils` 内部也用了它。新代码用严格模式的 `DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")`，解析失败直接抛错。时区取系统默认时区（与 `DateUtils` 行为一致）。

### 8. 试运行默认开启

写时间戳是**不可见**的破坏性操作：改文件名你能看见文件名变了，改时间你不打开"详细信息"或右键属性根本看不出来。因此默认 `dryRun = true`，第一次执行永远只打印 `旧值 -> 新值`。日志中显式标注 `DRY-RUN` 字样，避免"跑成功了怎么没效果"的误解。

### 9. 单个文件失败只记录，不中断整批

`setTimes` 可能因文件被占用或只读而抛 `IOException`。捕获后记录路径与原因，继续下一个文件，结束时输出 `成功 N / 跳过 M / 失败 K`。对个人批处理工具来说，"处理完 99 个"比"因为第 3 个失败而全部中止"更有用。

### 10. 复用目录/排除目录的思路，但不修改既有类

`leftFileRightFolder` 在 `RenameFileByNumberService` 里，职责是重命名，不属于时间工具。新 Service 自带同样形态的实现，避免把两个不相干的职责耦合进同一个类，也避免动到已经在用的重命名逻辑。

## Risks / Trade-offs

- **[之后任何一次复制都会把创建时间重置成"复制那一刻"]** → 缓解：明确把本工具定位为流水线的最后一步；移动与重命名不影响时间戳。这一点写进测试类的注释。
- **[原值不可恢复]** → 缓解：试运行默认开启，落盘前必然有一次可读的"旧值 -> 新值"记录。
- **[非 NTFS 盘的时间精度不足]** → 缓解：仅声明在 NTFS 上验证过；若目标是 exFAT/FAT32，跨盘复制后行为不保证，属于已知限制。
- **[单文件入口不筛扩展名，可能误改非图片文件]** → 缓解：试运行默认开启，且日志打印完整路径；单文件入口本身就是为了"显式指定一个目标"。
- **[递归扫描可能命中远超预期的大量文件]** → 缓解：先跑试运行看条数；`excludeFolderList` 可剪枝。
- **[时区变化会改变指定模式的解释结果]** → 接受。与 `DateUtils` 保持一致，都用本地时区；个人使用场景下不引入时区参数。
