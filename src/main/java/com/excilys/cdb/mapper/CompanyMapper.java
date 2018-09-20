package com.excilys.cdb.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

/**
 * Static class which convert a {@link ResultSet} into an object
 * {@link Company}.
 * 
 * @author samy
 *
 */
public class CompanyMapper {

	/**
	 * This class only provides static methods and should not be instantiated
	 */
	private CompanyMapper() {
	}

	/**
	 * Convert a {@link ResultSet} into an object {@link Company}.
	 * 
	 * @param resultSet {@link ResultSet}
	 * @return {@link Company}
	 * @throws SQLException if the columnLabel is not valid; if a database access
	 *                      error occurs or this method is called on a closed result
	 *                      set
	 */
	public static Company getCompany(ResultSet resultSet) throws SQLException {
		Long id = resultSet.getLong("id");
		String name = resultSet.getString("name");

		return new Company(id, name);
	}

}
