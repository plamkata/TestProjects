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

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.MenuFinder;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotMenu.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotMenu extends AbstractSWTBot {

	/**
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotMenu(MenuItem w) throws WidgetNotFoundException {
		super(w);
	}

	/**
	 * Constructs an instance of this with the given finder and the name of the menu to find.
	 * 
	 * @param finder the finder used to find controls.
	 * @param menuName the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu(Finder finder, String menuName) throws WidgetNotFoundException {
		super(finder, menuName);
	}

	/**
	 * Clicks on the menu item
	 */
	public void click() {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		checkEnabled();
		toggleSelection();
		notify(SWT.Selection);
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));
	}

	/**
	 * Toggle the selection of the checkbox if applicable.
	 */
	private void toggleSelection() {
		syncExec(new VoidResult() {
			public void run() {
				if (hasStyle(widget, SWT.CHECK) | hasStyle(widget, SWT.RADIO))
					((MenuItem) widget).setSelection(!((MenuItem) widget).getSelection());
			}
		});
	}

	/**
	 * Gets the menu matching the given name.
	 * 
	 * @param menuName the name of the menu item that is to be found
	 * @return the first menu that matches the menuName
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu menu(final String menuName) throws WidgetNotFoundException {
		MenuItem menuItem = (MenuItem) syncExec(new WidgetResult() {
			public Widget run() {
				Menu bar = ((MenuItem) widget).getMenu();
				List findMenus = new MenuFinder().findMenus(bar, WidgetMatcherFactory.menuMatcher(menuName), true);
				if (!findMenus.isEmpty())
					return (MenuItem) findMenus.get(0);
				return null;
			}
		});
		return new SWTBotMenu(menuItem);
	}

	protected Widget findWidget(int index) throws WidgetNotFoundException {
		return findMenu(getMenuMatcher(text), text);
	}

	protected IMatcher getMatcher() {
		return null;
	}

	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return ((MenuItem) widget).isEnabled();
			}
		});
	}

	/**
	 * Gets if this menu item is checked.
	 * 
	 * @return <code>true</code> if the menu is checked, <code>false</code> otherwise.
	 * @see MenuItem#getSelection()
	 * @since 1.2
	 */
	public boolean isChecked() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return ((MenuItem) widget).getSelection();
			}
		});
	}
}
