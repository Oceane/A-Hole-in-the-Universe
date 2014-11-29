package finalProject.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import finalProject.Client.Client;

public class connectToServer extends JFrame {
	
	//Server's IP address stored as String value
	public static String serverIPAddress = null;
	
	//constructor for connectToServer GUI
	public connectToServer(){
		super("Connect to Server");
		setSize(950, 650);
		setLocationRelativeTo(null);

		//adds new instance of ImagePanel that will add all components to the frame
		add(new ImagePanel("/Users/natalieanndunn/Documents/workspace/aHoleInTheUniverse/src/aHoleInTheUniverse/space.jpg"));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}

    public static String getServerIp(){
    	return serverIPAddress;
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
            
            
    		JButton quitButton = new JButton("Quit");
    		quitButton.setSize(160,50);
    		quitButton.setLocation(550,500);
    		quitButton.setFont(new Font("Avineer", Font.PLAIN, 15));
    		
    		JButton connectToServerButton = new JButton("Connect to Server");
    		connectToServerButton.setSize(160,50);
    		connectToServerButton.setLocation(300,500);
    		connectToServerButton.setFont(new Font("Avineer", Font.PLAIN, 15));
    		
    		//quit button exits the application once clicked
    		quitButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae){
    				System.exit(0);
    			}
    				
    			});
    		
    		//connect to server button opens a JOptionPane when clicked to prompt the user for a Server IP address
    		connectToServerButton.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent ae){
    				serverIPAddress = JOptionPane.showInputDialog(ImagePanel.this,"Please enter"
    						+ " the Server IP address: ","Connect to Server",
    				JOptionPane.QUESTION_MESSAGE);
    				new Client(serverIPAddress);
    				}
    				
    			});
    		
    		setLayout(null);
    		this.add(quitButton);
    		this.add(connectToServerButton);    
        }
        
        protected void paintComponent(Graphics g) {
        	//draws background image onto frame
        	 g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        	 
        	 g.setColor(Color.WHITE);
             g.setFont(new Font("Avineer", Font.PLAIN, 40));
             g.drawString("Connect to Server", 300,250);}
        }

public static void main (String [] args){
	//creates new instance of connectToServer
	connectToServer instance = new connectToServer();}
}
