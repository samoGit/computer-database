package com.excilys.cdb.model;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Contain data about a given computer.
 * 
 * @author samy
 */
public class Computer {
	private Long id;
	private String name;
	private Optional<LocalDate> dateIntroduced;
	private Optional<LocalDate> dateDiscontinued;
	private Optional<Company> company;

	/**
	 * Default constructor (should not be used "manually").
	 */
	public Computer() {
	}

	public Computer(Long id, String name, Optional<LocalDate> dateIntroduced, Optional<LocalDate> dateDiscontinued,
			Optional<Company> company) {
		this.id = id;
		this.name = name;
		this.dateIntroduced = dateIntroduced;
		this.dateDiscontinued = dateDiscontinued;
		this.company = company;
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
	public Optional<LocalDate> getDateIntroduced() {
		return dateIntroduced;
	}
	public void setDateIntroduced(Optional<LocalDate> dateIntroduced) {
		this.dateIntroduced = dateIntroduced;
	}
	public Optional<LocalDate> getDateDiscontinued() {
		return dateDiscontinued;
	}
	public void setDateDiscontinued(Optional<LocalDate> dateDiscontinued) {
		this.dateDiscontinued = dateDiscontinued;
	}
	public Optional<Company> getCompany() {
		return company;
	}
	public void setCompany(Optional<Company> company) {
		this.company = company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((dateDiscontinued == null) ? 0 : dateDiscontinued.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((dateIntroduced == null) ? 0 : dateIntroduced.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Computer other = (Computer) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (dateDiscontinued == null) {
			if (other.dateDiscontinued != null)
				return false;
		} else if (!dateDiscontinued.equals(other.dateDiscontinued))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (dateIntroduced == null) {
			if (other.dateIntroduced != null)
				return false;
		} else if (!dateIntroduced.equals(other.dateIntroduced))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Computer [id=" + id + ", name=" + name + ", dateIntroduced=" + dateIntroduced + ", dateDiscontinued=" + dateDiscontinued
				+ ", company=" + company + "]";
	}	
}
