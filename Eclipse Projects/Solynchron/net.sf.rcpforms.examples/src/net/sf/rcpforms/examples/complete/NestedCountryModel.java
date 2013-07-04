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
 * data model for sandbox3. Contains ui state info: SeparateKorrespondenzAdresse. is nested element.
 * 
 * @author Remo Loetscher
 */
@Observable
public class NestedCountryModel 
{
    public static final String P_Country = "country";

    private String m_Country;

    public String getCountry()
    {
        return (String) m_Country;
    }

    public void setCountry(String value)
    {
        m_Country = value;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("NestedCountryModel[");
        sb.append("Country: ");
        sb.append(getCountry());
        sb.append("]");
        return sb.toString();
    }

}