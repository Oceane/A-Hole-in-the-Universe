package finalProject.GUI.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import finalProject.Client.Client;
import finalProject.GUI.game.SpaceObjects.Blackhole;
import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class GameUI {
	static final int WIDTH = 950;
	static final int HEIGHT = 650;
	static final Image BG_IMG = Toolkit.getDefaultToolkit().getImage("Icons/BackgroundGame2.jpg");  // from a friend of mine
	private final int OFFSET = 9;
	private final int NUM_COMETS = 5;
	private static Random rand;
	private Vector<SpaceObject> vObjs;
	private Client uClient;
	
	public static void main(String [] args) {
		GameUI myGame = new GameUI();
		JFrame frame = new JFrame("A Hole in the Universe");
		frame.setLayout(null);
		myGame.setJFrame(frame);
		myGame.setPlayground(frame);
		myGame.uClient = new Client();
		myGame.uClient.sendMsg("Hello Server, lovely day aint it?");
	}
	
	private void setJFrame(JFrame frame) {
		// initial setup
		frame.setSize(GameUI.WIDTH, GameUI.HEIGHT);
		frame.setLocation(20, 20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private void setPlayground(JFrame frame) {
		vObjs = new Vector<SpaceObject>();
		JPanel uPanel = new JPanel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				
				g.drawImage(BG_IMG, 0, 0, GameUI.WIDTH, GameUI.HEIGHT, null);
			}
		};
		uPanel.setBounds(0, 0, frame.getContentPane().getWidth() + OFFSET, frame.getContentPane().getHeight() + OFFSET);
		uPanel.setLayout(null);
		Player uPlayer = new Player("earth", 100, 100, uPanel, frame);
		vObjs.add(uPlayer);
		for(int i=0; i<NUM_COMETS; i++){
			vObjs.add(new Comet(uPanel));
		}
		frame.add(uPanel);
		SpaceObjectManager uObjMan = new SpaceObjectManager(vObjs, new Blackhole(vObjs, uPanel), new ScorePanel(uPanel), uPanel);
		PowerUpGenerator uPUGen = new PowerUpGenerator(vObjs, uPlayer, uPanel);
		CometGenerator uCMGen = new CometGenerator(vObjs, uPanel);
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 * from http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
	 */
	public static int randInt(int min, int max) {
	
	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    rand = new Random();
	
	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	
	    return randomNum;
	}
}