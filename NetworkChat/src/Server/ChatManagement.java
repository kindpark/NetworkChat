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
	//생성자
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
					//나가려고 exit를 보낸 클라이언트에게 답변 보내기
					sendData.setManage(Command.EXIT);
					oow.writeObject(sendData);
					oow.flush();

					oor.close();
					oow.close();
					socket.close();
					list.remove(this);

					sendData.setManage(Command.SEND);
					sendData.setChat(nickName+"님 퇴장하였습니다");
					onChat(sendData);
					break;
				} else if(data.getManage()==Command.JOIN){
					ParticipantData sendData = new ParticipantData();
					sendData.setManage(Command.SEND);
					sendData.setChat(nickName+"님 입장하였습니다");
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
	//다른 클라이언트에게 전체 메세지 보내주기
	public void onChat(ParticipantData sendData) throws IOException {
		for(ChatManagement manage: list){
			//writer에 값을 보내서 작성 후 값 비우기
			manage.oow.writeObject(sendData);
			manage.oow.flush(); 
		}
	}
}

