package com.damnhandy.aspects.tracing.impl;

/**
 * @author Ryan J. McDonough
 * @since 1.0
 *
 */
public abstract aspect AbstractTracingAspect {

	abstract pointcut method();
	

	
	Object around(): method() {
		try {
			return proceed();
		}
		finally {
			
		}
	}
	
}
