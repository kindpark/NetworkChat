package ChatInterface;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class ChatInterface extends JFrame implements ActionListener, Runnable{
	public JTextArea jta; 
	private Socket socket;
	//Text를 JList로 받아서 수정, 코멘트 가능하도록
	private JList chatlog;
	public JTextField jtf; 
	public JButton button;
	private JList jlist;
	private DefaultListModel<String> participant;
	private DefaultListModel<String> chatlist;
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
	}
	
	public static void main(String[] args) 
	{
		new ChatInterface().service();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			String message = jtf.getText();
			ParticipantData pd = new ParticipantData();
			if(message.equals("out")) {
				pd.setManage(Command.EXIT);
			}
			else {
				pd.setManage(Command.SEND);
				pd.setChat(message);
				pd.setName(nickName);
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
				if(pd.getManage() == Command.EXIT) {
					input.close();
					output.close();
					socket.close();
					System.exit(0);
				}
				//커멘드를 받으면 채팅창에 반영
				else if(pd.getManage() == Command.SEND) {
					jta.append(pd.getChat() + "\n");
					int length = jta.getText().length();
					jta.setCaretPosition(length);
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public void service(){
		//서버 IP 입력받기
		//String serverIP = JOptionPane.showInputDialog(this, "서버IP를 입력하세요","서버IP",JOptionPane.INFORMATION_MESSAGE);
		String serverIP= JOptionPane.showInputDialog(this,"서버IP를 입력하세요","192.168.0.16");  //기본적으로 아이피 값이 입력되어 들어가게 됨
		if(serverIP==null || serverIP.length()==0){  //만약 값이 입력되지 않았을 때 창이 꺼짐
			JOptionPane.showMessageDialog(null, "IP 정보가 없습니다.");
			System.exit(0);
		}
		nickName= JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임" ,JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length()==0){
			nickName="guest";
		}
		try{
			socket = new Socket(serverIP,1234);
			//에러 발생
			input= new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			JOptionPane.showMessageDialog(null, "전송 준비 완료!"); 
			
		} catch(UnknownHostException e ){
			JOptionPane.showMessageDialog(null, "서버를 찾을 수 없습니다.");
			e.printStackTrace();
			System.exit(0);
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "서버와 연결이 안되었습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		try{
			ParticipantData data = new ParticipantData();
			data.setManage(Command.JOIN);
			data.setName(nickName);
			output.writeObject(data); 
			output.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		Thread t = new Thread(this);
		t.start();
		jtf.addActionListener(this);
		button.addActionListener(this);  //멕션 이벤트 추가
	}
}
