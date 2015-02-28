package com.swimmingdale.dff.core;

public class Utils {
	public static String getMessageDeletedSize(long deletedBytes) {
		StringBuilder stringSize = new StringBuilder();
		if (deletedBytes == 0) {
			stringSize.append("No files were deleted");
			return stringSize.toString();
		}

		long kb = 1024;
		long mb = 1048576;
		long gb = 1073741824;

		stringSize.append("You saved up ");
		if (deletedBytes <= kb) {
			stringSize.append(deletedBytes);
			stringSize.append(" bytes");
		} else if (deletedBytes >= kb && deletedBytes < mb) {
			stringSize.append(deletedBytes / kb);
			stringSize.append(" kbs");
		} else if (deletedBytes >= mb && deletedBytes < gb) {
			stringSize.append(deletedBytes / mb);
			stringSize.append(" mb.");
		} else {
			stringSize.append(deletedBytes / gb);
			stringSize.append(" gbs");
		}
		stringSize.append(" from your hard drive");
		return stringSize.toString();
	}
}
