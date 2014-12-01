package finalProject.GUI.game;

import java.awt.Color;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import finalProject.Client.Client;

public class ScorePanel extends JPanel implements Runnable{
	public static final Color uSemiTrans = new Color(0, 0, 0, 150);
	private int nScore;
	private int nEnemyScore;
	private int nRemainingTime;
	private JLabel uScoreLabel;
	private JLabel uEnemyScoreLabel;
	private JLabel uRemainingTimeLabel;
	private final int REFRESH_RATE = 1;
	
	public ScorePanel(JPanel uPanel){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBounds(0, 0, 150, 50);
		this.setBackground(uSemiTrans);
		this.uScoreLabel = new JLabel("Damage: 0");
		this.uScoreLabel.setForeground(Color.white);
		this.uEnemyScoreLabel = new JLabel("Enemy Damage: 0");
		this.uEnemyScoreLabel.setForeground(Color.white);
		this.uRemainingTimeLabel = new JLabel("Remaining Time: 123");
		this.add(this.uScoreLabel);
		this.add(this.uEnemyScoreLabel);
		this.add(this.uEnemyScoreLabel);
		uPanel.add(this);
		new Thread(this).start();
	}
	
	public void reset(){
		this.nScore = 0;
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}
	
	public void add(int nAdd){
		this.nScore += nAdd;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}
	
	public void subtract(int nSub){
		this.nScore -= nSub;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}

	@Override
	public void run() {
		while(true) {
			String msg = Client.sendMsg("GET_PLAYER_STATUS");
			if (msg.equals("ACTIVE")) {
				break;
			}
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
		
		while(true) {

			//Get the index of the active game that the current player is in:
			String msg = Client.sendMsg("GET_PLAYER_GAME_INDEX");
			int nGameIndex = Integer.parseInt(msg);
				
			// update enemy score
			{
				msg = Client.sendMsg("GET_PLAYER_INDEX " + nGameIndex);
				int nPlayerIndex = Integer.parseInt(msg);

				//Get the number of players
				msg = Client.sendMsg("GET_GAME_ACTIVE_NUM_PLAYERS " + nGameIndex);
				int numPlayers = Integer.parseInt(msg);

				for(int i=0; i<numPlayers; i++){
					if(i != nPlayerIndex){
						msg = Client.sendMsg("GET_GAME_ACTIVE_PLAYER " + nGameIndex + "," + i);
						//Parse the message returned to get the score from the player info:
						int nScore = Integer.parseInt(msg);
						this.nEnemyScore=nScore;
						this.uEnemyScoreLabel.setText("Damage: " + this.nEnemyScore);
					}
				}
			}
			
			// update remaining time
			{
				msg = Client.sendMsg("GET_GAME_ACTIVE " + nGameIndex);
				Scanner scan = new Scanner(msg);
				int remainingTime = 0;
				for (int i=0;i<4;i++) {
					remainingTime = Integer.parseInt(scan.next());	// 
				}
				this.uRemainingTimeLabel.setText("Remaining time: " + remainingTime);;
				scan.close();
			}
			try {
				Thread.sleep(1000/REFRESH_RATE);  // milliseconds
			} catch (InterruptedException ex) {
				System.out.print(ex.getMessage());
			}
		}
	}
	
}
