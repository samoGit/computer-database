package com.excilys.cdb.mapper;

public class InvalidComputerException extends MapperException {
	private static final long serialVersionUID = 7332433076596761154L;

	public InvalidComputerException(String messageKey) {
		super(messageKey);
	}
}
