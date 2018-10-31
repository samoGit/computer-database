package com.excilys.cdb.persistence;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.builder.ComputerBuilder;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

@Component
public class ComputerRowMapper implements RowMapper<Computer>{

	@Override
	public Computer mapRow(ResultSet resultSet, int rowNum) throws SQLException {
		Long id = resultSet.getLong("computer.id");
		String name = resultSet.getString("computer.name");

		Date dateIntroduced = resultSet.getDate("computer.introduced");
		LocalDate localDateIntroduced = null;
		if (dateIntroduced != null) {
			localDateIntroduced = dateIntroduced.toLocalDate();
		}

		Date dateDiscontinued = resultSet.getDate("computer.discontinued");
		LocalDate localDateDiscontinued = null;
		if (dateDiscontinued != null) {
			localDateDiscontinued = dateDiscontinued.toLocalDate();
		}

		Long companyId = resultSet.getLong("company.id");
		String companyName = resultSet.getString("company.name");
		Company company = null;
		if (companyId != null && companyName != null)
			company = new Company(companyId, companyName);

		return ComputerBuilder.newComputerBuilder()
							  .withId(id)
							  .withName(name)
							  .withDateIntroduced(localDateIntroduced)
							  .withDateDiscontinued(localDateDiscontinued)
							  .withCompany(company)
							  .buildComputer();
	}
}
