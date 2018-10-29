package com.excilys.cdb.persistence;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;

/**
 * Singleton that manage interactions with the BDD (for Computer).
 * 
 * @author samy
 */
@Repository
public class ComputerDao {

	private final static String SQL_SELECT_ALL_COMPUTERS = "SELECT "
			+ "computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "ORDER BY %s IS NOT NULL DESC, %s "
			+ "LIMIT ?,?; ";
	
	private final static String SQL_SELECT_ALL_COMPUTERS_BYNAME = "SELECT "
			+ "computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name LIKE ? "
			+ "ORDER BY %s IS NOT NULL DESC, %s "
			+ "LIMIT ?,?; ";

	final static String SQL_SELECT_NB_COMPUTERS = "SELECT count(id) as nbComputers FROM computer; ";
	final static String SQL_SELECT_NB_COMPUTERS_BYNAME = "SELECT count(id) as nbComputers FROM computer "
			+ "WHERE computer.name LIKE ? ;";

	private final static String SQL_INSERT_COMPUTER = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?);";
	private final static String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE id IN (%s);";
	private final static String SQL_UPDATE_COMPUTER = "UPDATE computer SET %s = %s  WHERE id = %s;";
	private final static String SQL_UPDATE_COMPUTER_ALLFIELDS = "UPDATE computer SET name = ?"
			+ ", introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";

	@Autowired
	JdbcTemplate jdbcTemplate;	
	@Autowired
	private ComputerRowMapper computerRowMapper;

	private String getSqlQueryWithOrderByValue(String sqlQuery, String orderBy) {
		String orderByValue = "computer.id";
		if (!"".equals(orderBy)) {
			switch (orderBy) {
			case "Name":
				orderByValue = "computer.name";
				break;
			case "Introduced":
				orderByValue = "computer.introduced";
				break;
			case "Discontinued":
				orderByValue = "computer.discontinued";
				break;
			case "Company":
				orderByValue = "company.name";
				break;
			}
		}
		return String.format(sqlQuery, orderByValue, orderByValue);
	}

	/**
	 * Return all the computers present in the BDD
	 * 
	 * @return List of {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputers(PageInfo pageInfo) throws DataBaseAccessException {
		try {
			// We can't use prepared statement for orderBy value in sql...
			String sqlQuery = getSqlQueryWithOrderByValue(SQL_SELECT_ALL_COMPUTERS, pageInfo.getOrderBy());
			return jdbcTemplate.query(sqlQuery, 
									  new Object[] {pageInfo.getOffset(), pageInfo.getNbComputersByPage()},  
									  computerRowMapper);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}
		
	/**
	 * Return all computers present in the BDD which the name respect the pattern "*searchedName*" 
	 * 
	 * @param name String
	 * @return List of {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputersByName(PageInfo pageInfo) throws DataBaseAccessException {
		try {
			// We can't use prepared statement for orderBy value in sql...
			String sqlQuery = getSqlQueryWithOrderByValue(SQL_SELECT_ALL_COMPUTERS_BYNAME, pageInfo.getOrderBy());
			return jdbcTemplate.query(	sqlQuery, 
									  	new Object[] {
									  			"%"+pageInfo.getSearchedName()+"%", 
									  			pageInfo.getOffset(), 
									  			pageInfo.getNbComputersByPage()
										},  
									  	computerRowMapper);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Create a new computer in the BDD.
	 * 
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param idCompany    Long
	 * @throws DataBaseAccessException 
	 */
	public void createNewComputer(Computer computer) throws DataBaseAccessException {
		try {
			jdbcTemplate.update(SQL_INSERT_COMPUTER, 
								computer.getName(),
								computer.getDateIntroduced().isPresent() ? Date.valueOf(computer.getDateIntroduced().get()) : null,
								computer.getDateDiscontinued().isPresent() ? Date.valueOf(computer.getDateDiscontinued().get()) : null,
								computer.getCompany().isPresent() ? computer.getCompany().get().getId() : null);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 * @throws DataBaseAccessException 
	 * @throws SQLException .
	 */
	public void deleteComputer(String listComputersId) throws DataBaseAccessException {
		try {
			// We can't use prepared statement for list of values in sql... >>> WHERE id IN (%s)
			String sqlQuery = String.format(SQL_DELETE_COMPUTER, listComputersId);
			jdbcTemplate.update(sqlQuery);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computer {@link Computer}
	 * @param field    String field of the Table to be updated
	 * @throws DataBaseAccessException 
	 */
	public void updateComputer(Computer computer, String field) throws DataBaseAccessException {
		try {
			String valueWithQuoteIfNeeded = "null";
			switch (field) {
			case "id":
				valueWithQuoteIfNeeded = computer.getId().toString();
				break;
			case "name":
				valueWithQuoteIfNeeded = computer.getName().toString();
				break;
			case "introduced":
				if (computer.getDateIntroduced().isPresent())
					valueWithQuoteIfNeeded = computer.getDateIntroduced().get().toString();
				break;
			case "discontinued":
				if (computer.getDateDiscontinued().isPresent())
					valueWithQuoteIfNeeded = computer.getDateDiscontinued().get().toString();
				break;
			case "company_id":
				if (computer.getCompany().isPresent())
					valueWithQuoteIfNeeded = computer.getCompany().get().getId().toString();
				break;
			}
	
			// in SQL null value must NOT be surrounded with quotes
			if (!valueWithQuoteIfNeeded.equals("null"))
				valueWithQuoteIfNeeded = "\"" + valueWithQuoteIfNeeded + "\"";
	
			String sqlQuery = String.format(SQL_UPDATE_COMPUTER, field, valueWithQuoteIfNeeded, computer.getId());
			jdbcTemplate.update(sqlQuery);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * 
	 * @param computer
	 * @throws DataBaseAccessException 
	 */
	public void updateComputer(Computer computer) throws DataBaseAccessException {
		try {
			jdbcTemplate.update(SQL_UPDATE_COMPUTER_ALLFIELDS, 
								computer.getName(),
								computer.getDateIntroduced().isPresent() ? Date.valueOf(computer.getDateIntroduced().get()) : null,
								computer.getDateDiscontinued().isPresent() ? Date.valueOf(computer.getDateDiscontinued().get()) : null,
								computer.getCompany().isPresent() ? computer.getCompany().get().getId() : null,
								computer.getId());
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputers() throws DataBaseAccessException {
		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_NB_COMPUTERS, Long.class);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Return the number of computer present in the BDD which the name respect the pattern "*searchedName*"
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputersByName(String searchedName) throws DataBaseAccessException {
		try {
			return jdbcTemplate.queryForObject(	SQL_SELECT_NB_COMPUTERS_BYNAME, 
												new Object[] {"%"+searchedName+"%"}, 
												Long.class);
		}
		catch (DataAccessException e) {
			throw new DataBaseAccessException();
		}
	}
}
