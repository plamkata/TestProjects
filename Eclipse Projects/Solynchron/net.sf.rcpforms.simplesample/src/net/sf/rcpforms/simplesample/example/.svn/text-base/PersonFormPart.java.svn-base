
package net.sf.rcpforms.simplesample.example;

import java.util.Date;

import net.sf.rcpforms.bindingvalidation.ValidationManager;
import net.sf.rcpforms.bindingvalidation.forms.RCPFormPart;
import net.sf.rcpforms.modeladapter.converter.AbstractModelValidator;
import net.sf.rcpforms.modeladapter.converter.RequiredValidator;
import net.sf.rcpforms.widgetwrapper.builder.GridBuilder;
import net.sf.rcpforms.widgetwrapper.wrapper.EControlState;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPCombo;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPSection;
import net.sf.rcpforms.widgetwrapper.wrapper.RCPText;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PersonFormPart extends RCPFormPart
{

    /**
     * Class DateFutureValidator is an example for a custom validator
     * 
     * @author Remo Loetscher
     */
    public static final class DateFutureValidator extends AbstractModelValidator
    {
        public Object[] getProperties()
        {
            return new String[]{PersonDataModel.P_Birthdate};
        }

        public IStatus validate(Object value)
        {
            PersonDataModel model = (PersonDataModel) value;
            IStatus result = ok();
            if (model.getBirthdate() != null && model.getBirthdate().after(new Date()))
            {
                result = error("Birthdate has to be in the past!");
            }
            return result;
        }
    }

    private RCPSection section;

    private RCPText name;

    private RCPText firstName;

    private RCPText street;

    private RCPText streetNumber;

    private RCPText city;

    private RCPCombo country;

    private RCPText birthdate;

    @Override
    public void bind(ValidationManager bm, Object modelBean)
    {
        bm.bindValue(name, modelBean, PersonDataModel.P_Name);
        bm.bindValue(firstName, modelBean, PersonDataModel.P_FirstName);
        bm.bindValue(street, modelBean, PersonDataModel.P_Street);
        bm.bindValue(streetNumber, modelBean, PersonDataModel.P_StreetNumber);
        bm.bindValue(city, modelBean, PersonDataModel.P_City);
        bm.bindValue(country, modelBean, PersonDataModel.P_Country);
        bm.bindValue(birthdate, modelBean, PersonDataModel.P_Birthdate);

        // add required validator
        bm.addValidator(this, new RequiredValidator(PersonDataModel.P_Name,
                PersonDataModel.P_FirstName, PersonDataModel.P_Birthdate));

        // add date validator
        bm.addValidator(this, new DateFutureValidator());
    }

    @Override
    public void createUI(FormToolkit toolkit, Composite parent)
    {
        // create RCPForm elements
        section = new RCPSection("This title will be invisible", Section.NO_TITLE);
        name = new RCPText("Name: ");
        name.setState(EControlState.MANDATORY, true);
        firstName = new RCPText("Firstname: ");
        firstName.setState(EControlState.MANDATORY, true);
        street = new RCPText("Street/Number: ");
        streetNumber = new RCPText(null);
        city = new RCPText("City: ");
        country = new RCPCombo("Country: ");
        birthdate = new RCPText("Birthdate: ");
        birthdate.setState(EControlState.MANDATORY, true);

        // add elements to container
        GridBuilder formPartBuilder = new GridBuilder(toolkit, parent, 1);
        GridBuilder sectionBuilder = formPartBuilder.addContainer(section, 4);
        // 1st line
        sectionBuilder.addLineGrabAndFill(name, 3);
        // 2nd line
        sectionBuilder.addLine(firstName);
        // 3rd line
        sectionBuilder.add(street);
        sectionBuilder.add(streetNumber);
        sectionBuilder.fillLine();
        // 4th line
        sectionBuilder.addLine(city);
        // 5th line
        sectionBuilder.addLine(country);
        // 6th line
        sectionBuilder.addLine(birthdate, 10);
    }

    @Override
    public void setState(EControlState state, boolean value)
    {
        section.setState(state, value);
    }

}
