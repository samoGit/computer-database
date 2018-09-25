package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/Dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DashboardServlet() {
        super();
        logger = LoggerFactory.getLogger("DashboardServlet");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doGet");

        String strNbComputersByPage = request.getParameter("nbComputersByPage");
		Long nbComputersByPage = (strNbComputersByPage == null || strNbComputersByPage.equals("")) ? 10 : Long.valueOf(strNbComputersByPage);
        request.setAttribute("nbComputersByPage", nbComputersByPage);
        
        Long nbComputers = computerService.getNbComputers();
        request.setAttribute("nbComputers", nbComputers);

        Long nbPage = nbComputers % nbComputersByPage == 0 ? nbComputers/nbComputersByPage : nbComputers/nbComputersByPage+1;
        request.setAttribute("nbPage", nbPage);

        String strPageNumber = request.getParameter("pageNumber");
        if (strPageNumber == null || strPageNumber.equals("")) {
        	strPageNumber = "1";
        }
        else if (strPageNumber.equals("lastPage")) {
        	strPageNumber = String.valueOf(nbPage);
        }
        Long pageNumber = Long.valueOf(strPageNumber);
        request.setAttribute("pageNumber", pageNumber);

        List<Computer> listComputers = computerService.getListComputers((pageNumber-1)*nbComputersByPage, nbComputersByPage);
        List<ComputerDto> listComputerDtos = new ArrayList<>();
        listComputers.forEach( (c) -> {
        	listComputerDtos.add(new ComputerDto(c));
        });
        request.setAttribute("listComputerDtos", listComputerDtos);

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
		doGet(request, response);
	}

}
