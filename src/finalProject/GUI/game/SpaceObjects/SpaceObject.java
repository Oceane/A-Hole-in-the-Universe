package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.lang.reflect.Field;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import finalProject.GUI.game.GameUI;

public class SpaceObject extends JPanel{
	private static final long serialVersionUID = 1L;
	protected int rad, velX, velY;
	protected JPanel uPanel;
	final int refreshRate = 30;
	private Color myColor;

	public SpaceObject(Color uColor, int rad, JPanel uPanel) {
		this.rad = rad;
		this.uPanel = uPanel;
		this.myColor = uColor;
		this.setBorder(new LineBorder(Color.GREEN));
		this.setBackground(Color.GREEN);
		this.setOpaque(false);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.myColor);
		g.fillOval(0, 0, (rad*2), (rad*2));
	}
	
	public void updatePos(){
		this.setLocation(this.getLocation().x + this.getVelX(), this.getLocation().y + this.getVelY());
	}
	
	public int getX(){
		return (this.getLocation().x + this.rad);
	}
	
	public void setX(int x){
		this.setLocation(x - this.rad, this.getLocation().y);
	}
	
	public int getY(){
		return (this.getLocation().y + this.rad);
	}
	
	public void setY(int y){
		this.setLocation(this.getLocation().x, y - this.rad);
	}
	
	public int getVelX(){
		return this.velX;
	}
	
	public void setVelX(int velX){
		this.velX = velX;
	}
	
	public int getVelY(){
		return this.velY;
	}
	
	public void setVelY(int velY){
		this.velY = velY;
	}
	
	public int getRad(){
		return this.rad;
	}
	
	public void setRad(int rad){
		this.rad = rad;
	}
	
	public void invertVelX(){
		this.velX = -this.velX;
	}
	
	public void invertVelY(){
		this.velY = -this.velY;
	}
	
	public void addVelX(int nAddVel){
		this.velX += nAddVel;
	}
	
	public void addVelY(int nAddVel){
		this.velY += nAddVel;
	}
}
