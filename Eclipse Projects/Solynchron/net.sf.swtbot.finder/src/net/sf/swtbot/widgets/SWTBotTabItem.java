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

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.matcher.WidgetMatcherFactory;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.wait.DefaultCondition;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTabItem.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotTabItem extends AbstractSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTabItem(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs a new instance of this object.
	 * 
	 * @param w the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTabItem(TabItem w) throws WidgetNotFoundException {
		super(w);
	}

	protected IMatcher getMatcher() {
		return WidgetMatcherFactory.tabItemMatcher(text);
	}

	/**
	 * Activates the tabItem.
	 * 
	 * @throws TimeoutException if the tab does not activate
	 */
	public void activate() throws TimeoutException {
		if (log.isTraceEnabled())
			log.trace("Activating " + SWTUtils.toString(getTabItem()));
		checkEnabled();
		// this runs in sync because tabFolder.setSelection() does not send a notification, and so should not block.
		asyncExec(new VoidResult() {
			public void run() {
				TabItem tabItem = getTabItem();
				tabItem.getParent().setSelection(tabItem);
				if (log.isDebugEnabled())
					log.debug("Activated " + tabItem);
			}
		});

		notify(SWT.Selection, createEvent(), tabFolder());

		new SWTBot().waitUntil(new DefaultCondition() {
			public boolean test() throws Exception {
				return isActive();
			}

			public String getFailureMessage() {
				return "Timed out waiting for " + SWTUtils.toString(widget) + " to activate";
			}
		});
	}

	/**
	 * Gets the tab folder.
	 * 
	 * @return The {@link TabFolder}.
	 */
	private TabFolder tabFolder() {
		return (TabFolder) syncExec(new WidgetResult() {
			public Widget run() {
				return getTabItem().getParent();
			}
		});
	}

	protected Event createEvent() {
		Event event = super.createEvent();
		event.widget = getTabItem();
		event.item = widget;
		return event;
	}

	/**
	 * @return the {@link #widget} casted into a TabItem.
	 */
	private TabItem getTabItem() {
		return (TabItem) widget;
	}

	/**
	 * @return <code>true</code> if the tab item is active, <code>false</code> otherwise.
	 */
	public boolean isActive() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return tabFolder().getSelection()[0] == widget;
			}
		});
	}

	public boolean isEnabled() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return getTabItem().getParent().isEnabled();
			}
		});
	}
}
