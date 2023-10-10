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
	//�γ�
	/*
	public void network() {
		String serverIP = JOptionPane.showInputDialog(this, "���� ip �Է�", "192.168.0.1");
		if(serverIP == null || serverIP.length() == 0) {
			System.out.println("���� ip ������ �����ϴ�.");
			System.exit(0);
		}
		nickName = JOptionPane.showInputDialog(this, "�г����� �Է��ϼ���", "�г���", JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length() == 0) {
			nickName = "anonymous";
		}
		try {
			//9500�� ���� ���ΰ�?
			
			socket = new Socket(serverIP, 9500);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			
			
		}catch(UnknownHostException e) {
			JOptionPane.showConfirmDialog(this, "������ ã�� �� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}catch(IOException e) {
			JOptionPane.showConfirmDialog(this, "������ ����Ǿ� ���� �ʽ��ϴ�.");
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
