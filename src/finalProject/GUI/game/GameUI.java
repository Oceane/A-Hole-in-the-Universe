package finalProject.GUI.game;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

public class GameUI {
	private final int width = 800;
	private final int height = 600;
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
		frame.setLayout(null);
		frame.setMinimumSize(new Dimension(800,600));
		frame.setVisible(true);
		frame.setBackground(Color.black);
//		frame.setResizable(false);
	}
	
	private void setPlayground(JFrame frame) {
		int width = frame.getContentPane().getWidth();
		int height = frame.getContentPane().getHeight();
		SpaceObject player = new SpaceObject(SpaceObjectType.planet,50, width/2, height/2, 0, 0, "blue");
		SpaceObject comet = new SpaceObject(SpaceObjectType.comet, "red", width, height);
		frame.add(player);
		frame.add(comet);
		// pass player to KeyWatcher to control its speed
		this.kw = new KeyWatcher(player);
		frame.addKeyListener(kw);
		// for some reason pack has to be called for player.getWidth() to return non-zero values
//		frame.pack();
		SpaceObjectBouncingThread playerThread = new SpaceObjectBouncingThread(player, width, height);
		SpaceObjectBouncingThread cometThread = new SpaceObjectBouncingThread(comet, width, height);
		playerThread.start();
		cometThread.start();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				player.setLocation((int)(player.locationX-player.radius), (int)(player.locationY-player.radius));
				comet.setLocation((int)(comet.locationX-comet.radius), (int)(comet.locationY-comet.radius));
//				frame.revalidate();
//				frame.repaint();
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