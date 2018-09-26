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

import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class DeleteComputerServelet
 */
@WebServlet("/DeleteComputerServelet")
public class DeleteComputerServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");

        String strSelection = request.getParameter("selection");
        if (strSelection != null)
        {
        	String[] strComputerId = strSelection.split(",");
        	for (String s : strComputerId) {
               	computerService.deleteComputer(Long.valueOf(s));
        	}
        }

		Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
		if (!pageNumber.isPresent() || "".equals(pageNumber.get()))
			pageNumber = Optional.of("1");
        response.sendRedirect("Dashboard?pageNumber=" + pageNumber.get());
	}

}
