package edu.usc.finalProject;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// from http://stackoverflow.com/questions/616924/how-to-check-if-the-key-pressed-was-an-arrow-key-in-java-keylistener
public class KeyWatcher extends KeyAdapter {
	private SpaceObject myBall;
	
	public KeyWatcher(SpaceObject ball) {
		this.myBall = ball;
	}

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) { 
        case KeyEvent.VK_UP:
        	// handle up 
        	this.myBall.locationY-=10;
        	break;
        case KeyEvent.VK_DOWN:
        	// handle down 
        	this.myBall.locationY+=10;
        	break;
        case KeyEvent.VK_LEFT:
        	// handle left
        	this.myBall.locationX-=10;
        	break;
        case KeyEvent.VK_RIGHT :
        	// handle right
        	this.myBall.locationX+=10;
        	break;
        }
    }
}