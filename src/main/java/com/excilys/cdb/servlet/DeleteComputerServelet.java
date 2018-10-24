//package com.excilys.cdb.servlet;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.context.support.SpringBeanAutowiringSupport;
//
//import com.excilys.cdb.service.ComputerService;
//
///**
// * Servlet implementation class DeleteComputerServelet
// */
//@WebServlet("/DeleteComputerServelet")
//public class DeleteComputerServelet extends HttpServlet {
//	private static final long serialVersionUID = -6028278204408049563L;
//
//	@Autowired
//	private ComputerService computerService;
//	
//	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");
//
//	@Override
//	public void init(ServletConfig config) throws ServletException {
//		super.init(config);
//		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//	}
//
//	/**
//	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		logger.info("\n\ndoGet");
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//	}
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
//	 *      response)
//	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		logger.info("\n\ndoPost");
//
//		String selection = request.getParameter("selection");
//		computerService.deleteComputer(selection);
//
//		Optional<String> pageNumber = Optional.ofNullable(request.getParameter("pageNumber"));
//		Optional<String> nbComputersByPage = Optional.ofNullable(request.getParameter("nbComputersByPage"));
//		response.sendRedirect("Dashboard?pageNumber=" + (pageNumber.isPresent() ? pageNumber.get() : "1")
//				+ "&nbComputersByPage=" + (nbComputersByPage.isPresent() ? nbComputersByPage.get() : "10"));
//	}
//}
