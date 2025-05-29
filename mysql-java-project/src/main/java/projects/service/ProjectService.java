package projects.service;

import java.util.List;
import java.util.NoSuchElementException;

import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;

public class ProjectService {
//	instantiate a new projectDao object of type ProjectDao
	private ProjectDao projectDao = new ProjectDao();

	public Project addProject(Project project) {
		// TODO Auto-generated method stub
		return projectDao.insertProject(project);
	}

	public List<Project> fetchAllProjects() {
		// TODO Auto-generated method stub
		return projectDao.fetchAllProjects();	}

	public Project fetchProjectById(Integer projectId) {
		// TODO Auto-generated method stub
		return projectDao.fetchProjectById(projectId).orElseThrow(() -> new NoSuchElementException(
				"Project with project ID=" + projectId + " does not exist."));
	}

	public void modifyProjectDetails(Project project) {
		// TODO Auto-generated method stub
//		if project does not exist 
		if(!projectDao.modifyProjectDetails(project)) {
//			throw a new exception stating the project id the user had selected does not exist
			throw new DbException("Project with ID=" + project.getProjectId() + " does not exist.");
		}
	}

	public void deleteProject(Integer projectId) {
		// TODO Auto-generated method stub
//		if project does not exist
		if(!projectDao.deleteProject(projectId)) {
//			throw an exception stating the project id the user selected does not exist
			throw new DbException("Project with ID=" + projectId + " does not exist.");
		}
	}

}
