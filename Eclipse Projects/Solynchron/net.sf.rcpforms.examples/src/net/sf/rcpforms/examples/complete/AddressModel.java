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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.rcpforms.modeladapter.converter.IModelValidator;
import net.sf.rcpforms.modeladapter.converter.MethodValidator;
import net.sf.rcpforms.widgetwrapper.Activator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.damnhandy.aspects.bean.Nested;
import com.damnhandy.aspects.bean.Observable;

/**
 * data model for sandbox3. Contains ui state info: SeparateKorrespondenzAdresse. is nested element.
 * TODO: cleanup duplicate AddressModel in example and test
 * 
 * @author vanmeegenm
 */
@Observable
public class AddressModel
{
    public static final String P_NestedAddress = "address";

    public static final String P_ValidFrom = "validFrom";

    public static final String P_ValidTo = "validTo";

    public static final String P_DifferentPostAddress = "differentPostAddress";

    public static final String P_Street = "street";

    public static final String P_HouseNumber = "houseNumber";

    @Nested
    private NestedAddressModel address = new NestedAddressModel();

    private Date validFrom;

    private Date validTo;

    private Boolean differentPostAddress;

    private String street;

    private Integer houseNumber;

    public Integer getHouseNumber()
    {
        return (Integer) houseNumber;
    }

    public void setHouseNumber(Integer value)
    {
        houseNumber = value;
    }

    public Date getValidTo()
    {
        return (Date) validTo;
    }

    public void setValidTo(Date value)
    {
        validTo = value;
    }

    public String getStreet()
    {
        return (String) street;
    }

    public void setStreet(String value)
    {
        street = value;
    }

    public Boolean getDifferentPostAddress()
    {
        return (Boolean) differentPostAddress;
    }

    public void setDifferentPostAddress(Boolean value)
    {
        differentPostAddress = value;
    }

    public Date getValidFrom()
    {
        return (Date) validFrom;
    }

    public void setValidFrom(Date value)
    {
        validFrom = value;
    }

    public NestedAddressModel getAddress()
    {
        return (NestedAddressModel) address;
    }

    public void setAddress(NestedAddressModel value)
    {
        address = value;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AddressModel[");
        sb.append(getAddress().toString());
        sb.append(", DiffPostAdr: ");
        sb.append(getDifferentPostAddress());
        sb.append(", House#: ");
        sb.append(getHouseNumber());
        sb.append(", Street: ");
        sb.append(getStreet());
        sb.append(", validFrom: ");
        sb.append(getValidFrom());
        sb.append(", validTo: ");
        sb.append(getValidTo());
        sb.append("]");

        return sb.toString();
    }

    /**
     * this validator registers the validation method with the framework. MAKE SURE TO PASS ALL
     * FIELDS THE VALIDATOR DEPENDS ON IN THE CONSTRUCTOR ! The constructor will register with the
     * Data Model so the validator is known. If you have a validator which might be useful in other
     * contexts too (like the example below), please subclass AbstractValidator and add it to the
     * framework in the same package the method validator is.
     */
    IModelValidator validator = new MethodValidator(this, P_ValidFrom, P_ValidTo)
    {
        public IStatus validate(Object object)
        {
            IStatus result = Status.OK_STATUS;
            // attention: make sure that validation is ok if required fields are not filled !
            // this is to avoid user by heaps of error messages if an empty form pops up.
            if (getValidFrom() != null && getValidTo() != null)
            {
                // make sure time fraction does not spoil the result
                boolean isValid = truncateTime(getValidFrom())
                        .compareTo(truncateTime(getValidTo())) <= 0;
                if (!isValid)
                {
                    result = new Status(Status.ERROR, Activator.PLUGIN_ID,
                            "Datum von muss vor Datum bis liegen oder gleich sein.");
                }
            }
            return result;
        }

        /** set time part of date to 0 */
        private Date truncateTime(Date date)
        {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            Date roundedFrom = calendar.getTime();
            return roundedFrom;
        }
    };

    /**
     * this validator registers the validation method with the framework; validation failures are
     * not shown as error messages. MAKE SURE TO PASS ALL FIELDS THE VALIDATOR DEPENDS ON IN THE
     * CONSTRUCTOR ! The constructor will register with the Data Model so the validator is known.
     */
    IModelValidator validator1 = new MethodValidator(P_DifferentPostAddress, P_HouseNumber)
    {
        public IStatus validate(Object object)
        {
            IStatus result = Status.OK_STATUS;
            // attention: make sure that validation is ok if required fields are not filled !
            // this is to avoid user by heaps of error messages if an empty form pops up.
            if (Boolean.TRUE == getDifferentPostAddress() && getHouseNumber() == null)
            {
                result = new Status(Status.ERROR, Activator.PLUGIN_ID,
                        "HouseNumber must not be empty.");
            }
            return result;
        }
    };

}