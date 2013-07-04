/**
 * 
 */
package com.startup.solynchron;

/**
 * Allows for specification of the execution context. If the 
 * execution context is the same (by value) then the action will be 
 * executed only once.
 * 
 * @author plamKaTa
 *
 */
public abstract class CodeExecutor implements ICodeExecutor {
	
	private Object context;

	/**
	 * Code executors are identified by their context.
	 */
	public CodeExecutor(Object context) {
		this.context = context;
	}

	public Object getContext() {
		return context;
	}
	
	public ICodeExecutor getRootExecutor() {
		CodeExecutor executor = this;
		while (executor.getContext() != null && 
				executor.getContext() instanceof CodeExecutor) {
			executor = (CodeExecutor) executor.getContext();
		}
		
		if (executor.getContext() != null && 
				executor.getContext() instanceof ICodeExecutor) {
			// context is custom executor
			return (ICodeExecutor) executor.getContext();
		} else {
			return executor;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CodeExecutor) {
			CodeExecutor other = (CodeExecutor) obj;
			if (other == null) return false;
			else if (context == null) {
				return super.equals(obj);
			} else {
				return context.equals(other.context);
			}
		} else {
			return super.equals(obj);
		}
	}
	
	@Override
	public int hashCode() {
		if (context != null) {
			return 7 ^ context.hashCode();
		} else {
			return super.hashCode();
		}
	}

}
