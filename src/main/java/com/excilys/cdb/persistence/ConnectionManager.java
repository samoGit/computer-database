package com.excilys.cdb.persistence;

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
        config = new HikariConfig("/home/excilys/eclipse-workspace/cdb/src/main/resources/hikari.properties");
		datasource = new HikariDataSource(config);
	}

	public Connection getConnection() throws SQLException {
		return datasource.getConnection();
	}
}
