package chatserver;
public class StartServer {
	public static void main(String[] args) {
		Server s = new Server();
		s.init(12345);
	}
}
