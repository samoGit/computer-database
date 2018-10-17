package com.excilys.cdb.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Repository
public class ConnectionManager {

	private HikariConfig config;
	private HikariDataSource datasource;
	private boolean isTestModeActivated;
	private final Logger logger = LoggerFactory.getLogger("ComputerDao");

	public ConnectionManager() {
		File fileHikariProperties = new File(getClass().getClassLoader().getResource("hikari.properties").getFile());
		config = new HikariConfig(fileHikariProperties.getAbsolutePath());
		datasource = new HikariDataSource(config);
		isTestModeActivated = false;

		// Register JDBC driver (just for test)
		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
			logger.error("Impossible to register JDBC driver.");
		}
	}

	public Connection getConnection() throws SQLException {
		if (isTestModeActivated) {
			return DriverManager.getConnection("jdbc:h2:mem:cbd_test;"
					+ "INIT=RUNSCRIPT FROM '/home/excilys/eclipse-workspace/cdb/src/test/resources/scripts/dbtest.sql'",
					"ADMINCDBTEST", "qwerty1234");
		} else {
			return datasource.getConnection();
		}
	}

	public void activateTestMode() {
		isTestModeActivated = true;
	}

	public void deactivateTestMode() {
		isTestModeActivated = false;
	}
}
