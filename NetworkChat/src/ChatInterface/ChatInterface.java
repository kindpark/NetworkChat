package ChatInterface;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.io.*;

public class ChatInterface extends JFrame implements ActionListener {
	private JTextArea jta; 
	private JTextField jtf; 
	private JButton button;
	private Socket socket;
	private ObjectInputStream input = null;
	private ObjectOutputStream output = null; 
	private JList jlist;
	private DefaultListModel<String> participant;
	private String nickName;
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
		JPanel chatLog = new JPanel();
		chatLog.setLayout(new BorderLayout()); 
		chatLog.add("Center", scroll);
		chatLog.add("East", jlist);
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
		new ChatInterface();
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


}
