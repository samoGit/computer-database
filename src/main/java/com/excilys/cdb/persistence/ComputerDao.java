package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

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
			+ "ORDER BY computer.id LIMIT ?,?; ";

	final static String SQL_SELECT_COMPUTERS_FROM_NAME = "SELECT "
			+ "computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name "
			+ "FROM computer LEFT JOIN company ON computer.company_id = company.id "
			+ "WHERE computer.name = ?; ";
	
	final static String SQL_SELECT_NB_COMPUTERS = "SELECT count(id) as nbComputers FROM computer;";
	
	private final static String SQL_INSERT_COMPUTER = "INSERT INTO computer ";
	private final static String SQL_DELETE_COMPUTER = "DELETE FROM computer WHERE id=?;";
	private final static String SQL_UPDATE_COMPUTER = "UPDATE computer SET %s = %s  WHERE id = %s;";

	private final ConnectionManager connectionManager = ConnectionManager.INSTANCE;

	/**
	 * Return all the computers present in the BDD
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputers(Long offset, Long nbComputersByPage) {
		ArrayList<Computer> listComputers = new ArrayList<>();

		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_ALL_COMPUTERS);
			stmt.setLong(1, offset);
			stmt.setLong(2, nbComputersByPage);
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
	 * Find every computers in the BDD with a given name
	 * 
	 * @param name String
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputersByName(String name) {
		ArrayList<Computer> listComputersFound = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_COMPUTERS_FROM_NAME);
			stmt.setString(1, name);
			logger.info(stmt.toString());			
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				listComputersFound.add(ComputerMapper.getComputer(resultSet));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return listComputersFound;
	}

	/**
	 * Create a new computer in the BDD.
	 * 
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param idCompany    Long
	 */
	public void CreateNewComputer(String name, Optional<LocalDate> introduced, Optional<LocalDate> discontinued,
			Optional<Company> company) {
		String query = SQL_INSERT_COMPUTER; // (?) VALUES (?);

		String indices = "name";
		String values = "?";
		if (introduced.isPresent()) {
			indices += ", introduced";
			values += ", ?";
		}
		if (discontinued.isPresent()) {
			indices += ", discontinued";
			values += ", ?";
		}
		if (company.isPresent()) {
			indices += ", company_id";
			values += ", ?";
		}
		query += " (" + indices + ") VALUES (" + values + ");";

		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(query);

			int num = 1;
			stmt.setString(num++, name);
			if (introduced.isPresent())
				stmt.setString(num++, introduced.get().toString());
			if (discontinued.isPresent())
				stmt.setString(num++, discontinued.get().toString());
			if (company.isPresent())
				stmt.setLong(num++, company.get().getId());
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
	 * @throws SQLException .
	 */
	public void DeleteComputer(Computer computer) {
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_COMPUTER);
			stmt.setLong(1, computer.getId());
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
	public void UpdateComputer(Computer computer, String field) {
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
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 */// 	final static String SQL_SELECT_NB_COMPUTERS = "SELECT count(id) as nbComputers FROM computer;";

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
}