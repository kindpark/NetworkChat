package Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import ChatInterface.*;

import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
public class Network{
	static Socket socket;
	static Info info;
	static ChatInterface cif;
	static String nickName;
	public void network() {
		String serverIP = JOptionPane.showInputDialog(cif, "���� ip �Է�", "192.168.0.1");
		if(serverIP == null || serverIP.length() == 0) {
			System.out.println("���� ip ������ �����ϴ�.");
			System.exit(0);
		}
		nickName = JOptionPane.showInputDialog(cif, "�г����� �Է��ϼ���", "�г���", JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length() == 0) {
			nickName = "anonymous";
		}
		try {
			//9500�� ���� ���ΰ�?
			
			socket = new Socket(serverIP, 8888);
			cif.input = new ObjectInputStream(socket.getInputStream());
			cif.output = new ObjectOutputStream(socket.getOutputStream());
			
			
		}catch(UnknownHostException e) {
			JOptionPane.showConfirmDialog(cif, "������ ã�� �� �����ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}catch(IOException e) {
			JOptionPane.showConfirmDialog(cif, "������ ����Ǿ� ���� �ʽ��ϴ�.");
			e.printStackTrace();
			System.exit(0);
		}
		try {
			ParticipantData pd = new ParticipantData();
			pd.setCommand(Info.SEND);
			pd.setNickName(nickName);
			cif.output.writeObject(pd);
			cif.output.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
