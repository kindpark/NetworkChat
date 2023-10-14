package Server;

import java.io.IOException;
import java.net.Socket;

import ChatInterface.*;

public class SendThread extends Thread{
	private Socket sendSocket;
	ChatInterface cif = new ChatInterface();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		ParticipantData pd = null;
		while(true) {
			try {
				pd = (ParticipantData) cif.input.readObject();
				//exit를 서버로부터 받으면 종료
				if(pd.getCommand() == Info.EXIT) {
					cif.input.close();
					cif.output.close();
					sendSocket.close();
					System.exit(0);
				}
				//커멘드를 받으면 채팅창에 반영
				else if(pd.getCommand() == Info.SEND) {
					cif.jta.append(pd.getChat() + "\n");
					int length = cif.jtf.getText().length();
					cif.jta.setCaretPosition(length);
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	public void setSocket(Socket s) {
		sendSocket = s;
	}
}
