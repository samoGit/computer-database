package com.excilys.cdb.persistence;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public enum ConnectionManager {

	INSTANCE;

//	private final Logger logger;

	protected final String JDBC_DRIVER_CLASS_NAME;
	protected final String BDD_URL;
	protected final String BDD_USER;
	protected final String BDD_PASSWORD;

	ConnectionManager() {
//		logger = LoggerFactory.getLogger("ConnectionManager");

		Properties properties = new Properties();
		try (InputStream input = new FileInputStream("config.properties")){
//			logger.info("Load config.properties file.");
			properties.load(input);
		} catch (FileNotFoundException e) {
//			logger.error(e.getMessage());
//			logger.error(e.getStackTrace().toString());
		}
		catch (IOException e) {
//			logger.error(e.getMessage());
//			logger.error(e.getStackTrace().toString());
		}
		
		JDBC_DRIVER_CLASS_NAME = properties.getProperty("JDBC_DRIVER_CLASS_NAME");
		BDD_URL = properties.getProperty("BDD_URL");
		BDD_USER = properties.getProperty("BDD_USER");
		BDD_PASSWORD = properties.getProperty("BDD_PASSWORD");		

		try {
//			logger.info("Init Driver");
			Class.forName(JDBC_DRIVER_CLASS_NAME);
		} catch (ClassNotFoundException e) {
//			logger.error(e.getMessage());
//			logger.error(e.getStackTrace().toString());
		}
	}

	public Connection getConnection() throws SQLException {
//		logger.info("Open connection to : {}", BDD_URL);
		return DriverManager.getConnection(BDD_URL, BDD_USER, BDD_PASSWORD);
	}
}
