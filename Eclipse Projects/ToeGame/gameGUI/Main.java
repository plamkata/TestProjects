// Created on 2005-5-16
//@author Anislav

package gameGUI;

import gameLogic.Settings;
import gameLogic.Game;

import javax.swing.*;

import gameGUI.battleField.BattleField;

import java.awt.*;


// Singleton - the main window
public class Main extends JFrame {
	private static final long serialVersionUID = 4195830225512706230L;
	
	private Settings settings;

    public Main(Game game, Settings set, BattleField field, Point mainLocation) {
        super("Toe Game");
        settings = set;

        setJMenuBar(new Menu(this));
        setContentPane(new Body(game, field));
        // Add the main panel in the Frame

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(mainLocation);
        setSize(275, 425);
        setResizable(false);
        setVisible(true);
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void updateLocation(Point _location) {
        _location.setLocation(getX(), getY());
    }

}
