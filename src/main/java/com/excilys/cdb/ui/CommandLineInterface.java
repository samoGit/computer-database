package com.excilys.cdb.ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.dto.ComputerDto;
import com.excilys.cdb.mapper.ComputerMapper;
import com.excilys.cdb.mapper.InvalidComputerException;
import com.excilys.cdb.mapper.InvalidDateException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * Display information in the terminal and manages interactions with the user.
 * 
 * @author samy
 */
@Component
public class CommandLineInterface {
	
	@Autowired
	private CompanyService companyService;
	@Autowired
	private ComputerService computerService;
	@Autowired
	private ComputerMapper computerMapper;
		
	private Scanner scanner;

	/**
	 * Constructor, init services.
	 */
	public CommandLineInterface() {
		scanner = new Scanner(System.in);
	}

	@PostConstruct
	public void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		// Do not work : computerService is still null...
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
		PageInfo pageInfo = new PageInfo(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), computerService.getNbComputers());
		boolean stop = false;
		while (!stop) {
			List<Computer> listComputers = computerService.getListComputers(pageInfo);
			if (listComputers.isEmpty()) {
				System.out.println("No computers found.");
			} else {
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
					pageInfo.setPageNumber(pageInfo.getPageNumber()+1);
					break;
				case PREVIOUS_PAGE:
					pageInfo.setPageNumber(pageInfo.getPageNumber()-1);
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
		System.out.println(
				"/--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\\");
		ComputerDto computerDtoTableHeader = new ComputerDto();
		computerDtoTableHeader.setId("id");
		computerDtoTableHeader.setName("name");
		computerDtoTableHeader.setDateIntroduced("date introduced");
		computerDtoTableHeader.setDateDiscontinued("date discontinued");
		computerDtoTableHeader.setCompanyName("company");
		this.displayRowComputer(computerDtoTableHeader);
		System.out.println(
				"|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		for (Computer computer : listComputers) {
			ComputerDto computerDto = new ComputerDto();
			computerDto.setId(String.valueOf(computer.getId()));
			computerDto.setName(String.valueOf(computer.getName()));
			computerDto.setDateIntroduced(String
					.valueOf(computer.getDateIntroduced().isPresent() ? computer.getDateIntroduced().get() : "?"));
			computerDto.setDateDiscontinued(String
					.valueOf(computer.getDateDiscontinued().isPresent() ? computer.getDateDiscontinued().get() : "?"));
			computerDto.setCompanyName(
					String.valueOf(computer.getCompany().isPresent() ? computer.getCompany().get().getName() : "?"));

			this.displayRowComputer(computerDto);
		}
		System.out.println(
				"\\--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------/");
	}

	/**
	 * Display a row in the table of computers.
	 */
	private void displayRowComputer(ComputerDto computerDto) {
		System.out.println("| " + String.format("%1$8s", computerDto.getId()) + " | "
				+ String.format("%1$70s", computerDto.getName()) + " | "
				+ String.format("%1$20s", computerDto.getDateIntroduced()) + " | "
				+ String.format("%1$20s", computerDto.getDateDiscontinued()) + " | "
				+ String.format("%1$50s", computerDto.getCompanyName()) + " |");
	}

	/**
	 * Launch the menu which allows the user to select a computer (with its name)
	 * and displays all the information known about this computer
	 */
	protected void launchMenuShowDetailComputer() {
		System.out.println("\n\nPlease enter the name of a computer : ");
		String name = scanner.nextLine();
		PageInfo pageInfo = new PageInfo(Optional.empty(), Optional.empty(), Optional.of(name), Optional.empty(), computerService.getNbComputers());
		List<Computer> listComputersFound = computerService.getListComputersByName(pageInfo);
		if (listComputersFound.isEmpty()) {
			System.out.println("The computer '" + name + "' is not found.");
		} else {
			this.displayTableComputers(listComputersFound);
		}
	}

	/**
	 * Ask the user to enter a date with the format "dd/MM/yyyy" or "?"
	 * 
	 * @param message String The text to be display until the user enter a date with
	 *                the expected format (or "?")
	 * @return A LocalDate object or Optional.empty() if the user enter "?"
	 */
	private Optional<String> getDateFromUser(String message) {
		Optional<String> date = Optional.empty();

		boolean dateFormatIsOk = false;
		while (!dateFormatIsOk) {
			System.out.println(message);
			String strDate = scanner.nextLine();
			try {
				LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				dateFormatIsOk = true;
				date = Optional.ofNullable(strDate);
			} catch (Exception e) {
				if (!strDate.equals("?")) {
					System.out.println("invalid date format.");
				} else
					dateFormatIsOk = true;
			}
		}

		return date;
	}

	/**
	 * Ask the user to enter a CompanyId or "?"
	 * 
	 * @param message String The text to be display until the user enter a CompnayId
	 *                (or "?")
	 * @return {@link Company}
	 */
	private Optional<String> getCompanyIdFromUser() {
		boolean companyUnknown = false;
		List<String> listCompanyId = companyService.getListCompanies().stream().map(c -> c.getId().toString())
				.collect(Collectors.toList());
		while (!companyUnknown) {
			this.displayAllCompanies();
			System.out.println("Please enter a company id (or '?') : ");
			String strCompanyId = scanner.nextLine();
			if (strCompanyId.equals("?"))
				companyUnknown = true;

			if (listCompanyId.contains(strCompanyId))
				return Optional.of(strCompanyId);
		}

		return Optional.empty();
	}

	/**
	 * Launch the menu which allows the user to create a new computer
	 */
	protected void launchMenuCreateComputer() {
		System.out.println("\nName : ");
		ComputerDto computerDto = new ComputerDto();
		computerDto.setName(Optional.ofNullable(scanner.nextLine()));
		computerDto.setDateIntroduced(this.getDateFromUser(
				"\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when introduced : "));
		computerDto.setDateDiscontinued(this.getDateFromUser(
				"\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when discontinued : "));
		computerDto.setCompanyId(this.getCompanyIdFromUser());

		try {
			computerService.createNewComputer(computerMapper.getComputer(computerDto));
		} catch (InvalidComputerException | InvalidDateException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Launch the menu which allows the user to choose a computer
	 * 
	 * @return {@link Computer}
	 */
	private Optional<Computer> launchMenuChooseComputer() {
		System.out.println("\nEnter the name of the computer : ");
		String name = scanner.nextLine();

		PageInfo pageInfo = new PageInfo(Optional.empty(), Optional.empty(), Optional.of(name), Optional.empty(), computerService.getNbComputers());
		List<Computer> listComputersFound = computerService.getListComputersByName(pageInfo);

		if (listComputersFound.isEmpty()) {
			System.out.println("No computer found with this name.");
		} else if (listComputersFound.size() == 1) {
			return Optional.ofNullable(listComputersFound.get(0));
		} else {
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
			computerService.deleteComputer(computerToBeDeleted.get().getId().toString());
	}

	/**
	 * Launch the menu which allows the user to update a computer
	 */
	protected void launchMenuUpdateComputer() {
		Optional<Computer> computerToBeUpdate = this.launchMenuChooseComputer();
		if (!computerToBeUpdate.isPresent())
			return;

		String field = "";
		boolean fieldIsOK = false;
		while (!fieldIsOK) {
			System.out.println("What field do you want to update ('name', 'introduced', 'discontinued', 'company')");
			field = scanner.nextLine();

			if (field.equals("id"))
				System.out.println("You can not update this field.");
			else if (field.equals("name") || field.equals("introduced") || field.equals("discontinued")
					|| field.equals("company"))
				fieldIsOK = true;
			else
				System.out.println("This field do not exist.");
		}

		if (field.equals("name")) {
			System.out.println("Enter the new name : ");
			String newName = scanner.nextLine();
			computerToBeUpdate.get().setName(newName);
		} else if (field.equals("introduced")) {
			Optional<String> dateIntroduced = this.getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateIntroducedFromString(dateIntroduced);
		} else if (field.equals("discontinued")) {
			Optional<String> dateDiscontinued = this.getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateDiscontinuedFromString(dateDiscontinued);
		} else if (field.equals("company")) {
			field = "company_id";
			Optional<String> companyId = this.getCompanyIdFromUser();
			Optional<Company> company = Optional.empty();
			if (companyId.isPresent()) {
				company = companyService.getCompanyFromId(Long.valueOf(companyId.get()));
			}
			computerToBeUpdate.get().setCompany(company);
		}

		computerService.updateComputer(computerToBeUpdate.get(), field);
	}

	/**
	 * Launch the menu which allows the user to delete a company AND all the computer linked to it.
	 */
	protected void launchMenuDeleteCompany() {
		Optional<String> companyToBeDeleted = this.getCompanyIdFromUser();
		if (companyToBeDeleted.isPresent())
			companyService.deleteCompany(Long.valueOf(companyToBeDeleted.get()));
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
				case DELETE_COMPANY:
					this.launchMenuDeleteCompany();
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
