// Created on 2005-5-17
//@author Anislav
package gameGUI;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class HelpContentsDialog extends JDialog {
	private static final long serialVersionUID = 5956946866715105983L;

	public HelpContentsDialog(){}
	
	public void createAndShowGUI(JFrame mainFrame) {
		//Create and set up the dialog.
		JDialog dialog = new JDialog(mainFrame, "Contents", true);
		dialog.setContentPane(createContentPane());

		//Display the window.
		dialog.setSize(280, 130);
		dialog.setLocation(mainFrame.getX() + 40, mainFrame.getY() + 40);
		dialog.setResizable(true);
		dialog.setVisible(true);
	}

	private Container createContentPane() {
		// Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new GridLayout(1, 1));

		contentPane.add(contents());

		// If true the component paints every pixel within its bounds.
		// Otherwise, the component may not paint some or all of its pixels,
		// allowing the underlying pixels to show through.
		contentPane.setOpaque(true);

		return contentPane;
	}

	private JPanel contents() {
		JPanel panel = new JPanel(new FlowLayout());

		JLabel text1 = new JLabel("This is the well known game of Tick Tack Toe.\n");
		JLabel text2 = new JLabel("The game is played at a field 3x3 and the \n");
		JLabel text3 = new JLabel("players play in tern. The winner is the one \n");
		JLabel text4 = new JLabel("who puts 3 of his marks on a line.");

		panel.add(text1);
		panel.add(text2);
		panel.add(text3);
		panel.add(text4);
		return panel;
	}
}
