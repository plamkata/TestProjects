/**
 * 
 */
package com.startup.solynchron.obj.problem;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.startup.solynchron.obj.ModelObject;

/**
 * @author plamKaTa
 *
 */
@Entity
public class ProblemArtifact extends ModelObject {
	
	private String purpose;
	
	private Problem problem;
	
	private Artifact artifact;

	/**
	 * 
	 */
	public ProblemArtifact() {
		super();
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@ManyToOne(optional = false)
	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	@ManyToOne(optional = false)
	public Artifact getArtifact() {
		return artifact;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact = artifact;
	}

}
