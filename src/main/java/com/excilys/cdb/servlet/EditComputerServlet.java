package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.service.CompanyService;

/**
 * Servlet implementation class EditComputerServlet
 */
@WebServlet("/EditComputer")
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static CompanyService companyService = CompanyService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("EditComputerServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("doGet");

		String pageNumber = request.getParameter("pageNumber");
		String nbComputersByPage = request.getParameter("nbComputersByPage");
		String computerId = request.getParameter("computerId");
		String computerName = request.getParameter("computerName");
		String dateDiscontinued = request.getParameter("dateDiscontinued");
		String dateIntroduced = request.getParameter("dateIntroduced");
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
		logger.info("doGet");
		doGet(request, response);
	}
}
