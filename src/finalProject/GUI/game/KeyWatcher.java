package finalProject.GUI.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import finalProject.GUI.game.SpaceObjects.SpaceObject;

// from http://stackoverflow.com/questions/616924/how-to-check-if-the-key-pressed-was-an-arrow-key-in-java-keylistener
public class KeyWatcher extends KeyAdapter {
	private SpaceObject myBall;
	private boolean bUp, bDown, bLeft, bRight;

	public KeyWatcher(SpaceObject ball) {
		this.myBall = ball;
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case KeyEvent.VK_UP:
			// handle up 
			this.bUp = true;
			break;
		case KeyEvent.VK_DOWN:
			// handle down 
			this.bDown = true;
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			this.bLeft = true;
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			this.bRight = true;
			break;
		}
		if (this.bDown) {
			this.myBall.setVelY(this.myBall.getVelY() + 2);
		}
		if (this.bUp) {
			this.myBall.setVelY(this.myBall.getVelY() - 2);
		}
		if (this.bLeft) {
			this.myBall.setVelX(this.myBall.getVelX() - 2);
		}
		if (this.bRight) {
			this.myBall.setVelX(this.myBall.getVelX() + 2);
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case KeyEvent.VK_UP:
			// handle up 
			this.bUp = false;
			break;
		case KeyEvent.VK_DOWN:
			// handle down 
			this.bDown = false;
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			this.bLeft = false;
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			this.bRight = false;
			break;
		}
	}
}