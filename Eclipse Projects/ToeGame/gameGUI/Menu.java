// Created on 2005-5-16
// @author Anislav

package gameGUI;

import gameLogic.Settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import load.Start;

public class Menu extends JMenuBar {
	private static final long serialVersionUID = -3155205988636863502L;
	
	private JMenu menuGame;
	private JMenuItem itemGameNew;
	private JMenuItem itemGameOptions;
	private JMenuItem itemGameExit;

	private JMenu menuHelp;
	private JMenuItem itemHelpContents;
	private JMenuItem itemHelpAbout;

    public Menu(Main mainFrame) {// Create the menu bar.
        super();
        JDialog.setDefaultLookAndFeelDecorated(true);
		// Build the game menu.
		menuGame = new JMenu("Game");
		menuGame.setMnemonic(KeyEvent.VK_G);
		add(menuGame);
        addItemsToGameMenu(mainFrame);

		// Build the help meniu.
		menuHelp = new JMenu("Help");
		menuHelp.setMnemonic(KeyEvent.VK_H);
		add(menuHelp);
        addItemsToHelpMenu(mainFrame);
    }

    private void addItemsToHelpMenu(final Main mainFrame) {
        itemHelpContents = new JMenuItem("Contents");
        itemHelpContents.setMnemonic(KeyEvent.VK_C);
        itemHelpContents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HelpContentsDialog helpContents = new HelpContentsDialog();
                helpContents.createAndShowGUI(mainFrame);
            }
        });
        menuHelp.add(itemHelpContents);
        menuHelp.addSeparator();
        itemHelpAbout = new JMenuItem("About");
        itemHelpAbout.setMnemonic(KeyEvent.VK_A);
        itemHelpAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                HelpAboutDialog helpAbout = new HelpAboutDialog();
                helpAbout.createAndShowGUI(mainFrame);
            }
        });
        menuHelp.add(itemHelpAbout);
    }

    private void addItemsToGameMenu(final Main mainFrame) {
        itemGameNew = new JMenuItem("New");
        itemGameNew.setMnemonic(KeyEvent.VK_N);
        itemGameNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // create default settings
                Settings settings = new Settings();
                // set the settings in the Main frame
                mainFrame.setSettings(settings);
                // set the settings in Start and start
                // new game with the new settings
                Start.createNewGame(settings);
            }
        });
        menuGame.add(itemGameNew);
        menuGame.addSeparator();
        itemGameOptions = new JMenuItem("Option");
        itemGameOptions.setMnemonic(KeyEvent.VK_O);
        itemGameOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GameOptionsDialog(mainFrame);
            }
        });
        menuGame.add(itemGameOptions);
        menuGame.addSeparator();
        itemGameExit = new JMenuItem("Exit");
        itemGameExit.setMnemonic(KeyEvent.VK_E);
        itemGameExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        menuGame.add(itemGameExit);
    }

}
