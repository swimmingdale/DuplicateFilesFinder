package com.swimmingdale.dff.executor;

import java.io.File;
import java.io.IOException;

public class Executor {
	public Executor() {

	}

	public static boolean deleteFile(File file) throws IOException {
		return file.delete();
	}

	public static long getFileSize(File file) {
		long size = file.length();
		return size;
	}
}
