package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleReplyAdapter;
public class ChatLoginState extends MultiModuleReplyAdapter
{
	public boolean Result=false;
	public String Message=null;
public ChatLoginState() {
	super();
}
public ChatLoginState(String theMsg)
{
	Result=false;
	Message=theMsg;
}
public ChatLoginState(boolean theResult,String theMsg)
{
	Result=theResult;
	Message=theMsg;
}
}