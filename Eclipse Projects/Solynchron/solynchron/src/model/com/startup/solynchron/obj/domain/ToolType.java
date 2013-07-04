/**
 * 
 */
package com.startup.solynchron.obj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.startup.solynchron.obj.ModelObject;

/**
 * This class represents various tool types like 'plug-in', 'library', 
 * 'os', 'platform', ... 
 * 
 * TODO: To be extended with tool type specific properties...
 * 
 * @author plamKaTa
 *
 */
@Entity
@NamedQuery(name = "toolType.byName", query = "from ToolType tt where tt.name like :typeName")
public class ToolType extends ModelObject {

	private String name;
	
	private List<Tool> tools = new ArrayList<Tool>();
	
	public ToolType() {
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
	
	@OneToMany(mappedBy="type", fetch = FetchType.LAZY)
    @OrderBy("name")
	public List<Tool> getTools() {
		return tools;
	}

	public void setTools(List<Tool> tools) {
		this.tools = tools;
	}
}
