package finalProject.GUI.JoinGame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.MatteBorder;

public class JoinGameGUI extends JPanel {
	public static final int WINDOW_X = 950;
	public static final int WINDOW_Y = 650;
	public static final Color uColorTrans = new Color(0, 0, 0, 0);
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
	private JLabel uLabelTitle = new JLabel("Title: ");
	private JLabel uLabelTime = new JLabel("Time: ");
	private JTextField uFieldTitle = new JTextField();
	private JComboBox<String> uComboTime;
	private String [] aAvailablePlayers = {"Yuxin", "Jonathan", "Natalie", "David", "Geoffrey"};
	private String [] aAvailableGames = {"Star Wars", "Kittens", "Bombs Away", "Hello World"};
	private String [] aAvailableTimes = {"1 minute", "2 minutes", "3 minutes", "4 minutes"};
	private String [] aPlayersInGame = {"Yuxin", "Jonathan"};
	private String [] aGameTimes = {"1 minute", "2 minutes", "3 minutes", "4 minutes", "5 minutes", "6 minutes", "7 minutes", "8 minutes", "9 minutes", "10 minutes"};
	private DefaultListModel<String> uModelAvailablePlayers = new DefaultListModel<>();
	private DefaultListModel<String> uModelAvailableGames = new DefaultListModel<>();
	private DefaultListModel<String> uModelPlayersInGame = new DefaultListModel<>();
	private JScrollPane uScrollAvailablePlayers;
	private JScrollPane uScrollAvailableGames;
	private JScrollPane uScrollPlayersInGame;
	private JList<String> uListAvailablePlayers;
	private JList<String> uListAvailableGames;
	private JList<String> uListPlayersInGame;
	private JButton uButtonBackToCreateProfile =  new JButton("Back To Profile");
	private JButton uButtonCreateGame = new JButton("Create");
	private JButton uButtonJoinGame = new JButton("Join");
	private JButton uButtonCancelCreateGame = new JButton("Cancel");
	private JButton uButtonDoneCreateGame = new JButton("Done");
	
	public JoinGameGUI(){
		initializeComponents();
		positionComponents();
		addActionListeners();
	}
	
	private void initializeComponents(){
		this.setPreferredSize(new Dimension(WINDOW_X*9/10, WINDOW_Y*9/10));
		this.setLayout(new GridLayout(1, 3 , 50, 0));
		
		//Card layout on right-hand side of screen:
		uPanelCard.setLayout(new CardLayout());
		
		//JLists
			//Available Players:
			uPanelAvailablePlayers.setLayout(new BoxLayout(uPanelAvailablePlayers, BoxLayout.Y_AXIS));
			uPanelAvailableGames.setLayout(new BoxLayout(uPanelAvailableGames, BoxLayout.Y_AXIS));
			uPanelAvailableGamesButtons.setLayout(new BoxLayout(uPanelAvailableGamesButtons, BoxLayout.X_AXIS));
			uPanelPlayersInGame.setLayout(new BoxLayout(uPanelPlayersInGame, BoxLayout.Y_AXIS));
			populateListStrings(uModelAvailablePlayers, aAvailablePlayers);
			uListAvailablePlayers = new JList<>(uModelAvailablePlayers);
			uListAvailablePlayers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//AvailableGames:
			populateListGames(uModelAvailableGames, aAvailableGames, aAvailableTimes);
			uListAvailableGames = new JList<>(uModelAvailableGames);
			uListAvailableGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//Players In Game:
			populateListStrings(uModelPlayersInGame, aPlayersInGame);
			uListPlayersInGame = new JList<>(uModelPlayersInGame);
			uListPlayersInGame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//Scroll bars:
			uScrollAvailablePlayers = new JScrollPane(uListAvailablePlayers);
			uScrollAvailableGames = new JScrollPane(uListAvailableGames);
			uScrollPlayersInGame = new JScrollPane(uListPlayersInGame);
		
		//Create game form:
		uPanelCreateGame.setLayout(new BoxLayout(uPanelCreateGame, BoxLayout.Y_AXIS));
		uPanelCreateGameFormCont.setBorder(new MatteBorder(1, 1, 1, 1, Color.GRAY));
		uPanelCreateGameFormCont.setBackground(Color.WHITE);
		uPanelCreateGameForm.setLayout(new BoxLayout(uPanelCreateGameForm, BoxLayout.Y_AXIS));
		uPanelCreateGameForm.setBackground(new Color(0, 0, 0, 0));
		uPanelTitle.setLayout(new BoxLayout(uPanelTitle, BoxLayout.X_AXIS));
		uPanelTitle.setBackground(uColorTrans);
		uPanelTime.setLayout(new BoxLayout(uPanelTime, BoxLayout.X_AXIS));
		uPanelTime.setBackground(uColorTrans);
		uLabelTitle.setLabelFor(uFieldTitle);
		uComboTime = new JComboBox<String>(aGameTimes);
		uLabelTime.setLabelFor(uComboTime);
		uPanelCreateGameButtons.setLayout(new BoxLayout(uPanelCreateGameButtons, BoxLayout.X_AXIS));
	}
	
	private void positionComponents(){
		JPanel uPanelCont;
		
		//Available Players:
		uPanelCont = new JPanel();
		uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		uPanelCont.add(uLabelAvailablePlayers);
		uPanelAvailablePlayers.add(uPanelCont);
		uPanelAvailablePlayers.add(uScrollAvailablePlayers);
		uScrollAvailablePlayers.setMaximumSize(new Dimension(uPanelAvailablePlayers.getMaximumSize().width, WINDOW_Y*8/10));
		uPanelAvailablePlayers.add(uButtonBackToCreateProfile);
		uPanelCont.setMaximumSize(new Dimension(uPanelAvailablePlayers.getMaximumSize().width, uLabelAvailablePlayers.getMinimumSize().height));

		
		//Available games:
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
		uScrollAvailableGames.setMaximumSize(new Dimension(uPanelAvailableGames.getMaximumSize().width, WINDOW_Y*8/10));
		
		//Players in game:
		uPanelCont = new JPanel();
		uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		uPanelCont.add(uLabelPlayersInGame);
		uPanelPlayersInGame.add(uPanelCont);
		uPanelPlayersInGame.add(uScrollPlayersInGame);
		uPanelCard.add(uPanelPlayersInGame, "PlayersInGame");
		uPanelCont.setMaximumSize(new Dimension(uPanelPlayersInGame.getMaximumSize().width, uLabelPlayersInGame.getMinimumSize().height));
		uScrollPlayersInGame.setMaximumSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y*8/10));
		
		//Create game:
		uPanelCont = new JPanel();
		uPanelCont.setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
		uPanelCont.add(uLabelCreateGame);
		uPanelCreateGame.add(uPanelCont);
			//Form:
			uPanelTitle.add(uLabelTitle);
			uPanelTitle.add(uFieldTitle);
			uPanelCreateGameForm.add(uPanelTitle);
			uPanelTime.add(uLabelTime);
			uPanelTime.add(uComboTime);
			uPanelCreateGameForm.add(uPanelTime);
		uPanelCreateGameFormCont.add(uPanelCreateGameForm);
		uPanelCreateGame.add(uPanelCreateGameFormCont);
		uPanelCreateGameButtons.add(uButtonCancelCreateGame);
		uPanelCreateGameButtons.add(Box.createHorizontalGlue());
		uPanelCreateGameButtons.add(uButtonDoneCreateGame);
		uPanelCreateGame.add(uPanelCreateGameButtons);
		uPanelCard.add(uPanelCreateGame, "CreateGame");
		uPanelCont.setMaximumSize(new Dimension(uPanelCreateGame.getMaximumSize().width, uLabelCreateGame.getMinimumSize().height));
		uPanelCreateGameFormCont.setPreferredSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y*8/10));
		uPanelCreateGameFormCont.setMaximumSize(new Dimension(uPanelCard.getMaximumSize().width, WINDOW_Y*8/10));
		
		//Add three main panels to the grid layout:
		add(uPanelAvailablePlayers);
		add(uPanelAvailableGames);
		add(uPanelCard);
	}
	
	private void addActionListeners(){
		uButtonBackToCreateProfile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
			}
		});
		
		uButtonCreateGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				CardLayout cardLayout = (CardLayout) uPanelCard.getLayout();
				cardLayout.show(uPanelCard, "CreateGame");
			}
		});
		uButtonJoinGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
			}
		});
		uButtonCancelCreateGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				uFieldTitle.setText("");
				uComboTime.setSelectedIndex(0);
				CardLayout cardLayout = (CardLayout) uPanelCard.getLayout();
				cardLayout.show(uPanelCard, "PlayersInGame");
			}
		});
		uButtonDoneCreateGame.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
			}
		});
	}
	
	public static void main(String [] args){
		JFrame uFrame = new JFrame("Join Game");
		uFrame.setLayout(new FlowLayout());
		uFrame.setSize(JoinGameGUI.WINDOW_X, JoinGameGUI.WINDOW_Y);
		uFrame.setLocation(100, 50);
		uFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //The program will close when the window is closed
		
		JoinGameGUI uJoinGame = new JoinGameGUI();
		uFrame.add(uJoinGame);
		uFrame.setVisible(true);
	}
	
	private void populateListGames(DefaultListModel<String> uModel, String [] aTitleStrings, String [] aTimeStrings){
		for(int i=0; i<aTitleStrings.length; i++){
			uModel.addElement(aTitleStrings[i] + " [" + aTimeStrings[i] + "]");
		}
	}
		
	private void populateListStrings(DefaultListModel<String> uModel, String [] aTitleStrings){
		for(int i=0; i<aTitleStrings.length; i++){
			uModel.addElement(aTitleStrings[i]);
		}
	}

}
