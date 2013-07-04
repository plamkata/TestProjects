
package net.sf.rcpforms.test.formpart;

public enum Country {
    FRANCE(250, "France", "FRA", "FR"), GERMANY(276, "Germany", "DEU", "DE"), LIECHTENSTEIN(438,
            "Liechtenstein", "LIE", "LI"), ITALY(380, "Italy", "ITA", "IT"), SWITZERLAND(756,
            "Switzerland", "CHE", "CH");

    private final int id;

    private final String name, alpha2, alpha3;

    Country(int countryId, String name, String alpha2, String alpha3)
    {
        this.id = countryId;
        this.name = name;
        this.alpha2 = alpha2;
        this.alpha3 = alpha3;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getAlpha2()
    {
        return alpha2;
    }

    public String getAlpha3()
    {
        return alpha3;
    }

    public String toString()
    {
        return alpha2 + " - " + this.name;
    }
}