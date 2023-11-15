package ChatInterface;

//유저 닉네임, 채팅, 커멘드 저장 데이터베이스
//일회성이 있어 추후 sql등 연동하면 좋을듯
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
