package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerBuilder;
import com.excilys.cdb.model.ComputerDto;
import com.excilys.cdb.model.DateMapper;
import com.excilys.cdb.model.InvalidComputerException;
import com.excilys.cdb.model.InvalidDateException;
import com.excilys.cdb.persistence.DataBaseAccessException;
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

	public Computer getComputer(ComputerDto computerDto) throws InvalidComputerException, InvalidDateException, DataBaseAccessException {
		Long id = null;
		if (!"".equals(computerDto.getId())) {
			id = Long.valueOf(computerDto.getId());
		}
		if ("".equals(computerDto.getName())) {
			throw new InvalidComputerException("label.invalidComputer.EmpyName");
		}

		LocalDate dateIntroduced = DateMapper.getLocalDate(computerDto.getDateIntroduced(), "introduced");
		LocalDate dateDiscontinued = DateMapper.getLocalDate(computerDto.getDateDiscontinued(), "discontinued");
		if (	dateIntroduced != null
			&&	dateDiscontinued != null
			&&	dateIntroduced.isAfter(dateDiscontinued)) {
			throw new InvalidComputerException("label.invalidComputer.IntroducedAfterDiscontinued");
		}
		
		Company company = null;
		if (!"".equals(computerDto.getCompanyId())) {
			try {
				company = companyService.getCompanyFromId(Long.valueOf(computerDto.getCompanyId()));
				if (company == null) {
					throw new InvalidComputerException("label.invalidComputer.noCompany");
				}
			} catch (NumberFormatException numberFormatException) {
				throw new InvalidComputerException("label.invalidComputer.noCompany");
			}
		}

		return ComputerBuilder.newComputerBuilder().withId(id).withName(computerDto.getName())
							  .withDateIntroduced(dateIntroduced).withDateDiscontinued(dateDiscontinued)
							  .withCompany(company).buildComputer();
	}
}
