package com.excilys.cdb.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.service.ComputerService;

@Controller
@EnableWebMvc
@RequestMapping
public class ComputerController {

	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	private final ComputerService computerService;

	@Autowired
	public ComputerController(ComputerService computerService) {
		this.computerService = computerService;
	}

	private Long getNbComputers(String searchedName) {
		if ("".equals(searchedName)) {
			return computerService.getNbComputers();
		} else {
			return computerService.getNbComputersByName(searchedName);
		}
	}

	private List<ComputerDto> getListComputerDto(PageInfo pageInfo) {
		if (!"".equals(pageInfo.getSearchedName())) {
			List<Computer> listComputer = computerService.getListComputersByName(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
		} else {
			List<Computer> listComputer = computerService.getListComputers(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
		}
	}

	@GetMapping("Dashboard")
	public String getDashboard(ModelMap model, 
			@RequestParam(required = false, defaultValue = "") String search,
			@RequestParam(required = false, defaultValue = "1") String pageNumber,
			@RequestParam(required = false, defaultValue = "") String nbComputersByPage,
			@RequestParam(required = false, defaultValue = "") String orderBy) {
		logger.info(" ------------------- model = " + model);
		logger.info(" ------------------- search = " + search);

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

		return "dashboard";
	}
}
