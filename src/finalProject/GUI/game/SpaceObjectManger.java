package finalProject.GUI.game;

import java.util.Vector;
import javax.swing.JPanel;
import finalProject.GUI.game.SpaceObjects.Blackhole;
import finalProject.GUI.game.SpaceObjects.Comet;
import finalProject.GUI.game.SpaceObjects.Player;
import finalProject.GUI.game.SpaceObjects.Powerup;
import finalProject.GUI.game.SpaceObjects.SpaceObject;

public class SpaceObjectManger extends Thread{
	public static final int REFRESH_RATE = 40;
	private Vector<SpaceObject> vObjs;
	private Blackhole uBlackHole;
	private ScorePanel uScorePanel;
	private JPanel uPanel;
	
	public SpaceObjectManger(Vector<SpaceObject> vObjs, Blackhole uBlackHole, ScorePanel uScorePanel, JPanel uPanel){
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
			if(uObj.getVelY() < uObj.getInitVel()){
				uObj.setVelY(uObj.getInitVel());
			}
			uObj.addVelY(uObj.getAccel());
		}
		if (uObj.getPressedUp()) {
			if(uObj.getVelY() > -uObj.getInitVel()){
				uObj.setVelY(-uObj.getInitVel());
			}
			uObj.addVelY(-uObj.getAccel());
		}
		if (uObj.getPressedLeft()) {
			if(uObj.getVelX() > -uObj.getInitVel()){
				uObj.setVelX(-uObj.getInitVel());
			}
			uObj.addVelX(-uObj.getAccel());
		}
		if (uObj.getPressedRight()) {
			if(uObj.getVelX() < uObj.getInitVel()){
				uObj.setVelX(uObj.getInitVel());
			}
			uObj.addVelX(uObj.getAccel());
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
		double mass;
		double mass2;
		double massSum;
		double massPercent;
		double massPercent2;
		double velSum;
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
				mass = uObj.getRad()*uObj.getRad()*uObj.getRad();
				mass2 = uObj2.getRad()*uObj2.getRad()*uObj2.getRad();
				massSum = mass + mass2;
				massPercent = mass / massSum;
				massPercent2 = 1 - massPercent;
				massPercent = 1;
				velSum = velTot + velTot2;
				uObj.setVelX(massPercent2 * velSum * refVelX);
				uObj.setVelY(massPercent2 * velSum * refVelY);
				uObj2.setVelX(massPercent * velSum * refVelX2);
				uObj2.setVelY(massPercent * velSum * refVelY2);
					while(collision(uObj, uObj2)){ //update positions until the objects are not overlapping:
						uObj.updatePos();
						uObj2.updatePos();
					}
				}
			}
		}
	}
	
	private void collectPowerUp(Player uObj){
		Powerup uPowerUp;
		for(int i=0; i<vObjs.size(); i++){
			if(vObjs.get(i) instanceof Powerup){
				uPowerUp = (Powerup)vObjs.get(i);
				//Remove powerup from the vector:
				if(collision(uObj, uPowerUp)){
					//Add powerup to the player:
					uObj.setPowerUp(uPowerUp);
					//Remove powerup from vector:
					uPanel.remove(uPowerUp);
					vObjs.remove(uPowerUp);
					break;
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
	
	private void blackHoleEatsPlayer(Player uObj) {
		if(collision(uObj, uBlackHole)){
			uScorePanel.add(100 * uObj.getRad()); //player can inflict damage on own blackhole
		}
		//This is a while loop to ensure the player is not generated over the black hole
		//which would cause points to be subtracted more than once
		while(collision(uObj, uBlackHole)) {
			uObj.setCenterX(GameUI.randInt(uObj.getRad(), uPanel.getWidth()-uObj.getRad()));
			uObj.setCenterY(GameUI.randInt(uObj.getRad(), uPanel.getHeight()-uObj.getRad()));
			uObj.setVelX(0);
			uObj.setVelY(0);
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
					if (!((Player)uObj).getInvinsible()) {
						blackHoleEatsPlayer((Player)uObj);
					}
					bounceOffBorders(uObj);
					collectPowerUp((Player)uObj);
					bounceOffObjects(uObj);
				}
				//Process comet:
				if(uObj instanceof Comet){
					bounceOffObjects(uObj);
					blackHoleEatsComet(uObj);
					if(isOffEdge(uObj)){
						uPanel.remove(uObj);
						vObjs.remove(uObj);
						vObjs.add(new Comet(uPanel));
					}
				}
				//Process powerup:
				if(uObj instanceof Powerup){
					if(isOffEdge(uObj)){
						uPanel.remove(uObj);
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
			
			//Send the black hole to the back:
			uPanel.setComponentZOrder(uBlackHole, uPanel.getComponents().length - 1);
			
			// Delay to get the correct frame rate:
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
}
