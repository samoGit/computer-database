package com.excilys.cdb.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDto;
import com.excilys.cdb.model.ComputerMapper;
import com.excilys.cdb.model.InvalidComputerException;
import com.excilys.cdb.model.InvalidDateException;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.persistence.DataBaseAccessException;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
@EnableWebMvc
@RequestMapping
public class ComputerController {

	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	private final ComputerService computerService;
	private final CompanyService companyService;
	private final ComputerMapper computerMapper;

	@Autowired
	public ComputerController(ComputerService computerService, CompanyService companyService,
			ComputerMapper computerMapper) {
		this.computerService = computerService;
		this.companyService = companyService;
		this.computerMapper = computerMapper;
	}

	private Long getNbComputers(String searchedName) throws DataBaseAccessException {
		if ("".equals(searchedName)) {
			return computerService.getNbComputers();
		} else {
			return computerService.getNbComputersByName(searchedName);
		}
	}

	private List<ComputerDto> getListComputerDto(PageInfo pageInfo) throws DataBaseAccessException {
		if (!"".equals(pageInfo.getSearchedName())) {
			List<Computer> listComputer = computerService.getListComputersByName(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
		} else {
			List<Computer> listComputer = computerService.getListComputers(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
		}
	}

	@GetMapping("Dashboard")
	public String getDashboard(ModelMap model, @RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "") String nbComputersByPage,
			@RequestParam(required = false, defaultValue = "") String orderBy) {
		logger.info(" ------------------- getDashboard");

		try {
			Long nbComputers = getNbComputers(search);
			logger.info(" ------------------- nbComputers = " + nbComputers);

			PageInfo pageInfo = new PageInfo(pageNumber, nbComputersByPage, search, orderBy, nbComputers);
			model.addAttribute("pageNumber", pageInfo.getPageNumber());
			model.addAttribute("nbComputersByPage", pageInfo.getNbComputersByPage());
			model.addAttribute("search", pageInfo.getSearchedName());
			model.addAttribute("orderBy", pageInfo.getOrderBy());
			model.addAttribute("nbComputers", pageInfo.getNbComputers());
			model.addAttribute("nbPage", pageInfo.getNbPageTotal());
			model.addAttribute("listComputerDtos", this.getListComputerDto(pageInfo));
		} catch (DataBaseAccessException e) {
			return "500";
		}

		return "dashboard";
	}

	@GetMapping("AddComputer")
	public String getAddComputer(ModelMap model, @RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "10") String nbComputersByPage,
			@RequestParam(required = false, defaultValue = "") String orderBy) {
		logger.info(" ------------------- getAddComputer");

		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("nbComputersByPage", nbComputersByPage);
		try {
			model.addAttribute("listCompanies", companyService.getListCompanies());
		} catch (DataBaseAccessException e) {
			return "500";
		}

		return "addComputer";
	}

	@PostMapping("AddComputer")
	public String postAddComputer(ModelMap model, @RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "10") String nbComputersByPage,
			@RequestParam(required = false, defaultValue = "") String orderBy, @RequestParam String computerName,
			@RequestParam String dateIntroduced, @RequestParam String dateDiscontinued,
			@RequestParam String companyId) {
		logger.info(" ------------------- postAddComputer");

		ComputerDto computerDto = new ComputerDto();
		computerDto.setName(Optional.ofNullable(computerName));
		computerDto.setDateIntroduced(Optional.ofNullable(dateIntroduced));
		computerDto.setDateDiscontinued(Optional.ofNullable(dateDiscontinued));
		computerDto.setCompanyId(Optional.ofNullable(companyId));

		model.addAttribute("nbComputersByPage", nbComputersByPage);

		try {
			computerService.createNewComputer(computerMapper.getComputer(computerDto));
		} catch (InvalidComputerException | InvalidDateException e) {
			String errorMsgKey = e.getMessageKey();
			logger.warn(errorMsgKey);
			model.addAttribute("errorMsgKey", errorMsgKey);
			model.addAttribute("computerName", computerDto.getName());
			model.addAttribute("dateIntroduced", computerDto.getDateIntroduced());
			model.addAttribute("dateDiscontinued", computerDto.getDateDiscontinued());
			model.addAttribute("companyId", computerDto.getCompanyId());
			try {
				model.addAttribute("listCompanies", companyService.getListCompanies());
			} catch (DataBaseAccessException e1) {
				return "500";
			}
			model.addAttribute("pageNumber", pageNumber);
			return "addComputer";
		} catch (DataBaseAccessException e) {
			return "500";
		}

		model.addAttribute("pageNumber", "lastPage");
		return "redirect:Dashboard";

	}

	@PostMapping("DeleteComputer")
	public String postDeleteComputer(ModelMap model, @RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "10") String nbComputersByPage,
			@RequestParam(required = false, defaultValue = "") String orderBy, @RequestParam String selection) {
		logger.info(" ------------------- postDeleteComputer");

		try {
			computerService.deleteComputer(selection);
		} catch (DataBaseAccessException e) {
			return "500";
		}
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("nbComputersByPage", nbComputersByPage);
		model.addAttribute("search", search);
		model.addAttribute("orderBy", orderBy);

		return "redirect:Dashboard";
	}

	@GetMapping("EditComputer")
	public String getEditComputer(ModelMap model, @RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "10") String nbComputersByPage,
			@RequestParam String computerId, @RequestParam String computerName, @RequestParam String dateIntroduced,
			@RequestParam String dateDiscontinued, @RequestParam String companyName) {
		logger.info(" ------------------- getEditComputer");
		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("nbComputersByPage", nbComputersByPage);
		model.addAttribute("computerId", computerId);
		model.addAttribute("computerName", computerName);
		model.addAttribute("dateIntroduced", dateIntroduced);
		model.addAttribute("dateDiscontinued", dateDiscontinued);
		model.addAttribute("companyName", companyName);
		try {
			model.addAttribute("listCompanies", companyService.getListCompanies());
		} catch (DataBaseAccessException e) {
			return "500";
		}
		return "editComputer";
	}

	@PostMapping("EditComputer")
	public String postEditComputer(ModelMap model,
			@RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "10") String nbComputersByPage,
			@RequestParam String computerId, @RequestParam String computerName, @RequestParam String dateIntroduced,
			@RequestParam String dateDiscontinued, @RequestParam String companyId) {
		logger.info(" ------------------- postEditComputer");

		ComputerDto computerDto = new ComputerDto();
		computerDto.setId(Optional.ofNullable(computerId));
		computerDto.setName(Optional.ofNullable(computerName));
		computerDto.setDateIntroduced(Optional.ofNullable(dateIntroduced));
		computerDto.setDateDiscontinued(Optional.ofNullable(dateDiscontinued));
		computerDto.setCompanyId(Optional.ofNullable(companyId));

		model.addAttribute("pageNumber", pageNumber);
		model.addAttribute("nbComputersByPage", nbComputersByPage);

		try {
			Computer computer = computerMapper.getComputer(computerDto);
			logger.info("computer to be update = " + computer);
			computerService.updateComputer(computer);

			return "redirect:Dashboard";
		} catch (InvalidComputerException | InvalidDateException e) {
			String errorMsgKey = e.getMessageKey();
			logger.warn(errorMsgKey);

			model.addAttribute("computerId", computerDto.getCompanyId());
			model.addAttribute("computerName", computerDto.getName());
			model.addAttribute("dateIntroduced", computerDto.getDateIntroduced());
			model.addAttribute("dateDiscontinued", computerDto.getDateDiscontinued());
			model.addAttribute("companyId", computerDto.getCompanyId());
			model.addAttribute("errorMsgKey", errorMsgKey);
			try {
				model.addAttribute("listCompanies", companyService.getListCompanies());
			} catch (DataBaseAccessException e1) {
				return "500";
			}
			return "editComputer";
		} catch (DataBaseAccessException e) {
			return "500";
		}
	}

	@GetMapping("*")
	public String get404(ModelMap model) {
		logger.info(" ------------------- get404");
		return "404";
	}
}
