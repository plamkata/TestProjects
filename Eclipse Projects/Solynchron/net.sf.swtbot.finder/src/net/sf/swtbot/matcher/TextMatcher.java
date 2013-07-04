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

import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.widgets.Text;

/**
 * Matches widgets if the getText() method of the widget matches the specified text.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TextMatcher.java 870 2008-07-30 20:21:49Z spextreme $
 */
public class TextMatcher extends AbstractWidgetMatcher {

	/** The text */
	protected String	text;

	/**
	 * A flag to denote if this should ignore case.
	 * 
	 * @since 1.2
	 */
	protected boolean	ignoreCase	= false;

	/**
	 * Constructs this matcher with the given text.
	 * 
	 * @param text the text to match on the {@link org.eclipse.swt.widgets.Widget}
	 */
	public TextMatcher(String text) {
		this(text, false);
	}

	/**
	 * Constructs this matcher with the given text.
	 * 
	 * @param text the text to match on the {@link org.eclipse.swt.widgets.Widget}
	 * @param ignoreCase Determines if this should ignore case during the comparison.
	 * @since 1.2
	 */
	public TextMatcher(String text, boolean ignoreCase) {
		text = text.replaceAll("\\r\\n", "\n");
		text = text.replaceAll("\\r", "\n");
		this.text = text;
		this.ignoreCase = ignoreCase;
	}

	// FIXME: optimize the if() code block, use strategy or something else.
	protected boolean doMatch(Object obj) {
		try {
			String widgetText = getText(obj);
			widgetText = widgetText.replaceAll(Text.DELIMITER, "\n");
			boolean result = false;
			if (ignoreCase)
				result = widgetText.equalsIgnoreCase(text);
			else
				result = widgetText.equals(text);
			return result;
		} catch (Exception e) {
			// do nothing
		}
		return false;
	}

	/**
	 * Gets the text of the object using the getText method. If the object doesn't contain a get text method an
	 * exception is thrown.
	 * 
	 * @param obj any object to get the text from.
	 * @return the return value of obj#getText()
	 * @throws NoSuchMethodException if the method "getText" does not exist on the object.
	 * @throws IllegalAccessException if the java access control does not allow invocation.
	 * @throws InvocationTargetException if the method "getText" throws an exception.
	 * @see Method#invoke(Object, Object[])
	 */
	String getText(Object obj) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return (String) SWTUtils.invokeMethod(obj, "getText");
	}

	public String description() {
		return "TextMatcher for: " + text;
	}

}
