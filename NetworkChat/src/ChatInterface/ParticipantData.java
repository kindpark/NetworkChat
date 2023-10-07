package ChatInterface;

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
