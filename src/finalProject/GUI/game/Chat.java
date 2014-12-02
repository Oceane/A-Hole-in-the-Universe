package finalProject.GUI.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.sql.Date;
import java.util.ArrayList;

import javax.swing.*;

import finalProject.Client.Client;

public class Chat extends JFrame {

	JTextField userText;
	JTextArea chatWindow;
	String message = "";
	String serverIP;
	ObjectOutputStream output;
	ObjectInputStream input;
	Socket connection;
	BufferedReader br;
	String lastMsg = "";
	public JComboBox<String> p = new JComboBox<String>();
	
	public Chat() {
		super("Chatter Bro");
		
		JPanel takeUrTopOff = new JPanel();
		takeUrTopOff.setLayout(new BoxLayout(takeUrTopOff, BoxLayout.Y_AXIS));
		
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));

		userText = new JTextField();
		userText.setEditable(true);
		userText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(ae.getActionCommand().replaceAll(" ", "").equals("")){
					//do nuthin
				} else {
					if(p.getSelectedIndex()==0){
						Client.sendMsg("NOTIFY_ALL "+ae.getActionCommand());
					} else {
						int index = 0;
						try{
							index = Integer.parseInt(Client.sendMsg("GET_INDEX_FROM_NAME "+p.getSelectedItem()).replace("GET_INDEX_FROM_NAME ", ""));
							Client.sendMsg("NOTIFY_PLAYER "+index+" "+ae.getActionCommand());
						} catch(NumberFormatException e){
							System.out.println("OOPS!");
						}
					}
					checkForNewMessage();
					userText.setText("");
				}
			}
		});

		takeUrTopOff.add(userText);
		
		refreshPlayas();
		takeUrTopOff.add(p);
		
		JButton refresh = new JButton("refresh");
		
		refresh.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				checkForNewMessage();
				refreshPlayas();
			}
			
		});
		
		add(takeUrTopOff, BorderLayout.NORTH);
		add(refresh,BorderLayout.SOUTH);

		setSize(400, 650);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(800, 100);
		setVisible(true);
	}
	
	public void refreshPlayas(){
		p.removeAllItems();
		int numP = 0;
		p.addItem("To All");
		try{
			numP = Integer.parseInt(Client.sendMsg("GET_LOBBY_NUM_PLAYERS").replace("GET_LOBBY_NUM_PLAYERS ", ""));
		} catch(NumberFormatException e){
			System.out.println("Oops!");
		}
		for(int i = 0; i < numP; i++){
			String s = Client.sendMsg("GET_PLAYER_NAME_BASED_OFF_INDEX "+i).replace("GET_PLAYER_NAME_BASED_OFF_INDEX ", "");
			System.out.println(s);
			if(s.contains("FAILURE")){
				
			} else {
				p.addItem(s);
			}
		}
	}

	private void close() {
		showMessage("Closing");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showMessage(final String m) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(m+"\n");
			}
		});
	}

	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}
	
	private void checkForNewMessage(){
		//find where the last new message is and type them all
		String[] msgs = new String[5];
		
		//clear
		chatWindow.setText("");
		
		//fill in the array
		for(int i = 0; i < 5; i++){
			if(!Client.sendMsg("NOTIFICATION_GET "+i).replace("NOTIFICATION_GET ", "").equals("")) //if not empty, print
				showMessage(Client.sendMsg("NOTIFICATION_GET "+i).replace("NOTIFICATION_GET ", ""));
		}
	}

	public static void main(String[] args) {
		Client.connect("localhost");
		Client.sendMsg("CREATE_PLAYER NeuerKicksZlatansAss Neslaou");
		Chat chat = new Chat();
	}
}