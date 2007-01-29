package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatSendNote extends MultiModuleCmdAdapter
{
	public String Target=null,Note=null;
public ChatSendNote() {
	super();
}
public ChatSendNote(String theTarget, String theNote)
{
	Target=theTarget;
	Note=theNote;
}
}