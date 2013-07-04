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

import java.util.Arrays;
import java.util.List;

/**
 * Matches if any of the matchers matches.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: DecoratingOrMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public class DecoratingOrMatcher extends AbstractWidgetMatcher {

	/** A list of matchers to use. */
	private final IMatcher[]	matchers;

	/**
	 * Constructs an instance of the matcher with the set of matchers to be used.
	 * 
	 * @param matcher1 the first matcher.
	 * @param matcher2 the second matcher.
	 */
	public DecoratingOrMatcher(IMatcher matcher1, IMatcher matcher2) {
		this(new IMatcher[] { matcher1, matcher2 });
	}

	/**
	 * Construcsts a matcher using the given list.
	 * 
	 * @param matchers the matchers
	 */
	public DecoratingOrMatcher(List matchers) {
		this((IMatcher[]) matchers.toArray(new IMatcher[0]));
	}

	/**
	 * Constructs the matcher with the given array of matchers.
	 * 
	 * @param matchers the matchers
	 */
	public DecoratingOrMatcher(IMatcher[] matchers) {
		this.matchers = matchers;
	}

	protected boolean doMatch(Object obj) {
		for (int i = 0; i < matchers.length; i++) {
			IMatcher matcher = matchers[i];
			if (matcher.match(obj))
				return true;
		}
		return false;
	}

	public String description() {
		return "DecoratingOrMatcher for any of " + Arrays.asList(matchers);
	}

}
