package finalProject.GUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Start extends JPanel {

	CardLayout cardLayout;
	JPanel cardPanel;
	JButton enterButton, quitButton;
	BufferedImage img;
	String name = "universe1.jpg";

	public Start(JPanel cardPanel) {
		// SETUP WINDOW
		this.cardPanel = cardPanel;
		cardLayout = (CardLayout) cardPanel.getLayout();

		setLayout(new GridBagLayout());

		window();
	}

	private void window() {
		enterButton = new JButton("Enter");
		quitButton = new JButton("Quit");

		enterButton.setPreferredSize(new Dimension(300, 100));
		enterButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "titlePanel");
			}
		});
		quitButton.setPreferredSize(new Dimension(300, 100));
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.gridy = 0;
		add(enterButton, gbc);

		gbc.gridy = 1;
		add(quitButton, gbc);

		try {
			img = ImageIO.read(new File(name));
			this.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Times New Roman", Font.PLAIN, 60));
		g.drawString("A Hole In The Universe", 180, 120);
	}

}
