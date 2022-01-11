package socket_connection_server;

public class LogServer {

	public static void log(String message_type, String action_type, String message) {
		System.out.println("[" + message_type + "]" + "[" + action_type + "]: " + message);
	}

}
