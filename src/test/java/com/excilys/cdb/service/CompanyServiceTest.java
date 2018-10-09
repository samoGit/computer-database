package com.excilys.cdb.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.model.Company;

public class CompanyServiceTest {

	private static CompanyService companyService;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		companyService = CompanyService.INSTANCE;
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
