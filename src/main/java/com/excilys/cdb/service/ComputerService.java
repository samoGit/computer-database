package com.excilys.cdb.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.persistence.ComputerDao;

/**
 * Manage computer.
 * 
 * @author samy
 */
public class ComputerService {
		
	private ComputerDao computerDao;
	
	public ComputerService() {
		computerDao = ComputerDao.INSTANCE;
	}

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
	public void CreateNewComputer(String name, Optional<LocalDate> introduced, Optional<LocalDate> discontinued, Optional<Company> company) {
		computerDao.CreateNewComputer(name, introduced, discontinued, company);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param c {@link Computer}
	 */	
	public void DeleteComputer(Computer c) {
		computerDao.DeleteComputer(c);
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
