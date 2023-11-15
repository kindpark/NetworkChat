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
	//Text�� JList�� �޾Ƽ� ����, �ڸ�Ʈ �����ϵ���
	public JButton button;
	private String nickName;
	public ObjectInputStream input = null;
	public ObjectOutputStream output = null;
	public Server(){
		jta = new JTextArea();
		jta.setEditable(false);
		JScrollPane scroll = new JScrollPane(jta);
		//��ũ�ѹ� ���� ����
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//ä�÷α׿� ä�� ������
		//ä�� �Է�â�� �Է� ��ư
		JPanel enterChat = new JPanel();
		enterChat.setLayout(new BorderLayout());
		button = new JButton("����");
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
			jta.setText("���� ���� ����");
			list = new ArrayList<ChatManagement>();
			while(true){
				Socket socket = serverSocket.accept();
				//������ ����
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
