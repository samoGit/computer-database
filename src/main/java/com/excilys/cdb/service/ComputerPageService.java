package com.excilys.cdb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.model.Computer;

public class ComputerPageService {

	private final Long NbComputersByPageDefaultValue = 10L;
	
	private ComputerService computerService = ComputerService.INSTANCE;
	private Long nbComputersByPage;
	private Long nbComputers;
	private Long nbPageTotal;
	private Long pageNumber;
	private List<ComputerDto> listComputerDtos;
	
	public ComputerPageService(Optional<String> strNbComputersByPage, Optional<String> strPageNumber) {
		nbComputersByPage = NbComputersByPageDefaultValue;
		if (strNbComputersByPage.isPresent() && !"".equals(strNbComputersByPage.get())) {
			nbComputersByPage = Long.valueOf(strNbComputersByPage.get());
		}
		
		nbComputers = computerService.getNbComputers();
		
        nbPageTotal = nbComputers/nbComputersByPage;
        if (nbComputers % nbComputersByPage != 0) {
        	nbPageTotal++;
        }
        
        if (!strPageNumber.isPresent() || "".equals(strPageNumber.get())) {
            pageNumber = 1L;
        }
        else if ("lastPage".equals(strPageNumber.get())) {
        	pageNumber = nbPageTotal;
        }
        else {
        	pageNumber = Long.valueOf(strPageNumber.get());        	
        }
        
        if (pageNumber < 1L) {
        	pageNumber = 1L;
        }
        else if (pageNumber > nbPageTotal) {
        	pageNumber = nbPageTotal;
        }

        List<Computer> listComputers = computerService.getListComputers((pageNumber-1)*nbComputersByPage, nbComputersByPage);
        listComputerDtos = new ArrayList<>();
        listComputers.forEach( (c) -> {
        	listComputerDtos.add(new ComputerDto(c));
        });
	}

	public Long getNbComputersByPage() {
		return nbComputersByPage;
	}
	public Long getNbComputers() {
		return nbComputers;
	}
	public Long getNbPageTotal() {
		return nbPageTotal;
	}
	public Long getPageNumber() {
		return pageNumber;
	}
	public List<ComputerDto> getListComputerDtos() {
		return listComputerDtos;
	}	
}
