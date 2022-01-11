package entity_server;

public class Group_User_Are_Pairing {
	public String userNickname_1;
	public String userNickname_2;
	public boolean accept_pairing_1 = false;
	public boolean accept_pairing_2 = false;

	public Group_User_Are_Pairing(String userNickname_1, String userNickname_2) {
		this.userNickname_1 = userNickname_1;
		this.userNickname_2 = userNickname_2;
	}

	public void setAccept_pairing_1(String userNickname, boolean accept_pairing) {
		if (this.userNickname_1.equals(userNickname)) {
			this.accept_pairing_1 = accept_pairing;
		}
	}

	public void setAccept_pairing_2(String userNickname, boolean accept_pairing) {
		if (this.userNickname_2.equals(userNickname)) {
			this.accept_pairing_2 = accept_pairing;
		}
	}

	public boolean inGroup(String userNickname) {
		if (userNickname == userNickname_1 || userNickname == userNickname_2) {
			return true;
		} else {
			return false;
		}
	}
}
