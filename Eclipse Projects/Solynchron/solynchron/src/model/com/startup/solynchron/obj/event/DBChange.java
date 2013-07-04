/**
 * 
 */
package com.startup.solynchron.obj.event;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.startup.solynchron.ReflectionManger;

/**
 * @author plamKaTa
 *
 */
@Entity
public class DBChange {
	
	private Long id;
	
	private String property;
	
	private String propertyType;
	
	private String value;

	private DBLog log;
	
	/**
	 * 
	 */
	public DBChange() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Transient
	public Object getRealValue(EntityManager em) {
		return ReflectionManger.convertFromString(
				em, getPropertyType(), getValue());
	}
	
	@Transient
	public void setRealValue(Object value) {
		setValue(ReflectionManger.convertToString(value));
	}
	
	@ManyToOne
	public DBLog getLog() {
		return log;
	}

	public void setLog(DBLog log) {
		this.log = log;
	}

}
