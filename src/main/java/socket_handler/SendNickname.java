package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import socket_connection_server.DataSocket;
import socket_connection_server.SocketConnection;

public class SendNickname {
	public static DataSocket dataSocket = new DataSocket();

	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
		try {
			String nickname = data.getString("nickname");

			Map<String, Socket> socketClients = SocketConnection.socketClients;
			String dataSend = "";
			if (socketClients.keySet().contains(nickname)) {
				dataSend = dataSocket.exportData_SendNickname(false,
						"Nickname này đã tồn tại. Vui lòng chọn nickname khác.");
			} else {
				socketClients.put(nickname, socket);
				dataSend = dataSocket.exportData_SendNickname(true, "");
			}

			out.write(dataSend);
			out.newLine();
			out.flush();
		} catch (IOException ex) {
			Logger.getLogger(SendNickname.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
}
