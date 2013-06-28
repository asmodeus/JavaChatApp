package chatserver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;

import chatclient.ChatMessage;

public class ClientConnection implements Runnable {
	
	public volatile Socket thisClient;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String username;
	
	public ClientConnection(Socket s) {
		this.thisClient = s;
		try {
			out = new ObjectOutputStream(s.getOutputStream());
			in = new ObjectInputStream(s.getInputStream());
			username = s.getLocalSocketAddress().toString();
			Server.serverSendToAll(new ChatMessage(username+" has connected."));
		} catch (IOException e) {
			System.out.println("ERROR ! "+e.getMessage());
		}
		new Thread(this).start();
	}
	
	public ObjectOutputStream getOut() {
		return out;
	}
	public Socket getSocket() {
		return thisClient;
	}
	
	private void SendToAll(ChatMessage cm) throws IOException {
		cm.setUsername(username); //skickar ut ett ChatMessage objekt till alla användare
		for (ClientConnection c : Server.connections ) {
			c.getOut().flush();
			c.getOut().writeObject(cm);
		}
		//PRINT NEW MESSAGE
		Calendar c = Calendar.getInstance(); //hämtar datumet för då meddelandet sänds
		System.out.println("["+String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))+"] "+cm.getUsername()+": "+cm.getChatMessage());
	}
	
	@Override
	public void run() {
		//Start thread for each client on server.
		System.out.println("Seperate client thread running on server.");
			while(true){
				try {
					ChatMessage cm = (ChatMessage) in.readObject(); //läser ett cm objekt som användaren skickar
					String message = cm.getChatMessage();
					if(message.substring(0, 1).equals("/")) { //kör speciella cases som cm börjar med '/'
						System.out.println("Command detected");
						String command;
						try {
							command = message.substring(1, 5);
						} catch(StringIndexOutOfBoundsException e) {
							command = "";
						}
						System.out.println(command); 
						switch(command) {
							case "name":
							case "nick": {
								if(message.substring(5).length()>1) {
									String newUsername = message.substring(6);
									if(!newUsername.toLowerCase().equals("system") && !newUsername.toLowerCase().equals("admin") && !newUsername.toLowerCase().equals("server")) { //ser till att nicket inte är system admin eller server
										if(!usernameExists(newUsername)) {
											cm = new ChatMessage("");
											Server.serverSendToAll(new ChatMessage(username+" changed name to "+newUsername));
											this.username = newUsername;
										}
									}
								}
							}
							
							case "ping": {
								
							}
						
						}
					}
					if(!cm.getChatMessage().equals("") || cm.getChatMessage().equals(" ")) { //ser till att meddelandet inte är ett blankspace						
						SendToAll(cm);
					}
				} catch (IOException e) {
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}	
				try {
					Thread.sleep(10); //servern kollar tio gånger per sekund efter meddelanden från varje klient
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}
	
	//övriga metoder
	
	public String getUsername() {
		return this.username;
	}
	
	public boolean usernameExists(String username) { 
		String u = username.toLowerCase();
		for(ClientConnection c : Server.connections) {
			if(c.getUsername().toLowerCase().equals(u)) return true;
			else return false;
		}
		return true;
	}
}
