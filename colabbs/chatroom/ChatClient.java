package colabbs.chatroom;

import java.util.*;

import colabbs.bbstp.MultiModuleCmd;

public interface ChatClient
{
	public String getBBSID();
	public String getChatNick(MultiModuleCmd theCmd);
	public void addChatNick(String theNick,MultiModuleCmd theCmd);
	public void removeChatNick(String theNick);
	public void replyCmd(Object source,Object theCmd);
	public void addChatMsg(int mid,String newMsg);
	void changeRoom(int mid,String room);
	void changeTopic(int mid,String newTopic);
}