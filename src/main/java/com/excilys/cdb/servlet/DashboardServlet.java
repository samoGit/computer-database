package com.excilys.cdb.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.service.ComputerPageService;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class HomeServlet
 */
@WebServlet("/Dashboard")
public class DashboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1182886835641082355L;

	private final ComputerService computerService = ComputerService.INSTANCE;
	private final Logger logger = LoggerFactory.getLogger("DashboardServlet");

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("\n\ndoGet");

		Optional<String> orderBy = Optional.ofNullable(request.getParameter("orderBy"));
		request.setAttribute("orderBy", orderBy.isPresent() ? orderBy.get() : "");

		Long nbComputersByPage = ComputerPageService
				.getNbComputersByPage(Optional.ofNullable(request.getParameter("nbComputersByPage")));
		request.setAttribute("nbComputersByPage", nbComputersByPage);

		Optional<String> strSearch = Optional.ofNullable(request.getParameter("search"));
		request.setAttribute("search", strSearch.isPresent() ? strSearch.get() : "");

		Long nbComputers = this.getNbComputer(strSearch);
		request.setAttribute("nbComputers", nbComputers);

		Long nbPageTotal = ComputerPageService.getNbPageTotal(nbComputersByPage, nbComputers);
		request.setAttribute("nbPage", nbPageTotal);

		Long pageNumber = ComputerPageService.getPageNumber(Optional.ofNullable(request.getParameter("pageNumber")),
				nbComputersByPage, nbComputers, nbPageTotal);
		request.setAttribute("pageNumber", pageNumber);

		List<ComputerDto> listComputerDto = this.getListComputerDto(pageNumber, nbComputersByPage, strSearch, orderBy);
		request.setAttribute("listComputerDtos", listComputerDto);

		this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
	}

	private Long getNbComputer(Optional<String> strSearch) {
		if (!strSearch.isPresent() || "".equals(strSearch.get())) {
			return computerService.getNbComputers();
		} else {
			return computerService.getNbComputersByName(strSearch.get());
		}
	}

	private List<ComputerDto> getListComputerDto(Long pageNumber, Long nbComputersByPage, Optional<String> strSearch, Optional<String> orderBy) {
		if (strSearch.isPresent() && !"".equals(strSearch.get())) {
			return ComputerPageService.getListComputerDtosByName(pageNumber, nbComputersByPage,
					strSearch.get(), orderBy);
		} else {
			return ComputerPageService.getListComputerDtos(pageNumber, nbComputersByPage, orderBy);
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
