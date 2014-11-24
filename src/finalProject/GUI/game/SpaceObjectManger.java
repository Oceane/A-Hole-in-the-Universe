package finalProject.GUI.game;

import java.util.Vector;
import javax.swing.JPanel;
import finalProject.GUI.game.SpaceObjects.Blackhole;
import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.PowerUp;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class SpaceObjectManger extends Thread{
	public static final int REFRESH_RATE = 40;
	private Vector<SpaceObject> vObjs;
	private SpaceObject uBlackHole;
	private ScorePanel uScorePanel;
	private JPanel uPanel;
	
	public SpaceObjectManger(Vector<SpaceObject> vObjs, SpaceObject uBlackHole, ScorePanel uScorePanel, JPanel uPanel){
		this.vObjs = vObjs;
		this.uBlackHole = uBlackHole;
		this.uScorePanel = uScorePanel;
		this.uPanel = uPanel;
		this.start();
	}
	
	private boolean collision(SpaceObject Obj1, SpaceObject Obj2){
		double nDistance = distance(Obj1, Obj2);
		double nRadSum = Obj1.getRad() + Obj2.getRad();
		if(nDistance < nRadSum){
			return true;
		}
		return false;
	}
	
	private double distance(SpaceObject Obj1, SpaceObject Obj2){
		double disX = Obj2.getCenterX() - Obj1.getCenterX();
		double disY = Obj2.getCenterY() - Obj1.getCenterY();
		return Math.sqrt(disX*disX + disY*disY);
	}
	
	private void processInputPlayer(Player uObj){
		if (uObj.getPressedDown()) {
			if(uObj.getVelY() < Player.INIT_VEL){
				uObj.setVelY(Player.INIT_VEL);
			}
			uObj.addVelY(Player.ACCEL);
		}
		if (uObj.getPressedUp()) {
			if(uObj.getVelY() > -Player.INIT_VEL){
				uObj.setVelY(-Player.INIT_VEL);
			}
			uObj.addVelY(-Player.ACCEL);
		}
		if (uObj.getPressedLeft()) {
			if(uObj.getVelX() > -Player.INIT_VEL){
				uObj.setVelX(-Player.INIT_VEL);
			}
			uObj.addVelX(-Player.ACCEL);
		}
		if (uObj.getPressedRight()) {
			if(uObj.getVelX() < Player.INIT_VEL){
				uObj.setVelX(Player.INIT_VEL);
			}
			uObj.addVelX(Player.ACCEL);
		}
	}
	
	private void bounceOffBorders(SpaceObject uObj){
		//Left boundary:
		if (uObj.getCenterX() - uObj.getRad()<0) {
			uObj.invertVelX();
			uObj.setCenterX(uObj.getRad());
		}
		//Right boundary:
		else if (uObj.getCenterX() + uObj.getRad()>uPanel.getWidth()) {
			uObj.invertVelX();
			uObj.setCenterX(uPanel.getWidth() - uObj.getRad());
		}
		//Top boundary:
		if (uObj.getCenterY() - uObj.getRad()<0) {
			uObj.invertVelY();
			uObj.setCenterY(uObj.getRad());
		}
		//Bottom boundary:
		else if (uObj.getCenterY() + uObj.getRad()>uPanel.getHeight()) {
			uObj.invertVelY();
			uObj.setCenterY(uPanel.getHeight() - uObj.getRad());
		}
	}
	
	private boolean isOffEdge(SpaceObject uObj){
		
		//Left boundary:
		if (uObj.getCenterX() + uObj.getRad()<0) {
			return true;
		}
		//Right boundary:
		else if (uObj.getCenterX() - uObj.getRad()>uPanel.getWidth()) {
			return true;		
		}
		//Top boundary:
		else if (uObj.getCenterY() + uObj.getRad()<0) {
			return true;
		}
		//Bottom boundary:
		else if (uObj.getCenterY() - uObj.getRad()>uPanel.getHeight()) {
			return true;
		}
		return false;
	}
	
	private void bounceOffObjects(SpaceObject uObj){
		double disToMove;
		double massRatio;
		//Obj:
		double normX;
		double normY;
		double normLength;
		double normUnitX;
		double normUnitY;
		double velX;
		double velY;
		double velTot;
		double velUnitX;
		double velUnitY;
		double NdotI;
		double refVelX;
		double refVelY;
		//Obj2:
		double normUnitX2;
		double normUnitY2;
		double velX2;
		double velY2;
		double velTot2;
		double velUnitX2;
		double velUnitY2;
		double NdotI2;
		double refVelX2;
		double refVelY2;
		SpaceObject uObj2;
		for(int i=0; i<vObjs.size(); i++){
			//Get all objects except the current object:
			if(vObjs.get(i) != uObj){
				//Detect collisions:
				if(collision(uObj, vObjs.get(i))){
				uObj2 = vObjs.get(i);
				//Reflect object off any objects it has collided with: R = 2 * N * (N dot I) - I
				//Normal obj:
				normX = uObj.getCenterX() - uObj2.getCenterX();
				normY = uObj.getCenterY() - uObj2.getCenterY();
				normLength = Math.sqrt(normX*normX + normY*normY);
				if(normLength == 0){
					continue;
				}
				normUnitX = normX / normLength;
				normUnitY = normY / normLength;
				//Incident obj:
				velX = uObj.getVelX();
				velY = uObj.getVelY();
				velTot = Math.sqrt(velX*velX + velY * velY);
				if(velTot == 0){
					continue;
				}
				velUnitX = velX / velTot;
				velUnitY = velY / velTot;
				NdotI = -Math.abs(velUnitX * normUnitX + velUnitY * normUnitY);
				refVelX = -(2*normUnitX*NdotI - velUnitX);
				refVelY = -(2*normUnitY*NdotI - velUnitY);
				//Normal obj2:
				normUnitX2 = -normUnitX;
				normUnitY2 = -normUnitY;
				//Incident obj2:
				velX2 = uObj2.getVelX();
				velY2 = uObj2.getVelY();
				velTot2 = Math.sqrt(velX2*velX2 + velY2 * velY2);
				if(velTot2 == 0){
					continue;
				}
				velUnitX2 = velX2 / velTot2;
				velUnitY2 = velY2 / velTot2;
				NdotI2 = -Math.abs(velUnitX2 * normUnitX2 + velUnitY2 * normUnitY2);
				refVelX2 = -(2*normUnitX2*NdotI2 - velUnitX2);
				refVelY2 = -(2*normUnitY2*NdotI2 - velUnitY2);
				//Move the objects so they are no longer intersecting:
				disToMove = ((uObj.getRad()+uObj2.getRad()) - distance(uObj, uObj2)) / 2; //overlap between the objects
				uObj.setCenterX(uObj.getCenterX() - normUnitX*disToMove);
				uObj.setCenterY(uObj.getCenterY() - normUnitY*disToMove);
				uObj2.setCenterX(uObj2.getCenterX() - normUnitX2*disToMove);
				uObj2.setCenterY(uObj2.getCenterY() - normUnitY2*disToMove);
				//Calculate new velocities:
				//massRatio = uObj.getRad() / uObj2.getRad();
				massRatio = 1;
				uObj.setVelX(velTot2 * refVelX / massRatio);
				uObj.setVelY(velTot2 * refVelY / massRatio);
				uObj2.setVelX(velTot * refVelX2 * massRatio);
				uObj2.setVelY(velTot * refVelY2 * massRatio);
				while(collision(uObj, uObj2)){
					vObjs.get(i).updatePos();
				}
				// Delay to get the correct frame rate:
				/*
				try {
					Thread.sleep(1000);  // milliseconds
				} catch (InterruptedException ex) {
					System.out.print(ex.getMessage());
				}
				*/
				}
			}
		}
	}
	
	private void collectPowerUp(SpaceObject uObj){
		for(int i=0; i<vObjs.size(); i++){
			if(vObjs.get(i) instanceof PowerUp){
				PowerUp uPowerUp = (PowerUp)vObjs.get(i);
				if(collision(uObj, uPowerUp)){
					uPanel.remove(uPowerUp);
					vObjs.remove(uPowerUp);
				}
			}
		}
	}
	
	private void accellToBlackHole(SpaceObject uObj){
		double disX = uBlackHole.getCenterX() - uObj.getCenterX();
		double disY = uBlackHole.getCenterY() - uObj.getCenterY();
		double disTot = Math.sqrt(disX*disX + disY*disY);
		double disXUnit = disX / disTot;
		double disYUnit = disY / disTot;
		if(disTot != 0){
			uObj.addVelX((disXUnit * Blackhole.G * (double)uObj.getRad() / disTot*disTot));
			uObj.addVelY((disYUnit * Blackhole.G * (double)uObj.getRad()/ disTot*disTot));
		}
	}
	
	private void blackHoleEatsPlayer(SpaceObject playerObj) {
		if (collision(playerObj, uBlackHole)) {
			playerObj.setCenterX(GameUI.randInt(playerObj.getRad(), uPanel.getWidth()-playerObj.getRad()));
			playerObj.setCenterY(GameUI.randInt(playerObj.getRad(), uPanel.getHeight()-playerObj.getRad()));
			playerObj.setVelX(0);
			playerObj.setVelY(0);
			uScorePanel.subtract(10000);
		}
	}
	
	private void blackHoleEatsComet(SpaceObject cometObj) {
		if (collision(cometObj, uBlackHole)) {
			uPanel.remove(cometObj);
			vObjs.remove(cometObj);
			vObjs.add(new Comet(uPanel));
			uScorePanel.add(100*cometObj.getRad());
		}
	}
	
	public void run() {
		SpaceObject uObj;
		
		while(true) {
			for(int i=0; i<vObjs.size(); i++){
				uObj = vObjs.get(i);
				//Process player:
				if(uObj instanceof Player){
					processInputPlayer((Player)uObj);
					blackHoleEatsPlayer(uObj);
					bounceOffBorders(uObj);
					collectPowerUp(uObj);
					bounceOffObjects(uObj);
				}
				//Process comet:
				if(uObj instanceof Comet){
					bounceOffObjects(uObj);
					blackHoleEatsComet(uObj);
					if(isOffEdge(uObj)){
						vObjs.remove(uObj);
						vObjs.add(new Comet(uPanel));
					}
				}
				//Process powerup:
				if(uObj instanceof PowerUp){
					if(isOffEdge(uObj)){
						vObjs.remove(uObj);
					}
				}
				//Accelerate all objects towards the black hole:
				accellToBlackHole(uObj);
			}
			
			//After all calculations, update positions of objects simultaneously:
			for(int i=0; i<vObjs.size(); i++){
				//Update the position of the object and repaint:
				vObjs.get(i).updatePos();
				uPanel.revalidate();
				uPanel.repaint();
			}
			
			// Delay to get the correct frame rate:
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
}
