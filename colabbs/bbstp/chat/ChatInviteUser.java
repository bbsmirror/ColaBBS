package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatInviteUser extends MultiModuleCmdAdapter
{
	public String Nick=null;
public ChatInviteUser() {
	super();
}
public ChatInviteUser(String theNick)
{
	Nick=theNick;
}
}