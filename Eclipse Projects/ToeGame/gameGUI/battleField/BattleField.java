/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-17
 * Time: 15:58:06
 * To change this template use File | Settings | File Templates.
 */
package gameGUI.battleField;

import constantsAndTypes.Weapon;
import constantsAndTypes.Skin;
import gameLogic.Game;
import gameLogic.players.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import gameGUI.Body;

/**
 * The panel added in the MainFrame and updated in gameLogic.
 * @see gameGUI.Main
 * @see gameLogic
 */
public class BattleField extends JPanel
        implements Game.BattleField {
	private static final long serialVersionUID = 958663328579997059L;
	
	private int columns = 3; // columns = number of cells wide
    private int rows = 3; // rows = number of cells high

    private Player user;
    private boolean userHasPlayed = false;
    private int userShotRowIndex = -1;
    private int userShotColIndex = -1;

    public BattleField(Skin skin) {
        super();
        columns = 3;
        rows = 3;

        userHasPlayed = false;
        userShotRowIndex = -1;
        userShotColIndex = -1;

        setSize(200, 200);
        setLayout(new GridLayout(columns, rows));

        for (int i = 0; i < columns * rows; i++) {
            Sector currentSector = new Sector(i, skin);
            currentSector.setSize(getWidth()/columns, getHeight()/rows);
            currentSector.addMouseListener(new MouseListenser());
            add(currentSector, i);
        }
        validate();
        setVisible(true);
    }

    /**
	 * Computer's Listener method.
	 * @param rowIndex
	 * @param colIndex
	 * @param computerWeapon
	 */
    public void addComputerShotAt(
            int rowIndex, int colIndex, Weapon computerWeapon) {
        // find the destination sector
        Sector destSector =
                (Sector) getComponent(3*rowIndex + colIndex);

        // fire the weapon at the destination sector
        destSector.fireWith(computerWeapon);
        Body.updateResultFields();
    }

    public void openUserConnection(Player user) {
        this.user = user;
        // user connection is opened,
        // since the actionListeners are added,
        // but data should be also initialized
        userHasPlayed = false;
        userShotRowIndex = -1;
        userShotColIndex = -1;
    }

    /**
	 * User's Speaker method.
	 * @return true if user has played, false otherwise.
	 * @see gameLogic.players.User
	 */
    public boolean provideUserHasPlayed() {
        return userHasPlayed;
    }

    /**
	 * User's Speaker method.
	 * @return the row index of user's choise (the real user).
	 * @see gameLogic.players.User
	 */
    public int provideUserShotRowIndex() {
        return userShotRowIndex;
    }

    /**
	 * User's Speaker method.
	 * @return the column index of user's choise (the real user).
	 * @see gameLogic.players.User
	 */
    public int provideUserShotColIndex() {
        return userShotColIndex;
    }

    public void closeUserConnection() {
        // user connection is closed, since the ActionListeners
        // are self-destructing, but we can also keep the data
        // initialized before and after our Speaker "speaks up"
        Body.updateResultFields();
        userHasPlayed = false;
        userShotRowIndex = -1;
        userShotColIndex = -1;
    }

    // User's Listener method
    public void addUserWeapon(Weapon userWeapon) {
        int countSectors = this.getComponentCount();
        for (int i = 0; i < countSectors; i++) {
            ((Sector)getComponent(i)).setUserWeapon(userWeapon);
        }
    }

    public void disableField() {
        int countSectors = this.getComponentCount();
        for (int i = 0; i < countSectors; i++) {
            ((Sector)getComponent(i)).removeListeners();
        }
    }

    public void refreshField() {
        userHasPlayed = false;
        userShotRowIndex = -1;
        userShotColIndex = -1;

        for (int i = 0; i < columns * rows; i++) {
            Sector currentSector = (Sector)getComponent(i);
            currentSector.refresh();
            currentSector.addMouseListener(new MouseListenser());
        }
        validate();
        setVisible(true);
    }

    private class MouseListenser extends MouseAdapter {

        // to be performed only the first time it occurs
        public void mousePressed(MouseEvent e){
            userHasPlayed = true;
            // get the target Sector of the mouse event -
            // this is the target sector of the User
            Sector target = (Sector) e.getSource();
            userShotRowIndex = target.getId()/3;
            userShotColIndex = target.getId()%3;
            // remove the current MouseListener,
            // when the MouseEvent is performed.
            ((Sector)e.getSource()).removeMouseListener(this);
            user.resumeUsher();
        }
    }

}
