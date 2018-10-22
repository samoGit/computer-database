package com.excilys.cdb.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.persistence.ComputerDao;

/**
 * Manage computer.
 * 
 * @author samy
 */
@Service
public class ComputerService {

	@Autowired
	private ComputerDao computerDao;

	private final Logger logger = LoggerFactory.getLogger("ComputerService");

	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputers(PageInfo pageInfo) {
		return computerDao.getListComputers(pageInfo);
	}
	
	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 */
	public List<Computer> getListComputersByName(PageInfo pageInfo) {
		return computerDao.getListComputersByName(pageInfo);
	}

	/**
	 * Create a new computer in the BDD
	 * 
	 * @param computerDto
	 * @throws InvalidComputerException
	 * @throws InvalidDateException
	 */
	public void createNewComputer(Computer newComputer) throws InvalidComputerException, InvalidDateException {	
		logger.info("Create the following computer : " + newComputer);
		computerDao.createNewComputer(newComputer);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 */
	public void deleteComputer(String listComputersId) {
		if (!"".equals(listComputersId)) {
			computerDao.deleteComputer(listComputersId);
		}
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
