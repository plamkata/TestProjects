package com.startup.solynchron.obj;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Indexed
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("user")
public class MyUser extends ModelObject {
	
	public static final String NAME = "name";
	
	public static final String TYPE = "type";
	
	
	private String name;
	
	// TODO: add password encryption
	private String password;
	
	private String type;

	public MyUser() {
		// TODO Auto-generated constructor stub
	}

	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Basic
	@Column(insertable = false, updatable = false)
	@Field(index = Index.UN_TOKENIZED, store = Store.YES)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}