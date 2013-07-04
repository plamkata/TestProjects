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
 * Defines one table column. An array of ColumnConfigurations is passed to the {@link WidgetFactory}
 * to create a new table viewer.
 * <p>
 * A table column specifies the column header, the bean property to display, a cell editor type (if
 * null, the colum is not editable) and style and layout properties.
 * <p>
 * Columns which grab horizontal will use extra space available for the table to stretch like
 * grabHorizontal behaviour in a grid layout, default is false.
 * 
 * @author Marco van Meegen
 */
public class ColumnConfiguration
{

    private String header;

    private String property;

    private int size;

    private boolean grabHorizontal = false;
    
    private boolean isColumnSortingEnabled = true;

    private int style;

    private ECellEditorType cellEditorType;

    private Object[] comboItems;

    /**
     * create a non-modifiable column bound to the given bean property
     * 
     * @param header
     * @param property
     * @param size preferred size of column in pixels; if grab is specified, column grabs extra
     *            space to grow
     * @param style one of SWT.LEFT, SWT.RIGHT, SWT.CENTER
     */
    public ColumnConfiguration(String header, String property, int size, int style)
    {
        this(header, property, size, style, false, null);
    }

    /**
     * create a column with a combo cell editor with the given combo values
     * 
     * @param header
     * @param property
     * @param size pixel ? TODO: nr of characters, weight, or pixel ?
     * @param style one of SWT.LEFT, SWT.RIGHT, SWT.CENTER
     * @param comboItems items to display in combo box; MUST BE OF THE TYPE OF THE ATTRIBUTE THE
     *            COLUMN IS BOUND TO !!! (e.g. Code)
     */
    public ColumnConfiguration(String header, String property, int size, int style,
                               Object[] comboItems)
    {
        this(header, property, size, style, false, ECellEditorType.COMBO);
        this.comboItems = comboItems;
    }

    /**
     * create a modifiable column using a cell editor with the given type.
     * 
     * @param header
     * @param property
     * @param size TODO: nr of characters, weight, or pixel ?
     * @param style
     * @param grabHorizontal grab horizontal
     * @param cellEditorType cell editor type
     */
    public ColumnConfiguration(String header, String property, int size, int style,
                               boolean grabHorizontal, ECellEditorType cellEditorType)
    {
        super();
        this.header = header;
        this.property = property;
        this.size = size;
        this.style = style;
        this.cellEditorType = cellEditorType;
    }

    public String getHeader()
    {
        return header;
    }

    public String getProperty()
    {
        return property;
    }

    public int getSize()
    {
        return size;
    }

    public int getStyle()
    {
        return style;
    }

    public ECellEditorType getCellEditorType()
    {
        return cellEditorType;
    }

    public Object[] getComboItems()
    {
        return comboItems;
    }

    /**
     * @return true if the column should
     */
    public boolean isGrabHorizontal()
    {
        return grabHorizontal;
    }

    /**
     * @param b true if column should grow to available space
     */
    public ColumnConfiguration setGrabHorizontal(boolean b)
    {
        grabHorizontal = b;
        return this;
    }

    public void setComboItems(Object[] comboItems)
    {
        this.comboItems = comboItems;
    }
    
    /**
     * Enable or disable sorting functionality for each column.
     * Default value is true, which means sorting is applied
     * @param enableSorting boolean to indicate if sorting should be enabled for this column
     */
    public void enableColumnSorting(boolean enableSorting)
    {
        this.isColumnSortingEnabled = enableSorting;
    }
    
    /**
     * @return flag if sorting is activated (true) or deactivated (false)
     */
    public boolean isColumnSortingEnabled()
    {
        return isColumnSortingEnabled;
    }

}
