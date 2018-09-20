package com.excilys.cdb.ui;

import java.util.Optional;

public enum UserChoicePage {
	NEXT_PAGE(1, "Display the next page"),
	PREVIOUS_PAGE(2, "Display the previous page"),
	BACK_TO_MENU(3, "Go back to the main menu");
	
	private final Integer value;
	private final String message;
	
	private UserChoicePage(Integer value, String message) {
		this.value = value;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Integer getValue() {
		return value;
	}

	public static Optional<UserChoicePage> fromString(String stringValue) {
		for (UserChoicePage userChoicePage : UserChoicePage.values()) {
			if (userChoicePage.value.toString().equals(stringValue))
				return Optional.of(userChoicePage);
		}
		return Optional.empty();
	}
}
