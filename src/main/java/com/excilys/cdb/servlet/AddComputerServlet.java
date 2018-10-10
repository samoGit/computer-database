package com.excilys.cdb.servlet;

import java.io.IOException;
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
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class AddComputerServlet
 */
@WebServlet("/AddComputer")
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1491265413354643955L;
	
	private static CompanyService companyService = CompanyService.INSTANCE;
	private static ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("AddComputerServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoGet");

		Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
		Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));

		request.setAttribute("pageNumber", (pageNumber.isPresent() ? pageNumber.get() : "1"));
		request.setAttribute("nbComputersByPage", (nbComputersByPage.isPresent() ? nbComputersByPage.get() : "1"));
		request.setAttribute("listCompanies", companyService.getListCompanies());

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoPost");

		ComputerDto computerDto = new ComputerDto();
		computerDto.setName(Optional.ofNullable(request.getParameter("computerName")));
		computerDto.setDateIntroduced(Optional.ofNullable(request.getParameter("dateIntroduced")));
		computerDto.setDateDiscontinued(Optional.ofNullable(request.getParameter("dateDiscontinued")));
		computerDto.setCompanyId(Optional.ofNullable(request.getParameter("companyId")));

		Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
		String pageNumberNeverEmpty = pageNumber.isPresent() ? pageNumber.get() : "1";
		Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));
		String nbComputersByPageNeverEmpty = nbComputersByPage.isPresent() ? nbComputersByPage.get() : "10";

		try {
			computerService.createNewComputer(ComputerMapper.getComputer(computerDto));
			response.sendRedirect("Dashboard?pageNumber=lastPage&nbComputersByPage="
					+ nbComputersByPageNeverEmpty);
		} catch (InvalidComputerException | InvalidDateException e) {
			String errorMsg = e.getMessage();
			logger.warn(errorMsg);

			request.setAttribute("computerName", computerDto.getName());
			request.setAttribute("dateIntroduced", computerDto.getDateIntroduced());
			request.setAttribute("dateDiscontinued", computerDto.getDateDiscontinued());
			request.setAttribute("companyId", computerDto.getCompanyId());

			request.setAttribute("listCompanies", companyService.getListCompanies());
			request.setAttribute("pageNumber", pageNumberNeverEmpty);
			request.setAttribute("nbComputersByPage", nbComputersByPageNeverEmpty);
			request.setAttribute("errorMsg", errorMsg);

			this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);
		}
	}
}
