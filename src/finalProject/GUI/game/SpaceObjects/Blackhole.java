package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;

import javax.swing.JPanel;

public class BlackHole extends SpaceObject{
	public static final int RAD = 50;
	public static final double G = 0.0002;
	
	public BlackHole(JPanel uPanel){
		super(Color.BLACK, RAD, 0, 0, uPanel);	
		this.setCenterX(uPanel.getWidth()/2);
		this.setCenterY(uPanel.getHeight()/2);
	}
	
}
