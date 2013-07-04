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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.common.NullValue;
import net.sf.rcpforms.examples.complete.widgets.RCPLabeledPickerComposite;
import net.sf.rcpforms.modeladapter.converter.ObservableStatusToBooleanAdapter;
import net.sf.rcpforms.modeladapter.converter.RequiredValidator;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPLabeledControl;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;
import net.sf.rcpforms.widgetwrapper.wrapper.radio.RadioGroupManager;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;

import com.damnhandy.aspects.bean.JavaBean;

/**
 * FormPart for Section 2 in sandbox example.
 * <p>
 * Demonstrates all variations of binding:
 * <ul>
 * <li>text fields bound to different attribute types (text, date, int, float)
 * <li>checkbox to boolean
 * <li>radio group with and without <null> value
 * <li>combo with and without <null> value
 * <li>readonly combo and text rendering
 * <li>RCPDatePicker compound with a date selection dialog
 * <li>general null value handling
 * <li>automatic use of range adapters for radio groups and combos
 * </ul>
 *
 * @author vanmeegenm
 */
public class Sandbox2FormPart extends RCPFormPart
{
    private RCPSection mainSection;

    private RCPText m_nameText;

    private RCPDatePicker m_birthDateText;

    private RCPCombo m_readOnlyGeschlechtCombo;

    private RCPCombo m_geschlechtCombo;

    private RCPCombo m_geschlechtComboWithNull;

    private RCPText m_geschlechtText;

    private RCPSimpleButton m_kontoueberzugCheck;

    private RCPText m_kinderanzahlText;

    private RCPText m_kontostandText;

    // radio to code binding
    private RCPRadioGroup m_geschlechtGroup;

    private RCPRadioGroup m_geschlechtGroupWithNull;

    private RCPSimpleButton m_kontoueberzugYesRadio;

    private RCPSimpleButton m_kontoueberzugNoRadio;

    private RCPSimpleButton m_resetValuesButton;

    private RCPSimpleButton m_isValidButton;

    private MessageDialog dialog;

    /**
     * Constructor for KontodatenViewModel.
     */
    public Sandbox2FormPart()
    {
    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create wrappers

        mainSection = new RCPSection("Section 1");
        m_nameText = new RCPText("Name:");
        m_nameText.setState(EControlState.MANDATORY, true);
        m_birthDateText = new RCPDatePicker("Geburtsdatum:");
        m_birthDateText.setState(EControlState.MANDATORY, true);
        m_geschlechtCombo = new RCPCombo("Geschlecht:", false);
        m_geschlechtComboWithNull = new RCPCombo("Geschlecht(incl. null):", true);
        m_geschlechtComboWithNull
                .setNullValuePresentationKey(NullValue.GENERIC_NULL_VALUE_REPRESENTATION_DASH);
        m_readOnlyGeschlechtCombo = new RCPCombo("Geschlecht(readonly combo):");
        m_readOnlyGeschlechtCombo.setState(EControlState.READONLY, true);
        m_geschlechtText = new RCPText("Geschlecht (readonly text):");
        m_geschlechtText.setState(EControlState.READONLY, true);
        m_kontoueberzugCheck = new RCPSimpleButton("Konto�berzug erw�nscht", SWT.CHECK);
        m_kinderanzahlText = new RCPText("Kinderanzahl:");
        m_kontostandText = new RCPText("Kontostand:");
        m_kontostandText.setState(EControlState.MANDATORY, true);
        m_geschlechtGroup = new RCPRadioGroup("Geschlecht:", false);
        m_geschlechtGroupWithNull = new RCPRadioGroup("Geschlecht(with null):", true);
        // m_geschlechtGroupWithNull.setNullValuePresentationKey(NullValue.GENERIC_NULL_VALUE_REPRESENTATION_NO_SELECTION);

        m_kontoueberzugYesRadio = new RCPSimpleButton("Konto �berziehbar", SWT.RADIO);
        m_kontoueberzugNoRadio = new RCPSimpleButton("Konto nicht �berziehbar", SWT.RADIO);
        m_resetValuesButton = new RCPSimpleButton("Reset Datamodel values to null", SWT.PUSH);
        m_isValidButton = new RCPSimpleButton("Form is valid:", SWT.CHECK);
        m_isValidButton.setState(EControlState.ENABLED, false);

        this.createDialog(parent);
        // build layout
        GridBuilder builder = new GridBuilder(toolkit, parent, 2);
        GridBuilder widgetBuilder = builder.addContainer(mainSection, 4);

        widgetBuilder.addLine(m_kinderanzahlText);

        widgetBuilder.addLine(m_kontostandText, 6);

        final Dialog d = dialog;

        IHyperlinkListener hlListener = new HyperlinkAdapter()
        {
            @Override
            public void linkActivated(HyperlinkEvent e)
            {

                // MessageDialog.openInformation(m_kontostandText.getSWTControl().getShell(),
                // "Money", sb.toString());
                d.open();
            }
        };
        m_kontostandText.getRCPHyperlink().addHyperlinkListener(hlListener);
        m_kontostandText.getRCPHyperlink().removeHyperlinkListener(new HyperlinkAdapter());
        widgetBuilder.addLine(m_nameText, 20);
        m_nameText.getRCPHyperlink().addHyperlinkListener(hlListener);
        m_nameText.getRCPHyperlink().removeHyperlinkListener(hlListener);
        // m_nameText.getRCPHyperlink().setState(EControlState.ENABLED, false);
        widgetBuilder.addLine(m_birthDateText, 8);
        
        widgetBuilder.fillLine();
        RCPText text = new RCPText("Using efficient layout:");
        widgetBuilder.addLineSpan(text, true);
        widgetBuilder.fillLine();
        
        RCPLabeledPickerComposite pcWidget = new RCPLabeledPickerComposite("PickerComposite (using efficient layout)");
        widgetBuilder.addLineSpan(pcWidget, true);
        widgetBuilder.fillLine();
        
        
        pcWidget = new RCPLabeledPickerComposite("PickerComposite:");
        widgetBuilder.addLineSpan(pcWidget, false);
        widgetBuilder.fillLine();
        
        RCPComposite pickerComposite = new RCPComposite();
        RCPDatePicker datePicker = new RCPDatePicker("");
        datePicker.setState(EControlState.MANDATORY, true);
        RCPSimpleLabel simpleLabel = new RCPSimpleLabel("DatePicker (Composite): ");
        widgetBuilder.add(simpleLabel);
        GridData gd = new GridData();
        gd.horizontalAlignment = SWT.END;
        simpleLabel.setLayoutData(gd);
        GridBuilder datePickerBuilder = widgetBuilder.addAlignedContainer(pickerComposite, 2, 3);
        datePickerBuilder.add(datePicker);
        datePicker.getText().getRCPHyperlink().setVisible(false);
        
        widgetBuilder.addSkipLeftLine(m_kontoueberzugCheck, 1);
        m_kontoueberzugCheck.getSWTButton().addSelectionListener(new SelectionAdapter(){

            @Override
            public void widgetSelected(SelectionEvent e)
            {
                boolean state = m_kontoueberzugCheck.getSWTButton().getSelection();
                System.out.println("");
                boolean state2 = m_readOnlyGeschlechtCombo.hasState(EControlState.READONLY);
                m_readOnlyGeschlechtCombo.setState(EControlState.READONLY, !state);
                
                m_birthDateText.setState(EControlState.READONLY, state);
                m_birthDateText.setState(EControlState.MANDATORY, true);
            }});
        // create one radio button for each code and associate a radio group manager
        widgetBuilder.add(m_geschlechtGroup);
        widgetBuilder.addLine(m_geschlechtGroupWithNull);

        widgetBuilder.fill(1).add(m_kontoueberzugYesRadio);
        widgetBuilder.fillLine();
        widgetBuilder.addSkipLeftLine(m_kontoueberzugNoRadio, 1);

        widgetBuilder.addLine(m_geschlechtCombo);
        widgetBuilder.addLine(m_geschlechtComboWithNull);
        widgetBuilder.addLine(m_readOnlyGeschlechtCombo);
        widgetBuilder.addLine(m_geschlechtText);
        widgetBuilder.addLine(m_resetValuesButton);
        widgetBuilder.addLine(m_isValidButton);

    }

    @Override
    public void bind(ValidationManager context, Object dataModel)
    {
        final TestModel dm = (TestModel) dataModel;
        context.bindValue(m_nameText, dm, TestModel.P_Name);
        context.bindValue(m_birthDateText, dm, TestModel.P_BirthDate);
        context.bindValue(m_kontoueberzugCheck, dm, TestModel.P_OverdrawAccount);
        context.bindValue(m_readOnlyGeschlechtCombo, dm, TestModel.P_Gender);
        context.bindValue(m_geschlechtCombo, dm, TestModel.P_Gender);
        context.bindValue(m_geschlechtComboWithNull, dm, TestModel.P_Gender);
        context.bindValue(m_geschlechtText, dm, TestModel.P_Gender);

        // need radiogroup manager for check
        RadioGroupManager rgm = new RadioGroupManager(new RCPSimpleButton[]{
                m_kontoueberzugYesRadio, m_kontoueberzugNoRadio}, new Object[]{Boolean.TRUE,
                Boolean.FALSE});
        context.bindValue(rgm, dm, TestModel.P_OverdrawAccount);
        context.bindValue(m_kinderanzahlText, dm, TestModel.P_ChildCount);
        context.bindValue(m_kontostandText, dm, TestModel.P_AccountBalance);
        context.bindValue(m_geschlechtGroup, dm, TestModel.P_Gender);
        context.bindValue(m_geschlechtGroupWithNull, dm, TestModel.P_Gender);
        // bind valid check
        ObservableStatusToBooleanAdapter booleanState = new ObservableStatusToBooleanAdapter(
                context.getValidationState());
        context.bindValue(m_isValidButton, booleanState);
        
        // context.bindInvertedState(m_isValidButton, EControlState.ENABLED, booleanState);
        // required field validator
        RequiredValidator validator1 = new RequiredValidator(TestModel.P_Name);
        context.addValidator(this, validator1);
        
        // example for validator which should not show message, not decorate field but only validate
        // to false
        RequiredValidator validator2 = new RequiredValidator(
                TestModel.P_AccountBalance);
        //hide field decoration
        validator2.setDecorateField(false);
        //hide error message in message header
        validator2.setShowMessage(false);
        
        context.addValidator(this, validator2);
        m_resetValuesButton.getSWTButton().addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                dm.setBirthDate(null);
                dm.setGender(null);
                dm.setIsSelectable(null);
                dm.setChildCount(null);
                dm.setAccountBalance(null);
                dm.setOverdrawAccount(null);
                dm.setName(null);
            }
        });
        m_resetValuesButton.getSWTButton().setToolTipText(
                "Resetting is only permitted if form is valid. See \"Form is valid:\" checkbox!");
        context.bindState(m_resetValuesButton, EControlState.ENABLED, booleanState);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

    public void setFocus()
    {
        m_nameText.getSWTControl().setFocus();
    }

    public static void main(String[] args)
    {

        final TestModel model = new TestModel();
        final RCPFormPart part = new Sandbox2FormPart();
        ((JavaBean) model).addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPFormFactory.getInstance().startTestShell("Sandbox2FormPart", part, model);
    }

    private void createDialog(Composite parent)
    {
        final StringBuilder sb = new StringBuilder("\n");
        sb.append("        $                $                $\n");
        sb.append("     ,$$$$$,          ,$$$$$,          ,$$$$$,\n");
        sb.append("   ,$$$'$`$$$       ,$$$'$`$$$       ,$$$'$`$$$\n");
        sb.append("   $$$  $   `       $$$  $   `       $$$  $   `\n");
        sb.append("   '$$$,$           '$$$,$           '$$$,$\n");
        sb.append("     '$$$$,           '$$$$,           '$$$$,\n");
        sb.append("       '$$$$,           '$$$$,           '$$$$,\n");
        sb.append("        $ $$$,           $ $$$,           $ $$$,\n");
        sb.append("    ,   $  $$$       ,   $  $$$       ,   $  $$$\n");
        sb.append("    $$$,$.$$$'       $$$,$.$$$'       $$$,$.$$$'\n");
        sb.append("     '$$$$$'          '$$$$$'          '$$$$$'\n");
        sb.append("        $                $                $\n");

        dialog = new MessageDialog(parent.getShell(), "Money", null, sb.toString(),
                MessageDialog.INFORMATION, new String[]{IDialogConstants.OK_LABEL}, 0)
        {

            @Override
            protected Control createMessageArea(Composite parent)
            {
                Control result = super.createMessageArea(parent);
                result.setFont(JFaceResources.getTextFont());
                if (result instanceof Composite)
                {
                    // set font on every child.
                    for (Control c : ((Composite) result).getChildren())
                        c.setFont(JFaceResources.getTextFont());
                }
                return result;
            }

        };
    }
}