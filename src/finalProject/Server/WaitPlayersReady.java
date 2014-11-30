package finalProject.Server;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WaitPlayersReady extends Thread{
	org.w3c.dom.Document doc;
	
	WaitPlayersReady(org.w3c.dom.Document doc){
		this.doc = doc;
		this.start();
	}
	
	private void activateGame(Node gameNode){
		//move to games_history
		Node gamesActiveNode = doc.getElementsByTagName("games_active").item(0);
		if(gamesActiveNode.getChildNodes().getLength() == 0){
			gamesActiveNode.appendChild(gameNode);
		}
		else{
			gamesActiveNode.insertBefore(gameNode, gamesActiveNode.getFirstChild());
		}
	}
	
	public void run(){
		while(true){
			synchronized(this.doc){
				//For each game under the games_available node, check to see if all the player ready states are true.
				//If they are all true, move the game to the games_active node.
				NodeList gamesAvailable = ((Element)this.doc.getElementsByTagName("games_available").item(0)).getElementsByTagName("game");
				for(int i=0; i<gamesAvailable.getLength(); i++){
					NodeList players = ((Element)(gamesAvailable.item(i))).getElementsByTagName("player");
					for(int j=0; j<players.getLength(); j++){
						Node ReadyNode = (Node)((Element)players.item(j)).getElementsByTagName("ready").item(0);
						if(!ReadyNode.getTextContent().equals("true")){
							break;
						}
						if(j == players.getLength() - 1){
							activateGame(gamesAvailable.item(i)); //all the players must have ready states of "true" to get here
						}
					}
				}
				//Write all the changes to the XML file and update the display:
				Server.writeToXML();
			}
		}
	}
	
}
