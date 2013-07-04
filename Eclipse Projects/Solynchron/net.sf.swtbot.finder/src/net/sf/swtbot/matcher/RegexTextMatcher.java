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
 * Matches widgets if the getText() method of the widget matches the specified regex
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: RegexTextMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public class RegexTextMatcher extends TextMatcher {

	/** The regular expression string. */
	private final String	regex;

	/**
	 * COnstructs the regular expression matcher with the given regular expression stirng.
	 * 
	 * @param regex the regex to match on the {@link org.eclipse.swt.widgets.Widget}
	 */
	public RegexTextMatcher(String regex) {
		super(regex);
		this.regex = regex;
	}

	protected boolean doMatch(Object obj) {
		try {
			return getText(obj).matches(regex);
		} catch (Exception e) {
			// do nothing
		}
		return false;
	}

	public String description() {
		return "RegexTextMatcher for: " + regex;
	}
}
