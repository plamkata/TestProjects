/**
 * 
 */
package com.startup.solynchron.obj.event;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.NotNull;

import com.startup.solynchron.ReflectionManger;
import com.startup.solynchron.obj.IModelObject;

/**
 * 
 * @author plamKaTa
 *
 */
@Entity
public class DBLog {
	
	private Long id;
	
	private Long entryId;
	
	private String entryType;
	
	private String operation;
	
	private Date entryDate;
	
	/**
	 * 
	 */
	public DBLog() {
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

	public Long getEntryId() {
		return entryId;
	}

	public void setEntryId(Long entryId) {
		this.entryId = entryId;
	}

	@NotNull
	public String getEntryType() {
		return entryType;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	
	@Transient
	public IModelObject getEntry(EntityManager em) {
		if (getEntryId() != null) {
			return (IModelObject) ReflectionManger.convertFromString(
					em, getEntryType(), getEntryId().toString());
		} else {
			return null;
		}
	}
	
	@Transient
	public void setEntry(IModelObject object) {
		if (object != null) {
			setEntryId(object.getId());
			setEntryType(object.getClass().getName());
		}
	}

}
