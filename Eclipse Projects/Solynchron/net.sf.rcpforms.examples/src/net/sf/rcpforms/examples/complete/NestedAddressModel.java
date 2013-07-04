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

import com.damnhandy.aspects.bean.Observable;

/**
 * data model for sandbox3. Contains ui state info: SeparateKorrespondenzAdresse.
 * 
 * @author vanmeegenm
 */
@Observable
public class NestedAddressModel 
{
    public static final String P_ZipCode = "zipCode";

    public static final String P_City = "city";

    public static final String P_NestedCountry = "country";

    private Integer zipCode;

    private String city;

    private NestedCountryModel m_Country = new NestedCountryModel();

    public String getCity()
    {
        return (String) city;
    }

    public void setCity(String value)
    {
        city = value;
    }

    public Integer getZipCode()
    {
        return (Integer) zipCode;
    }

    public void setZipCode(Integer value)
    {
        zipCode = value;
    }

    public NestedCountryModel getCountry()
    {
        return (NestedCountryModel) m_Country;
    }

    public void setCountry(NestedCountryModel value)
    {
        m_Country = value;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("NestedAddressModel[");
        sb.append(getCountry().toString());
        sb.append(", City: ");
        sb.append(getCity());
        sb.append(", ZipCode: ");
        sb.append(getZipCode());
        sb.append("]");

        return sb.toString();
    }

}
