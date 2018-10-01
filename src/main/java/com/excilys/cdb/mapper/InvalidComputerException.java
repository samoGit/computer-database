package com.excilys.cdb.mapper;

public class InvalidComputerException extends Exception {
	private static final long serialVersionUID = 7332433076596761154L;
	private String message;

	public InvalidComputerException() {
		message = "";
	}
	public InvalidComputerException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
