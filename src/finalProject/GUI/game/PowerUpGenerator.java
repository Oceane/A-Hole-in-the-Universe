package finalProject.GUI.game;

import java.util.Vector;

import javax.swing.JPanel;

import finalProject.GUI.game.SpaceObjects.PowerUp;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class PowerUpGenerator extends Thread{
	public static final int SECONDS_PER_POWERUP = 10;
	Vector<SpaceObject> vObjs;
	JPanel uPanel;
	
	public PowerUpGenerator(Vector<SpaceObject> vObjs, JPanel uPanel){
		this.vObjs = vObjs;
		this.uPanel = uPanel;
		this.start();
	}
	
	public boolean containsPowerUp(){
		for(int i=0; i<vObjs.size(); i++){
			if(vObjs.get(i) instanceof PowerUp){
				return true;
			}
		}
		return false;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(1000 * SECONDS_PER_POWERUP);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
			if(!containsPowerUp()){
				vObjs.add(new PowerUp(this.uPanel));
			}
		}
	}
}
