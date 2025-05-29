package projects;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectsApp {
	
	// scanner to take in user input
	private Scanner scanner = new Scanner(System.in);
	// instantiate a new ProjectService object called projectService
	private ProjectService projectService = new ProjectService();
	private Project curProject;
	
	// menu options
	// @formatter:off
	private List<String> operations = List.of(
			"1) Add a project",
			"2) List projects",
			"3) Select a project",
			"4) Update project details",
			"5) Delete a project"
	);
	// @formatter:on
	// user menu selection if you hit enter menu exits
	private void processUserSelection() {
		// TODO Auto-generated method stub
		boolean done = false;
		while(!done) {
			try {
				int selection = getUserSelection();
				
				switch(selection) {
				  case -1: 
					done = exitMenu();
					break;
				 
				  case 1:
					createProject();
					break;
					
				  case 2:
					  listProjects();
					  break;
				  case 3:
					  selectProject();
					  break;
				  case 4:
					  updateProjectDetails();
					  break;
				  case 5:
					  deleteProject();
					  break;
					  
				  default:
					  System.out.println("\n" + selection + " is not a valid selection. Try again.");
					  
				}
			} catch(Exception e) {
				System.out.println("\nError: " + e + "Try again!");
				e.printStackTrace();
			}
		}
	}
	
	private void deleteProject() {
		// TODO Auto-generated method stub
		// get a list of projects
		listProjects();
		// prompt user to insert the id of the item that they want to delete 
		// use getIntInput to grab the integer that the user input.
		Integer projectId = getIntInput("\nPlease enter the ID of the project you would like to delete");
		// call delete project from projectService
		projectService.deleteProject(projectId);
		// project was deleted successfully message
		System.out.println("Project " + projectId + " was deleted successfully.");
		// if curProject is not null and curProject id is the same as project id
		if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectId)) {
			// set curProject to null
			curProject = null;
		}
	}

	private void updateProjectDetails() {
		// TODO Auto-generated method stub
		// if curProject is null
		if (curProject == null) {
			// prompt the user to select a project
			System.out.println("\nPlease select a project.");
		}
		
		// set projectName, estimatedHours, acutalHours, difficulty, and notes to user input values
		String projectName = 
				getStringInput("Enter the project name [" + curProject.getProjectName() + "]");
		
		BigDecimal estimatedHours = 
				getDecimalInput("Enter the estimated hours [" + curProject.getEstimatedHours() + "]");
		
		BigDecimal actualHours = 
				getDecimalInput("Enter the actual hours [" + curProject.getActualHours() + "]");
		
		Integer difficulty = 
				getIntInput("Enter the project difficulty (1-5) [" + curProject.getDifficulty() + "]");
		
		String notes =
				getStringInput("Enter the project notes [" + curProject.getNotes() + "]");
		
		// instantiate a new project
		Project project = new Project();
		
		// set projectId to curProjectId
		project.setProjectId(curProject.getProjectId());
		// if projectName, estimatedHours, actualHours, difficulty, and notes are null
		// grab the user input, or set values to projectName, estimatedHours, actualHours, difficulty, and notes
		project.setProjectName(Objects.isNull(projectName) ? curProject.getProjectName() : projectName);
		project.setEstimatedHours(Objects.isNull(estimatedHours) ? curProject.getEstimatedHours() : estimatedHours);
		project.setActualHours(Objects.isNull(actualHours) ? curProject.getActualHours() : actualHours);
		project.setDifficulty(Objects.isNull(difficulty) ? curProject.getDifficulty() : difficulty);
		project.setNotes(Objects.isNull(notes) ? curProject.getNotes() : notes);
		
		// call modifyProjectDetails method from projectService
		projectService.modifyProjectDetails(project);
		
		// set curProject to the project with the same id by calling fetchProjectById from projectService
		curProject = projectService.fetchProjectById(curProject.getProjectId());
	}

	private void selectProject() {
		// TODO Auto-generated method stub
		listProjects();
		Integer projectId = getIntInput("Enter a project ID to select a project");
		curProject = null;
		curProject = projectService.fetchProjectById(projectId);
	}

	private void listProjects() {
		// TODO Auto-generated method stub
		List<Project> projects = projectService.fetchAllProjects();
		
		System.out.println("\nProjects:");
		
		projects.forEach(project -> System.out
				.println("  " + project.getProjectId() 
				+ ": " + project.getProjectName()));
	}

	private void printOperations() {
		// TODO Auto-generated method stub
		System.out.println("\nThese are the availible selections. Press Enter key to quit:");
		operations.forEach(line -> System.out.println("  " + line));
		
		if(Objects.isNull(curProject)) {
			System.out.println("\nYou are not working with a project");
		}
		else {
			System.out.println("\nYou are working with project: " + curProject);
		}
	}

	private void createProject() {
		// TODO Auto-generated method stub
		String projectName = getStringInput("Enter the project name");
		BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
		BigDecimal actualHours = getDecimalInput("Enter the actual hours");
		Integer difficulty = getIntInput("Enter project difficulty (1-5)");
		String notes = getStringInput("Enter the project notes");
		
		Project project = new Project();
		project.setProjectName(projectName);
		project.setEstimatedHours(estimatedHours);
		project.setActualHours(actualHours);
		project.setDifficulty(difficulty);
		project.setNotes(notes);
		
		Project dbProject = projectService.addProject(project);
		System.out.println("You have successfully created project: " + dbProject);
		
	}

	private BigDecimal getDecimalInput(String prompt) {
		// TODO Auto-generated method stub
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return new BigDecimal(input).setScale(2);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid decimal number.");
		}
	}

	private boolean exitMenu() {
		// TODO Auto-generated method stub
		System.out.println("Exiting the menu.");
		return true;
	}

	private int getUserSelection() {
		// TODO Auto-generated method stub
		printOperations();
		Integer input = getIntInput("Enter menu selection");
		return Objects.isNull(input) ? -1 : input;
	}


	
	private Integer getIntInput(String prompt) {
		String input = getStringInput(prompt);
		
		if (Objects.isNull(input)) {
			return null;
		}
		
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			throw new DbException(input + " is not a valid number.");
		}
	}

	private String getStringInput(String prompt) {
		// TODO Auto-generated method stub
		System.out.print(prompt + ": ");
		String input = scanner.nextLine();
		return input.isBlank() ? null : input.trim();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ProjectsApp().processUserSelection();
	}
}
