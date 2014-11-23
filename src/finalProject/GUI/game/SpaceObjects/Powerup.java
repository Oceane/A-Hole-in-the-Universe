package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;

import javax.swing.JPanel;

import finalProject.GUI.game.GameUI;

public class PowerUp extends SpaceObject{
	public static final int RAD = 20;
	public static final int MAX_VEL = 2;
	public static final int MIN_VEL = 1;
	
	public PowerUp(JPanel uPanel){
		super(Color.GREEN, RAD, 0, 0, uPanel);
		
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
				this.setVelX(GameUI.randInt(-MAX_VEL, MIN_VEL));
				this.setVelY(GameUI.randInt(-MAX_VEL, MAX_VEL));
				break;
			}
		}
		// panel general settings
		this.setBounds(0, 0, this.rad*2, this.rad*2);
	}
	
}
