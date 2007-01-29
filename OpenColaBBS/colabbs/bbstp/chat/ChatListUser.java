package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatListUser extends MultiModuleCmdAdapter
{
	public int mode=0;
	public String Room=null;
public ChatListUser() {
	super();
}
public ChatListUser(int themode, String theRoom)
{
	mode=themode;
	Room=theRoom;
}
}