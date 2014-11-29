package finalProject.Server;

import java.io.File;
import finalProject.Server.ClientHandler;

import java.io.IOException;

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
		case 'U':
			String uName = msgReceived.substring(msgReceived.indexOf('%'), msgReceived.indexOf('#'));
			if (isUsernameNotTaken(uName)) {
				msgSend = "Adding Username to XML...";
				String Character = msgReceived.substring(msgReceived.indexOf('#'), msgReceived.indexOf('$'));
			} else {
				msgSend = "Username is Taken";
			}
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

	public synchronized static boolean isUsernameNotTaken(String uName) {
		NodeList nList = doc.getElementsByTagName("player");
		for (int i = 0; i < nList.getLength(); i++) {
			String name = nList.item(i).getAttributes().getNamedItem("username").toString().replaceAll("username=", "");
			name = name.replaceAll("\"", "");
			if (uName.equals(name))
				return false;
		}

		return true;
	}

	public synchronized static boolean addUsernameToXML(String uName, String Character, String ipAddress) {
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

	private static void addElement(Document doc) {
		NodeList employees = doc.getElementsByTagName("Employee");
		Element emp = null;

		// loop for each employee
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			Element salaryElement = doc.createElement("salary");
			salaryElement.appendChild(doc.createTextNode("10000"));
			emp.appendChild(salaryElement);
		}
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