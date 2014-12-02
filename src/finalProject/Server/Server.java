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
import java.awt.Rectangle;
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
import javax.swing.SwingUtilities;
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
	private static JScrollPane uScrollPaneMsg;
	private static JScrollPane uScrollPaneDB;
	private static JButton uClearButton;
	private static JLabel uLabelConnections;
	private static int nNumLine = 1;
	private static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private static DocumentBuilder db;
	private static org.w3c.dom.Document doc = null;

	public Server() throws HeadlessException, UnknownHostException {
		super("A Hole In The Universe Server " + InetAddress.getLocalHost().getHostAddress());
		setSize(1300, 700);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(1, 2));
		// The program will close when the window is closed
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
			new ActiveGameCountdown(doc);
			new WaitPlayersReady(doc);
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
		msgType = uScan.next();
		switch (msgType) {
		case "CREATE_PLAYER":
			String sUsername = uScan.next();
			String sCharacter = uScan.next();
			if (isUsernameNotTaken(sUsername)) {
				// XML add player node goes here...
				if (addUsernameToXML(sUsername, sCharacter, sIP))
					msgSend = "CREATE_PLAYER SUCCESS";
				else
					msgSend = "CREATE_PLAYER FAILURE XML_ERROR";
			} else {
				msgSend = "CREATE_PLAYER FAILURE USERNAME_ALREADY_EXISTS";
			}
			break;
		case "GET_PLAYER_STATUS":
			msgSend = getPlayerStatus(sIP);
			break;
		case "GET_PLAYER_GAME_INDEX":
			msgSend = getPlayerGameIndex(sIP);
			break;
		case "GET_PLAYER_INDEX":
			msgSend = getPlayerIndex(sIP);
			break;
		case "GET_PLAYER_INFO":
			msgSend = getPlayerInfo(sIP);
			break;
		case "SET_PLAYER_INFO":
			msgSend = setPlayerInfo(sIP, uScan);
			break;
		case "JOIN_GAME":
			msgSend = joinGame(sIP, uScan.nextInt());
			break;
		case "READY_GAME":
			msgSend = readyGame(sIP);
			break;
		case "LEAVE_SCOREBOARD":
			msgSend = leaveScoreboard(sIP);
			break;
		case "LEAVE_GAME":
			msgSend = leaveGame(sIP);
			break;
		case "DISCONNECT":
			msgSend = disconnect(sIP);
			break;
		case "CREATE_GAME":
			msgSend = createGame(sIP, uScan.next(), uScan.next(), uScan.next());
			break;
		case "DELETE_GAME":
			msgSend = deleteGame(uScan.next());
			break;
		case "GET_NUM_PLAYERS_AVAILABLE":
			msgSend = getNumPlayersAvailable();
			break;
		case "GET_PLAYER_AVAILABLE":
			msgSend = getPlayerAvailable(uScan.nextInt());
			break;
		case "GET_NUM_GAMES_AVAILABLE":
			msgSend = getNumGamesAvailable();
			break;
		case "GET_GAME_AVAILABLE":
			msgSend = getGameAvailable(uScan.nextInt());
			break;
		case "GET_GAME_AVAILABLE_NUM_PLAYERS":
			msgSend = getGameAvailableNumPlayers(uScan.nextInt());
			break;
		case "GET_GAME_AVAILABLE_PLAYER":
			msgSend = getGameAvailablePlayer(uScan.nextInt(), uScan.nextInt());
			break;
		case "GET_NUM_GAMES_ACTIVE":
			msgSend = getNumGamesActive();
			break;
		case "GET_GAME_ACTIVE":
			msgSend = getGameActive(uScan.nextInt());
			break;
		case "GET_GAME_ACTIVE_NUM_PLAYERS":
			msgSend = getGameActiveNumPlayers(uScan.nextInt());
			break;
		case "GET_GAME_ACTIVE_PLAYER":
			msgSend = getGameActivePlayer(uScan.nextInt(), uScan.nextInt());
			break;
		case "GET_GAME_HISTORY_NUM_PLAYERS":
			msgSend = getGameHistoryNumPlayers(uScan.nextInt());
			break;
		case "GET_GAME_HISTORY_PLAYER":
			msgSend = getGameHistoryPlayer(uScan.nextInt(), uScan.nextInt());
			break;
		// Chat
		case "NOTIFICATIONS_GET_NUM":
			msgSend = getNumNotifications(sIP);
			break;
		case "NOTIFICATION_GET":
			msgSend = getNotificationAtIndex(sIP, uScan.nextInt());
			break;
		case "NOTIFY_ALL":
			msgSend = notifyAll(sIP, uScan.nextLine());
			break;
		case "NOTIFY_PLAYER":
			msgSend = notifyPlayer(sIP, uScan.nextInt(), uScan.nextLine());
			break;
		}

		// Print msg transaction to the screen:
		uTextAreaMsg.append((nNumLine++) + ". CMD: " + msgReceived + "\n");
		uTextAreaMsg.append((nNumLine++) + ". ECHO: " + msgSend + "\n");
		uScrollPaneMsg.scrollRectToVisible(new Rectangle(0, uTextAreaMsg.getHeight()-2,1,1));

		// Update the display of the database after modifications have been made:
		updateDBDisplay();

		return msgSend;
	}

	public static void updateDBDisplay() {
		try {
			// Open the XML DB reader:
			BufferedReader br;
			br = new BufferedReader(new FileReader(DB));

			// Write the contents of the XML DB to the DB textarea:
			String line;
			String content = "";
			line = br.readLine();
			while (line != null) {
				content += line + "\n";
				line = br.readLine();
			}
			uTextAreaDB.setText(content + "\n");
			// Close the reader:
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
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
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public synchronized static String joinGame(String ip, int index) {
		String msg = "JOIN_GAME FAILURE";

		Element p = getPlayerElement(ip, false);
		Element r = (Element) p.getParentNode();
		Element game = (Element)((Element)doc.getElementsByTagName("games_available").item(0)).getElementsByTagName("game").item(index);

		if(game != null){
		// add p to game
		game.appendChild(p);
		msg = "JOIN_GAME SUCCESS";
		}

		return msg;
	}

	public synchronized static String getPlayerIndex(String ip) {
		String msg = "GET_PLAYER_INDEX FAILURE";

		Element p = getPlayerElement(ip, true);
		Element r = (Element) p.getParentNode();
		NodeList nList = r.getChildNodes();
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).equals(p)) {
				msg = "GET_PLAYER_INDEX " + i;
				break;
			}
		}

		return msg;
	}

	public synchronized static String getPlayerGameIndex(String ip) {
		String msg = "GET_PLAYER_GAME_INDEX FAILURE";

		Element g = getGameElement(ip);
		Element r = (Element) g.getParentNode();
		NodeList nList = r.getElementsByTagName("game"); //left off here
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).equals(g)) {
				msg = "GET_PLAYER_GAME_INDEX " + i;
				break;
			}
		}

		return msg;
	}

	public synchronized static String notifyPlayer(String ip, int playerIndex, String msgChat) {
		String msg = "NOTIFY_PLAYER FAILURE";

		Element sender = getPlayerElement(ip, true);
		if (sender != null) {
			Element parent = (Element) sender.getParentNode();
			Element player = (Element) parent.getElementsByTagName("player").item(playerIndex);
			if (player != null) {
				msgChat = sender.getElementsByTagName("username").item(0).getTextContent() + ": " + msgChat;
				if (sendChatMessage(msgChat, sender) && sendChatMessage(msgChat, player)) {
					if (writeToXML())
						msg = "NOTIFY_PLAYER SUCCESS";
				}
			}
		}

		return msg;
	}

	public synchronized static String notifyAll(String ip, String msgChat) {
		String msg = "NOTIFY_ALL FAILURE";
		boolean flag = true;

		// create Message
		Element p = getPlayerElement(ip, true);
		String Vorname = p.getElementsByTagName("username").item(0).getTextContent();
		msgChat = Vorname + ": " + msgChat;
		Element parent = (Element)p.getParentNode();

		// Get Nodelist of all players' notifications
		NodeList nList = parent.getElementsByTagName("notifications");
		for (int i = 0; i < nList.getLength(); i++) { // update inboxes
			if (!sendChatMessage(msgChat, (Element) nList.item(i))){
				flag = false;
			}
		}

		if (flag && writeToXML())
			msg = "NOTIFY_ALL SUCCESS";

		return msg;
	}

	public synchronized static boolean sendChatMessage(String message, Element inbox) { 
		// takes in the message and the node of the recipient's inbox
		try {
			// loop through inbox, move every item up one
			NodeList nList = inbox.getElementsByTagName("notification");
			for (int i = 1; i < nList.getLength(); i++) {
				nList.item(i - 1).setTextContent(nList.item(i).getTextContent());
			}

			// add new message
			nList.item(4).setTextContent(message);
			return true;
		} catch (NullPointerException e) {
			return false;
		}
	}

	public synchronized static String getNotificationAtIndex(String ip, int index) {
		String msg = "NOTIFICATION_GET FAILURE";

		Element p = getPlayerElement(ip, true);
		if (p != null) {
			NodeList nList = p.getElementsByTagName("notification");
			if (nList.getLength() >= index) {
				msg = "NOTIFICATION_GET " + nList.item(index).getTextContent();
			}
		}

		return msg;
	}

	public synchronized static String getNumNotifications(String ip) {
		String msg = "NOTIFICATIONS_GET_NUM FAILURE";

		Element p = getPlayerElement(ip, true);
		int numNotes = 0;
		if (p != null) {
			numNotes = p.getElementsByTagName("notification").getLength();
			msg = "NOTIFICATIONS_GET_NUM " + numNotes;
		}

		return msg;
	}

	public synchronized static Element getPlayerElement(String ip, boolean history) {
		Element n = null;

		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)) {
				if (history)
					return myEl;
				else if (!myEl.getParentNode().toString().equals("games_history")) {
					return myEl; // if player is not in games_history
				}
			}
		}

		return n;
	}

	public synchronized static Element getGameElement(String ip) {
		Element n = null;

		Element player = getPlayerElement(ip, false);
		if(player != null){
			Element game = (Element)player.getParentNode();
			return game;
		}
			
		return n;
	}
	
	public synchronized static String getGameActive(int nIndex) {
		String msg = "GET_GAMES_ACTIVE FAILURE"; 
		Element gamesActive = (Element) doc.getElementsByTagName("games_active").item(0);
		if(gamesActive != null){
			Element game = (Element)gamesActive.getElementsByTagName("game").item(nIndex);
			if(game != null){
			String sTitle = game.getAttribute("title");
			String sRemainingTime = game.getAttribute("remaining_time");
			String sTotalTime = game.getAttribute("total_time");
			String sMaxPlayers = game.getAttribute("max_players");
			msg = "GET_GAME_ACTIVE " + sTitle + " " + sRemainingTime + " " + sTotalTime + " " + sMaxPlayers;
			}
		}
		return msg;
	}
	
		
	public synchronized static String getGameActiveNumPlayers(int nGameIndex){
		String msg = "GET_GAME_ACTIVE_NUM_PLAYERS FAILURE";

		int numPlayers = 0;
		Element game = (Element)((Element)doc.getElementsByTagName("games_active").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			numPlayers = game.getElementsByTagName("player").getLength();
			msg = "GET_GAME_ACTIVE_NUM_PLAYERS " + numPlayers;
		}
		return msg;
	}
	
		
	public synchronized static String getGameActivePlayer(int nGameIndex, int nPlayerIndex){
		String msg = "GET_GAME_ACTIVE_PLAYER FAILURE";

		Element game = (Element)((Element)doc.getElementsByTagName("games_active").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			Element player = (Element)game.getElementsByTagName("player").item(nPlayerIndex);
			if (player != null){
				//username, character, bReady, nScore, nComets, nDeaths, nPowerUps, nMaxSpin, nMaxVel
			String sUsername = player.getElementsByTagName("username").item(0).getTextContent();
			String sCharacter = player.getElementsByTagName("character").item(0).getTextContent();
			String sReady = player.getElementsByTagName("ready").item(0).getTextContent();
			String sScore = player.getElementsByTagName("score").item(0).getTextContent();
			String sComets = player.getElementsByTagName("comets").item(0).getTextContent();
			String sDeaths = player.getElementsByTagName("deaths").item(0).getTextContent();
			String sPowerUps = player.getElementsByTagName("powerups").item(0).getTextContent();
			String sMaxSpin = player.getElementsByTagName("max_spin").item(0).getTextContent();
			String sMaxVel = player.getElementsByTagName("max_vel").item(0).getTextContent();

			msg = "GET_GAME_ACTIVE_PLAYER " + sUsername + " " + sCharacter + " " + sReady + " " + sScore + " " + sComets + " " + sDeaths + " " + sPowerUps + " " + sMaxSpin + " " + sMaxVel;
			}
		}
		return msg;
	}
			
	public synchronized static String getGameHistoryPlayer(int nGameIndex, int nPlayerIndex){
		String msg = "GET_GAME_HISTORY_PLAYER FAILURE";

		Element game = (Element)((Element)doc.getElementsByTagName("games_history").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			Element player = (Element)game.getElementsByTagName("player").item(nPlayerIndex);
			String sUsername = player.getElementsByTagName("username").item(0).getTextContent();
			String sCharacter = player.getElementsByTagName("character").item(0).getTextContent();
			String sReady = player.getElementsByTagName("ready").item(0).getTextContent();
			String sScore = player.getElementsByTagName("score").item(0).getTextContent();
			String sComets = player.getElementsByTagName("comets").item(0).getTextContent();
			String sDeaths = player.getElementsByTagName("deaths").item(0).getTextContent();
			String sPowerUps = player.getElementsByTagName("powerups").item(0).getTextContent();
			String sMaxSpin = player.getElementsByTagName("max_spin").item(0).getTextContent();
			String sMaxVel = player.getElementsByTagName("max_vel").item(0).getTextContent();

			msg = "GET_GAME_HISTORY_PLAYER " + sUsername + " " + sCharacter + " " + sReady + " " + sScore + " " + sComets + " " + sDeaths + " " + sPowerUps + " " + sMaxSpin + " " + sMaxVel;
		}
		return msg;
	}

	public synchronized static String getNumGamesActive() {
		String msg = "GET_NUM_GAMES_ACTIVE FAILURE";

		int numGames = 0;
		Element gamesActive = (Element) doc.getElementsByTagName("games_active").item(0);
		if (gamesActive != null) {
			numGames = gamesActive.getElementsByTagName("game").getLength();
			msg = "GET_NUM_GAMES_ACTIVE " + numGames;
		}
		return msg;
	}

	public synchronized static String getNumGamesAvailable() {
		String msg = "GET_NUM_GAMES_AVAILABLE FAILURE";

		int numGames = 0;
		Element gamesAvailable = (Element) doc.getElementsByTagName("games_available").item(0);
		if (gamesAvailable != null) {
			numGames = gamesAvailable.getElementsByTagName("game").getLength();
			msg = "GET_NUM_GAMES_AVAILABLE " + numGames;
		}
		return msg;
	}
	
	public synchronized static String getGameAvailable(int nIndex){
		String msg = "GET_GAME_AVAILABLE FAILURE";
		Element gamesAvailable = (Element) doc.getElementsByTagName("games_available").item(0);
		if(gamesAvailable != null){
			Element game = (Element)gamesAvailable.getElementsByTagName("game").item(nIndex);
			if(game != null){
				String sTitle = game.getAttribute("title");
				String sRemainingTime = game.getAttribute("remaining_time");
				String sTotalTime = game.getAttribute("total_time");
				String sMaxPlayers = game.getAttribute("max_players");
				msg = "GET_GAME_AVAILABLE " + sTitle + " " + sRemainingTime + " " + sTotalTime + " " + sMaxPlayers;
			}
		}
		return msg;
	}
	
	public synchronized static String getGameAvailableNumPlayers(int nGameIndex){
		String msg = "GET_GAME_AVAILABLE_NUM_PLAYERS FAILURE";

		int numPlayers = 0;
		Element game = (Element)((Element)doc.getElementsByTagName("games_available").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			numPlayers = game.getElementsByTagName("player").getLength();
			msg = "GET_GAME_AVAILABLE_NUM_PLAYERS " + numPlayers;
		}
		return msg;
	}
	
	public synchronized static String getGameAvailablePlayer(int nGameIndex, int nPlayerIndex){
		String msg = "GET_GAME_AVAILABLE_PLAYER FAILURE";

		Element game = (Element)((Element)doc.getElementsByTagName("games_available").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			Element player = (Element)game.getElementsByTagName("player").item(nPlayerIndex);
			if(player != null){
				String sUsername = player.getElementsByTagName("username").item(0).getTextContent();
				String sCharacter = player.getElementsByTagName("character").item(0).getTextContent();
				msg = "GET_GAME_AVAILABLE_PLAYER " + sUsername + " " + sCharacter;
			}
		}
		return msg;
	}

	public synchronized static String getNumPlayersAvailable() {
		String msg = "GET_NUM_PLAYERS_AVAILABLE FAILURE";

		int numPlayers = 0;
		Element playersAvailable = (Element) doc.getElementsByTagName("players_available").item(0);
		if (playersAvailable != null) {
			numPlayers = playersAvailable.getElementsByTagName("player").getLength();
			msg = "GET_NUM_PLAYERS_AVAILABLE " + numPlayers;
		}
		return msg;
	}
	
	public synchronized static String getGameHistoryNumPlayers(int nGameIndex){
		String msg = "GET_GAME_HISTORY_NUM_PLAYERS FAILURE";

		int numPlayers = 0;
		Element game = (Element)((Element)doc.getElementsByTagName("games_history").item(0)).getElementsByTagName("game").item(nGameIndex);
		if (game != null) {
			numPlayers = game.getElementsByTagName("player").getLength();
			msg = "GET_GAME_HISTORY_NUM_PLAYERS " + numPlayers;
		}
		return msg;
	}

	public synchronized static String getPlayerAvailable(int nIndex){
		String msg = "GET_PLAYER_AVAILABLE FAILURE";
		Element playersAvailable = (Element) doc.getElementsByTagName("players_available").item(0);
		if(playersAvailable != null){
			Element player = (Element)playersAvailable.getElementsByTagName("player").item(nIndex);
			if(player != null){
				String sUsername = player.getElementsByTagName("username").item(0).getTextContent();
				String sCharacter = player.getElementsByTagName("character").item(0).getTextContent();
				msg = "GET_PLAYER_AVAILABLE " + sUsername + " " + sCharacter;
			}
		}
		return msg;
	}
	
	public synchronized static NodeList getPlayersFromGame(Element g) {
		return g.getElementsByTagName("player");
	}

	public synchronized static String deleteGame(String ip) {
		String msg = "DELETE_GAME FAILURE";

		boolean flag = true;
		// get node
		Element g = getGameElement(ip);
		if (g != null) {
			// move players
			NodeList players = getPlayersFromGame(g);
			for (int i = 0; i < players.getLength(); i++) {
				Element myEl = (Element) players.item(i);
				String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
				if (leaveGame(uIP).contains("FAIL")) {
					flag = false;
				}
			}

			// remove game
			g.getParentNode().removeChild(g);
			if (flag && writeToXML())
				msg = "DELETE_GAME SUCCESS";
		}
		return msg;
	}

	public synchronized static String createGame(String uIP, String title, String numMin, String numPla) {
		String msg = "CREATE_GAME FAILURE";

		// make sure no other games have the same name:
		NodeList games = doc.getElementsByTagName("game");
		for(int i=0; i<games.getLength(); i++){
			Element game = (Element)games.item(i);
			if(game.getAttribute("title").equals(title)){
				if(!game.getParentNode().getNodeName().equals("games_history")){
					return msg; //failure
				}
			}
		}
		
		// create the game:
		NodeList parentList = doc.getElementsByTagName("games_available");
		Node parent = parentList.item(0);
		Element game = doc.createElement("game");
		parent.appendChild(game);
		game.setAttribute("title", title);
		game.setAttribute("remaining_time", numMin);
		game.setAttribute("total_time", numMin);
		game.setAttribute("max_players", numPla);

		if (writeToXML())
			msg = "CREATE_GAME SUCCESS";

		return msg;
	}

	public synchronized static String disconnect(String ip) {
		String msg = "DISCONNECT FAILURE";

		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)) {
				// remove player
				nList.item(i).getParentNode().removeChild(nList.item(i));

				flag = true;

				break;
			}
		}

		if (flag && writeToXML())
			msg = "DISCONNECT SUCCESS";

		return msg;
	}

	public synchronized static String leaveGame(String ip) {
		String msg = "LEAVE_GAME FAILURE";

		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			if (nList.item(i).getParentNode().getParentNode().toString().contains("games_available") || nList.item(i).getParentNode().getParentNode().toString().contains("games_active")) {
				String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
				if (ip.equals(uIP)) {
					// reset info
					myEl.getElementsByTagName("score").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("comets").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("deaths").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("powerups").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("max_spin").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("max_vel").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("ready").item(0).setTextContent("false");

					// Move player
					NodeList mList = doc.getElementsByTagName("players_available");
					mList.item(0).appendChild(myEl);
					//nList.item(i).getParentNode().removeChild(nList.item(i));
					flag = true;
					break;
				}
			}
		}

		if (flag && writeToXML()){
			msg = "LEAVE_GAME SUCCESS";
		}
			
		return msg;
	}

	public synchronized static String leaveScoreboard(String ip) {
		String msg = "LEAVE_SCOREBOARD FAILURE";

		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			if (nList.item(i).getParentNode().getParentNode().toString().contains("games_history")) {
				String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
				if (ip.equals(uIP)) {
					// reset info
					myEl.getElementsByTagName("score").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("comets").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("deaths").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("powerups").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("max_spin").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("max_vel").item(0).setTextContent(0 + "");
					myEl.getElementsByTagName("ready").item(0).setTextContent("false");

					// Move player
					NodeList mList = doc.getElementsByTagName("players_available");
					mList.item(0).appendChild(myEl);
					nList.item(i).getParentNode().removeChild(nList.item(i));

					flag = true;

					break;
				}
			}
		}

		if (flag && writeToXML())
			msg = "LEAVE_SCOREBOARD SUCCESS";

		return msg;
	}

	public synchronized static String readyGame(String ip) {
		String msg = "READY_GAME FAILURE";

		boolean flag = false;
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String uIP = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)) {
				myEl.getElementsByTagName("ready").item(0).setTextContent("true");

				flag = true;

				break;
			}
		}

		if (flag && writeToXML())
			msg = "READY_GAME SUCCESS";

		return msg;
	}

	public synchronized static String setPlayerInfo(String uIP, Scanner uMsg) {
		String msg = "SET_PLAYER_INFO FAILURE";

		boolean flag = false;

		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String ip = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)) {
				// Set info
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

		if (flag && writeToXML())
			msg = "SET_PLAYER_INFO SUCCESS";

		return msg;
	}

	public synchronized static String getPlayerInfo(String uIP) {
		String msg = "GET_PLAYER_INFO FAILURE";

		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String ip = myEl.getElementsByTagName("ip_address").item(0).getTextContent();
			if (ip.equals(uIP)) {
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
				System.out.println(msg);
				break;
			}
		}

		return msg;
	}

	public synchronized static String getPlayerStatus(String uIP) {
		String msg = "GET_PLAYER_STATUS PLAYER_NOT_FOUND";

		NodeList nList = doc.getElementsByTagName("player");
		Element player = getPlayerElement(uIP, true);
		if(nList != null && player != null){
			if (player.getParentNode().getNodeName().equals("players_available")) {
				msg = "GET_PLAYER_STATUS AVAILABLE";
			} 
			else if (player.getParentNode().getParentNode().getNodeName().equals("games_available")) {
				msg = "GET_PLAYER_STATUS WAITING";
			} 
			else if (player.getParentNode().getParentNode().getNodeName().equals("games_active")) {
				msg = "GET_PLAYER_STATUS ACTIVE";
			} 
			else if (player.getParentNode().getParentNode().getNodeName().equals("games_history")) {
				msg = "GET_PLAYER_STATUS SCOREBOARD";
			}
		}
		return msg;
	}

	public synchronized static boolean isUsernameNotTaken(String uName) {
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			Element myEl = (Element) nList.item(i);
			String name = myEl.getElementsByTagName("username").item(0).getTextContent();
			if (uName.equals(name)) {
				if (nList.item(i).getParentNode().toString().contains("players_available") || nList.item(i).getParentNode().getParentNode().toString().contains("games_available")) {
					// erase this player
					nList.item(i).getParentNode().removeChild(nList.item(i));
				} else
					return false;
			}
		}

		return true;
	}

	public synchronized static boolean addUsernameToXML(String uName, String Character, String ipAddress) {
		// get parent
		NodeList parentList = doc.getElementsByTagName("players_available");
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
		
		for(int i = 0; i < 5; i++){
			Element note = doc.createElement("notification");
			notifications.appendChild(note);
		}

		return writeToXML();
	}

	public synchronized static void getPlayerNode() {

	}

	public synchronized static boolean writeToXML() {
		try {
			// write the updated document to file or console
			doc.getDocumentElement().normalize();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(DB));
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			updateDBDisplay();
		} catch (TransformerException e) {
			uTextAreaMsg.append("Unable to update XML file \n");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
