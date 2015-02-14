package com.dff.runner;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.dff.core.FilesFinder;
import com.dff.core.FilesFinderFactory;

public class TestRunner {
	public static void main(String... args) {
		String rootPath = "D:/dev/file app testing";
		FilesFinderFactory ffFactory = new FilesFinderFactory();
		FilesFinder ff = ffFactory.createFilesFinder(rootPath);
		if (ff != null) {
			Map<Long, List<File>> allFilesMap = ff.getFileSizeMap();
			System.out.println(allFilesMap.toString());
			Map<File, List<String>> allFilesTagsMap = ff.getAllFilesTagsMap();
			System.out.println(allFilesTagsMap.toString());
		}
	}
}
