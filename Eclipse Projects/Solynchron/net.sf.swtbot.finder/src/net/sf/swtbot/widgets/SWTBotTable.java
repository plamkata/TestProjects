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

import net.sf.swtbot.finder.Finder;
import net.sf.swtbot.finder.UIThreadRunnable.IntResult;
import net.sf.swtbot.finder.UIThreadRunnable.ListResult;
import net.sf.swtbot.finder.UIThreadRunnable.ObjectResult;
import net.sf.swtbot.finder.UIThreadRunnable.StringResult;
import net.sf.swtbot.finder.UIThreadRunnable.VoidResult;
import net.sf.swtbot.utils.TableCollection;
import net.sf.swtbot.utils.TableRow;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTable.java 913 2008-08-26 14:26:38Z kpadegaonkar $
 */
public class SWTBotTable extends AbstractLabelSWTBot {

	/**
	 * @param finder the finder used to find controls.
	 * @param text the text on the control.
	 * @throws WidgetNotFoundException if the widget is not found.
	 */
	public SWTBotTable(Finder finder, String text) throws WidgetNotFoundException {
		super(finder, text);
	}

	/**
	 * Constructs a new instance of this object.
	 *
	 * @param table the widget.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTable(Table table) throws WidgetNotFoundException {
		super(table);
	}

	protected Class nextWidgetType() {
		return Table.class;
	}

	/**
	 * Gets the row count.
	 *
	 * @return the number of rows in the table
	 */
	public int rowCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getItemCount();
			}
		});
	}

	/**
	 * Gets the control this represents.
	 *
	 * @return The {@link Table}.
	 */
	private Table getControl() {
		return (Table) widget;
	}

	/**
	 * Gets the column count.
	 *
	 * @return the number of columns in the table
	 */
	public int columnCount() {
		return syncExec(new IntResult() {
			public int run() {
				return getControl().getColumnCount();
			}
		});
	}

	/**
	 * Gets the columns in this table.
	 *
	 * @return the list of columns in the table.
	 */
	public List columns() {
		final int columnCount = columnCount();
		return syncExec(new ListResult() {
			public List run() {
				final String columns[] = new String[columnCount];
				for (int i = 0; i < columnCount; i++)
					columns[i] = getControl().getColumn(i).getText();
				return new ArrayList(Arrays.asList(columns));
			}
		});
	}

	/**
	 * Gets the column matching the given label.
	 *
	 * @param label the header text.
	 * @return the header of the table.
	 * @throws WidgetNotFoundException if the header is not found.
	 */
	public SWTBotTableColumn header(String label) throws WidgetNotFoundException {
		return new SWTBotTableColumn(getControl(), label);
	}

	/**
	 * Gets the cell data for the given row/column index.
	 *
	 * @param row the row in the table.
	 * @param column the column in the table.
	 * @return the cell at the location specified by the row and column
	 */
	public String cell(final int row, final int column) {
		assertIsLegalCell(row, column);

		return syncExec(new StringResult() {
			public String run() {
				TableItem item = getControl().getItem(row);
				return item.getText(column);
			}
		});
	}

	/**
	 * Gets the cell data for the given row and column label.
	 *
	 * @param row the row in the table
	 * @param columnName the column title.
	 * @return the cell in the table at the specified row and columnheader
	 */
	public String cell(int row, String columnName) {
		Assert.isLegal(columns().contains(columnName), "The column `" + columnName + "' is not found.");
		List columns = columns();
		int columnIndex = columns.indexOf(columnName);
		if (columnIndex == -1)
			return "";
		return cell(row, columnIndex);
	}

	/**
	 * Gets the selected item count.
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
	 * Gets the selected items.
	 *
	 * @return the selection in the table
	 */
	public TableCollection selection() {
		final int columnCount = columnCount();
		return (TableCollection) syncExec(new ObjectResult() {
			public Object run() {
				final TableCollection selection = new TableCollection();
				TableItem[] items = getControl().getSelection();
				for (int i = 0; i < items.length; i++) {
					TableItem item = items[i];
					TableRow tableRow = new TableRow();
					for (int j = 0; j < columnCount; j++)
						tableRow.add(item.getText(j));
					selection.add(tableRow);
				}
				return selection;
			}
		});
	}

	/**
	 * Sets the selected item to the given index.
	 *
	 * @param rowIndex the zero-based index of the row to be selected.
	 */
	public void select(final int rowIndex) {
		checkEnabled();
		assertIsLegalRowIndex(rowIndex);
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Selecting row [" + rowIndex + "] " + getControl().getItem(rowIndex).getText() + " in " + widget);
				getControl().setSelection(rowIndex);
			}
		});
		notifySelect();
	}

	private void assertIsLegalRowIndex(final int rowIndex) {
		Assert.isLegal(rowIndex < rowCount(), "The row number: " + rowIndex + " does not exist in the table");
	}

	/**
	 * Sets the selection to the given item.
	 *
	 * @param item the item to select in the table.
	 * @since 1.0
	 */
	public void select(final String item) {
		checkEnabled();
		int itemIndex = indexOf(item);
		Assert.isLegal(itemIndex >= 0, "Could not find item:" + item + " in table");
		select(itemIndex);
		notifySelect();
	}

	/**
	 * Gets the index of the item matching the given item.
	 *
	 * @param item the index of the item in the table, or -1 if the item does not exist in the table.
	 * @return the index of the specified item in the table.
	 * @since 1.0
	 */
	public int indexOf(final String item) {
		return syncExec(new IntResult() {
			public int run() {
				TableItem[] items = getControl().getItems();
				for (int i = 0; i < items.length; i++) {
					TableItem tableItem = items[i];
					if (tableItem.getText().equals(item))
						return i;
				}
				return -1;
			}
		});
	}

	/**
	 * Unselect all selections.
	 */
	public void unselect() {
		checkEnabled();
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled())
					log.debug("Unselecting all in " + widget);
				getControl().deselectAll();
			}
		});
	}

	/**
	 * Selects the given index items.
	 *
	 * @param indices the row indices to select in the table.
	 */
	public void select(final int[] indices) {
		checkEnabled();
		Arrays.sort(indices);
		assertIsLegalRowIndex(indices[indices.length - 1]);
		asyncExec(new VoidResult() {
			public void run() {
				if (log.isDebugEnabled()) {
					StringBuffer message = new StringBuffer("Selecting rows ");
					for (int i = 0; i < indices.length; i++)
						message.append(indices[i]);
					message.append(" in table ").append(widget);
					log.debug(message);
				}
				if (hasStyle(getControl(), SWT.MULTI))
					getControl().setSelection(indices);
				else
					log.warn("Table does not support SWT.MULTI, cannot make multiple selections");
			}
		});
		notifySelect();
	}

	/**
	 * Notifies the selection.
	 */
	protected void notifySelect() {
		notify(SWT.MouseEnter);
		notify(SWT.MouseMove);
		notify(SWT.Activate);
		notify(SWT.FocusIn);
		notify(SWT.MouseDown);
		notify(SWT.Selection);
		notify(SWT.MouseUp);
		notify(SWT.MouseHover);
		notify(SWT.MouseMove);
		notify(SWT.MouseExit);
		notify(SWT.Deactivate);
		notify(SWT.FocusOut);
	}

	/**
	 * Click on the table on given cell. This can be used to activate a cellEditor on a cell.
	 *
	 * @param row the row in the table.
	 * @param column the column in the table.
	 * @since 1.2
	 */
	public void click(final int row, final int column) {
		assertIsLegalCell(row, column);
		// for some reason, it does not work without setting selection first
		select(row);
		asyncExec(new VoidResult() {
			public void run() {
				TableItem item = getControl().getItem(row);
				Rectangle cellBounds = item.getBounds(column);
				clickXY(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2));
			}
		});
	}

	/**
	 * Click on the table on given cell. This can be used to activate a cellEditor on a cell.
	 *
	 * @param row the row in the table.
	 * @param column the column in the table.
	 * @since 1.2
	 */
	public void doubleClick(final int row, final int column) {
		assertIsLegalCell(row, column);

		asyncExec(new VoidResult() {
			public void run() {
				TableItem item = getControl().getItem(row);
				Rectangle cellBounds = item.getBounds(column);
				// for some reason, it does not work without setting selection first
				getControl().setSelection(row);
				doubleClickXY(cellBounds.x + (cellBounds.width / 2), cellBounds.y + (cellBounds.height / 2));
			}
		});
	}

	/**
	 * Asserts that the row and column are legal for this instance of the table.
	 *
	 * @param row the row number
	 * @param column the column number
	 * @since 1.2
	 */
	protected void assertIsLegalCell(final int row, final int column) {
		int rowCount = rowCount();
		int columnCount = columnCount(); // 0 if no TableColumn has been created by user

		Assert.isLegal(row < rowCount,
				"The row number (" + row + ") is more than the number of rows(" + rowCount + ") in the table.");
		Assert.isLegal(column < columnCount || (columnCount == 0 && column == 0),
				"The column number (" + column + ") is more than the number of column(" + columnCount
				+ ") in the table.");
	}

}
