package com.excilys.cdb.mapper;

public class InvalidDateException extends MapperException {
	private static final long serialVersionUID = 7792513824999192264L;

	public InvalidDateException() {
		super("label.invalidDateFormat");
	}
}
