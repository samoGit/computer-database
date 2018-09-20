package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;

/**
 * Manage company.
 * 
 * @author samy
 */
public class CompanyService {

	private CompanyDao companyDao;

	public CompanyService() {
		companyDao = CompanyDao.INSTANCE;
	}

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of Company
	 */
	public List<Company> getListCompanies() {
		return companyDao.getListCompanies();
	}
}
