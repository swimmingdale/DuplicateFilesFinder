package com.dff.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FilesFinder {
	private Map<Long, List<File>> allFilesSizeMap;
	private Map<File, List<String>> allFilesTagsMap;
	private List<File> allFiles;
	private String rootFilePath;
	private File rootFile;

	public FilesFinder(String rootFilePath) throws FileNotFoundException,
			NotDirectoryException {
		this.rootFilePath = rootFilePath;
		rootFile = new File(rootFilePath);
		if (!rootFile.exists()) {
			throw new FileNotFoundException();
		}
		if (!rootFile.isDirectory()) {
			throw new NotDirectoryException(rootFilePath);
		}

	}

	private List<File> getAllFilesInDirectoryAndSubdirectories() {
		if (allFiles == null) {
			allFiles = collectAllFiles(rootFile);
		}
		return allFiles;
	}

	private List<File> collectAllFiles(File rootDir) {
		List<File> allFiles = new ArrayList<File>();
		File[] subFiles = rootDir.listFiles();
		for (File file : subFiles) {
			if (file.isDirectory()) {
				allFiles.addAll(collectAllFiles(file));
			} else {
				allFiles.add(file);
			}
		}
		return allFiles;
	}

	public Map<Long, List<File>> getFileSizeMap() {
		if (allFilesSizeMap != null) {
			return allFilesSizeMap;
		}
		allFilesSizeMap = new HashMap<Long, List<File>>();
		List<File> allFiles = getAllFilesInDirectoryAndSubdirectories();
		for (File file : allFiles) {
			long fileSize = file.length();
			List<File> valueFiles;
			if ((valueFiles = allFilesSizeMap.get(fileSize)) == null) {
				valueFiles = new ArrayList<File>();
				valueFiles.add(file);
				allFilesSizeMap.put(fileSize, valueFiles);
			} else {
				valueFiles.add(file);
				allFilesSizeMap.put(fileSize, valueFiles);
			}
		}
		return allFilesSizeMap;
	}

	public Map<File, List<String>> getAllFilesTagsMap() {
		if (allFilesTagsMap != null) {
			return allFilesTagsMap;
		}
		allFilesTagsMap = new HashMap<File, List<String>>();
		List<File> allFiles = getAllFilesInDirectoryAndSubdirectories();
		for (File file : allFiles) {
			try {
				int lastDotIndex = file.getName().lastIndexOf(".");
				String fileNameOnly = file.getName().substring(0, lastDotIndex);
				String removedNonAlphanumeric = fileNameOnly.replaceAll("\\W+",
						" ");
				removedNonAlphanumeric = removedNonAlphanumeric.replaceAll("_",
						" ");
				removedNonAlphanumeric = removedNonAlphanumeric.replaceAll("-",
						" ");
				removedNonAlphanumeric = removedNonAlphanumeric.toLowerCase();
				String[] tagsArray = removedNonAlphanumeric.split("\\s+");
				List<String> tagsList = Arrays.asList(tagsArray);
				allFilesTagsMap.put(file, tagsList);
			} catch (Exception e) {
				AlertComponent.alertError("File does not have extension");
				continue;
			}
		}
		return allFilesTagsMap;
	}

	public ArrayList<ArrayList<File>> getCommonFilesInTuples() {
		ArrayList<ArrayList<File>> commonFilesInTuples = new ArrayList<ArrayList<File>>();
		Map<File, List<String>> allFilesTagsMap = getAllFilesTagsMap();
		Set<File> keySet = allFilesTagsMap.keySet();
		for (File key : keySet) {
			List<String> value = allFilesTagsMap.get(key);
			if (value != null) {
				ArrayList<File> similarFilesTuple = null;
				for (File key2 : keySet) {
					boolean thisFileWasSimilar = false;
					List<String> value2 = allFilesTagsMap.get(key2);
					if (key != key2 && value2 != null) {
						if (haveSimilarTags(value, value2)) {
							if (similarFilesTuple == null) {
								similarFilesTuple = new ArrayList<File>();
							} else {
								thisFileWasSimilar = true;
								similarFilesTuple.add(key);
								similarFilesTuple.add(key2);
							}
						}
					}
					if (thisFileWasSimilar) {
						allFilesTagsMap.remove(key2);
						commonFilesInTuples.add(similarFilesTuple);
					}
				}
			}
		}
		return commonFilesInTuples;
	}

	private boolean haveSimilarTags(List<String> first, List<String> second) {
		int similar = 0;

		if (similar >= 2) {
			return true;
		} else {
			return false;
		}
	}
}
