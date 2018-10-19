package com.excilys.cdb.persistence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.config.AppConfig;
import com.excilys.cdb.model.Company;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class CompanyDaoTest {

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
		Optional<Company> actualCompanyFound = companyDao.getCompanyFromId(2L);
		assertTrue(actualCompanyFound.isPresent());
		
		actualCompanyFound = companyDao.getCompanyFromId(3L);
		assertFalse(actualCompanyFound.isPresent());
	}
}
