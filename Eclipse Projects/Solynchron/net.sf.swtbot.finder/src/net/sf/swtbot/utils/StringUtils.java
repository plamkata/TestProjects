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

import org.eclipse.core.runtime.Assert;

/**
 * A set of utilities for string manipulation.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: StringUtils.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 * @since 1.0
 */
public class StringUtils {

	/**
	 * Joins an array of objects using the given delimiter as spacing.
	 * 
	 * @param toJoin the objects to join into a string.
	 * @param delimiter the delimiter used to join the objects.
	 * @return the result of joining the <code>toJoin</code> with <code>delimiter</code>.
	 */
	public static String join(Object[] toJoin, String delimiter) {
		if ((toJoin == null) || (toJoin.length == 0))
			return "";
		Assert.isTrue(!isEmptyOrNull(delimiter));
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < toJoin.length; i++) {
			Object object = toJoin[i];
			result.append(object);
			result.append(delimiter);
		}

		result.lastIndexOf(delimiter);
		result.replace(result.length() - delimiter.length(), result.length(), "");
		return result.toString();
	}

	/**
	 * Checks if the given string is <code>null</code> or empty.
	 * 
	 * @param string the string.
	 * @return <code>true</code> if string is null, blank or whitespaces. <code>false</code> otherwise.
	 */
	public static boolean isEmptyOrNull(String string) {
		return (string == null) || string.trim().equals("");
	}

	/**
	 * Joins the given integer array with the given delimiter.
	 * 
	 * @param toJoin the integers to join into a string.
	 * @param delimiter the delimiter.
	 * @return the result of joining the <code>toJoin</code> with <code>delimiter</code>.
	 */
	public static String join(int[] toJoin, String delimiter) {
		Integer[] ints = new Integer[toJoin.length];
		for (int i = 0; i < toJoin.length; i++)
			ints[i] = new Integer(toJoin[i]);
		return join(ints, delimiter);
	}
}
