package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * Static class which convert a {@link ResultSet} into an object
 * {@link Computer}.
 * 
 * @author samy
 *
 */
public class ComputerMapper {

	/**
	 * This class only provides static methods and should not be instantiated
	 */
	private ComputerMapper() {
	}

	/**
	 * Convert a {@link ResultSet} into an object {@link Computer}.
	 * 
	 * @param resultSet {@link ResultSet}
	 * @return {@link Computer}
	 * @throws SQLException if the columnLabel is not valid; if a database access
	 *                      error occurs or this method is called on a closed result
	 *                      set
	 */
	public static Computer getComputer(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("computer.id");
		String name = resultSet.getString("computer.name");

		Date dateIntroduced = resultSet.getDate("computer.introduced");
		LocalDate localDateintroduced = dateIntroduced != null ? dateIntroduced.toLocalDate() : null;

		Date dateDiscontinued = resultSet.getDate("computer.discontinued");
		LocalDate localDateDiscontinued = dateDiscontinued != null ? dateDiscontinued.toLocalDate() : null;

		Long companyId = resultSet.getLong("company.id");
		String companyName = resultSet.getString("company.name");
		Optional<Company> company = Optional.empty();
		if (companyId != null && companyName != null)
			company = Optional.of(new Company(companyId, companyName));

		return new Computer(id, name, Optional.ofNullable(localDateintroduced),
				Optional.ofNullable(localDateDiscontinued), company);
	}

}
