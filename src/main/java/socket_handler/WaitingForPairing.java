package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;
import socket_connection_server.DataSocket;
import socket_connection_server.LogServer;
import socket_connection_server.SocketConnection;

public class WaitingForPairing {
	public static DataSocket dataSocket = new DataSocket();
	public static ArrayList<String> userQueue = new ArrayList<>();
	public static Map<String, ArrayList<String>> denyUsers = new HashMap<String, ArrayList<String>>();

	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
		String nickname = data.getString("nickname");
		userQueue.add(nickname);
		LogServer.log("Socket", "user_queue", "Add: " + nickname);
	}

	public void getPair() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ex) {

			}
			if (userQueue.size() >= 2) {
				LogServer.log("Socket", "user_queue", "Checking");
				String user_nickname_1 = "";
				String user_nickname_2 = "";

				boolean is_found = false;
				for (int i = 0; i < userQueue.size() - 1; i++) {
					if (is_found) {
						break;
					}
					for (int j = i + 1; j < userQueue.size(); j++) {
						ArrayList<String> denyUser_1 = denyUsers.get(userQueue.get(i));
						ArrayList<String> denyUser_2 = denyUsers.get(userQueue.get(j));

						if (denyUser_1 == null) {
							denyUser_1 = new ArrayList<String>();
							denyUsers.put(userQueue.get(i), new ArrayList<String>());
						}

						if (denyUser_2 == null) {
							denyUser_2 = new ArrayList<String>();
							denyUsers.put(userQueue.get(j), new ArrayList<String>());
						}

						if (!denyUser_1.contains(userQueue.get(j)) && !denyUser_2.contains(userQueue.get(i))) {
							if (i > j) {
								user_nickname_1 = userQueue.remove(i);
								user_nickname_2 = userQueue.remove(j);
							} else {
								user_nickname_2 = userQueue.remove(j);
								user_nickname_1 = userQueue.remove(i);
							}

							is_found = true;
							break;
						}
					}
				}

				if (!is_found) {
					continue;
				}

				Map<String, Socket> userList = SocketConnection.socketClients;
				Socket socketUser1 = userList.get(user_nickname_1);
				Socket socketUser2 = userList.get(user_nickname_2);

				try {
					BufferedWriter outUser1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
					BufferedWriter outUser2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));

					String dataSendUser1 = dataSocket.exportData_SendInvitation(user_nickname_2);
					String dataSendUser2 = dataSocket.exportData_SendInvitation(user_nickname_1);

					LogServer.log("Socket", "socket_send", "Send: " + dataSendUser1);
					LogServer.log("Socket", "socket_send", "Send: " + dataSendUser2);

					outUser1.write(dataSendUser1);
					outUser1.newLine();
					outUser1.flush();

					outUser2.write(dataSendUser2);
					outUser2.newLine();
					outUser2.flush();

					new ReplyToPairing().addGroup(user_nickname_1, user_nickname_2);
				} catch (IOException ex) {
					Logger.getLogger(WaitingForPairing.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
}
