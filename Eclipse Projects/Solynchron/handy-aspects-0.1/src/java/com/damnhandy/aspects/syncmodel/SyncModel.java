/**
 * 
 */
package com.damnhandy.aspects.syncmodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Ryan J. McDonough
 * @since 1.0
 * @version $Revision: 1.1 $
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(value=RetentionPolicy.RUNTIME)
public @interface SyncModel {

}
