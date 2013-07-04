/**
 * 
 */
package com.startup.solynchron.dao;

import com.startup.solynchron.InitializationProgressException;
import com.startup.solynchron.obj.IModelObject;

/**
 * A general interface of all logic objects.
 * 
 * @author plamKaTa
 *
 */
public interface ILogic {
	
	public IModelObject find(Long id) throws InitializationProgressException;
	
	public void dispose();

}
