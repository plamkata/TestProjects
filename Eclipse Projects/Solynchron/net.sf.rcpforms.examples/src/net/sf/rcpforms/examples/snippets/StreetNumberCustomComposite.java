
package net.sf.rcpforms.examples.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.examples.complete.AddressModel;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPComposite;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSimpleText;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * example how to create a self contained composite which can be reused by clients as is, without
 * the ability to change layout of children.
 * 
 * @author Marco van Meegen, Remo Loetscher
 */
public class StreetNumberCustomComposite extends RCPComposite
{
    private RCPText m_street = new RCPText("Street/Nr:");

    private RCPSimpleText m_number = new RCPSimpleText();

    /** override createUI to create children and set layout */
    @Override
    public void createUI(FormToolkit formToolkit)
    {
        // create internal widgets
        m_street = new RCPText(getLabel(), getStyle());
        m_street.setLayoutData(new GridData());
        internalAdd(m_street);
        m_number = new RCPSimpleText();
        m_number.setLayoutData(new GridData());
        internalAdd(m_number);

        super.createUI(formToolkit);
        this.setLayout(new GridLayout(2, false));

    }

    @Override
    public boolean isExtensible()
    {
        return false; // make sure childrens swt controls will be created automatically
    }

    public void bind(ValidationManager bm, Object modelBean)
    {
        bm.bindValue(m_street, modelBean, TestModel.P_Address + "." + AddressModel.P_Street);
        bm.bindValue(m_number, modelBean, TestModel.P_Address + "." + AddressModel.P_HouseNumber);
    }

    public static void main(String[] args)
    {

        final TestModel model = new TestModel();
        model.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPFormPart part = new RCPFormPart()
        {
            @Override
            public void createUI(FormToolkit toolkit, Composite parent)
            {
                // create RCPForm elements
                RCPSection section = new RCPSection("StreetNumberComposite Section");
                // add elements to container
                GridBuilder formPartBuilder = new GridBuilder(toolkit, parent, 2);
                GridBuilder sectionBuilder = formPartBuilder.addContainer(section, 4);
                // 1st line
                sectionBuilder.addLine(new StreetNumberCustomComposite());
                sectionBuilder.addLine(new StreetNumberCustomComposite());
                sectionBuilder.addLine(new StreetNumberCustomComposite());
            }

            @Override
            public void setState(EControlState state, boolean value)
            {
            }

            @Override
            public void bind(ValidationManager bm, Object modelBean)
            {
                // TODO Auto-generated method stub

            }

        };

        RCPForm form = new RCPForm("StreetNumber Example", part);
        form.setInput(new Object[]{model});
        form.startTestShell();
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        // not needed here
    }
}
