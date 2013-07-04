package gameLogic.players;

import constantsAndTypes.Name;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;

/**
 * class Player represents both players -
 * the Computer and the User
 */
public abstract class Player {

    /**
     * Those are the weapon and the name of the Player.
     */
    protected Weapon weapon;
    protected Name name;
    /**
     * This field builder is the GUIcomponent, which will
     * represent the battle field, where the game will take
     * place.
     */
    protected BattleField builder;

    /**
     * Two players are created in Game's constructor
     * by the choosen settings of the game.
     */
    Player(Weapon weapon, Name name, BattleField GUIcomponent) {
        this.weapon = weapon;
        this.name = name;
        this.builder = GUIcomponent;
    }

    /**
     * This interface simply gathers the functionality of both interfaces
     * Computer.Listener and User.Speaker, and passes them to interface
     * Game.BattleField(it is somewhat a focus redirecting functionality).
     * The interface is to be extended by some other internal interface in
     * class Game. (The GUIcomponent is given to Game's constructor.)
     *
     * Helps for building the connection between our abstract players
     * and the real players, i.e. the real Usher and Computer.
     *
     * This connection is acctualy realized in the gameGUI, that's why
     * BattleField is a GUIcomponent(it is to be implemented in the gameGUI).
     *
     * Actually it makes sence that a player should know, wich exactly
     * is the BattleField he is playing at. (This makes sence if we have
     * multiple battle fields, and multiple players, but since this is
     * not the case I won't go further.)
     */
    public interface BattleField 
            extends Computer.Listener, User.Speaker, User.Listener {}

    /**
     * The method play() makes the general move of a player.
     * The method takes the field as an out parameter
     * so that when the player determines its
     * move he fires with his weapon at the field
     */ 
    public abstract void play(Combination/*out*/ _field);

    public Name getName() {
        return name;
    }

    public void prepareComputer(BattleField field) {
        builder = field;
    }

    public synchronized void resumeUsher() {
        User.Speaker speaker = (User.Speaker)builder;
        if (speaker.provideUserHasPlayed()) notify();
    }

}
