package com.excilys.cdb.persistence;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.config.CliAppConfig;
import com.excilys.cdb.model.Company;

import junit.framework.TestCase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CliAppConfig.class)
public class CompanyDaoTest extends TestCase {

	@Autowired
	private CompanyDao companyDao;

	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Test
	public void testGetListCompanies() {
		List<Company> actualListCompaniesFound = companyDao.getListCompanies();
		assertFalse(actualListCompaniesFound.isEmpty());
	}

	@Test
	public void testGetCompanyFromId() {
		Optional<Company> companyNotFound = companyDao.getCompanyFromId(-3L);
		assertFalse(companyNotFound.isPresent());

		Optional<Company> companyFound = companyDao.getCompanyFromId(9L);
		assertTrue(companyFound.isPresent());
		
	}
}
