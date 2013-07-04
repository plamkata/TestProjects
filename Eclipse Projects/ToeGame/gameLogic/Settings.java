package gameLogic;

import constantsAndTypes.Level;
import constantsAndTypes.Name;
import constantsAndTypes.Weapon;
import constantsAndTypes.Skin;

/**
 * This class contains the settings of the game and therefore
 * it is ushed as a field of the class Game.
 *
 * Note that when the settings are changed, a new game is started
 * with the new settings(the constructor of the class Game takes
 * Settings as a parametre).
 *
 * The class uses "The Gang of Four Pattern" and has an internal
 * interface Importer implemented in the GameOptionsDialog from the gameGUI.
 * As well as na interface Exporter, which is also implemented in the
 * GameOptionsDialog and used to display the current settings loaded.
 *
 * Settings are created either by default constructor, which refers
 * to default Settings, or by an Importer builder, which refers to
 * changed settings, while the project is running.
 */
public class Settings {

    private Level level;
    private Skin skin;

    private Name userName;
    private Weapon userWeapon;

    private Name computerName = new Name("Computer");
    private Weapon computerWeapon;

	/**
	 * This interface is implemented in the gameGUI.
	 * The functionality provided is used only by the export() method
	 * in this class. The real implementation of this functionality
	 * takes place in GameOptionsDialog, i.e. the export method is called
	 * by a hadle to GameOptionsDialog's object.
	 */
    public interface Exporter {
        void addLevel(Level level);
        void addSkin(Skin skin);
        void addUserName(String userName);
        void addUserWeapon(Weapon userWeapon);
    }

	/**
	 * This interface is also implemented in the gameGUI.
	 * The functionality provided is used only in the extra constructor
	 * of this class, which takes an Importer as a parameter, i.e. a handle
	 * to GameOptionsDialog's object. The real implementation of this
	 * functionality is implemented in GameOptionsDialog and the extra
	 * constructor is called only, when Settings(different from default)
	 * are created by the dialog itself, when somethingin the dialog is changed.
	 */
    public interface Importer {
        Level provideLevel();
         Skin provideSkin();
        String provideUserName();
        Weapon provideUserWeapon();
        void open();
        void close();
    }

	/**
	 * Default settings.
	 */
    public Settings() {
        level = Level.BEGINNER;
        skin = Skin.SIMPLE;

        userName = new Name("User");
        userWeapon = Weapon.XX;

        computerName = new Name("Computer");
        computerWeapon = Weapon.OO;
    }

	/**
	 * Creates new Settings from the builder taken as a parameter.
	 * @param builder - the dialog in gameGUI, which is responsible
	 * for displaying and changing the Settings of the Game.
	 */
    public Settings(Importer builder) {
        builder.open();

        level = builder.provideLevel();
        skin = builder.provideSkin();

        userName = new Name(builder.provideUserName());
        userWeapon = builder.provideUserWeapon();

        computerName = new Name("Computer"); // fixed
        if (userWeapon.equals(Weapon.XX)) {
            computerWeapon = Weapon.OO;
        } else {
            computerWeapon = Weapon.XX;
        }

        builder.close();
    }

	/**
	 * Used to export the data of this class, hiding its implementation,
	 * to the builder, before it is shown to the user.
	 * @param builder - the dialog in gameGUI, which is responsible
	 * for displaying and changing the Settings of the Game.
	 */
    public void export(Exporter builder) {
        builder.addLevel(level);
        builder.addSkin(skin);

        builder.addUserName(userName.toString());
        builder.addUserWeapon(userWeapon);
    }

    /**
	 * This method is to be used in gameGUI.
	 * @return the skin being choosen
	 */
    public Skin getSkin() {
        return skin;
    }

    /**
	 * These getters below are only to be used by the package gameLogic.
	 * The real data is actualy hiden by the Importer and Exporter pattern.
	 * Nothing of the implementation of this class is visible outside the
	 * package exept the skin since it is part of the Graphcal settings.
	 */
    Level getLevel() {
        return level;
    }

	/**
	 * These getters below are only to be used by the package gameLogic.
	 * The real data is actualy hiden by the Importer and Exporter pattern.
	 * Nothing of the implementation of this class is visible outside the
	 * package exept the skin since it is part of the Graphcal settings.
	 */
    Name getUserName() {
        return userName;
    }

	/**
	 * These getters below are only to be used by the package gameLogic.
	 * The real data is actualy hiden by the Importer and Exporter pattern.
	 * Nothing of the implementation of this class is visible outside the
	 * package exept the skin since it is part of the Graphcal settings.
	 */
    Weapon getUserWeapon() {
        return userWeapon;
    }

	/**
	 * These getters below are only to be used by the package gameLogic.
	 * The real data is actualy hiden by the Importer and Exporter pattern.
	 * Nothing of the implementation of this class is visible outside the
	 * package exept the skin since it is part of the Graphcal settings.
	 */
    Name getComputerName() {
        return computerName;
    }

	/**
	 * These getters below are only to be used by the package gameLogic.
	 * The real data is actualy hiden by the Importer and Exporter pattern.
	 * Nothing of the implementation of this class is visible outside the
	 * package exept the skin since it is part of the Graphcal settings.
	 */
    Weapon getComputerWeapon() {
        return computerWeapon;
    }

}