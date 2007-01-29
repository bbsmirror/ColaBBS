
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient.server;

import java.io.File;

import colabbs.ColaServer;
import colabbs.record.*;
import colabbs.bbsclient.CmdItem;
import colabbs.bbstp.BBSTPDirList;
import colabbs.bbstp.BBSTPDirItem;
import colabbs.bbstp.MultiModuleCmd;
import colabbs.bbstp.board.BBSTPPostCount;
import colabbs.bbstp.board.BBSTPPostItemCount;
import colabbs.bbstp.board.BBSTPPostList;
import colabbs.bbstp.board.BBSTPPostItem;

import colabbs.DATA.BOARD.*;

/**
 * ¥Î¥H³B²z°Q½×°Ï¤å³¹¦Cªí¥\¯à¤§¥~±¾µ{¦¡
 */
public class ServerPostList extends ServerDirList
{
  public ServerPostList(ClientUser theClient,BBSTPDirList theCmd/*,BBSTPDirItem theItem*/,Object theObj,String thePath,String theDir,RecordType thert)
  {
    super(theClient,theCmd/*,theItem*/,theObj,thePath,theDir,thert);
/*    myClient=theClient;
    myCmd=theCmd;
    myItem=theItem;
    lockObj=theObj;
    CurrentPath=thePath;
    MyDir=theDir;
    rt=thert;
    mode=theCmd.mode;
    range=theCmd.range;*/
  }

  public static void startup()
  {
    System.out.println("initializing client post command table....");
    try
    {
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostCount"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerPostList").getMethod("doPostCount",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.board.BBSTPPostCount")}),null));
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostList"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerPostList").getMethod("doPostList",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.board.BBSTPPostList")}),null));
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

  public synchronized static void doPostCount(ClientUser me,BBSTPPostCount theCmd)
  {
    int ListMax=0;
    BoardItem BoardBuf=null;

		BoardBuf = ColaServer.BList.get(theCmd.myBoardName);
    if(BoardBuf!=null)
		  ListMax=(int)RecordHandler.recordNumber(myPostType,ColaServer.INI.BBSHome+"boards"+File.separator+BoardBuf.Name+File.separator,".DIR");
    me.replyCmd(theCmd,new BBSTPPostItemCount(ListMax));
  }

  public synchronized static void doPostList(ClientUser me,BBSTPPostList theCmd)
  {
    System.out.println(theCmd.getID()+" do post dir list....");
    BoardItem BoardBuf=null;

		BoardBuf = ColaServer.BList.get(theCmd.myBoardName);

//					ColaServer.BBSUsers[pid].CurrentBoard=bi.Name;
					//for ¶iª©µe­±
/*					String WelcomeFileName = ColaServer.INI.BBSHome + "boards" + File.separator + bi.Name + File.separator + ".Welcome";
					File wf = new File(WelcomeFileName);
					if (wf.exists() && wf.length() > 2)
					{
						((TelnetUser)ColaServer.BBSUsers[pid]).ansimore(WelcomeFileName);
						((TelnetUser)ColaServer.BBSUsers[pid]).PressAnyKey();
					}
					WelcomeFileName = null;
					wf = null;*/
					//

    (new Thread(new ServerPostList(me,(BBSTPPostList)theCmd/*,new BBSTPPostItem()*/,BoardBuf,ColaServer.INI.BBSHome+"boards"+File.separator+BoardBuf.Name+File.separator,".DIR",new PostType()))).start();
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
		RecordHandler rh=null;

		if(RecordHandler.recordNumber(rt,CurrentPath,MyDir)<0)
		{
      return;
		}
		int ListMax=(int)RecordHandler.recordNumber(rt,CurrentPath,MyDir);
    switch(mode) //1:³Ì·s range ½g, 2: ³ÌÂÂ range ½g, 3: ¥þ³¡
    {
      case 1:
        start=ListMax-range;
        if(start<0)
          start=0;
        end=ListMax;
        break;
      case 2:
        start=0;
        end=range;
        if(end>ListMax)
          end=ListMax;
        break;
      case 3:
        start=0;
        end=ListMax;
        break;
      default:
        break;
    }

    try
		{
      synchronized(lockObj)
      {
				rh=new RecordHandler(CurrentPath,MyDir,false);

				rh.setIndex(start,rt);
        for(int i=start;i<=end;i++)
        {
          if(!rh.nextElement(rt))
						break;
          BBSTPPostItem theItem=(BBSTPPostItem)((BBSTPItemType)rt).getBBSTPItem();
          theItem.BoardName=((BBSTPPostList)myCmd).myBoardName;
          theItem.setIndex(i);
//          theItem.setData(rt);
          myClient.replyCmd(myCmd,theItem);
//          ColaServer.BBSUsers[pid].sends("  "+Utils.CutLeft(""+(i+1),4)+recordTag(rt)+rt.getRecordString()+"[m\r\n");
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