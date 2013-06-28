package chatserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

import chatclient.ChatMessage;

public class Server implements Runnable {
	public static volatile ArrayList<ClientConnection> connections; //connections är en ArrayList av klassen ClientConnection
	//det finns enbart en i servern vilket innebär att den är static. Volatile används för att undvika buggar när flera trådar använder sig av listan.
	public static ServerSocket myServer; //Servern har en socket totalt som tar emot all data från alla klienter.

	public void init(int port) {
		
		connections = new ArrayList<ClientConnection>();
		System.out.println("Server starting up...");
		try {
			myServer = new ServerSocket(port); 
			System.out.println("Server online at localhost:"+port); 
			new Listener(); 
			new Thread(this).start(); //skapar en ny tråd för den här servern se nedan för vad denna tråd gör
		} catch (IOException e) {
			System.err.println("ERROR! Could not start server on port "+ port + e.getMessage());
			System.exit(1); //tråden stängs om porten är blockerad eller upptagen eller om något gått snett
		}
	}
	
	static void serverSendToAll(ChatMessage cm) throws IOException { //Statisk metod för att sända ut meddelanden till alla chatklienter
		cm.setUsername("SYSTEM");
		for (ClientConnection c : Server.connections ) {	
			c.getOut().flush();  //Flushar klientens ström så att det inte ligger något skräp i den för nästa input. 
			c.getOut().writeObject(cm); //skriver ut ett meddelande till varje klient
		}
	}

	@Override
	public void run() {
		while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				ClientConnection toRemove = null; //skapar ett tomt objekt som tas bort efter underliggande loop, detta eftersom man inte kan ta bort objekt inuti en loop.
				ClientConnection nextClient = null;
				ArrayList<String> stillOnline = new ArrayList<String>();
				for(ClientConnection c : connections) {
					ObjectOutputStream toClient = c.getOut();  
		            try { 
		            	toClient.flush();
						toClient.writeObject("test"); //testar att skicka ett test objekt till klienten för att se om den svarar, detta är typ en ping
						stillOnline.add(c.getUsername()); //svarar klienten så läggs namnet på klienten till i online listan
						System.out.println("Client "+c.getUsername()+" running");
		            } catch (IOException e) {
						System.out.println("Client closed"); 
						toRemove = c; //blir det ett exception så tas denna klient bort
					}
				}
				if(toRemove!=null) {
					connections.remove(toRemove); //klienten tas bort från connections listan
					try {
						serverSendToAll(new ChatMessage(toRemove.getUsername()+" Closed connection"));
					} catch (IOException e) {
						System.err.println("Can't broadcast");
					}
				}
				
				for(ClientConnection c2 : connections) {  
					ObjectOutputStream toClient = c2.getOut();
					try {
						toClient.flush();
						toClient.writeObject(stillOnline); //skickar ut en lista på de användare som fortfarande är online till varje användare
					} catch (IOException e) {
						System.err.println("Can't send onlinelist"); //inträffar antingen om det inte finns några klienter inkopplade till servern eller om något gått snett.
					}
				}
		}
	}
}
