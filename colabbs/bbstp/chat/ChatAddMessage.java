package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleReplyAdapter;
public class ChatAddMessage extends MultiModuleReplyAdapter
{
	public String Message=null;
public ChatAddMessage() {
	super();
}
public ChatAddMessage(String Msg)
{
	Message=Msg;
}
}