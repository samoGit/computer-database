package com.excilys.cdb.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class WebAppConfig {

	private HikariDataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private final static Logger logger = LoggerFactory.getLogger("WebAppConfig");

	public WebAppConfig() {		
		dataSource = new HikariDataSource();
		ResourceBundle bundle = ResourceBundle.getBundle("hikari");
		String driverClassName = "";
		try {
			driverClassName = bundle.getString("driverClassName");
			Class.forName(driverClassName);
			dataSource.setJdbcUrl(bundle.getString("jdbcUrl"));
			dataSource.setUsername(bundle.getString("username"));
			dataSource.setPassword(bundle.getString("password"));
			dataSource.setDriverClassName(driverClassName);
			jdbcTemplate = new JdbcTemplate(dataSource);
		}
		catch (MissingResourceException e) {
			logger.error(" >>> hikari properties not found >>> \n");
			logger.error("   Message  = " + e.getMessage());
			logger.error("   KeyNotFound  = " + e.getKey() + "\n");
			e.printStackTrace();
			logger.error(" <<< hikari properties not found <<< \n");
		} catch (ClassNotFoundException e) {
			logger.error(" >>> driverClassName not found >>> \n");
			logger.error("   Message  = " + e.getMessage());
			logger.error("   driverClassNameNotFound  = " + driverClassName);
			e.printStackTrace();
			logger.error(" <<< hikari properties not found <<< \n");
		}
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return jdbcTemplate;
	}
}
