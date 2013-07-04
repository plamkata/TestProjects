// Created on 2005-5-17
//@author Anislav
package gameGUI;

import constantsAndTypes.Level;
import constantsAndTypes.Weapon;
import constantsAndTypes.Skin;
import gameLogic.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import load.Start;

/**
 * GameOptionsDialog is a singleton.
 * Only one instance of this class should be available, because
 * its field settings is initialized only here and we want to
 * encapsualte it. Actually, to initialize the settings we need
 * Game's settings, but since this will break our design's purpouse
 * tawords modularity we initialize it here, only the first time
 * with default settings, and we change the field each time the button
 * OK is pressed (see the ActionListener at the end). This way we
 * achieve that the first time we export() we export the default
 * settings and every following time we export() the updated settings.
 */
public class GameOptionsDialog extends JDialog
	implements
        gameLogic.Settings.Exporter,
		gameLogic.Settings.Importer {
	private static final long serialVersionUID = -3459871322353059534L;

	private Main mainFrame; 

	private JTextField nameField;
	
	private JCheckBox[] checkBoxLevelButtons;
	private final int numLevelButtons = 3;
	private ButtonGroup levelGroup;

    private JCheckBox[] checkBoxSkinButtons;
    private final int numSkinButtons = 4;
    private ButtonGroup skinGroup;

	private JRadioButton[] radioOptionsButtons;
	private final int numOptionsButtons = 2;
	private ButtonGroup optionsGroup;
	
	private JButton buttonOK;
	private JButton buttonCancel;
	
	/**
	 * The data of the Dialog.
	 */
	private Level level;
    private Skin skin;
	private String userName;
	private Weapon userWeapon;
	
	public GameOptionsDialog(Main mainFrame) {
		super(mainFrame, "Options", true);
        this.mainFrame = mainFrame;
        // update the information here
        mainFrame.getSettings().export(this);
		//set up the GameOptionsDialog.
		setContentPane(createContentPane());

		//Display the window.
		setSize(220, 280);
		setLocation(mainFrame.getX() + 40, mainFrame.getY() + 40);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Implements method from gameLogic.Settings.Exporter.
	 */
	public void addLevel(Level gameLevel) {
 		level = gameLevel;
	}

	/**
	 * Implements method from gameLogic.Settings.Exporter.
	 */
    public void addSkin(Skin skin) {
        this.skin = skin;
    }

	/**
	 * Implements method from gameLogic.Settings.Exporter.
	 */
	public void addUserName(String usersName) {
		userName = usersName;
	}

	/**
	 * Implements method from gameLogic.Settings.Exporter.
	 */
	public void addUserWeapon(Weapon usersWeapon) {
		userWeapon = usersWeapon;
	}
	
	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
	public Level provideLevel(){
	 	return level;
	 }

	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
    public Skin provideSkin() {
        return skin;
    }

	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
	public String provideUserName(){
     	return userName;
     }

	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
	public Weapon provideUserWeapon(){
     	return userWeapon;
     }

	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
	public void open() {
	 	userName = nameField.getText();

	 	String selectedWeapon =
                 optionsGroup.getSelection().getActionCommand();
	 	if(selectedWeapon.equals(Weapon.XX.toString())) {
	 		userWeapon = Weapon.XX;
	 	} else {
	 		userWeapon = Weapon.OO;
	 	}

	 	String selectedLevel =
                 levelGroup.getSelection().getActionCommand();
	 	if(selectedLevel.equals(Level.EXPERT.toString())){
	 		level = Level.EXPERT;
	 	} else if(selectedLevel.equals(Level.INTERMIDIATE.toString())) {
	 		level = Level.INTERMIDIATE;
	 	} else {
	 		level = Level.BEGINNER;
	 	}

        String selectedSkin =
                 skinGroup.getSelection().getActionCommand();
	 	if(selectedSkin.equals(Skin.SIMPLE.toString())){
	 		skin = Skin.SIMPLE;
	 	} else if (selectedSkin.equals(Skin.BLIND.toString())) {
            skin = Skin.BLIND;
        } else if(selectedSkin.equals(Skin.PEN.toString())) {
	 		skin = Skin.PEN;
	 	} else {
             skin = Skin.DYNAMIC;
	 	}
	 }

	/**
	 * Implements method from gameLogic.Settings.Importer.
	 */
	public void close(){
	 	userName = null;
	 	userWeapon = null;
	 	level = null;
        skin = null;
	 }

	private Container createContentPane() {
		// Create the content-pane-to-be.
		Container contentPane = getContentPane();

		contentPane.add(userOptions());
		contentPane.add(levelOptions());
        contentPane.add(skinOptions());
        contentPane.add(warningLabel());
		contentPane.add(buttons());

		contentPane.add(tabbedPane(), BorderLayout.PAGE_START);
        contentPane.add(warningLabel(), BorderLayout.CENTER);
		contentPane.add(buttons(), BorderLayout.PAGE_END);

		return contentPane;
	}

	private JTabbedPane tabbedPane(){
		JTabbedPane tabbedPane = new JTabbedPane();

		tabbedPane.add("Player", userOptions());
		tabbedPane.add("Level", levelOptions());
        tabbedPane.add("Skin", skinOptions());

		return tabbedPane;
	}

	private JPanel levelOptions() {

		JPanel box = new JPanel();
		
		checkBoxLevelButtons = new JCheckBox[numLevelButtons];
		levelGroup = new ButtonGroup();
		checkBoxLevelButtons[0] = new JCheckBox(Level.BEGINNER.toString());
		checkBoxLevelButtons[0].setName(Level.BEGINNER.toString());
		checkBoxLevelButtons[0].setActionCommand(Level.BEGINNER.toString());
		checkBoxLevelButtons[1] = new JCheckBox(Level.INTERMIDIATE.toString());
		checkBoxLevelButtons[1].setName(Level.INTERMIDIATE.toString());
		checkBoxLevelButtons[1].setActionCommand(Level.INTERMIDIATE.toString());
		checkBoxLevelButtons[2] = new JCheckBox(Level.EXPERT.toString());
		checkBoxLevelButtons[2].setName(Level.EXPERT.toString());
		checkBoxLevelButtons[2].setActionCommand(Level.EXPERT.toString());

		for (int i = 0; i < numLevelButtons; i++) {
			levelGroup.add(checkBoxLevelButtons[i]);
			if ((checkBoxLevelButtons[i].getName()).equals(level.toString())) {
				checkBoxLevelButtons[i].setSelected(true);
			}
		}

		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Level Options"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                )
        );

		for (int i = 0; i < numLevelButtons; i++) {
			box.add(checkBoxLevelButtons[i]);
		}

		return box;
	}

    private JPanel skinOptions() {

		JPanel box = new JPanel();

		checkBoxSkinButtons = new JCheckBox[numSkinButtons];
		skinGroup = new ButtonGroup();
		checkBoxSkinButtons[0] = new JCheckBox(Skin.SIMPLE.toString());
		checkBoxSkinButtons[0].setName(Skin.SIMPLE.toString());
		checkBoxSkinButtons[0].setActionCommand(Skin.SIMPLE.toString());
		checkBoxSkinButtons[1] = new JCheckBox(Skin.BLIND.toString());
		checkBoxSkinButtons[1].setName(Skin.BLIND.toString());
		checkBoxSkinButtons[1].setActionCommand(Skin.BLIND.toString());
		checkBoxSkinButtons[2] = new JCheckBox(Skin.PEN.toString());
		checkBoxSkinButtons[2].setName(Skin.PEN.toString());
		checkBoxSkinButtons[2].setActionCommand(Skin.PEN.toString());
		checkBoxSkinButtons[3] = new JCheckBox(Skin.DYNAMIC.toString());
		checkBoxSkinButtons[3].setName(Skin.DYNAMIC.toString());
		checkBoxSkinButtons[3].setActionCommand(Skin.DYNAMIC.toString());

		for (int i = 0; i < numSkinButtons; i++) {
			skinGroup.add(checkBoxSkinButtons[i]);
			if ((checkBoxSkinButtons[i].getName()).equals(skin.toString())) {
				checkBoxSkinButtons[i].setSelected(true);
			}
		}

		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		box.setBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Skin Options"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                )
        );

		for (int i = 0; i < numSkinButtons; i++) {
			box.add(checkBoxSkinButtons[i]);
		}

		return box;

    }

	private JPanel userOptions() {
		JPanel panel = new JPanel(new GridLayout(3, 2));

		JLabel userNameLabel = new JLabel("Name: ");

		nameField = new JTextField(userName, 15);
		nameField.setBackground(panel.getBackground());

		JLabel userWeaponLabel = new JLabel("Weapon: ");
		//spacer
		JLabel blank = new JLabel("");

		
		radioOptionsButtons = new JRadioButton[numOptionsButtons];
		optionsGroup = new ButtonGroup();
		radioOptionsButtons[0] = new JRadioButton(Weapon.XX.toString());
		radioOptionsButtons[0].setName(Weapon.XX.toString());
		radioOptionsButtons[0].setActionCommand(Weapon.XX.toString());
		radioOptionsButtons[1] = new JRadioButton(Weapon.OO.toString());
		radioOptionsButtons[1].setName(Weapon.OO.toString());
		radioOptionsButtons[1].setActionCommand(Weapon.OO.toString());

		for (int i = 0; i < numOptionsButtons; i++) {
			optionsGroup.add(radioOptionsButtons[i]);
			if ((radioOptionsButtons[i].getName()).equals(userWeapon.toString())) {
				radioOptionsButtons[i].setSelected(true);
			}
		}

		panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder("Player Options"),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                )
        );

		panel.add(userNameLabel);
		panel.add(nameField);
		panel.add(userWeaponLabel);
		panel.add(radioOptionsButtons[0]);
		panel.add(blank);
		panel.add(radioOptionsButtons[1]);

		return panel;
	}

    private JPanel warningLabel() {
        JPanel panel = new JPanel();

        JLabel firstLine = new JLabel("Warning: Changing the settings\n ");
        JLabel secondLine = new JLabel("will cause the end of your game. ");
        JLabel thirdLine = new JLabel("");
        panel.add(firstLine);
        panel.add(secondLine);
        panel.add(thirdLine);
        return panel;
    }

	private JPanel buttons() {
		JPanel panel = new JPanel();

		buttonOK = new JButton("OK");
		buttonOK.addActionListener(new ButtonOKListener());
		buttonCancel = new JButton("Cancel");
		buttonCancel.addActionListener(new ButtonCancelListener());

		panel.add(buttonOK);
		panel.add(buttonCancel);

		return panel;
	}

    private class ButtonCancelListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    dispose();
		}
	}

	private class ButtonOKListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {

			String levelCommand = levelGroup.getSelection().getActionCommand();
            String skinCommand = skinGroup.getSelection().getActionCommand();
			String optionsCommand = optionsGroup.getSelection().getActionCommand();

            boolean hasChanges =
                    !nameField.getText().equals(userName) ||
                    !levelCommand.equals(level.toString()) ||
                    !skinCommand.equals(skin.toString()) ||
                    !optionsCommand.equals(userWeapon.toString());
            if(hasChanges){
				Settings.Importer builder = GameOptionsDialog.this;
		        Settings settings = new Settings(builder);
                // change the settings in the Main frame
                mainFrame.setSettings(settings);
                // change the settings in Start and start
                // new game with the new settings
                Start.createNewGame(settings);
			}
			dispose();
		}
	}

}
