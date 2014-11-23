package aHoleInTheUniverse;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Start extends JFrame {

	JButton enterButton, quitButton;
	BufferedImage img;
	String name = "Backgrounds/universe1.jpg";

	public Start() {
		// SETUP WINDOW
		window();

		addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == e.VK_ESCAPE) {
					System.exit(0);
				} else {
					new Title();
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

	private void window() {
		JPanel startPanel = new StartPanel();
		startPanel.setLayout(new GridBagLayout());

		try {
			img = ImageIO.read(new File(name));
			startPanel.setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		add(startPanel);
	}

	public class StartPanel extends JPanel {

		public StartPanel() {

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

			g.setFont(new Font("Times New Roman", Font.PLAIN, 36));
			g.drawString("Press anything to start, [esc] to quit", 240, 500);
		}
	}

	public static void main(String[] args) {
		new Start();

	}

}
