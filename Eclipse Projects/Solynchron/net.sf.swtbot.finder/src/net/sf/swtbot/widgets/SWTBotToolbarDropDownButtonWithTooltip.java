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

import org.eclipse.swt.widgets.ToolItem;

/**
 * This represents a toolbar item that is a drop down button with a tooltip.
 * 
 * @since 1.2
 */
public class SWTBotToolbarDropDownButtonWithTooltip extends SWTBotToolbarDropDownButton {

	/**
	 * Constructs an instance of this object with the given data.
	 * 
	 * @param finder the finder used to find controls.
	 * @param toolTip the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarDropDownButtonWithTooltip(Finder finder, String toolTip) throws WidgetNotFoundException {
		super(finder, toolTip);
	}

	/**
	 * Constructs an instance of this object with the given data.
	 * 
	 * @param finder the finder used to find controls.
	 * @param index the index of the shell in case multiple shells have the same text.
	 * @param toolTip the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarDropDownButtonWithTooltip(Finder finder, String toolTip, int index) throws WidgetNotFoundException {
		super(finder, toolTip, index);
	}

	/**
	 * Constructs an instance of this object with the given widget.
	 * 
	 * @param w the tool item.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarDropDownButtonWithTooltip(ToolItem w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * @see net.sf.swtbot.widgets.SWTBotToolbarDropDownButton#getMatcher()
	 */
	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.toolbarDropDownButtonToolTipMatcher(text);
	}

}
