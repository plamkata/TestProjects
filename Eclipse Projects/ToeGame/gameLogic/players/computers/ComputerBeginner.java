package gameLogic.players.computers;

import constantsAndTypes.Name;
import constantsAndTypes.StateOfSector;
import constantsAndTypes.Weapon;
import gameLogic.combination.Combination;
import gameLogic.players.Computer;
import gameLogic.players.Player;

import java.util.Random;
import java.util.Vector;

/**
 * ComputerBeginner is created in Game when
 * Settings.level is set to BEGINNER.
 * @see constantsAndTypes.Level
 */
public class ComputerBeginner extends Computer {

    /**
     * This is the memory of the computers.
     * At the beginning it is empty, but in the proccess of playing,
     * at the end of every game the ComputerBeginner remembers some
     * valuable information kept in currentCombination.
     */
    protected Vector memory;
    private static final int memoryCapacity = 5000;
    private static final int memoryDecrement = 10;
    // protected static PrintStream ps;

    public ComputerBeginner(Weapon weapon, Name name, BattleField listener) {
        super(weapon, name, listener);
        memory = new Vector();
/*  	// this code is used for making the file newCombinations.txt
		if (this instanceof ComputerIntermidiate) {
			try {
				ps = new PrintStream(
								new FileOutputStream("newCombinations.txt"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
*/
	}

    /**
     * The real move is made in the shoot() method.
     * @param _field
     */
    public void play(Combination/*out*/ _field) {
        // find available fields
        // available[] contains the indexes (i, j), which
        // are BLANK in the _field, written in the form 3*i + j
        int[] available = new int[_field.rows*_field.columns];
        int countAvailable = 0;
        for (int i = 0; i < _field.rows; i++) {
            for (int j = 0; j < _field.columns; j++) {
                if (_field.stateAt(i, j) == StateOfSector.BLANK) {
                    // field is available
                    available[countAvailable++] = 3*i + j;
                }
            }
        }
        chooseAmongAvailable(available, countAvailable, _field);
    }

    /**
     * Prepares the computers for a new game.
     * The method takes the winner as a parameter
     * to know how to play the next game.
     * @param winner
     */
    public void reset(Player winner, Combination field) {
        addCurrentCombinationToMemory(winner, field);
    }

    /**
     * The real shot of Computer's move is made here.
     * The shoot() method is called in chooseAmongAvailable().
     * @param _field
     * @param choise
     */
    protected void shoot(Combination _field, int choise) {
        // make your move and fire your weapon at the field
        if (weapon == Weapon.OO) {
            _field.changeStateAt(choise/3, choise%3, StateOfSector.OO);
        } else {
            _field.changeStateAt(choise/3, choise%3, StateOfSector.XX);
        }
        /** There is a listener from the gameGUI waiting for your shot.
         * Display what is your choise.
         *
         * Our abstract builder is actually of type BattleField;
         * now BattleField extends from both Listener and Speaker
         * (internal interfaces respectively from Computer and User).
         *
         * Therefore builder has vast functionality and since here
         * we use it only as Computer.Listener we downcast it.
         */
        ((Computer.Listener)builder).addComputerShotAt(choise/3, choise%3, weapon);
    }

    /**
       * Helps for loading the initial memory in ComputerIntermidiate.
       * @param str
       * @return <code>null</code> if the <code>str</code> is corrupted.
       */
    protected Combination convertToCombination(String str) {
          if (str.length() != 9) return null;
          for (int i = 0; i < str.length(); i++) {
              if ((str.charAt(i) != '0') && (str.charAt(i) != '1') &&
                      (str.charAt(i) != '2')) return null;
          }

          // initialize witn BLANK
          Combination result = new Combination();
          for (int i = 0; i < str.length(); i++) {
              // 0 refers to BLANK
              // 1 refers to XX
              // 2 refers to OO
              if (str.charAt(i) == '1') {
                  result.changeStateAt(i/3, i%3, StateOfSector.XX);
              } else if (str.charAt(i) == '2'){
                  result.changeStateAt(i/3, i%3, StateOfSector.OO);
              }
          }
          return result;
      }

    /**
     * Helps play() to make the right move and makes
     * the move instead of play(), using shoot().
     * @param available
     * @param countAvailable
     * @param _field
     */
    private void chooseAmongAvailable(
            int[] available, int countAvailable, Combination _field) {
        boolean goodChoise = false;
        Random generator = new Random();
        Combination testCombination = new Combination(_field);
        while (!goodChoise) {
            // try choosing random among the available, i.e.
            // try choosing random index among the indexes of available[].
            int index = generator.nextInt(countAvailable);
            // choise = 3*i + j, where (i, j) is the choosen field
            int choise = available[index];

            // add to the testing Combination to check if it is OK
            addChoiseToCombination(testCombination, choise);
            // check if this is not among the loosing combinations
            boolean isOK = true;
            // if you have only one choise available make it, no matter if it
            // is in memory or not
            if (countAvailable <= 1) {
                isOK = true;
            } else {
                for (int i = 0; i < memory.size(); i++) {
                    Combination comb = (Combination)memory.elementAt(i);
                    if (testCombination.equals(comb)) {
                        isOK = false;
                        break;
                    }
                }
            }
            if (isOK) {
                shoot(_field, choise);
                goodChoise = true; // break the while
            } else {
                // remove the changed state from testCombination
                testCombination.changeStateAt(
                        choise/3, choise%3, StateOfSector.BLANK);
                // make the testing choise unavailable
                removeElementAt(available, countAvailable, index);
                countAvailable--;
                goodChoise = false; // try again
            }
        }
    }

    /**
     * Helps chooseAmongAvailable()
     * to make the testing choise unavailable,
     * when testing choise is in the memory.
     * @param _array
     * @param length
     * @param index
     */
    private void removeElementAt(int[] _array, int length, int index) {
        // _array is a dinamic array and _lenght is its pointer
        if (_array == null) return;
        if (length < 0 || length >= _array.length) return;
        if (index < 0 || index >= length) return;
        int i = index;
        while (i < length) {
            _array[i] = _array[i + 1];
            i++;
        }
        return;
    }

    /**
     * Helps choseAmongAvailable()
     * to add the choise to the testing Combination.
     * @param _testCombination
     * @param choise
     */
    private void addChoiseToCombination(
            Combination _testCombination, int choise) {
        if (weapon == Weapon.OO) {
            _testCombination.changeStateAt(choise/3, choise%3, StateOfSector.OO);
        } else if (weapon == Weapon.XX) {
            _testCombination.changeStateAt(choise/3, choise%3, StateOfSector.XX);
        }
    }

    /**
     * Called in reset() to refresh Computer's memory, when starting a new game.
     * @param winner
     */
    private void addCurrentCombinationToMemory(Player winner, Combination field) {
        Combination currentCombination = new Combination(field);
        if (winner == this) {
            /**
             * If computer is the winner, then he played last.
             * Computer should take back its last winning move
             * and remember the modified currentCombination,
             * to learn from User's mistake.
             */
            currentCombination.changeLastModified(StateOfSector.BLANK);
            currentCombination = new Combination(currentCombination.reverse());
        } else if (winner != null && winner != this){
            /**
             * If usher is the winner, then Computer didn't play last.
             * Therefore the last move was usher's move. We should erase
             * the last winning move of user and remember the Combination
             * without modification.
             */
            currentCombination.changeLastModified(StateOfSector.BLANK);
        } else if (winner == null){
            /** i.e. we have no winner.
             * Two choices of action:
             *      1) Computer is satisfied with equal game,
             *          so he will not remember the game.
             *      2) Computer is not satisfied with equal game,
             *          so he will remember the game, but to do this,
             *          he needs to know weather, he made the last move,
             *          or the usher. If he did the last move, he should
             *          modify the current combination before remembering it,
             *          if he didn't made the last move, no modification
             *          of currentCombination is needed.
             * We choose the second.
             */
            return;

        }

        // if the combination is remembered we have let the user beat us
        // in the same manner as before i.e. we had only one choise.
        // we shall take back the last two moves and remember this modified
        // combination(if we haven't done it before).
        if (isInMemory(currentCombination)) {
            currentCombination.changeLastModified(StateOfSector.BLANK);
            currentCombination.changeLastModified(StateOfSector.BLANK);
            if (!isInMemory(currentCombination) &&
                    winner != this && winner != null) {
                remember(currentCombination);
            }
        } else {
            remember(currentCombination);
        }

        // Make it forget the first memoryDecrement
        // combinations, if memoryCapacity is reached.
        if (memory.size() > memoryCapacity) {
            for (int i = 0; i < memoryDecrement; i++) {
                memory.removeElementAt(0);
            }
        }
    }

    private void remember(Combination currentCombination) {
        Combination[] allSimilar = currentCombination.findAllSimilar();
        for (int i = 0; i < allSimilar.length; i++) {
            memory.add(allSimilar[i]);
               if (this instanceof ComputerIntermidiate) {
                // ps.println(allSimilar[i].toString());
            }
        }
    }

    private boolean isInMemory(Combination currentCombination) {
        boolean isRemembered = false;
        for (int memoryIndex = 0; memoryIndex < memory.size(); memoryIndex++){
         	Combination combFromMemory = (Combination)memory.elementAt(memoryIndex);
            if (currentCombination.equals(combFromMemory)) {
            	isRemembered = true;
            }
        }
        return isRemembered;
    }

}
