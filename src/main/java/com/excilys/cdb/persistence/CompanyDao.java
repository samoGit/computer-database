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
			String hql = "FROM Company ORDER BY name";
			companies = session.createQuery(hql).list();
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
			String hql = "FROM Company WHERE id = :id";
			companies = session.createQuery(hql).setParameter("id", id).list();
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
		return companies.isEmpty() ? null : companies.get(0);
	}

	public void deleteCompany(Long companyId) throws DataBaseAccessException {
		try (Session session = sessionFactory.openSession()) {
			Transaction tx = session.beginTransaction();
			String hqlDeleteComputer = "DELETE Computer WHERE company_id = :companyId";
			session.createQuery(hqlDeleteComputer).setParameter("companyId", companyId).executeUpdate();
			String hqlDeleteCompany = "DELETE Company WHERE id = :companyId";
			session.createQuery(hqlDeleteCompany).setParameter("companyId", companyId).executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			throw new DataBaseAccessException();
		}
	}
}
