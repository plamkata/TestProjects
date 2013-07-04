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

/**
 * An object that represents a position in terms of line and column number.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: Position.java 803 2008-06-26 04:28:55Z kpadegaonkar $
 */
public class Position {

	/** the line number */
	public final int	line;
	/** the column number */
	public final int	column;

	/**
	 * Create a position.
	 * 
	 * @param line the line number.
	 * @param column the column number.
	 */
	public Position(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + line;
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		Position other = (Position) obj;
		return (column == other.column) || (line == other.line);
	}

	public String toString() {
		return "Position: (" + line + ", " + column + ")";
	}
}
