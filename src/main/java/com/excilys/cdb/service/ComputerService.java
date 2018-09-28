package com.excilys.cdb.service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private CompanyService companyService = CompanyService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("ComputerService");

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
	 * @param name         String
	 * @param introduced   LocalDate
	 * @param discontinued LocalDate
	 * @param company      {@link Company}
	 */
	public void createNewComputer(Optional<String> computerName, Optional<String> strIntroduced,
			Optional<String> strDiscontinued, Optional<String> companyId) {
		if (!computerName.isPresent() || "".equals(computerName.get())) {
			computerName = Optional.of("NO_NAME");// TODO throw an Exception
		}

		Optional<LocalDate> dateIntroduced = Optional.empty();
		try {
			if (strIntroduced.isPresent() && !"".equals(strIntroduced.get())) {
				dateIntroduced = Optional
						.ofNullable(LocalDate.parse(strIntroduced.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			}
		} catch (DateTimeException e) {
			throw new DateTimeException("Incorect date format unter in date introduced");
		}

		Optional<LocalDate> dateDiscontinued = Optional.empty();
		try {
			if (strDiscontinued.isPresent() && !"".equals(strDiscontinued.get())) {
				dateDiscontinued = Optional
						.ofNullable(LocalDate.parse(strDiscontinued.get(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			}
		} catch (DateTimeException e) {
			throw new DateTimeException("Incorect date format unter in date discontinued");
		}

		Optional<Company> company = Optional.empty();
		if (companyId.isPresent()) {
			company = companyService.getCompanyFromId(Long.valueOf(companyId.get()));
		}

		Computer newComputer = new Computer(Long.valueOf(-1), computerName.get(), dateIntroduced, dateDiscontinued,
				company);
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

	/**
	 * Return the number of computer present in the BDD
	 * 
	 * @return Long
	 */
	public Long getNbComputers() {
		return computerDao.getNbComputers();
	}
}
