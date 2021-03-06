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

package net.sf.rcpforms.examples.complete;

import java.util.Date;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.common.util.Validate;
import net.sf.rcpforms.examples.complete.TestModel.Gender;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.TableUtil;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.builder.GridLayoutFactory;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCheckboxTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPPushButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPTree;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Example part demonstrating the advanced table features of RCPForms.
 * <p>
 * <ul>
 * <li>creating an editable table just by configuring the column properties, no coding needed
 * <li>how converters from data binding are automatically used to render table data
 * <li>binding of multi selection state of a checkbox table to a list
 * <li>binding of multi selection state of a normal table to a list
 * <li>automatic creation of a table cursor if the table contains editable columns
 * <li>disabled rows if {@link RCPTableFormToolkit} is used
 * </ul>
 * 
 * @author Marco van Meegen
 */
public class SandboxTablePart extends RCPFormPart
{
    private RCPSection mainSection;

    private RCPCheckboxTable m_CheckboxTable;

    private RCPTable m_Table;

    private RCPTable m_TableCursorSupport;

    private RCPPushButton m_addColumn;

    private RCPPushButton m_removeColumn;

    private RCPPushButton m_toggleIsSelectable;

    private RCPPushButton m_removeInput;

    private TableViewer m_CheckboxTableViewer;

    private TableViewer m_TableViewer;

    private TableViewer m_TableViewerCursorSupport;

    private RCPTree m_TreeTable;

    private RCPTree m_CheckboxTreeTable;

    private TreeViewer m_TreeTableViewer;

    private TreeViewer m_checkedTreeTableViewer;

    private TableModel dataModel;

    private Binding tableCheckBinding;

    public SandboxTablePart()
    {
        m_addColumn = new RCPPushButton("add item");
        m_removeColumn = new RCPPushButton("remove item");
        m_toggleIsSelectable = new RCPPushButton("toggle selectable");
        m_removeInput = new RCPPushButton("replace data");

        m_CheckboxTable = new RCPCheckboxTable("CheckboxTable:");
        m_Table = new RCPTable("Table:");
        m_TableCursorSupport = new RCPTable("Table with cursor support:");

        // m_TreeTable = new RCPTree("TreeTable:", SWT.FULL_SELECTION);
        // m_CheckboxTreeTable = new RCPTree("CheckboxTreeTable:", SWT.FULL_SELECTION | SWT.CHECK);

    }

    private static ColumnConfiguration[] createColumnConfigurations(boolean editable)
    {
        // this configures the table with the attributes to display;
        // automatic conversion will be applied using the standard data binding converters
        // which are used in text fields too
        Object[] values = new Object[Gender.values().length + 1];
        // add null value
        values[0] = NullValue.getInstance();
        System.arraycopy(Gender.values(), 0, values, 1, values.length - 1);
        ColumnConfiguration[] columnConfigurations = {
                new ColumnConfiguration("Name", TestModel.P_Name, 100, SWT.LEFT, false,
                        editable ? ECellEditorType.TEXT : null).setGrabHorizontal(true),
                new ColumnConfiguration("Geburtsdatum", TestModel.P_BirthDate, 80, SWT.LEFT, false,
                        editable ? ECellEditorType.DATE : null),
                editable ? new ColumnConfiguration("Geschlecht", TestModel.P_Gender, 60, SWT.LEFT,
                        values) : new ColumnConfiguration("Geschlecht", TestModel.P_Gender, 60,
                        SWT.LEFT),
                new ColumnConfiguration("Konto�berzug", TestModel.P_OverdrawAccount, 50,
                        SWT.CENTER, false, editable ? ECellEditorType.CHECK : null),
                new ColumnConfiguration("Kinder", TestModel.P_ChildCount, 50, SWT.RIGHT, false,
                        editable ? ECellEditorType.TEXT : null),
                new ColumnConfiguration("Kontostand", TestModel.P_AccountBalance, 70, SWT.RIGHT,
                        false, editable ? ECellEditorType.TEXT : null)};
        Validate.noNullElements(columnConfigurations,
                "ColumnConfigurations must not have null elements");
        return columnConfigurations;
    }

    @Override
    public void createUI(FormToolkit formToolkit, Composite parent)
    {
        GridBuilder builder = new GridBuilder(formToolkit, parent, 1);
        mainSection = new RCPSection("Sandbox Table Section");
        GridBuilder sectionBuilder = builder.addContainer(mainSection, 2);
        RCPComposite buttonPanel = new RCPComposite();
        GridBuilder buttonWidgetFactory = sectionBuilder.fill(1).addContainer(buttonPanel, 8);
        buttonWidgetFactory.add(m_addColumn);
        m_addColumn.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                addRandomItem();
            }
        });

        buttonWidgetFactory.add(m_removeColumn);
        m_removeColumn.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeFirstItem();
            }
        });

        buttonWidgetFactory.add(m_toggleIsSelectable);
        m_toggleIsSelectable.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                toggleSelectableState();
            }
        });
        m_toggleIsSelectable.getSWTButton().setToolTipText(
                "removes all selections and toggle selectable state");

        buttonWidgetFactory.add(m_removeInput);
        m_removeInput.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeList();
            }
        });
        m_removeInput.getSWTButton().setToolTipText("replaces the list of the second table");

        // create normal Table with table cursor support
        sectionBuilder.add(m_TableCursorSupport);
        m_TableViewerCursorSupport = TableUtil.configureTableViewer(
                (TableViewer) m_TableCursorSupport.getViewer(), createColumnConfigurations(true),
                TestModel.class, false, true);

        // create normal Table
        sectionBuilder.add(m_Table);
        m_TableViewer = TableUtil.configureTableViewer((TableViewer) m_Table.getViewer(),
                createColumnConfigurations(true), TestModel.class, false, false);

        // create Checkbox Table
        sectionBuilder.add(m_CheckboxTable);
        GridData gd = GridLayoutFactory.createDefaultLayoutData();
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        gd.horizontalAlignment = SWT.FILL;
//        gd.verticalIndent = SWT.FILL;
        //show 3 columns with data and the header column
        gd.heightHint = sectionBuilder.getHeightHint(3);
        gd.heightHint += m_CheckboxTable.getSWTTable().getHeaderHeight();
        
        m_CheckboxTable.getSWTTable().setLayoutData(gd);
        ColumnConfiguration[] columnConfigurations = createColumnConfigurations(false);
        columnConfigurations[0].enableColumnSorting(false);
        columnConfigurations[2].enableColumnSorting(false);
        m_CheckboxTableViewer = TableUtil.configureTableViewer((TableViewer) m_CheckboxTable
                .getViewer(), columnConfigurations, TestModel.class, false, false);

        // create table tree
        // sectionBuilder.add(m_TreeTable);
        // class SampleObservableTreeListBeanContentProvider extends
        // ObservableTreeListBeanContentProvider{
        //            
        // public Object[] getChildren(Object parentElement)
        // {
        // return new Object[]{generateNewDataModelItem(), generateNewDataModelItem()};
        // }
        //
        // public Object getParent(Object element)
        // {
        // return null;
        // }
        //
        // public boolean hasChildren(Object element)
        // {
        // return ((TestModel) element).getName().equals("Mueller");
        // }
        //
        // public Object[] getElements(Object inputElement)
        // {
        // return dataModel.getList().toArray();
        // }
        //
        // public void dispose()
        // {
        // }
        // };
        // m_TreeTableViewer =
        // TableUtil.configureTreeTableViewer((TreeViewer)m_TreeTable.getViewer(),
        // (ITreeContentProvider)new SampleObservableTreeListBeanContentProvider(),
        // createColumnConfigurations(true), TestModel.class, false);
        //        
        // //create checked table tree
        // sectionBuilder.add(m_CheckboxTreeTable);
        // m_checkedTreeTableViewer =
        // TableUtil.configureTreeTableViewer((TreeViewer)m_CheckboxTreeTable.getViewer(),
        // (ITreeContentProvider)new SampleObservableTreeListBeanContentProvider(),
        // createColumnConfigurations(false), TestModel.class, false);

    }

    @Override
    public void bind(ValidationManager context, Object modelBean)
    {
        Validate.isTrue(modelBean instanceof TableModel);
        this.dataModel = (TableModel) modelBean;

        // set normal table viewers input
        m_TableViewerCursorSupport.setInput(dataModel.getList());
        m_TableViewer.setInput(dataModel.getList());
        // bind selection to table; if its checkbox table, the check state is bound,
        // otherwise the multi selection of rows.
        context.bindSelection(m_TableCursorSupport, dataModel.getSelectedList());
        tableCheckBinding = context.bindSelection(m_Table, dataModel.getSelectedList());

        // set the CheckboxViewer's input
        m_CheckboxTableViewer.setInput(dataModel.getList());

        // bind selection to table; if its checkbox table, the check state is bound,
        // otherwise the multi selection of rows.
        context.bindSelection(m_CheckboxTable, dataModel.getSelectedList());

        // set the TreeViewer's input
        // m_TreeTableViewer.setInput(dataModel.getList());
        // m_checkedTreeTableViewer.setInput(dataModel.getList());

    }

    private void removeFirstItem()
    {
        if (dataModel.getList().size() > 0)
        {
            dataModel.getList().remove(m_CheckboxTableViewer.getElementAt(0));
        }
    }

    private void addRandomItem()
    {
        dataModel.getList().add(this.generateNewDataModelItem());
    }

    private TestModel generateNewDataModelItem()
    {
        // generate Geschlechtscode
        Gender geschlecht = null;
        switch ((int) (Math.random() * 10000) % 3)
        {
            case 0:
                geschlecht = Gender.FEMALE;
                break;
            case 1:
                geschlecht = Gender.MALE;
                break;
            default:
                geschlecht = Gender.UNKNOWN;
                break;
        }

        return new TestModel("Mueller - " + (int) (Math.random() * 1000), new Date((long) (Math
                .random() * 1100000000000L)), (int) (Math.random() * 1000) % 2 == 0 ? true : false,
                (int) (Math.random() * 1000) % 5, Math.random() * 10000, geschlecht, (int) (Math
                        .random() * 1000) % 2 == 0 ? true : false);
    }

    private void toggleSelectableState()
    {
        // clear all selections
        WritableList wl = dataModel.getList();
        for (int i = 0; i < wl.size(); ++i)
        {
            TestModel s2dm = (TestModel) wl.get(i);
            s2dm.setIsSelectable(s2dm.getIsSelectable() ? false : true);
        }

        // Problem -> fires no event... manual refresh...
        m_TableViewerCursorSupport.refresh(true, true);
        m_CheckboxTableViewer.refresh(true, true);
        m_TableViewer.refresh(true, true);
    }

    private void removeList()
    {
        // unbind checkedList from table
        if (tableCheckBinding != null)
        {
            tableCheckBinding.dispose();
            tableCheckBinding = null;
        }

        // m_TableViewer.setInput(null);
        WritableList newList = new WritableList(SWTObservables.getRealm(Display.getDefault()));
        for (int i = 0; i < 10; ++i)
        {
            newList.add(this.generateNewDataModelItem());
        }
        m_TableViewer.setInput(newList);
    }

    public static void main(String[] args)
    {
        final TableModel model = new TableModel();
        final RCPFormPart part = new SandboxTablePart();
        RCPFormFactory.getInstance().startTestShell("SandboxTablePart", part, model);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }
}
