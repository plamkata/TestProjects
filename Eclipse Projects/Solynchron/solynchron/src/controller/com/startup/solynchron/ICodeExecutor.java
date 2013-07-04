/**
 * 
 */
package com.startup.solynchron;

import javax.persistence.EntityTransaction;

/**
 * The code executor allows for execution of custom code in a managed environment.
 * For example, custom code may be executed in a database transaction without having
 * to perform all those transaction operations: {@link EntityTransaction#begin()}, 
 * {@link EntityTransaction#commit()} and {@link EntityTransaction#rollback()} in a 
 * properly defined try catch block. The functionality can be used also to execute 
 * custom code in a background thread or for lazy execution without having to use 
 * any heavy syntax.
 * 
 * @author plamKaTa
 *
 */
public interface ICodeExecutor {

	/**
	 * A general method structure to be implemented by the caller for executing 
	 * the custom code. Any user exception thrown in the custom code should be 
	 * properly handled in the calling fragment. This dose not include exceptions 
	 * thrown by the persistence framework when executing custom code in a transaction or
	 * cancellation exceptions when executing custom code in a background thread.
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Object execute(Object[] params) throws Exception;
}
