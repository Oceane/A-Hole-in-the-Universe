package finalProject.Server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

public class Server extends JFrame implements Runnable{
	public static final int PORT_NUM = 653;
	public static final String DB = "DB.xml";
	private ServerSocket ss;
	private Vector<ClientHandler> chVector = new Vector<ClientHandler>();
	private JPanel uPanelMsg = new JPanel();
	private JPanel uPanelDB = new JPanel();
	private JTextArea uTextAreaMsg = new JTextArea();
	private JTextArea uTextAreaDB = new JTextArea();
	private JScrollPane uScrollPaneMsg;
	private JScrollPane uScrollPaneDB;
	private JButton uClearButton;
	private JLabel uLabelConnections;
	private int nNumLine = 1;
	
	public Server() throws HeadlessException, UnknownHostException{
		super("A Hole In The Universe Server " + InetAddress.getLocalHost().getHostAddress());
		setSize(1200, 700);
		setLocation(0, 0);
		setLayout(new GridLayout(1, 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //The program will close when the window is closed
		
		uClearButton = new JButton("Clear");
		uScrollPaneMsg = new JScrollPane(uTextAreaMsg);
		uLabelConnections = new JLabel("Connections: 0");
		uClearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				uTextAreaMsg.setText("");
			}
		});
		//uPanelMsg.setPreferredSize();
		uPanelMsg.setLayout(new BoxLayout(uPanelMsg, BoxLayout.Y_AXIS));
		uPanelMsg.setBorder(new LineBorder(Color.BLACK));
		uPanelMsg.add(uLabelConnections);
		uPanelMsg.add(uScrollPaneMsg);
		uPanelMsg.add(uClearButton);
		
		uPanelDB.setLayout(new BoxLayout(uPanelDB, BoxLayout.Y_AXIS));
		uPanelDB.add(new JLabel("XML Database:"));
		uTextAreaDB.setTabSize(2);
		uScrollPaneDB = new JScrollPane(uTextAreaDB);
		uPanelDB.add(uScrollPaneDB);
		this.add(uPanelMsg);
		this.add(uPanelDB);
		
		try {
			ss = new ServerSocket(PORT_NUM); //bind to port
			Thread uThread = new Thread(this);
			uThread.start();
		} catch (IOException ioe) {
			System.out.println("ioe in Server: " + ioe.getMessage());
		}
		updateDBDisplay();
		setVisible(true);
	}
	
	public synchronized String processMsg(String msgReceived, ClientHandler ch){
	Scanner uScan = new Scanner(msgReceived);
	String msgSend = null;
	String sIP;
	String msgType;
	String msgBody;
	//Determine ip address of client:
	sIP = ch.getSocket().getRemoteSocketAddress().toString();
	
	msgSend = "Dear client, the server deems this day to be beautiful as well.";
	
	//Print msg transaction to the screen:
	uTextAreaMsg.append((nNumLine++) + ". " + sIP + ": " + msgReceived + "\n");
	uTextAreaMsg.append((nNumLine++) + ". " + sIP + ": " + msgSend + "\n\n");
	
	//Update the display of the database after modifications have been made:
	updateDBDisplay();
	
	return msgSend;
	}
	
	public void updateDBDisplay(){
		try {
		//Open the XML DB reader:
		BufferedReader br;
		br = new BufferedReader(new FileReader(DB));
		
		//Clear the DB textarea:
		uTextAreaDB.setText("");
		//Write the contents of the XML DB to the DB textarea:
		String line;
		line = br.readLine();
		while (line != null){
			uTextAreaDB.append(line + "\n");
			line= br.readLine();
		}
		//Close the reader:
		br.close();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeClient(ClientHandler ch){
		chVector.remove(ch);
		uLabelConnections.setText("Connections: " + chVector.size());
	}
	
	public void run(){
		//Continually accept new client connections:
		Socket s;
		while(true){
			try {
				//Wait for new clients to connect:
				s = ss.accept();
				ClientHandler ch = new ClientHandler(s, this);
				chVector.add(ch);
				ch.start();
				uLabelConnections.setText("Connections: " + chVector.size());
			} catch (IOException e) {
				e.printStackTrace();
			} //blocking line: waiting state of thread
		}
	}
	
	public static void main(String [] args){
		try {
			Server uServer = new Server();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

