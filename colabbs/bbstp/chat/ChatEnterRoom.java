package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleReply;
import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatEnterRoom extends MultiModuleCmdAdapter implements MultiModuleReply
{
	public String Room=null;
public ChatEnterRoom() {
	super();
}
public ChatEnterRoom(String theRoom)
{
	Room=theRoom;
}
}