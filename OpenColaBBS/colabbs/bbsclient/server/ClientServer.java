package colabbs.bbsclient.server;

import java.util.*;
import java.io.*;
import java.net.*;

import colabbs.*;
import colabbs.bbsclient.*;
import colabbs.UTILS.*;

/**
 * 主從式架構的伺服器
 */
public final class ClientServer extends Thread
{
	private ClientDataServer myDataServer=null;

	public ClientServer()
	{
		super(ColaServer.userGroup,"Client Server");
	}
	public void run()
	{
		myDataServer=new ClientDataServer();
		System.out.println("Starting Client Server....");
		ServerSocket ClientServer;

		try
		{
//			ClientServer = new ServerSocket(5253,ColaServer.clientcount);
			ClientServer = new ServerSocket(5253,ColaServer.clientcount,ColaServer.INI.ServerAddress);

		}
		catch(IOException e)
		{
			e.printStackTrace();
//			ColaServer.BBSlog.println("Exception #45"+e);
			return;
		}
		System.out.println("Client/Server mode OK!");

		while(true)
		{
			try
			{
				int index;

				for(index=0;index<ColaServer.clientcount;index++)
				{
					synchronized(ColaServer.BBSUsers)
					{
//                        System.out.println(index+" is testing....");
						if(ColaServer.BBSUsers[index]==null)
						{
//                            System.out.println(index+" is empty....");
							ColaServer.BBSUsers[index]=new ClientUser(index);
							ColaServer.BBSUsers[index].UserLogout=false;
//							ColaServer.BBSUsers[index].ReMsg=false;
							ColaServer.BBSUsers[index].MsgID=-1;
							ColaServer.BBSUsers[index].MsgNum=0;
							ColaServer.BBSUsers[index].usermode=Modes.Login;
							break;
						}
						else if(ColaServer.BBSUsers[index].UserLogout==true)
						{
//                            System.out.println(index+" is logouted....");
							ColaServer.BBSUsers[index]=new ClientUser(index);
							ColaServer.BBSUsers[index].UserLogout=false;
//							ColaServer.BBSUsers[index].ReMsg=false;
							ColaServer.BBSUsers[index].MsgID=-1;
							ColaServer.BBSUsers[index].MsgNum=0;
							ColaServer.BBSUsers[index].usermode=Modes.Login;
							break;
						}
//                        System.out.println(index+" is not empty....");
					}
				}
				if(index==ColaServer.clientcount)
				{
					TIME.Delay(10000);
					continue;
				}
				ColaServer.BBSUsers[index].BBSSocket=ClientServer.accept();
				ColaServer.BBSUsers[index].from=ColaServer.BBSUsers[index].BBSSocket.getInetAddress();

				ColaServer.BBSUsers[index].Home=null;
				ColaServer.BBSUsers[index].Home=ColaServer.BBSUsers[index].from.getHostName();
				if(ColaServer.BBSUsers[index].Home==null&&ColaServer.INI.BanUnregistIP)
				{
					synchronized(ColaServer.BBSUsers)
					{
						ColaServer.BBSUsers[index].UserLogout=true;
						ColaServer.BBSUsers[index].BBSSocket.close();
						ColaServer.BBSUsers[index]=null;
					}
					continue;
				}
				else if(ColaServer.BBSUsers[index].Home==null)
					ColaServer.BBSUsers[index].Home=ColaServer.BBSUsers[index].from.toString();

				// the following is for client/server mode only
				ColaServer.BBSUsers[index].ois=new ObjectInputStream(ColaServer.BBSUsers[index].BBSSocket.getInputStream());
				ColaServer.BBSUsers[index].oos=new ObjectOutputStream(ColaServer.BBSUsers[index].BBSSocket.getOutputStream());
				//
				ColaServer.BBSUsers[index].os=new DataOutputStream(ColaServer.BBSUsers[index].BBSSocket.getOutputStream());
				ColaServer.BBSUsers[index].is=new DataInputStream(ColaServer.BBSUsers[index].BBSSocket.getInputStream());

				System.out.println("User accepted in Client Server mode....");
//				ColaServer.BBSUsers[index].telnetmode=false;
				ColaServer.BBSUsers[index].start();
			}
			catch( IOException e)
			{
				System.out.println("Exception #46: " + e);
				System.out.println("System aborted!");
			}
		}
}
}