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
// Created on 27.04.2008

package net.sf.rcpforms.test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.modeladapter.converter.AbstractModelValidator;
import net.sf.rcpforms.tablesupport.tables.ColumnConfiguration;
import net.sf.rcpforms.tablesupport.tables.ECellEditorType;
import net.sf.rcpforms.tablesupport.tables.TableUtil;
import net.sf.rcpforms.test.adapter.AddressModel;
import net.sf.rcpforms.test.adapter.TestModel;
import net.sf.rcpforms.test.adapter.TestModel.Gender;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.IconText;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.util.WidgetUtil;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCheckbox;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCheckboxTable;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPIconText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPList;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPPushButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPToggleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPTree;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * example form part using RCPForms controls. This part is used for testing widget layout and state
 * rendering by visually inspecting the result.
 * 
 * @author Marco van Meegen
 */
public class RCPWidgetsManualTest extends RCPFormPart
{
    private String ICON_TEXT = "Lorem ipsum dolor sit amet, consectetuer sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";

    /**
     * configures the editable table
     */
    private static final ColumnConfiguration[] TABLE_VIEWER_CONFIGURATION = {
            new ColumnConfiguration("Name", TestModel.P_Name, 100, SWT.LEFT, false,
                    ECellEditorType.TEXT).setGrabHorizontal(true),
            new ColumnConfiguration("Birthdate", TestModel.P_BirthDate, 80, SWT.LEFT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Gender", TestModel.P_Gender, 60, SWT.LEFT,
                    net.sf.rcpforms.test.adapter.TestModel.Gender.values()),
            new ColumnConfiguration("Overdraw", TestModel.P_OverdrawAccount, 50, SWT.CENTER, false,
                    ECellEditorType.CHECK),
            new ColumnConfiguration("Children", TestModel.P_ChildCount, 50, SWT.RIGHT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Balance", TestModel.P_AccountBalance, 70, SWT.RIGHT, false,
                    ECellEditorType.TEXT),
            new ColumnConfiguration("Zip", TestModel.P_Address + "." + AddressModel.P_ZipCode, 70,
                    SWT.RIGHT, false, ECellEditorType.TEXT)};

    protected RCPSection mainSection;

    protected RadioGroupManager rgManager;

    protected RCPCombo combo = new RCPCombo("Combo:");

    protected RCPText text = new RCPText("Text:");

    protected RCPText textNumber = new RCPText("Number:");

    protected RCPText multiText = new RCPText("Multiline:", SWT.MULTI);

    protected RCPText date = new RCPText("Date:");

    private RCPRadioGroup m_radioGroup;

    private RCPDatePicker datePicker = new RCPDatePicker("Date:");

    private RCPIconText iconText = new RCPIconText(Display.getCurrent().getSystemImage(
            SWT.ICON_INFORMATION));

    public RCPWidgetsManualTest()
    {

    }

    @Override
    public void createUI(FormToolkit formToolkit, Composite parent)
    {
        // main UI creation method
        // wrap parent into RCPWrapper to start building
        RCPComposite parentWidget = new RCPComposite(parent);
        // create a builder
        GridBuilder builder = new GridBuilder(formToolkit, parentWidget, 3);
        // create first section
        List<RCPControl> allControls = createSectionWithAllControls(parentWidget, builder);
        // create section with check boxes for states
        createSectionWithStateModificationControls(parentWidget, builder, allControls);
    }

    @Override
    public void bind(ValidationManager bm, Object modelBean)
    {
        // data binding of values to model, selecting the correct Observable and converter stuff is
        // mainly automatic
        bm.bindValue(rgManager, modelBean, TestModel.P_Gender);
        bm.bindValue(m_radioGroup, modelBean, TestModel.P_Gender);
        // combo.getViewer().setInput(TestModel.Gender.values());
        bm.bindValue(combo, modelBean, TestModel.P_Gender);
        bm.bindValue(text, modelBean, TestModel.P_Name);
        bm.bindValue(textNumber, modelBean, TestModel.P_Age);
        bm.bindValue(date, modelBean, TestModel.P_BirthDate);
        bm.bindValue(datePicker, modelBean, TestModel.P_BirthDate);
        multiText.setState(EControlState.READONLY, true);
        // bm.bindValue(multiText, modelBean, TestModel.P_Gender);
        // add a validator to the binding manager, it will automatically be revalidated once a
        // property used by the validator changes
        // markers will be set to a control bound to the properties defined by the validator
        createValidators(bm);
    }

    protected void createValidators(ValidationManager bm)
    {
        // add a validator which validates that age and birthdate fit
        bm.addValidator(this, new AbstractModelValidator()
        {
            private String[] properties = {TestModel.P_Age, TestModel.P_BirthDate};

            @Override
            public Object[] getProperties()
            {
                // used by ValidationManager to determine the widgets which should be error-marked
                return properties;
            }

            // the validation method; IStatus message is set into the error marker tooltips and the
            // message display
            @Override
            public IStatus validate(Object value)
            {
                // this is obviously wrong if your birthday has not passed
                // but good enough as example
                IStatus result = ok();
                TestModel model = (TestModel) value;
                if (model.getBirthDate() != null)
                {
                    Calendar calendar = GregorianCalendar.getInstance();
                    calendar.setTime(model.getBirthDate());
                    Calendar today = GregorianCalendar.getInstance();
                    today.setTime(new Date());
                    if (model.getAge() != today.get(Calendar.YEAR) - calendar.get(Calendar.YEAR))
                    {
                        result = error("Are you cheating about your age ?");
                    }
                }
                return result;
            }
        });
    }

    private void createSectionWithStateModificationControls(RCPComposite parent,
                                                            GridBuilder builder,
                                                            final List<RCPControl> allControls)
    {
        // create different widgets and sections
        RCPSection section = new RCPSection("Section 2");
        GridBuilder sectionBuilder = builder.addContainer(section);
        // add buttons for enable section
        final RCPToggleButton enableSectionButton = new RCPToggleButton("Enable Section 1");
        builder.add(enableSectionButton);
        enableSectionButton.getSWTButton().setSelection(true);
        enableSectionButton.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                boolean newState = ((Button) e.widget).getSelection();
                mainSection.setState(EControlState.ENABLED, newState);
            }
        });

        // add groups for states

        RCPGroup group1 = new RCPGroup("States", SWT.FLAT);
        final GridBuilder sf = sectionBuilder.addContainer(group1);

        for (EControlState state : EControlState.values())
        {
            final RCPSimpleButton button = new RCPSimpleButton(state.name(), SWT.CHECK);
            sf.add(button);
            button.getSWTButton().setSelection(
                    state == EControlState.VISIBLE || state == EControlState.ENABLED);
            final EControlState finalState = state;
            button.getSWTButton().addSelectionListener(new SelectionAdapter()
            {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                    boolean newState = ((Button) e.widget).getSelection();
                    setState(allControls, finalState, newState);
                }
            });
        }
    }

    private List<RCPControl> createSectionWithAllControls(RCPComposite parent, GridBuilder builder)
    {
        // create different widgets and sections
        mainSection = new RCPSection("Section 1");
        GridBuilder sectionBuilder = builder.addContainer(mainSection, 3);

        sectionBuilder.fill(1).addLine(new RCPPushButton("Button"));
        sectionBuilder.fill(1).addLine(new RCPToggleButton("Toggle"));
        sectionBuilder.fill(1).addLine(new RCPCheckbox("Check"));
        sectionBuilder.fill(1).addLine(new RCPRadioButton("Radio1"));
        sectionBuilder.fill(1).addLine(new RCPRadioButton("Radio2"));
        sectionBuilder.addLine(combo);
        sectionBuilder.addLine(text);
        sectionBuilder.addLine(textNumber);
        sectionBuilder.addLine(date);
        // TODO: implement vhint in builder -> see task 152106
        sectionBuilder.addLineGrabAndFill(multiText, 2, 3);
        multiText.getSWTText().setText("one\ntwo\nthree");
        sectionBuilder.addLine(iconText);
        IconText it = iconText.getTypedWidget();
        it.setTitle("Icon Text Title");
        it.setText(ICON_TEXT);
        sectionBuilder.addLine(new RCPList("List:"));
        createTableViewer(sectionBuilder);
        sectionBuilder.addLine(new RCPTree("Tree:"));
        sectionBuilder.fill(1).addLine(new RCPSimpleLabel("Label:"));
        sectionBuilder.addLine(datePicker);

        // create a radio group using the convenience widget (recommended)
        LabelProvider labelProvider = new LabelProvider()
        {
            @Override
            public String getText(Object element)
            {
                // convert default toString() of Gender to lower case
                return super.getText(element).toLowerCase();
            }
        };
        m_radioGroup = new RCPRadioGroup("Gender (using RCPRadioGroup with <null>):", SWT.DEFAULT,
                true, true, true, 2);
        List<Object> values = new ArrayList<Object>(Arrays.asList(Gender.values()));
        values.add(NullValue.getInstance());
        m_radioGroup.createRadioButtons(values, labelProvider);
        sectionBuilder.fill(1).addSpan(m_radioGroup, -1, 2, 1);

        // create a group and radio buttons for an enum value explicitly using builder, no null
        // representation added
        RCPGroup group = new RCPGroup("Gender (explicitly built):");
        rgManager = sectionBuilder.fill(1).addRadiogroup(group, TestModel.Gender.values(),
                labelProvider, 2);

        List<RCPWidget> createdControls = sectionBuilder.getCreatedControls();
        List<RCPControl> result = WidgetUtil.getControls(createdControls);
        return result;
    }

    private void createTableViewer(final GridBuilder sf)
    {
        RCPCheckboxTable table = new RCPCheckboxTable("Table:");
        sf.addLine(table);
        TableViewer tableViewer = (TableViewer) table.getViewer();
        TableUtil.configureTableViewer(tableViewer, TABLE_VIEWER_CONFIGURATION, TestModel.class,
                true, false);
        WritableList list = new WritableList();
        list.add(new TestModel("Schmitz", 55));
        list.add(new TestModel("Meier", 33));
        tableViewer.setInput(list);
    }

    protected void setState(List<RCPControl> allControls, EControlState state, boolean value)
    {
        for (RCPControl rcpWidget : allControls)
        {
            rcpWidget.setState(state, value);
        }
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

    public static void main(String[] args)
    {
        // simple starter to start the form part in a shell as swt application without osgi runtime
        final TestModel model = new TestModel();
        final RCPFormPart part = new RCPWidgetsManualTest();
        ((JavaBean) model).addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPFormFactory.getInstance().startTestShell("RCPWidgetsManualTest", part, model);
    }
}
