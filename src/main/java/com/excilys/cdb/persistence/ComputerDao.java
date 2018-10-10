package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;

/**
 * Singleton that manage interactions with the BDD (for Computer).
 * 
 * @author samy
 */
public enum ComputerDao {
	/**
	 * Instance of {@link ComputerDao} (for Singleton pattern).
	 */
	INSTANCE;

	private final Logger logger = LoggerFactory.getLogger("ComputerDao");

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

	private final static String SQL_INSERT_COMPUTER = "INSERT INTO computer ";
	private final static String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE id IN (%s);";
	private final static String SQL_UPDATE_COMPUTER = "UPDATE computer SET %s = %s  WHERE id = %s;";
	private final static String SQL_UPDATE_COMPUTER_ALLFIELDS = "UPDATE computer SET name = ?"
			+ ", introduced = ?, discontinued = ?, company_id = ? WHERE id = ?;";

	private final ConnectionManager connectionManager = ConnectionManager.INSTANCE;

	private String getOrderByValue(String orderBy) {
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
		return orderByValue;
	}

	/**
	 * Return all the computers present in the BDD
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputers(PageInfo pageInfo) {
		ArrayList<Computer> listComputers = new ArrayList<>();

		try (Connection connection = connectionManager.getConnection()) {
			// We can't use prepared statement for orderBy value in sql...
			String orderByValue = getOrderByValue(pageInfo.getOrderBy());
			String sqlQuery = String.format(SQL_SELECT_ALL_COMPUTERS, orderByValue, orderByValue);
			
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.setLong(1, pageInfo.getOffset());
			stmt.setLong(2, pageInfo.getNbComputersByPage());
			logger.info(stmt.toString());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				listComputers.add(ComputerMapper.getComputer(resultSet));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return listComputers;
	}
		
	/**
	 * Return all computers present in the BDD which the name respect the pattern "*searchedName*" 
	 * 
	 * @param name String
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputersByName(PageInfo pageInfo) {
		ArrayList<Computer> listComputers = new ArrayList<>();

		try (Connection connection = connectionManager.getConnection()) {
			// We can't use prepared statement for orderBy value in sql...
			String orderByValue = getOrderByValue(pageInfo.getOrderBy());
			String sqlQuery = String.format(SQL_SELECT_ALL_COMPUTERS_BYNAME, orderByValue, orderByValue);
			
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			stmt.setString(1, "%"+pageInfo.getSearchedName()+"%");
			stmt.setLong(2, pageInfo.getOffset());
			stmt.setLong(3, pageInfo.getNbComputersByPage());
			logger.info(stmt.toString());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				listComputers.add(ComputerMapper.getComputer(resultSet));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return listComputers;
	}

	/**
	 * Create a new computer in the BDD.
	 * 
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param idCompany    Long
	 */
	public void createNewComputer(Computer computer) {
		String query = SQL_INSERT_COMPUTER; // (?) VALUES (?);

		String indices = "name";
		String values = "?";
		if (computer.getDateIntroduced().isPresent()) {
			indices += ", introduced";
			values += ", ?";
		}
		if (computer.getDateDiscontinued().isPresent()) {
			indices += ", discontinued";
			values += ", ?";
		}
		if (computer.getCompany().isPresent()) {
			indices += ", company_id";
			values += ", ?";
		}
		query += " (" + indices + ") VALUES (" + values + ");";

		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(query);

			int num = 1;
			stmt.setString(num++, computer.getName());
			if (computer.getDateIntroduced().isPresent())
				stmt.setString(num++, computer.getDateIntroduced().get().toString());
			if (computer.getDateDiscontinued().isPresent())
				stmt.setString(num++, computer.getDateDiscontinued().get().toString());
			if (computer.getCompany().isPresent())
				stmt.setLong(num++, computer.getCompany().get().getId());
			logger.info(stmt.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 * @throws SQLException .
	 */
	public void deleteComputer(String listComputersId) {
		try (Connection connection = connectionManager.getConnection()) {
			String sqlQuery = String.format(SQL_DELETE_COMPUTER, listComputersId);
			PreparedStatement stmt = connection.prepareStatement(sqlQuery);
			logger.info(stmt.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computer {@link Computer}
	 * @param field    String field of the Table to be updated
	 */
	public void updateComputer(Computer computer, String field) {
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

		try (Connection connection = connectionManager.getConnection()) {
			String query = String.format(SQL_UPDATE_COMPUTER, field, valueWithQuoteIfNeeded, computer.getId());
			PreparedStatement stmt;
			stmt = connection.prepareStatement(query);
			logger.info(stmt.toString());
			logger.info(stmt.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
	}

	/**
	 * 
	 * @param computer
	 */
	public void updateComputer(Computer computer) {
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt;
			stmt = connection.prepareStatement(SQL_UPDATE_COMPUTER_ALLFIELDS);
			stmt.setString(1, computer.getName());
			
			if (computer.getDateIntroduced().isPresent()) {
				stmt.setString(2, computer.getDateIntroduced().get().toString());
			}
			else {
				stmt.setNull(2, Types.DATE);
			}
			
			if (computer.getDateDiscontinued().isPresent()) {
				stmt.setString(3, computer.getDateDiscontinued().get().toString());
			}
			else {
				stmt.setNull(3, Types.DATE);
			}
			
			if (computer.getCompany().isPresent()) {
				stmt.setLong(4, computer.getCompany().get().getId());
			}
			else {
				stmt.setNull(4, Types.INTEGER);
			}
			
			stmt.setLong(5, computer.getId());
			
			logger.info(stmt.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 */
	public Long getNbComputers() {
		Long nbComputer = null;
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_NB_COMPUTERS);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet != null)
				resultSet.next();
			nbComputer = resultSet.getLong("nbComputers");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return nbComputer;
	}
	

	/**
	 * Return the number of computer present in the BDD which the name respect the pattern "*searchedName*"
	 * 
	 * @return Long
	 */
	public Long getNbComputersByName(String searchedName) {
		Long nbComputer = null;
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_NB_COMPUTERS_BYNAME);
			stmt.setString(1, "%"+searchedName+"%");
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet != null)
				resultSet.next();
			nbComputer = resultSet.getLong("nbComputers");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return nbComputer;
	}
}
