/**
 * 
 */
package com.damnhandy.aspects.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark properties of an @Observable root class with an @Observable 
 * type as @Nested in order to add property change support for nested 
 * properties. Once an @Observable property is marked as @Nested the 
 * root class instances will fire property change events for any change 
 * that occurred in a non-null @Observable nested property instance. 
 * <p>
 * In order to listen for nested property changes the user should register
 * property change listeners for {basePropertyName}.{subPropertyName}. 
 * I.e. nested properties are specified by normal properties separated 
 * with a '.'. 
 * 
 * @author P. Alexandrov
 * @since 1.1
 * 
 * @see Observable
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface Nested { }
