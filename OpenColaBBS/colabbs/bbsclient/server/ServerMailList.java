
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient.server;

import java.io.*;

import colabbs.ColaServer;
import colabbs.record.*;
import colabbs.bbstp.BBSTPDirList;
import colabbs.bbstp.BBSTPDirItem;
import colabbs.bbstp.mail.*;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.server.ServerDirList;

/**
 * ノ矪瞶獺絚獺ンぇ本祘Α
 */
public class ServerMailList extends ServerDirList
{
  public ServerMailList(ClientUser theClient,BBSTPMailList theCmd,Object theObj,String thePath,String theDir,RecordType thert)
  {
    super(theClient,theCmd,theObj,thePath,theDir,thert);
  }

  public static void startup()
  {
    System.out.println("initializing client mail command table....");
    try
    {
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailCount"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerMailList").getMethod("doMailCount",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.mail.BBSTPMailCount")}),null));
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailList"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerMailList").getMethod("doMailList",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.mail.BBSTPMailList")}),null));
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(NoSuchMethodException e)
    {
      e.printStackTrace();
    }
  }

  public synchronized static void doMailCount(ClientUser me,BBSTPMailCount theCmd)
  {
    int ListMax=(int)RecordHandler.recordNumber(myMailType,ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(me.UFD.ID.charAt(0))+File.separator+me.UFD.ID+File.separator,".DIR");
    me.replyCmd(theCmd,new BBSTPMailItemCount(ListMax));
  }

  public synchronized static void doMailList(ClientUser me,BBSTPMailList theCmd)
  {
    System.out.println(theCmd.getID()+" do mail dir list....");
    (new Thread(new ServerMailList(me,theCmd/*,new BBSTPMailItem()*/,me.UserPassItem,ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(me.UFD.ID.charAt(0))+File.separator+me.UFD.ID+File.separator,".DIR",new MailType()))).start();
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
    switch(mode) //1:程穝 range 絞, 2: 程侣 range 絞, 3: 场, 4: 穝ゅ彻
    {
      case 1:
      case 2:
      case 3:
        super.run();
        return;
    }
		RecordHandler rh=null;

		if(RecordHandler.recordNumber(rt,CurrentPath,MyDir)<0)
		{
      return;
		}
		int ListMax=(int)RecordHandler.recordNumber(rt,CurrentPath,MyDir);

    try
		{
      synchronized(lockObj)
      {
				rh=new RecordHandler(CurrentPath,MyDir,false);
        int j=0;

				rh.setIndex(start,rt);
        for(int i=start;i<=end;i++)
        {
          if(!rh.nextElement(rt))
						break;
          BBSTPMailItem theItem=(BBSTPMailItem)((BBSTPItemType)rt).getBBSTPItem();
          if(!theItem.Read)
          {
            theItem.setIndex(j);
            j++;
            myClient.replyCmd(myCmd,theItem);
          }
        }
			}
    }
    finally
		{
      if(rh!=null)
			{
        rh.close();
				rh=null;
      }
    }
  }
}
