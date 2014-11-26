package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;

public class Blackhole extends SpaceObject{
	public static final int INNER_RAD = 15;
	public static final int OUTER_RAD = 50;
	public static final double G = 0.001;
	private Image img;
	
	public Blackhole(JPanel uPanel){
		super(Color.BLACK, OUTER_RAD, 0, 0, uPanel); //size of component should be larger than radius
		this.setCenterX(uPanel.getWidth()/2);
		this.setCenterY(uPanel.getHeight()/2);
		this.rad = INNER_RAD;
		// load image
		this.img = Toolkit.getDefaultToolkit().getImage("Icons/BlackholeGame.png");  // from http://icons.iconarchive.com/icons/zairaam/bumpy-planets/256/blackhole-icon.png
		uPanel.add(this);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Note: the black hole image radius should be larger than the object radius,
		//because the inner radius should be used for object processing while the outer radius is used for the image.
		g.drawImage(this.img, 0, 0, OUTER_RAD*2, OUTER_RAD*2, null);
	}	
}
