/*******************************************************************************
 *  Copyright 2007 SWTBot, http://swtbot.org/
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/

package net.sf.swtbot.wait;

import net.sf.swtbot.widgets.SWTBotShell;
import net.sf.swtbot.widgets.SWTBotTable;

/**
 * This is a helper object to give access to the standard conditions provided.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: Conditions.java 830 2008-07-05 20:26:55Z kpadegaonkar $
 */
public class Conditions {

	/**
	 * Gets the condition for checking tables have the proper number of rows. Useful in cases where the table is
	 * populated continuously from a non UI thread.
	 * 
	 * @param table the table
	 * @param rowCount the number of rows that the table must have, in order for {@link ICondition#test()} to evaluate
	 *            to <code>true</code>.
	 * @return <code>true</code> if the table has the number of rows specified. Otherwise <code>false</code>.
	 * @throws IllegalArgumentException Thrown if the row count is less then 1.
	 * @since 1.2
	 */
	public static ICondition tableHasRows(SWTBotTable table, int rowCount) {
		return new TableHasRows(table, rowCount);
	}

	/**
	 * Gets the condition for checking if shells have closed. Useful in cases where a shell takes long to close.
	 * 
	 * @param shell the shell to monitor
	 * @return a condition that evaluates to false until the shell is closed or invisible.
	 * @since 1.2
	 */
	public static ICondition shellCloses(SWTBotShell shell) {
		return new ShellCloses(shell);
	}

}
