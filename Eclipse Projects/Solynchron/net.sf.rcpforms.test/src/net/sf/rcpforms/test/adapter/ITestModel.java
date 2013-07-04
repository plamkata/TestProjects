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
// Created on 21.05.2009

package net.sf.rcpforms.test.adapter;

import java.util.Date;

/**
 * test model abstraction to be able to reuse test code for emf and beans
 * 
 * @author Marco van Meegen
 */
public interface ITestModel
{
    void setIsSelectable(Boolean isSelectable);

    Boolean getIsSelectable();

    void setBirthDate(Date value);

    Date getBirthDate();

    void setName(String value);

    String getName();

    void setOverdrawAccount(Boolean value);

    Boolean getOverdrawAccount();

    void setAge(int newValue);

    int getAge();

    void setAccountBalance(Double value);

    Double getAccountBalance();

    void setChildCount(Integer value);

    Integer getChildCount();
}
