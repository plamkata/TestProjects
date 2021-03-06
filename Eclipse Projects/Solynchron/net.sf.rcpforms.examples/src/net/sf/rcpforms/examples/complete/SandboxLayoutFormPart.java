/*******************************************************************************
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Remo Loetscher - initial API and implementation
 *
 ******************************************************************************
 */

package net.sf.rcpforms.examples.complete;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormFactory;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.modeladapter.converter.MethodValidator;
import net.sf.rcpforms.modeladapter.converter.SymmetricMultilineValidator;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.builder.GridLayoutFactory;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleButton;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleLabel;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * FormPart for GridLayouts in sandbox example. Demonstrates some simple layouts.
 * 
 * @author Remo Loetscher
 */
public class SandboxLayoutFormPart extends RCPFormPart
{
    private RCPSection mainSection;

    private RCPSimpleText m_multiLineText;

    private RCPSimpleLabel m_labelText;

    private RCPDatePicker m_birthDateText;

    private RCPCombo m_geschlechtCombo;

    private RCPSimpleButton m_kontoueberzugCheck;

    private RCPText m_singleLineText;

    /**
     * Constructor for KontodatenViewModel.
     */
    public SandboxLayoutFormPart()
    {
    }

    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create wrappers
        mainSection = new RCPSection("LayoutSection");
        m_labelText = new RCPSimpleLabel("Multiline:");
        m_multiLineText = new RCPSimpleText(SWT.MULTI);
        m_multiLineText.setState(EControlState.MANDATORY, true);

        m_singleLineText = new RCPText("SingleLine:");

        m_birthDateText = new RCPDatePicker("DatePicker:");
        m_birthDateText.setState(EControlState.RECOMMENDED, true);
        m_geschlechtCombo = new RCPCombo("Combo:", false);
        m_kontoueberzugCheck = new RCPSimpleButton("CheckBox", SWT.CHECK | SWT.RIGHT_TO_LEFT);

        // build layout
        GridBuilder builder = new GridBuilder(toolkit, parent, 2);
        GridBuilder widgetBuilder = builder.addContainer(mainSection, 4);

        widgetBuilder.add(m_labelText);

        // build customised layout
        GridData gd = GridLayoutFactory.createDefaultLayoutData();
//        gd.horizontalAlignment = SWT.FILL;
        gd.widthHint = widgetBuilder.getWidthHint(10);
        gd.horizontalSpan = 3;
        gd.grabExcessHorizontalSpace = true;
//        gd.verticalAlignment = SWT.FILL;
        gd.heightHint = widgetBuilder.getHeightHint(4);
        gd.verticalSpan = 3;
        gd.grabExcessVerticalSpace = true;
        widgetBuilder.add(m_multiLineText, gd);

        widgetBuilder.add(new RCPSimpleLabel("Vertical Span: 2nd Line..."));
        widgetBuilder.add(new RCPSimpleLabel("Vertical Span: 3rd Line..."));
        widgetBuilder.newLine();
        // go ahead with "normal" layout.

        // add empty lines
        widgetBuilder.add(new RCPSimpleLabel("Filler and empty line added..."));
        widgetBuilder.fillLine();
        widgetBuilder.newLine();

        widgetBuilder.addLine(m_birthDateText, 8);
        widgetBuilder.addSkipLeftLine(m_singleLineText, 1);
        widgetBuilder.addLine(m_kontoueberzugCheck);
        // create one radio button for each code and associate a radio group manager

        widgetBuilder.addLine(m_geschlechtCombo);

    }

    public void bind(ValidationManager context, Object dataModel)
    {
        context.bindValue(m_multiLineText, dataModel, SimpleBean.P_TEXT);
//        MethodValidator validator = new AsymmetricMultilineValidator(SimpleBean.P_TEXT, new int[]{1,2,3});
        MethodValidator validator = new SymmetricMultilineValidator(SimpleBean.P_TEXT, 3, 35);
        context.addValidator(this, validator); 
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

    public static void main(String[] args)
    {

        final Object model = new SimpleBean();
        final RCPFormPart part = new SandboxLayoutFormPart();
        RCPFormFactory.getInstance().startTestShell("SandboxLayoutFormPart", part, model);
    }

}
