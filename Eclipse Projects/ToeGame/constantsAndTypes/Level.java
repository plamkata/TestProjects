/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-13
 * Time: 20:52:35
 * To change this template use File | Settings | File Templates.
 */
package constantsAndTypes;

/** 
 * Enumeration class
 * There are three levels of the game:
 * BEGINNER, INTERMIDIATE and EXPERT.
 *
 * BEGINER means that the Computer will simply teach himself by
 * his errors, wile the Game is running.
 *
 * INTERMEDIATE means that the Computer will have the same feature
 * as BEGINNER, but will also be tought by default, i.e. he would
 * read from a file, containing some default basic Combinations.
 *
 * EXPERT means that the Computer will perform some simple strategy.
 */
public class Level {

    public static final Level BEGINNER = new Level("Beginner");
    public static final Level INTERMIDIATE = new Level("Intermidiate");
    public static final Level EXPERT = new Level("Expert");

    private final String myName; // for debug only

    private Level(String name) {
        myName = name;
    }

    public String toString() {
        return myName;
    }

}
