package com.excilys.cdb.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

/**
 * Singleton that manage interactions with the BDD (for Company).
 * 
 * @author samy
 */
@Repository
public class CompanyDao {
	private final static String SQL_SELECT_ALL_COMPANY = "SELECT id, name FROM company;";
	private final static String SQL_SELECT_COMPANY_FROM_ID = "SELECT id, name FROM company WHERE ID = ?;";
	private final static String SQL_DELETE_COMPANY_FROM_ID = "DELETE FROM company WHERE id = ?;";
	private final static String SQL_DELETE_COMPUTER_WHERE_COMPANY = "DELETE FROM computer WHERE company_id = ?;";

	@Autowired
	JdbcTemplate jdbcTemplate;	
	@Autowired
	private CompanyRowMapper companyRowMapper;

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of {@link Company}
	 */
	public List<Company> getListCompanies() {
		return jdbcTemplate.query(SQL_SELECT_ALL_COMPANY, new Object[] {}, companyRowMapper);
	}

	/**
	 * Return the company corresponding to the given id
	 * 
	 * @param id Long
	 * @return a {@link Company}
	 */
	public Optional<Company> getCompanyFromId(Long id) {
		List<Company> companies = jdbcTemplate.query(SQL_SELECT_COMPANY_FROM_ID, new Object[] {id}, companyRowMapper);
		return companies.isEmpty() ? Optional.empty() : Optional.of(companies.get(0));
	}

	public void deleteCompany(Long companyId) {
		jdbcTemplate.update(SQL_DELETE_COMPUTER_WHERE_COMPANY, companyId);
		jdbcTemplate.update(SQL_DELETE_COMPANY_FROM_ID, companyId);
	}
}
