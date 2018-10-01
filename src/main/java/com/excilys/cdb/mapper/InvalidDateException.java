package com.excilys.cdb.mapper;

public class InvalidDateException extends Exception {
	private static final long serialVersionUID = 7792513824999192264L;

	private String message;

	public InvalidDateException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
