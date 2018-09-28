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

import com.excilys.cdb.service.ComputerPageService;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/Dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("doGet");

		Optional<String> strNbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));
		Optional<String> strPageNumber = Optional.ofNullable(request.getParameter("pageNumber"));

		ComputerPageService computerPageService = new ComputerPageService(strNbComputersByPage, strPageNumber);

		request.setAttribute("nbComputersByPage", computerPageService.getNbComputersByPage());
		request.setAttribute("nbComputers", computerPageService.getNbComputers());
		request.setAttribute("nbPage", computerPageService.getNbPageTotal());
		request.setAttribute("pageNumber", computerPageService.getPageNumber());
		request.setAttribute("listComputerDtos", computerPageService.getListComputerDtos());

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("doPost");
		doGet(request, response);
	}
}
