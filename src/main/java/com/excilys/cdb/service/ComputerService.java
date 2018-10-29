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
import com.excilys.cdb.persistence.DataBaseAccessException;

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
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputers(PageInfo pageInfo) throws DataBaseAccessException {
		return computerDao.getListComputers(pageInfo);
	}
	
	/**
	 * Return the list of computer present in the BDD.
	 * 
	 * @return List of {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public List<Computer> getListComputersByName(PageInfo pageInfo) throws DataBaseAccessException {
		return computerDao.getListComputersByName(pageInfo);
	}

	/**
	 * Create a new computer in the BDD
	 * 
	 * @param computerDto
	 * @throws InvalidComputerException
	 * @throws InvalidDateException
	 * @throws DataBaseAccessException 
	 */
	public void createNewComputer(Computer newComputer) throws InvalidComputerException, InvalidDateException, DataBaseAccessException {	
		logger.info("Create the following computer : " + newComputer);
		computerDao.createNewComputer(newComputer);
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param computerId
	 * @throws DataBaseAccessException 
	 */
	public void deleteComputer(String listComputersId) throws DataBaseAccessException {
		if (!"".equals(listComputersId)) {
			computerDao.deleteComputer(listComputersId);
		}
	}

	/**
	 * Delete the given computer from the BDD
	 * 
	 * @param field String field of the Table to be updated
	 * @param c     {@link Computer}
	 * @throws DataBaseAccessException 
	 */
	public void updateComputer(Computer c, String field) throws DataBaseAccessException {
		computerDao.updateComputer(c, field);
	}
	
	public void updateComputer(Computer computer) throws DataBaseAccessException {
		computerDao.updateComputer(computer);
	}

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputers() throws DataBaseAccessException {
		return computerDao.getNbComputers();
	}

	/**
	 * Return the number of computer present in the BDD which the name respect the pattern "*searchedName*"  
	 * 
	 * @return Long
	 * @throws DataBaseAccessException 
	 */
	public Long getNbComputersByName(String searchedName) throws DataBaseAccessException {
		return computerDao.getNbComputersByName(searchedName);
	}
}
