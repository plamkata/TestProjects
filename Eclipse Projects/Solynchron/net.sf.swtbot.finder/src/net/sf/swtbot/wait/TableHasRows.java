package net.sf.swtbot.wait;

import net.sf.swtbot.widgets.SWTBotTable;

import org.eclipse.core.runtime.Assert;

/**
 * A condition that returns <code>false</code> until the table has the specified number of rows.
 * 
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id: TableHasRows.java 830 2008-07-05 20:26:55Z kpadegaonkar $
 */
class TableHasRows extends DefaultCondition {

	/**
	 * The row count.
	 */
	private final int			rowCount;
	/**
	 * The table (SWTBotTable) instance to check.
	 */
	private final SWTBotTable	table;

	/**
	 * Constructs an instance of the condition for the given table. The row count is used to know how many rows it needs
	 * to satisfy the condition.
	 * 
	 * @param table the table
	 * @param rowCount the number of rows needed.
	 * @throws NullPointerException Thrown if the table is <code>null</code>.
	 * @throws IllegalArgumentException Thrown if the row count is less then 1.
	 */
	public TableHasRows(SWTBotTable table, int rowCount) {
		Assert.isNotNull(table, "The table can not be null");
		Assert.isLegal(rowCount >= 0, "The row count must be greater then zero (0)");
		this.table = table;
		this.rowCount = rowCount;
	}

	/**
	 * Performs the check to see if the condition is satisfied.
	 * 
	 * @see net.sf.swtbot.wait.ICondition#test()
	 * @return <code>true</code> if the condition row count equals the number of rows in the table. Otherwise
	 *         <code>false</code> is returned.
	 */
	public boolean test() {
		return table.rowCount() == rowCount;
	}

	/**
	 * Gets the failure message if the test is not satisfied.
	 * 
	 * @see net.sf.swtbot.wait.ICondition#getFailureMessage()
	 * @return The failure message.
	 */
	public String getFailureMessage() {
		return "Timed out waiting for " + table + " to contain " + rowCount + " rows.";
	}
}
