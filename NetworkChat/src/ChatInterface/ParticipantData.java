package ChatInterface;

//���� �г���, ä��, Ŀ��� ���� �����ͺ��̽�
//��ȸ���� �־� ���� sql�� �����ϸ� ������
public class ParticipantData {
	private String name;
	private String chat;
	private Command manage;
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name= name;
	}
	
	public void setManage(Command man){
		this.manage= man;
	}
	public Command getManage(){
		return manage;
	}
	public void setChat(String message){
		this.chat = message; 
	}
	public String getChat(){
		return chat;
	}
}
