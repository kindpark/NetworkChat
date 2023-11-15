package Server;
import java.net.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import ChatInterface.*;
public class Server extends JFrame{
	private ServerSocket serverSocket;
	private List<ChatManagement> list;
	public JTextArea jta; 
	//Text를 JList로 받아서 수정, 코멘트 가능하도록
	public JButton button;
	private String nickName;
	public ObjectInputStream input = null;
	public ObjectOutputStream output = null;
	public Server(){
		jta = new JTextArea();
		jta.setEditable(false);
		JScrollPane scroll = new JScrollPane(jta);
		//스크롤바 세로 조정
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//채팅로그와 채팅 참가자
		//채팅 입력창과 입력 버튼
		JPanel enterChat = new JPanel();
		enterChat.setLayout(new BorderLayout());
		button = new JButton("종료");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
 
	
		Container container = this.getContentPane();
		container.add("Center", scroll);
		container.add("South", button);  
		setBounds(400, 400, 400, 400);
		setVisible(true);

		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){ 
				try{
					ParticipantData pd = new ParticipantData();
					pd.setName(nickName);

					output.writeObject(pd);
					output.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
		try{
			serverSocket= new ServerSocket(8500);
			jta.setText("서버 구동 시작");
			list = new ArrayList<ChatManagement>();
			while(true){
				Socket socket = serverSocket.accept();
				//스레드 시작
				ChatManagement handler = new ChatManagement(socket,list); 
				handler.start();
				list.add(handler); 
			}
		}catch(IOException e1){
			e1.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
