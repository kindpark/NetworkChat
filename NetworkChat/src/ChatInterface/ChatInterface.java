package ChatInterface;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class ChatInterface extends JFrame implements ActionListener, Runnable{
	private JTextArea jta; 
	//Text를 JList로 받아서 수정, 코멘트 가능하도록
	private JList chatlog;
	public JTextField jtf; 
	public JButton button;
	private JList jlist;
	private DefaultListModel<String> participant;
	private String name;
	private Socket socket;
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
					pd.setName(name);
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
				pd.setName(name);
			}
				output.writeObject(pd);
				output.flush();
				jtf.setText("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	public void service(){
		name= JOptionPane.showInputDialog(this,"닉네임을 입력하세요","닉네임" ,JOptionPane.INFORMATION_MESSAGE);
		if(name == ""){
			name="손님";
		}
		try{
			socket = new Socket("192.168.0.16",8500);
			//에러 발생
			input= new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
		} catch(UnknownHostException e ){
			jta.setText("서버를 찾을 수 없습니다.");
			e.printStackTrace();
			System.exit(0);
		} catch(IOException e){
			jta.setText("현재 서버가 없습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		try{
			ParticipantData data = new ParticipantData();
			data.setManage(Command.JOIN);
			data.setName(name);
			participant.addElement(name);
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
}
