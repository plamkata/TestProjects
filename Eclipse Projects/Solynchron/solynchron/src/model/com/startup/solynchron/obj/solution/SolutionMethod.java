/**
 * 
 */
package com.startup.solynchron.obj.solution;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.startup.solynchron.obj.ModelObject;

/**
 * This class represents a solution method the user has decided to stick to. 
 * Solution methods usually depend on the problem type. They also specify
 * typical types of artefacts that will be used/included by the solution.
 *  
 * @author plamKaTa
 *
 */
@Entity
public class SolutionMethod extends ModelObject {
	
	private String name;
	
	private List<Solution> solutions = new ArrayList<Solution>();

	/**
	 * 
	 */
	public SolutionMethod() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(mappedBy = "method", fetch = FetchType.LAZY)
	public List<Solution> getSolutions() {
		return solutions;
	}

	public void setSolutions(List<Solution> solutions) {
		this.solutions = solutions;
	}

}
