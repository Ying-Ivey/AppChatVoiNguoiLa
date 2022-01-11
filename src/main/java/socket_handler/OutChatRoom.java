package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import entity_server.Group_User_Are_Pairing;
import socket_connection_server.DataSocket;
import socket_connection_server.LogServer;
import socket_connection_server.SocketConnection;

public class OutChatRoom {
	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
		try {
			String nickname = data.getString("nickname");
			Group_User_Are_Pairing group = new ReplyToPairing().getGroup(nickname);
			DataSocket dataSocket = new DataSocket();
			Map<String, Socket> userList = SocketConnection.socketClients;
			String dataSend;

			if (group != null) {
				String userNickname_1 = group.userNickname_1;
				String userNickname_2 = group.userNickname_2;
				Socket socket_1 = userList.get(userNickname_1);
				Socket socket_2 = userList.get(userNickname_2);

				dataSend = dataSocket.exportDataOutRoom();

				BufferedWriter out_socket_1 = new BufferedWriter(new OutputStreamWriter(socket_1.getOutputStream()));
				out_socket_1.write(dataSend);
				out_socket_1.newLine();
				out_socket_1.flush();

				BufferedWriter out_socket_2 = new BufferedWriter(new OutputStreamWriter(socket_2.getOutputStream()));
				out_socket_2.write(dataSend);
				out_socket_2.newLine();
				out_socket_2.flush();

				new ReplyToPairing().removeGroup(userNickname_1);
				LogServer.log("Socket", "out_chat_room", "[Out Chat Room]: " + nickname);
			}

		} catch (IOException ex) {
			Logger.getLogger(OutChatRoom.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
