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
	private Color myColor;
	protected SpaceObjectType type;
	
	
	// constructor 1. For objects with known parameters, like the player
	public SpaceObject(SpaceObjectType type, float radius, float locationX, float locationY, float speedX, float speedY, String colorString) {
		this.type = type;
		this.radius = radius;
		this.locationX = locationX;
		this.locationY = locationY;
		this.speedX = speedX;
		this.speedY = speedY;
		this.setBounds(0, 0, (int)radius*2, (int)radius*2);
		this.setOpaque(false);
		
		// get color
		try {
			Field field = Class.forName("java.awt.Color").getField(colorString);
			this.myColor = (Color)field.get(null);
		} catch (Exception e) {
			this.myColor = null; // Not defined
		}
	}
	
	// constructor 2. For objects with random parameters, like the comets
	public SpaceObject(SpaceObjectType type, String colorString, int frameWidth, int frameHeight) {
		this.type = type;
		this.radius = GameUI.randInt(20, 80);
		int side = GameUI.randInt(1, 4);
		switch(side) {
		//  going in from top
		case 1:{
			this.locationY = 0-this.radius;
			this.locationX = GameUI.randInt(0-(int)this.radius, (int) (frameWidth+this.radius));
			this.speedX = GameUI.randInt(-30, 30);
			this.speedY = GameUI.randInt(0, 30);
			break;
		}
		// going in from left
		case 2:{
			this.locationY = GameUI.randInt(0-(int)this.radius, (int) (frameHeight+this.radius));
			this.locationX = 0-this.radius;
			this.speedX = GameUI.randInt(0, 30);
			this.speedY = GameUI.randInt(-30, 30);
			break;
		}
		// going in from bottom
		case 3:{
			this.locationY = this.radius+frameHeight;
			this.locationX = GameUI.randInt(0-(int)this.radius, (int) (frameWidth+this.radius));
			this.speedX = GameUI.randInt(-30, 30);
			this.speedY = GameUI.randInt(-30, 0);
			break;
		}
		// going in from right
		case 4:{
			this.locationY = GameUI.randInt(0-(int)this.radius, (int) (frameHeight+this.radius));
			this.locationX = this.radius+frameWidth;
			this.speedX = GameUI.randInt(-30, 0);
			this.speedY = GameUI.randInt(-30, 30);
			break;
		}
		}
		
		// panel general settings
		this.setBounds(0, 0, (int)this.radius*2, (int)this.radius*2);
		this.setOpaque(false);
		// get color
		try {
			Field field = Class.forName("java.awt.Color").getField(colorString);
			this.myColor = (Color)field.get(null);
		} catch (Exception e) {
			this.myColor = null; // Not defined
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// get width and height of the panel to paint to black
		panelWidth = getWidth();
		panelHeight = getHeight();
//		g.setColor(Color.white);
//		g.fillRect(0, 0, panelWidth, panelHeight);
		g.setColor(this.myColor);
		g.fillOval(0, 0, (int)(radius*2), (int)(radius*2));
	}
	
	public int getMyWidth() {
		return panelWidth;
	}
	
	public int getMyHeight() {
		return panelHeight;
	}
}
