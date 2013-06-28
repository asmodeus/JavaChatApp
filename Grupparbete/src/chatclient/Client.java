package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import clientgui.ChatPanel;


public class Client implements Runnable {
	
	private Socket serverConnection;
	private boolean isConnected;
	private ObjectOutputStream out;
	private ObjectInputStream inO;
	private BufferedReader in;
	
	private String username;
	private ChatPanel gui;

	
	
	public Client(String address, int port, ChatPanel panel) {
		gui = panel;

		// Connecting to a server
		try {
			 serverConnection = new Socket(address, port);
			 System.out.println("Connected succesfully!");
			 username = serverConnection.getLocalAddress().toString();
			 isConnected = true;
			 out = new ObjectOutputStream(serverConnection.getOutputStream());
			 inO = new ObjectInputStream(serverConnection.getInputStream());


			 in = new BufferedReader(new InputStreamReader(System.in));
			 

		} catch (UnknownHostException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);		
		}
		new Thread(this).start();
	}
	
	void write() throws IOException {
		 //This method just sends a chat string and leaves the sender field blank for now
		 //The sender is made on the server instead.
		 System.out.println("Enter message:");
		 String str = in.readLine();
		 out.flush();
		 out.writeObject(new ChatMessage(str));
	}
	
	//cloned the following method from the one above just with String arg in order to test sending via GUI
	public void writeFromFrame(String text) throws IOException {
		//This method just sends a chat string and leaves the sender field blank for now
		//The sender is made on the server instead.
		if(text.equals("")) return;
		out.flush();
		out.writeObject(new ChatMessage(text));
	}
	
	
	
	public void closing() {
		try {
			inO.close();
			out.close();
		    serverConnection.close();
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public String getUsername() {
		return this.username;
	}
	
	//added two getters below for temporary communication's purposes with GUI
	
	public Socket getServerConnection(){
		return this.serverConnection;
	}
	
	public boolean getConnectionStatus(){
		return this.isConnected;
	}


	@Override
	public void run() { //Listens for incoming messages and for server termination
		while(isConnected){
			//Removed hearbeat from code
				try {
					Object o = inO.readObject();
					if(o instanceof ChatMessage) {						
						ChatMessage cm = (ChatMessage) o;
						Calendar c = Calendar.getInstance();
						System.out.println("["+String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))+"] "+cm.getUsername()+": "+cm.getChatMessage());
						gui.addToText("["+String.format("%02d:%02d:%02d", c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND))+"] "+cm.getUsername()+": "+cm.getChatMessage());
					}
					else if(o instanceof ArrayList) {
						ArrayList<String> temp =(ArrayList<String>) o;
						gui.onlineUsers.setText("Online users:                            ");
						for(String usr : temp) {
							gui.onlineUsers.append("\n"+usr);
						}
					}
					//PRINT NEW MESSAGE
				} catch (IOException e) {
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}	
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	}
}