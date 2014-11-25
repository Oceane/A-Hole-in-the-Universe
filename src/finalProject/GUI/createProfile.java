package finalProject.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class createProfile extends JFrame implements MouseListener {
	public JPanel namePanel = new JPanel();
	JLabel nameLabel1 = new JLabel();
	JLabel nameLabel2 = new JLabel();
	JLabel nameLabel3 = new JLabel();
	JTextPane nameTextPane = new JTextPane();
	String OUBioText = new String(), NBioText = new String(), EBioText = new String(), GBioText = new String();
	int selectedPlanet = 0; // 1=Ora Uhlsax, 2 = Neslaou, 3 = Earth, 4 = Gigolo
	JLabel submit = new JLabel(new ImageIcon("Icons/submit.png"));
	boolean playedSound = false;

	public JPanel planetsPanel = new JPanel();

	public createProfile() {
		setTitle("Create Profile");
		setSize(950, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		// set up the name panel
		createNamePanel();
		mainPanel.add(namePanel);
		mainPanel.add(Box.createGlue());

		// set up the choosing character panel
		createPlanetsPanel();
		mainPanel.add(planetsPanel);

		// submit button
		createSubmitButton();

		// add main panel
		add(mainPanel);

		setVisible(true);

		addMouseListener(this);
	}

	public void createNamePanel() {
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
		namePanel.add(Box.createHorizontalGlue());
		nameLabel1.setFont(getFont("Dutch", 60));
		nameLabel1.setText("     }");
		nameLabel2.setFont(getFont("Eternal", 90));
		nameLabel2.setText("Username");
		nameLabel3.setFont(getFont("Dutch", 60));
		nameLabel3.setText("{     ");
		namePanel.add(nameLabel1);
		namePanel.add(nameLabel2);
		namePanel.add(nameLabel3);
		namePanel.add(Box.createHorizontalGlue());
		this.namePanel.setLayout(new BoxLayout(this.namePanel, BoxLayout.X_AXIS));
		nameTextPaneConstraints();
		nameTextPane.setFont(getFont("Lydia", 36));
		nameTextPane.setMinimumSize(new Dimension(200, 50));
		nameTextPane.setBorder(BorderFactory.createLineBorder(Color.black));
		nameTextPane.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				String txt = nameTextPane.getText().replaceAll("\r", "");
				nameTextPane.setText(txt.replaceAll("\n", ""));
				nameTextPane.setText(nameTextPane.getText().replaceAll("[^a-zA-Z]+", ""));
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					submit();
				}
			}

			public void keyReleased(KeyEvent arg0) {
				String txt = nameTextPane.getText().replaceAll("\r", "");
				nameTextPane.setText(txt.replaceAll("\n", ""));
				nameTextPane.setText(nameTextPane.getText().replaceAll("[^a-zA-Z]+", ""));
			}

			public void keyTyped(KeyEvent e) {
				String txt = nameTextPane.getText().replaceAll("\r", "");
				nameTextPane.setText(txt.replaceAll("\n", ""));
				nameTextPane.setText(nameTextPane.getText().replaceAll("[^a-zA-Z]+", ""));
			}
		});
		JPanel namerPanel = new JPanel();
		namerPanel.setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		namerPanel.add(nameTextPane, g);
		this.namePanel.add(namePanel);
		this.namePanel.add(namerPanel);
	}

	public void createPlanetsPanel() {
		planetsPanel.setLayout(new GridLayout(2, 2));

		JPanel OU = new JPanel(); // OU Start
		OU.setLayout(new BoxLayout(OU, BoxLayout.Y_AXIS));
		JLabel OUPic = new JLabel(new ImageIcon("Icons/OU2.png"));
		OUPic.setAlignmentX(CENTER_ALIGNMENT);
		OU.add(OUPic);
		JLabel OUTitleLabel = new JLabel("Ora Uhlsax"); // title start
		OUTitleLabel.setFont(getFont("OTitle", 25).deriveFont(Font.ITALIC));
		OUTitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		OU.add(OUTitleLabel); // title done
		if (selectedPlanet == 1) {
			if (!playedSound) {
				playSound("OUSound.wav");
				playedSound = true;
			}
			OUTitleLabel.setFont(getFont("OTitle", 40).deriveFont(Font.BOLD)); // bold
																				// it
			JTextField OUBio = new JTextField();
			OUBio.setText(OUBioText);
			OUBio.setFont(getFont("OTitle", 20));
		}
		planetsPanel.add(OU); // OU Done

		// Neslaou Start
		JPanel Neslaou = new JPanel();
		Neslaou.setLayout(new BoxLayout(Neslaou, BoxLayout.Y_AXIS));
		JLabel nIcon = new JLabel(new ImageIcon("Icons/Neslaou.png"));
		nIcon.setAlignmentX(CENTER_ALIGNMENT);
		Neslaou.add(nIcon);
		JLabel nTitle = new JLabel("Neslaou");
		nTitle.setFont(getFont("Neslaou", 35).deriveFont(Font.ITALIC));
		nTitle.setAlignmentX(CENTER_ALIGNMENT);
		Neslaou.add(nTitle);
		if (selectedPlanet == 2) {
			if (!playedSound) {
				playSound("NSound.mp3");
				playedSound = true;
			}
			nTitle.setFont(getFont("Neslaou", 60).deriveFont(Font.BOLD)); // bold
																			// it
			JTextField NBio = new JTextField();
			NBio.setText(NBioText);
			NBio.setFont(getFont("Neslaou", 20));
		}
		planetsPanel.add(Neslaou); // Neslaou End

		JPanel Earth = new JPanel(); // Earth Start
		Earth.setLayout(new BoxLayout(Earth, BoxLayout.Y_AXIS));
		JLabel eIcon = new JLabel(new ImageIcon("Icons/Earth.png"));
		JLabel eTitle = new JLabel("Earth circa 2556 A.D.");
		eTitle.setFont(getFont("Earth2", 25).deriveFont(Font.ITALIC));
		eIcon.setAlignmentX(CENTER_ALIGNMENT);
		eTitle.setAlignmentX(CENTER_ALIGNMENT);
		Earth.add(eIcon);
		Earth.add(eTitle);
		if (selectedPlanet == 3) {
			eTitle.setFont(getFont("Earth2", 40).deriveFont(Font.BOLD)); // bold
																			// it
			JTextField EBio = new JTextField();
			EBio.setText(EBioText);
			EBio.setFont(getFont("Earth2", 20));
		}
		planetsPanel.add(Earth); // Earth End

		// Gigolo Start
		JPanel Gigolo = new JPanel();
		Gigolo.setLayout(new BoxLayout(Gigolo, BoxLayout.Y_AXIS));
		JLabel gIcon = new JLabel(new ImageIcon("Icons/Gigolo.png"));
		JLabel gTitle = new JLabel("Gigolo");
		gTitle.setFont(getFont("Gigolo", 25).deriveFont(Font.ITALIC));
		gIcon.setAlignmentX(CENTER_ALIGNMENT);
		gTitle.setAlignmentX(CENTER_ALIGNMENT);
		if (selectedPlanet == 4) {
			gTitle.setFont(getFont("Gigolo", 40).deriveFont(Font.BOLD)); // bold
																			// it
			JTextField GBio = new JTextField();
			GBio.setText(GBioText);
			GBio.setFont(getFont("Gigolo", 20));
		}
		Gigolo.add(gIcon);
		Gigolo.add(gTitle);
		planetsPanel.add(Gigolo);
	}

	public Font getFont(String fontName, int size) {
		File fontFile = new File("Fonts/" + fontName + ".ttf");
		Font fawnt = new Font("Helvetica", 0, 36);
		try {
			fawnt = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Font f = fawnt.deriveFont(Font.PLAIN, size);
		return f;
	}

	public void nameTextPaneConstraints() {
		nameTextPane.setSize(750, 100);
		nameTextPane.setPreferredSize(new Dimension(750, 50));
	}

	public void createSubmitButton() {
		submit.setAlignmentX(CENTER_ALIGNMENT);
		this.namePanel.add(submit);
	}

	public void submit() {
		String Benutzername = nameTextPane.getText();
		if (Benutzername.length() <= 0) {
			JOptionPane.showMessageDialog(this, "Your username must have at least one character.", "Oh, come on...", JOptionPane.WARNING_MESSAGE);
		} else if (Benutzername.length() > 16) {
			JOptionPane.showMessageDialog(this, "Your username must be shorter than 16 characters.", "Now hold on there...", JOptionPane.WARNING_MESSAGE);
		} else if (selectedPlanet == 0) {
			JOptionPane.showMessageDialog(this, "You must select a planet.", "Hold your horses...", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this, Benutzername + ", your character has been saved!");
			// send data to XML file
			new JoinGameGUI();
			this.dispose();
		}
	}

	public static void main(String[] args) {
		new createProfile();
	}

	public void mouseClicked(MouseEvent arg0) {

	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}

	public void mouseReleased(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		if (x >= 142 && x <= 341) {
			if (y >= 172 && y <= 398) {
				if (selectedPlanet != 1) {
					selectedPlanet = 1;
					playedSound = false;
				}
				planetsPanel.removeAll();
				createPlanetsPanel();
				validate();
				repaint();
			}
		} else if (x >= 545 && x <= 866) {
			if (y >= 170 && y <= 378) {
				selectedPlanet = 2;
				planetsPanel.removeAll();
				createPlanetsPanel();
				validate();
				repaint();
			}
		}
		if (x >= 100 && x <= 377) {
			if (y >= 402 && y <= 634) {
				selectedPlanet = 3;
				planetsPanel.removeAll();
				createPlanetsPanel();
				validate();
				repaint();
			}
		} else if (x >= 558 && x <= 856) {
			if (y >= 399 && y <= 629) {
				selectedPlanet = 4;
				planetsPanel.removeAll();
				createPlanetsPanel();
				validate();
				repaint();
			}
		}
		if (x >= 839 && x <= 934) {
			if (y >= 52 && y <= 155) {
				submit();
			}
		}
	}

	public static synchronized void playSound(final String url) {
		try {
			File yourFile;
			AudioInputStream stream;
			AudioFormat format;
			DataLine.Info info;
			Clip clip;

			stream = AudioSystem.getAudioInputStream(new File("Sounds/" + url));
			format = stream.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
			clip.start();
		} catch (Exception e) {
			// whatevers
		}
	}
}
