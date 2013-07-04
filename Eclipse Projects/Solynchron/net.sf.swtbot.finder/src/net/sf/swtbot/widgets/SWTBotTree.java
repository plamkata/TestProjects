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

import net.sf.swtbot.SWTBot;
import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.BoolResult;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.utils.TableCollection;
import net.sf.swtbot.utils.TableRow;
import net.sf.swtbot.wait.DefaultCondition;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTree.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotTree extends AbstractLabelSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTree(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs an instance of this object with the given tree.
	 * 
	 * @param tree the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTree(Tree tree) throws WidgetNotFoundException {
		super(tree);
	}

	protected Class nextWidgetType() {
		return Tree.class;
	}

	/**
	 * Gets the number of rows in the tree.
	 * 
	 * @return the number of rows in the tree
	 */
	public int rowCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getItems().length;
			}
		});
	}

	/**
	 * Get the {@link Tree} control this is representing.
	 * 
	 * @return The {@link Tree}
	 */
	private Tree getControl() {
		return (Tree) widget;
	}

	/**
	 * Gets the column count of this tree.
	 * 
	 * @return the number of columns in the tree
	 */
	public int columnCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getColumnCount();
			}
		});
	}

	/**
	 * Gets the columns of this tree.
	 * 
	 * @return the list of columns in the tree.
	 */
	public List columns() {
		final int columnCount = columnCount();
		return syncExec(new ListResult() {
			public List run() {
				String columns[] = new String[columnCount];
				for (int i = 0; i < columnCount; i++)
					columns[i] = getControl().getColumn(i).getText();
				return new ArrayList(Arrays.asList(columns));
			}
		});
	}

	/**
	 * Gets the cell data for the given row/column index.
	 * 
	 * @param row the row index.
	 * @param column the column index.
	 * @return the cell at the location specified by the row and column
	 */
	public String cell(final int row, final int column) {
		int rowCount = rowCount();
		int columnCount = columnCount();

		Assert.isLegal(row < rowCount, "The row number (" + row + ") is more than the number of rows(" + rowCount + ") in the tree.");
		Assert.isLegal(column < columnCount, "The column number (" + column + ") is more than the number of column(" + columnCount
				+ ") in the tree.");

		return syncExec(new StringResult() {
			public String run() {
				TreeItem item = getControl().getItem(row);
				return item.getText(column);
			}
		});
	}

	/**
	 * Gets the cell data for the given row/column information.
	 * 
	 * @param row the row index.
	 * @param columnName the column name.
	 * @return the cell in the tree at the specified row and column header.
	 */
	public String cell(int row, String columnName) {
		List columns = columns();
		Assert.isLegal(columns.contains(columnName), "The column `" + columnName + "' is not found.");
		int columnIndex = columns.indexOf(columnName);
		if (columnIndex == -1)
			return "";
		return cell(row, columnIndex);
	}

	/**
	 * Gets the current selection count.
	 * 
	 * @return the number of selected items.
	 */
	public int selectionCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getSelectionCount();
			}
		});
	}

	/**
	 * Gets the table collection representing the selection.
	 * 
	 * @return the selection in the tree
	 */
	public TableCollection selection() {
		final int columnCount = columnCount();

		return (TableCollection) syncExec(new ObjectResult() {
			public Object run() {
				final TableCollection selection = new TableCollection();
				TreeItem[] items = getControl().getSelection();
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					TableRow tableRow = new TableRow();
					if (columnCount == 0)
						tableRow.add(item.getText());
					else
						for (int j = 0; j < columnCount; j++)
							tableRow.add(item.getText(j));
					selection.add(tableRow);
				}
				return selection;
			}
		});
	}

	/**
	 * Selects the given row index.
	 * 
	 * @param rowIndex the index of the row to select.
	 * @return this same instance.
	 */
	public SWTBotTree select(final int rowIndex) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Selecting row [" + rowIndex + "] " + getControl().getItem(rowIndex).getText() + " in " + widget);
				getControl().setSelection(getControl().getItem(rowIndex));
			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Selects the given item matching the name.
	 * 
	 * @param item the item to select
	 * @return this same instance.
	 */
	public SWTBotTree select(final String item) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				TreeItem[] treeItems = getControl().getItems();
				for (int i = 0; i < treeItems.length; i++) {
					TreeItem treeItem = treeItems[i];
					if (treeItem.getText().equals(item))
						getControl().setSelection(treeItem);
				}

			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Selects the items matching the array list.
	 * 
	 * @param items the items to select.
	 * @return this same instance.
	 */
	public SWTBotTree select(final String[] items) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				TreeItem[] treeItems = getControl().getItems();
				List selection = new ArrayList();
				for (int i = 0; i < treeItems.length; i++) {
					TreeItem treeItem = treeItems[i];
					for (int j = 0; j < items.length; j++) {
						String item = items[j];
						if (treeItem.getText().equals(item))
							selection.add(treeItem);
					}
				}
				getControl().setSelection((TreeItem[]) selection.toArray(new TreeItem[] {}));

			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Unselects the selection in the tree.
	 * 
	 * @return this same instance.
	 */
	public SWTBotTree unselect() {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Unselecting all in " + widget);
				getControl().deselectAll();
			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Select the indexes provided.
	 * 
	 * @param indices the indices to select.
	 * @return this same instance.
	 */
	public SWTBotTree select(final int[] indices) {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled()) {
					StringBuffer message = new StringBuffer("Selecting rows ");
					for (int i = 0; i < indices.length; i++)
						message.append(indices[i]);
					message.append(" in tree ").append(widget);
					log.debug(message);
				}
				if (hasStyle(getControl(), SWT.MULTI)) {
					TreeItem items[] = new TreeItem[indices.length];
					for (int i = 0; i < indices.length; i++)
						items[i] = getControl().getItem(indices[i]);
					getControl().setSelection(items);
				} else
					log.warn("Tree does not support SWT.MULTI, cannot make multiple selections");
			}
		});
		notifySelect();
		return this;
	}

	/**
	 * Notifies the tree widget about selection changes
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

	/**
	 * Expands the node matching the node information.
	 * 
	 * @param nodeText the text on the node.
	 * @return the Tree item that was expanded.
	 * @throws WidgetNotFoundException if the node is not found.
	 */
	public SWTBotTreeItem expandNode(final String nodeText) throws WidgetNotFoundException {
		checkEnabled();
		return getTreeItem(nodeText).expand();
	}

	/**
	 * Gets the visible row count.
	 * 
	 * @return the number of visible rows
	 */
	public int visibleRowCount() {
		return syncExec(new IntResult() {
			public int run() {
				TreeItem[] items = getControl().getItems();
				return getVisibleChildrenCount(items);
			}

			private int getVisibleChildrenCount(TreeItem item) {
				if (item.getExpanded())
					return getVisibleChildrenCount(item.getItems());
				return 0;
			}

			private int getVisibleChildrenCount(TreeItem[] items) {
				int count = 0;
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					int j = getVisibleChildrenCount(item) + 1;
					count += j;
				}
				return count;
			}
		});

	}

	/**
	 * Expands the nodes as if the plus sign was clicked.
	 * 
	 * @param nodeText the node to be expanded.
	 * @param recursive if the expansion should be recursive.
	 * @return the tree item that was expanded.
	 * @throws WidgetNotFoundException if the node is not found.
	 */
	public SWTBotTreeItem expandNode(final String nodeText, final boolean recursive) throws WidgetNotFoundException {
		checkEnabled();
		return (SWTBotTreeItem) syncExec(new ObjectResult() {
			public Object run() {
				SWTBotTreeItem item;
				try {
					item = getTreeItem(nodeText);
					expandNode(item);
				} catch (WidgetNotFoundException e) {
					throw new RuntimeException(e);
				}
				return item;
			}

			private void expandNode(SWTBotTreeItem item) throws WidgetNotFoundException {
				item.expand();
				if (recursive)
					expandTreeItem(item.getTreeItem());
			}

			private void expandTreeItem(TreeItem node) throws WidgetNotFoundException {
				TreeItem[] items = node.getItems();
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					expandNode(new SWTBotTreeItem(item));
				}
			}
		});
	}

	/**
	 * Gets the tree item matching the given name.
	 * 
	 * @param nodeText the text on the node.
	 * @return the tree item with the specified text.
	 * @throws WidgetNotFoundException if the node was not found.
	 */
	public SWTBotTreeItem getTreeItem(final String nodeText) throws WidgetNotFoundException {
		try {
			new SWTBot().waitUntil(new DefaultCondition() {
				public String getFailureMessage() {
					return "Could not find node with text " + nodeText;
				}

				public boolean test() throws Exception {
					return getItem(nodeText) != null;
				}
			});
		} catch (TimeoutException e) {
			throw new WidgetNotFoundException("Timed out waiting for tree item " + nodeText, e);
		}
		return new SWTBotTreeItem(getItem(nodeText));
	}

	/**
	 * Gets the item matching the given name.
	 * 
	 * @param nodeText the text on the node.
	 * @return the tree item with the specified text.
	 */
	private TreeItem getItem(final String nodeText) {
		return (TreeItem) syncExec(new WidgetResult() {
			public Widget run() {
				TreeItem[] items = getControl().getItems();
				for (int i = 0; i < items.length; i++) {
					TreeItem item = items[i];
					if (item.getText().equals(nodeText))
						return item;
				}
				return null;
			}
		});
	}

	/**
	 * Gets all the items in the tree.
	 * 
	 * @return the list of all tree items in the tree, or an empty list if there are none.
	 * @since 1.0
	 */
	public SWTBotTreeItem[] getAllItems() {
		return (SWTBotTreeItem[]) syncExec(new ObjectResult() {
			public Object run() {
				TreeItem[] items = getControl().getItems();
				SWTBotTreeItem[] result = new SWTBotTreeItem[items.length];

				for (int i = 0; i < items.length; i++)
					try {
						result[i] = new SWTBotTreeItem(items[i]);
					} catch (WidgetNotFoundException e) {
						return new SWTBotTreeItem[0];
					}
				return result;
			}
		});
	}

	/**
	 * Gets if this tree has items within it.
	 * 
	 * @return <code>true</code> if the tree has any items, <code>false</code> otherwise.
	 * @since 1.0
	 */
	public boolean hasItems() {
		return syncExec(new BoolResult() {
			public boolean run() {
				return getControl().getItemCount() > 0;
			}
		});
	}

}
