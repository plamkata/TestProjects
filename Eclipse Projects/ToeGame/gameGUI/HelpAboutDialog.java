// Created on 2005-5-17
//@author Anislav
package gameGUI;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.*;

public class HelpAboutDialog extends JDialog {
	private static final long serialVersionUID = -7157106815781945686L;

	public HelpAboutDialog(){}

    // factory method
	public void createAndShowGUI(JFrame mainFrame) {
		//Create and set up the dialog.
		JDialog dialog = new JDialog(mainFrame, "About", true);
		dialog.setContentPane(createContentPane());

		//Display the window.
		dialog.setSize(330, 150);
		dialog.setLocation(mainFrame.getX() + 40, mainFrame.getY() + 40);
		dialog.setResizable(true);
		dialog.setVisible(true);
	}

	private Container createContentPane() {
		//Create the content-pane-to-be.
		JPanel contentPane = new JPanel(new GridLayout(1, 1));

		contentPane.add(authors());

		// If true the component paints every pixel within its bounds.
		// Otherwise, the component may not paint some or all of its pixels,
		// allowing the underlying pixels to show through.
		contentPane.setOpaque(true);

		return contentPane;
	}

	private JPanel authors() {
		JPanel panel = new JPanel();

		JLabel authors1 = new JLabel("Copyright: Sofia University \"St. Klimet Ohridski\", ");
		JLabel authors2 = new JLabel("Sofia, Buglaria; \n");
		JLabel authors3 = new JLabel("Faculty of Mathematics and Informatics, ");
		JLabel authors4 = new JLabel("Computer Science, I course\n");
		JLabel authors5 = new JLabel("Authors: PLamen Vasilev Alexandrov, Anislav Dimitrov\n");
		panel.add(authors1);
		panel.add(authors2);
		panel.add(authors3);
		panel.add(authors4);
		panel.add(authors5);

		return panel;
	}

}
