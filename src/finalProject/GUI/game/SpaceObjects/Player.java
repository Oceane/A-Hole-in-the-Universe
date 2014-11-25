package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import finalProject.GUI.game.KeyWatcher;

public class Player extends SpaceObject implements Runnable{
	public static final int RAD = 20;
	public static final int PU_RAD = 50;
	public static final double ACCEL = 0.5;
	public static final double INIT_VEL = 0;
	public static enum eType{
		MARS,
		VENUS,
	}
	private boolean bUp, bDown, bLeft, bRight;
	private PowerUp uPowerUp;
	private double dAccel;
	private double dInitVel;
	
	public Player(int x, int y, JPanel uPanel, JFrame uFrame){
		super(Color.BLUE, RAD, x, y, uPanel);
		this.dAccel = ACCEL;
		this.dInitVel = INIT_VEL;
		//Add a keylistener to the frame to control player movement:
		uFrame.addKeyListener(new KeyWatcher(this));
		new Thread(this).start();
	}
	
	public void setPowerUp(PowerUp uPowerUp){
		this.uPowerUp = uPowerUp;
		switch(this.uPowerUp.getType()){
		case SPEED:
			this.dAccel = PowerUp.PL_ACCEL;
			break;
		case ENLARGE:
			this.rad = PowerUp.PL_RAD_LARGE;
			this.setSize(new Dimension(this.rad*2, this.rad*2));
			break;
		case SHRINK:
			this.rad = PowerUp.PL_RAD_SMALL;
			this.setSize(new Dimension(this.rad*2, this.rad*2));
			break;
		case INVINCIBILITY:
			break;
		}
	}
	
	public PowerUp getPowerUp(){
		return this.uPowerUp;
	}
	
	public double getAccel(){
		return this.dAccel;
	}
	
	public void setAccell(double dAccel){
		this.dAccel = dAccel;
	}
	
	public double getInitVel(){
		return this.dInitVel;
	}
	
	public void setInitVel(double dInitVel){
		this.dInitVel = dInitVel;
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
	
	public void run(){
		while(true){
			if(this.uPowerUp != null){
				try {
					Thread.sleep(1000 * PowerUp.SECONDS_PER_POWERUP);  // milliseconds delay
				} catch (InterruptedException ex) {
					System.out.print(ex.getMessage());
				}
				//Delete the player's powerup and restore values:
				this.uPowerUp = null;
				this.rad = RAD;
				this.setSize(new Dimension(this.rad*2, this.rad*2));
			}
		}
	}
}
