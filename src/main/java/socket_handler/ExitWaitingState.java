package socket_handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import socket_connection_server.DataSocket;
import static socket_handler.WaitingForPairing.denyUsers;
import static socket_handler.WaitingForPairing.userQueue;

public class ExitWaitingState {
	public void run(JSONObject data, Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            String nickname = data.getString("nickname");
            userQueue.remove(nickname);
            denyUsers.put(nickname, new ArrayList<String>());
            DataSocket dataSocket = new DataSocket();
            String dataSend = dataSocket.exportData_ExitWaitingState();
            out.write(dataSend);
            out.newLine();
            out.flush();
            
        } catch (IOException ex) {
        	Logger.getLogger(ExitWaitingState.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
