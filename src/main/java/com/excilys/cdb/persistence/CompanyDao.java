package com.excilys.cdb.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

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

//	private final Logger logger = LoggerFactory.getLogger("CompanyDao");

	private final static String SQL_SELECT_ALL_COMPANY = "SELECT id, name FROM company;";

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
//			logger.info(stmt.toString());
			ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next()) {
				listCompanies.add(CompanyMapper.getCompany(resultSet));
			}
		} catch (SQLException e) {
//			logger.error(e.getMessage());
//			logger.error(e.getStackTrace().toString());
		}
		return listCompanies;
	}

}
