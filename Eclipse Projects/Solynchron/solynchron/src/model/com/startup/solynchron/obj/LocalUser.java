/**
 * 
 */
package com.startup.solynchron.obj;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * The native local user of the plug-in.
 * 
 * @author plamKaTa
 *
 */
@Entity
@DiscriminatorValue("local")
public class LocalUser extends MyUser {

	/**
	 * 
	 */
	public LocalUser() {
		super();
	}

}
