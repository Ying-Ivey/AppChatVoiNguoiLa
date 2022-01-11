package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import org.json.JSONObject;

import entity_server.Group_User_Are_Pairing;
import socket_connection_server.DataSocket;
import socket_connection_server.LogServer;
import socket_connection_server.SocketConnection;

public class SendMessage {
	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
		DataSocket dataSocket = new DataSocket();
		String userNickname = data.getString("nickname");
		String message = data.getString("message");

		Group_User_Are_Pairing group = new ReplyToPairing().getGroup(userNickname);

		if (group != null) {
			int checkUserNickname = new ReplyToPairing().getNicknameInGroup(group, userNickname);
			Map<String, Socket> userList = SocketConnection.socketClients;
			String dataSend = dataSocket.exportData_SendMessage(userNickname, message);
			if (checkUserNickname == 1) {
				Socket socketUser2 = userList.get(group.userNickname_2);

				try {
					BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));
					LogServer.log("Socket", "socket_send", "Send: " + dataSend);
					outUser2.write(dataSend);
					outUser2.newLine();
					outUser2.flush();
				} catch (IOException ex) {}
			}
			if (checkUserNickname == 2) {
				Socket socketUser1 = userList.get(group.userNickname_1);
				try {
					BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
					LogServer.log("Socket", "socket_send", "Send: " + dataSend);
					outUser1.write(dataSend);
					outUser1.newLine();
					outUser1.flush();
				} catch (IOException ex) {}
			}

		}
	}
}
