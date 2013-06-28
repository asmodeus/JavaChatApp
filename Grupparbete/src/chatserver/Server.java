package chatserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;

import chatclient.ChatMessage;

public class Server implements Runnable {
	public static volatile ArrayList<ClientConnection> connections; //connections �r en ArrayList av klassen ClientConnection
	//det finns enbart en i servern vilket inneb�r att den �r static. Volatile anv�nds f�r att undvika buggar n�r flera tr�dar anv�nder sig av listan.
	public static ServerSocket myServer; //Servern har en socket totalt som tar emot all data fr�n alla klienter.

	public void init(int port) {
		
		connections = new ArrayList<ClientConnection>();
		System.out.println("Server starting up...");
		try {
			myServer = new ServerSocket(port); 
			System.out.println("Server online at localhost:"+port); 
			new Listener(); 
			new Thread(this).start(); //skapar en ny tr�d f�r den h�r servern se nedan f�r vad denna tr�d g�r
		} catch (IOException e) {
			System.err.println("ERROR! Could not start server on port "+ port + e.getMessage());
			System.exit(1); //tr�den st�ngs om porten �r blockerad eller upptagen eller om n�got g�tt snett
		}
	}
	
	static void serverSendToAll(ChatMessage cm) throws IOException { //Statisk metod f�r att s�nda ut meddelanden till alla chatklienter
		cm.setUsername("SYSTEM");
		for (ClientConnection c : Server.connections ) {	
			c.getOut().flush();  //Flushar klientens str�m s� att det inte ligger n�got skr�p i den f�r n�sta input. 
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
						toClient.writeObject("test"); //testar att skicka ett test objekt till klienten f�r att se om den svarar, detta �r typ en ping
						stillOnline.add(c.getUsername()); //svarar klienten s� l�ggs namnet p� klienten till i online listan
						System.out.println("Client "+c.getUsername()+" running");
		            } catch (IOException e) {
						System.out.println("Client closed"); 
						toRemove = c; //blir det ett exception s� tas denna klient bort
					}
				}
				if(toRemove!=null) {
					connections.remove(toRemove); //klienten tas bort fr�n connections listan
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
						toClient.writeObject(stillOnline); //skickar ut en lista p� de anv�ndare som fortfarande �r online till varje anv�ndare
					} catch (IOException e) {
						System.err.println("Can't send onlinelist"); //intr�ffar antingen om det inte finns n�gra klienter inkopplade till servern eller om n�got g�tt snett.
					}
				}
		}
	}
}
