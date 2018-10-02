package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;

public class ComputerPageService {
	private final static Long NbComputersByPageDefaultValue = 10L;

	private static ComputerService computerService = ComputerService.INSTANCE;

	public static Long getNbComputersByPage(Optional<String> strNbComputersByPage) {
		Long nbComputersByPage = NbComputersByPageDefaultValue;
		if (strNbComputersByPage.isPresent() && !"".equals(strNbComputersByPage.get())) {
			nbComputersByPage = Long.valueOf(strNbComputersByPage.get());
		}
		return nbComputersByPage;
	}
	
	public static Long getNbPageTotal(Long nbComputersByPage, Long nbComputers) {
		Long nbPageTotal = nbComputers / nbComputersByPage;
		if (nbComputers % nbComputersByPage != 0) {
			nbPageTotal++;
		}
		return nbPageTotal;
	}

	public static Long getPageNumber(Optional<String> strPageNumber, Long nbComputersByPage, Long nbComputers, Long nbPageTotal) {
		Long pageNumber;
		if (!strPageNumber.isPresent() || "".equals(strPageNumber.get())) {
			pageNumber = 1L;
		} else if ("lastPage".equals(strPageNumber.get())) {
			pageNumber = nbPageTotal;
		} else {
			pageNumber = Long.valueOf(strPageNumber.get());
			if (pageNumber < 1L) {
				pageNumber = 1L;
			} else if (pageNumber > nbPageTotal) {
				pageNumber = nbPageTotal;
			}
		}
		return pageNumber;
	}

	public static List<ComputerDto> getListComputerDtos(Long pageNumber, Long nbComputersByPage, Optional<String> orderBy) {
		Long ofSet = (pageNumber - 1) * nbComputersByPage;
		List<Computer> listComputer = computerService.getListComputers(ofSet, nbComputersByPage, orderBy);
		return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());	
	}

	public static List<ComputerDto> getListComputerDtosByName(Long pageNumber, Long nbComputersByPage,
			String searchedName, Optional<String> orderBy) {
		Long ofSet = (pageNumber - 1) * nbComputersByPage;
		List<Computer> listComputer = computerService.getListComputersByName(ofSet, nbComputersByPage, searchedName, orderBy);
		return listComputer.stream().map(c -> new ComputerDto(c)).collect(Collectors.toList());
	}
}
