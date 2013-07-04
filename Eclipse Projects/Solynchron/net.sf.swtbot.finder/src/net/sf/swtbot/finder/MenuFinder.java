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

import java.util.ArrayList;
import java.util.List;

import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.utils.SWTUtils;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Finds menus matching a particular matcher.
 *
 * @see UIThreadRunnable
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: MenuFinder.java 881 2008-08-06 17:47:30Z kpadegaonkar $
 */
public class MenuFinder {

	/**
	 * The logging instance for this class.
	 */
	private static final Logger	log	= Logger.getLogger(MenuFinder.class);

	/** The display */
	protected final Display		display;

	/**
	 * Creates a MenuFinder.
	 */
	public MenuFinder() {
		display = SWTUtils.display();
	}

	/**
	 * Finds a menu matching the given item in all available shells. If recursive is set, it will attempt to find the
	 * controls recursively in each of the menus it that is found.
	 *
	 * @param matcher the matcher that can match menus and menu items.
	 * @return all menus in all shells that match the matcher.
	 */
	public List findMenus(IMatcher matcher) {
		return findMenus(getShells(), matcher, true);
	}

	/**
	 * Finds all the menus using the given matcher in the set of shells provided. If recursive is set, it will attempt
	 * to find the controls recursively in each of the menus it that is found.
	 *
	 * @param shells the shells to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified shells that match the matcher.
	 */
	public List findMenus(Shell[] shells, IMatcher matcher, boolean recursive) {
		ListOrderedSet result = new ListOrderedSet();
		for (int i = 0; i < shells.length; i++)
			result.addAll(findMenus(shells[i], matcher, recursive));
		return new ArrayList(result);
	}

	/**
	 * Finds the menus in the given shell using the given matcher. If recursive is set, it will attempt to find the
	 * controls recursively in each of the menus it that is found.
	 *
	 * @param shell the shell to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified shell that match the matcher.
	 */
	public List findMenus(final Shell shell, IMatcher matcher, boolean recursive) {
		ListOrderedSet result = new ListOrderedSet();
		result.addAll(findMenus(menuBar(shell), matcher, recursive));
		return new ArrayList(result);
	}

	/**
	 * Gets the menu bar in the given shell.
	 *
	 * @param shell the shell.
	 * @return the menu in the shell.
	 * @see Shell#getMenuBar()
	 */
	protected Menu menuBar(final Shell shell) {
		return (Menu) UIThreadRunnable.syncExec(display, new WidgetResult() {
			public Widget run() {
				return shell.getMenuBar();
			}
		});

	}

	/**
	 * Finds all the menus in the given menu bar matching the given matcher. If recursive is set, it will attempt to
	 * find the controls recursively in each of the menus it that is found.
	 *
	 * @param bar the menu bar
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified menubar that match the matcher.
	 */
	public List findMenus(final Menu bar, final IMatcher matcher, final boolean recursive) {
		return UIThreadRunnable.syncExec(display, new ListResult() {
			public List run() {
				return findMenusInternal(bar, matcher, recursive);
			}
		});
	}

	/**
	 * Gets all of the shells in the current display.
	 *
	 * @return all shells in the display.
	 * @see Display#getShells()
	 */
	protected Shell[] getShells() {
		return (Shell[]) UIThreadRunnable.syncExec(display, new ObjectResult() {
			public Object run() {
				return display.getShells();
			}
		});
	}

	/**
	 * @param bar
	 * @param matcher
	 * @param recursive
	 * @return
	 */
	private List findMenusInternal(final Menu bar, final IMatcher matcher, final boolean recursive) {
		ListOrderedSet result = new ListOrderedSet();
		if (bar != null) {
			bar.notifyListeners(SWT.Show, new Event());
			MenuItem[] items = bar.getItems();
			for (int i = 0; i < items.length; i++) {
				MenuItem menuItem = items[i];
				if (isSeparator(menuItem)) {
					if (log.isTraceEnabled())
						log.trace(menuItem + " is a separator, skipping.");
					continue;
				}
				if (matcher.match(menuItem))
					result.add(menuItem);
				if (recursive)
					result.addAll(findMenusInternal(menuItem.getMenu(), matcher, recursive));
			}
			bar.notifyListeners(SWT.Hide, new Event());
		}
		return new ArrayList(result);
	}

	private boolean isSeparator(MenuItem menuItem) {
		// FIXME see https://bugs.eclipse.org/bugs/show_bug.cgi?id=208188
		// FIXED > 20071101 https://bugs.eclipse.org/bugs/show_bug.cgi?id=208188#c2
		return (menuItem.getStyle() & SWT.SEPARATOR) != 0;
	}

}
