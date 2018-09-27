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

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

/**
 * @author samy
 *
 */
public class ComputerServiceTst {

	private static ComputerService computerService;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		computerService = ComputerService.INSTANCE;
	}

	/**java.lang.AssertionError: expected:	<Computer [id=23, name=Macintosh Plus, dateIntroduced=Optional[1986-05-16], dateDiscontinued=Optional[1990-10-15], company=Optional[Company [id=1, name=Apple Inc.]]]> but was:
	 * 										<Computer [id=23, name=Macintosh Plus, dateIntroduced=Optional[1986-01-16], dateDiscontinued=Optional[1990-10-15], company=Optional[Company [id=1, name=Apple Inc.]]]>



	 * Test method for {@link com.excilys.cdb.service.ComputerService#getListComputers(java.lang.Long)}.
	 */
	@Test
	public void testGetListComputers() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		expectedComputerList.add(new Computer(22L,
				"Macintosh II", 
				Optional.empty(), 
				Optional.empty(),
				Optional.empty() 
			));
		expectedComputerList.add(new Computer(23L, 
				"Macintosh Plus", 
				Optional.ofNullable(LocalDate.parse("16/01/1986", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
				Optional.ofNullable(LocalDate.parse("15/10/1990", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
				Optional.ofNullable(new Company(Long.valueOf(1), "Apple Inc."))
			));

		List<Computer> actualComputerList = computerService.getListComputers(10L, 2L);
		

		for (int i=0; i<expectedComputerList.size(); i++) {
			System.out.println("\nexpectedComputerList.get(" + i + ") = ");
			System.out.println(expectedComputerList.get(i));
			System.out.println("actualComputerList.get(" + i + ")");
			System.out.println(actualComputerList.get(i));

			assertEquals(expectedComputerList.get(i), actualComputerList.get(i));
		}
	}

	/**[id=21, name=Macintosh II, dateIntroduced=Optional.empty, dateDiscontinued=Optional.empty, company=Optional.empty]> but was:<Computer 
	 * [id=21, name=Macintosh, dateIntroduced=Optional[1984-01-24], dateDiscontinued=Optional.empty, company=Optional[Company [id=1, name=Apple Inc.]]]>

	 * Test method for {@link com.excilys.cdb.service.ComputerService#getListComputersByName(java.lang.String)}.
	 */
	@Test
	public void testGetListComputersByName() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		Computer expectedComputer = new Computer(	319L, 
													"HP Mini 1000", 
													Optional.ofNullable(LocalDate.parse("29/10/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
													Optional.empty(), 
													Optional.ofNullable(new Company(27L, "Hewlett-Packard")));
		expectedComputerList.add(expectedComputer);
		List<Computer> actualComputerList = computerService.getListComputersByName("HP Mini 1000");

		assertEquals(expectedComputerList, actualComputerList);
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#CreateNewComputer(java.lang.String, java.util.Optional, java.util.Optional, java.util.Optional)}.
	 */
	@Test
	public void testCreateNewComputer() {
		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertTrue(computerListShouldBeEmpty.isEmpty());

		Optional<String> nameNewPC = Optional.ofNullable("testCreateNewComputer");
		Optional<String> dateIntoducedNewPC = Optional.ofNullable("01/02/2003");
		Optional<String> dateDiscontinuedNewPC = Optional.ofNullable("04/05/2006");
		computerService.createNewComputer(nameNewPC, dateIntoducedNewPC, dateDiscontinuedNewPC, Optional.empty());

		List<Computer> computerListFound = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListFound.isEmpty());

		Computer computerFound = computerListFound.get(0);
		assertEquals(computerFound.getName(), nameNewPC.get());
		assertEquals(computerFound.getDateIntroduced(), Optional.ofNullable(
				LocalDate.parse(dateIntoducedNewPC.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
		assertEquals(computerFound.getDateDiscontinued(), Optional.ofNullable(
				LocalDate.parse(dateDiscontinuedNewPC.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

		computerService.deleteComputer(computerFound.getId());
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#deleteComputer(com.excilys.cdb.model.Computer)}.
	 */
	@Test
	public void testDeleteComputer() {
		Optional<String> nameNewPC = Optional.of("testCreateNewComputer");
		Optional<String> dateIntoducedNewPC = Optional.of("01/02/2003");
		Optional<String> dateDiscontinuedNewPC = Optional.of("04/05/2006");
		computerService.createNewComputer(nameNewPC, dateIntoducedNewPC, dateDiscontinuedNewPC, Optional.empty());

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListShouldNotBeEmpty.isEmpty());
		
		computerService.deleteComputer(computerListShouldNotBeEmpty.get(0).getId());

		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertTrue(computerListShouldBeEmpty.isEmpty());
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#updateComputer(com.excilys.cdb.model.Computer, java.lang.String)}.
	 */
	@Test
	public void testUpdateComputer() {
		Optional<String> nameNewPC = Optional.of("testCreateNewComputer");
		Optional<String> dateIntoducedNewPC = Optional.of("01/02/2003");
		Optional<String> dateDiscontinuedNewPC = Optional.of("04/05/2006");
		computerService.createNewComputer(nameNewPC, dateIntoducedNewPC, dateDiscontinuedNewPC, Optional.empty());

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		Computer computerToBeUpdate = computerListShouldNotBeEmpty.get(0);
		computerToBeUpdate.setName("testCreateNewComputer_RENAMED");
		computerService.updateComputer(computerToBeUpdate, "name");
		
		List<Computer> computerListAfterRename = computerService.getListComputersByName("testCreateNewComputer_RENAMED");
		assertFalse(computerListAfterRename.isEmpty());

		Computer computerAfterRename = computerListAfterRename.get(0);
		assertEquals(computerToBeUpdate, computerAfterRename);

		computerService.deleteComputer(computerAfterRename.getId());
	}
}
