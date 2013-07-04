/**
 * 
 */
package com.startup.solynchron.obj.solution;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.startup.solynchron.obj.ModelObject;
import com.startup.solynchron.obj.problem.Problem;

/**
 * Represents a solution to a particular problem.
 * 
 * @author plamKaTa
 *
 */
@Entity
public class Solution extends ModelObject {
	
	private String description;
	
	private SolutionMethod method;
	
	private Problem problem;
	
	private List<SolutionArtifact> artifacts = new ArrayList<SolutionArtifact>();

	/**
	 * 
	 */
	public Solution() {
		// TODO Auto-generated constructor stub
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToOne
	public SolutionMethod getMethod() {
		return method;
	}

	public void setMethod(SolutionMethod method) {
		this.method = method;
	}

	@ManyToOne
	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@OneToMany(mappedBy = "solution", 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE}, 
			fetch = FetchType.EAGER)
	public List<SolutionArtifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(List<SolutionArtifact> artifacts) {
		this.artifacts = artifacts;
	}

}
