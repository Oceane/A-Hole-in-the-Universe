package finalProject.GUI.game;

import java.util.Vector;

import javax.swing.JPanel;

import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.Powerup;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class CometGenerator extends Thread {
	public static final int MAX_COMETS = 15;
	public static final int SECONDS_PER_COMET = 30;
	Vector<SpaceObject> vObjs;
	JPanel uPanel;
	
	public CometGenerator(Vector<SpaceObject> vObjs, JPanel uPanel){
		this.vObjs = vObjs;
		this.uPanel = uPanel;
		this.start();	
	}
	
	private int numComets(){
		int nCometCount = 0;
		synchronized(vObjs){
			for(SpaceObject uObj : vObjs){
				if(uObj instanceof Comet){
					nCometCount++;
				}
			}
		}
		return nCometCount;
	}
	
	public void run(){
		while(true){
			try {
				Thread.sleep(1000 * SECONDS_PER_COMET);  // milliseconds
				if(numComets() >= MAX_COMETS){
					break;
				}
				vObjs.add(new Comet(this.uPanel));
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
	
}
