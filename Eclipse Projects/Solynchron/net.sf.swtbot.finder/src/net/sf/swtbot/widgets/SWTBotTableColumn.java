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

import net.sf.swtbot.finder.UIThreadRunnable;
import net.sf.swtbot.finder.UIThreadRunnable.WidgetResult;
import net.sf.swtbot.matcher.IMatcher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: SWTBotTableColumn.java 804 2008-06-28 14:56:44Z kpadegaonkar $
 */
public class SWTBotTableColumn extends AbstractSWTBot {

	/**
	 * @param table the table.
	 * @param text the text on the column.
	 * @throws WidgetNotFoundException if the widget is <code>null</code> or widget has been disposed.
	 */
	public SWTBotTableColumn(Table table, String text) throws WidgetNotFoundException {
		super(findColumn(table, text));
	}

	/**
	 * Attempts to find the column information.
	 * 
	 * @param table the table containing the column.
	 * @param text the text on the column.
	 * @return the {@link TableColumn} with the specified text.
	 * @throws WidgetNotFoundException if the column is not found.
	 */
	protected static Widget findColumn(final Table table, final String text) throws WidgetNotFoundException {
		TableColumn tableColumn = (TableColumn) UIThreadRunnable.syncExec(table.getDisplay(), new WidgetResult() {
			public Widget run() {
				TableColumn[] columns = table.getColumns();
				for (int i = 0; i < columns.length; i++) {
					TableColumn tableColumn = columns[i];
					if (tableColumn.getText().equals(text))
						return tableColumn;
				}
				return null;
			}
		});
		if (tableColumn != null)
			return tableColumn;
		throw new WidgetNotFoundException("Could not find table column with text" + text);
	}

	/**
	 * Clicks the item.
	 */
	public void click() {
		checkEnabled();
		notify(SWT.Selection);
	}

	protected IMatcher getMatcher() {
		return null;
	}

	public boolean isEnabled() {
		return true;
	}

}
