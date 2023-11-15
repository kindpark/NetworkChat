package Server;
import java.io.*;
import java.net.*;
import java.util.*;
import ChatInterface.*;

public class ChatManagement extends Thread {
	private ObjectInputStream oor;
	private ObjectOutputStream oow;
	private Socket socket;
	private List <ChatManagement> list;
	//������
	public ChatManagement(Socket socket, List <ChatManagement> list) throws IOException {
		
		this.socket = socket;
		this.list = list;
		oow = new ObjectOutputStream(socket.getOutputStream());
		oor = new ObjectInputStream(socket.getInputStream());
		
	}
	public void run(){
		ParticipantData data = null;
		String nickName;
		try{
			while(true){
				data=(ParticipantData)oor.readObject();
				nickName=data.getName();
				
				if(data.getManage()==Command.EXIT){
					ParticipantData sendData = new ParticipantData();
					//�������� exit�� ���� Ŭ���̾�Ʈ���� �亯 ������
					sendData.setManage(Command.EXIT);
					oow.writeObject(sendData);
					oow.flush();

					oor.close();
					oow.close();
					socket.close();
					list.remove(this);

					sendData.setManage(Command.SEND);
					sendData.setChat(nickName+"�� �����Ͽ����ϴ�");
					onChat(sendData);
					break;
				} else if(data.getManage()==Command.JOIN){
					ParticipantData sendData = new ParticipantData();
					sendData.setManage(Command.SEND);
					sendData.setChat(nickName+"�� �����Ͽ����ϴ�");
					onChat(sendData);
				} else if(data.getManage()==Command.SEND){
					ParticipantData sendData = new ParticipantData();
					sendData.setManage(Command.SEND);
					sendData.setChat(" " + nickName + " : " + data.getChat());
					onChat(sendData);
				}
			}
		} catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}		
	}
	//�ٸ� Ŭ���̾�Ʈ���� ��ü �޼��� �����ֱ�
	public void onChat(ParticipantData sendData) throws IOException {
		for(ChatManagement manage: list){
			//writer�� ���� ������ �ۼ� �� �� ����
			manage.oow.writeObject(sendData);
			manage.oow.flush(); 
		}
	}
}

