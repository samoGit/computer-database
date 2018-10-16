package com.excilys.cdb.mapper;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.excilys.cdb.mapper.InvalidDateException;

public class DateMapper {

	/**
	 * Get a {@link LocalDate} from a string, if the given string is empty it doesn't
	 * throw InvalidDateException but return an empty Optional.
	 * 
	 * @param strDate
	 * @return Optional of LocalDate
	 * @throws InvalidDateException
	 */
	public static Optional<LocalDate> getLocalDate(String strDate, String dateFieldName) throws InvalidDateException {
		Optional<LocalDate> date = Optional.empty();
		try {
			if (!"".equals(strDate)) {
				date = Optional.ofNullable(LocalDate.parse(strDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			}
		} catch (DateTimeException e) {
			try {
				date = Optional.ofNullable(LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			} catch (DateTimeException dateTimeException) {
				throw new InvalidDateException("The date " + dateFieldName + " ('" + strDate + "') has an incorect format. The expected date format is dd/MM/yyyy");
			}
		}
		return date;
	}
}
