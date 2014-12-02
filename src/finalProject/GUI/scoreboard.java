package finalProject.GUI;
import finalProject.Client.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.File;


public class scoreboard extends JFrame {

	public static String serverIPAddress;
	static String msg="GET_PLAYER_INFO";
	
	//define all scoreboard values for both players
	public static String allPlayerInfoP1;
	public static String allPlayerInfoP2;
	public static String[] array1 = new String[10];
	public static String[] array2 = new String[10];
	public static String usernameP1;
	public static String characterP1;
	public static String damageP1;
	public static String cometsP1;
	public static String deathsP1;
	public static String powerupsP1;
	public static String max_velP1;
	public static Vector<String> usernames = new Vector<String>();
	public static Vector<String> characters = new Vector<String>();
	public static Vector<String> damages = new Vector<String>();
	public static Vector<String> comets = new Vector<String>();
	public static Vector<String> deaths = new Vector<String>();
	public static Vector<String> powerups = new Vector<String>();
	public static Vector<String> maxvels = new Vector<String>();

	public scoreboard() throws IOException{
        //retrieves all player info
    	//format: "GET_PLAYER_INFO username character ready score comets deaths powerups max_spin max_vel"
    	allPlayerInfoP1 = Client.sendMsg(msg);
    	
    	//splits all player info by whitespace into array
    	//format: [GET_PLAYER_INFO, username, character, ready, score, comets, deaths, powerups, max_spin, max_vel]
    	array1 = allPlayerInfoP1.split("\\s+");
		
    	//assign all values in the array to their respective score board values
    	usernameP1 = array1[1];
    	characterP1 = array1[2];
    	damageP1 = array1[4];
    	cometsP1 = array1[5];
    	deathsP1 = array1[6];
    	powerupsP1 = array1[7];
    	max_velP1 = array1[9];
    	
    	Scanner uScan;
    	
		//Get the index of the active game that the current player is in:
		msg = Client.sendMsg("GET_PLAYER_GAME_INDEX");
		if(msg.equals("GET_PLAYER_GAME_INDEX FAILURE")){
			return;
		}
		uScan = new Scanner(msg);
		uScan.next();
		int nGameIndex = Integer.parseInt(uScan.next());
		uScan.close();
			
		// update enemy score
			msg = Client.sendMsg("GET_PLAYER_INDEX " + nGameIndex);
			if(msg.equals("GET_PLAYER_INDEX FAILURE")){
				return;
			}
			uScan = new Scanner(msg);
			uScan.next();
			int nPlayerIndex = Integer.parseInt(uScan.next());
			uScan.close();
    	
		//Get the number of players
		msg = Client.sendMsg("GET_GAME_ACTIVE_NUM_PLAYERS " + nGameIndex);
		if(msg.equals("GET_GAME_ACTIVE_NUM_PLAYERS FAILURE")){
			return;
		}
		uScan = new Scanner(msg);
		uScan.next();
		int numPlayers = Integer.parseInt(uScan.next());
		uScan.close();
		
		for(int i=0; i<numPlayers; i++){
			if(i != nPlayerIndex){
				msg = Client.sendMsg("GET_GAME_ACTIVE_PLAYER " + nGameIndex + " " + i);
				if(msg.equals("GET_GAME_ACTIVE_PLAYER FAILURE")){
					continue;
				}
		    	array2 = msg.split("\\s+");
				//Parse the message returned to get the score from the player info:
				//username, character, bReady, nScore, nComets, nDeaths, nPowerUps, nMaxSpin, nMaxVel
		    	usernames.add(array2[1]);
		    	characters.add(array2[2]);
		    	damages.add(array2[4]);
		    	comets.add(array2[5]);
		    	deaths.add(array1[6]);
		    	powerups.add(array2[7]);
		    	maxvels.add(array2[9]);
			}
		}
		
		setTitle("A Hole in the Universe");
		setSize(950,650);
		setLocationRelativeTo(null);
		
		
		//add instance of ImagePanel, which takes the name of an image as input to construct a background image.
		//This class is also overriden by the paintComponent method to draw the table and title, and adds the 
		//Quit and Home JButtons.
		add(new ImagePanel("Backgrounds/universe1.jpg"));
		
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	
	public static class ImagePanel extends JPanel {

        BufferedImage img;
        BufferedImage planet;
       
        //constructor for ImagePanel- takes name of file and constructs a background image by adding it to a JLabel.
        ImagePanel(String name) {
            this.add(new JLabel(name));
            try {
                img = ImageIO.read(new File(name));
                planet = ImageIO.read(new File(name));
                this.setPreferredSize(new Dimension(
                    img.getWidth(), img.getHeight()));
                this.setPreferredSize(new Dimension(planet.getWidth(),planet.getHeight()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        
            
            //declares and adds the Quit and To Home JButtons
        	JButton quitButton = new JButton("Quit");
        	JButton toHomeButton = new JButton("To Home");
        	
        	quitButton.setFont(new Font("American Typewriter", Font.PLAIN, 15));
        	toHomeButton.setFont(new Font("American Typewriter", Font.PLAIN, 15));
        	setLayout(null);
        	toHomeButton.setLocation(560,530);
        	toHomeButton.setSize(100,50);
        	quitButton.setLocation(350,530);
        	quitButton.setSize(100,50);
        	this.add(toHomeButton);
        	this.add(quitButton);
        	

        	//exits application when clicked
    		quitButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae){
    				System.exit(0);
    			}
    				
    			});
    		//show Home Page card in CardLayout implementation
    		toHomeButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae){
    				
    			}
    				
    			});
            
            //displays character image for player-chosen character in the username table cell
    		if (array1[2].equals("Earth")){
        		ImageIcon imgEarth= new ImageIcon("Icons/Earth.png");
        		Image imageEarth = imgEarth.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgEarth = new ImageIcon(newImage);
        		JLabel labelEarth = new JLabel(imgEarth);
        		labelEarth.setSize(60,50);
        		labelEarth.setLocation(95,300);
        		this.add(labelEarth);
    		}
    		else if (array1[2].equals("Neslaou")){
    			ImageIcon imgNeslaou= new ImageIcon("Icons/Neslaou.png");
        		Image imageEarth = imgNeslaou.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgNeslaou = new ImageIcon(newImage);
        		JLabel labelNeslaou = new JLabel(imgNeslaou);
        		labelNeslaou.setSize(60,50);
        		labelNeslaou.setLocation(95,300);
        		this.add(labelNeslaou);
        		
    		}
    		else if (array1[2].equals("Gigolo")){
    			ImageIcon imgGigolo= new ImageIcon("Icons/Gigolo.png");
        		Image imageEarth = imgGigolo.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgGigolo = new ImageIcon(newImage);
        		JLabel labelGigolo = new JLabel(imgGigolo);
        		labelGigolo.setSize(60,50);
        		labelGigolo.setLocation(95,300);
        		this.add(labelGigolo);
    		}
    		else if (array1[2].equals("OraUhlsax")){
    			ImageIcon imgOU= new ImageIcon("Icons/OU2.png");
        		Image imageOU = imgOU.getImage();
        		Image newImage = imageOU.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgOU = new ImageIcon(newImage);
        		JLabel labelOU = new JLabel(imgOU);
        		labelOU.setSize(60,50);
        		labelOU.setLocation(95,300);
        		this.add(labelOU);
    		}
    		else if (array2[2].equals("Earth")){
    			ImageIcon imgEarth= new ImageIcon("Icons/Earth.png");
        		Image imageEarth = imgEarth.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgEarth = new ImageIcon(newImage);
        		JLabel labelEarth = new JLabel(imgEarth);
        		labelEarth.setSize(60,50);
        		labelEarth.setLocation(95,395);
        		this.add(labelEarth);
    		}
    		else if (array2[2].equals("Neslaou")){
    			ImageIcon imgNeslaou= new ImageIcon("Icons/Neslaou.png");
        		Image imageEarth = imgNeslaou.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgNeslaou = new ImageIcon(newImage);
        		JLabel labelNeslaou = new JLabel(imgNeslaou);
        		labelNeslaou.setSize(60,50);
        		labelNeslaou.setLocation(95,395);
        		this.add(labelNeslaou);
    		}
    		else if (array2[2].equals("Gigolo")){
    			ImageIcon imgGigolo= new ImageIcon("Icons/Gigolo.png");
        		Image imageEarth = imgGigolo.getImage();
        		Image newImage = imageEarth.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgGigolo = new ImageIcon(newImage);
        		JLabel labelGigolo = new JLabel(imgGigolo);
        		labelGigolo.setSize(60,50);
        		labelGigolo.setLocation(95,395);
        		this.add(labelGigolo);
    		}
    		else if (array2[2].equals("OraUhlsax")){
    			ImageIcon imgOU= new ImageIcon("Icons/OU2.png");
        		Image imageOU = imgOU.getImage();
        		Image newImage = imageOU.getScaledInstance(100,100, java.awt.Image.SCALE_SMOOTH);
        		imgOU = new ImageIcon(newImage);
        		JLabel labelOU = new JLabel(imgOU);
        		labelOU.setSize(60,50);
        		labelOU.setLocation(95,395);
        		this.add(labelOU);
    		}
        }
       

        @Override
        protected void paintComponent(Graphics g) {
       	    //draws the background image to fit the size of the JFrame
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
            

            //sets color and font for the Score Board title
            g.setColor(Color.WHITE);
            g.setFont(new Font("American Typewriter", Font.PLAIN, 40));
            g.drawString("Score Board", 380,100);
            
           
            //draws table row and column outlines
            g.drawLine(50,200,970,200);
            g.drawLine(50,450,970,450);
            g.drawLine(50,200,50,450);
            g.drawLine(970,200,970,450);
            g.drawLine(50, 262, 970, 262);
            g.drawLine(50,355,970,355);
            
            g.drawLine(203, 200, 203, 450);
            g.drawLine(356,200,356,450);
            g.drawLine(509,200,509,450);
            g.drawLine(662,200,662,450);
            g.drawLine(815,200,815,450);
            
            
            //draws column headers after setting font and text size
            g.setFont(new Font("American Typewriter",Font.ITALIC,12));
            g.drawString("Username",95,235);
            g.drawString("Damage",260,235);
            g.drawString("Comets Knocked Out",375,235);
            g.drawString("Max Velocity",550,235);
            g.drawString("Power Ups Used",690,235);
            g.drawString("Total Deaths", 855, 235);
            
            //draws player 1 info in the font of the corresponding planet they chose during Create Profile
            if (array1[2].equals("Neslaou")){
            	g.setFont(new Font("Cardinal",Font.BOLD,12));
            }
            else if (array1[2].equals("Gigolo")){
            	g.setFont(new Font("Hatch",Font.BOLD,12));
            }
            else if (array1[2].equals("Earth")){
            	g.setFont(new Font("Earth Kid",Font.BOLD,12));
            	
            }
            else if (array1[2].equals("OraUhlsax")){
            	g.setFont(new Font("Prakrta",Font.BOLD,12));
            	
            }
            else{
            	g.setFont(new Font("American Typewriter",Font.ITALIC,12));
            }
            //draws player 1 info into the table 
            g.drawString(usernameP1, 85,310);
            g.drawString(damageP1,245,310);
            g.drawString(cometsP1,400,310);
            g.drawString(max_velP1,565,310);
            g.drawString(powerupsP1,710,310);
            g.drawString(deathsP1,860,310);
            
          //draws player 2 info in the font of the corresponding planet they chose during Create Profile
            if (array2[2].equals("Neslaou")){
            	g.setFont(new Font("Cardinal",Font.BOLD,12));
            }
            else if (array2[2].equals("Gigolo")){
            	g.setFont(new Font("Hatch",Font.BOLD,12));
            }
            else if (array2[2].equals("Earth")){
            	g.setFont(new Font("Earth Kid",Font.BOLD,12));
            	
            }
            else if (array2[2].equals("OraUhlsax")){
            	g.setFont(new Font("Prakrta",Font.BOLD,12));
            	
            }
            else{
            	g.setFont(new Font("American Typewriter",Font.ITALIC,12));
            }
            
            //draws player 2 info into the table
            g.drawString(usernameP2, 85,405);
            g.drawString(damageP2,245,405);
            g.drawString(cometsP2,400,405);
            g.drawString(max_velP2,565,405);
            g.drawString(powerupsP2,710,405);
            g.drawString(deathsP2,860,405);
	
            }

        }
        
//main method creates instance of scoreBoardGUI()
public static void main (String [] args) throws IOException, ParserConfigurationException, SAXException{
	
	scoreboard instance = new scoreboard();}
	
}

