package com.excilys.cdb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.builder.ComputerBuilder;
import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * @author samy
 *
 */
public class ComputerServiceTest {

	private static ComputerService computerService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		computerService = ComputerService.INSTANCE;
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
		expectedComputerList.add(
			ComputerBuilder.newComputerBuilder().withId(22L).withName("Macintosh II").buildComputer()
		);
		expectedComputerList.add(ComputerBuilder.newComputerBuilder()
												.withId(23L)
												.withName("Macintosh Plus")
												.withDateIntroduced(Optional.of(
														LocalDate.parse("16/01/1986", 
														DateTimeFormatter.ofPattern("dd/MM/yyyy")))
													)
												.withDateDiscontinued(Optional.of(
														LocalDate.parse("15/10/1990", 
														DateTimeFormatter.ofPattern("dd/MM/yyyy")))
													)
												.withCompany(Optional.ofNullable(
														new Company(Long.valueOf(1), "Apple Inc."))
													)
												.buildComputer()
			);

		List<Computer> actualComputerList = computerService.getListComputers(10L, 2L, Optional.empty());

		for (int i = 0; i < expectedComputerList.size(); i++) {
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
		List<Computer> actualComputerList = computerService.getListComputersByName(0L, 10L, "HP Mini 1000", Optional.empty());

		assertEquals(expectedComputerList, actualComputerList);
	}

	/**
	 * Test method for
	 * {@link com.excilys.cdb.service.ComputerService#CreateNewComputer(java.lang.String, java.util.Optional, java.util.Optional, java.util.Optional)}.
	 */
	@Test
	public void testCreateNewComputer() {
		computerService.deleteComputer("testCreateNewComputer");
		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName(0L, 10L, "testCreateNewComputer", Optional.empty());
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

		List<Computer> computerListFound = computerService.getListComputersByName(0L, 10L, "testCreateNewComputer", Optional.empty());
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

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName(0L, 10L, "testCreateNewComputer", Optional.empty());
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		computerService.deleteComputer(computerListShouldNotBeEmpty.get(0).getId().toString());

		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName(0L, 10L, "testCreateNewComputer", Optional.empty());
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

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName(0L, 10L, "testCreateNewComputer", Optional.empty());
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		Computer computerToBeUpdate = computerListShouldNotBeEmpty.get(0);
		computerToBeUpdate.setName("testCreateNewComputer_RENAMED");
		computerService.updateComputer(computerToBeUpdate, "name");

		List<Computer> computerListAfterRename = computerService
				.getListComputersByName(0L, 10L, "testCreateNewComputer_RENAMED", Optional.empty());
		assertFalse(computerListAfterRename.isEmpty());

		Computer computerAfterRename = computerListAfterRename.get(0);
		assertEquals(computerToBeUpdate, computerAfterRename);

		computerService.deleteComputer(computerAfterRename.getId().toString());
	}
}
