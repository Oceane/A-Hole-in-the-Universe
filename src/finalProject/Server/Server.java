package finalProject.Server;

import java.io.File;
import java.io.IOException;
import finalProject.Server.ClientHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Server extends JFrame implements Runnable {
	public static final int PORT_NUM = 1025;
	public static final String DB = "DB.xml";
	private ServerSocket ss;
	private Vector<ClientHandler> chVector = new Vector<ClientHandler>();
	private JPanel uPanelMsg = new JPanel();
	private JPanel uPanelDB = new JPanel();
	private static JTextArea uTextAreaMsg = new JTextArea();
	private static JTextArea uTextAreaDB = new JTextArea();
	private JScrollPane uScrollPaneMsg;
	private JScrollPane uScrollPaneDB;
	private JButton uClearButton;
	private JLabel uLabelConnections;
	private int nNumLine = 1;
	private DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private DocumentBuilder db;
	private static org.w3c.dom.Document doc = null;

	public Server() throws HeadlessException, UnknownHostException {
		super("A Hole In The Universe Server "
				+ InetAddress.getLocalHost().getHostAddress());
		setSize(1300, 700);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(1, 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // The program will
														// close when the window
														// is closed

		uClearButton = new JButton("Clear");
		uScrollPaneMsg = new JScrollPane(uTextAreaMsg);
		uLabelConnections = new JLabel("Connections: 0");
		uClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				uTextAreaMsg.setText("");
			}
		});
		// uPanelMsg.setPreferredSize();
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
			ss = new ServerSocket(PORT_NUM); // bind to port
			Thread uThread = new Thread(this);
			uThread.start();
		} catch (IOException ioe) {
			System.out.println("ioe in Server: " + ioe.getMessage());
		}
		updateDBDisplay();
		setVisible(true);

		// set up XML Parsing
		try {
			db = dbf.newDocumentBuilder();
			doc = db.parse(new File(DB));
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			uTextAreaMsg.append("Error: Database not found. \n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized String processMsg(String msgReceived, ClientHandler ch) {
		Scanner uScan = new Scanner(msgReceived);
		String msgSend = null;
		String sIP;
		String msgType;
		String msgBody;
		// Determine ip address of client:
		sIP = ch.getSocket().getRemoteSocketAddress().toString();
		switch (msgReceived.charAt(0)) {
		case 'H':
			msgSend = "Dear client, the server deems this day to be beautiful as well.";
			break;
		}
		msgType = uScan.next();
		switch (msgType) {
			case "CREATE_PLAYER":
				String sUsername = uScan.next();
				String sCharacter = uScan.next();
				if(isUsernameNotTaken(sUsername)){
					//XML add player node goes here...
					if(addUsernameToXML(sUsername,sCharacter,sIP))
						msgSend = "CREATE_PLAYER SUCCESS";
					else
						msgSend = "CREATE_PLAYER FAILURE XML_ERROR";
				}
				else{
					msgSend = "CREATE_PLAYER FAILURE USERNAME_ALREADY_EXISTS";
				}
				break;
			case "GET_PLAYER_STATUS":
				msgSend = getPlayerStatus(sIP);
				break;
			case "GET_PLAYER_GAME_INDEX":
				
				break;
			case "GET_PLAYER_INFO":
				msgSend = getPlayerInfo(sIP);
				break;
			case "SET_PLAYER_INFO":
				msgSend = setPlayerInfo(sIP, uScan);
				break;
			case "JOIN_GAME":
				//msgSend = joinGame(sIP,uScan.next());
				break;
			case "READY_GAME":
				msgSend = readyGame(sIP);
				break;
			case "LEAVE_SCOREBOARD":
				msgSend = leaveScoreboard(sIP);
				break;
		}
		
		// Print msg transaction to the screen:
		uTextAreaMsg.append((nNumLine++) + ". CMD: " + msgReceived + "\n");
		uTextAreaMsg.append((nNumLine++) + ". ECHO: " + msgSend + "\n");

		// Update the display of the database after modifications have been
		// made:
		updateDBDisplay();

		return msgSend;
	}

	public static void updateDBDisplay() {
		try {
			// Open the XML DB reader:
			BufferedReader br;
			br = new BufferedReader(new FileReader(DB));

			// Clear the DB textarea:
			uTextAreaDB.setText("");
			// Write the contents of the XML DB to the DB textarea:
			String line;
			line = br.readLine();
			while (line != null) {
				uTextAreaDB.append(line + "\n");
				line = br.readLine();
			}
			// Close the reader:
			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeClient(ClientHandler ch) {
		chVector.remove(ch);
		uLabelConnections.setText("Connections: " + chVector.size());
	}

	public void run() {
		// Continually accept new client connections:
		Socket s;
		while (true) {
			try {
				// Wait for new clients to connect:
				s = ss.accept();
				ClientHandler ch = new ClientHandler(s, this);
				chVector.add(ch);
				ch.start();
				uLabelConnections.setText("Connections: " + chVector.size());
			} catch (IOException e) {
				e.printStackTrace();
			} // blocking line: waiting state of thread
		}
	}

	public static void main(String[] args) {
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
	
	public synchronized static String leaveScoreboard(String ip){
		String msg = "LEAVE_SCOREBOARD FAILURE";
		
		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			if(nList.item(i).getParentNode().getParentNode().toString().contains("games_history")){
				String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
				if (ip.equals(uIP)){
					//reset info
					myEl.getElementsByTagName("score").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("comets").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("deaths").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("powerups").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("max_spin").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("max_vel").item(0).setTextContent(0+"");
					myEl.getElementsByTagName("ready").item(0).setTextContent("false");
					
					//Move player
					NodeList mList = doc.getElementsByTagName("players_available");
					mList.item(0).appendChild(myEl);
					nList.item(i).getParentNode().removeChild(nList.item(i));
					
					flag = true;

					break;
				}
			}
		}
		
		if(flag && writeToXML()) msg = "LEAVE_SCOREBOARD SUCCESS";
		
		return msg;
	}
	
	public synchronized static String readyGame(String ip){
		String msg = "READY_GAME FAILURE";
		
		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)){
				myEl.getElementsByTagName("ready").item(0).setTextContent("true");
				
				flag = true;
				
				break;
			}
		}
		
		if(flag && writeToXML()) msg = "READY_GAME SUCCESS";
		
		return msg;
	}
	
	public synchronized static String setPlayerInfo(String uIP, Scanner uMsg){
		String msg = "SET_PLAYER_INFO FAILURE";
		
		boolean flag = false;
		
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			String ip = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)){
				//Set info
				myEl.getElementsByTagName("username").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("character").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("ready").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("score").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("comets").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("deaths").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("powerups").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("max_spin").item(0).setTextContent(uMsg.next());
				myEl.getElementsByTagName("max_vel").item(0).setTextContent(uMsg.next());
				
				flag = true;
				
				break;
			}
		}
		
		if(flag && writeToXML()) msg = "SET_PLAYER_INFO SUCCESS";
		
		return msg;
	}
	
	public synchronized static String getPlayerInfo(String uIP){
		String msg = "GET_PLAYER_INFO FAILURE";
		
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			String ip = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)){
				msg = "GET_PLAYER_INFO";
				msg += " " + myEl.getElementsByTagName("username").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("character").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("ready").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("score").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("comets").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("deaths").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("powerups").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("max_spin").item(0).getTextContent();
				msg += " " + myEl.getElementsByTagName("max_vel").item(0).getTextContent();
				
				break;
			}
		}
		
		return msg;
	}
	
	public synchronized static String getPlayerStatus(String uIP){
		String msg = "GET_PLAYER_STATUS PLAYER_NOT_FOUND";
		
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			String ip = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)){
				if(nList.item(i).getParentNode().toString().contains("players_available")){
					msg = "GET_PLAYER_STATUS AVAILABLE";
				} else  if(nList.item(i).getParentNode().toString().contains("games_available")){
					msg = "GET_PLAYER_STATUS WAITING";
				} else  if(nList.item(i).getParentNode().toString().contains("games_active")){
					msg = "GET_PLAYER_STATUS ACTIVE";
				} else  if(nList.item(i).getParentNode().toString().contains("games_history")){
					msg = "GET_PLAYER_STATUS SCOREBOARD";
				}
				break;
			}
		}
		
		return msg;
	}

	public synchronized static boolean isUsernameNotTaken(String uName) {
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element)nList.item(i);
			String name = myEl.getElementsByTagName("username").item(0).getTextContent();
			if (uName.equals(name)){
				if(nList.item(i).getParentNode().toString().contains("players_available")){
					//erase this player
					nList.item(i).getParentNode().removeChild(nList.item(i));
				} else 
					return false;
			}
		}

		return true;
	}

	public synchronized static boolean addUsernameToXML(String uName,
			String Character, String ipAddress) {
		// get parent
		NodeList parentList = doc.getElementsByTagName("players_available");
		System.out.println(parentList.toString());
		Node parent = parentList.item(0);
		Element player = doc.createElement("player"); // ip_address=\""+ipAddress+"\" username=\""+uName+"\" character=\""+Character+"\"
		parent.appendChild(player);

		Element username = doc.createElement("username");
		username.setTextContent(uName);
		player.appendChild(username);

		Element character = doc.createElement("character");
		character.setTextContent(Character);
		player.appendChild(character);

		Element ip = doc.createElement("ip_address");
		ip.setTextContent(ipAddress);
		player.appendChild(ip);

		Element ready = doc.createElement("ready");
		ready.setTextContent("false");
		player.appendChild(ready);

		Element score = doc.createElement("score");
		score.setTextContent("0");
		player.appendChild(score);

		Element comets = doc.createElement("comets");
		comets.setTextContent("0");
		player.appendChild(comets);

		Element deaths = doc.createElement("deaths");
		deaths.setTextContent("0");
		player.appendChild(deaths);

		Element powerups = doc.createElement("powerups");
		powerups.setTextContent("0");
		player.appendChild(powerups);

		Element max_spin = doc.createElement("max_spin");
		max_spin.setTextContent("0");
		player.appendChild(max_spin);

		Element max_vel = doc.createElement("max_vel");
		max_vel.setTextContent("0");
		player.appendChild(max_vel);

		Element notifications = doc.createElement("notifications");
		player.appendChild(notifications);

		return writeToXML();
	}

	public synchronized static boolean writeToXML() {
		try {
			// write the updated document to file or console
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(DB));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			uTextAreaMsg.append("XML file updated successfully");
			updateDBDisplay();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
