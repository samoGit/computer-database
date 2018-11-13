package com.excilys.cdb.config;

import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan("com.excilys.cdb.persistence, com.excilys.cdb.model, com.excilys.cdb.service, "
		+ "com.excilys.cdb.ui")
public class CliAppConfig {

	private final static Logger logger = LoggerFactory.getLogger("CliAppConfig");

	@Bean
	public DataSource getDataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		ResourceBundle bundle = ResourceBundle.getBundle("hikari");
		String driverClassName = "";
		try {
			driverClassName = bundle.getString("driverClassName");
			Class.forName(driverClassName);
			dataSource.setJdbcUrl(bundle.getString("jdbcUrl"));
			dataSource.setUsername(bundle.getString("username"));
			dataSource.setPassword(bundle.getString("password"));
			dataSource.setDriverClassName(driverClassName);
		} catch (MissingResourceException e) {
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
		return dataSource;
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public LocalSessionFactoryBean getSessionFactory(DataSource datasource) {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(datasource);
		sessionFactory.setPackagesToScan(new String[] { "com.excilys.cdb.model" });

		ResourceBundle bundle = ResourceBundle.getBundle("hibernate");
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", bundle.getString("dialect"));
		hibernateProperties.setProperty("hibernate.show_sql", bundle.getString("show_sql"));

		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;
	}

	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager manager = new HibernateTransactionManager();
		manager.setSessionFactory(sessionFactory);
		return manager;
	}
}
