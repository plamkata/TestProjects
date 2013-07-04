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

import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandItem;

/**
 * @author Mohamed Amine Limame &lt;mohamed-amine.limame [at] hotmail [dot] com&gt;
 * @version $Id$
 * @since 1.2
 */
public class SWTBotExpandItem extends AbstractSWTBot {

	private final ExpandItem	expandItem;

	/**
	 * @param expandItem the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotExpandItem(final ExpandItem expandItem) throws WidgetNotFoundException {
		super(expandItem);
		syncExec(new VoidResult() {
			public void run() {
				widget = expandItem.getParent();
			}
		});
		this.expandItem = expandItem;
	}

	protected IMatcher getMatcher() {
		return null;
	}

	/**
	 * @return the expandItem
	 */
	private ExpandItem getExpandItem() {
		return expandItem;
	}

	/**
	 * @return the expandBar item, after expanding it.
	 */
	public SWTBotExpandItem expand() {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				getExpandItem().setExpanded(true);
				expandNotify();
			}
		});
		return this;
	}

	/**
	 * @return the expandBar item, after unExpanding it.
	 */
	public SWTBotExpandItem unExpand() {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				getExpandItem().setExpanded(false);
				expandNotify();
			}
		});
		return this;
	}

	/**
	 * notifies the item of expansion.
	 */
	protected void expandNotify() {
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.Expand, createExpandEvent());
		notify(SWT.MeasureItem);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	/**
	 * @param expandBarItem
	 * @return
	 */
	private Event createExpandEvent() {
		Event event = createEvent();
		event.item = expandItem;
		return event;
	}

	public String getText() {
		return SWTUtils.getText(expandItem);
	}

	/**
	 * @return <code>true</code> if the item is expanded, <code>false</code> otherwise.
	 */
	public boolean isExpanded() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return expandItem.getExpanded();
			}
		});
	}
}
