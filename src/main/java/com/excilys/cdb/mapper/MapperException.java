package com.excilys.cdb.mapper;

public class MapperException extends Exception {
	private static final long serialVersionUID = 5018064759964586256L;

	private String messageKey;

	protected MapperException(String messageKey) {
		this.messageKey = messageKey;
	}	
	public String getMessageKey() {
		return messageKey;
	}
}
