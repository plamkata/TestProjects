package net.sf.rcpforms.examples.complete;

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
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SandboxTableMasterDetailFormPart extends RCPFormPart
{
    private IObservableValue masterValue = null;
    private TableModel model;
    private RCPSection mainSection;
    private RCPText name = new RCPText("name: "), 
    birthDate = new RCPText("birthDate: "), 
    age = new RCPText("age: "), 
    overdrawAccount = new RCPText("overdrawAccount: "), 
    childCount = new RCPText("childCount: "), 
    accountBalance = new RCPText("accountBalance: "), 
    gender = new RCPText("gender: ");
    
    private RCPSimpleTable m_Table = new RCPSimpleTable(SWT.SINGLE);
    private TableViewer m_TableViewer;


    @Override
    public void bind(ValidationManager bm, Object modelBean)
    {
        this.model = (TableModel) modelBean;
        m_TableViewer.setInput(model.getList());
        masterValue = bm.bindDetailValue(modelBean, TableModel.P_TEST_MODEL, new RCPControl[]{name,
                birthDate, age, overdrawAccount, childCount, accountBalance, gender},
                new String[]{TestModel.P_Name, TestModel.P_BirthDate, TestModel.P_Age,
                        TestModel.P_OverdrawAccount, TestModel.P_ChildCount,
                        TestModel.P_AccountBalance, TestModel.P_Gender});
        bm.bindSelection(m_Table, masterValue);
    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent)
    {
        GridBuilder mainBuilder = new GridBuilder(toolkit, parent, 2);
        mainSection = new RCPSection("MasterDetail binding");
        GridBuilder section = mainBuilder.addContainer(mainSection, 2);
        
        section.add(m_Table);
        m_TableViewer = TableUtil.configureTableViewer((TableViewer) m_Table.getViewer(),
                createColumnConfigurations(false), TestModel.class, false, false);
        
        RCPGroup group = new RCPGroup("Selected object: ");
        GridBuilder groupBuilder = section.addContainerSpan(group, 2, 2, 1, false);
        groupBuilder.addLineGrabAndFill(name, 1);
//        name.setState(EControlState.READONLY, true);
        
        groupBuilder.addLine(birthDate);
        birthDate.setState(EControlState.READONLY, true);
        
        groupBuilder.addLine(age);
        age.setState(EControlState.READONLY, true);
        
        groupBuilder.addLineGrabAndFill(overdrawAccount, 1);
        overdrawAccount.setState(EControlState.READONLY, true);
        
        groupBuilder.addLine(childCount);
        childCount.setState(EControlState.READONLY, true);
        
        groupBuilder.addLineGrabAndFill(accountBalance, 1);
        accountBalance.setState(EControlState.READONLY, true);
        
        groupBuilder.addLine(gender);
        gender.setState(EControlState.READONLY, true);

        toolkit.paintBordersFor(group.getClientComposite());
        
        toolkit.paintBordersFor(parent);
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
                        editable ? ECellEditorType.TEXT : null).setGrabHorizontal(true)};
        Validate.noNullElements(columnConfigurations,
                "ColumnConfigurations must not have null elements");
        return columnConfigurations;
    }
    
    @Override
    public void setState(EControlState state, boolean value)
    {
        // TODO Auto-generated method stub

    }
    
    public static void main(String[] args)
    {
        final TableModel model = new TableModel();
        final RCPFormPart part = new SandboxTableMasterDetailFormPart();
        RCPFormFactory.getInstance().startTestShell("SandboxTablePart", part, model);
    }

}
