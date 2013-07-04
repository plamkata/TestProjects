/*******************************************************************************
 * Copyright 2007 SWTBot, http://swtbot.org/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.swtbot.utils;

/**
 * A utility for class based work.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ClassUtils.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 */
public abstract class ClassUtils {

	/**
	 * Gets the simple class name of an object or an empty string if not valid.
	 * 
	 * @param object the object
	 * @return the classname of the object or an empty string.
	 */
	public static String simpleClassName(Object object) {
		return object == null ? "" : ClassUtils.simpleClassName(object.getClass());
	}

	/**
	 * Gets the simple class name for the given class.
	 * 
	 * @param clasz the class
	 * @return the classname of the clasz
	 */
	public static String simpleClassName(Class clasz) {
		if (clasz == null)
			return "";
		String claszName = clasz.getName();
		return claszName.substring(claszName.lastIndexOf(".") + 1);
	}
}
