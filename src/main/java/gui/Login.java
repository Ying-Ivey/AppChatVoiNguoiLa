package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.json.JSONObject;

import data.User;
import socket_connection_client.DataSocket;
import socket_connection_client.SocketConnection;
import socket_connection_client.SocketHandler;

public class Login extends JFrame {
	JButton btnLogin;
	JTextField txtNickname;
	JLabel usernameError;
	JLabel picLabel, lbNickname;
	JLabel notificationLabel;
	SocketConnection socket = new SocketConnection();
	DataSocket dataSocket = new DataSocket();
	String nickname = "";

	public Login() throws IOException {

		BufferedImage myPicture = ImageIO.read(new File("src\\main\\java\\images\\icon.png"));
		ImageIcon newImage = new ImageIcon(myPicture);
		Image image = newImage.getImage();
		Image newimg = image.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
		newImage = new ImageIcon(newimg);
		picLabel = new JLabel(newImage);
		notificationLabel = new JLabel();
		notificationLabel.setText("");
		this.setTitle("Đăng nhập");
		txtNickname = new JTextField() {

			public void updateUI() {
				super.updateUI();
				setOpaque(false);
			}
		};

		btnLogin = new JButton("ĐĂNG NHẬP") {

			public void updateUI() {
				super.updateUI();
				setOpaque(false);
			}
		};

		usernameError = new JLabel();
		lbNickname = new JLabel("Nhập nickname: ");

		init();

		if (User.nickname != null) {
			txtNickname.setText(User.nickname);
		}

		socket.addListenConnection("send_nickname", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				boolean isSuccess = data.getBoolean("is_success");

				if (isSuccess) {
					User.nickname = nickname;
					MainScreen mainScreen = new MainScreen();
					mainScreen.setVisible(true);
					dispose();
				} else {
					String message = data.getString("message");
					notificationLabel.setText(message);
				}
			}
		});

		socket.addListenConnection("exit_app", new SocketHandler() {
			@Override
			public void onHandle(JSONObject data, BufferedReader in, BufferedWriter out) {
				dispose();
				System.exit(0);
			}
		});

		addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				String ObjButtons[] = { "Thoát", "Không" };
				int PromptResult = JOptionPane.showOptionDialog(null, "Bạn có muốn thoát khỏi chương trình này không?",
						"Thoát chương trình", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
						ObjButtons[0]);
				if (PromptResult == JOptionPane.YES_OPTION) {
					String nickname = "";
					if (User.nickname != null) {
						nickname = User.nickname;
					}
					String dataSend = dataSocket.exportData_ExitApp(nickname);
					socket.sendData(dataSend);
					setVisible(false);
					System.out.println("User thoát chương trình");
				}
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

	public void addEventListeners() {

		btnLogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String strNickname = txtNickname.getText();
				strNickname = strNickname.trim();
				nickname = strNickname;
				if (txtNickname.getText().length() >= 3 && txtNickname.getText().length() <= 50) {
					String data = dataSocket.exportData_SendNickname(nickname);
					socket.sendData(data);
				}

			}

		});

		txtNickname.getDocument().addDocumentListener(new DocumentListener() {

			public void removeUpdate(DocumentEvent e) {
				if (txtNickname.getText().length() >= 3 && txtNickname.getText().length() <= 50) {
					usernameError.setForeground(new Color(50, 168, 58));
					usernameError.setText("Nickname hợp lệ");
					notificationLabel.setText("");

				} else if ((txtNickname.getText().length() < 3)) {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname ít nhất là 3");
					notificationLabel.setText("");
				} else {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname nhiều nhất là 50");
					notificationLabel.setText("");
				}
			}

			public void insertUpdate(DocumentEvent e) {
				if (txtNickname.getText().length() >= 3 && txtNickname.getText().length() <= 50) {
					usernameError.setForeground(new Color(50, 168, 58));
					usernameError.setText("Nickname hợp lệ");
					notificationLabel.setText("");

				} else if ((txtNickname.getText().length() < 3)) {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname ít nhất là 3");
					notificationLabel.setText("");
				} else {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname nhiều nhất là 50");
					notificationLabel.setText("");
				}
			}

			public void changedUpdate(DocumentEvent e) {
				if (txtNickname.getText().length() >= 3 && txtNickname.getText().length() <= 50) {
					usernameError.setForeground(new Color(50, 168, 58));
					usernameError.setText("Nickname hợp lệ");
					notificationLabel.setText("");

				} else if ((txtNickname.getText().length() < 3)) {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname ít nhất là 3");
					notificationLabel.setText("");
				} else {
					usernameError.setForeground(Color.RED);
					usernameError.setText("Độ dài nickname nhiều nhất là 50");
					notificationLabel.setText("");
				}
			}
		});

	}

	public void init() {

		txtNickname.setPreferredSize(new Dimension(250, 35));
		txtNickname.setForeground(Color.gray);

		btnLogin.setPreferredSize(new Dimension(250, 35));
		btnLogin.setForeground(Color.decode("#249ce3"));
		btnLogin.setFocusPainted(false);

		usernameError.setFont(new Font("SansSerif", Font.BOLD, 11));
		usernameError.setForeground(Color.RED);

		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(Color.WHITE);
		Insets textInsets = new Insets(10, 10, 5, 10);
		Insets buttonInsets = new Insets(20, 10, 10, 10);
		Insets errorInsets = new Insets(0, 20, 0, 0);

		GridBagConstraints input = new GridBagConstraints();
		input.anchor = GridBagConstraints.CENTER;
		input.gridy = 1;
		this.add(picLabel, input);

		input.anchor = GridBagConstraints.CENTER;
		input.insets = textInsets;
		input.gridy = 2;
		this.add(lbNickname, input);

		input.anchor = GridBagConstraints.CENTER;
		input.insets = textInsets;
		input.gridy = 3;
		this.add(txtNickname, input);

		input.gridy = 4;
		input.insets = errorInsets;
		input.anchor = GridBagConstraints.WEST;
		this.add(usernameError, input);

		input.anchor = GridBagConstraints.WEST;
		input.insets = errorInsets;
		input.gridy = 5;
		this.add(notificationLabel, input);

		input.insets = buttonInsets;
		input.anchor = GridBagConstraints.CENTER;
		input.gridx = 0;
		input.gridy = 6;
		this.add(btnLogin, input);

		this.setSize(450, 500);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.setVisible(true);
		this.setResizable(false);
		this.setLocationRelativeTo(null);

		this.requestFocus();
		addEventListeners();

	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Login().setVisible(true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
