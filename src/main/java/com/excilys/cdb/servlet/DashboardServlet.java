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


        Long offset = 1L;
        String pageNumber = request.getParameter("pageNumber");
        if (pageNumber == null || pageNumber.equals("")) {
        	pageNumber = "1";
        }
		offset = (Long.valueOf(pageNumber)-1)*10;

		Long nbComputersByPage = Long.valueOf(10);
        List<Computer> listComputers = computerService.getListComputers(offset, nbComputersByPage);
        List<ComputerDto> listComputerDtos = new ArrayList<>();
        listComputers.forEach( (c) -> {
        	listComputerDtos.add(new ComputerDto(c));
        });
        
        request.setAttribute("listComputerDtos", listComputerDtos);
        Long nbComputers = computerService.getNbComputers();
        request.setAttribute("nbComputers", nbComputers);
        request.setAttribute("pageNumber", pageNumber);
        
        Long nbPage = nbComputers/10;
        if (10*nbPage < nbComputers) {
        	nbPage++;
        }
        request.setAttribute("nbPage", nbPage);

        this.getServletContext().getRequestDispatcher( "/WEB-INF/views/dashboard.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("doPost");
		doGet(request, response);
	}

}
