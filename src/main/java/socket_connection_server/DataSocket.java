package socket_connection_server;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataSocket {
	public DataSocket() {}
	
	public String exportData_SendNickname(boolean is_success, String message){
        JSONObject jo = new JSONObject();      
        JSONObject data = new JSONObject();      
        
        jo.put("type", "send_nickname");
        
        data.put("is_success", is_success);        
        data.put("message", message);

        jo.put("data", data);
        return jo.toString();
    }
    
	public String exportData_SendInvitation(String nickname){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "send_invitation");
        data.put("nickname", nickname);
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
	
	public String exportData_ExitWaitingState(){
        JSONObject jo = new JSONObject();          
        JSONObject data = new JSONObject();   
        
        jo.put("type", "exit_waiting_state");
        jo.put("data", data);
        return jo.toString();
    }

    public String exportData_SendMessage(String nickname, String message){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "send_message");
        data.put("nickname", nickname);
        data.put("message", message);
        jo.put("data", data);
        return jo.toString();
    }
 
    public String exportData_Start(boolean is_started){
        JSONObject jo = new JSONObject();        
        JSONObject data = new JSONObject();
        
        jo.put("type", "start_chat");
        data.put("is_started", is_started);
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportDataOutRoom(){
        JSONObject jo = new JSONObject();          
        JSONObject data = new JSONObject();   
        
        jo.put("type", "out_chat_room");
        jo.put("data", data);
        return jo.toString();
    }
    
    public String exportData_ExitApp(){
        JSONObject jo = new JSONObject();          
        JSONObject data = new JSONObject();   
        
        jo.put("type", "exit_app");
        jo.put("data", data);
        return jo.toString();
    }
    
    public JSONObject importData(String rawData){
        return new JSONObject(rawData);
    }
    
    public JSONArray importDataList(String rawData){
        return new JSONArray(rawData);
    }
    
}
