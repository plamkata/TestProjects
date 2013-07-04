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
package net.sf.swtbot.finder;

import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Finds context menus for a given control.
 * 
 * @see UIThreadRunnable
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ContextMenuFinder.java 783 2008-06-23 07:27:51Z kpadegaonkar $
 */
public class ContextMenuFinder extends MenuFinder {

	/**
	 * The control to find context menus.
	 */
	private final Control	control;

	/**
	 * Constructs the context menu finder for the given control to be searched.
	 * 
	 * @param control the control that has a context menu.
	 */
	public ContextMenuFinder(Control control) {
		super();
		Assert.isNotNull(control, "The control cannot be null");
		this.control = control;
	}

	/**
	 * Gets the menubar for the given shell.
	 * 
	 * @see net.sf.swtbot.finder.MenuFinder#menuBar(org.eclipse.swt.widgets.Shell)
	 * @param shell The shell to find the menu bar for.
	 * @return The menu bar found.
	 */
	protected Menu menuBar(final Shell shell) {
		return (Menu) UIThreadRunnable.syncExec(display, new WidgetResult() {
			public Widget run() {
				return control.getMenu();
			}
		});
	}
}
