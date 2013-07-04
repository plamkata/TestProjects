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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;
import net.sf.rcpforms.modeladapter.util.Validate;
import net.sf.rcpforms.tablesupport.Messages;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

/**
 * Class TableUtil is a powerful utility to create an editable table just by defining the column
 * properties like heading, property to display, editor type to use for the column.
 * 
 * @author Marco van Meegen
 * @author Remo Loetscher
 */
public class TableUtil
{
    /**
     * configures the viewer using the given columnConfigurations. Make sure that the input to the
     * editor is a {@link WritableList} and for the elements of the list a ModelAdapter is
     * registered.
     * <p>
     * 
     * @param tableViewer table viewer which should be configured; if column auto resizing is
     *            needed, make sure the table from the viewer is of Type {@link SizeFixedTable}.
     * @param columnConfigurations configures the columns and cell editors
     * @param rowElementClass Class of the elements which display a row
     * @param separateCheckBoxColumn if creating a viewer for a table with {@link SWT#CHECK} style,
     *            setting this to true will create an empty first column with the check boxes
     *            instead of the swt standard of inserting the checkbox into the first column
     * @param enableTableCursorSupport if cursor support is enabled table can be browsed by keyboard
     *            an cell editing (if available) will be activating by clicking \<enter\>
     * @return fully configured and functional table viewer, just set the input.
     */
    public static TableViewer configureTableViewer(final TableViewer tableViewer,
                                                   ColumnConfiguration[] columnConfigurations,
                                                   Object rowElementClass,
                                                   boolean separateCheckBoxColumn,
                                                   boolean enableTableCursorSupport)
    {
        return configureTableViewer(tableViewer, columnConfigurations, rowElementClass,
                separateCheckBoxColumn, enableTableCursorSupport, null);
    }

    /**
     * configures the viewer using the given columnConfigurations. Make sure that the input to the
     * editor is a {@link WritableList} and for the elements of the list a ModelAdapter is
     * registered.
     * <p>
     * 
     * @param tableViewer table viewer which should be configured; if column auto resizing is
     *            needed, make sure the table from the viewer is of Type {@link SizeFixedTable}.
     * @param columnConfigurations configures the columns and cell editors
     * @param rowElementMetaClass Class of the elements which display a row
     * @param separateCheckBoxColumn if creating a viewer for a table with {@link SWT#CHECK} style,
     *            setting this to true will create an empty first column with the check boxes
     *            instead of the swt standard of inserting the checkbox into the first column
     * @param enableTableCursorSupport if cursor support is enabled table can be browsed by keyboard
     *            an cell editing (if available) will be activating by clicking \<enter\>
     * @param propertyLabelProvider add a custom {@link PropertyLabelProviderAndCellModifier} to the
     *            viewer or null if default should be used. If a custom
     *            {@link PropertyLabelProviderAndCellModifier} is passed, it will be initialised
     *            with the given columnConfigurations and rowElementClass calling the
     *            init(ColumnConfiguration[], Class<?>) method on it.
     * @return fully configured and functional table viewer, just set the input.
     */
    public static TableViewer configureTableViewer(
                                                   final TableViewer tableViewer,
                                                   ColumnConfiguration[] columnConfigurations,
                                                   Object rowElementMetaClass,
                                                   boolean separateCheckBoxColumn,
                                                   boolean enableTableCursorSupport,
                                                   PropertyLabelProviderAndCellModifier propertyLabelProvider)
    {

        boolean isEditable = TableUtil.columnConfigurationCheck(columnConfigurations);
        boolean isTableCursorSupportEnabled = enableTableCursorSupport;
        final Table table = tableViewer.getTable();

        Validate.isTrue(!isEditable || (table.getStyle() & SWT.FULL_SELECTION) != 0,
                "Use SWT.FULL_SELECTION for the table, cell editors need it"); //$NON-NLS-1$

        Validate.isTrue(tableViewer instanceof TableViewer,
                "Viewer for tables has to be a subclass of TableViewer"); //$NON-NLS-1$

        if ((table.getStyle() & SWT.CHECK) != 0 && separateCheckBoxColumn)
        {
            columnConfigurations = TableUtil.createModifiedColumnConfig(columnConfigurations);
        }

        propertyLabelProvider = TableUtil.initPropertyLabelProvider(propertyLabelProvider,
                columnConfigurations, rowElementMetaClass);

        // build editing supports for columns
        int[] cols = new int[columnConfigurations.length];
        final GenericTableSorter[] genericSorters = new GenericTableSorter[columnConfigurations.length];
        for (int i = 0; i < columnConfigurations.length; i++)
        {
            TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
            column.getColumn().setWidth(columnConfigurations[i].getSize());
            column.getColumn().setText(columnConfigurations[i].getHeader());
            column.setEditingSupport(new TableEditingSupport(tableViewer, propertyLabelProvider,
                    columnConfigurations[i]));
            // add sorting functionality
            if(columnConfigurations[i].isColumnSortingEnabled())
            {
                final int columnIndex = i;
                genericSorters[i] = new GenericTableSorter(columnConfigurations[i].getProperty());
                column.getColumn().addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        GenericTableSorter sorter = genericSorters[columnIndex];
                        // switch sorting direction
                        sorter.switchSortDirection();
                        
                        // set sort indicator to the table header
                        table.setSortColumn((TableColumn) e.widget);
                        table.setSortDirection(sorter.isDescendingOrder() ? SWT.DOWN : SWT.UP);
                        
                        // set sorter or only refresh
                        if (tableViewer.getSorter() != sorter)
                            tableViewer.setSorter(sorter);
                        else
                            tableViewer.refresh();
                        
                    }
                });
            }
            cols[i] = columnConfigurations[i].getSize();
        }

        // set content and label provider after creating columns, otherwise
        // column label provider are not created
        ModelAdapter adapter = ModelAdapter.getAdapterForMetaClass(rowElementMetaClass);
        Validate
                .notNull(
                        adapter,
                        "No adapter found for the meta class "
                                + rowElementMetaClass
                                + ". Make sure you register a suitable adapter using ModelAdapter.registerAdapter().");

        IStructuredContentProvider defaultContentProvider = adapter.createDefaultContentProvider();
        Validate.notNull(defaultContentProvider,
                "ModelAdapter must return a default content provider");
        TableUtil.initViewer(tableViewer, defaultContentProvider, propertyLabelProvider);

        int[] columnWeights = new int[columnConfigurations.length];
        int sum = 0;
        for (int i = 0; i < columnConfigurations.length; i++)
        {
            columnWeights[i] = columnConfigurations[i].getSize();
            sum += columnWeights[i];
        }
        if (table instanceof SizeFixedTable)
        {
            // if table supports special calculation, use table column resize
            // adapter to get rid of
            // "last" empty column
            new TableColumnResizeAdapter(table, columnWeights);
            ((SizeFixedTable) table).setAccumulatedColumnMinWidth(sum);
        }

        // flag was introduced to have the choice of either TableCursor support
        // or editing by simple clicking
        if (isEditable && isTableCursorSupportEnabled)
        {
            createTableCursor(tableViewer, table);
        }
        return tableViewer;
    }

    /**
     * @param tableViewer
     * @param table
     */
    private static void createTableCursor(final TableViewer tableViewer, final Table table)
    {
        // create a TableCursor to navigate around the table
        final TableCursor cursor = new TableCursor(table, SWT.NONE);
        cursor.setVisible(true);

        cursor.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
                // when the user hits "ENTER" in the TableCursor,
                // let the table do cell editing
                TableItem row = cursor.getRow();
                int column = cursor.getColumn();
                tableViewer.editElement(row.getData(), column);
            }
        });

        cursor.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent e)
            {
                TableItem row = cursor.getRow();
                int column = cursor.getColumn();
                tableViewer.editElement(row.getData(), column);
            }

            public void mouseDown(MouseEvent e)
            {
                // do nothing
            }

            public void mouseUp(MouseEvent e)
            {
                // do nothing
            }

        });
        cursor.addTraverseListener(new TraverseListener()
        {
            public void keyTraversed(TraverseEvent e)
            {
                // skip table control if cursor is
                if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS)
                {
                    e.doit = false;
                    table.traverse(SWT.TRAVERSE_TAB_PREVIOUS);
                }
            }
        });
        table.addFocusListener(new FocusAdapter()
        {
            // if the table first gets the focus, position the table cursor
            @Override
            public void focusGained(FocusEvent e)
            {
                Display.getCurrent().asyncExec(new Runnable()
                {
                    public void run()
                    {
                        // find the currently selected row and position cursor
                        // there
                        if (cursor.getRow() == null)
                        {
                            TableItem[] selection = table.getSelection();
                            TableItem row = null;
                            if (selection.length == 0)
                            {
                                int topIndex = table.getTopIndex();
                                if (topIndex < table.getItemCount())
                                {
                                    row = table.getItem(topIndex);
                                }
                            }
                            else
                            {
                                row = selection[0];
                            }
                            if (row != null)
                            {
                                table.showItem(row);
                                // first time set
                                cursor.setSelection(row, 0);
                            }
                        }
                        cursor.setFocus();
                    }
                });
            }
        });
    }

    /**
     * configures the tree viewer using the given columnConfigurations to create a table tree
     * viewer. Make sure that the input to the editor is a {@link WritableList} and for the elements
     * of the list a ModelAdapter is registered.
     * 
     * @param treeViewer tree viewer which should be configured;
     * @param columnConfigurations configures the columns and cell editor
     * @param rowElementClass Class of the elements which display a row
     * @param separateCheckBoxColumn if creating a viewer for a table with {@link SWT#CHECK} style,
     *            setting this to true will create an empty first column with the check boxes
     *            instead of the swt standard of inserting the checkbox into the first column
     * @return fully configured and functional tree viewer, just set the input.
     */
    public static TreeViewer configureTreeTableViewer(final TreeViewer treeViewer,
                                                      ITreeContentProvider contentProvider,
                                                      ColumnConfiguration[] columnConfigurations,
                                                      Class<?> rowElementClass,
                                                      boolean separateCheckBoxColumn)
    {
        return configureTreeTableViewer(treeViewer, contentProvider, columnConfigurations,
                rowElementClass, separateCheckBoxColumn, null);
    }

    /**
     * configures the tree viewer using the given columnConfigurations to create a table tree
     * viewer. Make sure that the input to the editor is a {@link WritableList} and for the elements
     * of the list a ModelAdapter is registered.
     * 
     * @param treeViewer tree viewer which should be configured;
     * @param columnConfigurations configures the columns and cell editor
     * @param rowElementClass Class of the elements which display a row
     * @param separateCheckBoxColumn if creating a viewer for a table with {@link SWT#CHECK} style,
     *            setting this to true will create an empty first column with the check boxes
     *            instead of the swt standard of inserting the checkbox into the first column
     * @param propertyLabelProvider add a custom {@link PropertyLabelProviderAndCellModifier} to the
     *            viewer or null if default should be used. If a custom
     *            {@link PropertyLabelProviderAndCellModifier} is passed, it will be initialised
     *            with the given columnConfigurations and rowElementClass calling the
     *            init(ColumnConfiguration[], Class<?>) method on it.
     * @return fully configured and functional tree viewer, just set the input.
     */
    public static TreeViewer configureTreeTableViewer(
                                                      final TreeViewer treeViewer,
                                                      ITreeContentProvider contentProvider,
                                                      ColumnConfiguration[] columnConfigurations,
                                                      Class<?> rowElementClass,
                                                      boolean separateCheckBoxColumn,
                                                      PropertyLabelProviderAndCellModifier propertyLabelProvider)
    {

        boolean isEditable = TableUtil.columnConfigurationCheck(columnConfigurations);
        final Tree tree = treeViewer.getTree();
        tree.setHeaderVisible(true);

        Validate.isTrue(!isEditable || (tree.getStyle() & SWT.FULL_SELECTION) != 0,
                "Use SWT.FULL_SELECTION for the tree, cell editors need it"); //$NON-NLS-1$

        Validate.isTrue(treeViewer instanceof TreeViewer,
                "Viewer for Trees has to be a subclass of TreeViewer"); //$NON-NLS-1$

        if ((tree.getStyle() & SWT.CHECK) != 0)
        {
            if (separateCheckBoxColumn)
            {
                columnConfigurations = TableUtil.createModifiedColumnConfig(columnConfigurations);
            }
        }

        propertyLabelProvider = TableUtil.initPropertyLabelProvider(propertyLabelProvider,
                columnConfigurations, rowElementClass);

        final GenericTableSorter[] genericSorters = new GenericTableSorter[columnConfigurations.length];

        // build editing supports for columns
        int[] cols = new int[columnConfigurations.length];
        for (int i = 0; i < columnConfigurations.length; i++)
        {
            TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.NONE);
            column.getColumn().setWidth(columnConfigurations[i].getSize());
            column.getColumn().setText(columnConfigurations[i].getHeader());
            column.setEditingSupport(new TableEditingSupport(treeViewer, propertyLabelProvider,
                    columnConfigurations[i]));
            // add sorting functionality
            if(columnConfigurations[i].isColumnSortingEnabled())
            {
                final int columnIndex = i;
                genericSorters[i] = new GenericTableSorter(columnConfigurations[i].getProperty());
                column.getColumn().addSelectionListener(new SelectionAdapter()
                {
                    @Override
                    public void widgetSelected(SelectionEvent e)
                    {
                        GenericTableSorter sorter = genericSorters[columnIndex];
                        // switch sorting direction
                        sorter.switchSortDirection();
                        
                        // set sort indicator to the tree header
                        tree.setSortColumn((TreeColumn) e.widget);
                        tree.setSortDirection(sorter.isDescendingOrder() ? SWT.DOWN : SWT.UP);
                        
                        // set sorter or only refresh
                        if (treeViewer.getSorter() != sorter)
                            treeViewer.setSorter(sorter);
                        else
                            treeViewer.refresh();
                        
                    }
                });
            }
            cols[i] = columnConfigurations[i].getSize();
        }

        // set content and label provider after creating columns, otherwise
        // column label provider
        // are not created
        TableUtil.initViewer(treeViewer, contentProvider, propertyLabelProvider);

        return treeViewer;
    }

    private static void initViewer(ColumnViewer columnViewer, IContentProvider contentProvider,
                                   PropertyLabelProviderAndCellModifier propertyLabelProvider)
    {
        // only set if available; e.g. emf binding will not use this content provider as default
        if (contentProvider != null)
        {
            columnViewer.setContentProvider(contentProvider);
        }
        columnViewer.setLabelProvider(propertyLabelProvider);
        columnViewer.setCellModifier(propertyLabelProvider);
    }

    /**
     * add dummy column for checkbox viewer if checkboxes should be in its own column
     * 
     * @return adapted ColumnConfiguration array
     */
    private static ColumnConfiguration[] createModifiedColumnConfig(
                                                                    ColumnConfiguration[] originalColConf)
    {
        ColumnConfiguration[] modifiedColumnConfigurations = new ColumnConfiguration[originalColConf.length + 1];
        modifiedColumnConfigurations[0] = new ColumnConfiguration(Messages
                .getString("TableUtil.SelectionHeader"), null, 55, SWT.NONE); //$NON-NLS-1$
        System.arraycopy(originalColConf, 0, modifiedColumnConfigurations, 1,
                originalColConf.length);
        return modifiedColumnConfigurations;
    }

    /**
     * @param columnConfigurations
     * @return indicates if viewer has to support editable functionality
     */
    private static boolean columnConfigurationCheck(ColumnConfiguration[] columnConfigurations)
    {
        boolean isEditable = false;
        // check if it should be editable
        // verify column configurations
        for (ColumnConfiguration columnConfiguration : columnConfigurations)
        {
            if (columnConfiguration.getCellEditorType() != null)
            {
                isEditable = true;
            }
            if (columnConfiguration.getCellEditorType() == ECellEditorType.COMBO)
            {
                Validate.notNull(columnConfiguration.getComboItems(),
                        "Pass combo items in constructor for cell editors of type COMBO"); //$NON-NLS-1$
                Validate.noNullElements(columnConfiguration.getComboItems(),
                        "Null items are not allowed"); //$NON-NLS-1$
            }
        }

        return isEditable;
    }

    /**
     * create label provider with integrated cell modifier or take the one passed
     * 
     * @param propertyLabelProvider
     * @param modifiedColumnConfigurations
     * @param rowElementMetaClass
     */
    private static PropertyLabelProviderAndCellModifier initPropertyLabelProvider(
                                                                                  PropertyLabelProviderAndCellModifier propertyLabelProvider,
                                                                                  ColumnConfiguration[] modifiedColumnConfigurations,
                                                                                  Object rowElementMetaClass)
    {

        if (propertyLabelProvider == null)
        {
            propertyLabelProvider = new PropertyLabelProviderAndCellModifier(
                    modifiedColumnConfigurations, rowElementMetaClass);
        }
        else
        {
            propertyLabelProvider.init(modifiedColumnConfigurations, rowElementMetaClass);
        }

        return propertyLabelProvider;

    }

}



/**
 * Generic table sorter. Sorts the viewer input using {@link org.eclipse.jface.viewers.ViewerSorter}
 * . Extracts the value from datamodel by using the given property. if (and only if) both objects
 * are of type comparable than calls compareTo on the first object if sorting direction is
 * descending or the second object if direction is ascending. Otherwise it uses comparing logic from
 * {@link org.eclipse.jface.viewers.ViewerSorter#compare(Viewer, Object, Object)}
 * 
 * @author Loetscher Remo
 */
class GenericTableSorter extends ViewerSorter
{
    private static final Logger LOG = Logger.getLogger(GenericTableSorter.class.getName());

    private boolean descendingOrder = true;

    private String property;

    public GenericTableSorter(String property)
    {
        this.property = property;
    }

    public void switchSortDirection()
    {
        // switch sorting order
        descendingOrder = !descendingOrder;

    }

    public boolean isDescendingOrder()
    {
        return descendingOrder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        int returnValue = 0;
        Object value1 = this.getPropertyValue(e1, property);
        Object value2 = this.getPropertyValue(e2, property);

        // only use comparable if both values are instanceof comparable. if one
        // value is null: super.compare() will be called
        boolean useComparableInterface = value1 instanceof Comparable
                && value2 instanceof Comparable;

        if (useComparableInterface)
        {
            returnValue = ((Comparable) value1).compareTo(value2);
        }
        else
        {
            returnValue = super.compare(viewer, value1, value2);
        }
        return descendingOrder ? -returnValue : returnValue;
    }

    @Override
    public boolean isSorterProperty(Object element, String property)
    {
        return this.property.equals(property);
    }

    @Override
    public void sort(Viewer viewer, Object[] elements)
    {
        super.sort(viewer, elements);
    }

    private Object getPropertyValue(Object object, String property)
    {
        Object result = null;
        try
        {
            PropertyDescriptor pd = new PropertyDescriptor(property, object.getClass());
            Method readMethod = pd.getReadMethod();
            result = readMethod.invoke(object, new Object[]{});
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, "Error getting value for property " + property + "!", ex); //$NON-NLS-1$ //$NON-NLS-2$
        }
        return result;
    }
}
