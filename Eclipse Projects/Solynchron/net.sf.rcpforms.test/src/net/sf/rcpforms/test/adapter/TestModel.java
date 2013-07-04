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

import java.util.Calendar;
import java.util.Date;

import net.sf.rcpforms.tablesupport.tables.ITableSelectable;

import com.damnhandy.aspects.bean.Nested;
import com.damnhandy.aspects.bean.Observable;

/**
 * Data model for Sandbox2, has one field for all supported data types
 * 
 * @author vanmeegenm
 */
@Observable
public class TestModel implements ITableSelectable, ITestModel
{
    public static enum Gender {
        MALE, FEMALE, UNKNOWN
    };

    // needed for generic tables
    public static final String P_Name = "name";

    public static final String P_BirthDate = "birthDate";

    public static final String P_Age = "age";

    public static final String P_OverdrawAccount = "overdrawAccount";

    public static final String P_ChildCount = "childCount";

    public static final String P_AccountBalance = "accountBalance";

    public static final String P_Gender = "gender";

    public static final String P_Address = "address";

    private Gender gender;

    private String name;

    private Date birthDate;

    private Boolean overdrawAccount;

    private Integer childCount;

    private int age;

    private Double accountBalance;

    private Boolean isSelectable = true;

    @Nested
    private AddressModel address;

    public TestModel()
    {
        super();
        setAddress(new AddressModel());
    }

    /**
     * Constructor for TestModel
     */
    public TestModel(String name, Date birthDate, Boolean kontoueberzug, Integer kinderAnzahl,
                     Double amount, Gender geschlecht, boolean isSelectable)
    {
        super();
        setName(name);
        setBirthDate(birthDate);
        setOverdrawAccount(kontoueberzug);
        setChildCount(childCount);
        setAccountBalance(amount);
        setGender(geschlecht);
        setAddress(new AddressModel());
        this.isSelectable = isSelectable;
    }

    /**
     * Constructor for TestModel
     * 
     * @param name
     * @param birthDate
     * @param kontoueberzug
     * @param kinderAnzahl
     * @param amount
     * @param geschlechtCodeId
     */
    public TestModel(String name, Date birthDate, Long waehrungCodeId, boolean kontoueberzug,
                     int kinderAnzahl, double amount, Gender geschlecht, boolean isSelectable)
    {
        this(name, birthDate, kontoueberzug, kinderAnzahl, amount, geschlecht, isSelectable);
    }

    /**
     * Constructor for TestModel
     * 
     * @param string
     * @param i
     */
    public TestModel(String name, int age)
    {
        setName(name);
        setAge(age);
        setAddress(new AddressModel());
    }

    /** puts some useful data on this test model instance */
    public void initTestData()
    {
        setName("Mustermann");
        setAge(55);
        setGender(Gender.MALE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(1980, 11, 24);
        setBirthDate(calendar.getTime());
    }

    /**
     * @return Returns the address.
     */
    public AddressModel getAddress()
    {
        return address;
    }

    /**
     * @param address The address to set.
     */
    public void setAddress(AddressModel value)
    {
        address = value;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender value)
    {
        this.gender = value;
    }

    public Integer getChildCount()
    {
        return childCount;
    }

    public void setChildCount(Integer value)
    {
        this.childCount = value;
    }

    public Double getAccountBalance()
    {
        return accountBalance;
    }

    public void setAccountBalance(Double value)
    {
        this.accountBalance = value;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int newValue)
    {
        this.age = newValue;
    }

    public Boolean getOverdrawAccount()
    {
        return overdrawAccount;
    }

    public void setOverdrawAccount(Boolean value)
    {
        this.overdrawAccount = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String value)
    {
        this.name = value;
    }

    public Date getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(Date value)
    {
        this.birthDate = value;
    }

    public Boolean getIsSelectable()
    {
        return isSelectable;
    }

    public void setIsSelectable(Boolean isSelectable)
    {
        this.isSelectable = isSelectable;
    }

    public String toString()
    {
        return "Name: " + name + ", Birthdate: " + birthDate + ", Gender: " + gender
                + ", Account Overdraw: " + overdrawAccount + ", ChildCount: " + childCount
                + ", AccountBalance: " + accountBalance;
    }

}