/**
 * 
 */
package com.startup.solynchron.obj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.startup.solynchron.obj.ModelObject;

/**
 * @author plamKaTa
 *
 */
@Entity
public class Tool extends ModelObject {
	
	private String name;
	
	private ToolType type;
	
	private List<ToolVersion> versions = new ArrayList<ToolVersion>();
	
	private List<DomainTool> domains = new ArrayList<DomainTool>();

	/**
	 * 
	 */
	public Tool() {
		super();
	}

	@Basic
	@Column(unique = true, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public ToolType getType() {
		return type;
	}

	public void setType(ToolType type) {
		this.type = type;
	}

	@OneToMany(mappedBy = "tool", fetch = FetchType.EAGER)
	@OrderBy("toolVersion")
	public List<ToolVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<ToolVersion> versions) {
		this.versions = versions;
	}

	@OneToMany(mappedBy = "tool", fetch = FetchType.LAZY)
	@OrderBy("toolVersion")
	public List<DomainTool> getDomains() {
		return domains;
	}

	public void setDomains(List<DomainTool> domains) {
		this.domains = domains;
	}

}
