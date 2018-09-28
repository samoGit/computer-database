package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.DateTimeException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class AddComputerServlet
 */
@WebServlet("/AddComputer")
public class AddComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static CompanyService companyService = CompanyService.INSTANCE;
	private static ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("AddComputerServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

        Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
        Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));

        request.setAttribute("pageNumber", (pageNumber.isPresent() ? pageNumber.get() : "1"));
        request.setAttribute("nbComputersByPage", (nbComputersByPage.isPresent() ? nbComputersByPage.get() : "1"));
        request.setAttribute("listCompanies", companyService.getListCompanies());

        this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");

        Optional<String> computerName = Optional.ofNullable(request.getParameter("computerName"));
        Optional<String> strIntroduced = Optional.ofNullable(request.getParameter("introduced"));
        Optional<String> strDiscontinued = Optional.ofNullable(request.getParameter("discontinued"));
        Optional<String> companyId = Optional.ofNullable(request.getParameter("companyId"));

        Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
        Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));

        try {
        	computerService.createNewComputer(computerName, strIntroduced, strDiscontinued, companyId);
        	response.sendRedirect("Dashboard?pageNumber=" + (pageNumber.isPresent() ? pageNumber.get() : "1")
        			 + "&nbComputersByPage=" + (nbComputersByPage.isPresent() ? nbComputersByPage.get() : "10"));
        }
        catch (DateTimeException e) {
        	logger.warn(e.getMessage());
            request.setAttribute("listCompanies", companyService.getListCompanies());
            this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
		}
	}
}
