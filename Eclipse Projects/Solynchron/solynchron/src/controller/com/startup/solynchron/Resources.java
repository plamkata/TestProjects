/**
 * 
 */
package com.startup.solynchron;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author plamKaTa
 *
 */
public class Resources {
	
	// resource bundle for multi-language support
	private static ResourceBundle resourceBundle;

	static {
		try {
			resourceBundle = ResourceBundle
					.getBundle("com.startup.solynchron.SolynchronResources"); //$NON-NLS-1$
		} catch (MissingResourceException ex) {
			resourceBundle = null;
			Activator.log(ex);
		}
	}
	
	public static String getResource(String key) {
		String strKey = '!' + key + '!';
		try {
			if (resourceBundle != null) {
				return resourceBundle.getString(key);
			} else return strKey;
		} catch (MissingResourceException e) {
			return strKey;
		}
	}

}
