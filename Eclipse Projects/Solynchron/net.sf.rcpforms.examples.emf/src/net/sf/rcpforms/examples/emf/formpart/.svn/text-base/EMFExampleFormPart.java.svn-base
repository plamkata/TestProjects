
package net.sf.rcpforms.examples.emf.formpart;

import java.util.List;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.emf.EMFFormFactory;
import net.sf.rcpforms.emf.test.*;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.TableUtil;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Example EMF form part having a table section BEWARE: STILL ALPHA, please contribute !
 * 
 * @author Marco van Meegen
 */
public class EMFExampleFormPart extends RCPFormPart
{
    public static class TestModelLabelProvider extends LabelProvider
    {
        @Override
        public String getText(Object instance)
        {
            if (instance instanceof TestModel)
            {
                String name = ((TestModel) instance).getName();
                return name == null ? "<unnamed>" : name;
            }
            else
            {
                return super.getText(instance);
            }
        }
    }

    // main section
    private RCPSection section;

    private RCPText name;

    private RCPText age;

    private RCPCombo gender;

    private RCPDatePicker birthdate;

    // table section
    private RCPSection tableSection = new RCPSection("Table Viewer", Section.TITLE_BAR
            | Section.DESCRIPTION);

    private RCPPushButton addBtn = new RCPPushButton("add entry");

    private RCPPushButton removeBtn = new RCPPushButton("remove entry");

    private RCPTable table = new RCPTable(null);

    private TableViewer tableViewer;

    private ConfigurationModel model = null;

    private Adapter changeListener;

    private static ColumnConfiguration[] columnConfigurations = {
            new ColumnConfiguration("Name", TestPackage.eINSTANCE.getTestModel_Name().getName(),
                    100, SWT.LEFT, false, ECellEditorType.TEXT),
            new ColumnConfiguration("Gender",
                    TestPackage.eINSTANCE.getTestModel_Gender().getName(), 80, SWT.RIGHT, Gender
                            .values()),
            new ColumnConfiguration("Age", TestPackage.eINSTANCE.getTestModel_Age().getName(), 60,
                    SWT.RIGHT, false, ECellEditorType.TEXT),
            new ColumnConfiguration("Birthdate", TestModelConstants.P_BirthDate, 80, SWT.LEFT,
                    false, ECellEditorType.DATE),
            new ColumnConfiguration("Overdraw", TestModelConstants.P_OverdrawAccount, 60,
                    SWT.CENTER, false, ECellEditorType.CHECK),
            new ColumnConfiguration("Children", TestModelConstants.P_ChildCount, 80, SWT.RIGHT,
                    false, ECellEditorType.TEXT),
            new ColumnConfiguration("Balance", TestModelConstants.P_AccountBalance, 70, SWT.RIGHT,
                    false, ECellEditorType.TEXT)

    };

    public EMFExampleFormPart()
    {
        // listen to changes of levels in the list and update the table viewer
        changeListener = new AdapterImpl()
        {
            @Override
            public void notifyChanged(Notification msg)
            {
                if (tableViewer != null)
                {
                    tableViewer.update(msg.getNotifier(), null);
                }
            }
        };
    }

    @Override
    public void bind(ValidationManager bm, Object modelBean)
    {
        assert modelBean instanceof ConfigurationModel : "EMFExampleFormPart only suitable for input of ConfigurationModel";
        model = (ConfigurationModel) modelBean;
        TestModel testModel = model.getTestModel();
        // bind values for main section
        bm.bindValue(name, testModel, TestPackage.eINSTANCE.getTestModel_Name().getName());
        bm.bindValue(age, testModel, TestPackage.eINSTANCE.getTestModel_Age().getName());
        // TODO: check enum conversion stuff with EMF
        bm.bindValue(gender, testModel, TestPackage.eINSTANCE.getTestModel_Gender().getName());
        // TODO: check Date conversion by emf, it needs yyyy-mm-dd which conflicts with DatePicker
        // output
        bm
                .bindValue(birthdate, testModel, TestPackage.eINSTANCE.getTestModel_BirthDate()
                        .getName());

        // and configure table section
        tableViewer = TableUtil.configureTableViewer((TableViewer) table.getViewer(),
                columnConfigurations, TestPackage.eINSTANCE.getTestModel(), false, true);
        tableViewer.setInput(EMFObservables.observeList(model, TestPackage.eINSTANCE
                .getConfigurationModel_TestModels()));

    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // table section
        RCPComposite parentWidget = new RCPComposite(parent);
        GridBuilder builder = new GridBuilder(toolkit, parentWidget, 1);
        section = new RCPSection("This title will be invisible", Section.NO_TITLE);
        name = new RCPText("Name: ");
        name.setState(EControlState.MANDATORY, true);
        age = new RCPText("Age");
        gender = new RCPCombo("Gender: ");
        birthdate = new RCPDatePicker("Birthdate: ");
        birthdate.setState(EControlState.MANDATORY, true);

        // add elements to container
        GridBuilder formPartBuilder = new GridBuilder(toolkit, parent, 1);
        GridBuilder sectionMainBuilder = formPartBuilder.addContainer(section, 3);
        // 1st line
        sectionMainBuilder.addLineGrabAndFill(name, 2);
        // 2nd line
        sectionMainBuilder.addLine(age, 2);
        // 3rd line
        sectionMainBuilder.addLine(gender);
        // 4th line
        sectionMainBuilder.addLine(birthdate, 10);

        GridBuilder tableSectionBuilder = builder.addContainer(tableSection, 3);
        tableSection.getSWTSection().setDescription(
                "Successively add test models by pressing 'add row', "
                        + "then edit a displayed value by clicking on it and pressing enter.\n");
        tableSectionBuilder.add(addBtn);
        addBtn.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                addEntry();
            }
        });
        tableSectionBuilder.addLine(removeBtn);
        removeBtn.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                removeEntry();
            }
        });
        tableSectionBuilder.addLine(table);
        GridData layoutData = new GridData(SWT.LEFT, SWT.TOP, true, true);
        layoutData.heightHint = 100;
        layoutData.horizontalSpan = 3;
        table.setLayoutData(layoutData);
    }

    protected void addEntry()
    {
        if (model != null)
        {
            TestModel testModel = TestFactory.eINSTANCE.createTestModel();
            testModel.setName("new");
            model.getTestModels().add(testModel);
            testModel.eAdapters().add(changeListener);
        }
        table.reflowForm();
    }

    @SuppressWarnings("unchecked")
    protected void removeEntry()
    {
        if (model != null && tableViewer != null && !tableViewer.getSelection().isEmpty())
        {
            IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
            for (TestModel entry : (List<TestModel>) selection.toList())
            {
                model.getTestModels().remove(entry);
                entry.eAdapters().remove(changeListener);
            }
        }
        table.reflowForm();
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        // not needed here
    }

    public static void main(String[] args)
    {
        // create an emf model with change listeners
        AdapterImpl adapter = new AdapterImpl()
        {
            @Override
            public void notifyChanged(Notification msg)
            {
                super.notifyChanged(msg);
                System.out.println("Model changed: " + msg);
            }
        };
        ConfigurationModel model = TestFactory.eINSTANCE.createConfigurationModel();
        // single instance with direct binding
        TestModel testModel = TestFactory.eINSTANCE.createTestModel();
        testModel.setName("entry");
        model.setTestModel(testModel);
        testModel.eAdapters().add(adapter);

        // instances in list for table binding
        TestModel testModel1 = TestFactory.eINSTANCE.createTestModel();
        testModel1.setName("entry1");
        model.getTestModels().add(testModel1);
        TestModel testModel2 = TestFactory.eINSTANCE.createTestModel();
        testModel2.setName("entry2");
        testModel2.eAdapters().add(adapter);
        model.getTestModels().add(testModel2);
        model.eAdapters().add(adapter);
        // create the form part
        final RCPFormPart part = new EMFExampleFormPart();
        // start a test shell using EMFFormFactory
        // EMFFormFactory.getInstance() will init rcpforms to work with EMF,
        // registering the EMFModelAdapter, EMFUpdateValueStrategy for converters, subclassing
        // ValidationManager to use EMF bindings and
        // set default table content provider to one supporting emf
        EMFFormFactory.getInstance().startTestShell("EMF Example", part, model);
    }

}
