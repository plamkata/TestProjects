/**
 * 
 */
package com.startup.solynchron.obj;

import java.beans.PropertyChangeListener;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.aspectj.lang.annotation.SuppressAjWarnings;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * @author plamKaTa
 *
 */
@MappedSuperclass
public abstract class ModelObject implements IModelObject {
	
	public static final String ID = "id";
	
	public static final String CREATE_DATE = "createDate";
	
	public static final String CREATED_BY = "createdBy";
	
	public static final String UPDATE_DATE = "updateDate";
	
	public static final String UPDATED_BY = "updatedBy";
	

	private Long id;
	
	private Date createDate;

	private MyUser createdBy;
	
	private Date updateDate;
	
	private MyUser updatedBy;
	
	private Integer version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public MyUser getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(MyUser createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	public MyUser getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(MyUser updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Version
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Transient
	public boolean isNew() {
		return id == null;
	}
	
	@Transient
	@SuppressAjWarnings
	public PropertyChangeListener[] getPropertyChangeListeners() {
		PropertyChangeListener[] listeners = null;
		if (this instanceof JavaBean) {
			listeners = ((JavaBean) this).getPropertyChangeListeners();
		}
		return listeners;
	}

}