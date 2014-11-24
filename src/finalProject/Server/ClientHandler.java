package finalProject.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
	private Socket uSocket;
	private Server uServer;
	private PrintWriter pw;
	private BufferedReader br;
	
	public ClientHandler(Socket uSocket, Server uServer) {
		this.uSocket = uSocket;
		this.uServer = uServer;
		try {
			this.pw = new PrintWriter(uSocket.getOutputStream());
			this.br = new BufferedReader(new InputStreamReader(uSocket.getInputStream()));
		} catch (IOException ioe) {
			System.out.println("ioe in ChatThread constructor: " + ioe.getMessage());
		}
		this.start();
	}
	
	public Socket getSocket(){
		return this.uSocket;
	}
	
	public void run(){
		while(true){
			try {
				String msgReceived;
				String msgSend;
				msgReceived = br.readLine();
				msgSend = uServer.processMsg(msgReceived, this);
				pw.println(msgSend);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
