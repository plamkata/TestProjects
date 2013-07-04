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

import java.util.List;

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;

/**
 * This represents a toolbar item that is a drop down button.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotToolbarDropDownButton.java 811 2008-06-29 04:33:16Z kpadegaonkar $
 * @since 1.2
 */
public class SWTBotToolbarDropDownButton extends SWTBotToolbarButton {

	/**
	 * Construcst an new instance of this item.
	 * 
	 * @param w the tool item.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotToolbarDropDownButton(ToolItem w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Construcst an new instance of this item.
	 * 
	 * @param finder the finder used to find controls.
	 * @param index the index of the shell in case multiple shells have the same text.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarDropDownButton(Finder finder, String text, int index) throws WidgetNotFoundException {
		super(finder, text, index);
	}

	/**
	 * Construcst an new instance of this item.
	 * 
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotToolbarDropDownButton(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.toolbarDropDownButtonMatcher(text);
	}

	/**
	 * Finds the submenu inside this menu item.
	 * 
	 * @param menuItem the submenu to search
	 * @return the menu item with the specified text
	 * @throws WidgetNotFoundException if the menuItem could not be found
	 * @since 1.0
	 */
	public SWTBotMenu menuItem(String menuItem) throws WidgetNotFoundException {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		checkEnabled();
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.MouseUp);
		notify(SWT.Selection, arrowEvent());
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));

		List findMenus = finder.findMenus((Shell) new SWTBot().activeShell().widget, WidgetMatcherFactory.menuMatcher(menuItem), true);
		log.debug(findMenus);
		if (findMenus.isEmpty())
			throw new WidgetNotFoundException("Could not find a menu item");
		return new SWTBotMenu((MenuItem) findMenus.get(0));
	}

	/**
	 * Gets the arrow event.
	 * 
	 * @return The event.
	 */
	private Event arrowEvent() {
		Event event = createEvent();
		event.detail = SWT.ARROW;
		return event;
	}
}
