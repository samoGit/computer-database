package com.excilys.cdb.model;

public class PageInfo {

	public final static Long DEFAULT_NB_COMPUTERS_BY_PAGE = 10L;

	private Long pageNumber;
	private Long nbComputersByPage;
	private Long offset;
	private Long nbPageTotal;
	private Long nbComputers;
	private String searchedName;
	private String orderBy;

	public PageInfo(String pageNumber, String nbComputersByPage, String searchedName, String orderBy, Long nbComputers) {
		this.nbComputersByPage = getNbComputersByPage(nbComputersByPage);
		this.nbComputers = nbComputers;
		this.nbPageTotal = getNbPageTotal(this.nbComputersByPage, this.nbComputers);
		this.pageNumber = getPageNumber(pageNumber, this.nbPageTotal);
		this.offset = (this.pageNumber - 1) * this.nbComputersByPage;
		this.searchedName = searchedName;
		this.orderBy = orderBy;
	}
	
	private Long getNbComputersByPage(String nbComputersByPage) {
		if (!"".equals(nbComputersByPage)) {
			return Long.valueOf(nbComputersByPage);
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
	
	private Long getPageNumber(String strPageNumber, Long nbPageTotal) {
		Long pageNumber = 1L;
		if (!"".equals(strPageNumber)) {
			if ("lastPage".equals(strPageNumber)) {
				pageNumber = nbPageTotal;
			} else {
				pageNumber = Long.valueOf(strPageNumber);
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

	@Override
	public String toString() {
		return "PageInfo [pageNumber=" + pageNumber + ", nbComputersByPage=" + nbComputersByPage + ", offset=" + offset
				+ ", nbPageTotal=" + nbPageTotal + ", nbComputers=" + nbComputers + ", searchedName=" + searchedName
				+ ", orderBy=" + orderBy + "]";
	}
}
