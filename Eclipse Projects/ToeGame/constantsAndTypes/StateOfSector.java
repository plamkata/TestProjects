/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-13
 * Time: 23:39:28
 * To change this template use File | Settings | File Templates.
 */
package constantsAndTypes;

/**
 * Enumeration type.
 * These are the States of a particular sector from the battle field
 * used in gameGUI.battleField.BattleField and in
 * gameLogic.combination.Combination.
 * @see gameGUI.battleField.BattleField
 * @see gameLogic.combination.Combination
 */
public class StateOfSector {

    public static final StateOfSector BLANK = new StateOfSector("BLANK");
    public static final StateOfSector XX = new StateOfSector("XX");
    public static final StateOfSector OO = new StateOfSector("OO");

    private final String myName; // for debug only

    private StateOfSector(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }
}
