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
		//���Ϳ� TextArea�����
		jta = new JTextArea();
		jta.setEditable(false);
		JScrollPane scroll = new JScrollPane(jta);
		//��ũ�ѹ� ���� ����
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  //�׻� ��ũ�ѹٰ� ���η� ������
		jlist = new JList<>();
		participant = new DefaultListModel<>();
		participant.addElement("adasdas");
		jlist.setModel(participant);
		JLabel ppl = new JLabel("������");
		ppl.setLayout(new BorderLayout());
		ppl.add("Center", jlist);
		//ä�÷α׿� ä�� ������
		JPanel chatLog = new JPanel();
		chatLog.setLayout(new BorderLayout()); 
		chatLog.add("Center", scroll);
		chatLog.add("East", ppl);
		//ä�� �Է�â�� �Է� ��ư
		JPanel enterChat = new JPanel();
		enterChat.setLayout(new BorderLayout());
		jtf = new JTextField();
		button = new JButton("����");
		enterChat.add("Center", jtf); 
		enterChat.add("East", button);  
		
		participant = new DefaultListModel<>();
		Container container = this.getContentPane();
		container.add("Center", chatLog);
		container.add("South", enterChat);  
		setBounds(400, 400, 400, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//������ �̺�Ʈ
		
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){ 
				try{
					ParticipantData pd = new ParticipantData();
					pd.setNickName(nickName);
					pd.setCommand(Info.EXIT);
					output.writeObject(pd);  //���������� �ʿ䰡 ����
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
				//exit�� �����κ��� ������ ����
				if(pd.getCommand() == Info.EXIT) {
					input.close();
					output.close();
					socket.close();
					System.exit(0);
				}
				//Ŀ��带 ������ ä��â�� �ݿ�
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
