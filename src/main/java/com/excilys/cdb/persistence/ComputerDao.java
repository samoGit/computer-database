package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;

/**
 * Singleton that manage interactions with the BDD (for Computer).
 * 
 * @author samy
 */
@Repository
public class ComputerDao {
	private final Logger logger = LoggerFactory.getLogger("ComputerDao");

	private final static String HQL_SELECT_ALL_COMPUTERS = "FROM Computer ORDER BY %s ";
	private final static String HQL_SELECT_ALL_COMPUTERS_BYNAME = "FROM Computer WHERE name LIKE :searchedName ORDER BY %s ";

	private final static String HQL_UPDATE_COMPUTER = "UPDATE Computer SET %s = :newValue WHERE id = :id ";
	
	private final static String HQL_UPDATE_COMPUTER_ALLFIELDS = "UPDATE Computer SET name = :name, dateIntroduced = "
			+ ":dateIntroduced, dateDiscontinued = :dateDiscontinued, company = :company WHERE id = :id  ";
	private final static String HQL_DELETE_COMPUTER = "DELETE Computer WHERE id IN (%s) ";

	final static String HQL_SELECT_NB_COMPUTERS = "SELECT count(id) as nbComputers FROM Computer ";
	final static String HQL_SELECT_NB_COMPUTERS_BYNAME = "SELECT count(id) as nbComputers FROM Computer "
			+ "WHERE name LIKE :searchedName ";	

	@Autowired
	private SessionFactory sessionFactory;

	private String getSqlOrderBy(String orderBy) {
		String sqlOrderBy = "id";
		if (!"".equals(orderBy)) {
			switch (orderBy) {
			case "Name":
				sqlOrderBy = "name";
				break;
			case "Introduced":
				sqlOrderBy = "dateIntroduced DESC";
				break;
			case "Discontinued":
				sqlOrderBy = "dateDiscontinued DESC";
				break;
			case "Company":
				sqlOrderBy = "company.name";
				break;
			}
		}
		return sqlOrderBy;
	}

	/**
	 * Return all the computers present in the BDD
	 * 
	 * @return List of {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputers(PageInfo pageInfo) throws DataBaseAccessException {
		logger.info("\n\n\n     --- Start getListComputers, pageInfo = " + pageInfo + " --- \n\n\n");
		List<Computer> computers = null;
		try (Session session = sessionFactory.openSession()) {
			String hql = String.format(HQL_SELECT_ALL_COMPUTERS, getSqlOrderBy(pageInfo.getOrderBy()));
			Query<Computer> hibernateQuery = session.createQuery(hql, Computer.class);
			hibernateQuery.setFirstResult(pageInfo.getOffset().intValue());
			hibernateQuery.setMaxResults(pageInfo.getNbComputersByPage().intValue());
			logger.info("\n\n\n     --- hibernateQuery = " + hibernateQuery.getQueryString() + " --- \n\n\n");
			
			computers = hibernateQuery.list();
			logger.info("\n\n\n     --- nb computers found = " + computers.size() + " --- \n\n\n");
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return computers;
	}
		
	/**
	 * Return all computers present in the BDD which the name respect the pattern "*searchedName*" 
	 * 
	 * @param name String
	 * @return List of {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputersByName(PageInfo pageInfo) throws DataBaseAccessException {
		logger.info("\n\n\n     --- Start getListComputersByName, pageInfo = " + pageInfo + " --- \n\n\n");		
		List<Computer> computers = null;
		try (Session session = sessionFactory.openSession()) {
			String hql = String.format(HQL_SELECT_ALL_COMPUTERS_BYNAME, getSqlOrderBy(pageInfo.getOrderBy()));
			Query<Computer> hibernateQuery = session.createQuery(hql, Computer.class);
			hibernateQuery.setParameter("searchedName", "%"+pageInfo.getSearchedName()+"%");
			hibernateQuery.setFirstResult(pageInfo.getOffset().intValue());
			hibernateQuery.setMaxResults(pageInfo.getNbComputersByPage().intValue());
			logger.info("\n\n\n     --- hibernateQuery = " + hibernateQuery.getQueryString() + " --- \n\n\n");
			
			computers = hibernateQuery.list();
			logger.info("\n\n\n     --- nb computers found = " + computers.size() + " --- \n\n\n");
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return computers;
	}

	/**
	 * Create a new computer in the BDD.
	 * 
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param idCompany    Long
	 * @throws DataBaseAccessException 
	 */
	public void createNewComputer(Computer computer) throws DataBaseAccessException {
		try (Session session = sessionFactory.openSession()) {
			session.save(computer);
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 * @throws DataBaseAccessException 
	 * @throws SQLException .
	 */
	public void deleteComputer(String listComputersId) throws DataBaseAccessException {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			String hql = String.format(HQL_DELETE_COMPUTER, listComputersId);			
			@SuppressWarnings("rawtypes")
			Query hibernateQuery = session.createQuery(hql);
			logger.info("\n\n\n     --- hibernateQuery = " + hibernateQuery.getQueryString() + " --- \n\n\n");
			hibernateQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computer {@link Computer}
	 * @param field    String field of the Table to be updated
	 * @throws DataBaseAccessException 
	 */
	public void updateComputer(Computer computer, String field) throws DataBaseAccessException {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			@SuppressWarnings("rawtypes")
			Query hibernateQuery = null;
			
			switch (field) {
			case "id":
				hibernateQuery = session.createQuery(String.format(HQL_UPDATE_COMPUTER, "id"));				
				hibernateQuery.setParameter("newValue", computer.getId());
				break;
			case "name":
				hibernateQuery = session.createQuery(String.format(HQL_UPDATE_COMPUTER, "name"));				
				hibernateQuery.setParameter("newValue", computer.getName());
				break;
			case "introduced":
				hibernateQuery = session.createQuery(String.format(HQL_UPDATE_COMPUTER, "dateIntroduced"));				
				hibernateQuery.setParameter("newValue", computer.getDateIntroduced());
				break;
			case "discontinued":
				hibernateQuery = session.createQuery(String.format(HQL_UPDATE_COMPUTER, "dateDiscontinued"));				
				hibernateQuery.setParameter("newValue", computer.getDateDiscontinued());
				break;
			case "company_id":
				hibernateQuery = session.createQuery(String.format(HQL_UPDATE_COMPUTER, "company"));				
				hibernateQuery.setParameter("newValue", computer.getCompany());
				break;
			}
			hibernateQuery.setParameter("id", computer.getId());
			
			hibernateQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DataBaseAccessException();
		}
	}

	/**
	 * 
	 * @param computer
	 * @throws DataBaseAccessException 
	 */
	public void updateComputer(Computer computer) throws DataBaseAccessException {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			@SuppressWarnings("rawtypes")
			Query hibernateQuery = session.createQuery(HQL_UPDATE_COMPUTER_ALLFIELDS);
			hibernateQuery.setParameter("name", computer.getName());
			hibernateQuery.setParameter("dateIntroduced", computer.getDateIntroduced());
			hibernateQuery.setParameter("dateDiscontinued", computer.getDateDiscontinued());
			hibernateQuery.setParameter("company", computer.getCompany());
			hibernateQuery.setParameter("id", computer.getId());

			logger.info("\n\n\n     --- hibernateQuery = " + hibernateQuery.getQueryString() + " --- \n\n\n");
			hibernateQuery.executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DataBaseAccessException();
		}
	}

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputers() throws DataBaseAccessException {
		try (Session session = sessionFactory.openSession()) {
			List<Long> list = session.createQuery(HQL_SELECT_NB_COMPUTERS, Long.class).list();
			if (!list.isEmpty())
				return list.get(0);
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return 0L;
	}

	/**
	 * Return the number of computer present in the BDD which the name respect the pattern "*searchedName*"
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputersByName(String searchedName) throws DataBaseAccessException {
		try (Session session = sessionFactory.openSession()) {
			List<Long> list = session.createQuery(HQL_SELECT_NB_COMPUTERS_BYNAME, Long.class)
									.setParameter("searchedName", "%"+searchedName+"%")
									.list();
			if (!list.isEmpty())
				return list.get(0);
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return 0L;
	}
}
