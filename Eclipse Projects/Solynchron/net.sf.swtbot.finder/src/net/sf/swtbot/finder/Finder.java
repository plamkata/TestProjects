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

import java.util.List;

import net.sf.swtbot.matcher.IMatcher;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * A wrapper around {@link ControlFinder} and {@link MenuFinder} that delegates to either of them.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: Finder.java 782 2008-06-23 04:44:49Z kpadegaonkar $
 */
public class Finder {

	/**
	 * The control finder to use.
	 */
	private final ControlFinder	controlFinder;
	/**
	 * The menu finder to use.
	 */
	private final MenuFinder	menuFinder;
	/**
	 * The display to use.
	 */
	private final Display		display;

	/**
	 * Constructs the finder with the given control and menu finder.
	 * 
	 * @param controlFinder the finder that finds controls.
	 * @param menuFinder the finder that finds menus.
	 */
	public Finder(ControlFinder controlFinder, MenuFinder menuFinder) {
		this.controlFinder = controlFinder;
		this.menuFinder = menuFinder;
		display = controlFinder.display;
	}

	/**
	 * Establishes the finder from an existing finder (control finder only) and the given new menu finder.
	 * 
	 * @param finder the finder
	 * @param menuFinder the finder that finds menus.
	 */
	public Finder(Finder finder, MenuFinder menuFinder) {
		this(finder.controlFinder, menuFinder);
	}

	/**
	 * Gets the currently active shell.
	 * 
	 * @return the active shell.
	 * @see ControlFinder#activeShell()
	 */
	public Shell activeShell() {
		return controlFinder.activeShell();
	}

	/**
	 * Finds the controls in the active shell matching the given matcher.
	 * <p>
	 * This method is thread safe.
	 * </p>
	 * 
	 * @param matcher the matcher used to find controls in the active shell.
	 * @return all controls in the active shell that the matcher matches.
	 * 
	 * @see ControlFinder#findControls(IMatcher)
	 */
	public List findControls(IMatcher matcher) {
		return controlFinder.findControls(matcher);
	}

	/**
	 * Finds the controls matching one of the widgets using the given matcher. If recursive is set, it will attempt to
	 * recursively find the controls in each {@code children} widget if they exist.
	 * 
	 * @param children the list of widgets.
	 * @param matcher the matcher used to match the widgets.
	 * @param recursive if the match should be recursive.
	 * @return all visible widgets in the children that the matcher matches. If recursive is <code>true</code> then find
	 *         the widgets within each of the widget.
	 * 
	 * @see ControlFinder#findControls(List, IMatcher, boolean)
	 */
	public List findControls(List children, IMatcher matcher, boolean recursive) {
		return controlFinder.findControls(children, matcher, recursive);
	}

	/**
	 * Finds the controls starting with the given parent widget and uses the given matcher. If recursive is set, it will
	 * attempt to find the controls in each child widget if they exist.
	 * <p>
	 * This method is thread safe.
	 * </p>
	 * 
	 * @param widget the parent widget in which controls should be found.
	 * @param matcher the matcher used to match the widgets.
	 * @param recursive if the match should be recursive.
	 * @return all visible widgets in the parentWidget that the matcher matches. If recursive is <code>true</code> then
	 *         find the widget within each of the parentWidget.
	 * 
	 * @see ControlFinder#findControls(Widget, IMatcher, boolean)
	 */
	public List findControls(Widget widget, IMatcher matcher, boolean recursive) {
		return controlFinder.findControls(widget, matcher, recursive);
	}

	/**
	 * Finds the shell matching the given text (shell.getText()).
	 * 
	 * @param text The text on the Shell
	 * @return A Shell containing the specified text
	 * 
	 * @see ControlFinder#findShells(String)
	 */
	public List findShells(String text) {
		return controlFinder.findShells(text);
	}

	/**
	 * Gets the shells registered with the display.
	 * 
	 * @return the shells
	 * 
	 * @see ControlFinder#getShells()
	 */
	public Shell[] getShells() {
		return controlFinder.getShells();
	}

	/**
	 * Finds a menu matching the given item in all available shells. This searches for menus recursively.
	 * 
	 * @param matcher the matcher that can match menus and menu items.
	 * @return all menus in all shells that match the matcher.
	 * 
	 * @see MenuFinder#findMenus(IMatcher)
	 */
	public List findMenus(IMatcher matcher) {
		return menuFinder.findMenus(matcher);
	}

	/**
	 * Fins all the menus in the given menu bar matching the given matcher. If recursive is set, it will attempt to find
	 * the controls recursively in each of the menus it that is found.
	 * 
	 * @param bar the menu bar
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified menubar that match the matcher.
	 * 
	 * @see MenuFinder#findMenus(Menu, IMatcher, boolean)
	 */
	public List findMenus(Menu bar, IMatcher matcher, boolean recursive) {
		return menuFinder.findMenus(bar, matcher, recursive);
	}

	/**
	 * Finds the menus in the given shell using the given matcher. If recursive is set, it will attempt to find the
	 * controls recursively in each of the menus it that is found.
	 * 
	 * @param shell the shell to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified shell that match the matcher.
	 * 
	 * @see MenuFinder#findMenus(Shell, IMatcher, boolean)
	 */
	public List findMenus(Shell shell, IMatcher matcher, boolean recursive) {
		return menuFinder.findMenus(shell, matcher, recursive);
	}

	/**
	 * Finds all the menus using the given matcher in the set of shells provided. If recursive is set, it will attempt
	 * to find the controls recursively in each of the menus it that is found.
	 * 
	 * @param shells the shells to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to true, will find sub-menus as well.
	 * @return all menus in the specified shells that match the matcher.
	 * 
	 * @see MenuFinder#findMenus(Shell[], IMatcher, boolean)
	 */
	public List findMenus(Shell[] shells, IMatcher matcher, boolean recursive) {
		return menuFinder.findMenus(shells, matcher, recursive);
	}

	/**
	 * Checks if this should return items that are not visible when performing the search for controls.
	 * 
	 * @return <code>true</code> if the finder should return items that are not visible. Otherwise <code>false</code> is
	 *         returned to show no items will be returned if they are not visible.
	 * @since 1.0
	 */
	public boolean shouldFindInvisibleControls() {
		return controlFinder.shouldFindInVisibleControls;
	}

	/**
	 * This sets the flag to know if items should be returned if they are not visible.
	 * 
	 * @param shouldFindInVisibleControls <code>true</code> to cause controls that are not visible to be found. Use
	 *            false so that controls that are visible will be returned.
	 * @since 1.0
	 */
	public void setShouldFindInvisibleControls(boolean shouldFindInVisibleControls) {
		controlFinder.shouldFindInVisibleControls = shouldFindInVisibleControls;
	}

	/**
	 * Gets the display that has been set.
	 * 
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}

}