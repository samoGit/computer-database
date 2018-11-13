package com.excilys.cdb.model;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateMapper {

	/**
	 * Get a {@link LocalDate} from a string, if the given string is empty it doesn't
	 * throw InvalidDateException but return an empty Optional.
	 * 
	 * @param strDate
	 * @return Optional of LocalDate
	 * @throws InvalidDateException
	 */
	public static LocalDate getLocalDate(String strDate, String dateFieldName) throws InvalidDateException {
		LocalDate date = null;
		try {
			if (!"".equals(strDate)) {
				date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			}
		} catch (DateTimeException e) {
			try {
				date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			} catch (DateTimeException dateTimeException) {
				throw new InvalidDateException();
			}
		}
		return date;
	}
}
