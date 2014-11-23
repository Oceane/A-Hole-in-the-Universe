package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;

import javax.swing.JPanel;

public class Player extends SpaceObject{
	public static final int RAD = 50;
	public static enum eType{
		MARS,
		VENUS,
	}
	
	
	public Player(Color uColor, JPanel uPanel, int x, int y){
		super(uColor, RAD, uPanel);
		this.rad = RAD;
		this.setBounds(x, y, RAD*2, RAD*2);
		this.setX(x);
		this.setY(y);
	}
	
	
	
}
