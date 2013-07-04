package constantsAndTypes;

/**
 * This class contains the score of the game.
 * There is a field of this type in Game.
 */
public class Score {

    private int computerScore;
    private int userScore;

    public Score() {
        computerScore = 0;
        userScore = 0;
    }

    public int getComputerScore() {
        return computerScore;
    }

    public void updateComputerScore() {
        computerScore++;
    }

    public int getUserScore() {
        return userScore;
    }

    public void updateUserScore() {
        userScore++;
    }

}
