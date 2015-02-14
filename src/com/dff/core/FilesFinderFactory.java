package com.dff.core;

import java.io.FileNotFoundException;
import java.nio.file.NotDirectoryException;

public class FilesFinderFactory {
	FilesFinder finder;
	
	public FilesFinderFactory() {
		
	}
	
	public FilesFinder createFilesFinder(String rootPath) {
		if(rootPath != null && !(rootPath.replaceAll(" ", "").equals(""))) {
			try {
				finder = new FilesFinder(rootPath);
				return finder;
			} catch (NotDirectoryException e) {
				AlertComponent.alertError("Selected folder is not a directory: " + rootPath);
				return null;
			} catch (FileNotFoundException e) {
				AlertComponent.alertError("The selected folder was not found: " + rootPath);
				return null;
			}
		} else {
			AlertComponent.alertError("Not a valid file path!");
			return null;
		}
	}
	
}
