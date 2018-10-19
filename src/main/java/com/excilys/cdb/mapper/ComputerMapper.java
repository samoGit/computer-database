package com.excilys.cdb.mapper;

import java.sql.ResultSet;
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
