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
package net.sf.swtbot.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a table row.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TableRow.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 */
public class TableRow {

	private final List	tableColumns;

	/**
	 * Constructs an empty table row.
	 */
	public TableRow() {
		this(32);
	}

	/**
	 * Constructs a row with the specified number of columns.
	 * 
	 * @param columns the number of columns in the row.
	 */
	public TableRow(int columns) {
		tableColumns = new ArrayList(columns);
	}

	/**
	 * Constructs a table row with the specified columns.
	 * 
	 * @param strings the items in the row.
	 */
	public TableRow(String[] strings) {
		this(strings.length);
		for (int i = 0; i < strings.length; i++)
			tableColumns.add(strings[i]);
	}

	/**
	 * Adds a column text label to the list.
	 * 
	 * @param text the item to be added at the end of the row.
	 */
	public void add(String text) {
		tableColumns.add(text);
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tableColumns == null) ? 0 : tableColumns.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TableRow other = (TableRow) obj;
		if (tableColumns.equals(other.tableColumns))
			return true;
		return false;
	}

	public String toString() {
		return tableColumns.toString();
	}

	/**
	 * Gets the column at the given index.
	 * 
	 * @param index the index of the column in the row.
	 * @return the text at the specified column in the row.
	 */
	public String get(int index) {
		return (String) tableColumns.get(index);
	}

	/**
	 * Gets the number of columns.
	 * 
	 * @return the number of columns
	 */
	public int columnCount() {
		return tableColumns.size();
	}

}
