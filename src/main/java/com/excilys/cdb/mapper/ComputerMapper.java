package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;

/**
 * Static class which convert a {@link ResultSet} into an object
 * {@link Computer}.
 * 
 * @author samy
 *
 */
public class ComputerMapper {

	private static CompanyService companyService = CompanyService.INSTANCE;

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

	/**
	 * Convert a {@link ResultSet} into an object {@link Computer}.
	 * 
	 * @param resultSet {@link ResultSet}
	 * @return {@link Computer}
	 * @throws SQLException if the columnLabel is not valid; if a database access
	 *                      error occurs or this method is called on a closed result
	 *                      set
	 */
	public static Computer getComputer(Optional<String> computerName, Optional<String> strDateIntroduced,
			Optional<String> strDateDiscontinued, Optional<String> strCompanyId) throws InvalidComputerException, InvalidDateException {
		return ComputerMapper.getComputer(Optional.of("-1"), computerName, strDateIntroduced, strDateDiscontinued, strCompanyId);
	}
	
	public static Computer getComputer(Optional<String> computerId, Optional<String> computerName, Optional<String> strDateIntroduced,
			Optional<String> strDateDiscontinued, Optional<String> strCompanyId) throws InvalidComputerException, InvalidDateException {
		Long id = -1L;
		if (computerName.isPresent() && !"".equals(computerName.get())) {
			id = Long.valueOf(computerId.get());
		}

		if (!computerName.isPresent() || "".equals(computerName.get())) {
			throw new InvalidComputerException("Computer name value should not be empty.");
		}

		Optional<LocalDate> dateIntroduced = DateMapper.getLocalDate(strDateIntroduced, "introduced");
		Optional<LocalDate> dateDiscontinued = DateMapper.getLocalDate(strDateDiscontinued, "discontinued");
		if (	dateIntroduced.isPresent()
			&&	dateDiscontinued.isPresent()
			&&	dateIntroduced.get().isAfter(dateDiscontinued.get())) {
			throw new InvalidComputerException("The Introduced date value ('" + dateIntroduced.get()
					+ "') should be before the Discontinued date value ('" + dateDiscontinued.get() + "').");
		}
		
		Optional<Company> company = Optional.empty();
		if (strCompanyId.isPresent() && !"".equals(strCompanyId.get())) {
			try {
				company = companyService.getCompanyFromId(Long.valueOf(strCompanyId.get()));
				if (!company.isPresent()) {
					throw new InvalidComputerException("No company found with the companyId : '" + strCompanyId.get() + "'");
				}
			} catch (NumberFormatException numberFormatException) {
				throw new InvalidComputerException("No company found, companyId value ('" + strCompanyId.get() + "') is not a number)");
			}
		}

		return new Computer(id, computerName.get(), dateIntroduced, dateDiscontinued, company);
	}
}
