package socket_connection_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import org.json.JSONObject;

public abstract class SocketHandler {
    public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {}
}
