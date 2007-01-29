package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatDoAction extends MultiModuleCmdAdapter
{
	public String Arg1=null,Arg2=null;
public ChatDoAction() {
	super();
}
public ChatDoAction(String theArg1, String theArg2)
{
	Arg1=theArg1;
	Arg2=theArg2;
}
}