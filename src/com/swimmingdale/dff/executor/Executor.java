package com.swimmingdale.dff.executor;

import java.io.File;

public class Executor {
	public Executor() {
		
	}

	public static boolean deleteFile(File file) {
		return file.delete();
	}
	
	public static long getFileSize(File file) {
		long size = file.length();
		return size;
	}
}
