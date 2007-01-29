package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatChangeNick extends MultiModuleCmdAdapter
{
	public String newName=null;
public ChatChangeNick() {
	super();
}
public ChatChangeNick(String theNewName)
{
	newName=theNewName;
}
}