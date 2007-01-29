package colabbs.bbstp.chat;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatLogin extends MultiModuleCmdAdapter
{
	public String Nick=null;
public ChatLogin() {
	super();
}
public ChatLogin(String theNick)
{
	Nick=theNick;
}
}