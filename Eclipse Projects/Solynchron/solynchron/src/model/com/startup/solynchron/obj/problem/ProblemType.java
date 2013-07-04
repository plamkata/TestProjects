/**
 * 
 */
package com.startup.solynchron.obj.problem;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import com.damnhandy.aspects.bean.Observable;
import com.startup.solynchron.obj.ModelObject;
import com.startup.solynchron.obj.event.CompareManager;

/**
 * Represents a particular problem type, for example: error, how to use, . 
 * Populated with data initially.
 * 
 * @author plamKaTa
 *
 */
@Observable
@Indexed
@Entity
public class ProblemType extends ModelObject {
	
	public static final String NAME = "name";
	
	public static final String DESCRIPTION = "description";
	
	public static final String PROBLEMS = "problems";

	private String name;
	
	private String description;
	
	private List<Problem> problems = new ArrayList<Problem>();
	
	/**
	 * 
	 */
	public ProblemType() {
		super();
	}

	@Basic
	@Column(unique = true, nullable = false)
	@Field(index = Index.TOKENIZED, store = Store.NO)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Field(index = Index.TOKENIZED, store = Store.NO)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
	@OrderBy("domain")
	public List<Problem> getProblems() {
		return problems;
	}

	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProblemType) {
			ProblemType other = (ProblemType) obj;
			return CompareManager.equals(this.getName(), other.getName());
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int nameHash = 37;
		if (name != null) {
			nameHash = 37 ^ name.hashCode();
		}
		return nameHash;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}