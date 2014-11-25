package finalProject.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	public static final int PORT_NUM = 653;
	private Socket s;
	private BufferedReader br;
	private PrintWriter pw;
	
	public Client(){
		Scanner scan = new Scanner(System.in);
		System.out.print("What is the name/IP of the server? ");
		String hostname = scan.nextLine();
		try{
			this.s = new Socket(hostname, PORT_NUM);
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} catch(IOException ioe){
			System.out.println("ioe in Client: " + ioe.getMessage());
		}
	}
	
	//This function will send a message to the server and wait until the server responds.
	//When the server responds, return the message to the caller.
	//This function is synchronized so that only one thread can send a msg at a time.
	public synchronized String sendMsg(String msgSend){
		PrintWriter pw;
		String msgReceive = null;
		try {
			pw = new PrintWriter(s.getOutputStream());
			pw.println(msgSend);
			pw.flush(); //make sure to flush, if it's yellow let it mellow if its brown flush it down.
			msgReceive = br.readLine();
			while(msgReceive == null){ //wait until the server sends a response
				msgReceive = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msgReceive;
	}
}
