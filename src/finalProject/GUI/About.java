package finalProject.GUI;

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

		setLayout(new FlowLayout());

		window();
	}

	private void window() {

		// JONATHAN
		ImageIcon imageIcon = new ImageIcon("Backgrounds/unnamed.jpg");
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		add(new JLabel(imageIcon));

		// DAVID
		imageIcon = new ImageIcon("Backgrounds/YosemitePic.jpg");
		image = imageIcon.getImage(); // transform it
		newimg = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		add(new JLabel(imageIcon));

		// GEOFFREY
		imageIcon = new ImageIcon("Backgrounds/geoffrey.jpg");
		image = imageIcon.getImage(); // transform it
		newimg = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		add(new JLabel(imageIcon));

		// NATALIE
		imageIcon = new ImageIcon("Backgrounds/natalie.jpg");
		image = imageIcon.getImage(); // transform it
		newimg = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		add(new JLabel(imageIcon));

		// YUXIN
		imageIcon = new ImageIcon("Backgrounds/yuxin.jpg");
		image = imageIcon.getImage(); // transform it
		newimg = image.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg); // transform it back
		add(new JLabel(imageIcon));

		JButton returnButton = new JButton("Return");

		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "titlePanel");
			}
		});
		add(returnButton);

	}
}