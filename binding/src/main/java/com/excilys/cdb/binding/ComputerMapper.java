package com.excilys.cdb.binding;

import java.sql.ResultSet;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerBuilder;
import com.excilys.cdb.model.ComputerDto;
import com.excilys.cdb.model.DateMapper;
import com.excilys.cdb.model.InvalidComputerException;
import com.excilys.cdb.model.InvalidDateException;

/**
 * Static class which convert a {@link ResultSet} into an object
 * {@link Computer}.
 * 
 * @author samy
 *
 */
@Component
public class ComputerMapper {

	public Computer getComputerWithNoCompany(ComputerDto computerDto)
			throws InvalidComputerException, InvalidDateException {
		Long id = null;
		if (!"".equals(computerDto.getId())) {
			id = Long.valueOf(computerDto.getId());
		}
		if ("".equals(computerDto.getName())) {
			throw new InvalidComputerException("label.invalidComputer.EmpyName");
		}

		LocalDate dateIntroduced = DateMapper.getLocalDate(computerDto.getDateIntroduced(), "introduced");
		LocalDate dateDiscontinued = DateMapper.getLocalDate(computerDto.getDateDiscontinued(), "discontinued");
		if (dateIntroduced != null && dateDiscontinued != null && dateIntroduced.isAfter(dateDiscontinued)) {
			throw new InvalidComputerException("label.invalidComputer.IntroducedAfterDiscontinued");
		}

		return ComputerBuilder.newComputerBuilder().withId(id).withName(computerDto.getName())
				.withDateIntroduced(dateIntroduced).withDateDiscontinued(dateDiscontinued).buildComputer();
	}
}
