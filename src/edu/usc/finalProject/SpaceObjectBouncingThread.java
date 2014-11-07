package edu.usc.finalProject;

public class SpaceObjectBouncingThread extends Thread{
	private SpaceObject myBall;
	private int boundX, boundY;  // these are the bounds of the panel that the SpaceObject is in
	
	
	public SpaceObjectBouncingThread(SpaceObject ball, int boundX, int boundY) {
		this.myBall = ball;
		this.boundX = boundX;
		this.boundY = boundY;
	}
	
	public void run() {
		while(true) {
			// update position
//			myBall.locationX += myBall.speedX;
//			myBall.locationY += myBall.speedY;
			
			// check x bound
			if (myBall.locationX-myBall.radius<0) {
				myBall.speedX = -myBall.speedX;
				myBall.locationX = myBall.radius;
			}
			else if (myBall.locationX+myBall.radius>boundX) {
				myBall.speedX = -myBall.speedX;
				myBall.locationX = boundX-myBall.radius;
			}
			
			// check y bound
			if (myBall.locationY-myBall.radius<0) {
				myBall.speedY = -myBall.speedY;
				myBall.locationY = myBall.radius;
			}
			else if (myBall.locationY+myBall.radius>boundY) {
				myBall.speedY = -myBall.speedY;
				myBall.locationY = boundY-myBall.radius;
			}
			
			// delay
			try {
				Thread.sleep(1000/myBall.refreshRate);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
}
