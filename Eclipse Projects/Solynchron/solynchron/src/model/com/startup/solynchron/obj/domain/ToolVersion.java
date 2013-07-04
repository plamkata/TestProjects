/**
 * 
 */
package com.startup.solynchron.obj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.startup.solynchron.obj.ModelObject;

/**
 * @author plamKaTa
 *
 */
@Entity
public class ToolVersion extends ModelObject {

	private String toolVersion;
	
	private Tool tool;
	
	private List<DomainTool> domains = new ArrayList<DomainTool>();
	
	/**
	 * 
	 */
	public ToolVersion() {
		super();
	}

	public String getToolVersion() {
		return toolVersion;
	}

	public void setToolVersion(String toolVersion) {
		this.toolVersion = toolVersion;
	}

	@ManyToOne
	public Tool getTool() {
		return tool;
	}

	public void setTool(Tool tool) {
		this.tool = tool;
	}

	@OneToMany(mappedBy = "toolVersion", fetch = FetchType.LAZY)
	public List<DomainTool> getDomains() {
		return domains;
	}

	public void setDomains(List<DomainTool> domains) {
		this.domains = domains;
	}

}
