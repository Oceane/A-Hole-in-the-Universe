package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class SpaceObject extends JPanel{
	private static final long serialVersionUID = 1L;
	private static final int OFFSET = 2;
	public static final double MAX_VEL = 20;
	public static final double MIN_VEL = 0.5;
	protected int rad;
	protected double x, y, velX, velY;
	protected JPanel uPanel;
	final int refreshRate = 30;
	private Color myColor;
	

	public SpaceObject(Color uColor, int rad, int x, int y, JPanel uPanel) {
		this.rad = rad;
		this.uPanel = uPanel;
		this.myColor = uColor;
		this.x = x;
		this.y = y;
		this.setBounds(x, y, rad*2 + OFFSET, rad*2 + OFFSET); //an offset is required to prevent clipping
//		this.setBorder(new LineBorder(Color.GREEN));
//		this.setBackground(Color.GREEN);
		this.setOpaque(false);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.myColor);
		//g.fillOval((this.getWidth() - this.rad*2)/2, (this.getHeight() - this.rad*2)/2, (rad*2), (rad*2));

	}
	
	public synchronized void updatePos(){
		this.x += this.velX;
		this.y += this.velY;
		this.setLocation((int)(this.x), (int)(this.y));
	}
	
	public synchronized double getCenterX(){
		//extra (this.getWidth() - this.rad*2)/2 term allows object smaller than its container to be centered
		return (this.x + this.rad + (this.getWidth() - this.rad*2)/2);
	}
	
	public synchronized void setCenterX(double x){
		//extra (this.getWidth() - this.rad*2)/2 term allows object smaller than its container to be centered
		this.x = x - this.rad - (this.getWidth() - this.rad*2)/2;
		this.setLocation((int)this.x, this.getLocation().y);
	}
	
	public synchronized double getCenterY(){
		//extra (this.getHeight() - this.rad*2)/2 term allows object smaller than its container to be centered
		return (this.y + this.rad + (this.getHeight() - this.rad*2)/2);
	}
	
	public synchronized void setCenterY(double y){
		//extra (this.getHeight() - this.rad*2)/2 term allows object smaller than its container to be centered
		this.y = y - this.rad - (this.getHeight() - this.rad*2)/2;
		this.setLocation(this.getLocation().x, (int)this.y);
	}
	
	public synchronized double getVelX(){
		return this.velX;
	}
	
	public synchronized void setVelX(double velX){
		this.velX = velX;
	}
	
	public synchronized double getVelY(){
		return this.velY;
	}
	
	public synchronized void setVelY(double velY){
		this.velY = velY;
	}
	
	public synchronized int getRad(){
		return this.rad;
	}
	
	public synchronized void setRad(int rad){
		this.rad = rad;
		this.setSize(new Dimension(this.rad*2, this.rad*2));
	}
	
	public synchronized void invertVelX(){
		this.velX = -this.velX;
	}
	
	public synchronized void invertVelY(){
		this.velY = -this.velY;
	}
	
	public synchronized void addVelX(double nAddVel){
		this.velX += nAddVel;
	}
	
	public synchronized void addVelY(double nAddVel){
		this.velY += nAddVel;
	}
	
	public synchronized double getTotVel(){
		return Math.sqrt(this.velX*this.velX + this.velY*this.velY);
	}
}
