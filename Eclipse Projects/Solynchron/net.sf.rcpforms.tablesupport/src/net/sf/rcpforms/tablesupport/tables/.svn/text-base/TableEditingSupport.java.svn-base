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

import java.util.logging.Logger;

import net.sf.rcpforms.modeladapter.configuration.ModelAdapter;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

/**
 * Class EditingSupport for ColumnViewer.
 * 
 * @author Remo Loetscher
 * @version
 */
public class TableEditingSupport extends EditingSupport
{
    private static final Logger LOG = Logger.getLogger(TableEditingSupport.class.getName());

    private CellEditor cellEditor;
    
    private ColumnConfiguration conf;

    private ColumnViewer viewer;

    private PropertyLabelProviderAndCellModifier propertyLabelProvider;

    public TableEditingSupport(ColumnViewer viewer,
                               PropertyLabelProviderAndCellModifier propertyLabelProvider,
                               ColumnConfiguration conf)
    {
        super(viewer);
        this.viewer = viewer;
        this.conf = conf;
        // initialize table editors
        this.propertyLabelProvider = propertyLabelProvider;
        if(conf.getCellEditorType() != null)
        {
            Composite parent = (Composite) viewer.getControl();
            switch(conf.getCellEditorType())
            {
                case CHECK:
                    this.cellEditor = new CheckboxCellEditor(parent);
                    break;
                case COMBO:
                    this.cellEditor = new ComboBoxCellEditor(parent, getLabelsForItems(conf.getComboItems(), propertyLabelProvider));
                    ((CCombo) cellEditor.getControl()).select(0);
                    LOG.fine("selection index:" + ((CCombo) cellEditor.getControl()).getSelectionIndex()); //$NON-NLS-1$
                    break;
                case DATE:
                    this.cellEditor = new DateCellEditor(parent);
                    break;
                case TEXT:
                    this.cellEditor = new TextCellEditor(parent);
                    break;
            }
            
        }
    }

    protected boolean canEdit(Object element)
    {
        // delegate to cell modifier
        return viewer.getCellModifier().canModify(element, conf.getProperty());
    }

    protected CellEditor getCellEditor(Object element)
    {

        if (element != null && conf.getCellEditorType() != null)
        {
            switch (conf.getCellEditorType())
            {
                case COMBO:
                    if (element instanceof ITableComboItem)
                    {
                        String[] comboItems = getLabelsForItems(((ITableComboItem) element)
                                .getComboItems(), propertyLabelProvider);
                        conf.setComboItems(((ITableComboItem) element).getComboItems());
                        ((ComboBoxCellEditor)cellEditor).setItems(comboItems);
                    }

                case CHECK:
                case TEXT:
                case DATE:
                    return cellEditor;
            }
        }
        return null;
    }

    /**
     * Return the value which is selected (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
     */
    protected Object getValue(Object element)
    {
        // delegate to cell modifier
        return viewer.getCellModifier().getValue(element, conf.getProperty());
    }

    protected void setValue(Object element, Object index)
    {
        // delegate to cell modifier
        viewer.getCellModifier().modify(element, conf.getProperty(), index);
    }

    /** translates combo items to values */
    private String[] getLabelsForItems(Object[] comboItems,
                                       PropertyLabelProviderAndCellModifier provider)
    {
        String[] result = new String[comboItems != null ? comboItems.length : 0];
        if (comboItems != null)
        {
            for (int i = 0; i < result.length; i++)
            {
                ModelAdapter adapter = ModelAdapter.getAdapterForInstance(comboItems[i]);
                result[i] = provider.convertValueToString(adapter,comboItems[i], conf);
            }
        }
        return result;
    }
}