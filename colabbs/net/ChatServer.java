package colabbs.net;

/**
 * ²á¤Ñ«Ç¥\¯à
 */
import java.util.*;
import java.text.*;
import java.io.*;
import java.net.*;

import colabbs.ColaServer;
//import colabbs.INIReader;
import colabbs.Consts;
import colabbs.BBSINI;
import colabbs.bbsclient.server.ClientUser;
import colabbs.bbsclient.CmdTable;
import colabbs.Prompt;
import colabbs.chatroom.*;
import colabbs.bbstp.*;

public final class ChatServer extends NetworkServer
{
	public static ChatRoom myChat=null;
public ChatServer()
{
	super();
}
public void doService()	throws IOException
{
	(new ChatServerUser(is,os)).chatShell();
}
public static void startup()
{
	try
	{
		myChat=new colabbs.chatroom.ChatRoom();
		myChat.BBSName=ColaServer.INI.BBSName;
  	ColaServer.myChat=myChat;
//		(new ChatServer()).startServer(5253,ColaServer.INI.BBSName);
    ChatServerUser.initCmdTable(ClientUser.myCmdTable);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

public static void main(String args[])
{
	try
	{
    if(!(new File("Messages.INI")).exists())
    {
      System.out.println("Messages.INI Not Found at : "+(new File("Messages.INI")).getAbsolutePath());
			System.exit(1);
    }
		Prompt.LoadFile(Consts.INI_Messages);
//		Prompt.LoadMsgs();
		myChat=new colabbs.chatroom.ChatRoom();
		(new ChatServer()).startServer(5253,"ColaChat");
    ChatServerUser.myCmdTable=new CmdTable();
    ChatServerUser.initCmdTable(ChatServerUser.myCmdTable);
		System.out.println("Cola Chat Version 1.0 by Ying-haur Wu 1999/5/12");
		System.out.println("Startup OK!");
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
}