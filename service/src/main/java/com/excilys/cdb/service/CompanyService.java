package com.excilys.cdb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDao;
import com.excilys.cdb.persistence.DataBaseAccessException;

@Service
public class CompanyService {

	@Autowired
	private CompanyDao companyDao;

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of Company
	 * @throws DataBaseAccessException
	 */
	public List<Company> getListCompanies() throws DataBaseAccessException {
		return companyDao.getListCompanies();
	}

	/**
	 * Return the company corresponding to the given id
	 * 
	 * @param id Long
	 * @return a {@link Company}
	 * @throws DataBaseAccessException
	 */
	public Company getCompanyFromId(Long id) throws DataBaseAccessException {
		return companyDao.getCompanyFromId(id);
	}

	@Transactional
	public void deleteCompany(Long id) throws DataBaseAccessException {
		companyDao.deleteCompany(id);
	}

	@Transactional
	public void updateCompany(Company company) throws DataBaseAccessException {
		companyDao.updateCompany(company);
	}

	@Transactional
	public void addCompany(String companyName) throws DataBaseAccessException {
		companyDao.addCompany(new Company(-1L, companyName));
	}
}
