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

public class ChatInterface extends JFrame implements ActionListener, Runnable {
	private JTextArea jta; 
	private JTextField jtf; 
	private Socket socket;
	public JButton button;
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
		Thread thread = new Thread(this);
		thread.start();
		input.addActionListener(this);
		button.addActionListener(this);
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
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ParticipantData pd = null;
		while(true) {
			try {
				pd = (ParticipantData) input.readObject();
				//exit를 서버로부터 받으면 종료
				if(pd.getCommand() == Info.EXIT) {
					input.close();
					output.close();
					socket.close();
					System.exit(0);
				}
				//커멘드를 받으면 채팅창에 반영
				else if(pd.getCommand() == Info.SEND) {
					jta.append(pd.getChat() + "\n");
					int length = jtf.getText().length();
					jta.setCaretPosition(length);
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}
