/************************************************************ 
File: ResourceEntity.java
 
Version 1.0 
Date         Author         Changes 
09.06.2006 Ilya Platonov  Created   

Copyright (c) 2006, IBM Corporation 
All rights reserved. 
*************************************************************/
package indexer.database;

import org.eclipse.core.resources.IResource;

/**
 * This class stores resource information.
 * @author Ilya Platonov
 *
 */
public class ResourceEntity {

	/** name of resource projects. */
	private String projectName;
	/** resource name. */
	private String resourceName;
	/** path to resource. */
	private String resourcePath;
	
	/**
	 * Default constructor.
	 */
	public ResourceEntity() {
		
	}
	
	/**
	 * Create ResourceEntity by <code>IResource</code>.
	 * @param resource to get information from
	 */
	public ResourceEntity(IResource resource) {
		projectName = resource.getProject().getName();
		resourceName = resource.getName();
		resourcePath = resource.getFullPath().toString();
	}
	/**
	 * @return Returns the projectName.
	 */
	public String getProjectName() {
		return projectName;
	}
	/**
	 * @param projectName The projectName to set.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	/**
	 * @return Returns the resourceName.
	 */
	public String getResourceName() {
		return resourceName;
	}
	/**
	 * @param resourceName The resourceName to set.
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	/**
	 * @return Returns the resourcePath.
	 */
	public String getResourcePath() {
		return resourcePath;
	}
	/**
	 * @param resourcePath The resourcePath to set.
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	
	
}
