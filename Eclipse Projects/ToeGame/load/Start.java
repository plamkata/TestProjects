package load;

import gameGUI.Main;
import gameGUI.battleField.BattleField;
import gameLogic.Settings;
import gameLogic.Game;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.awt.*;

/** 
 * This class starts loading the settings and the field.
 * And also starts the game and displays the GUI.
 * i.e. it simply loads the hole project. Here is the main() method.
 */
public final class Start {

    private static Settings settings;
    private static BattleField field;
    private static Game game;
    private static Main main;
    private static Point mainLocation = new Point(250, 150);

    public static Thread gameStarter;
    public static Thread guiStarter;

	/**
	 * Called in the main() function in this class.
	 * @throws FileNotFoundException when Game() do so.
	 * @see gameLogic.Game
	 */
    private Start() throws FileNotFoundException {
        // set default settings
        settings = new Settings();
        // create the BattleField
        field = new BattleField(settings.getSkin());
        game = new Game(settings, field);
        // start the new game in it's own thread
        gameStarter = new GameThread();
        gameStarter.start();
        createGUI();
    }

	/**
	 * This method creates comlpetely new game, by the new Settings passed,
	 * and starts the Game in its own thread and the graphical part in another.
	 * @param newSettings - the new Settings to be loaded.
	 * @see gameLogic.Game
	 * @see gameLogic.Settings
	 * @see gameGUI.battleField.BattleField
	 */
    public static void createNewGame(Settings newSettings) {
        try {
            settings = newSettings;
            field = new BattleField(settings.getSkin());
            // create new game
            game = null;// destroy the old game
            game = new Game(settings, field);

            gameStarter = null;// kill the previouse thread
            gameStarter = new GameThread();
            gameStarter.start();

            // keep the previouse location
            main.updateLocation(mainLocation);
            main.dispose();
            // make a new window
            createGUI();
        } catch (FileNotFoundException e) {
            e.printStackTrace();// print some message
        }
    }

	/**
	 * This method continues with the same game
	 * as it clears the field and starts the game again,
	 * when required by the user.
	 */
    public static void continueWithAnotherGame() {
        field.refreshField();

        gameStarter = null;// kill the previouse thread
        gameStarter = new GameRestarterThread();
        gameStarter.start();
        main.repaint();
    }

	/**
	 * The main() method simply makes an instance of this class.
	 * @param args
	 */
	public static void main(String[] args) {
        try {
            new Start();
        } catch (FileNotFoundException e) {
            // this should never happen, since
            // the default settings are loaded
            e.printStackTrace();
        }
	}

    private static void createGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        main = new Main(game, settings, field, mainLocation);
    }

    private static class GameThread extends Thread {
        public GameThread() {
            super();
            this.setName("GameThread");
        }

        public void run() {
            game.start();
        }
    }

    private static class GameRestarterThread extends Thread {
        public GameRestarterThread() {
            super();
            this.setName("GameRestarterThread");
        }

        public void run() {
            game.clear(field);
            game.start();
        }
    }

}
