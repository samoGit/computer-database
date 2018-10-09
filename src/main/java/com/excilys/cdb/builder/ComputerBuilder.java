package com.excilys.cdb.builder;

import java.time.LocalDate;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class ComputerBuilder {
	private Long id;
	private String name;
	private Optional<LocalDate> dateIntroduced;
	private Optional<LocalDate> dateDiscontinued;
	private Optional<Company> company;
	
	private ComputerBuilder() {
		this.dateIntroduced = Optional.empty();
		this.dateDiscontinued = Optional.empty();
		this.company = Optional.empty();
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
	
	public ComputerBuilder withDateIntroduced(Optional<LocalDate> dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
		return this;
	}
	
	public ComputerBuilder withDateDiscontinued(Optional<LocalDate> dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
		return this;
	}
	
	public ComputerBuilder withCompany(Optional<Company> company) {
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
