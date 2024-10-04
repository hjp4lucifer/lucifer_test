package cn.lucifer.demo.file.num;

import cn.lucifer.util.StrUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RenameFileByNumberService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final String numberRegex = "\\d+";
	private final Pattern numberPattern = Pattern.compile(numberRegex);

	public Pair<File[], File[]> leftFileRightFolder(File folder) {
		File[] allFiles = folder.listFiles(file -> !file.isDirectory());
		File[] allDirectory = folder.listFiles(File::isDirectory);

		return Pair.of(allFiles, allDirectory);
	}

	public void analysisFormat(File[] allFiles) {
		validateAnalysisFormat(allFiles);

		AnalysisFileContext ctx = new AnalysisFileContext(allFiles);

		analysisFileFormat(ctx);
		analysisArgsIndex(ctx);

		ArrayListMultimap<FileFormatDto, String> dto2fileNameMap = ctx.getDto2fileNameMap();
		for (FileFormatDto dto : dto2fileNameMap.keySet()) {
			if (dto.getMaxNumLen() == dto.getMinNumLen()) {
				String msg = StrUtils.generateStr("folder={}的fileFormat={}, maxNumLen={}和minNumLen={}一致, 整目录无需rename! ",
						allFiles[0].getParentFile().getAbsolutePath(),
						dto.getFileFormat(), dto.getMaxNumLen(), dto.getMinNumLen());
				logger.info(msg);
				continue;
			}

			String fileNameNumFormat = StrUtils.generateStr("%0{}d", dto.getMaxNumLen());
			logger.info("fileNameNumFormat={}", fileNameNumFormat);
			List<String> fnList = dto2fileNameMap.get(dto);
			for (String fn : fnList) {
				File f = ctx.getFileName2FileMap().get(fn);

				// 分析提取数字参数
				Matcher matcher = numberPattern.matcher(fn);
				List<String> args = Lists.newArrayList();


				int j = -1;
				while (matcher.find()) {
					j++;
					if (dto.getArgIndex() != j) {
						args.add(matcher.group());
						continue;
					}

					String newNum = String.format(fileNameNumFormat, Integer.parseInt(matcher.group()));
					args.add(newNum);
				}

				if (CollectionUtils.isEmpty(args)) {
					throw new RuntimeException(StrUtils.generateStr(StrUtils.generateStr("fn={}的args为空", fn)));
				}
				String newFn = StrUtils.generateStr(dto.getFileFormat(), args.toArray());
				if (fn.equals(newFn)) {
					logger.debug("新旧fn名一样, 无需rename! oldFn={}, newFn={}", fn, newFn);
					continue;
				}
				logger.info(StrUtils.generateStr("oldFn={}, newFn={}, parent={}", fn, newFn, f.getParent()));
				f.renameTo(new File(f.getParentFile(), newFn));
			}
		}


	}

	private void analysisArgsIndex(AnalysisFileContext ctx) {

		ArrayListMultimap<String, String> fileName2NumberArgsMap = ctx.getFileName2NumberArgsMap();
		ArrayListMultimap<FileFormatDto, String> dto2fileNameMap = ctx.getDto2fileNameMap();
		for (FileFormatDto dto : dto2fileNameMap.keySet()) {
			int argIndex = 0;

			List<String> fileNameList = dto2fileNameMap.get(dto);
			if (1 == fileNameList.size()) {
				// 只有一个文件时的特殊处理
				dto.resetMaxMinNumLen(1);
				continue;
			}

			String fileParent = ctx.getFileName2FileMap().get(fileNameList.get(0)).getParent();
			for (; argIndex < dto.getMaxArgsCount(); argIndex++) {
				Set<String> numberSet = Sets.newHashSet();
				for (String fn : fileNameList) {
					String number = fileName2NumberArgsMap.get(fn).get(argIndex);
					numberSet.add(number);
				}

				if (numberSet.size() > 1) {
					logger.debug(StrUtils.generateStr("fn={}, fileFormat={}, index={}数字不同", fileParent, dto.getFileFormat(), argIndex));
					if (fileNameList.size() != numberSet.size()) {
						String msg = StrUtils.generateStr("fn={}数字个数需要和文件数量一致! fileNameList.size={}, numberSet.size()={}",
								fileParent, fileNameList.size(), numberSet.size());
						throw new RuntimeException(msg);
					}

					for (String n : numberSet) {
						int len = n.length();
						dto.resetMaxMinNumLen(len);
					}
					break;
				}
			}

			dto.validateMaxMinNumLen(fileParent);
			dto.setArgIndex(argIndex);
		}
	}

	private void analysisFileFormat(AnalysisFileContext ctx) {
		Set<String> fileFormatSet = Sets.newHashSet();
		ArrayListMultimap<String, String> fileName2NumberArgsMap = ctx.getFileName2NumberArgsMap();
		for (File f : ctx.getAllFiles()) {
			String fn = f.getName();
			String fileFormat = fn.replaceAll(numberRegex, "{}");

			ctx.fillDto2fileNameMap(fn, fileFormat);

			fileFormatSet.add(fileFormat);

			// 分析提取数字参数
			Matcher matcher = numberPattern.matcher(fn);
			int argCount = 0;
			while (matcher.find()) {
				fileName2NumberArgsMap.put(fn, matcher.group());
				argCount++;
			}
			ctx.resetMaxArgsCount(fileFormat, argCount);
		}
		logger.debug("fn={}, fileFormatSet={}", ctx.getAllFiles()[0].getParent(), JSON.toJSONString(fileFormatSet));
	}

	private void validateAnalysisFormat(File[] allFiles) {
		if (null == allFiles || 0 == allFiles.length) {
			throw new RuntimeException("allFiles不能为空");
		}
		for (File f : allFiles) {
			if (f.isDirectory()) {
				throw new RuntimeException("allFiles不能包含目录, f=" + f.getAbsolutePath());
			}
		}
	}


}
