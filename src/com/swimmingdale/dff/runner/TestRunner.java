package com.swimmingdale.dff.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.swimmingdale.dff.core.FilesFinder;
import com.swimmingdale.dff.core.FilesFinderFactory;

public class TestRunner {
	public static void main(String... args) {
		String rootPath = "D:/Music";
		FilesFinderFactory ffFactory = new FilesFinderFactory();
		FilesFinder ff = ffFactory.createFilesFinder(rootPath);
		if (ff != null) {
			Map<Long, List<File>> allFilesMap = ff.getFileSizeMap();
			for(Long key : allFilesMap.keySet()) {
				if(allFilesMap.get(key).size() > 1) {
					System.out.println(allFilesMap.get(key).toString());
				}
			}
			System.out.println("Total number of files: " + ff.getAllFilesNumber());
			ArrayList<ArrayList<File>> commonFilesInTuples = ff.getCommonFilesInTuples();
			System.out.println(commonFilesInTuples);
		}
	}
}
