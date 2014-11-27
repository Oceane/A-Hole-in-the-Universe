package finalProject.GUI.game.SpaceObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;

import finalProject.GUI.game.GameUI;
import finalProject.GUI.game.SpaceObjectManager;

public class Blackhole extends SpaceObject implements Runnable{
	public static final int INNER_RAD = 15;
	public static final int OUTER_RAD = 50;
	public static final int MS_PER_SHRINK = 10;
	public static final Image IMG = Toolkit.getDefaultToolkit().getImage("Icons/BlackholeGame.png");
	public static final double G = 0.001;
	private Image img;
	private int nOuterRad;
	private Vector<SpaceObject> vObjs;
	private Vector<SpaceObject> vTrapped = new Vector<SpaceObject>();
	
	public Blackhole(Vector<SpaceObject> vObjs, JPanel uPanel){
		super(Color.BLACK, OUTER_RAD, 0, 0, uPanel); //size of component should be larger than radius
		this.setCenterX(uPanel.getWidth()/2);
		this.setCenterY(uPanel.getHeight()/2);
		this.rad = INNER_RAD;
		this.nOuterRad = OUTER_RAD;
		// load image
		this.img = IMG;  // from http://icons.iconarchive.com/icons/zairaam/bumpy-planets/256/blackhole-icon.png
		this.vObjs = vObjs;
		uPanel.add(this);
		new Thread(this).start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Note: the black hole image radius should be larger than the object radius,
		//because the inner radius should be used for object processing while the outer radius is used for the image.
		g.drawImage(this.img, 0, 0, this.nOuterRad*2, this.nOuterRad*2, null);
	}	
	
	public int getOuterRad(){
		return this.nOuterRad;
	}
	
	public void setOuterRad(int nOuterRad){
		this.nOuterRad = nOuterRad;
	}
	
	public void trapped(SpaceObject uObj){
		vTrapped.add(uObj);
	}
	
	public void run(){
		try {
			while(true){
				Thread.sleep(MS_PER_SHRINK);  // milliseconds
				synchronized(vTrapped){
					for(SpaceObject uObj : vTrapped){
						if(uObj.getRad() > 0){
							double x = uObj.getCenterX();
							double y = uObj.getCenterY();
							uObj.setRad(uObj.getRad() - 1); //decrease radius
							uObj.setCenterX(x); //ensure the center stays the same
							uObj.setCenterY(y);
							uObj.setCenterX(0.1*(this.getCenterX() - uObj.getCenterX()) + uObj.getCenterX());//move towards the center of the black hole
							uObj.setCenterY(0.1*(this.getCenterY() - uObj.getCenterY()) + uObj.getCenterY());
						}
						else if(uObj instanceof Player){
							vTrapped.remove(uObj);
							//This is a while loop to ensure the player is not generated over the black hole
							//which would cause points to be subtracted more than once
							uObj.setRad(Player.RAD); //reset the radius of the player
							uObj.setVelX(0); //make sure the velocity is zero
							uObj.setVelY(0);
							while(SpaceObjectManager.collision(uObj, this)) { //if randomly generated over the black hole, try again
								uObj.setCenterX(GameUI.randInt(uObj.getRad(), uPanel.getWidth()-uObj.getRad()));
								uObj.setCenterY(GameUI.randInt(uObj.getRad(), uPanel.getHeight()-uObj.getRad()));
							}
							vObjs.add(uObj); //add the player back into the vector of objects
							break;
						}
						else{
							vTrapped.remove(uObj);
							uPanel.remove(uObj);
							break;
						}
					}
				}
			}
		} catch (InterruptedException ex) {
			System.out.print(ex.getMessage());
		}
	}
}
