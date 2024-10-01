package cn.lucifer.demo.file.num;

import cn.lucifer.util.StrUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.File;
import java.util.Map;

public class AnalysisFileContext {

	private final File[] allFiles;

	private final ImmutableMap<String, File> fileName2FileMap;

	private final ArrayListMultimap<String, String> fileName2NumberArgsMap = ArrayListMultimap.create();

	/**
	 * key=FileFormatDto
	 */
	private final ArrayListMultimap<FileFormatDto, String> dto2fileNameMap = ArrayListMultimap.create();

	/**
	 * key=fileFormat
	 */
	private final Map<String, FileFormatDto> fileFormatMap = Maps.newHashMap();

	public AnalysisFileContext(File[] allFiles) {
		this.allFiles = allFiles;
		Map<String, File> fnMap = Maps.newHashMapWithExpectedSize(allFiles.length);
		for (File f : allFiles) {
			fnMap.put(f.getName(), f);
		}
		fileName2FileMap = ImmutableMap.<String, File>builder().putAll(fnMap).build();
	}

	public void fillDto2fileNameMap(String fileName, String fileFormat) {
		FileFormatDto dto = getAndGenerateFileFormatDto(fileFormat);
		dto2fileNameMap.put(dto, fileName);
	}

	public FileFormatDto getAndGenerateFileFormatDto(String fileFormat) {
		FileFormatDto dto = fileFormatMap.get(fileFormat);
		if (null == dto) {
			dto = new FileFormatDto(fileFormat);
			fileFormatMap.put(fileFormat, dto);
		}
		return dto;
	}

	public void resetMaxArgsCount(String fileFormat, int argsCount) {
		FileFormatDto dto = getAndGenerateFileFormatDto(fileFormat);
		int maxArgsCount = Math.max(dto.getMaxArgsCount(), argsCount);
		dto.setMaxArgsCount(maxArgsCount);
	}

	public File[] getAllFiles() {
		return allFiles;
	}

	public ArrayListMultimap<String, String> getFileName2NumberArgsMap() {
		return fileName2NumberArgsMap;
	}

	public ArrayListMultimap<FileFormatDto, String> getDto2fileNameMap() {
		return dto2fileNameMap;
	}

	public Map<String, FileFormatDto> getFileFormatMap() {
		return fileFormatMap;
	}

	public ImmutableMap<String, File> getFileName2FileMap() {
		return fileName2FileMap;
	}
}
