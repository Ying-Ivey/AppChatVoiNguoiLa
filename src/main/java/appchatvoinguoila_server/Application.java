package appchatvoinguoila_server;

import socket_connection_server.SocketConnection;

public class Application {
	public static void main(String[] args) {
		SocketConnection socket = new SocketConnection();
		socket.startConnection();
	}
}
