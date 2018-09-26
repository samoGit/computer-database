package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.DateTimeException;

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

        request.setAttribute("listCompanies", companyService.getListCompanies());
        this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");

        String computerName = request.getParameter("computerName");
        String strIntroduced = request.getParameter("introduced");
        String strDiscontinued = request.getParameter("discontinued");
        String companyId = request.getParameter("companyId");
        String pageNumber = request.getParameter("pageNumber");

        try {
        	computerService.createNewComputer(computerName, strIntroduced, strDiscontinued, companyId);
        	response.sendRedirect("Dashboard?pageNumber=" + pageNumber);
        }
        catch (DateTimeException e) {
        	logger.warn(e.getMessage());
            request.setAttribute("listCompanies", companyService.getListCompanies());
            this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
		}
	}
}
