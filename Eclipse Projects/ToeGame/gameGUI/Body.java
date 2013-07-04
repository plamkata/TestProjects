package gameGUI;

import gameGUI.battleField.BattleField;
import gameLogic.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import load.Start;

public class Body extends JPanel {
	private static final long serialVersionUID = 2460729464393938620L;

	private static BodyData bodyData;

    // pointer to the game(in Start)
    private static Game game;
    // pointer to field(in Start)
    private static BattleField field;

    private static JLabel winnerLabel;
    private static JProgressBar resultBar;
    private static JLabel userScoreLabel;
    private static JLabel computerScoreLabel;
    private static JButton b;

    public Body(Game mainGame, BattleField battleField) {
        super();
        bodyData = new BodyData();
        field = battleField;
        game = mainGame;
        // update the information here
        game.exportResult(bodyData);

        // display score and players names
        JPanel resultPanel = createResultPanel();
        add(resultPanel);

        // display the field
        add(field);

        setOpaque(true);
    }

    public static void updateResultFields() {
        if (game.getWinner() != null) {
            field.disableField();
            game.updateScore();
            game.exportResult(bodyData);
            if (game.getWinner() != "No Winner") {
                winnerLabel.setText("The winner is " + game.getWinner());
                setProgressBar();
                userScoreLabel.setText(
                        Integer.toString(bodyData.getUserScore()));
                computerScoreLabel.setText(
                        Integer.toString(bodyData.getComputerScore()));
            } else {
                winnerLabel.setText("No winner");
            }
            b.setEnabled(true);
        }
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new GridLayout(5, 1));

        winnerLabel = new JLabel("");
        resultPanel.add(winnerLabel, BorderLayout.CENTER);

        JPanel playerNamesPanel = new JPanel(new BorderLayout());
        JLabel userNameLabel = new JLabel(bodyData.getUserName());
        playerNamesPanel.add(userNameLabel, BorderLayout.WEST);
        JLabel computerNameLabel = new JLabel(bodyData.getComputerName());
        playerNamesPanel.add(computerNameLabel, BorderLayout.EAST);
        resultPanel.add(playerNamesPanel);

        resultBar = new JProgressBar(JProgressBar.HORIZONTAL);
        // set the progress bar
        setProgressBar();
        resultPanel.add(resultBar, BorderLayout.CENTER);

        JPanel playerScoresPanel = new JPanel(new BorderLayout());
        userScoreLabel = new JLabel(Integer.toString(bodyData.getUserScore()));
        playerScoresPanel.add(userScoreLabel, BorderLayout.WEST);
        computerScoreLabel = new JLabel(Integer.toString(bodyData.getComputerScore()));
        playerScoresPanel.add(computerScoreLabel, BorderLayout.EAST);
        resultPanel.add(playerScoresPanel);

        b = new JButton("Go");
        b.addActionListener(new ButtonListener());
        b.setToolTipText("Press this button to continue.");
        Font buttonFont = new Font("Arial",Font.BOLD, 15);
        b.setFont(buttonFont);
        b.setEnabled(false);
        resultPanel.add(b, BorderLayout.CENTER);
        return resultPanel;
    }

    private static void setProgressBar() {
        if (bodyData.getUserScore() == 0 && bodyData.getComputerScore() == 0) {
            resultBar.setValue(50);
        } else {
            resultBar.setValue(
                    (bodyData.getUserScore()*100)/
                    (bodyData.getUserScore() +
                    bodyData.getComputerScore())
            );
        }
    }

    private class BodyData implements Game.ResultExporter {

        // gameLogic.Game's score fields
        private String userName;
        private String computerName;
        private int userScore;
        private int computerScore;

        public void addUserName(String name) {
            userName = name;
        }

        public void addComputerName(String name) {
            computerName = name;
        }

        public void addScore(int computerScore, int userScore) {
            this.userScore = userScore;
            this.computerScore = computerScore;
        }

        String getUserName() {
            return userName;
        }

        String getComputerName() {
            return computerName;
        }

        int getUserScore() {
            return userScore;
        }

        int getComputerScore() {
            return computerScore;
        }

    }

    private class ButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            winnerLabel.setText("");
            b.setEnabled(false);
            Start.continueWithAnotherGame();
        }

    }

}
