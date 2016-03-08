package nl.retaliation.networking;

public class RTSProtocolServer{
	private static final int WAITING = 0;
	private static final int SENT_ACK = 1;
	private static final int SENT_TILEMAP = 2;
	private static final int SENT_GAMEOBJECTS = 3;
	
	private int state = WAITING;
	
	public RTSProtocolServer() {
		
	}
	
	public String processInput(String input) {
		String output = "";
		if (state == WAITING && input == "pls send me stuff") {
			output = "you want some tilemap with that?";
			state = SENT_ACK;
		} else if (state == SENT_ACK){
			if (input == "ye") {
				output = "TILEMAP";
				state = SENT_TILEMAP;
			} else if (input == "no") {
				output = sendGameObjects();
				state = SENT_GAMEOBJECTS;
			}
		} else if (state == SENT_TILEMAP && input == "k thx") {
			output = sendGameObjects();
			state = SENT_GAMEOBJECTS;
		} else if (state == SENT_GAMEOBJECTS && input == "bye") {
			state = WAITING;
		}
		
		return output;
	}
	private String sendGameObjects() {
		
		return "GameObjects";
	}
}
