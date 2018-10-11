package com.excilys.cdb.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.ConnectionManager;

public class CompanyServiceTest {

	private static CompanyService companyService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		companyService = CompanyService.INSTANCE;
		ConnectionManager.INSTANCE.activateTestMode();
	}

	@AfterClass
	public static void setUpAfterClass() throws Exception {
		ConnectionManager.INSTANCE.deactivateTestMode();
	}

	@Test
	public void testGetListCompanies() {
		List<String> expectedName5firstCompany = new ArrayList<>();
		expectedName5firstCompany.add("Apple Inc.");
		expectedName5firstCompany.add("Thinking Machines");
		expectedName5firstCompany.add("RCA");

		List<Company> actualListCompany = companyService.getListCompanies();
		for (int i = 0; i < 3; i++) {
			assertEquals(expectedName5firstCompany.get(i), actualListCompany.get(i).getName());
		}
	}

}
