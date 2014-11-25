package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Blackhole extends SpaceObject{
	public static final int RAD = 50;
	public static final double G = 0.001;
	private Image img;
	
	public Blackhole(JPanel uPanel){
		super(Color.BLACK, RAD, 0, 0, uPanel);
		this.setCenterX(uPanel.getWidth()/2);
		this.setCenterY(uPanel.getHeight()/2);
		// load image
		this.img = Toolkit.getDefaultToolkit().getImage("Icons/BlackholeGame.png");  // from http://icons.iconarchive.com/icons/zairaam/bumpy-planets/256/blackhole-icon.png
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.rad*2, this.rad*2, null);
	}	
}
