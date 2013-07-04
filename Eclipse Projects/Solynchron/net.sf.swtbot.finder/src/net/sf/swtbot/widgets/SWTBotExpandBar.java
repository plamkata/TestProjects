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
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.wait.DefaultCondition;

import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Mohamed Amine Limame &lt;mohamed-amine.limame [at] hotmail [dot] com&gt;
 * @version $Id$
 * @since 1.2
 */
public class SWTBotExpandBar extends AbstractLabelSWTBot {

	/**
	 * Constructs an isntance of this with the given finder and the text to search for.
	 *
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotExpandBar(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this with the given widget.
	 *
	 * @param expandBar the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotExpandBar(ExpandBar expandBar) throws WidgetNotFoundException {
		super(expandBar);
	}

	protected Class nextWidgetType() {
		return ExpandBar.class;
	}

	/**
	 * @return the number of items in the ExpandBar
	 */
	public int itemCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getItemCount();
			}
		});
	}

	private ExpandBar getControl() {
		return (ExpandBar) widget;
	}

	/**
	 * @return the number of items that are currently in an expanded state.
	 * @see ExpandItem#getExpanded()
	 */
	public int expandedItemCount() {
		return syncExec(new IntResult() {
			public int run() {
				int expandeditemscount = 0;
				ExpandItem[] items = getControl().getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].getExpanded()) {
						expandeditemscount++;
					}
				}
				return expandeditemscount;
			}
		});
	}

	/**
	 * Expands the specified item in the expand bar.
	 *
	 * @param itemText the text on the expand item.
	 * @return the item that was just expanded.
	 * @throws WidgetNotFoundException if the specified item could bot be found.
	 */
	public SWTBotExpandItem expandItem(final String itemText) throws WidgetNotFoundException {
		checkEnabled();
		return getExpandItem(itemText).expand();
	}

	/**
	 * Collapses the specified item in the expand bar.
	 *
	 * @param itemText the text on the expand item.
	 * @return the item that was just expanded.
	 * @throws WidgetNotFoundException if the specified item could bot be found.
	 */
	public SWTBotExpandItem collapseItem(final String itemText) throws WidgetNotFoundException {
		checkEnabled();
		return getExpandItem(itemText).unExpand();
	}

	/**
	 * @param itemText the text on the node.
	 * @return the expandBar item with the specified text.
	 * @throws WidgetNotFoundException if the node was not found.
	 */
	public SWTBotExpandItem getExpandItem(final String itemText) throws WidgetNotFoundException {
		try {
			waitForItem(itemText);
			return new SWTBotExpandItem(getItem(itemText));
		} catch (TimeoutException e) {
			throw new WidgetNotFoundException("Timed out waiting for expandBar item " + itemText, e);
		}
	}

	/**
	 * Waits until an item with the specified text is available in the expand bar.
	 *
	 * @param itemText the item text.
	 * @throws TimeoutException if the item could not be found.
	 */
	private void waitForItem(final String itemText) throws TimeoutException {
		new SWTBot().waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Could not find node with text " + itemText;
			}

			public boolean test() throws Exception {
				return getItem(itemText) != null;
			}
		});
	}

	private ExpandItem getItem(final String nodeText) {
		return (ExpandItem) syncExec(new WidgetResult() {
			public Widget run() {
				ExpandItem[] items = getControl().getItems();
				for (int i = 0; i < items.length; i++) {
					ExpandItem item = items[i];
					if (item.getText().equals(nodeText))
						return item;
				}
				return null;
			}
		});
	}

	/**
	 * @return the list of all expandBar items in the expandBar, or an empty list if there are none.
	 */
	public SWTBotExpandItem[] getAllItems() {
		return (SWTBotExpandItem[]) syncExec(new ObjectResult() {
			public Object run() {
				ExpandItem[] items = getControl().getItems();
				SWTBotExpandItem[] result = new SWTBotExpandItem[items.length];
				for (int i = 0; i < items.length; i++)
					try {
						result[i] = new SWTBotExpandItem(items[i]);
					} catch (WidgetNotFoundException e) {
						return new SWTBotExpandItem[0];
					}
				return result;
			}
		});
	}

	/**
	 * @return <code>true</code> if the expandBar has any items, <code>false</code> otherwise.
	 */
	public boolean hasItems() {
		return itemCount() > 0;
	}
}
