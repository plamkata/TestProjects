/**
 * 
 */
package com.startup.solynchron.obj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Domain extends ModelObject {
	
	public static final String DESCRIPTION = "description";
	
	public static final String CATEGORY = "category";
	
	public static final String TOOLS = "tools";
	

	private String description;
	
	private DomainCategory category;
	
	private List<DomainTool> tools = new ArrayList<DomainTool>();
	
	/**
	 * 
	 */
	public Domain() {
		super();
	}

	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public DomainCategory getCategory() {
		return category;
	}

	public void setCategory(DomainCategory category) {
		this.category = category;
	}


	@OneToMany(mappedBy = "domain", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@OrderBy("tool, toolVersion")
	public List<DomainTool> getTools() {
		return tools;
	}

	public void setTools(List<DomainTool> tools) {
		this.tools = tools;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Domain) {
			Domain other = (Domain) obj;
			return CompareManager.equals(this.getDescription(), other.getDescription()) &&
				CompareManager.equals(this.getCategory(), other.getCategory());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int descrHash = 17;
		if (description != null) {
			descrHash = 17 ^ description.hashCode();
		}
		
		int categHash = 31;
		if (category != null) {
			if (category.isNew()) {
				categHash = category.hashCode();
			} else {
				categHash = 31 ^ category.getId().hashCode();
			}
		}
		return descrHash ^ categHash;
	}

}
