/**
 * 
 */
package com.damnhandy.aspects.bean.test;

import com.damnhandy.aspects.bean.Observable;
import com.damnhandy.aspects.bean.Silent;

/**
 * @author plamKaTa
 */
@Observable
public class NestedObject {
	
	private String name;
	
	private Integer id;
	
	private String type;
	
	@Silent
	private String type1;

	/**
	 * 
	 */
	public NestedObject() {
		
	}
	
	/**
	 * 
	 */
	public NestedObject(Integer id, String name) {
		setId(id);
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	@Silent
	public void setType(String type) {
		this.type = type;
	}

	public String getType1() {
		return type1;
	}

	public void setType1(String type1) {
		this.type1 = type1;
	}

}
