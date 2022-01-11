package gui;

import java.awt.Color;

import java.awt.EventQueue;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONObject;

import data.User;
import socket_connection_client.DataSocket;
import socket_connection_client.SocketConnection;
import socket_connection_client.SocketHandler;

public class WaitingForPairing extends JFrame implements ActionListener {
	private Container c;
	private JButton btnDongY;
	private JButton btnExit;
	private JPanel mainPanel;
	private JLabel title;
	SocketConnection socket = new SocketConnection();
	DataSocket dataSocket = new DataSocket();

	public WaitingForPairing() {
		init();

		socket.addListenConnection("send_invitation", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				String nickname = data.getString("nickname");

				Notification_Pairing getPairing = new Notification_Pairing(nickname);
				getPairing.setVisible(true);
				dispose();
			}
		});

		socket.addListenConnection("exit_waiting_state", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {

				MainScreen mainScreen = new MainScreen();
				mainScreen.setVisible(true);
				dispose();

			}
		});

		String nickname = User.nickname;
		String data = dataSocket.exportData_WaitingForPairing(nickname);
		socket.sendData(data);

	}

	public void init() {
		setTitle("GHÉP CẶP");
		setBounds(400, 250, 450, 170);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);

		c = getContentPane();
		c.setLayout(null);

		mainPanel = new JPanel();

		title = new JLabel("Đang tìm kiếm bạn chat...");
		title.setFont(new Font("Arial", Font.PLAIN, 15));
		title.setSize(200, 20);
		title.setLocation(135, 20);
		c.add(title);

		btnExit = new JButton("THOÁT");
		btnExit.setFont(new Font("Arial", Font.PLAIN, 15));
		btnExit.setSize(100, 20);
		btnExit.setLocation(160, 90);
		btnExit.setForeground(Color.decode("#de1912"));
		btnExit.setFocusPainted(false);
		btnExit.addActionListener(this);
		c.add(btnExit);

	}

	public static void main(String[] args) throws Exception {}

	public void actionPerformed(ActionEvent e) {
		String dataSend = dataSocket.exportData_ExitWaitingState(User.nickname);
		socket.sendData(dataSend);
	}

}
