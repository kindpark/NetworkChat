package ChatInterface;
import javax.swing.*;

import Network.Network;

//import Network.ObjectInputStream;
//import Network.ObjectOutputStream;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.io.*;

public class ChatInterface extends JFrame implements ActionListener {
	private JTextArea jta; 
	private JTextField jtf; 
	private Socket socket;
	private JButton button;
	private JList jlist;
	private DefaultListModel<String> participant;
	private String nickName;
	public ObjectInputStream input = null;
	public ObjectOutputStream output = null;
	public ChatInterface() {
		//센터에 TextArea만들기
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
		jtf = new JTextField();
		button = new JButton("전송");
		enterChat.add("Center", jtf); 
		enterChat.add("East", button);  
		
		participant = new DefaultListModel<>();
		Container container = this.getContentPane();
		container.add("Center", chatLog);
		container.add("South", enterChat);  
		setBounds(400, 400, 400, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//윈도우 이벤트
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){ 
				try{
					ParticipantData pd = new ParticipantData();
					pd.setNickName(nickName);
					pd.setCommand(Info.EXIT);
					output.writeObject(pd);  //역슬러쉬가 필요가 없음
					output.flush();
				}catch(IOException io){
					io.printStackTrace();
				}
			}
		});
	}
	public static void main(String[] args) 
	{
		Network nw = new Network();
		new ChatInterface();
		nw.network();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			String msg = jta.getText();
			ParticipantData pd = new ParticipantData();
			if(msg.equals("out")) {
				pd.setCommand(Info.EXIT);
			}
			else {
				pd.setCommand(Info.SEND);
				pd.setChat(msg);
				pd.setNickName(nickName);
			}
				output.writeObject(pd);
				output.flush();
				jtf.setText("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	//부낳
	/*
	public void network() {
		String serverIP = JOptionPane.showInputDialog(this, "서버 ip 입력", "192.168.0.1");
		if(serverIP == null || serverIP.length() == 0) {
			System.out.println("서버 ip 정보가 없습니다.");
			System.exit(0);
		}
		nickName = JOptionPane.showInputDialog(this, "닉네임을 입력하세요", "닉네임", JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length() == 0) {
			nickName = "anonymous";
		}
		try {
			//9500은 무슨 뜻인가?
			
			socket = new Socket(serverIP, 9500);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			
		}catch(UnknownHostException e) {
			JOptionPane.showConfirmDialog(this, "서버를 찾을 수 없습니다.");
			e.printStackTrace();
			System.exit(0);
		}catch(IOException e) {
			JOptionPane.showConfirmDialog(this, "서버에 연결되어 있지 않습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		try {
			ParticipantData pd = new ParticipantData();
			pd.setCommand(Info.SEND);
			pd.setNickName(nickName);
			output.writeObject(pd);
			output.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	*/
}
