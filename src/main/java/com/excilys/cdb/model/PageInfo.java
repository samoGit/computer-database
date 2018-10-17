package com.excilys.cdb.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excilys.cdb.service.ComputerService;

//@Component
public class PageInfo {

	public final static Long DEFAULT_NB_COMPUTERS_BY_PAGE = 10L;

//	@Autowired
//	private ComputerService computerService;

	private Long pageNumber;
	private Long nbComputersByPage;
	private Long offset;
	private Long nbPageTotal;
	private Long nbComputers;
	private String searchedName;
	private String orderBy;

	public PageInfo(Optional<String> pageNumber, Optional<String> nbComputersByPage, Optional<String> searchedName,
			Optional<String> orderBy, Long nbComputers) {
		this.nbComputersByPage = getNbComputersByPage(nbComputersByPage);
		this.nbComputers = nbComputers;
		this.nbPageTotal = getNbPageTotal(this.nbComputersByPage, this.nbComputers);
		this.pageNumber = getPageNumber(pageNumber, this.nbPageTotal);
		this.offset = (this.pageNumber - 1) * this.nbComputersByPage;
		
		this.searchedName = searchedName.isPresent() ? searchedName.get() : "";
		this.orderBy = orderBy.isPresent() ? orderBy.get() : "";
	}
	
	private Long getNbComputersByPage(Optional<String> nbComputersByPage) {
		if (nbComputersByPage.isPresent()) {
			return Long.valueOf(nbComputersByPage.get());
		}
		else {
			return DEFAULT_NB_COMPUTERS_BY_PAGE;
		}
	}

	private Long getNbPageTotal(Long nbComputersByPage, Long nbComputers) {
		Long nbPageTotal = nbComputers / nbComputersByPage;
		if (nbComputers % nbComputersByPage != 0) {
			nbPageTotal++;
		}
		return nbPageTotal;
	}
	
	private Long getPageNumber(Optional<String> strPageNumber, Long nbPageTotal) {
		Long pageNumber = 1L;
		if (strPageNumber.isPresent()) {
			if ("lastPage".equals(strPageNumber.get())) {
				pageNumber = nbPageTotal;
			} else {
				pageNumber = Long.valueOf(strPageNumber.get());
				if (pageNumber < 1L) {
					pageNumber = 1L;
				} else if (pageNumber > nbPageTotal) {
					pageNumber = nbPageTotal;
				}
			}
		}
		return pageNumber;
	}
	
	public Long getPageNumber() {
		return pageNumber;
	}
	public Long getNbComputersByPage() {
		return nbComputersByPage;
	}
	public Long getOffset() {
		return offset;
	}
	public Long getNbPageTotal() {
		return nbPageTotal;
	}
	public Long getNbComputers() {
		return nbComputers;
	}
	public String getSearchedName() {
		return searchedName;
	}
	public String getOrderBy() {
		return orderBy;
	}
	
	public void setPageNumber(Long pageNumber) {
		this.pageNumber = pageNumber;
		if (this.pageNumber < 1L) {
			this.pageNumber = this.nbPageTotal;
		}
		else if (this.pageNumber > this.nbPageTotal) {
			this.pageNumber = 1L;
		}
		this.offset = (this.pageNumber - 1) * this.nbComputersByPage;
	}
}
