package gameLogic.combination;

import constantsAndTypes.StateOfSector;

/**
 * Combination represents the battle field.
 * It is a type used only by Computer to operate with different situations,
 * which have already happened or are happenning now.
 * Note that this type is visible only in package gameLogic.players.computers,
 * therefore it is the internal representation of the battle field used by Computer.
 */
public class Combination {

    public final int rows = 3;
    public final int columns = 3;

    private StateOfSector[][] data;

    /**
     * Track is a Stack, which keeps track of what was modified first,
     * second, ..., last. It keeps the information as it remembers the
     * index (i, j) in the form 3*i + j, when field data[i][j] is changed
     * from the initial state of data[][] (by changeStateAt()).
     */
    private Stack track;

    /**
	 * Createss a Combination(3*3) with BLANK sectors.
	 */
    public Combination(){
        data = new StateOfSector[rows][columns];
        for(int i = 0; i < data.length; i++)
            for(int j = 0; j < data[0].length; j++)
                data[i][j] = StateOfSector.BLANK;
        track = new StackDinamic();
    }

	/**
	 * Creates a Combination with the same sectors as the given data
	 * @param data
	 */
    public Combination(Combination data) {
        this.data = new StateOfSector[data.rows][data.columns];
        this.track = new StackDinamic();
        ((StackDinamic)data.track).copyIn((StackDinamic)this.track);
        for(int i = 0; i < this.data.length; i++)
            for(int j = 0; j < this.data[0].length; j++)
                this.data[i][j] = data.stateAt(i, j);
    }

    public boolean equals(Object comb) {
        // do not compare the tracks
		if (comb instanceof Combination) {
			for(int i = 0; i < data.length; i++)
				for(int j = 0; j < data[0].length; j++)
					if(data[i][j] != ((Combination)comb).stateAt(i, j)) return false;
			return true;
		} else {
			return super.equals(comb);
		}
	}

	/**
	 * Gives you the StateOfSector of the pointed sector of the Combination.
	 * @param i - the row index
	 * @param j - the column index
	 * @return the state of the destination sector of the Combination.
	 */
    public StateOfSector stateAt(int i, int j){
        return data[i][j];
    }

	/**
	 * You can change the StateOfSector of every
	 * sector of the Combination at any time.
	 * @param i - the row index
	 * @param j - the column index
	 * @param newState
	 */
    public void changeStateAt(int i, int j, StateOfSector newState){
            if (i < 0 || i >= data.length) {
                return;
            } else if (j < 0 || j >= data[0].length) {
                return;
            } else {
                data[i][j] = newState;
                try {
                    track.push(3*i + j);
                } catch (StackException e) {
                    // should never occure
                    e.printStackTrace();
                }
            }
        }

	/**
	 * Gives you the StateOfSector of the last modified sector of the Combination.
	 * The index - lastModified is given a value different from -1,
	 * when method changeStateAt() is called previousely.
	 * @return the state of the destination sector of the Combination.
	 */
    public StateOfSector stateAtLastModified() {
        // let it crash if not initialized from track
        int lastModified = -1;
        try {
            lastModified = track.top();
        } catch (StackException e) {
            e.printStackTrace();
        }
        return data[lastModified/3][lastModified%3];
    }

	/**
	 * Changes the last modified sector of data.
	 * The index - lastModified is given a value different from -1,
	 * when method changeStateAt() is called previousely, since
     * changeStateAt() pushes in the track the modified sector.
     * changeLastModified() also pops the top of the track,
     * so that when you call it again it would refer to the
     * previouse modified.
	 * @param newState
	 */
    public void changeLastModified(StateOfSector newState){
        // let it crash if not initialized from track
        int lastModified = -1;
        try {
            lastModified = track.pop();
        } catch (StackException e) {
            e.printStackTrace();
        }
        data[lastModified/3][lastModified%3] = newState;
    }

	/**
	 * Converts the current combination to a String with lenght 9.
	 * Every sector of the Combination is transformed into a
	 * digit: 0, 1 and 2, written in the String at its position.
	 * The coding is simple - StateOfSector.BLANK refers to 0,
	 * SatteOfField.XX refers to 1 and StateOfSector.OO refers to 2.
	 * The coding begins from the first line, from left to right.
	 * @return the coded combination as String.
	 */
    public String toString() {
        StringBuffer result = new StringBuffer("");
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] == StateOfSector.BLANK) {
                    result.append(0);
                } else if (data[i][j] == StateOfSector.XX) {
                    result.append(1);
                } else {
                    result.append(2);
                }
            }
        }
        return result.toString();
    }

	/**
	 * Reverses the current combination.
	 * StateOfSector.XX is transformed in StateOfSector.OO and
	 * vice versa StateOfSector.OO is transformed in StateOfSector.XX.
	 * @return the reversed Combination
	 */
    public Combination reverse() {
        Combination result = new Combination(this);
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                if (data[i][j] == StateOfSector.XX) {
                    result.data[i][j] = StateOfSector.OO;
                } else if (data[i][j] == StateOfSector.OO) {
                    result.data[i][j] = StateOfSector.XX;
                }
                    // BLANK remains the same
            }
        }
        return result;
    }

    /**
     * Rotates the combination with 90 degrees, clockwise.
     * Be carefull the rotated combination will have only the
     * last modified updated in its track and nothing else.
     * @return the rotated Combination
     */
    public Combination rotate() {
        Combination result = new Combination();
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                if (data[i][j] == StateOfSector.XX) {
                    result.changeStateAt(j, 2 - i, StateOfSector.XX);
                } else if (data[i][j] == StateOfSector.OO) {
                    result.changeStateAt(j, 2 - i, StateOfSector.OO);
                }
            }
        }
        // keep track only of the last modifed
        int lastModified = -1;
        try {
            lastModified = 3*(this.track.top()%3) + 2 - this.track.top()/3;
            result.track = new StackDinamic();
            result.track.push(lastModified);
        } catch (StackException e) {
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * Finds the symmetric Combination with respect to the second vertical.          *
     * Be carefull the symmetric combination will have only the
     * last modified updated in its track and nothing else.
	 * @return the symmetric Combination
	 */
	public Combination findSymetric() {
        Combination result = new Combination();
        for(int i = 0; i < data.length; i++) {
            for(int j = 0; j < data[0].length; j++) {
                if (data[i][j] == StateOfSector.XX) {
                    result.changeStateAt(i, 2 - j, StateOfSector.XX);
                } else if (data[i][j] == StateOfSector.OO) {
                    result.changeStateAt(i, 2 - j, StateOfSector.OO);
                }
            }
        }
        // keep track only of the last modifed
        int lastModified = -1;
        try {
            lastModified = 3*(this.track.top()/3) + 2 - this.track.top()%3;
            result.track = new StackDinamic();
            result.track.push(lastModified);
        } catch (StackException e) {
            e.printStackTrace();
        }
        return result;
	}

	/**
	 * Finds all combinations similar to the current.
	 * All similar combinations are generated in the following manner:
	 * 		First add the current Combination,
	 * 		then add the currrent.findSymetric() combination.
	 * 		Then rotate the current and repeat the priviouse two operations.
	 * 		Rotate until it makes sence(i.e. three times, since the fourth
	 * 		time you will be again in the beggining).
	 * Note that the returned array of Combinations dosen't contain equal combinations.
	 * @return an array of all Combinations similar to the current.
	 */
    public Combination[] findAllSimilar() {
        Combination[] allPossible = new Combination[8];
		int countCombinations = 0;

		int i = 0;
		Combination current = new Combination(this);
		Combination symetric = new Combination(current.findSymetric());
		while (i < allPossible.length) {
			if (find(current, allPossible, countCombinations) == -1) {
				allPossible[countCombinations++] = current;
			}
			if (find(symetric, allPossible, countCombinations) == -1) {
				allPossible[countCombinations++] = symetric;
			}
			current = new Combination(current.rotate());
			symetric = new Combination(current.findSymetric());
			i += 2;
		}

		Combination[] allSimilar = new Combination[countCombinations];
		System.arraycopy(allPossible, 0, allSimilar, 0, allSimilar.length);
        return allSimilar;
    }

	private int find(Combination current, Combination[] result, int countCombinations) {
		for (int i = 0; i < countCombinations; i++) {
			if (current.equals(result[i])) return i;
		}
		return -1;
	}

}