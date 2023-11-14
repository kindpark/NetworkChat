package Server;
import java.io.*;
import java.net.*;
import java.util.*;

import ChatInterface.Info;
import ChatInterface.ParticipantData;
public class ChatManagement extends Thread {
	private ObjectInputStream reader;
	private ObjectOutputStream writer;
	private Socket socket;
	private List <ChatManagement> list;
	//������
	public ChatManagement(Socket socket, List <ChatManagement> list) throws IOException {
		
		this.socket = socket;
		this.list = list;
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		//������ �ڹٲ�� ���� �Է¹��� ���ϴ� ��Ȳ�� �������� ������ �ݵ�� writer���� ���������־�� ��!!!!!!
		
	}
	public void run(){
		ParticipantData data = null;
		String nickName;
		try{
			while(true){
				data=(ParticipantData)reader.readObject();
				nickName=data.getNickName();
				
				//System.out.println("�迭 ũ��:"+ar.length);
				//����ڰ� ������ ������ ���. ���α׷��� �������� �ȵǰ� ���� ����ڵ鿡�� ����޼����� ������� �Ѵ�. 
				if(data.getCommand()==Info.EXIT){
					ParticipantData sendData = new ParticipantData();
					//�������� exit�� ���� Ŭ���̾�Ʈ���� �亯 ������
					sendData.setCommand(Info.EXIT);
					writer.writeObject(sendData);
					writer.flush();

					reader.close();
					writer.close();
					socket.close();
					//�����ִ� Ŭ���̾�Ʈ���� ����޼��� ������
					list.remove(this);

					sendData.setCommand(Info.SEND);
					sendData.setChat(nickName+"�� �����Ͽ����ϴ�");
					onChat(sendData);
					break;
				} else if(data.getCommand()==Info.JOIN){
					//��� ����ڿ��� �޼��� ������
					//nickName = dto.getNickName();
					//��� Ŭ���̾�Ʈ���� ���� �޼����� ������ ��
					ParticipantData sendData = new ParticipantData();
					sendData.setCommand(Info.SEND);
					sendData.setChat(nickName+"�� �����Ͽ����ϴ�");
					onChat(sendData);
				} else if(data.getCommand()==Info.SEND){
					ParticipantData sendData = new ParticipantData();
					sendData.setCommand(Info.SEND);
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
			manage.writer.writeObject(sendData);
			manage.writer.flush(); 
		}
	}
}
