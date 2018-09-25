package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;


/**
 * Display information in the terminal and manages interactions with the user.
 * 
 * @author samy
 */
public class CommandLineInterface {

	public static final Long NB_COMPUTERS_BY_PAGE=Long.valueOf(20);

	private CompanyService companyService;
	private ComputerService computerService;

	/**
     * Manage interaction with the user.
     */
	private Scanner scanner;

    /**
     * Constructor, init services.
     */
	public CommandLineInterface() {
		companyService = CompanyService.INSTANCE;
		computerService = ComputerService.INSTANCE;
    	scanner = new Scanner(System.in);
	}

    /**
     * Display info about all companies.
     */
	protected void displayAllCompanies() {
    	List<Company> listCompanies = companyService.getListCompanies();
    	System.out.println("\nList of companies : ");
		System.out.println("/---------------------------------------------------------------\\");
		System.out.println("|       id |                                               name |");
		System.out.println("|---------------------------------------------------------------|");
    	for (Company c : listCompanies) {
    		String strId = String.valueOf(c.getId());
    		String strName = String.valueOf(c.getName());
    		System.out.println("| " + String.format("%1$8s", strId) + " | " + String.format("%1$50s", strName) + " |");
    	}
		System.out.println("\\---------------------------------------------------------------/");
	}

	/**
     * Display info about all computers.
     */
	protected void displayAllComputers() {
		Long nbComputers = computerService.getNbComputers();

		Long offset = nbComputers - NB_COMPUTERS_BY_PAGE;
		boolean stop = false;
		while (!stop) {
	    	List<Computer> listComputers = computerService.getListComputers(offset, NB_COMPUTERS_BY_PAGE);
			if (listComputers.isEmpty()) {
				System.out.println("No computers found.");
			}
			else {
				this.displayTableComputers(listComputers);
			}

			System.out.println("\n\nWhat do you want to do ?");
			Integer minValue = Integer.MAX_VALUE;
			Integer maxValue = Integer.MIN_VALUE;
			for (UserChoicePage userChoicePage : UserChoicePage.values()) {
				if (minValue > userChoicePage.getValue()) {
					minValue = userChoicePage.getValue();
				}
				if (maxValue < userChoicePage.getValue()) {
					maxValue = userChoicePage.getValue();
				}

				System.out.println("\t" + userChoicePage.getValue() + ") " + userChoicePage.getMessage());				
			}
			System.out.print("Please enter a number between " + minValue + " and " + maxValue + " : ");
			String strChoice = scanner.nextLine();

			Optional<UserChoicePage> userChoice = UserChoicePage.fromString(strChoice);
			if (userChoice.isPresent()) {
				switch (userChoice.get()) {
				case NEXT_PAGE:
					offset += NB_COMPUTERS_BY_PAGE;
					if (offset >= nbComputers) {
						offset = Long.valueOf(0);
					}
					break;
				case PREVIOUS_PAGE:
					offset -= NB_COMPUTERS_BY_PAGE;
					if (offset < 0) {
						offset = nbComputers - NB_COMPUTERS_BY_PAGE;
					}
					break;
				case BACK_TO_MENU:
					stop = true;
					break;
				}
			}
		}
	}

	/**
     * Display all the given computer in a table
     */
	private void displayTableComputers(List<Computer> listComputers) {
 	    System.out.println("/--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\\");
		this.displayRowComputer("id", "name", "date introduced", "date discontinued", "company");
 	    System.out.println("|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");		
    	for (Computer computer : listComputers) {
    		String strId = String.valueOf(computer.getId());
    		String strName = String.valueOf(computer.getName()); 
    		String strIntroduced = String.valueOf( computer.getDateIntroduced().isPresent() ? computer.getDateIntroduced().get() : "?" );
    		String strDiscontinued = String.valueOf( computer.getDateDiscontinued().isPresent() ? computer.getDateDiscontinued().get() : "?");
    		String strCompanyName = String.valueOf( computer.getCompany().isPresent() ? computer.getCompany().get().getName() : "?");

    		this.displayRowComputer(strId, strName, strIntroduced, strDiscontinued, strCompanyName);
    	}
 	    System.out.println("\\--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------/");
	}
	
	/**
     * Display a row in the table of computers.
     */
	private void displayRowComputer(String strId, String strName, String strIntroduced, String strDiscontinued, String strCompanyName) {
		System.out.println(	 "| " + String.format("%1$8s", strId) + 
							" | " + String.format("%1$70s", strName) + 
							" | " + String.format("%1$20s", strIntroduced) + 
							" | " + String.format("%1$20s", strDiscontinued) +		
							" | " + String.format("%1$50s", strCompanyName) + " |");		
	}
	
	/**
     * Launch the menu which allows the user to select a computer (with its name) and displays all the information known about this computer
     */ 
	protected void launchMenuShowDetailComputer() {
		System.out.println("\n\nPlease enter the name of a computer : ");
		String name = scanner.nextLine();
		List<Computer> listComputersFound = computerService.getListComputersByName(name);
		if (listComputersFound.isEmpty()) {
			System.out.println("The computer '" + name + "' is not found.");
		}
		else {
			this.displayTableComputers(listComputersFound);
		}
	}

	/**
     * Ask the user to enter a date with the format "dd/MM/yyyy" or "?"
     * 
     * @param message String The text to be display until the user enter a date with the expected format (or "?")
     * @return A LocalDate object or Optional.empty() if the user enter "?"
     */ 
	private Optional<LocalDate> getDateFromUser(String message) {		
		Optional<LocalDate> date = Optional.empty();

		boolean dateFormatIsOk = false;
		while (!dateFormatIsOk) {
			System.out.println(message);
			String strDate = scanner.nextLine();
			try {
				LocalDate localDate = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				dateFormatIsOk = true;
				date = Optional.ofNullable(localDate);
			}
			catch (Exception e) {
				if (!strDate.equals("?")) {
					System.out.println("invalid date format.");
				}
				else
					dateFormatIsOk = true;					
			}
		}

		return date;
	}

	/**
     * Ask the user to enter a CompanyId or "?"
     * 
     * @param message String The text to be display until the user enter a CompnayId (or "?")
     * @return {@link Company} 
     */ 
	private Optional<Company> getCompanyFromUser() {		
		boolean companyUnknown = false;
		while (!companyUnknown) {
			this.displayAllCompanies();
			System.out.println("Please enter a company id (or '?') : ");
			String strCompanyId = scanner.nextLine();
			if (strCompanyId.equals("?"))
				companyUnknown = true;

			for (Company c : companyService.getListCompanies()) {
				if (c.getId().toString().equals(strCompanyId))
					return Optional.ofNullable(c);					
			}
		}
		
		return Optional.empty();
	}

	/**
     * Launch the menu which allows the user to create a new computer
     */ 
	protected void launchMenuCreateComputer() {
		System.out.println("\nName : ");
		String newComputerName = scanner.nextLine();
	 	Optional<LocalDate> dateIntroduced = this.getDateFromUser("\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when introduced : ");
	 	Optional<LocalDate> dateDiscontinued = this.getDateFromUser("\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when discontinued : ");
	 	Optional<Company> companyNewComputer = this.getCompanyFromUser();
		computerService.CreateNewComputer(new Computer(Long.valueOf(-1), newComputerName, dateIntroduced, dateDiscontinued, companyNewComputer));		
	}
	
	/**
     * Launch the menu which allows the user to choose a computer
	 * 
	 * @return {@link Computer}
	 */
	private Optional<Computer> launchMenuChooseComputer() {
		System.out.println("\nEnter the name of the computer : ");
		String name = scanner.nextLine();
		
		List<Computer> listComputersFound = computerService.getListComputersByName(name);
		
		if (listComputersFound.isEmpty()) {
			System.out.println("No computer found with this name.");			
		}
		else if (listComputersFound.size() == 1) {
			return Optional.ofNullable(listComputersFound.get(0));
		}
		else {
			this.displayTableComputers(listComputersFound);
			System.out.println("Multiple computer have the same name, please enter the id of the computer : ");
			String strID = scanner.nextLine();
			
			for (Computer computer : listComputersFound) {
				if (computer.getId().toString().equals(strID)) {
					return Optional.ofNullable(computer);
				}
			}
		}
		
		return Optional.empty();
	}
	
	/**
     * Launch the menu which allows the user to delete a computer
     */ 
	protected void launchMenuDeleteComputer() {
		Optional<Computer> computerToBeDeleted = this.launchMenuChooseComputer();
		if (computerToBeDeleted.isPresent())
			computerService.DeleteComputer(computerToBeDeleted.get().getId());
	}
	
	/**
     * Launch the menu which allows the user to update a computer
     */ 
	protected void launchMenuUpdateComputer() {
		Optional<Computer> computerToBeUpdate = this.launchMenuChooseComputer();
		if (!computerToBeUpdate.isPresent())
			return ;

		String field = "";
		boolean fieldIsOK = false;
		while (!fieldIsOK) {
			System.out.println("What field do you want to update ('name', 'introduced', 'discontinued', 'company')");
			field = scanner.nextLine();
			
			if (field.equals("id"))
				System.out.println("You can not update this field.");
			else if (field.equals("name")  || field.equals("introduced")  || field.equals("discontinued")  || field.equals("company"))
				fieldIsOK = true;
			else
				System.out.println("This field do not exist.");
		}

		if (field.equals("name")) {
			System.out.println("Enter the new name : ");
			String newName = scanner.nextLine();
			computerToBeUpdate.get().setName(newName);
		}
		else if (field.equals("introduced")){
			Optional<LocalDate> dateIntroduced = this.getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateIntroduced(dateIntroduced);
		}
		else if (field.equals("discontinued")){
			Optional<LocalDate> dateDiscontinued = this.getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateDiscontinued(dateDiscontinued);
		}
		else if (field.equals("company")) {
			field = "company_id";
		 	Optional<Company> company = this.getCompanyFromUser();
			computerToBeUpdate.get().setCompany(company);
		}

		computerService.UpdateComputer(computerToBeUpdate.get(), field);
	}

	/**
     * Launch the main menu.
     */
	public void launchMainMenu() {
		boolean stop = false;
		while (!stop) {
			System.out.println("\n\nWhat do you want to do ?");
			Integer minValue = Integer.MAX_VALUE;
			Integer maxValue = Integer.MIN_VALUE;
			for (UserChoiceMain userChoiceMain : UserChoiceMain.values()) {
				if (minValue > userChoiceMain.getValue())
					minValue = userChoiceMain.getValue();
				if (maxValue < userChoiceMain.getValue())
					maxValue = userChoiceMain.getValue();

				System.out.println("\t" + userChoiceMain.getValue() + ") " + userChoiceMain.getMessage());				
			}
			System.out.print("Please enter a number between " + minValue + " and " + maxValue + " : ");
			String strChoice = scanner.nextLine();
			
			Optional<UserChoiceMain> userChoice = UserChoiceMain.fromString(strChoice);
			if (userChoice.isPresent()) {
				switch (userChoice.get()) {
				case DISPLAY_COMPUTERS:
					this.displayAllComputers();
					break;
				case DISPLAY_COMPANIES:
					this.displayAllCompanies();
					break;
				case SHOW_COMPUTER_DETAILS:
					this.launchMenuShowDetailComputer();
					break;
				case CREATE_COMPUTER:
					this.launchMenuCreateComputer();
					break;
				case UPDATE_COMPUTER:
					this.launchMenuUpdateComputer();
					break;
				case DELETE_COMPUTER:
					this.launchMenuDeleteComputer();
					break;
				case QUIT:
					stop = true;
					break;
				}
			}
		
		}
	}

	/**
     * Entry point of the application.
     * 
     * @param args String[] not used
     */
	public static void main(String[] args) {
		System.out.println("Hello !");
    	CommandLineInterface CLI = new CommandLineInterface();
    	CLI.launchMainMenu();
		System.out.println("Goodbye !");
	}
}
