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

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.SWTUtils;
import net.sf.swtbot.utils.StringUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.List;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotList.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotList extends AbstractLabelSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotList(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an isntance of this with the given list widget.
	 * 
	 * @param list the list.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotList(List list) throws WidgetNotFoundException {
		super(list);
	}

	protected Class nextWidgetType() {
		return List.class;
	}

	/**
	 * Selects the item matching the given text.
	 * 
	 * @param item the item to select in the list.
	 */
	public void select(final String item) {
		if (log.isDebugEnabled())
			log.debug("Set selection " + SWTUtils.toString(widget) + " to text " + item);
		checkEnabled();
		final int indexOf = indexOf(item);
		Assert.isTrue(indexOf != -1, "Item `" + item + "' not found in list.");
		asyncExec(new VoidResult() {
			public void run() {
				getList().setSelection(indexOf);
			}
		});
		notifySelect();
	}

	/**
	 * Selects the given index.
	 * 
	 * @param index the selection index.
	 */
	public void select(final int index) {
		if (log.isDebugEnabled())
			log.debug("Set selection " + SWTUtils.toString(widget) + " to index " + index);
		checkEnabled();
		int itemCount = itemCount();
		Assert.isTrue(index <= itemCount, "The index (" + index + ") is more than the number of items (" + itemCount + ") in the list.");
		asyncExec(new VoidResult() {
			public void run() {
				getList().setSelection(index);
			}
		});
		notifySelect();
	}

	/**
	 * Gets the item count in the list
	 * 
	 * @return the number of items in the list.
	 */
	public int itemCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getList().getItemCount();
			}
		});
	}

	/**
	 * Gets the selection count.
	 * 
	 * @return the number of selected items in the list.
	 */
	public int selectionCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getList().getSelectionCount();
			}
		});
	}

	/**
	 * Gets the list widget.
	 * 
	 * @return The {@link List}.
	 */
	private List getList() {
		return ((List) widget);
	}

	/**
	 * Gets the arrray of selected items.
	 * 
	 * @return the selected items in the list.
	 */
	public String[] selection() {
		return (String[]) syncExec(new ObjectResult() {
			public Object run() {
				return getList().getSelection();
			}
		});
	}

	/**
	 * Selects the indexes provided.
	 * 
	 * @param indices the indices to select in the list.
	 */
	public void select(final int[] indices) {
		if (log.isDebugEnabled())
			log.debug("Set selection " + SWTUtils.toString(widget) + " to indices " + StringUtils.join(indices, ", ") + "]");
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				getList().setSelection(indices);
			}

		});
		notifySelect();
	}

	/**
	 * Sets the selection to the given list of items.
	 * 
	 * @param items the items to select in the list.
	 */
	public void select(final String[] items) {
		if (log.isDebugEnabled())
			log.debug("Set selection " + SWTUtils.toString(widget) + " to items [" + StringUtils.join(items, ", ") + "]");
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				getList().deselectAll();
				for (int i = 0; i < items.length; i++) {
					int index = getList().indexOf(items[i]);
					if (index != -1)
						getList().select(index);
				}
			}
		});
		notifySelect();
	}

	/**
	 * Notifies of a selection.
	 */
	protected void notifySelect() {
		notify(SWT.MouseDown);
		notify(SWT.Selection);
		notify(SWT.MouseUp);
	}

	/**
	 * Unselects everything.
	 */
	public void unselect() {
		asyncExec(new VoidResult() {
			public void run() {
				getList().deselectAll();
			}
		});
		notifySelect();
	}

	/**
	 * Gets the index of the given item.
	 * 
	 * @param item the search item.
	 * @return the index of the item, or -1 if the item does not exist.
	 */
	public int indexOf(final String item) {
		return syncExec(new IntResult() {
			public int run() {
				return getList().indexOf(item);
			}
		});
	}

	/**
	 * Gets the item at the given index.
	 * 
	 * @param index the zero based index.
	 * @return the item at the specified index.
	 */
	public String itemAt(final int index) {
		return syncExec(new StringResult() {
			public String run() {
				return getList().getItem(index);
			}
		});
	}
}
