package gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;

import org.json.JSONObject;
import data.User;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import socket_connection_client.DataSocket;
import socket_connection_client.SocketConnection;
import socket_connection_client.SocketHandler;

import javax.swing.text.DefaultCaret;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

public class ChatRoomClient extends JFrame {
	SocketConnection socket = new SocketConnection();
	DataSocket dataSocket = new DataSocket();
	private static String partnerNickname = "";
	private JLabel txtNickName;
	private JPanel panelMessage;
	private JTextPane txtDisplayChat;
	private JLabel textState, lblReceive;
	private JButton btnExit, btnSend;
	private JTextPane txtMessage;
	private JScrollPane scrollPane, scrollPane2;
	private JLabel lbNoti;
	private Container c;

	public ChatRoomClient(String userNickname) {
		partnerNickname = userNickname;
		init();

		socket.addListenConnection("send_message", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				String nickname = data.getString("nickname");
				String message = data.getString("message");
				System.out.println(nickname + ' ' + message);
				appendToTextPaneDisplayChat(txtDisplayChat,
						"<div class='left' style='width: 40%; background-color: #f1f0f0;'>" + message + "</div>");
			}
		});
		socket.addListenConnection("out_chat_room", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {

				JOptionPane.showMessageDialog(null, "Cuộc trò chuyện đã kết thúc", "Thông báo",
						JOptionPane.INFORMATION_MESSAGE);
				MainScreen mainScreen = new MainScreen();
				mainScreen.setVisible(true);
				dispose();
			}
		});

		addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent e) {
				String ObjButtons[] = { "Có", "Không" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Bạn có muốn rời phòng chat này không?",
						"Thoát phòng chat", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[0]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					String dataSend = dataSocket.exportData_OutChatRoom(User.nickname);
					socket.sendData(dataSend);
					setVisible(false);
					System.out.println("User rời phòng chat");
				}
			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static void main(String[] args) { new ChatRoomClient(null).setVisible(true);}

	private void init() {
		setTitle("PHÒNG CHAT");
		setBounds(300, 20, 773, 600);
		getContentPane().setLayout(null);
		setResizable(false);
		c = getContentPane();
		c.setLayout(null);

		lbNoti = new JLabel("");
		lbNoti.setSize(150, 30);
		lbNoti.setLocation(15, 15);
		lbNoti.setText("Bạn đang chat với ");
		c.add(lbNoti);

		txtNickName = new JLabel("<html>" + partnerNickname);
		txtNickName.setFont(new Font("Arial", Font.BOLD, 15));
		txtNickName.setForeground(Color.BLUE);
		txtNickName.setSize(470, 50);
		txtNickName.setLocation(140, 6);
		c.add(txtNickName);

		panelMessage = new JPanel();
		panelMessage.setBounds(6, 413, 749, 131);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Gửi tin nhắn"));
		c.add(panelMessage);
		panelMessage.setLayout(null);

		txtMessage = new JTextPane();
		txtMessage.setBounds(12, 21, 650, 100);
		scrollPane2 = new JScrollPane(txtMessage);
		scrollPane2.setBounds(12, 21, 650, 100);
		panelMessage.add(scrollPane2);

		btnSend = new JButton("");
		btnSend.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnSend.setBounds(670, 33, 65, 39);
		btnSend.setBorder(new EmptyBorder(0, 0, 0, 0));
		btnSend.setContentAreaFilled(false);
		btnSend.setIcon(new javax.swing.ImageIcon(ChatRoomClient.class.getResource("/images/send.png")));
		panelMessage.add(btnSend);

		btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String message = txtMessage.getText();

				message = message.strip();

				if (!message.equals("")) {
					String dataSend = dataSocket.exportData_SendMessage(User.nickname, message);
					socket.sendData(dataSend);
					txtMessage.setText("");
					appendToTextPaneDisplayChat(txtDisplayChat,
							"<table class='bang' style='color: white; clear:both; width: 100%;'>" + "<tr align='right'>"
									+ "<td style='width: 59%; '></td>"
									+ "<td style='width: 40%; background-color: #329cff;'>" + message + "</td> </tr>"
									+ "</table>");
				}
			}
		});

		btnExit = new JButton("RỜI PHÒNG");
		btnExit.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String ObjButtons[] = { "Có", "Không" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Bạn có muốn rời phòng chat này không?",
						"Thoát phòng chat", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[0]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					String dataSend = dataSocket.exportData_OutChatRoom(User.nickname);
					socket.sendData(dataSend);
					setVisible(false);
					System.out.println("User rời phòng chat");
				}
			}
		});

		btnExit.setBounds(639, 15, 110, 30);
		btnExit.setForeground(Color.decode("#de1912"));
		btnExit.setFocusPainted(false);
		c.add(btnExit);

		txtDisplayChat = new JTextPane();
		txtDisplayChat.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		txtDisplayChat.setEditable(false);
		txtDisplayChat.setContentType("text/html");
		txtDisplayChat.setMargin(new Insets(6, 6, 6, 6));
		txtDisplayChat.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
		txtDisplayChat.setBounds(6, 59, 720, 291);
		appendToTextPaneDisplayChat(txtDisplayChat, "<div class='clear' style='background-color:white'></div>");
		c.add(txtDisplayChat);

		scrollPane = new JScrollPane(txtDisplayChat);
		scrollPane.setBounds(10, 65, 739, 331);
		c.add(scrollPane);

	}

	private void appendToTextPaneDisplayChat(JTextPane tp, String msg) {
		HTMLDocument doc = (HTMLDocument) tp.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit) tp.getEditorKit();
		try {

			editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
			tp.setCaretPosition(doc.getLength());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
