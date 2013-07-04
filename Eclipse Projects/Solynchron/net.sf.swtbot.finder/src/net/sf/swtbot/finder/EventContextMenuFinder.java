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

import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

/**
 * Context menu finder that uses events to get the current context menu. It must be used instead of ContextMenuFinder
 * when the context menu is not associated with a Widget. It must be registered before the context menu appears.
 * <p>
 * Here is a sample usage:
 * </p>
 * 
 * <pre>
 * EventContextMenuFinder eventContextMenuFinder = new EventContextMenuFinder();
 * try {
 * 	eventContextMenuFinder.register();
 * 	button.click(); // a popup menu appears below the button
 * 	SWTBotMenu menu = new SWTBotMenu(new Finder(new ControlFinder(), eventContextMenuFinder), &quot;Menu Text&quot;);
 * 	menu.click();
 * } finally {
 * 	eventContextMenuFinder.unregister();
 * }
 * </pre>
 * 
 * This is not convenient to use but the need for this is not so frequent. In case you have better idea on the
 * implementation or usage please add a comment to <a href="http://swtbot.org/bugzilla/show_bug.cgi?id=19">this bug</a>.
 * 
 * @author Cedric Chabanois &lt;cchabanois [at] no-log [dot] org&gt;
 * @version $Id: EventContextMenuFinder.java 772 2008-06-22 04:19:08Z kpadegaonkar $
 * @since 1.0
 */
public class EventContextMenuFinder extends MenuFinder {
	/**
	 * The current context menu.
	 */
	private Menu					currentContextMenu	= null;
	/**
	 * The listener to be used.
	 */
	private final ShowHideListener	showHideListener;
	/**
	 * The display to use.
	 */
	private final Display			display;

	/**
	 * Creates an event based context menu finder.
	 * 
	 * @param display the display
	 * @throws NullPointerException Thrown if the display is <code>null</code>.
	 */
	public EventContextMenuFinder(Display display) {
		Assert.isNotNull(display, "The display can not be null");
		this.display = display;
		showHideListener = new ShowHideListener();
	}

	/**
	 * Creates an event based context menu finder.
	 */
	public EventContextMenuFinder() {
		this(SWTUtils.display());
	}

	/**
	 * Registers this finder so that it may start 'looking for' controls. It does so by listening for {@link SWT#Show}
	 * and {@link SWT#Hide} events on menus.
	 */
	public void register() {
		UIThreadRunnable.syncExec(display, new VoidResult() {
			public void run() {
				display.addFilter(SWT.Show, showHideListener);
				display.addFilter(SWT.Hide, showHideListener);
			}
		});
	}

	/**
	 * Unregisters this finder so that it may stop 'looking for' controls. It does so by listening for {@link SWT#Show}
	 * and {@link SWT#Hide} events on menus.
	 */
	public void unregister() {
		UIThreadRunnable.syncExec(display, new VoidResult() {
			public void run() {
				display.removeFilter(SWT.Show, showHideListener);
				display.removeFilter(SWT.Hide, showHideListener);
			}
		});
	}

	/**
	 * Gets the menu bar that has been found. This may be <code>null</code> if one had not been found yet.
	 * 
	 * @see net.sf.swtbot.finder.MenuFinder#menuBar(org.eclipse.swt.widgets.Shell)
	 * @param shell This is not used.
	 * @return The menu or <code>null</code> if not yet found.
	 */
	protected Menu menuBar(final Shell shell) {
		return currentContextMenu;
	}

	/**
	 * A private class to listen for the show/hide events.
	 */
	private class ShowHideListener implements Listener {
		/**
		 * Handles the event by checking if it is the proper event. If it is a show, then the current context menu is
		 * set. Otherwise it will be set to <code>null</code> if it is a hide event.
		 * 
		 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
		 * @param event the event to check.
		 */
		public void handleEvent(Event event) {
			if (!(event.widget instanceof Menu))
				return;
			Menu menu = (Menu) event.widget;
			if (SWTUtils.hasStyle(menu, SWT.POP_UP)) {
				if (event.type == SWT.Show)
					currentContextMenu = menu;
				if (event.type == SWT.Hide)
					currentContextMenu = null;
			}
		}
	}

}
