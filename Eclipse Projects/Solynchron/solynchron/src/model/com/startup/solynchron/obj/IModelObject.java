/**
 * 
 */
package com.startup.solynchron.obj;

/**
 * This interface declares basic system properties of a model object.
 * 
 * @author plamKaTa
 *
 */
public interface IModelObject extends IAuditable {
	
	public Long getId();

	public void setId(Long id);

	
	public Integer getVersion();

	public void setVersion(Integer version);
	
	public boolean isNew();

}
