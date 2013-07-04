
package net.sf.rcpforms.comparison.rcpforms;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.modeladapter.converter.RequiredValidator;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PersonFormPart extends RCPFormPart
{

    private RCPSection section;

    private RCPText name;

    private RCPText firstName;

    @Override
    public void bind(ValidationManager bm, Object modelBean)
    {
    	// bind values to the model bean
        bm.bindValue(name, modelBean, PersonDataModel.P_Name);
        bm.bindValue(firstName, modelBean, PersonDataModel.P_FirstName);
        
        // add required validator
        bm.addValidator(this, new RequiredValidator(PersonDataModel.P_Name,
                PersonDataModel.P_FirstName));
    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create RCPForm elements
        section = new RCPSection("This title will be invisible", Section.NO_TITLE);
        name = new RCPText("First Name: ");
        name.setState(EControlState.MANDATORY, true);
        firstName = new RCPText("Last Firstname: ");
        firstName.setState(EControlState.MANDATORY, true);

        // add elements to container
        GridBuilder formPartBuilder = new GridBuilder(toolkit, parent, 2);
        // 1st line
        formPartBuilder.addLineGrabAndFill(name, 1);
        // 2nd line
        formPartBuilder.addLineGrabAndFill(firstName, 1);
        
        toolkit.paintBordersFor(parent);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        section.setState(state, value);
    }

}
