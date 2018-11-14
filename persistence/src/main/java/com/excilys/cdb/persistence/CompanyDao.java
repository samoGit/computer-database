package com.excilys.cdb.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.model.Company;

@Repository
public class CompanyDao {
	private final static String HQL_SELECT_ALL_COPANIES = "FROM Company ORDER BY name ";
	private final static String HQL_SELECT_COPANY = "FROM Company WHERE id = :id ";
	private final static String HQL_DELETE_COPUTER_WHERE_COMPANY = "DELETE Computer WHERE company_id = :companyId ";
	private final static String HQL_DELETE_COPANY = "DELETE Company WHERE id = :companyId ";

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Return the list of companies present in the BDD
	 * 
	 * @return List of {@link Company}
	 * @throws DataBaseAccessException
	 */
	@SuppressWarnings("unchecked")
	public List<Company> getListCompanies() throws DataBaseAccessException {
		List<Company> companies = null;
		try (Session session = sessionFactory.openSession()) {
			companies = session.createQuery(HQL_SELECT_ALL_COPANIES).list();
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return companies;
	}

	/**
	 * Return the company corresponding to the given id
	 * 
	 * @param id Long
	 * @return a {@link Company}
	 * @throws DataBaseAccessException
	 */
	@SuppressWarnings("unchecked")
	public Company getCompanyFromId(Long id) throws DataBaseAccessException {
		List<Company> companies = null;
		try (Session session = sessionFactory.openSession()) {
			companies = session.createQuery(HQL_SELECT_COPANY).setParameter("id", id).list();
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return companies.isEmpty() ? null : companies.get(0);
	}

	public void deleteCompany(Long companyId) throws DataBaseAccessException {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			session.createQuery(HQL_DELETE_COPUTER_WHERE_COMPANY).setParameter("companyId", companyId).executeUpdate();
			session.createQuery(HQL_DELETE_COPANY).setParameter("companyId", companyId).executeUpdate();
			transaction.commit();
		} catch (HibernateException e) {
			transaction.rollback();
			throw new DataBaseAccessException();
		}
	}
}
