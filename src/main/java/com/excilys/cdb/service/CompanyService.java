package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;

/**
 * Manage company.
 * 
 * @author samy
 */
@Service
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of Company
	 */
	public List<Company> getListCompanies() {
		return companyDao.getListCompanies();
	}

	/**
	 * Return the company corresponding to the given id
	 * 
	 * @param id Long
	 * @return a {@link Company}
	 */
	public Optional<Company> getCompanyFromId(Long id) {
		return companyDao.getCompanyFromId(id);
	}
	
	public void deleteCompany(Long id) {
		companyDao.deleteCompany(id);		
	}

}
