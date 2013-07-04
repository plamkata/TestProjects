
package net.sf.rcpforms.simplesample.example;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonDataModel extends JavaBean
{

    public static final String P_Name = "name";

    public static final String P_FirstName = "firstName";

    public static final String P_Street = "street";

    public static final String P_StreetNumber = "streetNumber";

    public static final String P_City = "city";

    public static final String P_Country = "country";

    public static final String P_Birthdate = "birthdate";

    private String name;

    private String firstName;

    private String street;

    private Integer streetNumber;

    private String city;

    private Country country;

    private Date birthdate;

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy");

    public PersonDataModel()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        Object oldValue = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_Name, oldValue, name);
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        Object oldValue = this.firstName;
        this.firstName = firstName;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_FirstName, oldValue, firstName);
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        Object oldValue = this.street;
        this.street = street;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_Street, oldValue, street);
    }

    public Integer getStreetNumber()
    {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber)
    {
        Object oldValue = this.streetNumber;
        this.streetNumber = streetNumber;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_StreetNumber, oldValue,
                streetNumber);
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        Object oldValue = this.city;
        this.city = city;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_City, oldValue, city);
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        Object oldValue = this.country;
        this.country = country;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_Country, oldValue, country);
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(Date birthdate)
    {
        Object oldValue = this.birthdate;
        this.birthdate = birthdate;
        propertyChangeSupport.firePropertyChange(PersonDataModel.P_Birthdate, oldValue, birthdate);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(this.getClass().getCanonicalName());
        sb.append(", Name: ").append(name).append(" ").append(firstName).append(" - ").append(
                birthdate == null ? "not set" : dateFormatter.format(birthdate)).append(", ");
        sb.append("Street: ").append(street).append(" ").append(streetNumber).append(", ");
        sb.append("Street: ").append(street).append(" ").append(streetNumber).append(", ");
        sb.append("City: ").append(city).append(", ");
        sb.append("Country: ").append(country).append(", ");
        return sb.toString();
    }
}