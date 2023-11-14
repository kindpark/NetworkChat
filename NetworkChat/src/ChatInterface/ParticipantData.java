package ChatInterface;
//유저 닉네임, 채팅, 커멘드 저장 데이터베이스
//일회성이 있어 추후 sql등 연동하면 좋을듯
public class ParticipantData {
	private String nickname;
	private String chat;
	private Info command;
	
	public String getNickName(){
		return nickname;
	}
	public void setNickName(String nickName){
		this.nickname= nickName;
	}
	
	public void setCommand(Info command){
		this.command= command;
	}
	public Info getCommand(){
		return command;
	}
	public void setChat(String message){
		this.chat = message; 
	}
	public String getChat(){
		return chat;
	}
}
