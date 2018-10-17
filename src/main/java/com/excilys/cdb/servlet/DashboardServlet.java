package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/Dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1182886835641082355L;

	@Autowired
	private ComputerService computerService;

	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoGet");

		Long nbComputers = getNbComputers(Optional.ofNullable(request.getParameter("search")));
		PageInfo pageInfo = new PageInfo(Optional.ofNullable(request.getParameter("pageNumber")), 
										 Optional.ofNullable(request.getParameter("nbComputersByPage")), 
										 Optional.ofNullable(request.getParameter("search")), 
										 Optional.ofNullable(request.getParameter("orderBy")), 
										 nbComputers);

		request.setAttribute("pageNumber", pageInfo.getPageNumber());
		request.setAttribute("nbComputersByPage", pageInfo.getNbComputersByPage());		
		request.setAttribute("search", pageInfo.getSearchedName());
		request.setAttribute("orderBy", pageInfo.getOrderBy());
		request.setAttribute("nbComputers", pageInfo.getNbComputers());
		request.setAttribute("nbPage", pageInfo.getNbPageTotal());
		request.setAttribute("listComputerDtos", this.getListComputerDto(pageInfo));

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}

	private List<ComputerDto> getListComputerDto(PageInfo pageInfo) {
		if (!"".equals(pageInfo.getSearchedName())) {
			List<Computer> listComputer = computerService.getListComputersByName(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
		} else {
			List<Computer> listComputer = computerService.getListComputers(pageInfo);
			return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());	
		}
	}
	
	private Long getNbComputers(Optional<String> searchedName) {
		if (!searchedName.isPresent() || "".equals(searchedName.get())) {
			return computerService.getNbComputers();
		} else {
			return computerService.getNbComputersByName(searchedName.get());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoPost");
		doGet(request, response);
	}
}
