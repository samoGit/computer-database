package com.excilys.cdb.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.CompanyDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDto;
import com.excilys.cdb.persistence.DataBaseAccessException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@RestController
public class CompanyControllerRest {

	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");
	private final static String REST_BASE_URL = "/api/rest/";

	private final CompanyService companyService;
	private final ComputerService computerService;

	@Autowired
	public CompanyControllerRest(ComputerService computerService, CompanyService companyService) {
		this.companyService = companyService;
		this.computerService = computerService;
	}

	@GetMapping(REST_BASE_URL + "companies")
	public List<CompanyDto> getAllCompanies() {
		logger.info(" ------------------- getAllComputers");
		try {
			List<Company> listCompanies = companyService.getListCompanies();
			List<CompanyDto> listCompaniesDto = listCompanies.stream().map((Company c) -> {
				return new CompanyDto(c);
			}).collect(Collectors.toList());
			logger.info(listCompaniesDto.toString());
			return listCompaniesDto;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@GetMapping(REST_BASE_URL + "company/{id}")
	public CompanyDto getCompany(@PathVariable("id") Long companyId) {
		logger.info(" ------------------- getCompany");
		try {
			CompanyDto companyDto = new CompanyDto(companyService.getCompanyFromId(companyId));
			logger.info(companyDto.toString());
			return companyDto;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@GetMapping(REST_BASE_URL + "computersByCompanyId/{id}")
	public List<ComputerDto> getListComputersByCompanyId(@PathVariable("id") Long companyId) {
		logger.info(" ------------------- getCompanyById");
		try {
			List<Computer> listComputers = computerService.getListComputersByCompanyId(companyId);
			List<ComputerDto> listComputersDto = listComputers.stream().map((Computer c) -> {
				return new ComputerDto(c);
			}).collect(Collectors.toList());
			logger.info(listComputersDto.toString());
			return listComputersDto;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@PatchMapping(REST_BASE_URL + "company/{id}/name/{newCompanyName}")
	public HttpStatus postUpdateCompanyById(@PathVariable("id") Long companyId,
			@PathVariable("newCompanyName") String newCompanyName) {
		logger.info(" ------------------- postUpdateCompanyById");
		try {
			companyService.updateCompany(new Company(companyId, newCompanyName));
			return HttpStatus.OK;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@DeleteMapping(REST_BASE_URL + "company/{id}")
	public HttpStatus postDeleteCompanyById(@PathVariable("id") Long companyId) {
		logger.info(" ------------------- postDeleteCompanyById");
		try {
			companyService.deleteCompany(companyId);
			return HttpStatus.OK;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@PostMapping(REST_BASE_URL + "company/name/{companyName}")
	public HttpStatus postCompany(@PathVariable("companyName") String companyName) {
		logger.info(" ------------------- postDeleteCompanyById");
		try {
			companyService.addCompany(companyName);
			return HttpStatus.OK;
		} catch (DataBaseAccessException e) {
			logger.error(e.getMessage());
		}
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}

}
