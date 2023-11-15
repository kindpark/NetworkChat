package ChatInterface;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;


public class ChatInterface extends JFrame implements ActionListener, Runnable{
	public JTextArea jta; 
	private Socket socket;
	//Text�� JList�� �޾Ƽ� ����, �ڸ�Ʈ �����ϵ���
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
				//exit�� �����κ��� ������ ����
				if(pd.getManage() == Command.EXIT) {
					input.close();
					output.close();
					socket.close();
					System.exit(0);
				}
				//Ŀ��带 ������ ä��â�� �ݿ�
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
		//���� IP �Է¹ޱ�
		//String serverIP = JOptionPane.showInputDialog(this, "����IP�� �Է��ϼ���","����IP",JOptionPane.INFORMATION_MESSAGE);
		String serverIP= JOptionPane.showInputDialog(this,"����IP�� �Է��ϼ���","192.168.0.16");  //�⺻������ ������ ���� �ԷµǾ� ���� ��
		if(serverIP==null || serverIP.length()==0){  //���� ���� �Էµ��� �ʾ��� �� â�� ����
			JOptionPane.showMessageDialog(null, "IP ������ �����ϴ�.");
			System.exit(0);
		}
		nickName= JOptionPane.showInputDialog(this,"�г����� �Է��ϼ���","�г���" ,JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length()==0){
			nickName="guest";
		}
		try{
			socket = new Socket(serverIP,1234);
			//���� �߻�
			input= new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			JOptionPane.showMessageDialog(null, "���� �غ� �Ϸ�!"); 
			
		} catch(UnknownHostException e ){
			JOptionPane.showMessageDialog(null, "������ ã�� �� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		} catch(IOException e){
			JOptionPane.showMessageDialog(null, "������ ������ �ȵǾ����ϴ�.");
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
		button.addActionListener(this);  //�߼� �̺�Ʈ �߰�
	}
}
