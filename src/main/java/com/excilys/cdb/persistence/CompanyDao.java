package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.mapper.CompanyMapper;
import com.excilys.cdb.model.Company;

/**
 * Singleton that manage interactions with the BDD (for Company).
 * 
 * @author samy
 */
public enum CompanyDao {
	/**
	 * Instance of {@link CompanyDao} (for Singleton pattern).
	 */
	INSTANCE;

	private final Logger logger = LoggerFactory.getLogger("CompanyDao");

	private final static String SQL_SELECT_ALL_COMPANY = "SELECT id, name FROM company;";
	private final static String SQL_SELECT_COMPANY_FROM_ID = "SELECT id, name FROM company WHERE ID = ?;";

	private final ConnectionManager connectionManager = ConnectionManager.INSTANCE;

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of {@link Company}
	 */
	public List<Company> getListCompanies() {
		ArrayList<Company> listCompanies = new ArrayList<>();
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_ALL_COMPANY);
			logger.info(stmt.toString());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				listCompanies.add(CompanyMapper.getCompany(resultSet));
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return listCompanies;
	}

	/**
	 * Return the company corresponding to the given id
	 * 
	 * @param id Long
	 * @return a {@link Company}
	 */
	public Optional<Company> getCompanyFromId(Long id) {
		Optional<Company> company = Optional.empty();
		try (Connection connection = connectionManager.getConnection()) {
			PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_COMPANY_FROM_ID);
			stmt.setLong(1, id);
			logger.info(stmt.toString());
			ResultSet resultSet = stmt.executeQuery();
			resultSet.next();
			company = Optional.ofNullable(CompanyMapper.getCompany(resultSet));
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		return company;
	}
}
