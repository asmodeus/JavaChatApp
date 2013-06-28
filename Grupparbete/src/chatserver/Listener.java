package chatserver;

import java.io.IOException;
import java.net.Socket;

public class Listener implements Runnable {

	private boolean listening;

	Listener() {
		listening = true;
		new Thread(this).start(); //Run anropas för denna klassen när listener blir constructad
	}

	@Override
	public void run() {
		while (listening) {
			System.out.println("Listening for connections...");
			boolean newCon = false;
			Socket c = null;
			try {
				c = Server.myServer.accept(); //Listener tråden sitter och vänter på inkommande connections
				newCon = true;
			} catch (IOException e) {
				System.err.println("Client failed to connect! "+ e.getMessage());
			}
			if (newCon && c != null) {
				ClientConnection temp = new ClientConnection(c); //ClientConnection anropas och konstueras, vilket skapar
				//en ny separat ClientConnection tråd. Det är alltså en tråd för varje klient anslutning.
				Server.connections.add(temp);
				System.out.println("Client connected! ["+c.getLocalAddress()+"]");
			}	
		}
		try {
			System.out.println("Server shutting down");
			Server.myServer.close(); //När klienten inte lyssnar längre så stängs myServer socketen
		} catch (IOException e) {
			System.err.println("ERROR! Can't close server! " + e.getMessage());
		}
	}

	public void close() { //används denna metod?
		listening = false; 
	}

}
