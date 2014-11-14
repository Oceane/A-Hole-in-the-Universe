package WaitGameJoinee;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class WaitGameJoineeGUI extends JPanel{
	public static final int WINDOW_X = 950;
	public static final int WINDOW_Y = 650;
	private JPanel uPanelPlayersInGame = new JPanel();
	private JPanel uPanelButtons = new JPanel();
	private JLabel uLabelPlayersInGame = new JLabel("Who's Playing:");
	private String [] aPlayersInGame = {"Yuxin", "Jonathan"};
	private String [] aGameTimes = {"1 minute", "2 minutes", "3 minutes", "4 minutes", "5 minutes", "6 minutes", "7 minutes", "8 minutes", "9 minutes", "10 minutes"};
	private DefaultListModel<String> uModelPlayersInGame = new DefaultListModel<>();
	private JScrollPane uScrollPlayersInGame;
	private JList<String> uListPlayersInGame;
	private JButton uButtonCancelGame = new JButton("Cancel");
	private JButton uButtonReady = new JButton("Ready");
	
	public WaitGameJoineeGUI(){
		initializeComponents();
		positionComponents();
		addActionListeners();
	}
	
	private void initializeComponents(){
		this.setPreferredSize(new Dimension(WINDOW_X*9/10, WINDOW_Y*9/10));
		this.setLayout(new GridLayout(1, 3 , 50, 0));
		//Players In Game:
		uPanelPlayersInGame.setLayout(new BoxLayout(uPanelPlayersInGame, BoxLayout.Y_AXIS));
		populateListStrings(uModelPlayersInGame, aPlayersInGame);
		uListPlayersInGame = new JList<>(uModelPlayersInGame);
		uListPlayersInGame.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		uScrollPlayersInGame = new JScrollPane(uListPlayersInGame);
		uPanelButtons.setLayout(new BoxLayout(uPanelButtons, BoxLayout.X_AXIS));
	}
	
	private void positionComponents(){
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
		uScrollPlayersInGame.setMaximumSize(new Dimension(uPanelPlayersInGame.getMaximumSize().width, WINDOW_Y*8/10));
		
		add(new JPanel());
		add(uPanelPlayersInGame);
		add(new JPanel());
	}
	
	private void addActionListeners(){
		
	}
	
	public static void main(String[] args) {
		JFrame uFrame = new JFrame("Wait Game");
		uFrame.setLayout(new FlowLayout());
		uFrame.setSize(WaitGameJoineeGUI.WINDOW_X, WaitGameJoineeGUI.WINDOW_Y);
		uFrame.setLocation(100, 50);
		uFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //The program will close when the window is closed
		
		WaitGameJoineeGUI uWaitGame = new WaitGameJoineeGUI();
		uFrame.add(uWaitGame);
		uFrame.setVisible(true);
	}
	
	private void populateListStrings(DefaultListModel<String> uModel, String [] aTitleStrings){
		for(int i=0; i<aTitleStrings.length; i++){
			uModel.addElement(aTitleStrings[i]);
		}
	}

}
