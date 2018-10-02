package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
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
	private final Logger logger = LoggerFactory.getLogger("ComputerService");

	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputers(Long offset, Long nbComputersByPage, Optional<String> orderBy) {
		return computerDao.getListComputers(offset, nbComputersByPage, orderBy);
	}
	
	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputersByName(Long offset, Long nbComputersByPage, String searchedName, Optional<String> orderBy) {
		return computerDao.getListComputersByName(offset, nbComputersByPage, searchedName, orderBy);
	}

	/**
	 * Create a new computer in the BDD
	 * 
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param company      {@link Company}
	 * @throws InvalidComputerException
	 * @throws InvalidDateException 
	 */
	public void createNewComputer(Optional<String> computerName, Optional<String> strDateIntroduced,
			Optional<String> strDateDiscontinued, Optional<String> strCompanyId) throws InvalidComputerException, InvalidDateException {
		
		Computer newComputer = ComputerMapper.getComputer(computerName, strDateIntroduced, strDateDiscontinued, strCompanyId);
		logger.info("Create the following computer : " + newComputer);
		computerDao.createNewComputer(newComputer);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 */
	public void deleteComputer(Long computerId) {
		computerDao.deleteComputer(computerId);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param field String field of the Table to be updated
	 * @param c     {@link Computer}
	 */
	public void updateComputer(Computer c, String field) {
		computerDao.updateComputer(c, field);
	}
	
	public void updateComputer(Computer computer) {
		computerDao.updateComputer(computer);
	}

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 */
	public Long getNbComputers() {
		return computerDao.getNbComputers();
	}
	
	/**
	 * Return the number of computer present in the BDD which the name respect the pattern "*searchedName*"  
	 * 
	 * @return Long
	 */
	public Long getNbComputersByName(String searchedName) {
		return computerDao.getNbComputersByName(searchedName);
	}
}
