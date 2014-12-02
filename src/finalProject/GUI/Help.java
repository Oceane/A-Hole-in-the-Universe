package finalProject.GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Help extends JPanel {

	JLabel objectiveLabel, gameplayLabel, controlsLabel;
	JTextArea objective, gameplay, controls;
	CardLayout cardLayout;
	JPanel cardPanel;

	public Help(JPanel cardPanel) {

		// SETUP WINDOW
		this.cardPanel = cardPanel;
		cardLayout = (CardLayout) cardPanel.getLayout();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		window();
	}

	private void window() {

		// OBJECTIVE
		objectiveLabel = new JLabel("GAME OBJECTIVE");
		objective = new JTextArea();
		objective.setText("The objective of the game is to keep your damage lower than the other players when the timer runs out.");
		objective.setEditable(false);

		// GAMEPLAY
		gameplayLabel = new JLabel("GAMEPLAY");
		gameplay = new JTextArea();
		gameplay.setText("Every player is a planet, which they will customize upon creation of a game session. " + "\nEach player plays the game independently of other players and is competing for the lowest damage. " + "\nThus, each player only sees their own player on the screen. " + "\nThe player controls the planet’s movement with arrow keys, and it is constantly being pulled by gravity, making movement challenging." + "\nA comet will bounce off the player according to the velocities and spins of the colliding objects. " + "\n Damage is taken by letting comets into black holes or falling into them yourself. Every comet will be assigned a random starting point, point value, and velocity. " + "\nThe point values will be between 1,000 and 20,000 in increments of 1,000." + "\nThe point value of a comet will be displayed on top of the comet’s " + "\nThere will be a maximum of 10 comets on the screen at a given time."
				+ "\nIf a comet falls into a black hole or goes off the screen, it will be deleted and a new random comet will be generated and added to the scene.");
		gameplay.setEditable(false);

		// CONTROLS
		controlsLabel = new JLabel("CONTROLS");
		controls = new JTextArea();
		controls.setText("Up, down, left, right for player movement. " + "\nMouse for selecting chat box.");
		controls.setEditable(false);

		// RETURN BUTTON
		JButton returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "titlePanel");
			}
		});

		// ADDING TO PANEL
		add(objectiveLabel);
		add(objective);
		add(gameplayLabel);
		add(gameplay);
		add(controlsLabel);
		add(controls);
		add(returnButton);

	}
}
