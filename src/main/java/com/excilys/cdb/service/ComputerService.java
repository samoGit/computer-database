package com.excilys.cdb.service;

import java.util.List;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;

/**
 * Manage computer.
 * 
 * @author samy
 */
public enum ComputerService {
	/**
	 * Instance of {@link ComputerService} (for Singleton pattern).
	 */
	INSTANCE;

	private ComputerDao computerDao = ComputerDao.INSTANCE;

	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 */	
	public List<Computer> getListComputers(Long offset, Long nbComputersByPage) {
		return computerDao.getListComputers(offset, nbComputersByPage);
	}

	/**
	 * Find every computers in the BDD with a given name
	 * 
	 * @param name String
	 * @return a list of {@link Computer} 
	 */
	public List<Computer> getListComputersByName(String name) {
		return computerDao.getListComputersByName(name);
	}

	/**
	 * Create a new computer in the BDD
	 * 
	 * @param name String
	 * @param introduced LocalDate
	 * @param discontinued LocalDate
	 * @param company {@link Company}
	 */
	public void CreateNewComputer(Computer computer) {
		computerDao.CreateNewComputer(computer);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 */	
	public void DeleteComputer(Long computerId) {
		computerDao.DeleteComputer(computerId);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param field String field of the Table to be updated
	 * @param c {@link Computer}
	 */	
	public void UpdateComputer(Computer c, String field) {
		computerDao.UpdateComputer(c, field);
	}
	
	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 */
	public Long getNbComputers() {
		return computerDao.getNbComputers();
	}
}
