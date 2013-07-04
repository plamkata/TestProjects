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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.ContextMenuFinder;
import net.sf.swtbot.finder.ControlFinder;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.MenuFinder;
import net.sf.swtbot.finder.UIThreadRunnable;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.ClassUtils;
import net.sf.swtbot.utils.SWTBotEvents;
import net.sf.swtbot.utils.SWTBotPreferences;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.DefaultCondition;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Helper to find SWT {@link Widget}s and perform operations on them.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: AbstractSWTBot.java 848 2008-07-21 14:26:11Z kpadegaonkar $
 */
public abstract class AbstractSWTBot {

	/** The logger. */
	protected static Logger	log;
	/** With great power comes great responsibility, use carefully. */
	public Widget			widget;
	/** With great power comes great responsibility, use carefully. */
	public final Display	display;
	/** The text to look for on the widget */
	protected String		text;
	/** Widgets that have the same {@link #text} as the one identified by {@link #widget} */
	protected List			similarWidgets;
	/** The finder used to find widgets. */
	protected Finder		finder;

	/**
	 * Constructs a new instance with the given widget.
	 *
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public AbstractSWTBot(Widget w) throws WidgetNotFoundException {
		if (w == null)
			throw new WidgetNotFoundException("The widget was null.");
		if (w.isDisposed())
			throw new WidgetNotFoundException("The widget was disposed." + SWTUtils.toString(w));
		finder = new Finder(new ControlFinder(), new MenuFinder());
		widget = w;
		display = w.getDisplay();
		log = Logger.getLogger(getClass());
	}

	/**
	 * Constructs a new instance with the given finder.
	 *
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public AbstractSWTBot(Finder finder, String text) throws WidgetNotFoundException {
		this(finder, text, 0);
	}

	/**
	 * This constructor calls {@link #findControls(IMatcher)} to find all controls matching {@link #matcher()}, in case
	 * a widget is not found, the constructor retries until a timeout occurs. The first such control is set into
	 * {@link #widget} the other controls, if any that match {@link #matcher()} are held in the {@link #similarWidgets}
	 * list.
	 *
	 * @see SWTBot#waitUntil(net.sf.swtbot.wait.ICondition)
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @param index the index of the control in case multiple controls are found.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public AbstractSWTBot(Finder finder, String text, final int index) throws WidgetNotFoundException {
		display = finder.getDisplay();
		this.finder = finder;
		this.text = text;
		log = Logger.getLogger(getClass());
		try {
			new SWTBot().waitUntil(new DefaultCondition() {
				public boolean test() throws Exception {
					similarWidgets = findControls(matcher());
					widget = findWidget(index);
					return widget != null;

				}

				public String getFailureMessage() {
					return "Could not find widget";
				}
			});
		} catch (TimeoutException e) {
			throw new WidgetNotFoundException("Could not find widget with text " + text, e);
		}
	}

	/**
	 * Finds the widget at the given index.
	 *
	 * @param index the index of the widget, in case there are multiple widgets.
	 * @return the widget matching {@link #text}, this is set into the {@link #widget} field by
	 *         {@link #AbstractSWTBot(Finder, String)}
	 * @throws WidgetNotFoundException if a widget is not found.
	 * @since 1.0
	 */
	protected Widget findWidget(int index) throws WidgetNotFoundException {
		return findControl(index);
	}

	/**
	 * Finds a widget at index 0.
	 * <p>
	 * The findWidget( int index ) method should be used instead.
	 * </p>
	 *
	 * @return the widget matching {@link #text}, this is set into the {@link #widget} field by
	 *         {@link #AbstractSWTBot(Finder, String)}
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @deprecated Use {@link #findWidget(int)}
	 */
	protected Widget findWidget() throws WidgetNotFoundException {
		return findWidget(0);
	}

	/**
	 * Gets the matcher instance.
	 *
	 * @return {@link #getMatcher()} if it is not <code>null</code>, else returns
	 *         {@link WidgetMatcherFactory#nullMatcher()}.
	 */
	protected IMatcher matcher() {
		IMatcher matcher = getMatcher();
		if (matcher == null)
			return WidgetMatcherFactory.nullMatcher();
		return matcher;
	}

	/**
	 * Gets the matcher.
	 *
	 * @return a matcher used to find controls. Subclasses must override.
	 */
	protected abstract IMatcher getMatcher();

	/**
	 * Sends a non-blocking notification of the specified type to the widget.
	 *
	 * @param eventType the event type.
	 * @see Widget#notifyListeners(int, Event)
	 */
	protected void notify(final int eventType) {
		notify(eventType, createEvent());
	}

	/**
	 * Sends a non-blocking notification of the specified type to the {@link #widget}.
	 *
	 * @param eventType the type of event.
	 * @param createEvent the event to be sent to the {@link #widget}.
	 */
	protected void notify(final int eventType, final Event createEvent) {
		notify(eventType, createEvent, widget);
	}

	/**
	 * Sends a non-blocking notification of the specified type to the widget.
	 *
	 * @param eventType the type of event.
	 * @param createEvent the event to be sent to the {@link #widget}.
	 * @param widget the widget to send the event to.
	 */
	protected void notify(final int eventType, final Event createEvent, final Widget widget) {
		final String eventString = SWTBotEvents.toString(eventType) + createEvent.toString();
		if (log.isTraceEnabled())
			log.trace("Enquing event " + eventString + " on " + SWTUtils.toString(widget));
		asyncExec(new VoidResult() {
			public void run() {
				if ((widget == null) || widget.isDisposed()) {
					if (log.isTraceEnabled())
						log.trace("Not notifying " + SWTUtils.toString(widget) + " is null or has been disposed");
					return;
				}
				if (!isEnabledInternal()) {
					log.warn("Widget is not enabled: " + SWTUtils.toString(widget));
					return;
				}
				if (log.isTraceEnabled())
					log.trace("Sending event " + eventString + " to " + widget);
				widget.notifyListeners(eventType, createEvent);
				if (log.isDebugEnabled())
					log.debug("Sent event " + eventString + " to " + widget);
			}
		});

		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				// do nothing, just wait for sync.
			}
		});

		long playbackDelay = SWTBotPreferences.playbackDelay();
		if (playbackDelay > 0)
			sleep(playbackDelay);
	}

	/**
	 * Sleeps for millis milliseconds. Delegate to {@link SWTUtils#sleep(long)}
	 *
	 * @param millis the time in milli seconds
	 */
	protected static void sleep(long millis) {
		SWTUtils.sleep(millis);
	}

	/**
	 * Creates an event.
	 *
	 * @return an event that encapsulates {@link #widget} and {@link #display}. Subclasses may override to set other
	 *         event properties.
	 */
	protected Event createEvent() {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = display;
		return event;
	}

	/**
	 * Create a mouse event
	 *
	 * @param x the x co-ordinate of the mouse event.
	 * @param y the y co-ordinate of the mouse event.
	 * @param button the mouse button that was clicked.
	 * @param stateMask the state of the keyboard modifier keys.
	 * @param count the number of times the mouse was clicked.
	 * @return an event that encapsulates {@link #widget} and {@link #display}
	 * @since 1.2
	 */
	protected Event createMouseEvent(int x, int y, int button, int stateMask, int count) {
		Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = widget;
		event.display = display;
		event.x = x;
		event.y = y;
		event.button = button;
		event.stateMask = stateMask;
		event.count = count;
		return event;
	}

	public String toString() {
		return ClassUtils.simpleClassName(this) + " " + SWTUtils.toString(widget);
	}

	/**
	 * Finds a menu matching the current {@link IMatcher}.
	 *
	 * @param matcher the matcher used to find menus.
	 * @return all menus that match the matcher.
	 */
	protected List findMenus(IMatcher matcher) {
		return finder.findMenus(matcher);
	}

	/**
	 * Finds the controls matching the given {@link IMatcher}.
	 *
	 * @param matcher the matcher used to find the controls.
	 * @return all controls that matches the matcher
	 */
	protected List findControls(IMatcher matcher) {
		return finder.findControls(matcher);
	}

	/**
	 * Finds the widget at the given index.
	 *
	 * @param index the index of the widgets in {@link #similarWidgets}.
	 * @return the index(th) widget that matches the matcher
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	protected Widget findControl(int index) throws WidgetNotFoundException {
		try {
			List findControls = similarWidgets;
			if (!findControls.isEmpty())
				return (Widget) findControls.get(index);
		} catch (Exception e) {
			throw new WidgetNotFoundException("Widget not found using matcher: " + getMatcher(), e);
		}
		throw new WidgetNotFoundException("Widget not found using matcher: " + getMatcher());
	}

	/**
	 * Finds the control at index 0.
	 * <p>
	 * The findControl( int index ) method should be used instead.
	 * </p>
	 *
	 * @return the first widget that matches the matcher
	 * @throws WidgetNotFoundException if the widget is not found.
	 * @deprecated use {@code #findControl(0)} instead
	 */
	protected Widget findControl() throws WidgetNotFoundException {
		return findControl(0);
	}

	/**
	 * Finds the menu on the main menu bar matching the given information.
	 *
	 * @param menuName the name of the menu.
	 * @param matcher the matcher used to find the menu.
	 * @return the first menuItem that matches the matcher
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	protected Widget findMenu(IMatcher matcher, String menuName) throws WidgetNotFoundException {
		return findMenu(getMenuMatcher(menuName), 0);
	}

	/**
	 * Gets the menu matcher for the given name.
	 *
	 * @param menuName the name of the menuitem that the matcher must match.
	 * @return {@link WidgetMatcherFactory#menuMatcher(String)}
	 */
	protected IMatcher getMenuMatcher(String menuName) {
		return WidgetMatcherFactory.menuMatcher(menuName);
	}

	/**
	 * Finds the menu on the main menu bar matching the given information.
	 *
	 * @param matcher the matcher used to find the menu.
	 * @param index the index in the list of the menu items that match the matcher.
	 * @return the index(th) menuItem that matches the matcher
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	protected Widget findMenu(IMatcher matcher, int index) throws WidgetNotFoundException {
		List findMenus = findMenus(matcher);
		if (!findMenus.isEmpty())
			return (MenuItem) findMenus.get(index);
		throw new WidgetNotFoundException("Could not find menu using matcher " + matcher);
	}

	/**
	 * Gets the text of this object's widget.
	 *
	 * @return the text on the widget.
	 */
	public String getText() {
		return SWTUtils.getText(widget);
	}

	/**
	 * Gets the tooltip of this object's widget.
	 *
	 * @return the tooltip on the widget.
	 * @since 1.0
	 */
	public String getToolTipText() {
		return syncExec(new StringResult() {
			public String run() {
				return SWTUtils.getToolTipText(widget);
			}
		});
	}

	/**
	 * Check if this widget has a style attribute.
	 *
	 * @param w the widget.
	 * @param style the style bits, one of the constants in {@link SWT}.
	 * @return <code>true</code> if style is set on the widget.
	 */
	protected boolean hasStyle(Widget w, int style) {
		return SWTUtils.hasStyle(w, style);
	}

	/**
	 * Gets the context menu matching the text.
	 *
	 * @param text the text on the context menu.
	 * @return the menu that has the given text.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotMenu contextMenu(String text) throws WidgetNotFoundException {
		if (widget instanceof Control) {
			Control control = (Control) widget;
			return new SWTBotMenu(new Finder(finder, new ContextMenuFinder(control)), text);
		}
		throw new WidgetNotFoundException("Could not find menu: " + text);
	}

	/**
	 * Gets if the object's widget is enabled.
	 *
	 * @return <code>true</code> if the widget is enabled.
	 * @see Control#isEnabled()
	 */
	public boolean isEnabled() {
		if (widget instanceof Control)
			return syncExec(new BoolResult() {
				public boolean run() {
					return isEnabledInternal();
				}
			});
		return false;
	}

	/**
	 * Gets if the widget is enabled.
	 * <p>
	 * This method is not thread safe, and must be called from the UI thread.
	 * </p>
	 *
	 * @return <code>true</code> if the widget is enabled.
	 * @since 1.0
	 */
	protected boolean isEnabledInternal() {
		try {
			return ((Boolean) SWTUtils.invokeMethod(widget, "isEnabled")).booleanValue();
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * Invokes {@link VoidResult#run()} on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 */
	protected void syncExec(VoidResult toExecute) {
		UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link ListResult#run()} on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the list returned by toExecute
	 */
	protected List syncExec(ListResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the boolean returned by toExecute
	 */
	protected boolean syncExec(BoolResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the boolean returned by toExecute
	 */

	protected String syncExec(StringResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link WidgetResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the Widget returned by toExecute
	 */
	protected Widget syncExec(WidgetResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link IntResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the integer returned by toExecute
	 */

	protected int syncExec(IntResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link ObjectResult#run()} synchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 * @return the Object returned by toExecute
	 */

	protected Object syncExec(ObjectResult toExecute) {
		return UIThreadRunnable.syncExec(display, toExecute);
	}

	/**
	 * Invokes {@link BoolResult#run()} asynchronously on the UI thread.
	 *
	 * @param toExecute the object to be invoked in the UI thread.
	 */
	protected void asyncExec(VoidResult toExecute) {
		UIThreadRunnable.asyncExec(display, toExecute);
	}

	/**
	 * Gets a copy of the list of similar widgets.
	 *
	 * @return a copy of the similarWidgets. Changing the returned copy will not affect the copy with this object
	 *         instance.
	 */
	public List getSimilarWidgets() {
		return new ArrayList(similarWidgets);
	}

	/**
	 * Gets the foreground color of the widget.
	 *
	 * @return the foreground color on the widget, or <code>null</code> if the widget is not an instance of
	 *         {@link Control}.
	 * @since 1.0
	 */
	public Color foregroundColor() {
		return (Color) syncExec(new ObjectResult() {
			public Object run() {
				if (widget instanceof Control)
					return ((Control) widget).getForeground();
				return null;
			}
		});
	}

	/**
	 * Gets the background color of the widget.
	 *
	 * @return the background color on the widget, or <code>null</code> if the widget is not an instance of
	 *         {@link Control}.
	 * @since 1.0
	 */
	public Color backgroundColor() {
		return (Color) syncExec(new ObjectResult() {
			public Object run() {
				if (widget instanceof Control)
					return ((Control) widget).getBackground();
				return null;
			}
		});
	}

	/**
	 * Check if the widget is enabled, throws {@link WidgetDisabledException} if the widget is disabled.
	 *
	 * @since 1.0
	 */
	protected void checkEnabled() {
		if (!isEnabled())
			throw new WidgetDisabledException(widget);
	}

	/**
	 * Checks if the widget is visible.
	 *
	 * @return <code>true</code> if the widget is visible, <code>false</code> otherwise.
	 * @since 1.0
	 */
	public boolean isVisible() {
		return syncExec(new BoolResult() {
			public boolean run() {
				if (widget instanceof Control)
					return ((Control) widget).isVisible();
				return true;
			}
		});
	}

	/**
	 * Sets the focus on this control.
	 *
	 * @since 1.2
	 */
	public void setFocus() {
		checkEnabled();
		boolean focusSet = syncExec(new BoolResult() {
			public boolean run() {
				if (widget instanceof Control)
					return ((Control) widget).setFocus();
				return true;
			}
		});
		Assert.assertTrue("Could not set focus", focusSet);
	}

}
