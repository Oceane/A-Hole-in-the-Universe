package finalProject.Server;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ActiveGameCountdown extends Thread{
	org.w3c.dom.Document doc;
	
	
	public ActiveGameCountdown(org.w3c.dom.Document doc){
		this.doc = doc;
		this.start();
	}
	
	public void run(){
		try {
			while(true){
				Thread.sleep(1000);
				//Decrement all active game times by 1 second:
				NodeList activeList = this.doc.getElementsByTagName("games_active");
				NodeList gamesList = ((Element)activeList.item(0)).getElementsByTagName("game");
				System.out.println(activeList.getLength());
				for(int i=0; i<gamesList.getLength(); i++){
					Node gameNode = (Node)gamesList.item(i);
					String sTime = gameNode.getAttributes().getNamedItem("remaining_time").getNodeValue();
					int nMinutes = Integer.parseInt(sTime.substring(0, sTime.indexOf(":")));
					int nSeconds = Integer.parseInt(sTime.substring(sTime.indexOf(":") + 1));
					
					//If time is zero, move the game node to the games history node:
					
					//If time is non-zero, subtract 1 second from the time:
					if(nMinutes > 0 || nSeconds > 0){
						nSeconds -= 1;
						if(nSeconds < 0){
							nSeconds = 59;
							nMinutes -= 1;
						}
						gameNode.getAttributes().getNamedItem("remaining_time").setNodeValue(nMinutes + ":" + nSeconds);
					}
					else{
						//move to games_history
					}	
				}
				
				//Write all the changes to the XML file and update the display:
				Server.writeToXML();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
