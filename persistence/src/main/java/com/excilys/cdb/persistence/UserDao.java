package com.excilys.cdb.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.User;

@Component
public class UserDao {
	private final Logger logger = LoggerFactory.getLogger("UserDao");
	private final static String HQL_SELECT_USER_BYNAME = "FROM User WHERE username = :username";

	@Autowired
	private SessionFactory sessionFactory;

	public User findUserByUsername(String username) {
		logger.info("\n\n\n     --- Start findUserByUsername --- \n\n\n" + username + " \n\n\n");
		User user = null;
		try (Session session = sessionFactory.openSession()) {
			Query<User> hibernateQuery = session.createQuery(HQL_SELECT_USER_BYNAME, User.class);
			hibernateQuery.setParameter("username", username);
			logger.info("\n\n\n     --- hibernateQuery = " + hibernateQuery.getQueryString() + " --- \n\n\n");
			
			user = hibernateQuery.getSingleResult();
			logger.info("\n\n\n     --- user found = " + user + " --- \n\n\n");
		} catch (HibernateException e) {
			//throw new DataBaseAccessException();
			logger.info("\n\n\n     --- HibernateException TODO MANAGE THIS!!! --- \n\n\n");
		}
		return user;
	}
}
