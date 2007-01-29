
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.server;

import java.io.*;
import java.net.*;
import java.util.Vector;

import colabbs.ColaServer;
import colabbs.BBSINI;
import colabbs.bbsclient.CmdItem;
//import colabbs.bbsclient.server.DataChannel;

/**
 * 伺服器端 DataChannel 的伺服器
 */
public class ClientDataServer extends Thread
{
	private ServerSocket ClientDataServer=null;
	public static Vector AllDataChannels=new Vector();

	public ClientDataServer()
	{
  	try
    {
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPReadPost"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("SendPost",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.board.BBSTPReadPost")}),null));
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPReadMail"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("SendMail",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.mail.BBSTPReadMail")}),null));
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPSendPost"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("ReadPost",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.board.BBSTPSendPost")}),null));
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPSendMail"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("ReadMail",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.mail.BBSTPSendMail")}),null));
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPTalkRequest"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("TalkRequest",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.user.BBSTPTalkRequest")}),null));
	  	DataChannel.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPTalkReply"),new CmdItem(Class.forName("colabbs.bbsclient.server.DataChannel").getMethod("TalkReply",new Class[]{Class.forName("colabbs.bbsclient.server.DataChannel"),Class.forName("colabbs.bbstp.user.BBSTPTalkReply")}),null));
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
  	this.start();
	}

  public void run()
  {
		System.out.println("Starting Client Data Server....");
		ServerSocket ClientDataServer=null;

		try
		{
			ClientDataServer = new ServerSocket(5254,ColaServer.clientcount,ColaServer.INI.ServerAddress);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return;
		}
		System.out.println("Client Data Server StartUp OK!");

		while(true)
		{
			try
			{
      	DataChannel myChannel=new DataChannel();
        AllDataChannels.addElement(myChannel);
				myChannel.mySocket=ClientDataServer.accept();
				System.out.println("User accepted in Client Data Server mode....");
				myChannel.from=myChannel.mySocket.getInetAddress();

				myChannel.Home=myChannel.from.getHostName();
				System.out.println("before check home....");
				if(myChannel.Home==null&&ColaServer.INI.BanUnregistIP)
				{
					System.out.println("before close socket....");
					synchronized(myChannel)
					{
						myChannel.mySocket.close();
            AllDataChannels.removeElement(myChannel);
					}
					continue;
				}
				else if(myChannel.Home==null)
					myChannel.Home=myChannel.from.toString();

				// the following is for client/server mode only
				myChannel.ois=new ObjectInputStream(myChannel.mySocket.getInputStream());
				myChannel.oos=new ObjectOutputStream(myChannel.mySocket.getOutputStream());
				//
				myChannel.os=new DataOutputStream(myChannel.mySocket.getOutputStream());
				myChannel.is=new DataInputStream(myChannel.mySocket.getInputStream());

				myChannel.start();
			}
			catch( IOException e)
			{
				System.out.println("System aborted!");
			}
		}
  }
}