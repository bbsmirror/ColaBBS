package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatPostMsg extends MultiModuleCmdAdapter
{
	public String Message=null;
public ChatPostMsg() {
	super();
}
public ChatPostMsg(String theMsg)
{
	Message=theMsg;
}
}