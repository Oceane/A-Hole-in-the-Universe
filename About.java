package Intro;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class About extends JPanel {

	JLabel JonathanLabel, NatalieLabel, GeoffreyLabel, YuxinLabel, DavidLabel;
	JTextArea Jonathan, Natalie, Geoffrey, Yuxin, David;
	CardLayout cardLayout;
	JPanel cardPanel;

	public About(JPanel cardPanel) {

		// SETUP WINDOW
		this.cardPanel = cardPanel;
		cardLayout = (CardLayout) cardPanel.getLayout();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		window();
	}

	private void window() {

		// INSTANTIATION
		DavidLabel = new JLabel("DAVID");
		David = new JTextArea();
		David.setEditable(false);

		GeoffreyLabel = new JLabel("GEOFFREY");
		Geoffrey = new JTextArea();
		Geoffrey.setEditable(false);

		JonathanLabel = new JLabel("JONATHAN");
		Jonathan = new JTextArea();
		Jonathan.setEditable(false);

		NatalieLabel = new JLabel("NATALIE");
		Natalie = new JTextArea();
		Natalie.setEditable(false);

		YuxinLabel = new JLabel("YUXIN");
		Yuxin = new JTextArea();
		Yuxin.setEditable(false);

		JButton returnButton = new JButton("Return");

		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "titlePanel");
			}
		});

		// ADDING
		add(DavidLabel);
		add(David);
		add(GeoffreyLabel);
		add(Geoffrey);
		add(JonathanLabel);
		add(Jonathan);
		add(NatalieLabel);
		add(Natalie);
		add(YuxinLabel);
		add(Yuxin);
		add(returnButton);

	}

}
