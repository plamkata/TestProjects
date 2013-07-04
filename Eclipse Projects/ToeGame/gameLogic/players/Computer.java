package gameLogic.players;

import constantsAndTypes.Name;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;

/**
 * This class is used in Game to simulate
 * one of the players - the Computer.
 *
 * Our abstract computers (any real object of type Computer) must be connected
 * to the gameGUI. This is realized by the interface Listener.
 *
 * Interface Listener is internal for this class and represents the
 * functionality needed to implement the play() method(since Computer
 * is also a Player).
 *
 * This is "The Gang of Four" pattern in acction.
 */
public abstract class Computer extends Player {

    /**
     * The listener argument of type BattleField passed to the constructor
     * is the GUIcomponent, wich will "listen" to our abstract computers,
     * i.e. listen to his moves and visualize them on screen.
     * Everithing is displayed at the BattleField including the moves of
     * the Computer.
     */
    public Computer(Weapon weapon, Name name, BattleField listener) {
        super(weapon, name, listener);
    }

    /**
     * Since the BattleField is to be implemented in the gameGUI,
     * the interface Listener will be also implemented at the
     * same time(since BattleField extends Listener).
     *
     * Note that:
     *      interface Player.BattleField extends
     *      interface Computer.Listener,
     *  while
     *      class Player is extended by
     *      class Computer.
     * This is reversed inheritance with internal interfaces.
     *
     * Listener is protected here with the purpose to be
     * extended by BattleField up there in Player, but also
     * to be used for downcast in the ComputerBeginner,
     * ComputerIntermidiate, and ComputerExpert when the
     * move is determined.
     */
    protected interface Listener {
        void addComputerShotAt(
                int rowIndex,
                int colIndex,
                Weapon computerWeapon
                );
    }

    public abstract void play(Combination/*out*/ _field);

    // prepares the computers for a new game
    // the method takes the winner as a parameter
    // to know how to play the next game
    public abstract void reset(Player winner, Combination field);

}
