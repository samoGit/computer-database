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
		computerService = new ComputerService();
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#getListComputers(java.lang.Long)}.
	 */
	@Test
	public void testGetListComputers() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		expectedComputerList.add(new Computer(Long.valueOf(10),
				"Apple IIc Plus", 
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty()
			));
		expectedComputerList.add(new Computer(Long.valueOf(11), 
				"Apple II Plus", 
				Optional.empty(), 
				Optional.empty(), 
				Optional.empty()
			));
		expectedComputerList.add(new Computer(Long.valueOf(12), 
				"Apple III", 
				Optional.ofNullable(LocalDate.parse("01/05/1980", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
				Optional.ofNullable(LocalDate.parse("01/04/1984", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
				Optional.ofNullable(new Company(Long.valueOf(1), "Apple Inc."))
			));

		List<Computer> actualComputerList = computerService.getListComputers(Long.valueOf(9), Long.valueOf(3));
		

		for (int i=0; i<3; i++) {
			System.out.println("\nexpectedComputerList.get(" + i + ") = ");
			System.out.println(expectedComputerList.get(i));
			System.out.println("actualComputerList.get(" + i + ")");
			System.out.println(actualComputerList.get(i));

			assertEquals(expectedComputerList.get(i), actualComputerList.get(i));
		}
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#getListComputersByName(java.lang.String)}.
	 */
	@Test
	public void testGetListComputersByName() {
		List<Computer> expectedComputerList = new ArrayList<Computer>();
		Computer expectedComputer = new Computer(	Long.valueOf(319), 
													"HP Mini 1000", 
													Optional.ofNullable(LocalDate.parse("29/10/2008", DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
													Optional.empty(), 
													Optional.ofNullable(new Company(Long.valueOf(27), "Hewlett-Packard")));
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

		String nameNewPC = "testCreateNewComputer";
		LocalDate dateIntoducedNewPC = LocalDate.parse("01/02/2003", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate dateDiscontinuedNewPC = LocalDate.parse("04/05/2006", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		computerService.CreateNewComputer(nameNewPC, Optional.ofNullable(dateIntoducedNewPC), Optional.ofNullable(dateDiscontinuedNewPC),  Optional.empty());

		List<Computer> computerListFound = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListFound.isEmpty());

		Computer computerFound = computerListFound.get(0);
		assertEquals(computerFound.getName(), nameNewPC);
		assertEquals(computerFound.getDateIntroduced().get(), dateIntoducedNewPC);
		assertEquals(computerFound.getDateDiscontinued().get(), dateDiscontinuedNewPC);

		computerService.DeleteComputer(computerFound);
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#DeleteComputer(com.excilys.cdb.model.Computer)}.
	 */
	@Test
	public void testDeleteComputer() {
		String nameNewPC = "testCreateNewComputer";
		LocalDate dateIntoducedNewPC = LocalDate.parse("01/02/2003", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate dateDiscontinuedNewPC = LocalDate.parse("04/05/2006", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		computerService.CreateNewComputer(nameNewPC, Optional.ofNullable(dateIntoducedNewPC), Optional.ofNullable(dateDiscontinuedNewPC),  Optional.empty());		

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListShouldNotBeEmpty.isEmpty());
		
		computerService.DeleteComputer(computerListShouldNotBeEmpty.get(0));

		List<Computer> computerListShouldBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertTrue(computerListShouldBeEmpty.isEmpty());
	}

	/**
	 * Test method for {@link com.excilys.cdb.service.ComputerService#UpdateComputer(com.excilys.cdb.model.Computer, java.lang.String)}.
	 */
	@Test
	public void testUpdateComputer() {
		String nameNewPC = "testCreateNewComputer";
		LocalDate dateIntoducedNewPC = LocalDate.parse("01/02/2003", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		LocalDate dateDiscontinuedNewPC = LocalDate.parse("04/05/2006", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		computerService.CreateNewComputer(nameNewPC, Optional.ofNullable(dateIntoducedNewPC), Optional.ofNullable(dateDiscontinuedNewPC),  Optional.empty());

		List<Computer> computerListShouldNotBeEmpty = computerService.getListComputersByName("testCreateNewComputer");
		assertFalse(computerListShouldNotBeEmpty.isEmpty());

		Computer computerToBeUpdate = computerListShouldNotBeEmpty.get(0);
		computerToBeUpdate.setName("testCreateNewComputer_RENAMED");
		computerService.UpdateComputer(computerToBeUpdate, "name");
		
		List<Computer> computerListAfterRename = computerService.getListComputersByName("testCreateNewComputer_RENAMED");
		assertFalse(computerListAfterRename.isEmpty());

		Computer computerAfterRename = computerListAfterRename.get(0);
		assertEquals(computerToBeUpdate, computerAfterRename);

		computerService.DeleteComputer(computerAfterRename);
	}
}
