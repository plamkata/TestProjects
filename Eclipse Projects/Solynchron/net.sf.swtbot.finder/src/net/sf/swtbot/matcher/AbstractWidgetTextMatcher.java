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

import net.sf.swtbot.utils.ClassUtils;

/**
 * Matches a widget of a given type and text on it.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: AbstractWidgetTextMatcher.java 779 2008-06-22 20:05:32Z kpadegaonkar $
 */
public abstract class AbstractWidgetTextMatcher extends AbstractWidgetMatcher {

	/** The type of the widget */
	protected final ClassMatcher	classMatcher;
	/** the text on the widget */
	protected final String			text;
	/** The matcher that matches the {@link #text} */
	protected final TextMatcher		textMatcher;
	/** The description, used for logging, etc. */
	private String					description;

	/**
	 * Constructs the widget text matcher. This is an abstrct class so it should be extended.
	 * 
	 * @param text the text to match on a widget.
	 */
	public AbstractWidgetTextMatcher(String text) {
		this.text = text;
		textMatcher = getTextMatcher();
		classMatcher = new ClassMatcher(getWidgetClass());
	}

	/**
	 * Subclasses must return a subclassof {@link org.eclipse.swt.widgets.Widget}.
	 * 
	 * @return a subclass of {@link org.eclipse.swt.widgets.Widget}
	 */
	protected abstract Class getWidgetClass();

	/**
	 * Subclasses may return a different text matcher
	 * 
	 * @return a {@link TextMatcher}
	 */
	protected TextMatcher getTextMatcher() {
		return new MnemonicTextMatcher(text);
	}

	protected boolean doMatch(Object obj) {
		return classMatcher.doMatch(obj) && textMatcher.doMatch(obj);
	}

	public String description() {
		if (description == null)
			description = "Matcher for widget [" + ClassUtils.simpleClassName(classMatcher.clazz) + "] with text [" + text + "]";
		return description;
	}
}
