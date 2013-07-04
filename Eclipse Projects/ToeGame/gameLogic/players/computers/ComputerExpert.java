package gameLogic.players.computers;

import constantsAndTypes.Name;
import constantsAndTypes.StateOfSector;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;
import gameLogic.players.Player;

import java.util.Random;

public class ComputerExpert extends ComputerBeginner {

    public ComputerExpert(Weapon weapon, Name name, Player.BattleField listener) {
        super(weapon, name, listener);
    }

    public void play(Combination/*out*/ _field) {
        if (weapon == Weapon.XX) {
            // first check if you can win
            if (checkField(_field, StateOfSector.XX)) return;
            // after that check if you can prevent the user from winning
            if (checkField(_field, StateOfSector.OO)) return;
            // after that enter your defending strategy
            if (defend(_field, StateOfSector.XX)) return;
        } else {
            if (checkField(_field, StateOfSector.OO)) return;
            if (checkField(_field, StateOfSector.XX)) return;
            if (defend(_field, StateOfSector.OO)) return;
        }
        super.play(_field);
    }

    /**
     * Checks the field for the current marker using checkLine().
     * @param _field - the field from Game
     * @param marker - the marker to be checked
     * @return true, if move was forced
     */
    private boolean checkField(Combination _field, StateOfSector marker) {
        // check the rows
        if (checkLine(_field, 0, 1, 2, marker)) return true;
        if (checkLine(_field, 3, 4, 5, marker)) return true;
        if (checkLine(_field, 6, 7, 8, marker)) return true;
        // check the columns
        if (checkLine(_field, 0, 3, 6, marker)) return true;
        if (checkLine(_field, 1, 4, 7, marker)) return true;
        if (checkLine(_field, 2, 5, 8, marker)) return true;
        // check the diagonals
        if (checkLine(_field, 0, 4, 8, marker)) return true;
        if (checkLine(_field, 2, 4, 6, marker)) return true;
        return false;
    }
    
    /**
     * The method takes three parameters, which refer to three
     * indexes of sectors in the _field, the field itself and the marker.
     * If two sectors of those three have been filled with
     * the marker and the 3rd is empty CmputerExpert forces
     * the choise of the third.
     * 0 <= x, y, z < _field.lenght*_field[0].lenght
     * @param _field - the field where the game is played
     * @param x = 3*i + j, where (i, j) is the index in the field
     * @param y = 3*i + j, where (i, j) is the index in the field
     * @param z = 3*i + j, where (i, j) is the index in the field
     * @param marker - the StateOfSector to be checked on the line
     * @return true if shot was forced
     */
    private boolean checkLine(
            Combination _field,
            int x, int y, int z,
            StateOfSector marker
    ) {
        boolean forcedMove = false;
        StateOfSector xState = _field.stateAt(x/3, x%3);
        StateOfSector yState = _field.stateAt(y/3, y%3);
        StateOfSector zState = _field.stateAt(z/3, z%3);
        if (
                xState == StateOfSector.BLANK &&
                yState == marker &&
                zState == marker
        ) {
            shoot(_field, x);
            forcedMove =  true;
        } else if (
                xState == marker &&
                yState == StateOfSector.BLANK &&
                zState == marker
        ) {
            shoot(_field, y);
            forcedMove = true;
        } else if (
                xState == marker &&
                yState == marker &&
                zState == StateOfSector.BLANK
        ) {
            shoot(_field, z);
            forcedMove =  true;
        }
        return forcedMove;
    }

    /**
     * Every defencing strategy beggins with playing in the middle.
     * @param _field
     */
    private boolean defend(Combination _field, StateOfSector marker) {
        int countAvailable = 0;
        for (int i = 0; i < _field.rows; i++) {
            for (int j = 0; j < _field.columns; j++) {
                if (_field.stateAt(i, j) == StateOfSector.BLANK) countAvailable++;
            }
        }

        if (countAvailable == 8) {
			if (_field.stateAt(1, 1) == StateOfSector.BLANK) {
				shoot(_field, 4);
				return true;
			} else {
            	Random generator = new Random();
            	int choise = 2;
            	while (choise == 2) {
                	choise = generator.nextInt(5);
            	}
            	shoot(_field, 2*choise);
            	return true;
			}
        } else if (countAvailable == 6) {
			Combination currentCombination = new Combination(_field);

			Combination badCombination = convertToCombination("100020001");
			if (marker == StateOfSector.XX) {
				badCombination = badCombination.reverse();
			}
            if (currentCombination.equals(badCombination) ||
				currentCombination.equals(badCombination.rotate())
			) {
                Random generator = new Random();
                int choise = generator.nextInt(4);
                shoot(_field, 2*choise + 1);
                return true;
            }

			badCombination = convertToCombination("200010001");
			if (marker == StateOfSector.XX) {
				badCombination = badCombination.reverse();
			}
			Combination badCombination1 = badCombination.rotate();
			Combination badCombination2 = badCombination1.rotate();
			Combination badCombination3 = badCombination2.rotate();
            if (currentCombination.equals(badCombination) ||
				currentCombination.equals(badCombination2)
			) {
                shoot(_field, 6);
                return true;
            } else if (currentCombination.equals(badCombination1) ||
				currentCombination.equals(badCombination3)
			) {
                shoot(_field, 0);
                return true;
            }

			badCombination = convertToCombination("010120000");
			if (marker == StateOfSector.XX) {
				badCombination = badCombination.reverse();
			}
			if (currentCombination.equals(badCombination)) {
                Random generator = new Random();
                int choise = 2;
				while (choise == 2) {
					choise = generator.nextInt(4);
				}
                shoot(_field, 2*choise);
                return true;
			} else if (currentCombination.equals(badCombination = badCombination.rotate())) {
                Random generator = new Random();
                int choise = 2;
				while (choise == 2 || choise == 3) {
					choise = generator.nextInt(5);
				}
                shoot(_field, 2*choise);
                return true;
			} else if (currentCombination.equals(badCombination = badCombination.rotate())) {
                Random generator = new Random();
                int choise = 2;
				while (choise == 2 || choise == 0) {
					choise = generator.nextInt(5);
				}
                shoot(_field, 2*choise);
                return true;
			} else if (currentCombination.equals(badCombination = badCombination.rotate())) {
                Random generator = new Random();
                int choise = 2;
				while (choise == 2 || choise == 1) {
					choise = generator.nextInt(5);
				}
                shoot(_field, 2*choise);
                return true;
			}
        }

        return false;
    }

}
