package finalProject.GUI.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import finalProject.GUI.game.SpaceObjects.BlackHole;
import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class GameUI {
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private final int OFFSET = 9;
	private final int NUM_COMETS = 10;
	private static Random rand;
	private Vector<SpaceObject> vObjs;
	
	public static void main(String [] args) {
		GameUI myGame = new GameUI();
		JFrame frame = new JFrame("A Hole in the Universe");
		frame.setLayout(null);
		myGame.setJFrame(frame);
		myGame.setPlayground(frame);
	}
	
	private void setJFrame(JFrame frame) {
		// initial setup
		frame.setSize(this.WIDTH, this.HEIGHT);
		frame.setLocation(20, 20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize(new Dimension(WIDTH, HEIGHT));
		frame.setBackground(Color.black);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	private void setPlayground(JFrame frame) {
		vObjs = new Vector<SpaceObject>();
		JPanel uPanel = new JPanel();
		uPanel.setBounds(0, 0, frame.getContentPane().getWidth() + OFFSET, frame.getContentPane().getHeight() + OFFSET);
		//uPanel.setBounds(0, 0, WIDTH, HEIGHT);
		uPanel.setBorder(new LineBorder(Color.GREEN));
		uPanel.setLayout(null);
		vObjs.add(new Player(100, 100, uPanel, frame));
		for(int i=0; i<NUM_COMETS; i++){
			vObjs.add(new Comet(uPanel));
		}
		frame.add(uPanel);
		SpaceObjectManger uObjMan = new SpaceObjectManger(vObjs, new BlackHole(uPanel), uPanel);
		PowerUpGenerator uPUGen = new PowerUpGenerator(vObjs, uPanel);
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