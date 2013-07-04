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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.TextMatcher;
import net.sf.swtbot.resolvers.DefaultChildrenResolver;
import net.sf.swtbot.resolvers.DefaultParentResolver;
import net.sf.swtbot.resolvers.IChildrenResolver;
import net.sf.swtbot.resolvers.IParentResolver;
import net.sf.swtbot.utils.SWTUtils;

import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * Finds controls matching a particular matcher.
 * 
 * @see UIThreadRunnable
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: ControlFinder.java 772 2008-06-22 04:19:08Z kpadegaonkar $
 */
public class ControlFinder {

	/**
	 * The logging instance for this class.
	 */
	private static final Logger	log							= Logger.getLogger(ControlFinder.class);

	/** The childrenResolver */
	protected IChildrenResolver	childrenResolver;

	/** The display */
	protected Display			display;

	/** The parentResolver */
	protected IParentResolver	parentResolver;

	/**
	 * Set to true if the control finder should find invisible controls. Invisible controls are ones hidden from the
	 * display (isVisible() = false)
	 * 
	 * @since 1.0
	 */
	public boolean				shouldFindInVisibleControls	= false;

	/**
	 * Creates a Control finder using {@link DefaultChildrenResolver} and {@link DefaultParentResolver}.
	 */
	public ControlFinder() {
		this(new DefaultChildrenResolver(), new DefaultParentResolver());
	}

	/**
	 * Creates a control finder using the given resolvers.
	 * 
	 * @param childrenResolver the resolver used to resolve children of a control.
	 * @param parentResolver the resolver used to resolve parent of a control.
	 */
	public ControlFinder(IChildrenResolver childrenResolver, IParentResolver parentResolver) {
		display = SWTUtils.display();
		this.childrenResolver = childrenResolver;
		this.parentResolver = parentResolver;
	}

	/**
	 * Finds the controls in the active shell matching the given matcher.
	 * <p>
	 * Note: This method is thread safe.
	 * </p>
	 * 
	 * @param matcher the matcher used to find controls in the active shell.
	 * @return all controls in the active shell that the matcher matches.
	 * @see Display#getActiveShell()
	 */
	public List findControls(IMatcher matcher) {
		return findControls(activeShell(), matcher, true);
	}

	/**
	 * Finds the controls matching one of the widgets using the given matcher. This will also go recursively though the
	 * {@code widgets} provided.
	 * 
	 * @param widgets the list of widgets.
	 * @param matcher the matcher used to match the widgets.
	 * @param recursive if the match should be recursive.
	 * @return all visible widgets in the children that the matcher matches. If recursive is <code>true</code> then find
	 *         the widgets within each of the widget.
	 */
	public List findControls(final List widgets, final IMatcher matcher, final boolean recursive) {
		return findControlsInternal(widgets, matcher, recursive);
	}

	/**
	 * Returns true if the widget is a control and it is visible.
	 * <p>
	 * This method is not thread safe and must be invoked from the UI thread.
	 * </p>
	 * <p>
	 * TODO visibility of tab items.
	 * </p>
	 * 
	 * @param w the widget
	 * @return <code>true</code> if the control is visible, <code>false</code> otherwise.
	 * @see Control#getVisible()
	 * @since 1.0
	 */
	protected boolean visible(Widget w) {
		if (shouldFindInVisibleControls)
			return true;
		return !((w instanceof Control) && !((Control) w).getVisible());
	}

	/**
	 * Finds the controls starting with the given parent widget and uses the given matcher. If recursive is set, it will
	 * attempt to find the controls recursively in each child widget if they exist.
	 * <p>
	 * This method is thread safe.
	 * </p>
	 * 
	 * @param parentWidget the parent widget in which controls should be found.
	 * @param matcher the matcher used to match the widgets.
	 * @param recursive if the match should be recursive.
	 * @return all visible widgets in the parentWidget that the matcher matches. If recursive is <code>true</code> then
	 *         find the widget within each of the parentWidget.
	 */
	public List findControls(final Widget parentWidget, final IMatcher matcher, final boolean recursive) {
		return UIThreadRunnable.syncExec(display, new ListResult() {
			public List run() {
				return findControlsInternal(parentWidget, matcher, recursive);
			}
		});
	}

	/**
	 * This finds controls using the list of widgets and the matcher. If recursive is set, it will attempt to find the
	 * controls recursively in each child widget if they exist.
	 * <p>
	 * This method is not thread safe and must be invoked from the UI thread.
	 * </p>
	 * 
	 * @see #findControls(List, IMatcher, boolean)
	 */
	private List findControlsInternal(final List widgets, final IMatcher matcher, final boolean recursive) {
		ListOrderedSet list = new ListOrderedSet();
		for (Iterator iter = widgets.iterator(); iter.hasNext();) {
			Widget w = (Widget) iter.next();
			list.addAll(findControlsInternal(w, matcher, recursive));
		}
		return new ArrayList(list);
	}

	/**
	 * Find controls starting from the parent widget using the given matcher. If recursive is set, it will attempt to
	 * find the controls recursively in each child widget if they exist.
	 * <p>
	 * This method is not thread safe and must be invoked from the UI thread.
	 * </p>
	 * 
	 * @see #findControlsInternal(Widget, IMatcher, boolean)
	 */
	private List findControlsInternal(final Widget parentWidget, final IMatcher matcher, final boolean recursive) {
		if ((parentWidget == null) || parentWidget.isDisposed())
			return new ArrayList();
		if (!visible(parentWidget)) {
			if (log.isTraceEnabled())
				log.trace(parentWidget + " is not visible, skipping.");
			return new ArrayList();
		}
		ListOrderedSet controls = new ListOrderedSet();
		if (matcher.match(parentWidget) && !controls.contains(parentWidget))
			controls.add(parentWidget);
		if (recursive) {
			List children = getChildrenResolver().getChildren(parentWidget);
			controls.addAll(findControlsInternal(children, matcher, recursive));
		}
		return new ArrayList(controls);
	}

	/**
	 * Finds the shell matching the given text (shell.getText()).
	 * 
	 * @param text The text on the Shell
	 * @return A Shell containing the specified text
	 */
	public List findShells(String text) {
		return findControls(new TextMatcher(text));
	}

	/**
	 * Gets the registered children resolver. If the resolver had never been set a default resolver will be created.
	 * 
	 * @return the childrenResolver
	 */
	public IChildrenResolver getChildrenResolver() {
		if (childrenResolver == null)
			childrenResolver = new DefaultChildrenResolver();
		return childrenResolver;
	}

	/**
	 * Gets the registered parent resolver. If the resolver was not registered then a default instance will be returned.
	 * 
	 * @return the parentResolver
	 */
	public IParentResolver getParentResolver() {
		if (parentResolver == null)
			parentResolver = new DefaultParentResolver();
		return parentResolver;
	}

	/**
	 * Gets the path to the widget. The path is the list of all parent containers of the widget.
	 * 
	 * @param w the widget.
	 * @return the path to the widget w.
	 */
	public TreePath getPath(Widget w) {
		return new TreePath(getParents(w).toArray());
	}

	/**
	 * Gets the shells registered with the display.
	 * 
	 * @return the shells
	 */
	public Shell[] getShells() {
		return (Shell[]) UIThreadRunnable.syncExec(display, new ObjectResult() {
			public Object run() {
				return display.getShells();
			}
		});
	}

	/**
	 * Return the active shell.
	 * 
	 * @return the active shell.
	 * @see Display#getActiveShell()
	 */
	public Shell activeShell() {
		Shell activeShell = (Shell) UIThreadRunnable.syncExec(display, new WidgetResult() {
			public Widget run() {
				return display.getActiveShell();
			}
		});
		if (activeShell != null)
			return activeShell;
		return (Shell) UIThreadRunnable.syncExec(display, new WidgetResult() {
			public Widget run() {
				Shell[] shells = getShells();
				for (int i = 0; i < shells.length; i++)
					if (shells[i].isFocusControl())
						return shells[i];
				return null;
			}
		});
	}

	private List getParents(final Widget w) {
		return UIThreadRunnable.syncExec(display, new ListResult() {
			public List run() {
				Widget parent = w;
				List parents = new LinkedList();
				while (parent != null) {
					parents.add(parent);
					parent = getParentResolver().getParent(parent);
				}
				Collections.reverse(parents);
				return parents;
			}
		});
	}

}
