package com.excilys.cdb.servlet;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
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
	private final Logger logger;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComputerServlet() {
        super();
        companyService = CompanyService.INSTANCE;
        logger = LoggerFactory.getLogger("AddComputerServlet");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

		List<Company> listCompanies = companyService.getListCompanies();
        request.setAttribute("listCompanies", listCompanies);

        this.getServletContext().getRequestDispatcher( "/WEB-INF/views/addComputer.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");

        String computerName = request.getParameter("computerName");

        String strIntroduced = request.getParameter("introduced");
        Optional<LocalDate> dateIntroduced = Optional.empty();
        try {
        	dateIntroduced = Optional.ofNullable(LocalDate.parse(strIntroduced, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        catch (DateTimeException e) {
        	if (!strIntroduced.equals("")) {
        		logger.warn("Incorect date format unter in date introduced");
        	}
		}

        String strDiscontinued = request.getParameter("discontinued");
        Optional<LocalDate> dateDiscontinued = Optional.empty();
        try {
            dateDiscontinued = Optional.ofNullable(LocalDate.parse(strDiscontinued, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        catch (DateTimeException e) {
        	if (!strDiscontinued.equals("")) {
        		logger.warn("Incorect date format unter in date discontinued");
        	}
		}

        String companyId = request.getParameter("companyId");
        Optional<Company> company = Optional.empty();
        
        Computer newComputer = new Computer(Long.valueOf(-1), computerName, dateIntroduced, dateDiscontinued, company); 
        logger.info("Create the following computer : " + newComputer);
        computerService.CreateNewComputer(newComputer);

        this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
	}

}
