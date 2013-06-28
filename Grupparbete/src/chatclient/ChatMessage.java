package chatclient;

import java.io.Serializable;

public class ChatMessage implements Serializable {

	//Random UID
	private static final long serialVersionUID = 3599290290477187625L;
	private String username, message;
	
	public ChatMessage(String message) {
		this.message = message;
		this.username = "Unidentified";
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	public String getChatMessage() {
		return this.message;
	}
	
	public String getUsername() {
		return this.username;
	}
}
