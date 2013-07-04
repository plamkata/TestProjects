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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Matches {@link org.eclipse.swt.widgets.Widget}s with the specified text. Skips mnemonics, so "Username" will match
 * items with text "&amp;Username" and "User&amp;name"
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: MnemonicTextMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public class MnemonicTextMatcher extends TextMatcher {
	/**
	 * Constructs the Mnemonic text matcher with the given text.
	 * 
	 * @param text the mnemonic to match on the {@link org.eclipse.swt.widgets.Widget}
	 */
	public MnemonicTextMatcher(String text) {
		super(text);
	}

	/**
	 * Constructs the Mnemonic text matcher with the given text.
	 * 
	 * @param text the mnemonic to match on the {@link org.eclipse.swt.widgets.Widget}
	 * @param ignoreCase Determines if this should ignore case during the comparison.
	 * @since 1.2
	 */
	public MnemonicTextMatcher(String text, boolean ignoreCase) {
		super(text, ignoreCase);
	}

	/**
	 * Extends the behaviour of TextMatcher my removing the mnemonics "&amp;" that are used as keyboard accessors from
	 * the text.
	 * 
	 * @see net.sf.swtbot.matcher.TextMatcher#getText(java.lang.Object)
	 * @param obj The object to get the text from.
	 * @return The newly formated string.
	 * @throws NoSuchMethodException if the method "getText" does not exist on the object.
	 * @throws IllegalAccessException if the java access control does not allow invocation.
	 * @throws InvocationTargetException if the method "getText" throws an exception.
	 * @see Method#invoke(Object, Object[])
	 */
	String getText(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return super.getText(obj).replaceAll("&", "");
	}
}
