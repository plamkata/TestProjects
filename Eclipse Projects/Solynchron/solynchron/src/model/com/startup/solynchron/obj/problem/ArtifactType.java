/**
 * 
 */
package com.startup.solynchron.obj.problem;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.startup.solynchron.obj.ModelObject;

/**
 * Artifact types are dependent on the specified problem type or solution method.
 *  
 * @author plamKaTa
 *
 */
@Entity
public class ArtifactType extends ModelObject {

	private String name;
	
	private Set<Artifact> artifacts = new HashSet<Artifact>();
	
	/**
	 * 
	 */
	public ArtifactType() {
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

	@OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
	public Set<Artifact> getArtifacts() {
		return artifacts;
	}

	public void setArtifacts(Set<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

}
