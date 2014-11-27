package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import finalProject.GUI.game.KeyWatcher;

public class Player extends SpaceObject implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int RAD = 20;
	public static final double ACCEL = 0.3;
	public static final double INIT_VEL = 0;
	private String sCharacter;
	private boolean bUp, bDown, bLeft, bRight;
	private Powerup uPowerUp;
	private double dAccel;
	private double dInitVel;
	private Image img;
	private boolean invinsible;
	
	public Player(String sCharacter, int x, int y, JPanel uPanel, JFrame uFrame){
		super(Color.BLUE, RAD, x, y, uPanel);
		this.dAccel = ACCEL;
		this.dInitVel = INIT_VEL;
		this.sCharacter = sCharacter;
		//Add a keylistener to the frame to control player movement:
		uFrame.addKeyListener(new KeyWatcher(this));
		// load image
		this.img =  getCharacterImg(sCharacter);
		this.invinsible = false;
		uPanel.add(this);
		new Thread(this).start(); //start player thread after all other initialization
	}
	
	static Image getCharacterImg(String sCharacter){
		switch(sCharacter){
		case "earth":
			return Toolkit.getDefaultToolkit().getImage("Icons/EarthGame.png"); // from http://commons.wikimedia.org/wiki/File:Globe.png
		} 
		//Make earth the default image:
		return Toolkit.getDefaultToolkit().getImage("Icons/EarthGame.png");
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.rad*2, this.rad*2, null);
	}
	
	public void setPowerUp(Powerup uPowerUp){
		this.uPowerUp = uPowerUp;
		switch(this.uPowerUp.getType()){
		case SPEED:
			this.dAccel = Powerup.PL_ACCEL;
			break;
		case ENLARGE:
			this.rad = Powerup.PL_RAD_LARGE;
			this.setSize(new Dimension(this.rad*2, this.rad*2));
			break;
		case SHRINK:
			this.rad = Powerup.PL_RAD_SMALL;
			this.setSize(new Dimension(this.rad*2, this.rad*2));
			break;
		case INVINCIBILITY:
			this.invinsible = true;
			break;
		}
	}
	
	public Powerup getPowerUp(){
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
		try {	
			while(true){
				Thread.sleep(1000 * Powerup.SECONDS_PER_POWERUP);  // milliseconds delay
				if(this.uPowerUp != null){
					//Delete the player's powerup and restore values:
					this.uPowerUp = null;
					this.dAccel = ACCEL;
					this.invinsible = false;
					this.rad = RAD;
					this.setSize(new Dimension(this.rad*2, this.rad*2));
				}
			}
		} catch (InterruptedException ex) {
			System.out.print(ex.getMessage());
		}
	}
	
	public boolean getInvinsible() {
		return this.invinsible;
	}
}
