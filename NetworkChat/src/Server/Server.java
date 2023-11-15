package Server;
import java.net.*;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ChatInterface.Command;
import ChatInterface.ParticipantData;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class Server extends JFrame{
	private ServerSocket serverSocket;
	private List<ChatManagement> list;
	public JTextArea jta; 
	private Socket socket;
	//Text를 JList로 받아서 수정, 코멘트 가능하도록
	private JList chatlog;
	public JButton button1;
	private JButton button2;
	private JList jlist;
	private DefaultListModel<String> participant;
	private DefaultListModel<String> chatlist;
	private String nickName;
	public ObjectInputStream input = null;
	public ObjectOutputStream output = null;
	public Server(){
		try{
			jta = new JTextArea();
			jta.setEditable(false);
			JScrollPane scroll = new JScrollPane(jta);
			//스크롤바 세로 조정
			scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  //항상 스크롤바가 세로로 떠있음
			jlist = new JList<>();
			participant = new DefaultListModel<>();
			participant.addElement("adasdas");
			jlist.setModel(participant);
			JLabel ppl = new JLabel("참가자");
			ppl.setLayout(new BorderLayout());
			ppl.add("Center", jlist);
			//채팅로그와 채팅 참가자
			JPanel chatLog = new JPanel();
			chatLog.setLayout(new BorderLayout()); 
			chatLog.add("Center", scroll);
			chatLog.add("East", ppl);
			//채팅 입력창과 입력 버튼
			JPanel enterChat = new JPanel();
			enterChat.setLayout(new BorderLayout());
			button1 = new JButton("시작");
			button2 = new JButton("종료");
			enterChat.add(button1); 
			enterChat.add(button2);  
		
			Container container = this.getContentPane();
			container.add("Center", chatLog);
			container.add("South", enterChat);  
			setBounds(400, 400, 400, 400);
			setVisible(true);

			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){ 
					try{
						ParticipantData pd = new ParticipantData();
						pd.setName(nickName);
						pd.setManage(Command.EXIT);
						output.writeObject(pd);
						output.flush();
					}catch(IOException io){
						io.printStackTrace();
					}
				}
			});
			serverSocket= new ServerSocket(1234);
			JOptionPane.showMessageDialog(null, "서버 구동 시작");
			list = new ArrayList<ChatManagement>();
			while(true){
				Socket socket = serverSocket.accept();
				//스레드 시작
				ChatManagement handler = new ChatManagement(socket,list); 
				handler.start();
				list.add(handler); 
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
