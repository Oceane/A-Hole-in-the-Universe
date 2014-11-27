package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import finalProject.GUI.game.GameUI;

public class Powerup extends SpaceObject{
	public static final int SECONDS_PER_POWERUP = 20;

	public static final int RAD = 20;
	public static final int MAX_VEL = 4;
	public static final int MIN_VEL = 3;
	public static final int PL_RAD_LARGE = 50;
	public static final int PL_RAD_SMALL = 10;
	public static final int PL_ACCEL = 2;
	private Image img;
	
	public static enum eType{
		SPEED,
		ENLARGE,
		SHRINK,
		INVINCIBILITY,
	}
	private eType uType;
	
	public Powerup(JPanel uPanel){
		super(Color.GREEN, RAD, 0, 0, uPanel);
		this.uType = eType.values()[GameUI.randInt(0, eType.values().length-1)]; //generate a random powerup type
		int side = GameUI.randInt(1, 4);
		switch(side) {
			//  going in from top
			case 1:{
				this.setCenterY(0-this.rad);
				this.setCenterX(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getWidth()+this.rad)));
				this.setVelX(GameUI.randInt(-MAX_VEL, MAX_VEL));
				this.setVelY(GameUI.randInt(MIN_VEL, MAX_VEL));
				break;
			}
			// going in from left
			case 2:{
				this.setCenterY(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getHeight()+this.rad)));
				this.setCenterX(0-this.rad);
				this.setVelX(GameUI.randInt(MIN_VEL, MAX_VEL));
				this.setVelY(GameUI.randInt(-MAX_VEL, MAX_VEL));
				break;
			}
			// going in from bottom
			case 3:{
				this.setCenterY(this.rad+uPanel.getHeight());
				this.setCenterX(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getWidth()+this.rad)));
				this.setVelX(GameUI.randInt(-MAX_VEL, MAX_VEL));
				this.setVelY(-GameUI.randInt(MIN_VEL, MAX_VEL));
				break;
			}
			
			// going in from right
			case 4:{
				this.setCenterY(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getHeight()+this.rad)));
				this.setCenterX(this.rad+uPanel.getWidth());
				this.setVelX(-GameUI.randInt(MIN_VEL, MAX_VEL));
				this.setVelY(GameUI.randInt(-MAX_VEL, MAX_VEL));
				break;
			}
		}
		// panel general settings
		this.setBounds(0, 0, this.rad*2, this.rad*2);
		
		// load image
		this.img = Toolkit.getDefaultToolkit().getImage("Icons/PowerupGame.jpg");  // from https://lh4.googleusercontent.com/-dsEpTjgb8EE/AAAAAAAAAAI/AAAAAAAAAAA/swMctonVNEc/photo.jpg
		uPanel.add(this);
	}
	
	public eType getType(){
		return this.uType;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.rad*2, this.rad*2, null);
	}	
}
