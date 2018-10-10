package com.excilys.cdb.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum ConnectionManager {

	INSTANCE;
	
	private HikariConfig config;
    private DataSource datasource;

	ConnectionManager() {		        
		File fileHikariProperties = new File(getClass().getClassLoader().getResource("hikari.properties").getFile());
		config = new HikariConfig(fileHikariProperties.getAbsolutePath());
		datasource = new HikariDataSource(config);
	}

	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
}
