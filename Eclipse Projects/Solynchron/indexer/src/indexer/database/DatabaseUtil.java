/************************************************************ 
File: DatabaseUtil.java
 
Version 1.0 
Date         Author         Changes 
09.06.2006 Ilya Platonov  Created   

Copyright (c) 2006, IBM Corporation 
All rights reserved. 
*************************************************************/
package indexer.database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.datasources.PerUserPoolDataSource;
import org.apache.derby.jdbc.EmbeddedConnectionPoolDataSource;

import indexer.Activator;

/**
 * Utiltiy class that handles database manipulation methods.
 * @author Ilya Platonov
 *
 */
public class DatabaseUtil {

	/**
	 * Datasource to use for connection.
	 */
	private static PerUserPoolDataSource datasource;
	
	/**
	 * Initializes database and creates datasource instance for it.
	 */
	public static void initDatasource () {
		EmbeddedConnectionPoolDataSource connectionPoolDatasource;

		connectionPoolDatasource = new EmbeddedConnectionPoolDataSource(); 
		String dbFilePath = Activator.getDefault().getStateLocation()
				.toFile().getAbsolutePath() + File.separator + "resourcesDB";
		connectionPoolDatasource.setDatabaseName(dbFilePath);
		connectionPoolDatasource.setCreateDatabase("create");
		System.out.println("Connecting to database: " + dbFilePath);
		
		datasource = new PerUserPoolDataSource();
		datasource.setConnectionPoolDataSource(connectionPoolDatasource);
		datasource.setDefaultAutoCommit(false);
		
		try {
			Connection connection = datasource.getConnection();
			try {
				Statement statement = connection.createStatement();
				
				// searching for Resources table in database and if there is no one then initialize database
				try {
					statement.execute("SELECT 1 FROM Resources");
				} catch (SQLException ex) {
					ResourcesDatabaseInitiaizer.initDatabase(connection);			
				} finally {
					statement.close();
				}
				connection.commit();
			} finally {
				connection.close();
			}
		} catch (SQLException ex) {
			Activator.log(ex);
			datasource = null;
		} 	
	}
	/**
	 * Closes datasource.
	 */
	public static void closeDatasource() {
		if (datasource != null) {
			try {
				datasource.close();
			} catch (Exception ex) {
				Activator.log(ex);
			}
			datasource = null;
		}
	}
	
	/**
	 * Adds resouce entry in database
	 * @param resource resource to add into database
	 * @throws SQLException if SQL error ocurred
	 */
	public static final void addResource(ResourceEntity resource) throws SQLException {
		Connection connection = datasource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
		try {
			PreparedStatement s = connection.prepareStatement("INSERT INTO Resources (path, name, project) VALUES(?,?,?)");
			try {
		        s.setString(1, resource.getResourcePath());
		        s.setString(2, resource.getResourceName());
		        s.setString(3, resource.getProjectName());
		        s.execute();
		    } finally {
		        s.close();
		    }
		    connection.commit();
		} catch (SQLException e) {
			Activator.log(e);
			connection.rollback();
		} finally {
			connection.close();
		}
	}
	
	/**
	 * Update resource information in database.
	 * @param path old resource path if path was changed, or just current path if it was not
	 * @param newResource new information for resource
	 * @throws SQLException if SQL error ocurred
	 */
	public static final void updateResource(String path, ResourceEntity newResource) throws SQLException{
		Connection connection = datasource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
	    try {
	    	PreparedStatement s = connection.prepareStatement("UPDATE Resources SET path=?, name=?, project=? WHERE path=?");
			try {
		    	s.setString(1, newResource.getResourcePath());
		        s.setString(2, newResource.getResourceName());
		        s.setString(3, newResource.getProjectName());
		        s.setString(4, path);
		        s.execute();
		    } finally {
		        s.close();
		    }
		    connection.commit();
	    } catch (SQLException e) {
			Activator.log(e);
			connection.rollback();
		} finally {
	    	connection.close();
	    }
	}
	
	/**
	 * Removes information about resource from database.
	 * @param resource to remove
	 * @throws SQLException if SQL error ocurred
	 */
	public static final void removeResource(ResourceEntity resource) throws SQLException {
		Connection connection = datasource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	    
		try {
			PreparedStatement s = connection.prepareStatement(
					"DELETE FROM Resources WHERE path=?");
		    try {
		        s.setString(1, resource.getResourcePath());
		        s.execute();
	        } finally {
		        s.close();
		    }
	        connection.commit();
		} catch (SQLException e) {
			Activator.log(e);
			connection.rollback();
		} finally {
			connection.close();
		}
	}
	
	/**
	 * Cleans project by project name.
	 * @param projectName name of the project to clean
	 * @throws SQLException if SQL error ocurred
	 */
	public static final void cleanProject (String projectName) throws SQLException{
		Connection connection = datasource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
	    
	    try {
	    	PreparedStatement s = connection.prepareStatement(
	    			"DELETE FROM Resources WHERE project like ?");
			try {
		        s.setString(1, projectName);
		        s.execute();
		    } finally {
		        s.close();
		    }
		    connection.commit();
	    } catch (SQLException e) {
			Activator.log(e);
			connection.rollback();
		} finally {
	    	connection.close();
	    }
	}
	/**
	 * Query all resource from database and returns it.
	 * @param searchString string which should be included in resource name
	 * @return List of ResourceEntity
	 * @throws SQLException if SQL error ocurred
	 */
	public static final /* ResourceEntity*/ List getResources(String searchString) throws SQLException {
		Connection connection = datasource.getConnection();
		connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
		
		List result = new ArrayList();
	    
	    try {
	    	PreparedStatement s = connection.prepareStatement(
	    			"SELECT name, path, project FROM Resources WHERE lower(path) like ? ORDER BY name, path");
			try {
				s.setString(1, "%" + searchString.toLowerCase() + "%");
		        ResultSet rs = s.executeQuery();
		        while (rs.next()) {
		        	ResourceEntity resourceEntity = new ResourceEntity();
		        	resourceEntity.setResourceName(rs.getString(1));
		        	resourceEntity.setResourcePath(rs.getString(2));
		        	resourceEntity.setProjectName(rs.getString(3));
		        	result.add(resourceEntity);
		        }
		        rs.close();
		    } finally {
		        s.close();
		    }
		    connection.commit();
	    } catch (SQLException e) {
			Activator.log(e);
			connection.rollback();
		} finally {
	    	connection.close();
	    }	    
	    return result;
	}
}
