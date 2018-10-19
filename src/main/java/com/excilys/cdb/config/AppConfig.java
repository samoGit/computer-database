package com.excilys.cdb.config;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackages ={"com.excilys.cdb.config", "com.excilys.cdb.mapper", "com.excilys.cdb.persistence",
		"com.excilys.cdb.service", "com.excilys.cdb.servlet", "com.excilys.cdb.ui"})
public class AppConfig {

	private HikariDataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private final static Logger logger = LoggerFactory.getLogger("AppConfig");

	public AppConfig() {
		File fileHikariProperties = new File(AppConfig.class.getClassLoader().getResource("hikari.properties").getFile());
		HikariConfig config = new HikariConfig(fileHikariProperties.getAbsolutePath());
		dataSource = new HikariDataSource(config);
		try {
			Class.forName(dataSource.getDriverClassName());
		} catch (ClassNotFoundException e) {
			logger.error("Driver not found", e);
		}

		jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.setResultsMapCaseInsensitive(true);
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return jdbcTemplate;
	}
}
