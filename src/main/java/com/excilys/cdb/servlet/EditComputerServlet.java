package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerPageService;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class EditComputerServlet
 */
@WebServlet("/EditComputer")
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 6852085386625285312L;

	private static CompanyService companyService = CompanyService.INSTANCE;
	private static ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("EditComputerServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoGet");

		String pageNumber = request.getParameter("pageNumber");
		String nbComputersByPage = request.getParameter("nbComputersByPage");
		String computerId = request.getParameter("computerId");
		String computerName = request.getParameter("computerName");
		String dateIntroduced = request.getParameter("dateDiscontinued");
		String dateDiscontinued = request.getParameter("dateIntroduced");
		String companyName = request.getParameter("companyName");

		List<Company> listCompanies = companyService.getListCompanies();

		request.setAttribute("pageNumber", pageNumber);
		request.setAttribute("nbComputersByPage", nbComputersByPage);
		request.setAttribute("computerId", computerId);
		request.setAttribute("computerName", computerName);
		request.setAttribute("dateDiscontinued", dateDiscontinued);
		request.setAttribute("dateIntroduced", dateIntroduced);
		request.setAttribute("companyName", companyName);
		request.setAttribute("listCompanies", listCompanies);

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoPost");

		ComputerDto computerDto = new ComputerDto();
		computerDto.setId(Optional.ofNullable(request.getParameter("computerId")));
		computerDto.setName(Optional.ofNullable(request.getParameter("computerName")));
		computerDto.setDateIntroduced(Optional.ofNullable(request.getParameter("dateIntroduced")));
		computerDto.setDateDiscontinued(Optional.ofNullable(request.getParameter("dateDiscontinued")));
		computerDto.setCompanyId(Optional.ofNullable(request.getParameter("companyId")));
		
		Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
		String pageNumberNeverEmpty = pageNumber.isPresent() ? pageNumber.get() : "1";		
		Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));
		String nbComputersByPageNeverEmpty = ComputerPageService.DEFAULT_NB_COMPUTERS_BY_PAGE.toString();
		if (nbComputersByPage.isPresent()) {
			nbComputersByPageNeverEmpty = nbComputersByPage.get();
		}

		try {
			Computer computer = ComputerMapper.getComputer(computerDto); 
			logger.info("computer to be update = " + computer);
			computerService.updateComputer(computer);
			
			response.sendRedirect("Dashboard?pageNumber=" + pageNumberNeverEmpty + "&nbComputersByPage="
					+ nbComputersByPageNeverEmpty);
		} catch (InvalidComputerException | InvalidDateException e) {
			String errorMsg = e.getMessage();
			logger.warn(errorMsg);

			request.setAttribute("computerId", computerDto.getCompanyId());
			request.setAttribute("computerName", computerDto.getName());
			request.setAttribute("dateIntroduced", computerDto.getDateIntroduced());
			request.setAttribute("dateDiscontinued", computerDto.getDateDiscontinued());
			request.setAttribute("companyId", computerDto.getCompanyId());

			request.setAttribute("listCompanies", companyService.getListCompanies());
			request.setAttribute("pageNumber", pageNumberNeverEmpty);
			request.setAttribute("nbComputersByPage", nbComputersByPageNeverEmpty);
			request.setAttribute("errorMsg", errorMsg);
			
			this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
		}
	}	
}
