package com.excilys.cdb.builder;

import java.time.LocalDate;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerBuilder {
	private Long id;
	private String name;
	private LocalDate dateIntroduced;
	private LocalDate dateDiscontinued;
	private Company company;
	
	private ComputerBuilder() {
	}
	
	public static ComputerBuilder newComputerBuilder() {
		return new ComputerBuilder();
	}
	
	public ComputerBuilder withId(Long id) {
		this.id = id;
		return this;
	}
	
	public ComputerBuilder withName(String name) {
		this.name = name;
		return this;
	}
	
	public ComputerBuilder withDateIntroduced(LocalDate dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
		return this;
	}
	
	public ComputerBuilder withDateDiscontinued(LocalDate dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
		return this;
	}
	
	public ComputerBuilder withCompany(Company company) {
		this.company = company;
		return this;
	}
	
	public Computer buildComputer() {
		Computer computer = new Computer();
		computer.setId(id);
		computer.setName(name);
		computer.setDateIntroduced(dateIntroduced);
		computer.setDateDiscontinued(dateDiscontinued);
		computer.setCompany(company);
		return computer;
	}
}
