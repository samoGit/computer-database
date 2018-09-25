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
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class EditComputerServlet
 */
@WebServlet("/EditComputer")
public class EditComputerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static CompanyService companyService = CompanyService.INSTANCE;
	private final Logger logger;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputerServlet() {
        super();
        logger = LoggerFactory.getLogger("EditComputerServlet");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

        String computerName = request.getParameter("computerName");
        request.setAttribute("computerName", computerName);
        
        String dateDiscontinued = request.getParameter("dateDiscontinued");
        request.setAttribute("dateDiscontinued", dateDiscontinued);
        
        String dateIntroduced = request.getParameter("dateIntroduced");
        request.setAttribute("dateIntroduced", dateIntroduced);
        
        String companyName = request.getParameter("companyName");
        request.setAttribute("companyName", companyName);
        
        List<Company> listCompanies = companyService.getListCompanies();
        request.setAttribute("listCompanies", listCompanies);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/editComputer.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

		doGet(request, response);
	}
}
