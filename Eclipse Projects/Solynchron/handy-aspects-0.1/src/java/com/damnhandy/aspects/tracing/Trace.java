/**
 * 
 */
package com.damnhandy.aspects.tracing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Ryan J. McDonough
 * @since 1.0
 */
@Target({ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Trace {

}
