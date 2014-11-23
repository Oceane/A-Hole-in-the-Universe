package finalProject.GUI.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

// from http://stackoverflow.com/questions/616924/how-to-check-if-the-key-pressed-was-an-arrow-key-in-java-keylistener
public class KeyWatcher extends KeyAdapter{
	private Player uPlayer;

	public KeyWatcher(Player uPlayer) {
		this.uPlayer = uPlayer;
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case KeyEvent.VK_UP:
			// handle up 
			this.uPlayer.setPressedUp(true);
			break;
		case KeyEvent.VK_DOWN:
			// handle down 
			this.uPlayer.setPressedDown(true);
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			this.uPlayer.setPressedLeft(true);
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			this.uPlayer.setPressedRight(true);
			break;
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch( keyCode ) {
		case KeyEvent.VK_UP:
			// handle up 
			this.uPlayer.setPressedUp(false);
			break;
		case KeyEvent.VK_DOWN:
			// handle down 
			this.uPlayer.setPressedDown(false);
			break;
		case KeyEvent.VK_LEFT:
			// handle left
			this.uPlayer.setPressedLeft(false);
			break;
		case KeyEvent.VK_RIGHT :
			// handle right
			this.uPlayer.setPressedRight(false);
			break;
		}
	}
}