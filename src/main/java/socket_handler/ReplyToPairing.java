package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import entity_server.Group_User_Are_Pairing;
import socket_connection_server.DataSocket;
import socket_connection_server.LogServer;
import socket_connection_server.SocketConnection;

public class ReplyToPairing {
	private static DataSocket datasocket = new DataSocket();
	static public ArrayList<Group_User_Are_Pairing> groups = new ArrayList<>();

	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {

		String userNickname = data.getString("nickname");
		boolean is_accepted = data.getBoolean("is_accepted");
		Group_User_Are_Pairing group = getGroup(userNickname);

		if (group == null) {
			return;
		}

		String dataSend;
		boolean is_success = false;
		if (is_accepted) {
			group.setAccept_pairing_1(userNickname, true);
			group.setAccept_pairing_2(userNickname, true);

			if (group.accept_pairing_1 && group.accept_pairing_2) {
				dataSend = datasocket.exportData_Start(true);
				is_success = true;
			} else {
				return;
			}
		} else {
			dataSend = datasocket.exportData_Start(false);
			removeGroup(userNickname);
		}

		Map<String, Socket> userList = SocketConnection.socketClients;
		Socket socketUser1 = userList.get(group.userNickname_1);
		Socket socketUser2 = userList.get(group.userNickname_2);

		try {
			BufferedWriter outputUserNickname1 = new BufferedWriter(new OutputStreamWriter(socketUser1.getOutputStream()));
			BufferedWriter outputUserNickname2 = new BufferedWriter(new OutputStreamWriter(socketUser2.getOutputStream()));
			LogServer.log("Socket", "socket_send", "Send: " + dataSend);
			
			outputUserNickname1.write(dataSend);
			outputUserNickname1.newLine();
			outputUserNickname1.flush();
			outputUserNickname2.write(dataSend);
			outputUserNickname2.newLine();
			outputUserNickname2.flush();

		} catch (IOException ex) {
			Logger.getLogger(ReplyToPairing.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (is_success) {
			WaitingForPairing.denyUsers.put(group.userNickname_1, new ArrayList<String>());
			WaitingForPairing.denyUsers.put(group.userNickname_2, new ArrayList<String>());
		} else {
			ArrayList<String> denyUsers1 = WaitingForPairing.denyUsers.get(group.userNickname_1);
			ArrayList<String> denyUsers2 = WaitingForPairing.denyUsers.get(group.userNickname_2);
			denyUsers1.add(group.userNickname_2);
			denyUsers2.add(group.userNickname_1);
			WaitingForPairing.denyUsers.put(group.userNickname_1, denyUsers1);
			WaitingForPairing.denyUsers.put(group.userNickname_2, denyUsers2);
		}

	}

	public Group_User_Are_Pairing getGroup(String userNickname) {
		for (Group_User_Are_Pairing g : groups) {
			if (g.userNickname_1.equals(userNickname) || g.userNickname_2.equals(userNickname)) {
				return g;
			}
		}
		return null;
	}

	public int getNicknameInGroup(Group_User_Are_Pairing group, String userNickname) {

		if (group.userNickname_1.equals(userNickname)) {
			return 1;
		} else if (group.userNickname_2.equals(userNickname)) {
			return 2;
		} else
			return 0;
	}

	public void addGroup(String user1, String user2) {
		Group_User_Are_Pairing group = new Group_User_Are_Pairing(user1, user2);
		groups.add(group);
	}

	public void removeGroup(String userNickname) {
		for (Group_User_Are_Pairing g : groups) {
			if (g.userNickname_1.equals(userNickname) || g.userNickname_2.equals(userNickname)) {
				groups.remove(g);
				break;
			}
		}
	}
}
