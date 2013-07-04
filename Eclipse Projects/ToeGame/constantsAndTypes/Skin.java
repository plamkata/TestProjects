/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-26
 * Time: 22:00:48
 * To change this template use File | Settings | File Templates.
 */
package constantsAndTypes;

/**
 * Enumeration type.
 * There is a field of this type in gameLogic.Settings and it is
 * used when graphics are cerated - to know which graphics exactly
 * should be loaded.
 */
public class Skin {

    public static final Skin SIMPLE = new Skin("Simple");
    public static final Skin PEN = new Skin("Pen");
    public static final Skin BLIND = new Skin("Blind");
    public static final Skin DYNAMIC = new Skin("Dynamic");

    private final String myName; // for debug only

    private Skin(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
