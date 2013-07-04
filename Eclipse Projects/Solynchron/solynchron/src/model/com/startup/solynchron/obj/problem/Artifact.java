/**
 * 
 */
package com.startup.solynchron.obj.problem;

import java.sql.Clob;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.startup.solynchron.obj.ModelObject;

/**
 * An artifact represents a large or small data with textual representation 
 * that is used as an attachment to describe a particular problem or solution.
 * Artifacts can be source code, exceptions, configurations, online resources, ... 
 * used to give an example, to clarify specifics of the problem/solution.
 * 
 * @author plamKaTa
 *
 */
@Entity
public class Artifact extends ModelObject {
	
	private String title;
	
	private String shortText;
	
	private Clob longText;
	
	private ArtifactType type;

	/**
	 * 
	 */
	public Artifact() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public Clob getLongText() {
		return longText;
	}

	public void setLongText(Clob longText) {
		this.longText = longText;
	}

	@ManyToOne(optional = false)
	public ArtifactType getType() {
		return type;
	}

	public void setType(ArtifactType type) {
		this.type = type;
	}

}
