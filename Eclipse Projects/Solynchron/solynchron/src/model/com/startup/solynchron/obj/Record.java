package com.startup.solynchron.obj;

import javax.persistence.Entity;

@Entity
public class Record extends ModelObject {
	
	private Integer quantity;
	
	public Record() {
		
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
