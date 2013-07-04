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
import java.util.Arrays;
import java.util.List;

import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.matcher.IMatcher;
import net.sf.swtbot.utils.SWTUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTreeItem.java 840 2008-07-12 13:37:47Z kpadegaonkar $
 */
public class SWTBotTreeItem extends AbstractSWTBot {

	private final TreeItem	treeItem;

	/**
	 * The instance of the parent tree.
	 *
	 * @since 1.2
	 */
	public Widget			tree;

	/**
	 * @param treeItem the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTreeItem(final TreeItem treeItem) throws WidgetNotFoundException {
		super(treeItem);
		syncExec(new VoidResult() {
			public void run() {
				widget = treeItem.getParent();
				tree = widget;
			}
		});
		this.treeItem = treeItem;
	}

	protected IMatcher getMatcher() {
		return null;
	}

	/**
	 * Gets the tree item this represents.
	 *
	 * @return the treeitem
	 */
	protected TreeItem getTreeItem() {
		return treeItem;
	}

	/**
	 * Gets the tree which is the parent of this tree item.
	 *
	 * @return the tree item.
	 * @since 1.0
	 */
	protected Tree getTree() {
		return (Tree) widget;
	}

	/**
	 * Expands the tree item to simulate click the plus sign.
	 *
	 * @return the tree item, after expanding it.
	 */
	public SWTBotTreeItem expand() {
		checkEnabled();
		preExpandNotify();
		syncExec(new VoidResult() {
			public void run() {
				getTreeItem().setExpanded(true);
			}
		});
		postExpandNotify();
		return this;
	}

	/**
	 * Send the {@link SWT#Expand} event to build the child items of the tree item that we are expanding.
	 * @since 1.2
	 */
	private void preExpandNotify() {
		notify(SWT.Expand, createExpandEvent());
	}

	/**
	 * Notifies the item of expansion.
	 * @since 1.2
	 */
	private void postExpandNotify() {
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.MeasureItem);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	/**
	 * Creates the expand event.
	 *
	 * @return The event created.
	 */
	private Event createExpandEvent() {
		Event event = selectionEvent();
		event.item = treeItem;
		return event;
	}

	/**
	 * Gets the nodes of the tree item.
	 *
	 * @return the list of nodes in the treeitem.
	 */
	public List getNodes() {
		return syncExec(new ListResult() {
			public List run() {
				TreeItem[] items = getTreeItem().getItems();
				ArrayList result = new ArrayList(items.length);
				for (int i = 0; i < items.length; i++)
					result.add(items[i].getText());
				return result;
			}
		});

	}

	/**
	 * Expands the node matching the given node text.
	 *
	 * @param nodeText the text on the node.
	 * @return the node that was expanded or <code>null</code> if not match exists.
	 */
	public SWTBotTreeItem expandNode(final String nodeText) {
		checkEnabled();
		return getNode(nodeText).expand();
	}

	/**
	 * Gets the node matching the given node text.
	 * @param nodeText the text on the node.
	 * @return the node with the specified text <code>null</code> if not match exists.
	 * @since 1.2
	 */
	public SWTBotTreeItem getNode(final String nodeText) {
		return (SWTBotTreeItem) syncExec(new ObjectResult() {
			public Object run() {
				try {
					TreeItem[] items = getTreeItem().getItems();
					for (int i = 0; i < items.length; i++) {
						TreeItem treeItem = items[i];
						if (treeItem.getText().equals(nodeText))
							return new SWTBotTreeItem(treeItem);
					}
				} catch (WidgetNotFoundException e) {
					// do nothing
				}
				return null;
			}
		});
	}

	/**
	 * Selects the current tree item.
	 *
	 * @return the current node.
	 * @since 1.0
	 */
	public SWTBotTreeItem select() {
		checkEnabled();
		syncExec(new VoidResult() {
			public void run() {
				getTree().setFocus();
				getTree().setSelection(getTreeItem());
			}
		});
		notifySelection();
		return this;
	}

	/**
	 * Triggers the notification of the selection.
	 */
	private void notifySelection() {
		notify(SWT.Selection, selectionEvent());
	}

	/**
	 * Gets the selection event.
	 *
	 * @return The event.
	 */
	private Event selectionEvent() {
		Event event = createEvent();
		event.item = treeItem;
		return event;
	}

    /**
	 * Click on the table at given coordinates
	 *
	 * @param x the x co-ordinate of the click
	 * @param y the y co-ordinate of the click
	 * @since 1.2
	 */
	protected void clickXY(int x, int y) {
		if (log.isDebugEnabled())
			log.debug("Clicking on " + SWTUtils.getText(widget));
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown, createMouseEvent(x, y, 1, SWT.BUTTON1, 1));
		notify(SWT.MouseUp);
		notifySelection();
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
		if (log.isDebugEnabled())
			log.debug("Clicked on " + SWTUtils.getText(widget));
	}

	/**
	 * Clicks on this node.
	 *
	 * @return the current node.
	 * @since 1.2
	 */
	public SWTBotTreeItem click() {
		checkEnabled();
		Rectangle cellBounds = (Rectangle) syncExec(new ObjectResult() {
			public Object run() {
				return treeItem.getBounds();
			}
		});
		clickXY(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2));
		return this;
	}

	/**
	 * Double clicks on this node.
	 *
	 * @return the current node.
	 * @since 1.2
	 */
	public SWTBotTreeItem doubleClick() {
		click();
		notify(SWT.MouseDown);
		notify(SWT.MouseDoubleClick);
		notify(SWT.MouseUp);
		return this;
	}

	/**
	 * Selects the items matching the array provided.
	 *
	 * @param items the items to select.
	 * @return the current node.
	 * @since 1.0
	 */
	public SWTBotTreeItem select(final String[] items) {
		checkEnabled();
		final List nodes = Arrays.asList(items);
		Assert.isTrue(getNodes().containsAll(nodes));

		syncExec(new VoidResult() {
			public void run() {
				TreeItem[] treeItems = getTreeItem().getItems();
				ArrayList selection = new ArrayList();

				for (int i = 0; i < treeItems.length; i++) {
					TreeItem treeItem = treeItems[i];
					if (nodes.contains(treeItem.getText()))
						selection.add(treeItem);
				}
				getTree().setFocus();
				getTree().setSelection((TreeItem[]) selection.toArray(new TreeItem[] {}));
			}
		});

		notifySelect();
		return this;
	}

	/**
	 * Selects the item matching the given name.
	 *
	 * @param item the items to select.
	 * @return the current node.
	 * @since 1.0
	 */
	public SWTBotTreeItem select(final String item) {
		return select(new String[] { item });
	}

	/**
	 * notifies the tree widget about selection changes.
	 *
	 * @since 1.0
	 */
	protected void notifySelect() {
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.Selection);
		notify(SWT.DefaultSelection);
		notify(SWT.MouseUp);
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	public String getText() {
		return SWTUtils.getText(treeItem);
	}

	public SWTBotMenu contextMenu(String text) throws WidgetNotFoundException {
		new SWTBotTree((Tree) widget).checkEnabled();
		select();
		notify(SWT.MenuDetect);
		return super.contextMenu(text);
	}
}
