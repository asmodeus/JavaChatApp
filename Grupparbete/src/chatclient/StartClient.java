package chatclient;
import java.io.IOException;
public class StartClient {
	public static void main(String[] args) {
	Client client1 = new Client("localhost", 12345, null);
		while(true){
			try {
				client1.write();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}