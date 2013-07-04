package gameLogic.players;

import constantsAndTypes.Name;
import constantsAndTypes.StateOfSector;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;

/**
 * This class is used in Game to simulate
 * one of the players - the User.
 *
 * Our abstract user (any real object of type User) must be connected
 * to the real User by the gameGUI. This is realized by the interface Speaker.
 *
 * Interface Speaker is internal for this class and represents the
 * functionality needed to implement the play() method(since User
 * is also a Player).
 *
 * This is "The Gang of Four" pattern in acction.
 */
public class User extends Player {

    /**
     * The speaker argument of type BattleField passed to the constructor
     * is the GUIcomponent, wich will "speak" to our abstract user.
     *
     * The GUIcomponent is able to find out where the real User have
     * played, therefore the GUIcomponent has the information that we
     * need(it has something to "speak about").
     *
     * Since interface BattleField would be implemented by the GUIcomponent
     * and since interface BattleField contains the functionality of Speaker,
     * hence the GUIcomponent will provide the functionality needed to
     * implement the play() mehtod in class User.
     *
     * Everithing is displayed at the BattleField including the moves of
     * the Computer.
     */
    public User(Weapon weapon, Name name, BattleField speaker) {
        super(weapon, name, speaker);
    }

    /**
     * Since the BattleField is to be implemented in the gameGUI,
     * the interface Speaker will be also implemented at the
     * same time(since BattleField extends Listener).
     *
     * Note that:
     *      interface Player.BattleField extends
     *      interface User.Speaker,
     *  while
     *      class Player is extended by
     *      class User.
     * This is reversed inheritance with internal interfaces.
     * Speaker is protected here with the purpose to be
     * extended by BattleField up there in Player.
     */
    protected interface Speaker{
        // indexes of field, choosen by the user
        void openUserConnection(Player usher);
        boolean provideUserHasPlayed();
        int provideUserShotRowIndex();
        int provideUserShotColIndex();
        void closeUserConnection();
    }

    /**
	 * This interface simply exports the weapon of the user.
	 */
    protected interface Listener {
        void addUserWeapon(Weapon userWeapon);
    }

    public synchronized void play(Combination/*out*/ _field) {
        Speaker speaker = (Speaker)builder;
        Listener listener = (Listener)builder;
        listener.addUserWeapon(weapon);
        if (speaker == null || listener == null) return;

        try {
            speaker.openUserConnection(this);
            while (!speaker.provideUserHasPlayed()) {
                wait();
            }
            // determine where exactly user has played
            int colIndex = speaker.provideUserShotColIndex();
            int rowIndex = speaker.provideUserShotRowIndex();
            // make usher's move and fire his weapon
            if(weapon == Weapon.XX){
                _field.changeStateAt(rowIndex, colIndex, StateOfSector.XX);
            } else {
                _field.changeStateAt(rowIndex, colIndex, StateOfSector.OO);
            }
            speaker.closeUserConnection();
        } catch (InterruptedException e) {
        }
    }

}
