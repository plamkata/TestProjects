package net.sf.rcpforms.comparison.rcpforms;

public class PersonDataModel extends JavaBean
{
    public static final String P_Name = "name";

    public static final String P_FirstName = "firstName";

    private String name;

    private String firstName;

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

    public String toString()
    {
        StringBuilder sb = new StringBuilder(this.getClass().getCanonicalName());
        sb.append(", Name: ").append(name).append(" ").append(firstName);
        return sb.toString();
    }
}