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
	//생성자
	public ChatManagement(Socket socket, List <ChatManagement> list) throws IOException {
		
		this.socket = socket;
		this.list = list;
		writer = new ObjectOutputStream(socket.getOutputStream());
		reader = new ObjectInputStream(socket.getInputStream());
		//순서가 뒤바뀌면 값을 입력받지 못하는 상황이 벌어지기 때문에 반드시 writer부터 생성시켜주어야 함!!!!!!
		
	}
	public void run(){
		ParticipantData data = null;
		String nickName;
		try{
			while(true){
				data=(ParticipantData)reader.readObject();
				nickName=data.getNickName();
				
				//System.out.println("배열 크기:"+ar.length);
				//사용자가 접속을 끊었을 경우. 프로그램을 끝내서는 안되고 남은 사용자들에게 퇴장메세지를 보내줘야 한다. 
				if(data.getCommand()==Info.EXIT){
					ParticipantData sendData = new ParticipantData();
					//나가려고 exit를 보낸 클라이언트에게 답변 보내기
					sendData.setCommand(Info.EXIT);
					writer.writeObject(sendData);
					writer.flush();

					reader.close();
					writer.close();
					socket.close();
					//남아있는 클라이언트에게 퇴장메세지 보내기
					list.remove(this);

					sendData.setCommand(Info.SEND);
					sendData.setChat(nickName+"님 퇴장하였습니다");
					onChat(sendData);
					break;
				} else if(data.getCommand()==Info.JOIN){
					//모든 사용자에게 메세지 보내기
					//nickName = dto.getNickName();
					//모든 클라이언트에게 입장 메세지를 보내야 함
					ParticipantData sendData = new ParticipantData();
					sendData.setCommand(Info.SEND);
					sendData.setChat(nickName+"님 입장하였습니다");
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
	//다른 클라이언트에게 전체 메세지 보내주기
	public void onChat(ParticipantData sendData) throws IOException {
		for(ChatManagement manage: list){
			//writer에 값을 보내서 작성 후 값 비우기
			manage.writer.writeObject(sendData);
			manage.writer.flush(); 
		}
	}
}
