package finalProject.GUI.WaitGameJoinee;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import finalProject.Client.Client;
import finalProject.GUI.JoinGame.JoinGameGUI;
import finalProject.GUI.game.GameUI;

public class WaitGameJoineeGUI extends JFrame {
	public static final int WINDOW_X = 950;
	public static final int WINDOW_Y = 650;
	private JPanel uPanelPlayersInGame = new JPanel();
	private JPanel uPanelButtons = new JPanel();
	private JLabel uLabelPlayersInGame = new JLabel("Who's Playing:");
	private Vector<String> vPlayersInGameUsernames = new Vector<String>();
	private Vector<String> vPlayersInGameCharacters = new Vector<String>();
	private DefaultListModel uModelPlayersInGame = new DefaultListModel();
	private JScrollPane uScrollPlayersInGame;
	private JList uListPlayersInGame;
	private JButton uButtonCancelGame = new JButton("Cancel");
	private JButton uButtonReady = new JButton("Ready");

	public WaitGameJoineeGUI() {

		setLayout(new FlowLayout());
		setSize(WaitGameJoineeGUI.WINDOW_X, WaitGameJoineeGUI.WINDOW_Y);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel uWaitGame = new WaitGameJoineePanel();
		add(uWaitGame);
		setVisible(true);
	}

	private class WaitGameJoineePanel extends JPanel implements Runnable{

		public WaitGameJoineePanel() {

			initializeComponents();
			positionComponents();
			addActionListeners();
			new Thread(this).start();
		}

		private void initializeComponents() {
			this.setPreferredSize(new Dimension(WINDOW_X * 9 / 10, WINDOW_Y * 9 / 10));
			this.setLayout(new GridLayout(1, 3, 50, 0));
			// Players In Game:
			uPanelPlayersInGame.setLayout(new BoxLayout(uPanelPlayersInGame, BoxLayout.Y_AXIS));
			populateListStrings(uModelPlayersInGame, vPlayersInGameUsernames, vPlayersInGameCharacters);
			uListPlayersInGame = new JList(uModelPlayersInGame);
			uListPlayersInGame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			uScrollPlayersInGame = new JScrollPane(uListPlayersInGame);
			uPanelButtons.setLayout(new BoxLayout(uPanelButtons, BoxLayout.X_AXIS));
		}

		private void positionComponents() {
			JPanel uPanelCont = new JPanel();
			uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			uPanelCont.add(uLabelPlayersInGame);
			uPanelPlayersInGame.add(uPanelCont);
			uPanelPlayersInGame.add(uScrollPlayersInGame);
			uPanelButtons.add(uButtonCancelGame);
			uPanelButtons.add(Box.createHorizontalGlue());
			uPanelButtons.add(uButtonReady);
			uPanelPlayersInGame.add(uPanelButtons);
			uPanelCont.setMaximumSize(new Dimension(uPanelPlayersInGame.getMaximumSize().width, uLabelPlayersInGame.getMinimumSize().height));
			uScrollPlayersInGame.setMaximumSize(new Dimension(uPanelPlayersInGame.getMaximumSize().width, WINDOW_Y * 8 / 10));

			add(new JPanel());
			add(uPanelPlayersInGame);
			add(new JPanel());
		}

		private void addActionListeners() {
			uButtonCancelGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					//Leave the game:
					String sMsg = Client.sendMsg("LEAVE_GAME");
					if(sMsg.equals("LEAVE_GAME FAILURE")){
						leaveGameFailure();
						return;	
					}
					new JoinGameGUI();
					dispose();
				}
			});
			uButtonReady.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Scanner uScan = new Scanner(Client.sendMsg("GET_PLAYER_INFO"));
					uScan.next();
					String sUsername = uScan.next();
					String sCharacter = uScan.next();
					String sReady = uScan.next();
					sReady = "true";
					String sScore = uScan.next();
					String sComets = uScan.next();
					String sDeaths = uScan.next();
					String sPowerUps = uScan.next();
					String sMaxSpin = uScan.next();
					String sMaxVel = uScan.next();
					Client.sendMsg("SET_PLAYER_INFO " + sUsername + " " + sCharacter + " " + sReady + " " + sScore + " " + sComets + " " + sDeaths + " " + sPowerUps + " " + sMaxSpin + " " + sMaxVel);
					uScan.close();
				}
			});
		}
		
		private void leaveGameFailure(){
			JOptionPane.showMessageDialog(this, "Cannot leave the game at this time!", "Error: ", JOptionPane.PLAIN_MESSAGE);
		}

		private void populateListStrings(DefaultListModel uModel, Vector<String> vUsernameStrings, Vector<String> vCharacterStrings) {
			//Set/add strings:
			for (int i=0; i<vUsernameStrings.size(); i++) {
				String sContent = vUsernameStrings.get(i) + " (" + vCharacterStrings.get(i) + ")";
				if(i < uModel.getSize()){
					uModel.set(i, sContent);
				}
				else{
					uModel.addElement(sContent);
				}
			}
			//Remove extra strings:
			for(int i=vUsernameStrings.size(); i<uModel.size(); i++){
				uModel.remove(i);
			}
		}
		
		private void populateListStrings(DefaultListModel uModel, String[] aTitleStrings) {
			for (int i = 0; i < aTitleStrings.length; i++) {
				uModel.addElement(aTitleStrings[i]);
			}
		}
		
		public void run(){
			Scanner uScan;
			String sMsg;
			
			while(true){
			//Update the list boxes every second:
				//Thread.sleep(1000);
				//Update the players in game:
					//Get the game index:
					sMsg = Client.sendMsg("GET_PLAYER_GAME_INDEX");
					if(sMsg.equals("GET_PLAYER_GAME_INDEX FAILURE")){
						continue;
					}
					uScan = new Scanner(sMsg);
					uScan.next();
					int nGameIndex = uScan.nextInt();
					if(nGameIndex < 0){
						continue;
					}
					sMsg = Client.sendMsg("GET_GAME_AVAILABLE_NUM_PLAYERS " + nGameIndex);
					if(sMsg.equals("GET_GAME_AVAILABLE_NUM_PLAYERS FAILURE")){
						continue;
					}
					vPlayersInGameUsernames.clear();
					vPlayersInGameCharacters.clear();
					uScan.close();
					uScan = new Scanner(sMsg);
					uScan.next();
					int nNumPlayersInGame = Integer.parseInt(uScan.next());
					uScan.close();
					for(int i=0; i<nNumPlayersInGame; i++){
						sMsg = Client.sendMsg("GET_GAME_AVAILABLE_PLAYER " + nGameIndex + " " + i);
						if(sMsg.equals("GET_GAME_AVAILABLE_PLAYER FAILURE")){
							continue;
						}
						uScan = new Scanner(sMsg);
						uScan.next();
						vPlayersInGameUsernames.add(uScan.next());
						vPlayersInGameCharacters.add(uScan.next());
						uScan.close();
					}
					populateListStrings(uModelPlayersInGame, vPlayersInGameUsernames, vPlayersInGameCharacters);
					
					//Check to see if the player is in an active game:
					uScan = new Scanner(Client.sendMsg("GET_PLAYER_STATUS"));
					uScan.next();
					String status = uScan.next();
					if(status == "ACTIVE"){
						new GameUI();
						dispose();
					}
			}
		}
	}
}