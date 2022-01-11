package socket_connection_server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import socket_handler.ExitWaitingState;
import socket_handler.OutChatRoom;
import socket_handler.ReplyToPairing;
import socket_handler.ReturnLogin;
import socket_handler.SendMessage;
import socket_handler.SendNickname;
import socket_handler.WaitingForPairing;

public class SocketConnection {
	private static ServerSocket server = null;
	private static int socketPort = 1234;
    public static Map<String, Socket> socketClients = new HashMap<String, Socket>(); 
    Socket socket;
    BufferedReader in;
    BufferedWriter out;
    public SocketConnection() {
    }

    public void startConnection() {
        try {
            server = new ServerSocket(socketPort);
            System.out.println("========== Server has started ==========");
            LogServer.log("Socket",  "socket_start", "========== Server has started ===============");

            while (true) {
                socket = server.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                LogServer.log("Socket",  "user_join", "user " + socket + " joined");
                
                Thread thread_reply_to_pairing = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new WaitingForPairing().getPair();
                    }
                });
                thread_reply_to_pairing.start();
                
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handleClient(socket, in, out);
                    }
                });
                thread.start();
            }

        } catch (IOException e) {
//            System.err.println(e);
        }
    }

    public void handleClient(Socket socket, BufferedReader in, BufferedWriter out) {
        try {
            DataSocket dataSocket = new DataSocket();
            boolean is_running = true;
            while (is_running) {
                String dataReceive = in.readLine();
                
                if (dataReceive == null || dataReceive.trim().equals("")){
                    continue;
                }
                
                System.out.println(dataReceive);
                
                LogServer.log("Socket", "socket_received", "Received: " + dataReceive);
                JSONObject jsonDataReceive = dataSocket.importData(dataReceive);
                JSONObject data = jsonDataReceive.getJSONObject("data");
                String type = jsonDataReceive.getString("type");

                switch (type) {
                    case "send_nickname":
                    	LogServer.log("Socket", "socket_type", "send_nickname");
                        new SendNickname().run(data, socket, in, out);
                        break;
                    case "return_login":
                    	LogServer.log("Socket", "socket_type", "return_login");
                        new ReturnLogin().run(data, socket, in, out);
                        break;
                    case "reply_to_pairing":
                    	LogServer.log("Socket", "socket_type", "reply_to_pairing");
                        new ReplyToPairing().run(data, socket, in, out);
                        break;
                    case "waiting_for_pairing":
                    	LogServer.log("Socket", "socket_type", "waiting_for_pairing");
                        new WaitingForPairing().run(data, socket, in, out);
                        break;
                    case "send_message":
                    	LogServer.log("Socket", "socket_type", "send_message");
                        new SendMessage().run(data, socket, in, out);
                        break;                    
                    case "out_chat_room":
                    	LogServer.log("Socket", "socket_type", "out_chat_room");
                        new OutChatRoom().run(data, socket, in, out);
                        break;
                    case "exit_waiting_state":
                    	LogServer.log("Socket", "socket_type", "exit_waiting_state");
                        new ExitWaitingState().run(data, socket, in, out);
                        break;
                    case "exit_app":
                        String nickname = data.getString("nickname");
                        if (!nickname.equals("") && socketClients.containsKey(nickname)){
                            socketClients.remove(nickname);
                        }
                        
                        String dataSend = dataSocket.exportData_ExitApp();
                        out.write(dataSend);
                        out.newLine();
                        out.flush();
                        
                        in.close();
                        out.close();
                        socket.close();
                        is_running = false;
                        LogServer.log("Socket", "socket_type", "exit_app");
                        break;
                }
            }
        } catch (IOException e) {
        	LogServer.log("Socket", "user_disconnect", "");
            socketClients.remove("");
        }
    }

    public void stopConnection() {
        try {
            server.close();
            LogServer.log("Socket", "socket_close", "========== Closed server ==========");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void updateSocketClients() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Map<String, Socket> userList = new SocketConnection().getSocketClients();
                    System.out.println(userList.size());

                    for (Map.Entry<String, Socket> e : userList.entrySet()) {
                        Socket socketClient = e.getValue();
                        if (socketClient.isClosed()) {
                            socketClients.remove(e.getKey());
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                }
            }
        });
        thread.start();
    }

    public static Map<String, Socket> getSocketClients() {
        return socketClients;
    }

    public static void main(String[] args) {
    }
}
