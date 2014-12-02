package finalProject.GUI.JoinGame;

import java.awt.CardLayout;
import java.awt.Color;
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
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import javax.swing.border.MatteBorder;









import finalProject.Client.Client;
import finalProject.GUI.createProfile;
import finalProject.GUI.WaitGameCreator.WaitGameCreatorGUI;
import finalProject.GUI.WaitGameJoinee.WaitGameJoineeGUI;

public class JoinGameGUI extends JFrame{
	public static final int WINDOW_X = 950;
	public static final int WINDOW_Y = 650;
	
	public JoinGameGUI() {
		setLayout(new FlowLayout());
		setSize(JoinGameGUI.WINDOW_X, JoinGameGUI.WINDOW_Y);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel uJoinGame = new JoinGamePanel();
		add(uJoinGame);
		setVisible(true);
	}

	public class JoinGamePanel extends JPanel implements Runnable{
		final Color uColorTrans = new Color(0, 0, 0, 0);
		private JPanel uPanelAvailablePlayers = new JPanel();
		private JPanel uPanelAvailableGames = new JPanel();
		private JPanel uPanelAvailableGamesLabelCont = new JPanel();
		private JPanel uPanelAvailableGamesButtons = new JPanel();
		private JPanel uPanelCard = new JPanel();
		private JPanel [] aPanelAvailableGames;
		private JPanel uPanelPlayersInGame = new JPanel();
		private JPanel uPanelCreateGame = new JPanel();
		private JPanel uPanelCreateGameFormCont = new JPanel();
		private JPanel uPanelCreateGameForm = new JPanel();
		private JPanel uPanelCreateGameButtons = new JPanel();
		private JLabel uLabelAvailablePlayers = new JLabel("Available Players:");
		private JLabel uLabelAvailableGames = new JLabel("Games:");
		private JLabel uLabelPlayersInGame = new JLabel("Who's Playing:");
		private JLabel uLabelCreateGame = new JLabel("Create Game:");
		private JPanel uPanelTitle = new JPanel();
		private JPanel uPanelTime = new JPanel();
		private JPanel uPanelMaxPlayers = new JPanel();
		private JLabel uLabelTitle = new JLabel("Title: ");
		private JLabel uLabelTime = new JLabel("Time: ");
		private JLabel uLabelMaxPlayers = new JLabel("Max Players: ");
		private JTextField uFieldTitle = new JTextField();
		private JComboBox uComboTime;
		private JTextField uFieldMaxPlayers = new JTextField();
		private Vector<String> vAvailablePlayersUsernames = new Vector<String>();
		private Vector<String> vAvailablePlayersCharacters = new Vector<String>();
		private Vector<String> vAvailableGamesTitles = new Vector<String>();
		private Vector<String> vAvailableGamesTimes = new Vector<String>();
		private Vector<String> vPlayersInGameUsernames = new Vector<String>();
		private Vector<String> vPlayersInGameCharacters = new Vector<String>();
		private String [] aGameTimes = { "1 minute", "2 minutes", "3 minutes", "4 minutes", "5 minutes", "6 minutes", "7 minutes", "8 minutes", "9 minutes", "10 minutes" };
		private DefaultListModel uModelAvailablePlayers = new DefaultListModel();
		private DefaultListModel uModelAvailableGames = new DefaultListModel();
		private DefaultListModel uModelPlayersInGame = new DefaultListModel();
		private JScrollPane uScrollAvailablePlayers;
		private JScrollPane uScrollAvailableGames;
		private JScrollPane uScrollPlayersInGame;
		private JList uListAvailablePlayers;
		private JList uListAvailableGames;
		private JList uListPlayersInGame;
		private JButton uButtonBackToCreateProfile = new JButton("Back To Profile");
		private JButton uButtonCreateGame = new JButton("Create");
		private JButton uButtonJoinGame = new JButton("Join");
		private JButton uButtonCancelCreateGame = new JButton("Cancel");
		private JButton uButtonDoneCreateGame = new JButton("Done");
		private Thread uThread;
		private boolean bDisplay = true;
		
		public JoinGamePanel() {
			initializeComponents();
			positionComponents();
			addActionListeners();
			uThread = new Thread(this);
			uThread.start();
		}

		private void initializeComponents() {
			this.setPreferredSize(new Dimension(WINDOW_X * 9 / 10, WINDOW_Y * 9 / 10));
			this.setLayout(new GridLayout(1, 3, 50, 0));

			// Card layout on right-hand side of screen:
			uPanelCard.setLayout(new CardLayout());

			// JLists
			// Available Players:
			uPanelAvailablePlayers.setLayout(new BoxLayout(uPanelAvailablePlayers, BoxLayout.Y_AXIS));
			uPanelAvailableGames.setLayout(new BoxLayout(uPanelAvailableGames, BoxLayout.Y_AXIS));
			uPanelAvailableGamesButtons.setLayout(new BoxLayout(uPanelAvailableGamesButtons, BoxLayout.X_AXIS));
			uPanelPlayersInGame.setLayout(new BoxLayout(uPanelPlayersInGame, BoxLayout.Y_AXIS));
			populateListStrings(uModelAvailablePlayers, vAvailablePlayersUsernames, vAvailablePlayersCharacters);
			uListAvailablePlayers = new JList(uModelAvailablePlayers);
			uListAvailablePlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// AvailableGames:
			populateListGames(uModelAvailableGames, vAvailableGamesTitles, vAvailableGamesTimes);
			uListAvailableGames = new JList(uModelAvailableGames);
			uListAvailableGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// Players In Game:
			populateListStrings(uModelPlayersInGame, vPlayersInGameUsernames, vPlayersInGameCharacters);
			uListPlayersInGame = new JList(uModelPlayersInGame);
			uListPlayersInGame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// Scroll bars:
			uScrollAvailablePlayers = new JScrollPane(uListAvailablePlayers);
			uScrollAvailableGames = new JScrollPane(uListAvailableGames);
			uScrollPlayersInGame = new JScrollPane(uListPlayersInGame);

			// Create game form:
			uPanelCreateGame.setLayout(new BoxLayout(uPanelCreateGame, BoxLayout.Y_AXIS));
			uPanelCreateGameFormCont.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
			uPanelCreateGameFormCont.setBackground(Color.WHITE);
			uPanelCreateGameForm.setLayout(new BoxLayout(uPanelCreateGameForm, BoxLayout.Y_AXIS));
			uPanelCreateGameForm.setBackground(new Color(0, 0, 0, 0));
			uPanelTitle.setLayout(new BoxLayout(uPanelTitle, BoxLayout.X_AXIS));
			uPanelTitle.setBackground(uColorTrans);
			uPanelTime.setLayout(new BoxLayout(uPanelTime, BoxLayout.X_AXIS));
			uPanelTime.setBackground(uColorTrans);
			uPanelMaxPlayers.setLayout(new BoxLayout(uPanelMaxPlayers, BoxLayout.X_AXIS));
			uPanelMaxPlayers.setBackground(uColorTrans);
			uLabelTitle.setLabelFor(uFieldTitle);
			uComboTime = new JComboBox(aGameTimes);
			uLabelTime.setLabelFor(uComboTime);
			uLabelMaxPlayers.setLabelFor(uFieldMaxPlayers);
			uPanelCreateGameButtons.setLayout(new BoxLayout(uPanelCreateGameButtons, BoxLayout.X_AXIS));
		}

		private void positionComponents() {
			JPanel uPanelCont;

			// Available Players:
			uPanelCont = new JPanel();
			uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			uPanelCont.add(uLabelAvailablePlayers);
			uPanelAvailablePlayers.add(uPanelCont);
			uPanelAvailablePlayers.add(uScrollAvailablePlayers);
			uScrollAvailablePlayers.setMaximumSize(new Dimension(uPanelAvailablePlayers.getMaximumSize().width, WINDOW_Y * 8 / 10));
			uPanelAvailablePlayers.add(uButtonBackToCreateProfile);
			uPanelCont.setMaximumSize(new Dimension(uPanelAvailablePlayers.getMaximumSize().width, uLabelAvailablePlayers.getMinimumSize().height));

			// Available games:
			uPanelCont = new JPanel();
			uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			uPanelCont.add(uLabelAvailableGames);
			uPanelAvailableGames.add(uPanelCont);
			uPanelAvailableGames.add(uScrollAvailableGames);
			uPanelAvailableGamesButtons.add(uButtonCreateGame);
			uPanelAvailableGamesButtons.add(Box.createHorizontalGlue());
			uPanelAvailableGamesButtons.add(uButtonJoinGame);
			uPanelAvailableGames.add(uPanelAvailableGamesButtons);
			uPanelCont.setMaximumSize(new Dimension(uPanelAvailableGames.getMaximumSize().width, uLabelAvailableGames.getMinimumSize().height));
			uScrollAvailableGames.setMaximumSize(new Dimension(uPanelAvailableGames.getMaximumSize().width, WINDOW_Y * 8 / 10));

			// Players in game:
			uPanelCont = new JPanel();
			uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			uPanelCont.add(uLabelPlayersInGame);
			uPanelPlayersInGame.add(uPanelCont);
			uPanelPlayersInGame.add(uScrollPlayersInGame);
			uPanelCard.add(uPanelPlayersInGame, "PlayersInGame");
			uPanelCont.setMaximumSize(new Dimension(uPanelPlayersInGame.getMaximumSize().width, uLabelPlayersInGame.getMinimumSize().height));
			uScrollPlayersInGame.setMaximumSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y * 8 / 10));

			// Create game:
			uPanelCont = new JPanel();
			uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
			uPanelCont.add(uLabelCreateGame);
			uPanelCreateGame.add(uPanelCont);
			// Form:
			uPanelTitle.add(uLabelTitle);
			uPanelTitle.add(uFieldTitle);
			uPanelCreateGameForm.add(uPanelTitle);
			uPanelTime.add(uLabelTime);
			uPanelTime.add(uComboTime);
			uPanelCreateGameForm.add(uPanelTime);
			uPanelMaxPlayers.add(uLabelMaxPlayers);
			uPanelMaxPlayers.add(uFieldMaxPlayers);
			uPanelCreateGameForm.add(uPanelMaxPlayers);
			uPanelCreateGameFormCont.add(uPanelCreateGameForm);
			uPanelCreateGame.add(uPanelCreateGameFormCont);
			uPanelCreateGameButtons.add(uButtonCancelCreateGame);
			uPanelCreateGameButtons.add(Box.createHorizontalGlue());
			uPanelCreateGameButtons.add(uButtonDoneCreateGame);
			uPanelCreateGame.add(uPanelCreateGameButtons);
			uPanelCard.add(uPanelCreateGame, "CreateGame");
			uPanelCont.setMaximumSize(new Dimension(uPanelCreateGame.getMaximumSize().width, uLabelCreateGame.getMinimumSize().height));
			uPanelCreateGameFormCont.setPreferredSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y * 8 / 10));
			uPanelCreateGameFormCont.setMaximumSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y * 8 / 10));

			// Add three main panels to the grid layout:
			add(uPanelAvailablePlayers);
			add(uPanelAvailableGames);
			add(uPanelCard);
		}

		private void addActionListeners() {
			uButtonBackToCreateProfile.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					new createProfile();
					dispose();
				}
			});

			uButtonCreateGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					CardLayout cardLayout = (CardLayout) uPanelCard.getLayout();
					cardLayout.show(uPanelCard, "CreateGame");
				}
			});
			uButtonJoinGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					int nSelectedIndex = uListAvailableGames.getSelectedIndex();
					if(nSelectedIndex < 0){
						return;
					}
					joinGame(nSelectedIndex);
					new WaitGameJoineeGUI();
					dispose();
				}
			});
			uButtonCancelCreateGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					uFieldTitle.setText("");
					uComboTime.setSelectedIndex(0);
					uFieldMaxPlayers.setText("");
					CardLayout cardLayout = (CardLayout) uPanelCard.getLayout();
					cardLayout.show(uPanelCard, "PlayersInGame");
				}
			});
			uButtonDoneCreateGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String sTitle = uFieldTitle.getText();
					int nMinutes = Integer.parseInt(aGameTimes[uComboTime.getSelectedIndex()].substring(0, aGameTimes[uComboTime.getSelectedIndex()].indexOf(" m")));
					int nMaxPlayers = 0;
					if(Client.isInteger(uFieldMaxPlayers.getText())){
						try{ //hard
							nMaxPlayers = Integer.parseInt(uFieldMaxPlayers.getText());
						
							if(!createGame(sTitle, nMinutes, nMaxPlayers)){
								showGameCreateError();
							}
							else{
								Scanner uScan = new Scanner(Client.sendMsg("GET_NUM_GAMES_AVAILABLE"));
								uScan.next();
								int nNumAvailableGames = Integer.parseInt(uScan.next());
								joinGame(nNumAvailableGames - 1);
								uScan.close();
								new WaitGameCreatorGUI();
								dispose();
								bDisplay = false;
							}
						} catch(NumberFormatException n) {
							//noting
						}
					} else {
						Icon kev = new ImageIcon("Icons/Mitnick.png");
						JOptionPane.showMessageDialog(null, "Enter only integers for maximum Players, Mitnick", "Stop it, Kevin!",JOptionPane.ERROR_MESSAGE, kev);
					}
				}
			});
		}
		
		private void showGameCreateError(){
			JOptionPane.showMessageDialog(this, "A game with the same name already exists!", "Error: ", JOptionPane.PLAIN_MESSAGE);
		}

		private void populateListGames(DefaultListModel uModel, Vector<String> vTitleStrings, Vector<String> vTimeStrings) {			
			//Set/add strings:
			for (int i=0; i<vTitleStrings.size(); i++) {
				String sContent = vTitleStrings.get(i) + " [" + vTimeStrings.get(i) + " minutes]";
				if(i < uModel.getSize()){
					uModel.set(i, sContent);
				}
				else{
					uModel.addElement(sContent);
				}
			}
			//Remove extra strings:
			for(int i=vTitleStrings.size(); i<uModel.size(); i++){
				uModel.remove(i);
			}
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
		
		private boolean createGame(String sTitle, int nMinutes, int nMaxPlayers){
			String msg = Client.sendMsg("CREATE_GAME " + sTitle + " " + nMinutes + ":00 " + nMaxPlayers);
			if(msg.equals("CREATE_GAME FAILURE")){
				return false;
			}
			else{
				return true;
			}
		}
		
		private boolean joinGame(int nGameIndex){
			String msg = Client.sendMsg("JOIN_GAME " + nGameIndex);
			if(msg.equals("JOIN_GAME SUCCESS")){
				return true;
			}
			else{
				return false;
			}
		}
		
		public void run(){
			Scanner uScan;
			String sMsg;
			
			while(true){
				
				if(!bDisplay){
					break;
				}
				
			//Update the list boxes every second:
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//Update the available players:
					vAvailablePlayersUsernames.clear();
					vAvailablePlayersCharacters.clear();
					uScan = new Scanner(Client.sendMsg("GET_NUM_PLAYERS_AVAILABLE"));
					uScan.next();
					int nNumAvailablePlayers = Integer.parseInt(uScan.next());
					uScan.close();
					for(int i=0; i<nNumAvailablePlayers; i++){
						sMsg = Client.sendMsg("GET_PLAYER_AVAILABLE " + i);
						if(sMsg.equals("GET_PLAYER_AVAILABLE FAILURE")){
							continue;
						}
						uScan = new Scanner(sMsg);
						uScan.next();
						vAvailablePlayersUsernames.add(uScan.next());
						vAvailablePlayersCharacters.add(uScan.next());
						uScan.close();
					}
					populateListStrings(uModelAvailablePlayers, vAvailablePlayersUsernames, vAvailablePlayersCharacters);
				//Update the available games:
					vAvailableGamesTitles.clear();
					vAvailableGamesTimes.clear();
					uScan = new Scanner(Client.sendMsg("GET_NUM_GAMES_AVAILABLE"));
					uScan.next();
					int nNumAvailableGames = Integer.parseInt(uScan.next());
					uScan.close();
					for(int i=0; i<nNumAvailableGames; i++){
						sMsg = Client.sendMsg("GET_GAME_AVAILABLE " + i);
						if(sMsg.equals("GET_GAME_AVAILABLE FAILURE")){
							continue;
						}
						uScan = new Scanner(sMsg);
						uScan.next();
						vAvailableGamesTitles.add(uScan.next());
						uScan.next();
						String sTime = uScan.next();
						try{vAvailableGamesTimes.add("" + sTime.substring(0, sTime.indexOf(":")));}
						catch(NullPointerException n){} catch(StringIndexOutOfBoundsException s){}
						uScan.close();
					}
					populateListGames(uModelAvailableGames, vAvailableGamesTitles, vAvailableGamesTimes);
				//Update the players in game:
					vPlayersInGameUsernames.clear();
					vPlayersInGameCharacters.clear();
					int nGameIndex = uListAvailableGames.getSelectedIndex();
					if(nGameIndex < 0){
						continue;
					}
					sMsg = Client.sendMsg("GET_GAME_AVAILABLE_NUM_PLAYERS " + nGameIndex);
					if(sMsg.equals("GET_GAME_AVAILABLE_NUM_PLAYERS FAILURE")){
						continue;
					}
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
			}
		}
	}
}