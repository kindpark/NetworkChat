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
	static ChatInterface cif;
	static String nickName;
	public String getServerIp() {
		InetAddress local = null;
		try {
			local= InetAddress.getLocalHost();
		}
		catch(UnknownHostException e) {
			e.printStackTrace();
		}
		if(local == null) {
			return "";
		}
		else {
			String ip = local.getHostAddress();
			return ip;
		}
	}
	public void network() {
		/*
		String serverIP = JOptionPane.showInputDialog(cif, "서버 ip 입력", "192.168.0.1");
		if(serverIP == null || serverIP.length() == 0) {
			System.out.println("서버 ip 정보가 없습니다.");
			System.exit(0);
		}
		*/
		String serverIP = getServerIp();
		nickName = JOptionPane.showInputDialog(cif, "닉네임을 입력하세요", "닉네임", JOptionPane.INFORMATION_MESSAGE);
		if(nickName == null || nickName.length() == 0) {
			nickName = "anonymous";
		}
		try {
			//아직 서버 구현을 안 해서 서버를 찾을 수 없음. 그러기에 에러가 뜸
			socket = new Socket(serverIP, 8888);
			cif.input = new ObjectInputStream(socket.getInputStream());
			cif.output = new ObjectOutputStream(socket.getOutputStream());
			
			
		}catch(UnknownHostException e) {
			JOptionPane.showConfirmDialog(cif, "서버를 찾을 수 없습니다.");
			e.printStackTrace();
			System.exit(0);
		}catch(IOException e) {
			JOptionPane.showConfirmDialog(cif, "서버에 연결되어 있지 않습니다.");
			e.printStackTrace();
			System.exit(0);
		}
		try {
			ParticipantData pd = new ParticipantData();
			pd.setCommand(Info.JOIN);
			pd.setNickName(nickName);
			cif.output.writeObject(pd);
			cif.output.flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
