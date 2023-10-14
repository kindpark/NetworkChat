package Server;

import java.io.*;
import java.net.Socket;

public class ReceiveThread extends Thread{
	private Socket receiveSocket;
	@Override
	public void run() {
		super.run();
		try {
			String chat;
			while(true) {
				ch
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	public void setSocket(Socket s) {
		receiveSocket = s;
	}
}
