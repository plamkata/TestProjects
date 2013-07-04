
package net.sf.rcpforms.examples.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.examples.complete.TestModel;
import net.sf.rcpforms.form.RCPForm;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.customwidgets.RCPDatePicker;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPRadioGroup;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ExampleFormPart extends RCPFormPart
{
    private RCPRadioGroup radioGroup = new RCPRadioGroup("Gender again:", false);

    private RCPText name = new RCPText("Name:");

    private RCPText age = new RCPText("Age:");

    private RCPCombo genderCombo = new RCPCombo("Gender:");

    private RCPDatePicker birthdate = new RCPDatePicker("Birthdate:");

    public void createUI(FormToolkit formToolkit, Composite parent)
    {

        final RCPSection mainSection = new RCPSection("RCPForms Example Widgets and Binding");
        // create different widgets and sections
        GridBuilder sectionBuilder = new GridBuilder(formToolkit, parent, 2).addContainer(
                mainSection, 3);
        sectionBuilder.addLine(name, 30);
        sectionBuilder.addLine(age, 3);
        sectionBuilder.addLine(genderCombo);
        sectionBuilder.addLine(birthdate);
        sectionBuilder.fill(1).addLine(radioGroup);
        // create buttons for values using label provider to create label
        radioGroup.createRadioButtons(TestModel.Gender.values(), new LabelProvider());
    }

    public void bind(ValidationManager bm, Object modelBean)
    {
        bm.bindValue(name, modelBean, TestModel.P_Name);
        bm.bindValue(age, modelBean, TestModel.P_Age);
        bm.bindValue(genderCombo, modelBean, TestModel.P_Gender);
        bm.bindValue(birthdate, modelBean, TestModel.P_BirthDate);
        bm.bindValue(radioGroup, modelBean, TestModel.P_Gender);
    }

    public static void main(String[] args)
    {

        final TestModel model = new TestModel();
        final ExampleFormPart part = new ExampleFormPart();
        model.addPropertyChangeListener(new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                System.out.println("Model changed: " + model);
            }
        });
        RCPForm form = new RCPForm("Quickstart Example", part);
        form.setInput(new Object[]{model});
        form.startTestShell();
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        // not needed here
    }
}
