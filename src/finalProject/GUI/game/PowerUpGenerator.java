package finalProject.GUI.game;

import java.util.Vector;

import javax.swing.JPanel;

import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.Powerup;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class PowerUpGenerator extends Thread{
	public static final int SECONDS_PER_POWERUP = 1;
	Vector<SpaceObject> vObjs;
	Player uPlayer;
	JPanel uPanel;
	
	public PowerUpGenerator(Vector<SpaceObject> vObjs, Player uPlayer, JPanel uPanel){
		this.vObjs = vObjs;
		this.uPlayer = uPlayer;
		this.uPanel = uPanel;
		this.start();
	}
	
	public boolean powerUpExists(){
		//Check to see if the player has a powerup:
		if(uPlayer.getPowerUp() != null){
			return true;
		}
		//Check to see if 
		for(int i=0; i<vObjs.size(); i++){
			if(vObjs.get(i) instanceof Powerup){
				return true;
			}
		}
		return false;
	}
	
	public void run(){
		while(true){
			try {
				if(!powerUpExists()){
					Thread.sleep(1000 * SECONDS_PER_POWERUP);  // milliseconds
					vObjs.add(new Powerup(this.uPanel));
				}
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
}
