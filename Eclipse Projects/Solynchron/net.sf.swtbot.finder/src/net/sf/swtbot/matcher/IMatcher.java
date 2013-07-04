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
package net.sf.swtbot.matcher;

/**
 * This interface is used as a filter by {@link net.sf.swtbot.finder.ControlFinder} to find widgets, and by
 * {@link net.sf.swtbot.finder.MenuFinder} to finnd menu items.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: IMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public interface IMatcher {

	/**
	 * Performs the comparison and returns the result of the comparison.
	 * 
	 * @param w the object to match
	 * @return <code>true</code> if the matcher can match the object, <code>false</code> otherwise.
	 */
	public boolean match(Object w);

	/**
	 * Gets the description of this matcher.
	 * 
	 * @return the string representation of this matcher, purely for display reasons.
	 */
	public String description();

}
