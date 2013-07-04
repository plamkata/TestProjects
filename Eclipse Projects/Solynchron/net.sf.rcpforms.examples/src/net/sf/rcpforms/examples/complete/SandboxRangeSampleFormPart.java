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
import net.sf.rcpforms.modeladapter.configuration.IntegerRangeAdapter;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * FormPart for IntegerRange in sandbox example.
 * <p>
 * Demonstrates all variations of integerRange support:
 * <ul>
 * <li>Range by setting input
 * <li>Range by using simple {@link IntegerRangeAdapter#IntegerRangeAdapter(int, int, int) annotation without steps
 * <li>Range by using simple {@link IntegerRangeAdapter#IntegerRangeAdapter(int, int, int) annotation without positive step
 * <li>Range by using simple {@link IntegerRangeAdapter#IntegerRangeAdapter(int, int, int) annotation without negative step
 * </ul>
 * 
 * @author Remo Loetscher
 */
public class SandboxRangeSampleFormPart extends RCPFormPart
{
    private RCPSection mainSection;

    private RCPCombo m_numberComboAnnotations, m_numberCombo, m_numberComboAnnotationsDEC,
            m_numberComboAnnotationsINC;

    /**
     * Constructor for KontodatenViewModel.
     */
    public SandboxRangeSampleFormPart()
    {
    }

    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create wrappers

        mainSection = new RCPSection("MinMax Values using integerRanges");

        m_numberCombo = new RCPCombo("Numbers (setInput): ");
        m_numberComboAnnotations = new RCPCombo("Numbers (annotated): ");
        m_numberComboAnnotationsDEC = new RCPCombo("Numbers (annotated, range DEC): ");
        m_numberComboAnnotationsINC = new RCPCombo("Numbers (annotated, range INC): ");

        // build layout
        GridBuilder builder = new GridBuilder(toolkit, parent, 2);
        GridBuilder widgetBuilder = builder.addContainer(mainSection, 3);

        widgetBuilder.addLine(m_numberCombo);
        widgetBuilder.addLine(m_numberComboAnnotations);
        widgetBuilder.addLine(m_numberComboAnnotationsDEC);
        widgetBuilder.addLine(m_numberComboAnnotationsINC);

    }

    public void bind(ValidationManager context, Object dataModel)
    {
        final IntegerRangeTestModel dm = (IntegerRangeTestModel) dataModel;

        context.bindValue(m_numberCombo, dm, IntegerRangeTestModel.P_Number);
        m_numberCombo.getViewer().setContentProvider(new ArrayContentProvider());
        m_numberCombo.getViewer().setInput(dm.getNumbersList());

        context.bindValue(m_numberComboAnnotations, dm, IntegerRangeTestModel.P_NumberAnnotated);
        context.bindValue(m_numberComboAnnotationsDEC, dm,
                IntegerRangeTestModel.P_NumberStepAnnotatedDEC);
        context.bindValue(m_numberComboAnnotationsINC, dm,
                IntegerRangeTestModel.P_NumberStepAnnotatedINC);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        mainSection.setState(state, value);

    }

    public void setFocus()
    {
        m_numberComboAnnotations.getSWTControl().setFocus();
    }

    public static void main(String[] args)
    {

        final IntegerRangeTestModel model = new IntegerRangeTestModel();
        final RCPFormPart part = new SandboxRangeSampleFormPart();
        model.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPFormFactory.getInstance().startTestShell("SandboxRangeSampleFormPart", part, model);
    }
}
