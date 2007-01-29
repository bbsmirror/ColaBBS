package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatKickUser extends MultiModuleCmdAdapter
{
	public String Nick=null;
public ChatKickUser() {
	super();
}
public ChatKickUser(String theNick)
{
	Nick=theNick;
}
}