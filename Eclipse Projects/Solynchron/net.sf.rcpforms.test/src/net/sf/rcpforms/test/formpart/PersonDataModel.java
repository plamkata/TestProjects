
package net.sf.rcpforms.test.formpart;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.damnhandy.aspects.bean.Observable;

@Observable
public class PersonDataModel
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
        this.name = name;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public Integer getStreetNumber()
    {
        return streetNumber;
    }

    public void setStreetNumber(Integer streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Country getCountry()
    {
        return country;
    }

    public void setCountry(Country country)
    {
        this.country = country;
    }

    public Date getBirthdate()
    {
        return birthdate;
    }

    public void setBirthdate(Date birthdate)
    {
        this.birthdate = birthdate;
    }

    @Override
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