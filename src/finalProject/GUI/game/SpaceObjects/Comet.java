package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

import finalProject.GUI.game.GameUI;

public class Comet extends SpaceObject{
	public static final int MAX_RAD = 30;
	public static final int MIN_RAD = 10;
	public static final int MAX_VEL = 1;
	public static final int MIN_VEL = 1;
	private Image img;
	
	public Comet(JPanel uPanel){
		super(Color.RED, 10, 0, 0, uPanel);
		
		double sign = GameUI.randInt(0, 1)*2 - 1; //either 1 or -1
		this.rad = GameUI.randInt(MIN_RAD, MAX_RAD);
		int side = GameUI.randInt(1, 4);
		switch(side) {
		//  going in from top
		case 1:{
			this.setCenterY(-this.rad);
			this.setCenterX(GameUI.randInt(0, (uPanel.getWidth()+2*this.rad))-this.rad);
			this.setVelX(sign*GameUI.randInt(MIN_VEL, MAX_VEL));
			this.setVelY(GameUI.randInt(MIN_VEL, MAX_VEL));
			break;
		}
		// going in from left
		case 2:{
			this.setCenterY(GameUI.randInt(0, (uPanel.getHeight()+2*this.rad))-this.rad);
			this.setCenterX(-this.rad);
			this.setVelX(GameUI.randInt(MIN_VEL, MAX_VEL));
			this.setVelY(sign*GameUI.randInt(MIN_VEL, MAX_VEL));
			break;
		}
		// going in from bottom
		case 3:{
			this.setCenterY(this.rad+uPanel.getHeight());
			this.setCenterX(GameUI.randInt(0, (uPanel.getWidth()+2*this.rad))-this.rad);
			this.setVelX(sign*GameUI.randInt(MIN_VEL, MAX_VEL));
			this.setVelY(-GameUI.randInt(MIN_VEL, MAX_VEL));
			break;
		}
		
		// going in from right
		case 4:{
			this.setCenterY(GameUI.randInt(0, (uPanel.getHeight()+2*this.rad))-this.rad);
			this.setCenterX(this.rad+uPanel.getWidth());
			this.setVelX(-GameUI.randInt(MIN_VEL, MAX_VEL));
			this.setVelY(sign*GameUI.randInt(MIN_VEL, MAX_VEL));
			break;
		}
		}
		
		// panel general settings
		this.repaint();
		this.setBounds(0, 0, this.rad*2, this.rad*2);
		// load image
		this.img = Toolkit.getDefaultToolkit().getImage("Icons/Comet.png");  // from http://findicons.com/icon/218935/mars02?id=218935
		uPanel.add(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.rad*2, this.rad*2, null);
	}
}
