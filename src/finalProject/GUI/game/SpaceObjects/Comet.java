package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;

import javax.swing.JPanel;

import finalProject.GUI.game.GameUI;

public class Comet extends SpaceObject{

	public Comet(Color uColor, JPanel uPanel){
		super(uColor, 10, uPanel);
		
		this.rad = GameUI.randInt(20, 80);
		int side = GameUI.randInt(1, 4);
		switch(side) {
		//  going in from top
		case 1:{
			this.setY(0-this.rad);
			this.setX(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getWidth()+this.rad)));
			this.setVelX(GameUI.randInt(-30, 30));
			this.setVelY(GameUI.randInt(5, 30));
			break;
		}
		// going in from left
		case 2:{
			this.setY(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getHeight()+this.rad)));
			this.setX(0-this.rad);
			this.setVelX(GameUI.randInt(5, 30));
			this.setVelY(GameUI.randInt(-30, 30));
			break;
		}
		// going in from bottom
		case 3:{
			this.setY(this.rad+uPanel.getHeight());
			this.setX(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getWidth()+this.rad)));
			this.setVelX(GameUI.randInt(-30, 30));
			this.setVelY(GameUI.randInt(-30, -5));
			break;
		}
		
		// going in from right
		case 4:{
			this.setY(GameUI.randInt(0-(int)this.rad, (int) (uPanel.getHeight()+this.rad)));
			this.setX(this.rad+uPanel.getWidth());
			this.setVelX(GameUI.randInt(-30, -5));
			this.setVelY(GameUI.randInt(-30, 30));
			break;
		}
		}
		
		// panel general settings
		this.setBounds(0, 0, (int)this.rad*2, (int)this.rad*2);
		
		
	}
	
}
