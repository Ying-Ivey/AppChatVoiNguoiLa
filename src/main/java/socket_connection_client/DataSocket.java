package socket_connection_client;

import org.json.JSONObject;

public class DataSocket {
	public DataSocket() {
	}

	public String exportData_SendNickname(String userNickname) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();
        jo.put("type", "send_nickname");
        data.put("nickname", userNickname);
        jo.put("data", data);
        return jo.toString();
    }
	
	public String exportData_SendMessage(String userNickname, String message) {
		JSONObject jo = new JSONObject();
		JSONObject data = new JSONObject();

		jo.put("type", "send_message");
		data.put("nickname", userNickname);
		data.put("message", message);
		jo.put("data", data);
		return jo.toString();
	}
	
	public String exportData_ReturnLogin(String nickname){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "return_login");
        data.put("nickname", nickname);
        jo.put("data", data);
        return jo.toString();
    }
	
    public String exportData_WaitingForPairing(String userNickname) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();
        jo.put("type", "waiting_for_pairing");
        data.put("nickname", userNickname);
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportData_ReplyToPairing(String userNickname, boolean is_accepted) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "reply_to_pairing");
        data.put("nickname", userNickname);
        data.put("is_accepted", is_accepted);
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportData_OutChatRoom(String userNickname) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "out_chat_room");
        data.put("nickname", userNickname);
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportData_ExitWaitingState(String userNickname) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "exit_waiting_state");
        data.put("nickname", userNickname);
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportData_ExitApp(String userNickname) {
        JSONObject jo = new JSONObject();
        JSONObject data = new JSONObject();

        jo.put("type", "exit_app");
        data.put("nickname", userNickname);
        jo.put("data", data);
        return jo.toString();
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
}
