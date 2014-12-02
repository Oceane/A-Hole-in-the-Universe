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
	public static final Color uSemiTrans = new Color(0, 0, 0, 150);
	private JFrame uFrame;
	private int nScore;
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
		this.setBounds(0, 0, 200, 50);
		this.setBackground(uSemiTrans);
		this.uScoreLabel = new JLabel("You: 0");
		this.uScoreLabel.setForeground(Color.white);
		this.uRemainingTimeLabel = new JLabel("Remaining Time: 0:0");
		this.uRemainingTimeLabel.setForeground(Color.white);
		this.add(this.uRemainingTimeLabel);
		this.add(new JLabel("Damage: "));
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

	@Override
	public void run() {
		Scanner uScan;
		String sStatus = "";
		String sPrevStatus = "";
		
		while(true) {
			String msg = Client.sendMsg("GET_PLAYER_STATUS");
			if (msg.equals("GET_PLAYER_STATUS ACTIVE")) {
				break;
			}
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
		
		while(true) {

			
			//Update database with current score:
			uScan = new Scanner(Client.sendMsg("GET_PLAYER_INFO"));
			uScan.next();
			String sUsername = uScan.next();
			String sCharacter = uScan.next();
			String sReady = uScan.next();
			String sScore = uScan.next();
			sScore = this.nScore + "";
			String sComets = uScan.next();
			String sDeaths = uScan.next();
			String sPowerUps = uScan.next();
			String sMaxSpin = uScan.next();
			String sMaxVel = uScan.next();
			Client.sendMsg("SET_PLAYER_INFO " + sUsername + " " + sCharacter + " " + sReady + " " + sScore + " " + sComets + " " + sDeaths + " " + sPowerUps + " " + sMaxSpin + " " + sMaxVel);
			uScan.close();
			
			
			//Get the index of the active game that the current player is in:
			String msg = Client.sendMsg("GET_PLAYER_GAME_INDEX");
			if(msg.equals("GET_PLAYER_GAME_INDEX FAILURE")){
				continue;
			}
			uScan = new Scanner(msg);
			uScan.next();
			int nGameIndex = Integer.parseInt(uScan.next());
			uScan.close();
				
			// update enemy score
				msg = Client.sendMsg("GET_PLAYER_INDEX " + nGameIndex);
				if(msg.equals("GET_PLAYER_INDEX FAILURE")){
					continue;
				}
				uScan = new Scanner(msg);
				uScan.next();
				int nPlayerIndex = Integer.parseInt(uScan.next());
				uScan.close();

				//Get the number of players
				msg = Client.sendMsg("GET_GAME_ACTIVE_NUM_PLAYERS " + nGameIndex);
				if(msg.equals("GET_GAME_ACTIVE_NUM_PLAYERS FAILURE")){
					continue;
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
							continue;
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
				
			// update remaining time
				msg = Client.sendMsg("GET_GAME_ACTIVE " + nGameIndex);
				if(msg.equals("GET_GAME_ACTIVE FAILURE")){
					continue;
				}
				Scanner scan = new Scanner(msg);
				String remainingTime = "";
				for (int i=0;i<3;i++) {
					remainingTime = scan.next();
				}
				this.uRemainingTimeLabel.setText("Remaining Time: " + remainingTime);
				scan.close();
				
			// check to see if the status is score board and navigate to the scoreboard:
				//Check to see if the player is in an active game:
				msg = Client.sendMsg("GET_PLAYER_STATUS");
				uScan = new Scanner(msg);
				uScan.next();
				sStatus = uScan.next();
				System.out.println(msg);
				if(sStatus.equals("SCOREBOARD") && sPrevStatus.equals("ACTIVE")){
					try {
						new scoreboard();
						this.uFrame.dispose();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				sPrevStatus = sStatus;
				
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
	
}
