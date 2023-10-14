package Server;
import java.net.*;
import java.util.*;
import java.io.*;
public class Server {
	private ServerSocket serverSocket;
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
		}
	}
	public static void main(String[] args) {
		new Server();
	}
}
