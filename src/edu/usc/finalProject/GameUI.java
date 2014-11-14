package edu.usc.finalProject;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class GameUI {
	private final int width = 1600;
	private final int height = 1400;
	private Timer timer = new Timer();
	private static Random rand;
	private KeyWatcher kw;
	
	public static void main(String [] args) {
		GameUI myGame = new GameUI();
		JFrame frame = new JFrame("A Hole in the Universe");
//		JLayeredPane layeredPane = frame.getLayeredPane();
		myGame.setJFrame(frame);
		myGame.setPlayground(frame);
	}
	
	private void setJFrame(JFrame frame) {
		// initial setup
		frame.setSize(this.width,this.height);
		frame.setLocation(20, 20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setMinimumSize(new Dimension(800,600));
		frame.setVisible(true);
//		frame.setResizable(false);
	}
	
	private void setPlayground(JFrame frame) {
		SpaceObject player = new SpaceObject(50, 100, 100, 3, 4, "blue");
		frame.add(player);
		this.kw = new KeyWatcher(player);
		frame.addKeyListener(kw);
		// for some reason pack has to be called for player.getWidth() to return non-zero values
		frame.pack();
		SpaceObjectBouncingThread playerThread = new SpaceObjectBouncingThread(player, player.getWidth(), player.getHeight());
		playerThread.start();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				player.repaint();
			}
		}, 0, (long) (1000/player.refreshRate));
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