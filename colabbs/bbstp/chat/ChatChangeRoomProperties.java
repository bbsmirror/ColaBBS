package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatChangeRoomProperties extends MultiModuleCmdAdapter
{
	public boolean lock=false,hide=false;
public ChatChangeRoomProperties() {
	super();
}
public ChatChangeRoomProperties(boolean thelock, boolean thehide)
{
	lock=thelock;
	hide=thehide;
}
}