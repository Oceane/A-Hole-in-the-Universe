package finalProject.GUI.game;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class GameUI {
	private final int WIDTH = 800;
	private final int HEIGHT = 600;
	private Timer timer = new Timer();
	private static Random rand;
	private KeyWatcher kw;
	
	public static void main(String [] args) {
		GameUI myGame = new GameUI();
		JFrame frame = new JFrame("A Hole in the Universe");
		frame.setLayout(null);
//		JLayeredPane layeredPane = frame.getLayeredPane();
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
		JPanel uPanel = new JPanel();
		uPanel.setBounds(0, 0, frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
		uPanel.setBorder(new LineBorder(Color.GREEN));
		uPanel.setLayout(null);
		int width = uPanel.getWidth();
		int height = uPanel.getHeight();
		SpaceObject player = new Player(Color.BLUE, uPanel, WIDTH/2, HEIGHT/2);
		SpaceObject comet = new Comet(Color.RED, uPanel);
		uPanel.add(player);
		uPanel.add(comet);
		frame.add(uPanel);
		// pass player to KeyWatcher to control its speed
		this.kw = new KeyWatcher(player);
		frame.addKeyListener(kw);
		// for some reason pack has to be called for player.getWidth() to return non-zero values
//		frame.pack();
		SpaceObjectBouncingThread playerThread = new SpaceObjectBouncingThread(player, uPanel);
		SpaceObjectBouncingThread cometThread = new SpaceObjectBouncingThread(comet, uPanel);
		playerThread.start();
		cometThread.start();
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