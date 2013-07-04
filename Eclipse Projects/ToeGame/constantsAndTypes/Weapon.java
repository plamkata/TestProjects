/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-13
 * Time: 21:01:52
 * To change this template use File | Settings | File Templates.
 */
package constantsAndTypes;

/**
 * Enumeration type.
 * There are two weapons - the XX and the OO.
 */
public class Weapon {

    public static final Weapon XX = new Weapon("XX");
    public static final Weapon OO = new Weapon("OO");

    private final String myName;

    private Weapon(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }

}
