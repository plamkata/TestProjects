/************************************************************ 
File: ResourcesDatabaseInitialier.java
 
Version 1.0 
Date         Author         Changes 
20.06.2006 Ilya Platonov  Created   

Copyright (c) 2005, IBM Corporation 
All rights reserved. 
*************************************************************/
package resources_indexer.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Utility database initializer database for Resource database. 
 * @author Ilya Platonov
 */
public class ResourcesDatabaseInitiaizer {

	/**
	 * private constructor to prevent instatiation of utility class.
	 */
	private ResourcesDatabaseInitiaizer() {
	}
	
    /** SQL script for creating Resources table. */
    private static final String CREATE_TABLE_RESOURCES = "CREATE TABLE Resources" 
        + "("
        + "path varchar(1024),"
        + "name varchar(255),"
        + "project varchar(255),"
        + "PRIMARY KEY(path)"
        + ")";
    
	/**
	 * Initilizes tables for resource database.
	 * @param connection connectio to use for database initialization
	 * @throws SQLException if SQL error ocurred
	 */
	public static void initDatabase(Connection connection) throws SQLException {
        try {
            Statement s = connection.createStatement();
            try {
            	s.execute(CREATE_TABLE_RESOURCES);
            } finally {
            	s.close();
            }
            connection.commit();
        } catch (SQLException ex) {
            connection.rollback();
            throw ex;
        }

	}

}
