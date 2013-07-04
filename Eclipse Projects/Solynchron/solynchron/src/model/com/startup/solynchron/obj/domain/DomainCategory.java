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
import com.startup.solynchron.obj.event.CompareManager;

/**
 * @author plamKaTa
 *
 */
@Entity
public class DomainCategory extends ModelObject {
	
	@Basic
	@Column(unique = true, nullable = false)
	private String name;
	
	private DomainCategory parent;
	
	private List<DomainCategory> children = new ArrayList<DomainCategory>();
	
	private List<Domain> domains = new ArrayList<Domain>();

	/**
	 * 
	 */
	public DomainCategory() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	public DomainCategory getParent() {
		return parent;
	}

	public void setParent(DomainCategory parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
	@OrderBy("name")
	public List<DomainCategory> getChildren() {
		return children;
	}

	public void setChildren(List<DomainCategory> children) {
		this.children = children;
	}

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
	public List<Domain> getDomains() {
		return domains;
	}

	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DomainCategory) {
			DomainCategory other = (DomainCategory) obj;
			return CompareManager.equals(this.getName(), other.getName()) && 
				CompareManager.equals(this.getParent(), other.getParent());
				
			
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int nameHash = 22;
		if (name != null) {
			nameHash = 22 ^ name.hashCode();
		}
		
		int parentHash = 37;
		if (parent != null) {
			if (parent.isNew()) {
				parentHash = parent.hashCode();
			} else {
				parentHash = 37 ^ parent.getId().hashCode();
			}
		}
		
		return nameHash ^ parentHash;
	}

}