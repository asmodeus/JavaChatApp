package bookmark;

import java.io.Serializable;

public class Bookmark implements Serializable {


	private String serverName;
	private int portNumber;
	private String serverAdress;

	Bookmark() {
		this("thisComputer", 50505, "127.0.0.1");
	}

	public Bookmark(String name, int portNr, String adress) {
		serverName = name;
		portNumber = portNr;
		serverAdress = adress;
	}

	public void printValues() {
		System.out.println("Object name in memory: "+this.toString());
		System.out.println(this.serverName);
		System.out.println(this.serverAdress);
		System.out.println(this.portNumber);
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	
	
	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	
	
	public String getServerAdress() {
		return serverAdress;
	}

	public void setServerAdress(String serverAdress) {
		this.serverAdress = serverAdress;
	}

	
	
}
