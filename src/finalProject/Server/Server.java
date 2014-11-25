package finalProject.Server;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Server extends JFrame implements Runnable{
	public static final int PORT_NUM = 653;
	public static final String DB = "DB.xml";
	private ServerSocket ss;
	private Vector<ClientHandler> chVector = new Vector<ClientHandler>();
	private JTextArea uTextArea = new JTextArea();
	private JScrollPane uScrollPane;
	private JButton uClearButton;
	private JLabel uLabelConnections;
	private int nNumLine = 1;
	
	public Server() throws HeadlessException, UnknownHostException{
		super("A Hole In The Universe Server " + InetAddress.getLocalHost().getHostAddress());
		setSize(800, 600);
		setLocation(200, 50);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //The program will close when the window is closed
		
		uClearButton = new JButton("Clear");
		uTextArea = new JTextArea();
		uScrollPane = new JScrollPane(uTextArea);
		uLabelConnections = new JLabel("Connections: 0");
		uClearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				uTextArea.setText("");
			}
		});
		this.add(uLabelConnections, BorderLayout.NORTH);
		this.add(uScrollPane);
		this.add(uClearButton, BorderLayout.SOUTH);
		
		try {
			ss = new ServerSocket(PORT_NUM); //bind to port
			Thread uThread = new Thread(this);
			uThread.start();
		} catch (IOException ioe) {
			System.out.println("ioe in Server: " + ioe.getMessage());
		}
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
	uTextArea.append((nNumLine++) + ". " + sIP + ": " + msgReceived + "\n");
	uTextArea.append((nNumLine++) + ". " + sIP + ": " + msgSend + "\n\n");
	
	return msgSend;
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

