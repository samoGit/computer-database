package com.excilys.cdb.console;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.cdb.config.CliAppConfig;
import com.excilys.cdb.binding.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDto;
import com.excilys.cdb.model.InvalidComputerException;
import com.excilys.cdb.model.InvalidDateException;
import com.excilys.cdb.model.PageInfo;
import com.excilys.cdb.persistence.DataBaseAccessException;
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

	private Scanner scanner = new Scanner(System.in);

	/**
	 * Display info about all companies.
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void displayAllCompanies() throws DataBaseAccessException {
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
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void displayAllComputers() throws DataBaseAccessException {
		PageInfo pageInfo = new PageInfo("", "", "", "", computerService.getNbComputers());
		boolean stop = false;
		while (!stop) {
			List<Computer> listComputers = computerService.getListComputers(pageInfo);
			if (listComputers.isEmpty()) {
				System.out.println("No computers found.");
			} else {
				displayTableComputers(listComputers);
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
					pageInfo.setPageNumber(pageInfo.getPageNumber() + 1);
					break;
				case PREVIOUS_PAGE:
					pageInfo.setPageNumber(pageInfo.getPageNumber() - 1);
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
		displayRowComputer(computerDtoTableHeader);
		System.out.println(
				"|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|");
		for (Computer computer : listComputers) {
			ComputerDto computerDto = new ComputerDto();
			computerDto.setId(String.valueOf(computer.getId()));
			computerDto.setName(String.valueOf(computer.getName()));
			computerDto.setDateIntroduced(
					String.valueOf(computer.getDateIntroduced() != null ? computer.getDateIntroduced() : "?"));
			computerDto.setDateDiscontinued(
					String.valueOf(computer.getDateDiscontinued() != null ? computer.getDateDiscontinued() : "?"));
			computerDto.setCompanyName(
					String.valueOf(computer.getCompany() != null ? computer.getCompany().getName() : "?"));

			displayRowComputer(computerDto);
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
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void launchMenuShowDetailComputer() throws DataBaseAccessException {
		System.out.println("\n\nPlease enter the name of a computer : ");
		String name = scanner.nextLine();
		PageInfo pageInfo = new PageInfo("", "", name, "", computerService.getNbComputers());
		List<Computer> listComputersFound = computerService.getListComputersByName(pageInfo);
		if (listComputersFound.isEmpty()) {
			System.out.println("The computer '" + name + "' is not found.");
		} else {
			displayTableComputers(listComputersFound);
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
	 * @throws DataBaseAccessException
	 */
	private Optional<String> getCompanyIdFromUser() throws DataBaseAccessException {
		boolean companyUnknown = false;
		List<String> listCompanyId = companyService.getListCompanies().stream().map(c -> c.getId().toString())
				.collect(Collectors.toList());
		while (!companyUnknown) {
			displayAllCompanies();
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
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void launchMenuCreateComputer() throws DataBaseAccessException {
		System.out.println("\nName : ");
		ComputerDto computerDto = new ComputerDto();
		computerDto.setName(Optional.ofNullable(scanner.nextLine()));
		computerDto.setDateIntroduced(
				getDateFromUser("\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when introduced : "));
		computerDto.setDateDiscontinued(getDateFromUser(
				"\n(Expected format = 'DD/MM/YYYY'    or    '?' if unknown)\nDate when discontinued : "));
		computerDto.setCompanyId(getCompanyIdFromUser());

		try {
			Computer computerToBeCreated = computerMapper.getComputerWithNoCompany(computerDto);
			if (!"".equals(computerDto.getCompanyId())) {
				try {
					Company company = companyService.getCompanyFromId(Long.valueOf(computerDto.getCompanyId()));
					if (company != null) {
						computerToBeCreated.setCompany(company);
					}
					else {
						throw new InvalidComputerException("label.invalidComputer.noCompany");
					}
				} catch (NumberFormatException numberFormatException) {
					throw new InvalidComputerException("label.invalidComputer.noCompany");
				}
			}
			computerService.createNewComputer(computerToBeCreated);
		} catch (InvalidComputerException | InvalidDateException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Launch the menu which allows the user to choose a computer
	 * 
	 * @return {@link Computer}
	 * @throws DataBaseAccessException
	 */
	private Optional<Computer> launchMenuChooseComputer() throws DataBaseAccessException {
		System.out.println("\nEnter the name of the computer : ");
		String name = scanner.nextLine();

		PageInfo pageInfo = new PageInfo("", "", name, "", computerService.getNbComputers());
		List<Computer> listComputersFound = computerService.getListComputersByName(pageInfo);

		if (listComputersFound.isEmpty()) {
			System.out.println("No computer found with this name.");
		} else if (listComputersFound.size() == 1) {
			return Optional.ofNullable(listComputersFound.get(0));
		} else {
			displayTableComputers(listComputersFound);
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
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void launchMenuDeleteComputer() throws DataBaseAccessException {
		Optional<Computer> computerToBeDeleted = launchMenuChooseComputer();
		if (computerToBeDeleted.isPresent())
			computerService.deleteComputer(computerToBeDeleted.get().getId().toString());
	}

	/**
	 * Launch the menu which allows the user to update a computer
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void launchMenuUpdateComputer() throws DataBaseAccessException {
		Optional<Computer> computerToBeUpdate = launchMenuChooseComputer();
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
			Optional<String> dateIntroduced = getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateIntroducedFromString(dateIntroduced);
		} else if (field.equals("discontinued")) {
			Optional<String> dateDiscontinued = getDateFromUser("Enter the new date : ");
			computerToBeUpdate.get().setDateDiscontinuedFromString(dateDiscontinued);
		} else if (field.equals("company")) {
			field = "company_id";
			Optional<String> companyId = getCompanyIdFromUser();
			Company company = null;
			if (companyId.isPresent()) {
				company = companyService.getCompanyFromId(Long.valueOf(companyId.get()));
			}
			computerToBeUpdate.get().setCompany(company);
		}

		computerService.updateComputer(computerToBeUpdate.get(), field);
	}

	/**
	 * Launch the menu which allows the user to delete a company AND all the
	 * computer linked to it.
	 * 
	 * @throws DataBaseAccessException
	 */
	protected void launchMenuDeleteCompany() throws DataBaseAccessException {
		Optional<String> companyToBeDeleted = getCompanyIdFromUser();
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
			try {
				if (userChoice.isPresent()) {
					switch (userChoice.get()) {
					case DISPLAY_COMPUTERS:
						displayAllComputers();
						break;
					case DISPLAY_COMPANIES:
						displayAllCompanies();
						break;
					case SHOW_COMPUTER_DETAILS:
						launchMenuShowDetailComputer();
						break;
					case CREATE_COMPUTER:
						launchMenuCreateComputer();
						break;
					case UPDATE_COMPUTER:
						launchMenuUpdateComputer();
						break;
					case DELETE_COMPUTER:
						launchMenuDeleteComputer();
						break;
					case DELETE_COMPANY:
						launchMenuDeleteCompany();
						break;
					case QUIT:
						stop = true;
						break;
					}
				}
			} catch (DataBaseAccessException e) {
				System.err.println("ERROR : an error occur while accessing the database");
				e.printStackTrace();
			}
		}

	}

	/**
	 * Entry point of the application.
	 * 
	 * @param args String[] not used
	 */
	public static void main(String[] edrsze) {
		System.out.println("Hello !");
		@SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(CliAppConfig.class);
		CommandLineInterface CLI = context.getBean(CommandLineInterface.class);
		CLI.launchMainMenu();
		System.out.println("Goodbye !");
	}
}
