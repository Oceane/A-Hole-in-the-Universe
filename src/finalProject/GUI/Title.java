package finalProject.GUI;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Title extends JFrame {

	BufferedImage img;
	String name = "Backgrounds/universe5.jpg";
	JButton aboutButton, helpButton, createButton;
	CardLayout cardLayout = new CardLayout();
	JPanel cardPanel;
	JPanel titlePanel, aboutPanel, helpPanel;

	// CONSTRUCTOR
	public Title() {

		// INSTANTIATE PANELS
		cardPanel = new JPanel();
		cardPanel.setLayout(cardLayout);
		titlePanel = new TitlePanel();
		aboutPanel = new About(cardPanel);
		helpPanel = new Help(cardPanel);

		// CARDLAYOUT
		cardPanel.add(titlePanel, "titlePanel");
		cardPanel.add(aboutPanel, "aboutPanel");
		cardPanel.add(helpPanel, "helpPanel");

		// TITLE PAGE PANEL
		titlePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// buttons
		createButton = new JButton("Start");
		createButton.setPreferredSize(new Dimension(300, 100));
		createButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				createProfile.main(null);
			}
		});

		helpButton = new JButton("Help");
		helpButton.setPreferredSize(new Dimension(300, 100));
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "helpPanel");
			}
		});
		aboutButton = new JButton("About");
		aboutButton.setPreferredSize(new Dimension(300, 100));
		aboutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				cardLayout.show(cardPanel, "aboutPanel");
			}
		});

		// buttons layout
		gbc.weightx = 0;
		titlePanel.add(createButton, gbc);
		gbc.gridy = 1;
		titlePanel.add(helpButton, gbc);
		gbc.gridy = 2;
		titlePanel.add(aboutButton, gbc);

		add(cardPanel);

		// KEY LISTENER
		setFocusable(true);

		addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == e.VK_BACK_SPACE || key == e.VK_ESCAPE) {
					new Start();
					dispose();
				}

			}

			public void keyReleased(KeyEvent e) {

			}

			public void keyTyped(KeyEvent e) {

			}

		});

		setTitle("A Hole In The Universe");
		setSize(950, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public class TitlePanel extends JPanel {

		public TitlePanel() {

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
		}
	}

}
