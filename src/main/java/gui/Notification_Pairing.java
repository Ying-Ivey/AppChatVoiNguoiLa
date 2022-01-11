package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import data.User;
import socket_connection_client.DataSocket;
import socket_connection_client.SocketConnection;
import socket_connection_client.SocketHandler;

public class Notification_Pairing extends JFrame implements ActionListener {
	private Container c;
	private JButton btnAgree;
	private JButton btnExit;
	private JLabel title, txtNickName;
	SocketConnection socket = new SocketConnection();
	DataSocket dataSocket = new DataSocket();

	public Notification_Pairing(final String userNickname) {

		init(userNickname);
		socket.addListenConnection("start_chat", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				boolean is_started = data.getBoolean("is_started");
				
				if (is_started) {
					ChatRoomClient chatRoom = new ChatRoomClient(userNickname);
					chatRoom.setVisible(true);
					dispose();
				} else {
					WaitingForPairing waiting = new WaitingForPairing();
					waiting.setVisible(true);
					dispose();
				}
			}
		});
		
	}

	public void init(String userNickname) {
		setTitle("GHÉP CẶP");
		setBounds(400, 150, 450, 170);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setResizable(false);

		c = getContentPane();
		c.setLayout(null);

		title = new JLabel("Bạn có muốn chat với ");
		title.setFont(new Font("Arial", Font.PLAIN, 15));
		title.setSize(150, 20);
		title.setLocation(55, 30);
		c.add(title);

		String partnerNickname = userNickname;
		txtNickName = new JLabel("<html>" + partnerNickname);
		txtNickName.setFont(new Font("Arial", Font.BOLD, 15));
		txtNickName.setForeground(Color.BLUE);
		txtNickName.setSize(200, 50);
		txtNickName.setLocation(215, 15);
		c.add(txtNickName);

		btnAgree = new JButton("ĐỒNG Ý");
		btnAgree.setFont(new Font("Arial", Font.PLAIN, 15));
		btnAgree.setSize(100, 20);
		btnAgree.setLocation(110, 90);
		btnAgree.setForeground(Color.decode("#249ce3"));
		btnAgree.setFocusPainted(false);
		btnAgree.addActionListener(this);
		c.add(btnAgree);

		btnExit = new JButton("TỪ CHỐI");
		btnExit.setFont(new Font("Arial", Font.PLAIN, 15));
		btnExit.setSize(100, 20);
		btnExit.setLocation(220, 90);
		btnExit.setForeground(Color.decode("#de1912"));
		btnExit.setFocusPainted(false);
		btnExit.addActionListener(this);
		c.add(btnExit);

		setVisible(true);
	}

	public static void main(String[] args) throws Exception {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAgree) {
			String dataSend = dataSocket.exportData_ReplyToPairing(User.nickname, true);
	        socket.sendData(dataSend);
	        btnAgree.setEnabled(false);
		}
		if (e.getSource() == btnExit) {
			String dataSend = dataSocket.exportData_ReplyToPairing(User.nickname, false);
	        socket.sendData(dataSend);
		}
	}
}
