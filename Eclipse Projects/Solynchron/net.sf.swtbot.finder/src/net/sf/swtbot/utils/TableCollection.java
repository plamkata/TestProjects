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
import java.util.Iterator;
import java.util.List;

/**
 * Represents a table.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TableCollection.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 */
public class TableCollection {

	private final List	rows;

	/**
	 * @param tableRow the rows in the table.
	 */
	public TableCollection(TableRow tableRow) {
		this(new TableRow[] { tableRow });
	}

	/**
	 * Cosntructs the table collection with the given table rows as an array.
	 * 
	 * @param tableRows the rows in the table.
	 */
	public TableCollection(TableRow[] tableRows) {
		this(tableRows.length);
		for (int i = 0; i < tableRows.length; i++)
			add(tableRows[i]);
	}

	/**
	 * Creates an empty table.
	 */
	public TableCollection() {
		this(32);
	}

	/**
	 * Creates a table with the specified number of rows.
	 * 
	 * @param rowCount the number of rows.
	 */
	public TableCollection(int rowCount) {
		rows = new ArrayList(rowCount);
	}

	/**
	 * Adds a new row to the table collection.
	 * 
	 * @param tableRow the row to be added at the end of the table.
	 * @return a reference to this object.
	 */
	public TableCollection add(TableRow tableRow) {
		rows.add(tableRow);
		return this;
	}

	/**
	 * Gets the row count.
	 * 
	 * @return the number of rows in the selection.
	 */
	public int rowCount() {
		return rows.size();
	}

	/**
	 * Gets the string data for the given row/column index.
	 * 
	 * @param row the index of the row.
	 * @param column the column in the row.
	 * @return the string at the specified cell in the collection.
	 */
	public String get(int row, int column) {
		return get(row).get(column);
	}

	/**
	 * Gets the row at the given index.
	 * 
	 * @param row the row index.
	 * @return the row at the index.
	 */
	public TableRow get(int row) {
		return (TableRow) rows.get(row);
	}

	/**
	 * Gets the column count.
	 * 
	 * @return the number of columns
	 */
	public int columnCount() {
		if (rowCount() > 0)
			return ((TableRow) rows.get(0)).columnCount();
		return 0;
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();
		for (final Iterator iterator = rows.iterator(); iterator.hasNext();) {
			final TableRow row = (TableRow) iterator.next();
			buf.append(row);
			buf.append("\n");
		}
		return buf.toString();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rows == null) ? 0 : rows.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TableCollection other = (TableCollection) obj;
		if (rows.equals(other.rows))
			return true;
		return false;
	}
}
