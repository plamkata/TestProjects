/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Marco van Meegen - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.tablesupport.tables;

/**
 * Interface to indicate if a table item is selectable; This is a workaround, because currently
 * table items cannot make disabled. {@link see
 * http://www.eclipsezone.com/forums/thread.jspa?messageID=92156400&#92156400}
 * 
 * @author lindoerfern
 */
public interface ITableSelectable
{

    /**
     * Returns true if the object is selectable inside the table where it is shown.
     * 
     * @return true if condition above true.
     */
    public Boolean getIsSelectable();

}
