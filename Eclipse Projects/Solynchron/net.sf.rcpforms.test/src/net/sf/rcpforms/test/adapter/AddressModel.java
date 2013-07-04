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

package net.sf.rcpforms.test.adapter;

import java.util.Date;

import com.damnhandy.aspects.bean.Observable;

@Observable
public class AddressModel
{
    public static final String P_ZipCode = "zipCode";

    public static final String P_ValidFrom = "validFrom";

    public static final String P_ValidTo = "validTo";

    public static final String P_DifferentPostAddress = "differentPostAddress";

    public static final String P_Street = "street";

    public static final String P_HouseNumber = "houseNumber";

    private Integer zipCode;

    private Date validFrom;

    private Date validTo;

    private boolean differentPostAddress;

    private String street;

    private Integer houseNumber;
    
    public AddressModel() {}

    public Integer getHouseNumber()
    {
        return houseNumber;
    }

    public void setHouseNumber(Integer value)
    {
        houseNumber = value;
    }

    public Date getValidTo()
    {
        return validTo;
    }

    public void setValidTo(Date value)
    {
        validTo = value;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String value)
    {
        street = value;
    }

    public boolean getDifferentPostAddress()
    {
        return differentPostAddress;
    }

    public void setDifferentPostAddress(boolean value)
    {
        differentPostAddress = value;
    }

    public Date getValidFrom()
    {
        return validFrom;
    }

    public void setValidFrom(Date value)
    {
        validFrom = value;
    }

    public Integer getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(Integer value)
    {
        zipCode = value;
    }
}
