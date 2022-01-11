package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.JSONObject;

import data.User;
import socket_connection_client.DataSocket;
import socket_connection_client.SocketConnection;
import socket_connection_client.SocketHandler;

public class MainScreen extends JFrame{
	private Container c;
	private JLabel title, title2;
	private JButton btnPairing, btnExit;
	SocketConnection socket = new SocketConnection();
    DataSocket dataSocket = new DataSocket();
	public MainScreen() {
		init();
		
		socket.addListenConnection("return_login", new SocketHandler() {
            @Override
            public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				try {
					Login login = new Login();
					login.setVisible(true);
	                dispose();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
		
		addWindowListener(new WindowListener() {

			@Override
			public void windowClosing(WindowEvent e) {
				String ObjButtons[] = { "Có", "Không" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Bạn có muốn rời khỏi đây không?",
						"Đăng xuất", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[0]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					String dataSend = dataSocket.exportData_ReturnLogin(User.nickname);
			        socket.sendData(dataSend);
			        dispose();
			        System.out.println("User rời khỏi màn hình chính");
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
	
	private void init() {
		setTitle("ỨNG DỤNG CHAT VỚI NGƯỜI LẠ");
		setBounds(350, 150, 540, 390);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		c = getContentPane();
		c.setLayout(null);
		
		title = new JLabel("ỨNG DỤNG CHAT VỚI NGƯỜI LẠ");
		title.setFont(new Font("Arial", Font.PLAIN, 20));
		title.setSize(350, 30);
		title.setLocation(90, 50);
		c.add(title);
		
		title2 = new JLabel("Chúc bạn có những giây phút vui vẻ ^^!");
		title2.setFont(new Font("Arial", Font.PLAIN, 20));
		title2.setSize(350, 30);
		title2.setLocation(75, 150);
		c.add(title2);
		
		btnPairing = new JButton("GHÉP ĐÔI");
		btnPairing.setFont(new Font("Arial", Font.PLAIN, 15));
		btnPairing.setSize(120, 20);
		btnPairing.setLocation(145, 250);
		btnPairing.setForeground(Color.decode("#249ce3"));
		btnPairing.setFocusPainted(false);
		c.add(btnPairing);
		
		btnExit = new JButton("THOÁT");
		btnExit.setFont(new Font("Arial", Font.PLAIN, 15));
		btnExit.setSize(100, 20);
		btnExit.setLocation(275, 250);
		btnExit.setForeground(Color.decode("#de1912"));
		btnExit.setFocusPainted(false);
		c.add(btnExit);
		
		setVisible(true);
		addEventListeners();
	}
	
	public static void main(String[] args) throws Exception {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	new MainScreen().setVisible(true);
            }
        });
    }

	public void addEventListeners() {
		//System.exit(0);
		btnPairing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitingForPairing waitingForPairing = new WaitingForPairing();
				waitingForPairing.setVisible(true);
				dispose();
			}

		});
		
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String dataSend = dataSocket.exportData_ReturnLogin(User.nickname);
		        socket.sendData(dataSend);
		        dispose();
			}
		});
	}
}
