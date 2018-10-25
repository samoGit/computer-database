package com.excilys.cdb.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class WebAppConfig {

	private HikariDataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private final static Logger logger = LoggerFactory.getLogger("WebAppConfig");

	public WebAppConfig() {
		// TODO use ResourceBundle
		File fileHikariProperties = new File(
				WebAppConfig.class.getClassLoader().getResource("hikari.properties").getFile());
		HikariConfig config = new HikariConfig(fileHikariProperties.getAbsolutePath());
		dataSource = new HikariDataSource(config);
		try {
			Class.forName(dataSource.getDriverClassName());
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		}
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return jdbcTemplate;
	}
}
