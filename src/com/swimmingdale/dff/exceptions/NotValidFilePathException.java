package com.swimmingdale.dff.exceptions;

public class NotValidFilePathException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -599881241022274355L;
	private String path;

	public NotValidFilePathException(String path) {
		this.path = path;
	}
	
	public String getMessage() {
		return "The file '" + path + "' is not a valid file path";
	}
}