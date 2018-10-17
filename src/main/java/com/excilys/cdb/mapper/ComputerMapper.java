package com.excilys.cdb.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.builder.ComputerBuilder;
import com.excilys.cdb.dto.ComputerDto;
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
@Component
public class ComputerMapper {

	@Autowired
	private CompanyService companyService;

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
	public Computer getComputer(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("computer.id");
		String name = resultSet.getString("computer.name");

		Date dateIntroduced = resultSet.getDate("computer.introduced");
		Optional<LocalDate> localDateIntroduced = Optional.empty();
		if (dateIntroduced != null) {
			localDateIntroduced = Optional.of(dateIntroduced.toLocalDate());
		}

		Date dateDiscontinued = resultSet.getDate("computer.discontinued");
		Optional<LocalDate> localDateDiscontinued = Optional.empty();
		if (dateDiscontinued != null) {
			localDateDiscontinued = Optional.of(dateDiscontinued.toLocalDate());
		}

		Long companyId = resultSet.getLong("company.id");
		String companyName = resultSet.getString("company.name");
		Optional<Company> company = Optional.empty();
		if (companyId != null && companyName != null)
			company = Optional.of(new Company(companyId, companyName));

		return ComputerBuilder.newComputerBuilder()
							  .withId(id)
							  .withName(name)
							  .withDateIntroduced(localDateIntroduced)
							  .withDateDiscontinued(localDateDiscontinued)
							  .withCompany(company)
							  .buildComputer();
	}

	public Computer getComputer(ComputerDto computerDto) throws InvalidComputerException, InvalidDateException {
		Long id = null;
		if (!"".equals(computerDto.getId())) {
			id = Long.valueOf(computerDto.getId());
		}
		if ("".equals(computerDto.getName())) {
			throw new InvalidComputerException("Computer name value should not be empty.");
		}

		Optional<LocalDate> dateIntroduced = DateMapper.getLocalDate(computerDto.getDateIntroduced(), "introduced");
		Optional<LocalDate> dateDiscontinued = DateMapper.getLocalDate(computerDto.getDateDiscontinued(), "discontinued");
		if (	dateIntroduced.isPresent()
			&&	dateDiscontinued.isPresent()
			&&	dateIntroduced.get().isAfter(dateDiscontinued.get())) {
			throw new InvalidComputerException("The Introduced date value ('" + dateIntroduced.get()
					+ "') should be before the Discontinued date value ('" + dateDiscontinued.get() + "').");
		}
		
		Optional<Company> company = Optional.empty();
		if (!"".equals(computerDto.getCompanyId())) {
			try {
				company = companyService.getCompanyFromId(Long.valueOf(computerDto.getCompanyId()));
				if (!company.isPresent()) {
					throw new InvalidComputerException("No company found with the companyId : '" + computerDto.getCompanyId() + "'");
				}
			} catch (NumberFormatException numberFormatException) {
				throw new InvalidComputerException("No company found, companyId value ('" + computerDto.getCompanyId() + "') is not a number)");
			}
		}

		return ComputerBuilder.newComputerBuilder().withId(id).withName(computerDto.getName())
							  .withDateIntroduced(dateIntroduced).withDateDiscontinued(dateDiscontinued)
							  .withCompany(company).buildComputer();
	}
}
