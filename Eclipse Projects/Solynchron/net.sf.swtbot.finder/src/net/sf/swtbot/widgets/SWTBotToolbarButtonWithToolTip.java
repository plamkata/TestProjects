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
package net.sf.swtbot.widgets;

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;

import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This represents a {@link ToolBar} with a button and tooltip.
 *
 * @author Sathyan S Nair.
 * @version $Id: SWTBotToolbarButtonWithToolTip.java 882 2008-08-06 17:48:42Z kpadegaonkar $
 * @since 1.0
 */
public class SWTBotToolbarButtonWithToolTip extends SWTBotToolbarButton {

	/**
	 * Construcats an instance of the object.
	 *
	 * @param finder the finder used to find controls.
	 * @param index the index of the shell in case multiple shells have the same text.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarButtonWithToolTip(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	/**
	 * Construcats an instance of the object.
	 *
	 * @param finder the finder used to find controls.
	 * @param tooltip the toolTip on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarButtonWithToolTip(Finder finder, String tooltip) throws WidgetNotFoundException {
		super(finder, tooltip);
	}

	/**
	 * Constructs an instance of this object with the underlying toolbar item.
	 *
	 * @param w Tht toolbar item widet.
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @since 1.2
	 */
	public SWTBotToolbarButtonWithToolTip(ToolItem w) throws WidgetNotFoundException {
		super(w);
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.toolbarButtonToolTipMatcher(text);
	}

}
