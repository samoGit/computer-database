package com.excilys.cdb.model;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ComputerDto {
	private String id;
	private String name;
	private String dateIntroduced;
	private String dateDiscontinued;
	private String companyId;
	private String companyName;

	public ComputerDto() {
		this.id = "";
		this.name = "";
		this.dateIntroduced = "";
		this.dateDiscontinued = "";
		this.companyId = "";
		this.companyName = "";
	}

	public ComputerDto(Computer computer) {
		this.id = computer.getId().toString();
		this.name = computer.getName();

		this.dateIntroduced = "";
		if (computer.getDateIntroduced() != null) {
			this.dateIntroduced = computer.getDateIntroduced().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		this.dateDiscontinued = "";
		if (computer.getDateDiscontinued() != null) {
			this.dateDiscontinued = computer.getDateDiscontinued()
					.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		this.companyId = "";
		this.companyName = "";
		if (computer.getCompany() != null) {
			this.companyId = computer.getCompany().getId().toString();
			this.companyName = computer.getCompany().getName();
		}
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setId(Optional<String> id) {
		if (id.isPresent()) {
			this.id = id.get();
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setName(Optional<String> name) {
		if (name.isPresent()) {
			this.name = name.get();
		}
	}
	
	public String getDateIntroduced() {
		return dateIntroduced;
	}
	public void setDateIntroduced(String dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
	}
	public void setDateIntroduced(Optional<String> dateIntroduced) {
		if (dateIntroduced.isPresent()) {
			this.dateIntroduced = dateIntroduced.get();
		}
	}

	public String getDateDiscontinued() {
		return dateDiscontinued;
	}
	public void setDateDiscontinued(String dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
	}
	public void setDateDiscontinued(Optional<String> dateDiscontinued) {
		if (dateDiscontinued.isPresent()) {
			this.dateDiscontinued = dateDiscontinued.get();
		}
	}

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public void setCompanyId(Optional<String> companyId) {
		if (companyId.isPresent()) {
			this.companyId = companyId.get();
		}
	}

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setCompanyName(Optional<String> companyName) {
		if (companyName.isPresent()) {
			this.companyName = companyName.get();
		}
	}	
}
