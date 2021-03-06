package finalProject.GUI.game;

import java.awt.Color;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import finalProject.Client.Client;
import finalProject.GUI.scoreboard;

public class ScorePanel extends JPanel implements Runnable{
	public static final int HEIGHT = 50;
	public static final Color uSemiTrans = new Color(0, 0, 0, 150);
	private JFrame uFrame;
	//nScore, nComets, nDeaths, nPowerUps, nMaxSpin, nMaxVel
	private int nScore = 0;
	private int nComets = 0;
	private int nDeaths = 0;
	private int nPowerUps = 0;
	private int nMaxSpin = 0;
	private int nMaxVel = -1;
	private Vector<String> vEnemyUsernames = new Vector<String>();
	private Vector<String> vEnemyCharacters = new Vector<String>();
	private Vector<Integer> vEnemyScores = new Vector<Integer>();
	
	private int nRemainingTime;
	private JLabel uScoreLabel;
	private Vector<JLabel> vEnemyScoreLabels = new Vector<JLabel>();
	private JLabel uRemainingTimeLabel;
	private final int REFRESH_RATE = 1;
	
	public ScorePanel(JPanel uPanel, JFrame uFrame){
		this.uFrame = uFrame;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBounds(0, 0, 200, HEIGHT);
		this.setBackground(uSemiTrans);
		this.uScoreLabel = new JLabel("You: 0");
		this.uScoreLabel.setForeground(Color.white);
		this.uRemainingTimeLabel = new JLabel("Remaining Time: 0:0");
		this.uRemainingTimeLabel.setForeground(Color.white);
		this.add(this.uRemainingTimeLabel);
		JLabel uDamageLabel = new JLabel("Damage: ");
		uDamageLabel.setForeground(Color.white);
		this.add(uDamageLabel);
		this.add(this.uScoreLabel);
		uPanel.add(this);
		new Thread(this).start();
	}
	
	public void reset(){
		this.nScore = 0;
		this.uScoreLabel.setText("You: " + this.nScore);
	}
	
	public void add(int nAdd){
		this.nScore += nAdd;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("You: " + this.nScore);
	}
	
	public void subtract(int nSub){
		this.nScore -= nSub;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("You: " + this.nScore);
	}
	
	public void countComet(){
		this.nComets++;
	}
	
	public void countDeaths(){
		this.nDeaths++;
	}
	
	public void countPowerUps(){
		this.nPowerUps++;
	}
	
	public void countMaxSpin(){ //not yet implemented
		this.nMaxSpin++;
	}
	
	public void calcMaxVel(int nCurVel){
		System.out.println("");
		if(nCurVel > this.nMaxVel){
			this.nMaxVel = nCurVel;
		}
	}
	
	@Override
	public void run() {
		String msg;
		Scanner uScan;
		String sStatus = "";
		String sPrevStatus = "";
		
		bigloop:
		while(true) {
			msg = Client.sendMsg("GET_PLAYER_STATUS");
			while (msg.equals("GET_PLAYER_STATUS ACTIVE")) {
				msg = Client.sendMsg("GET_PLAYER_STATUS");
				break bigloop;
			}
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
		
		l:
		while(true) {
			
			// check to see if the status is score board and navigate to the scoreboard:
			//Check to see if the player is in an active game:
			msg = Client.sendMsg("GET_PLAYER_STATUS");
			System.out.println(msg);
			uScan = new Scanner(msg);
			uScan.next();
			sStatus = uScan.next();
			System.out.println(msg);
			if(sStatus.equals("SCOREBOARD") && sPrevStatus.equals("ACTIVE")){
				try {
					new scoreboard();
					this.uFrame.dispose();
					break;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			sPrevStatus = sStatus;	
			
			//Update database with current score:
			uScan = new Scanner(Client.sendMsg("GET_PLAYER_INFO"));
			uScan.next();
			String sUsername = uScan.next();
			String sCharacter = uScan.next();
			String sReady = uScan.next();
			String sScore = uScan.next();
			sScore = this.nScore + "";
			String sComets = uScan.next();
			sComets = this.nComets + "";
			String sDeaths = uScan.next();
			sDeaths = this.nDeaths + "";
			String sPowerUps = uScan.next();
			sPowerUps = this.nPowerUps + "";
			String sMaxSpin = uScan.next();
			String sMaxVel = uScan.next();
			sMaxVel = this.nMaxVel + "";
			Client.sendMsg("SET_PLAYER_INFO " + sUsername + " " + sCharacter + " " + sReady + " " + sScore + " " + sComets + " " + sDeaths + " " + sPowerUps + " " + sMaxSpin + " " + sMaxVel);
			uScan.close();
			
			
			//Get the index of the active game that the current player is in:
			msg = Client.sendMsg("GET_PLAYER_GAME_INDEX");
			if(msg.equals("GET_PLAYER_GAME_INDEX FAILURE")){
				continue l;
			}
			uScan = new Scanner(msg);
			uScan.next();
			int nGameIndex = Integer.parseInt(uScan.next());
			uScan.close();
				
			// update enemy score
				msg = Client.sendMsg("GET_PLAYER_INDEX " + nGameIndex);
				if (msg.equals("GET_PLAYER_INDEX FAILURE")){
					continue l;
				}
				uScan = new Scanner(msg);
				uScan.next();
				int nPlayerIndex = Integer.parseInt(uScan.next());
				uScan.close();

				//Get the number of players
				msg = Client.sendMsg("GET_GAME_ACTIVE_NUM_PLAYERS " + nGameIndex);
				if(msg.equals("GET_GAME_ACTIVE_NUM_PLAYERS FAILURE")){
					continue l;
				}
				uScan = new Scanner(msg);
				uScan.next();
				int numPlayers = Integer.parseInt(uScan.next());
				uScan.close();

				vEnemyUsernames.clear();
				vEnemyCharacters.clear();
				vEnemyScores.clear();
				
				for(int i=0; i<numPlayers; i++){
					if(i != nPlayerIndex){
						msg = Client.sendMsg("GET_GAME_ACTIVE_PLAYER " + nGameIndex + " " + i);
						if(msg.equals("GET_GAME_ACTIVE_PLAYER FAILURE")){
							continue l;
						}
						uScan = new Scanner(msg);
						uScan.next();
						//Parse the message returned to get the score from the player info:
						//username, character, bReady, nScore, nComets, nDeaths, nPowerUps, nMaxSpin, nMaxVel
						vEnemyUsernames.add(uScan.next());
						vEnemyCharacters.add(uScan.next());
						uScan.next();
						vEnemyScores.add(new Integer(uScan.nextInt()));
					}
				}
				// remove the old labels:
				for(int i=0; i<vEnemyScoreLabels.size(); i++){
					this.remove(vEnemyScoreLabels.get(i));
				}
				vEnemyScoreLabels.clear();
				// create the new labels:
				for(int i=0; i<vEnemyUsernames.size(); i++){
					this.vEnemyScoreLabels.add(new JLabel(vEnemyUsernames.get(i) + " " + vEnemyScores.get(i)));
					this.vEnemyScoreLabels.get(i).setForeground(Color.WHITE);
				}
				// add the labels to the score panel:
				for(int i=0; i<vEnemyScoreLabels.size(); i++){
					this.add(vEnemyScoreLabels.get(i));
				}
				this.setSize(this.getWidth(), HEIGHT + vEnemyUsernames.size()*20);
				
			// update remaining time
				msg = Client.sendMsg("GET_GAME_ACTIVE " + nGameIndex);
				if (msg.equals("GET_GAME_ACTIVE FAILURE")){
					continue l;
				}
				Scanner scan = new Scanner(msg);
				String remainingTime = "";
				for (int i=0;i<3;i++) {
					remainingTime = scan.next();
				}
//				String min = remainingTime.split(":")[0];
//				String sec = remainingTime.split(":")[1];
//				// if remaining time is 0:0, stop this awesome time
//				if (Integer.parseInt(min)==0 && Integer.parseInt(sec)==0) {
//					try {
//						new scoreboard();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					this.uFrame.dispose();
//				}
				this.uRemainingTimeLabel.setText("Remaining Time: " + remainingTime);
				scan.close();
				
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
	
}
