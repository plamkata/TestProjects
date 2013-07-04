package gameLogic;

import constantsAndTypes.Level;
import constantsAndTypes.Score;
import constantsAndTypes.StateOfSector;
import constantsAndTypes.Weapon;
import gameLogic.players.Computer;
import gameLogic.players.Player;
import gameLogic.players.User;
import gameLogic.players.computers.ComputerBeginner;
import gameLogic.players.computers.ComputerExpert;
import gameLogic.players.computers.ComputerIntermidiate;
import gameLogic.combination.Combination;

import java.io.FileNotFoundException;

public class Game {

    private Settings settings;

    /**
     * This is the battle field.
     * Moreover, this field is used only in the gameLogic and
     * dosen't have any connection to the gameGUI BattleField.
     * This connection is realized in the play() methods of the players.
     */
    private Combination field;

    // real objects - the players of the game
    private Player user;
    private Player computer;

    // pointers to one of the players
    private Player first;
    private Player winner;

    private Score score;

	/**
	 * This is the only way to create an object of this class.
	 * @param settings - the settings should be clear when the game is created.
	 * @param GUIcomponent - visualises the moves of the players(i.e. their shots).
	 * @throws FileNotFoundException - thrown only in the case, when ComputerIntermediate
	 * is choosen in the Settings and no file for loading combinations is found.
	 * @see ComputerIntermidiate
	 * @see constantsAndTypes.Level
	 */
    public Game(Settings settings, BattleField GUIcomponent)
            throws FileNotFoundException {
        this.settings = settings;

        field = new Combination();

        // create user
        user  = new User(
                settings.getUserWeapon(),
                settings.getUserName(),
                GUIcomponent
        );

        // cerate computer according to the level from settings
        if (settings.getLevel() == Level.BEGINNER) {
            computer = new ComputerBeginner(
                    settings.getComputerWeapon(),
                    settings.getComputerName(),
                    GUIcomponent
            );
        } else if (settings.getLevel() == Level.INTERMIDIATE){
            computer = new ComputerIntermidiate(
                    settings.getComputerWeapon(),
                    settings.getComputerName(),
                    GUIcomponent
            );
        } else {
            computer = new ComputerExpert(
                    settings.getComputerWeapon(),
                    settings.getComputerName(),
                    GUIcomponent
            );
        }

        // set the pointers and the score to default
        first = user;
        winner = null;
        score = new Score();
    }

    /**
     * Players should know where is the battle field,
     * but it is actualy property of the Game.
     * It is just renamed here.
     * This is the interface to be implemented in the
     * gameGUI as the real battle field.
     */
    public interface BattleField
            extends Player.BattleField {}
    /**
	 * Exports the result in the MainFrame, when we have a winner.
	 */
    public interface ResultExporter {
        void addUserName(String name);
        void addComputerName(String name);
        void addScore(int computerScore, int userScore);
    }

    /**
     * Simulates the game.
     */
    public void start() {
        if (first == computer) {
            while (true) {
                computer.play(field);
                if (isFinished()) break;
                user.play(field);
                if (isFinished()) break;
            }
        } else if (first == user) {
            while (true) {
                user.play(field);
                if (isFinished()) break;
                computer.play(field);
                if (isFinished()) break;
            }
        }
    }

    /**
     * Called in restart().
     * Prepares Game's class fields for starting a new game.
     */
    public void clear(BattleField battleField) {
        // who's first the next game?
        if (first == user) {
            first = computer;
        } else if (first == computer) {
            first = user;
        }
        computer.prepareComputer(battleField);
        user.prepareComputer(battleField);
        // Only Computer has reset() method,
        // Player dosen't have since we don't know anything about him.
        ((Computer)computer).reset(winner, field);
        // clear field
        field = new Combination();
        // set no winner
        winner = null;
    }


    /**
     * You can have the winner at any time.
     *
     * @return the name of the user from the settings,
     *  if user is the winner.
     * @return the name of the computer from settings,
     *  if computer is the winner.
     * @return <code>"No Winner"</code>, if the game has finished equal.
     * @return <code>null</code>, if the game is in progress.
     */
    public String getWinner() {
        if (winner == computer) {
            return settings.getComputerName().toString();
        } else if (winner == user) {
            return settings.getUserName().toString();
        } else {// i.e. winner = null
            if (isFinished()) {
                return "No Winner";// to be replaced
            } else {
                return null;
            }
        }
    }

    /**
     * Called in start() after the game is finished.
     *
     * It simply updates the score according to
     * who is the winner.
     */
    public void updateScore(){
        if (winner == computer) {
            score.updateComputerScore();
        } else if (winner == user) {
            score.updateUserScore();
        }
    }

    public void exportResult(ResultExporter builder) {
        builder.addUserName(user.getName().toString());
        builder.addComputerName(computer.getName().toString());
        builder.addScore(score.getComputerScore(), score.getUserScore());
    }

    /**
     * Used in start() and in getWinner().
     *
     * Every time a Player plays we check if the
     * game is finnished.
     *
     * @return false, if the game is in progress.
     * @return true, if we have a winner, or if
     * there are no blanks in the battle field.
     */
    private boolean isFinished() {
        if (hasWinner()) {
            return true;
        } else if (!fieldHasBlanks()) {
            winner = null; // no winner
            return true;
        } else {
            return false;
        }
    }

    /**
     * Used in getWinner(), and in start().
     *
     * Searches through field[][] for a winner, and
     * calls updateWinnerAt() if a winner is found.
     *
     * @return true, if three fields in a row, column,
     * or diagonal have the same state in the field[][].
     * @return false, if there are no such fields
     */
    private boolean hasWinner() {
        boolean finished = false;
        // check the rows
        for (int row = 0; row < field.rows; row++) {
            int col = 0;
            if (field.stateAt(row, col) == StateOfSector.BLANK) continue;
            if (
                    (field.stateAt(row, col) == field.stateAt(row, col + 1)) &&
                    (field.stateAt(row, col) == field.stateAt(row, col + 2))
            ) {
                updateWinnerAt(row, col);
                finished = true;
            }
        }
        // check the columns
         for (int col = 0; col < field.columns; col++) {
            int row = 0;
            if (field.stateAt(row, col) == StateOfSector.BLANK) continue;
            if (
                    (field.stateAt(row, col) == field.stateAt(row + 1, col)) &&
                    (field.stateAt(row, col) == field.stateAt(row + 2, col))
            ) {
                updateWinnerAt(row, col);
                finished = true;
            }
        }
        // check the diagonals
        int row = 0;
        int col = 0;
        if (
                (field.stateAt(row, col) != StateOfSector.BLANK) &&
                (field.stateAt(row, col) == field.stateAt(row + 1, col + 1)) &&
                (field.stateAt(row, col) == field.stateAt(row + 2, col + 2))
        ) {
            updateWinnerAt(row, col);
            finished = true;
        }

        row = 2;
        col = 0;
        if (
                (field.stateAt(row, col) != StateOfSector.BLANK) &&
                (field.stateAt(row, col) == field.stateAt(row - 1, col + 1)) &&
                (field.stateAt(row, col) == field.stateAt(row - 2, col + 2))
        ) {
            updateWinnerAt(row, col);
            finished = true;
        }

        return finished;
    }

    /**
     * Used to avoid repetition of code in hasWinner().
     *
     * UpdateWinnerAt() suggests that we have a winner, who has
     * played at field[row][col]. The method updates the field
     * winner with respect to what was fired at field[row][col].
     * @param row
     * @param col
     */
    private void updateWinnerAt(int row, int col) {
        if(field.stateAt(row, col) == StateOfSector.OO) {
            // the winner plays with the OO
            // find who is the winner
            if(settings.getUserWeapon() == Weapon.OO) {
                winner = user;
            } else if (settings.getComputerWeapon() == Weapon.OO) {
                winner = computer;
            }
        } else if(field.stateAt(row, col) == StateOfSector.XX) {
            // the winner plays with the XX
            // find who is the winner
            if(settings.getUserWeapon() == Weapon.XX) {
                winner = user;
            } else if (settings.getComputerWeapon() == Weapon.XX) {
                winner = computer;
            }
        }
    }

    /**
     * Used in isFinished(), to check if field[][] has blanks.
     * @return true, if there are still blanks at the field.
     * @return false, if the hole field has been filled by
     * the players.
     */
    private boolean fieldHasBlanks() {
        boolean hasBlanks = false;
        for (int i = 0; i < field.rows; i++) {
            for (int j = 0; j < field.columns; j++) {
                if (field.stateAt(i, j) == StateOfSector.BLANK) {
                    hasBlanks = true;
                }
            }
        }
        return hasBlanks;
    }

}