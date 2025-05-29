package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
import projects.exception.DbException;
import projects.util.DaoBase;

public class ProjectDao extends DaoBase {
	// define constants
	private static final String CATEGORY_TABLE = "category";
	private static final String MATERIAL_TABLE = "material";
	private static final String PROJECT_TABLE = "project";
	private static final String PROJECT_CATEGORY_TABLE = "project_category";
	private static final String STEP_TABLE = "step";

	public Project insertProject(Project project) {
		// TODO Auto-generated method stub
		// @formatter:off
		String sql = ""
				+ "INSERT INTO " + PROJECT_TABLE + " " 
				+ "(project_name, estimated_hours, actual_hours, difficulty, notes) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?)";
		// @formatter:on
		
		// get connection
		try(Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				
				stmt.executeUpdate();
				
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				commitTransaction(conn);
				
				project.setProjectId(projectId);
				return project;
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		}
		catch(SQLException e) {
			throw new DbException(e);
		}
	}

	public List<Project> fetchAllProjects() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM " + PROJECT_TABLE + " ORDER BY project_name";
		
		try(Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
				try(ResultSet rs = stmt.executeQuery()) {
					List<Project> projects = new LinkedList<>();
					
					while(rs.next()) {
						projects.add(extract(rs, Project.class));
					}
					return projects;
				}
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		}
		catch(SQLException e) {
			throw new DbException(e);
		}
	}
	
	public Optional<Project> fetchProjectById(Integer projectId) {
		String sql = "SELECT * FROM " + PROJECT_TABLE + " WHERE project_id = ?";
		
		try(Connection conn = DbConnection.getConnection()) {
			startTransaction(conn);
			
			try {
				Project project = null;
				
				try(PreparedStatement stmt = conn.prepareStatement(sql)) {
					setParameter(stmt, 1, projectId, Integer.class);
					
					try(ResultSet rs = stmt.executeQuery()) {
						if(rs.next()) {
							project = extract(rs, Project.class);
						}
					}
				}
				if(Objects.nonNull(project)) {
					project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
					project.getSteps().addAll(fetchStepsForProject(conn, projectId));
					project.getCategories().addAll(fetchCategoriesForProject(conn, projectId));
				}
				
				commitTransaction(conn);
				return Optional.ofNullable(project);
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		}
		catch(SQLException e) {
			throw new DbException(e);
		}
	}

	private List<Category> fetchCategoriesForProject(Connection conn, Integer projectId) throws SQLException {
		// TODO Auto-generated method stub
		// @formatter:off
		String sql = ""
				+ "SELECT c.* FROM " + CATEGORY_TABLE + " c "
				+ "JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id) "
				+ "Where project_id = ?";
		// @formatter:on
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Category> categories = new LinkedList<>();
				
				while(rs.next()) {
					categories.add(extract(rs, Category.class));
				}
				return categories;
			}
		}
	}
	
	private List<Step> fetchStepsForProject(Connection conn, Integer projectId) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM " + STEP_TABLE + " Where project_id = ?";
				
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Step> steps = new LinkedList<>();
						
					while(rs.next()) {
						steps.add(extract(rs, Step.class));
					}
					return steps;
			}
		}
	}
	
	private List<Material> fetchMaterialsForProject(Connection conn, Integer projectId) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM " + MATERIAL_TABLE + " Where project_id = ?";
		
		try(PreparedStatement stmt = conn.prepareStatement(sql)) {
			setParameter(stmt, 1, projectId, Integer.class);
			
			try(ResultSet rs = stmt.executeQuery()) {
				List<Material> materials = new LinkedList<>();
						
					while(rs.next()) {
						materials.add(extract(rs, Material.class));
					}
					return materials;
			}
		}
	}

	public boolean modifyProjectDetails(Project project) {
		// TODO Auto-generated method stub
//		update sql statement
		// @formatter: off
		String sql = ""
				+ "UPDATE " + PROJECT_TABLE + " SET "
				+ "project_name = ?, " 
				+ "estimated_hours = ?, "
				+ "actual_hours = ?, "
				+ "difficulty = ?, "
				+ "notes = ? "
				+ "WHERE project_id = ?";
		// @formmater: on
//		try to connect to server
		try (Connection conn = DbConnection.getConnection()) {
//			if successful start transaction
			startTransaction(conn);
			try(PreparedStatement stmt = conn.prepareStatement(sql)) {
//				attempt to update the values projectName, estimatedHours, actualHours, difficulty, and notes
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
//				set project id
				setParameter(stmt, 6, project.getProjectId(), Integer.class);
//				set boolean modified to stmt.executeUpdate() == 1
				boolean modified = stmt.executeUpdate() == 1;
//				commit transaction
				commitTransaction(conn);
//				return modified
				return modified;
			} 
//			if changes fail
			catch(Exception e) {
//				roll back transaction
				rollbackTransaction(conn);
//				throw an exception
				throw new DbException(e);
			}
		}
//		if SQL fails
		catch(SQLException e) {
//			throw a SQL exception
			throw new DbException(e);
		}
	}

	public boolean deleteProject(Integer projectId) {
		// TODO Auto-generated method stub
		// sql delete statement
		// @formatter:off
		String sql = "DELETE FROM " + PROJECT_TABLE + " WHERE project_id = ?; ";
		// @formatter:on
//		attempt connection
		try (Connection conn = DbConnection.getConnection()) {
//			start transaction
			startTransaction(conn);
//			attempt to process sql statement
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//				if successful set the projectId to the user input projectId
				setParameter(stmt, 1, projectId, Integer.class);
//				set boolean deleted to the stmt.executeUpdate() == 1
				boolean deleted = stmt.executeUpdate() == 1;
//				commit transaction
				commitTransaction(conn);
//				return deleted
				return deleted;
			}
//			if transaction fails
			catch(Exception e) {
//				roll back transaction
				rollbackTransaction(conn);
//				throw an exception
				throw new DbException(e);
			}
		}
//		if connection fails
		catch(Exception e) {
//			throw a new exception
			throw new DbException(e);
		}
	}

}
