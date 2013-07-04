/**
 * 
 */
package com.startup.solynchron.obj.solution;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.startup.solynchron.obj.ModelObject;
import com.startup.solynchron.obj.problem.Artifact;

/**
 * @author plamKaTa
 *
 */
@Entity
public class SolutionArtifact extends ModelObject {
	
	private String purpose;
	
	private Solution solution;
	
	private Artifact artifact;

	/**
	 * 
	 */
	public SolutionArtifact() {
		// TODO Auto-generated constructor stub
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@ManyToOne
	public Solution getSolution() {
		return solution;
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	@ManyToOne
	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

}
