package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import finalProject.GUI.game.KeyWatcher;

public class Player extends SpaceObject{
	public static final int RAD = 20;
	public static final double ACCEL = 2;
	public static final double INIT_VEL = 0;
	public static enum eType{
		MARS,
		VENUS,
	}
	private boolean bUp, bDown, bLeft, bRight;
	private PowerUp uPowerUp;
	
	public Player(int x, int y, JPanel uPanel, JFrame uFrame){
		super(Color.BLUE, RAD, x, y, uPanel);
		//Add a keylistener to the frame to control player movement:
		uFrame.addKeyListener(new KeyWatcher(this));
	}
	
	public void setPowerUp(PowerUp uPowerUp){
		this.uPowerUp = uPowerUp;
	}
	
	public PowerUp getPowerUp(){
		return this.uPowerUp;
	}
	
	public boolean getPressedUp(){
		return this.bUp;
	}
	
	public void setPressedUp(boolean tf){
		this.bUp = tf;
	}
	
	public boolean getPressedDown(){
		return this.bDown;
	}
	
	public void setPressedDown(boolean tf){
		this.bDown = tf;
	}
	
	public boolean getPressedLeft(){
		return this.bLeft;
	}
	
	public void setPressedLeft(boolean tf){
		this.bLeft = tf;
	}
	
	public boolean getPressedRight(){
		return this.bRight;
	}
	
	public void setPressedRight(boolean tf){
		this.bRight = tf;
	}
	
}
