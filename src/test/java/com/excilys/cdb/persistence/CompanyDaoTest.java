package com.excilys.cdb.persistence;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.excilys.cdb.builder.ComputerBuilder;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ConnectionManager;

public class CompanyDaoTest {

	private ConnectionManager connectionManager = ConnectionManager.INSTANCE;
	private CompanyDao companyDao = CompanyDao.INSTANCE;

	Connection connection = null;

	@Before
	public void setUp() throws Exception {
		System.out.println("Connecting to database...");
		connectionManager.activateTestMode();
		connection = connectionManager.getConnection();
		System.out.println("Connected to database successfully.");
	}

	@After
	public void tearDown() throws Exception {
		connection.close();
		connectionManager.deactivateTestMode();
	}

	@Test
	public void testGetListCompanies() {
		List<Company> expectedListCompaniesFound = new ArrayList<>();
		expectedListCompaniesFound.add(new Company(1L, "Apple Inc."));
		expectedListCompaniesFound.add(new Company(2L, "Thinking Machines"));

		List<Company> actualListCompaniesFound = companyDao.getListCompanies();
		assertEquals(expectedListCompaniesFound, actualListCompaniesFound);
	}

	@Test
	public void testGetCompanyFromId() {
		Optional<Company> expectedCompanyFound = Optional.of(new Company(2L, "Thinking Machines"));
		Optional<Company> actualCompanyFound = companyDao.getCompanyFromId(2L);
		assertEquals(expectedCompanyFound, actualCompanyFound);

		expectedCompanyFound = Optional.empty();
		actualCompanyFound = companyDao.getCompanyFromId(3L);
		assertEquals(expectedCompanyFound, actualCompanyFound);
	}

	@Test
	public void testDeleteCompany() {
		ArrayList<Company> expectedListCompaniesFound = new ArrayList<>();
		expectedListCompaniesFound.add(new Company(1L, "Apple Inc."));
		expectedListCompaniesFound.add(new Company(2L, "Thinking Machines"));
		
		ArrayList<Computer> expectedListComputersFound = new ArrayList<>();
		expectedListComputersFound.add(ComputerBuilder.newComputerBuilder().withId(1L).withName("MacBook1")
			.withCompany(Optional.of(new Company(1L, "Apple Inc."))).buildComputer());
		expectedListComputersFound.add(ComputerBuilder.newComputerBuilder().withId(2L).withName("MacBook2")
			.withCompany(Optional.of(new Company(1L, "Apple Inc.")))
			.withDateIntroduced(Optional.of(LocalDate.parse("01/05/1980", DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
		  .withDateDiscontinued(Optional.of(LocalDate.parse("01/04/1984", DateTimeFormatter.ofPattern("dd/MM/yyyy"))))
			.buildComputer());
		expectedListComputersFound.add(ComputerBuilder.newComputerBuilder().withId(3L).withName("TM")
			.withCompany(Optional.of(new Company(2L, "Thinking Machines"))).buildComputer());

		ArrayList<Company> actualListCompaniesFound_BeforeDelete = new ArrayList<>();
		ArrayList<Company> actualListCompaniesFound_AfterDelete = new ArrayList<>();
		ArrayList<Computer> actualListComputersFound_BeforeDelete = new ArrayList<>();
		ArrayList<Computer> actualListComputersFound_AfterDelete = new ArrayList<>();
		try {
			ResultSet resultSet = connection.prepareStatement("SELECT id, name FROM company;").executeQuery();
			while (resultSet.next()) {
				actualListCompaniesFound_BeforeDelete
						.add(new Company(resultSet.getLong("id"), resultSet.getString("name")));
			}
			resultSet = connection.prepareStatement("SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id;").executeQuery();
			while (resultSet.next()) {
				actualListComputersFound_BeforeDelete.add(ComputerMapper.getComputer(resultSet));
			}

			companyDao.deleteCompany(1L);
			resultSet = connection.prepareStatement("SELECT id, name FROM company;").executeQuery();
			while (resultSet.next()) {
				actualListCompaniesFound_AfterDelete
						.add(new Company(resultSet.getLong("id"), resultSet.getString("name")));
			}
			resultSet = connection.prepareStatement("SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id = company.id;").executeQuery();
			while (resultSet.next()) {
				actualListComputersFound_AfterDelete.add(ComputerMapper.getComputer(resultSet));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertEquals(expectedListCompaniesFound, actualListCompaniesFound_BeforeDelete);
		assertEquals(expectedListComputersFound, actualListComputersFound_BeforeDelete);
		expectedListCompaniesFound.remove(0);
		expectedListComputersFound.remove(0);
		expectedListComputersFound.remove(0);
		assertEquals(expectedListCompaniesFound, actualListCompaniesFound_AfterDelete);
		assertEquals(expectedListComputersFound, actualListComputersFound_AfterDelete);
	}
}
