package socket_handler;

import static socket_connection_server.SocketConnection.socketClients;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import socket_connection_server.DataSocket;

public class ReturnLogin {
	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            String nickname = data.getString("nickname");
            
            if (socketClients.containsKey(nickname)){
                socketClients.remove(nickname);
            }
            
            DataSocket dataSocket = new DataSocket();
            String dataSend = dataSocket.exportData_ReturnLogin(nickname);
            out.write(dataSend);
            out.newLine();
            out.flush();
            
        } catch (IOException ex) {
        	Logger.getLogger(ReturnLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}
