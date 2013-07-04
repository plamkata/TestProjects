/*******************************************************************************
 *  Copyright 2007 SWTBot, http://swtbot.org/
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/
package net.sf.swtbot.matcher;

/**
 * This is a simple matcher to match the string of an object to the passed in string. Support is also offered to ignore
 * case if required.
 * 
 * @author @author Stephen Paulin &lt;paulin [at] spextreme [dot] com&gt;
 * @version $Id$
 * @since 1.2
 */
public class StringMatcher implements IMatcher {

	/**
	 * The string to compare against.
	 */
	private String	str					= "";
	/** The flag to denote if comparisons should be done with case in mind */
	private boolean	caseSensitiveFlag	= true;

	/**
	 * Constructs the matcher with the given text label to match against.
	 * 
	 * @param label The text string to match against
	 */
	public StringMatcher(String label) {
		this(label, true);
	}

	/**
	 * Constructs the string matcher with the given label and the case sensitivity value.
	 * 
	 * @param label The text string to match against.
	 * @param caseSensitive The case sensitivity flag. <code>true</code> to make the match case sensitive.
	 *            <code>false</code> to ignore case in the match.
	 */
	public StringMatcher(String label, boolean caseSensitive) {
		this.caseSensitiveFlag = caseSensitive;
		this.str = label;
	}

	/**
	 * The description of this matcher.
	 * 
	 * @return The string description.
	 */
	public String description() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("StringMatcher for: ");
		buffer.append(str);
		buffer.append(" using a case ");

		if (caseSensitiveFlag)
			buffer.append("sensitive");
		else
			buffer.append("insensitive");

		return buffer.toString();
	}

	/**
	 * Attempts to match the given object to the given string.
	 * 
	 * @param obj The object to match.
	 * @return <code>true</code> if the string matches. Otherwise <code>false</code>.
	 */
	public boolean match(Object obj) {
		if (caseSensitiveFlag)
			return str.equals(obj.toString());

		return str.equalsIgnoreCase(obj.toString());
	}

}
