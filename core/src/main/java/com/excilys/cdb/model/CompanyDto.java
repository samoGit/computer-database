package com.excilys.cdb.model;

import java.util.Optional;

public class CompanyDto {
	private String id;
	private String name;

	public CompanyDto() {
		this.id = "";
		this.name = "";
	}

	public CompanyDto(Company company) {
		this.id = company.getId().toString();
		this.name = company.getName();
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
}
