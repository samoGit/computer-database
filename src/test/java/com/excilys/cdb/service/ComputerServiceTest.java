package com.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.builder.ComputerBuilder;
import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.persistence.ConnectionManager;

/**
 * @author samy
 *
 */
public class ComputerServiceTest {

	private static ComputerService computerService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		computerService = ComputerService.INSTANCE;
		ConnectionManager.INSTANCE.activateTestMode();
	}

	@AfterClass
	public static void setUpAfterClass() throws Exception {
		ConnectionManager.INSTANCE.deactivateTestMode();
	}

	/**
	 * java.lang.AssertionError: expected: <Computer [id=23, name=Macintosh Plus,
	 * dateIntroduced=Optional[1986-05-16], dateDiscontinued=Optional[1990-10-15],
	 * company=Optional[Company [id=1, name=Apple Inc.]]]> but was: <Computer
	 * [id=23, name=Macintosh Plus, dateIntroduced=Optional[1986-01-16],
	 * dateDiscontinued=Optional[1990-10-15], company=Optional[Company [id=1,
	 * name=Apple Inc.]]]>
	 * 
	 * 
	 * 
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#getListComputers(java.lang.Long)}.
	 */
	@Test
	public void testGetListComputers() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		expectedComputerList.add(ComputerBuilder.newComputerBuilder()
				.withId(579L)
				.withName("0000")
				.withCompany(Optional.ofNullable(new Company(Long.valueOf(1), "Apple Inc.")))
				.buildComputer());
		expectedComputerList.add(ComputerBuilder.newComputerBuilder()
												.withId(580L)
												.withName("0001")
												.withDateIntroduced(Optional.of(
														LocalDate.parse("01/01/2001", 
														DateTimeFormatter.ofPattern("dd/MM/yyyy")))
													)
												.withDateDiscontinued(Optional.of(
														LocalDate.parse("02/02/2002", 
														DateTimeFormatter.ofPattern("dd/MM/yyyy")))
													)
												.withCompany(Optional.ofNullable(
														new Company(Long.valueOf(1), "Apple Inc."))
													)
												.buildComputer()
			);

		PageInfo pageInfo = new PageInfo(Optional.of("1"), Optional.of("2"), Optional.empty(), Optional.of("Name"));
		List<Computer> actualComputerList = computerService.getListComputers(pageInfo);

		for (int i = 0; i < 2; i++) {
			assertEquals(expectedComputerList.get(i), actualComputerList.get(i));
		}
	}

	/**
	 * [id=21, name=Macintosh II, dateIntroduced=Optional.empty,
	 * dateDiscontinued=Optional.empty, company=Optional.empty]> but was:<Computer
	 * [id=21, name=Macintosh, dateIntroduced=Optional[1984-01-24],
	 * dateDiscontinued=Optional.empty, company=Optional[Company [id=1, name=Apple
	 * Inc.]]]>
	 * 
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#getListComputersByName(java.lang.String)}.
	 */
	@Test
	public void testGetListComputersByName() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		Computer expectedComputer = ComputerBuilder.newComputerBuilder()
												   .withId(319L)
												   .withName("HP Mini 1000")
												   .withDateIntroduced(Optional.ofNullable(
														   LocalDate.parse("29/10/2008", 
														   DateTimeFormatter.ofPattern("dd/MM/yyyy"))
														))
												   .withCompany(Optional.ofNullable(
														   new Company(27L, "Hewlett-Packard")
														))
												   .buildComputer();

		expectedComputerList.add(expectedComputer);
		PageInfo pageInfo = new PageInfo(Optional.of("1"), Optional.of("10"), Optional.of("HP Mini 1000"), Optional.empty());
		List<Computer> actualComputerList = computerService.getListComputersByName(pageInfo);

		assertEquals(expectedComputerList, actualComputerList);
	}

	/**
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#CreateNewComputer(java.lang.String, java.util.Optional, java.util.Optional, java.util.Optional)}.
	 */
	@Test
	public void testCreateNewComputer() {
		computerService.deleteComputer("testCreateNewComputer");
		PageInfo pageInfo = new PageInfo(Optional.of("1"), Optional.of("10"), Optional.of("testCreateNewComputer"), Optional.empty());
		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName(pageInfo);
		assertTrue(computerListShouldBeEmpty.isEmpty());

		Optional<String> nameNewPC = Optional.ofNullable("testCreateNewComputer");
		Optional<String> dateIntoducedNewPC = Optional.ofNullable("01/02/2003");
		Optional<String> dateDiscontinuedNewPC = Optional.ofNullable("04/05/2006");

		ComputerDto computerDto = new  ComputerDto();
		computerDto.setName(nameNewPC);
		computerDto.setDateIntroduced(dateIntoducedNewPC);
		computerDto.setDateDiscontinued(dateDiscontinuedNewPC);
		
		try {
			computerService.createNewComputer(ComputerMapper.getComputer(computerDto));
		}
		catch (InvalidComputerException | InvalidDateException e) {
			assertTrue(e.getMessage(), false);
		}

		PageInfo pageInfo2 = new PageInfo(Optional.of("1"), Optional.of("10"), Optional.of("testCreateNewComputer"), Optional.empty());
		List<Computer> computerListFound = computerService.getListComputersByName(pageInfo2);
		assertFalse(computerListFound.isEmpty());

		Computer computerFound = computerListFound.get(0);
		assertEquals(computerFound.getName(), nameNewPC.get());
		assertEquals(computerFound.getDateIntroduced(), Optional
				.ofNullable(LocalDate.parse(dateIntoducedNewPC.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		assertEquals(computerFound.getDateDiscontinued(), Optional
				.ofNullable(LocalDate.parse(dateDiscontinuedNewPC.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

		computerService.deleteComputer(computerFound.getId().toString());
	}

	/**
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#deleteComputer(com.excilys.cdb.model.Computer)}.
	 */
	@Test
	public void testDeleteComputer() {
		ComputerDto computerDto = new  ComputerDto();
		computerDto.setName(Optional.of("testCreateNewComputer"));
		computerDto.setDateIntroduced(Optional.of("01/02/2003"));
		computerDto.setDateDiscontinued(Optional.of("04/05/2006"));
		
		try {
			computerService.createNewComputer(ComputerMapper.getComputer(computerDto));
		}
		catch (InvalidComputerException | InvalidDateException e) {
			assertTrue(e.getMessage(), false);
		}

		PageInfo pageInfo = new PageInfo(Optional.of("1"), Optional.of("10"), Optional.of("testCreateNewComputer"), Optional.empty());
		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName(pageInfo);
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		computerService.deleteComputer(computerListShouldNotBeEmpty.get(0).getId().toString());

		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName(pageInfo);
		assertTrue(computerListShouldBeEmpty.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#updateComputer(com.excilys.cdb.model.Computer, java.lang.String)}.
	 */
	@Test
	public void testUpdateComputer() {
		ComputerDto computerDto = new  ComputerDto();
		computerDto.setName(Optional.of("testCreateNewComputer"));
		computerDto.setDateIntroduced(Optional.of("01/02/2003"));
		computerDto.setDateDiscontinued(Optional.of("04/05/2006"));
		
		try {
			computerService.createNewComputer(ComputerMapper.getComputer(computerDto));
		}
		catch (InvalidComputerException | InvalidDateException e) {
			assertTrue(e.getMessage(), false);
		}

		PageInfo pageInfo = new PageInfo(Optional.of("1"), Optional.of("10"), Optional.of("testCreateNewComputer"), Optional.empty());
		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName(pageInfo);
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		Computer computerToBeUpdate = computerListShouldNotBeEmpty.get(0);
		computerToBeUpdate.setName("testCreateNewComputer_RENAMED");
		computerService.updateComputer(computerToBeUpdate, "name");

		List<Computer> computerListAfterRename = computerService.getListComputersByName(pageInfo);
		assertFalse(computerListAfterRename.isEmpty());

		Computer computerAfterRename = computerListAfterRename.get(0);
		assertEquals(computerToBeUpdate, computerAfterRename);

		computerService.deleteComputer(computerAfterRename.getId().toString());
	}
}
