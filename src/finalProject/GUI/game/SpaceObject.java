package finalProject.GUI.game;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;

import javax.swing.JPanel;

public class SpaceObject extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected float radius, locationX, locationY, speedX, speedY;
	protected int panelWidth, panelHeight;
	final int refreshRate = 30;
	private static Color myColor;
	
	
	// constructor 1. For objects with known parameters, like the player
	public SpaceObject(float radius, float locationX, float locationY, float speedX, float speedY, String colorString) {
		this.radius = radius;
		this.locationX = locationX;
		this.locationY = locationY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.setBounds(0, 0, (int)radius*2, (int)radius*2);
		
		// get color
		try {
			Field field = Class.forName("java.awt.Color").getField(colorString);
			SpaceObject.myColor = (Color)field.get(null);
		} catch (Exception e) {
			SpaceObject.myColor = null; // Not defined
		}
	}
	
	// constructor 2. For objects with random parameters, like the comets
	public SpaceObject(String colorString) {
		this.radius = GameUI.randInt(1, 100);
		int side = GameUI.randInt(1, 4);
		switch(side) {
		//  going in from top
		case 1:{
			this.locationY = 0-this.radius;
			this.locationX = GameUI.randInt(-1*(int)this.radius, 180);
			break;
		}
		case 2:{
			break;
		}
		case 3:{
			break;
		}
		case 4:{
			break;
		}
		}
		// get color
		try {
			Field field = Class.forName("java.awt.Color").getField(colorString);
			SpaceObject.myColor = (Color)field.get(null);
		} catch (Exception e) {
			SpaceObject.myColor = null; // Not defined
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// get width and height of the panel to paint to black
		panelWidth = getWidth();
		panelHeight = getHeight();
//		g.setColor(Color.white);
//		g.fillRect(0, 0, panelWidth, panelHeight);
		g.setColor(SpaceObject.myColor);
		g.fillOval(0, 0, (int)(radius*2), (int)(radius*2));
	}
	
	public int getMyWidth() {
		return panelWidth;
	}
	
	public int getMyHeight() {
		return panelHeight;
	}
}
