package socket_connection_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SocketConnection {
	private static Socket socket = null;
    private static BufferedReader in = null;
    private static BufferedWriter out = null;
    private static String socketHost = "localhost";    
    private static int socketPort = 1234;                  
    private static Map <String, SocketHandler> actions = new HashMap<String, SocketHandler>();

    public SocketConnection() {}
    
    public void startConnection(){
        try {
            socket = new Socket(socketHost, socketPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("========== Connected to server ==========");
            
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    handleServer(socket, in, out);
                }
            });  
            thread.start();
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void handleServer(Socket socket, BufferedReader in, BufferedWriter out){
        try {
            DataSocket dataSocket = new DataSocket();
            while (true){
                String rawDateReceive = in.readLine();
                
                if (rawDateReceive == null){
                    continue;
                }
                
                System.out.println("Receive: " + rawDateReceive);
                JSONObject dataReceive = dataSocket.importData(rawDateReceive);
                JSONObject data = dataReceive.getJSONObject("data");
                String type = dataReceive.getString("type");

                switch (type) {
	                case "send_nickname":
	                    System.out.println("send_nickname");
	                    actions.get("send_nickname").onHandle(data, in, out);
	                    break;
                    case "send_message":
                        System.out.println("send_message");
                        actions.get("send_message").onHandle(data, in, out);
                        break;
                    case "send_invitation":
                        System.out.println("send_invitation");
                        actions.get("send_invitation").onHandle(data, in, out);
                        break;
                    case "return_login":
                    	System.out.println("return_login");
                        actions.get("return_login").onHandle(data, in, out);
                        break;
                    case "start_chat":
                        System.out.println("start_chat");
                        actions.get("start_chat").onHandle(data, in, out);
                        break;
                    case "out_chat_room":
                        System.out.println("out_chat_room");
                        actions.get("out_chat_room").onHandle(data, in, out);
                        break;
                    case "exit_waiting_state":
                        System.out.println("exit_waiting_state");
                        actions.get("exit_waiting_state").onHandle(data, in, out);
                        break;
                    case "exit_app":
                        System.out.println("exit_app");
                        actions.get("exit_app").onHandle(data, in, out);
                        break;
                }
            }
        } catch (IOException e) { System.err.println(e); }
    }
    
    public void addListenConnection(String actionID, SocketHandler handler){
        actions.put(actionID, handler);
    }
            
    public void stopConnection(){
        try {
            in.close();
            out.close();
            socket.close();
            System.out.println("========== Closed connection to server ==========");
        } catch (IOException e) { System.err.println(e); }
    }
     
    public void sendData(String data){
        try {
            out.write(data);
            out.newLine();
            out.flush();
        } catch (IOException e) { System.err.println(e); }
    }
}
