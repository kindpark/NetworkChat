package Server;
import java.net.*;
import java.util.*;
import java.io.*;

public class Server {
	private ServerSocket serverSocket;
	private List<ChatManagement> list;
	public Server(){
		try{
			serverSocket= new ServerSocket (8888);
			list = new ArrayList<ChatManagement>();
			while(true){
				Socket socket = serverSocket.accept();
				//스레드 시작
				ChatManagement handler = new ChatManagement(socket,list); 
				handler.start();
				list.add(handler); 
			}//while
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
/*
public class Server implements Runnable{
	private ServerSocket serverSocket;
	/*
	public Server() {
		try {
			serverSocket = new ServerSocket(8888);
			Socket chatSocket = serverSocket.accept();
			ReceiveThread rt = new ReceiveThread();
			rt.setSocket(chatSocket);
			SendThread st = new SendThread();
			st.setSocket(chatSocket);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public Server() {
		try {
			serverSocket = new ServerSocket(8888);
			System.out.println("서버 개시 중");
			new Thread(this).start();
		}catch(Exception e) {
			System.out.println("error" + e);
		}
	}
	public static void main(String[] args) {
		new Server();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(8888);
			Socket chatSocket = serverSocket.accept();
			ReceiveThread rt = new ReceiveThread();
			rt.setSocket(chatSocket);
			SendThread st = new SendThread();
			st.setSocket(chatSocket);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}
	}
}
*/
