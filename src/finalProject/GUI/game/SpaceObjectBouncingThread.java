package finalProject.GUI.game;

import javax.swing.JPanel;

import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class SpaceObjectBouncingThread extends Thread{
	public static final int REFRESH_RATE = 30;
	private SpaceObject myBall;
	private int boundX, boundY;  // these are the bounds of the panel that the SpaceObject is in
	private JPanel uPanel;
	
	
	public SpaceObjectBouncingThread(SpaceObject ball, JPanel uPanel) {
		this.myBall = ball;
		this.boundX = uPanel.getWidth();
		this.boundY = uPanel.getHeight();
		this.uPanel = uPanel;
	}
	
	public void run() {
		while(true) {
			// update position
			myBall.updatePos();
			
			// if comet, don't check bound
			if (myBall instanceof Player) {
				// check x bound
				if (myBall.getX()<0) {
					myBall.invertVelX();
				}
				else if (myBall.getX() + myBall.getRad()*2>boundX) {
					myBall.invertVelX();				
				}
				// check y bound
				if (myBall.getY()<0) {
					myBall.invertVelY();
				}
				else if (myBall.getY() + myBall.getRad()*2>boundY) {
					myBall.invertVelY();			
				}
			}
			uPanel.revalidate();
			uPanel.repaint();
			// delay
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
}
