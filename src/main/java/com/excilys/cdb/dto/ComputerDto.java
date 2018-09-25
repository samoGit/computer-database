package com.excilys.cdb.dto;

import java.time.format.DateTimeFormatter;

import com.excilys.cdb.model.Computer;

public class ComputerDto {
	private Long id;
	private String name;
	private String dateIntroduced;
	private String dateDiscontinued;
	private String companyName;

	public ComputerDto() {
	}
	public ComputerDto(Computer computer) {
		this.id = computer.getId();
		this.name = computer.getName();
		this.dateIntroduced = "";
		if (computer.getDateIntroduced().isPresent())
			this.dateIntroduced = computer.getDateIntroduced().get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.dateDiscontinued = "";
		if (computer.getDateDiscontinued().isPresent())
			this.dateDiscontinued = computer.getDateDiscontinued().get().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		this.companyName = "";
		if (computer.getCompany().isPresent())
			this.companyName = computer.getCompany().get().getName();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDateIntroduced() {
		return dateIntroduced;
	}
	public void setDateIntroduced(String dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
	}
	public String getDateDiscontinued() {
		return dateDiscontinued;
	}
	public void setDateDiscontinued(String dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "ComputerDto [id=" + id + ", name=" + name + ", dateIntroduced=" + dateIntroduced + ", dateDiscontinued="
				+ dateDiscontinued + ", companyName=" + companyName + "]";
	}
}
