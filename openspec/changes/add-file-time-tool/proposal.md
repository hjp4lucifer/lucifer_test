# Proposal

## Why

图片经过下载、复制、解压之后，创建时间会被重置成"操作发生的那一刻"，而修改时间往往还保留着原始日期。于是同一个文件上的三个时间戳互相矛盾：Explorer 里按"创建日期"排序、按日期筛选相册，得到的都是无意义的结果。

现有代码里没有任何写时间戳的能力（全仓库没有 `setLastModified` / `FileTime` 的调用），手工用工具逐个改又不现实。需要一个能把三个时间戳对齐的小工具。

## What Changes

- 新增 `ChangeFileTimeService`：一次性写入单个文件的**创建时间、修改时间、访问时间**，三者写成同一个值。
- 两种取值模式，由 `normalizeToMin` 开关控制，**默认打开**：
  - **打开（对齐模式）**：读取该文件的创建时间与修改时间，取两者中**较小**的一个作为目标值，三格同写。适用于"还原照片真实日期"。
  - **关闭（指定模式）**：用调用方传入的 `yyyy-MM-dd HH:mm:ss` 字符串作为目标值，三格同写。
- `basePath` 支持两种入口，靠 `isDirectory()` 自动分流：
  - **目录**：递归处理目录下的 `jpg` / `jpeg` / `png`（扩展名大小写不敏感），并跳过 `excludeFolderList` 里的子目录。
  - **单文件**：只处理这一个文件，**不筛选扩展名**，用于先拿一个文件验证行为。
  - 路径不存在时直接报错，不静默跳过。
- 提供 `dryRun` 开关，**默认打开**：只打印 `旧值 -> 新值`，不落盘。
- 三格已经相等时直接跳过，不产生写入，保证幂等、可重复执行。
- 单个文件写入失败（被占用、只读）记录日志后继续，结束时汇总 `成功 / 跳过 / 失败` 三个计数。

## Capabilities

### New Capabilities

- `file-time`: 读取并统一文件的三个时间戳。包含"取最小值对齐"与"写入指定时间"两种取值模式，以及"目录批量"与"单文件"两种入口。

### Modified Capabilities

（无。项目当前没有任何 spec，本变更不修改既有行为。）

## Impact

- **新增代码**：`src/main/java/cn/lucifer/demo/file/time/ChangeFileTimeService.java`，以及承载参数的类。
- **新增测试**：`src/test/java/cn/lucifer/demo/file/time/ChangeFileTimeServiceTest.java`，含"指向目录"与"指向单个文件"两个用例。
- **复用而不修改**：`cn.lucifer.demo.file.num.RenameFileByNumberService#leftFileRightFolder` 的"文件/子目录分离 + 排除目录"思路；`cn.lucifer.util.DateUtils` 的时间解析能力。
- **依赖**：不新增第三方依赖，只用 JDK 21 的 `java.nio.file` 与 `java.time`。
- **影响范围**：与已有的重命名 demo 完全隔离，不改动任何现有类。仅在 Windows + NTFS 上验证过行为。
