package colabbs.bbstp.chat;

import java.io.*;

import colabbs.bbstp.MultiModuleCmdAdapter;
public class ChatHelps extends MultiModuleCmdAdapter
{
	public int mode=0;
public ChatHelps() {
	super();
}
public ChatHelps(int themode)
{
	mode=themode;
}
}