package appchatvoinguoila_client;

import java.io.IOException;

import gui.Login;
import socket_connection_client.SocketConnection;

public class Application {
	 public static void main(String[] args) {
	        SocketConnection socket = new SocketConnection();
	        socket.startConnection();
	        try {
				new Login().setVisible(true);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
}
