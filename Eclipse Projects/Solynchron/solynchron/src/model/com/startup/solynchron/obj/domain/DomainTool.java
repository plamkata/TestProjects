/**
 * 
 */
package com.startup.solynchron.obj.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.startup.solynchron.obj.ModelObject;

/**
 * Realizes a many-to-many relation between domains and tools.
 * 
 * @author plamKaTa
 *
 */
@Entity
public class DomainTool extends ModelObject {

	private Domain domain;
	
	private Tool tool;
	
	private ToolVersion toolVersion;
	
	/**
	 * 
	 */
	public DomainTool() {
		// TODO Auto-generated constructor stub
	}

	@ManyToOne
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public ToolVersion getToolVersion() {
		return toolVersion;
	}

	public void setToolVersion(ToolVersion toolVersion) {
		this.toolVersion = toolVersion;
	}

}
