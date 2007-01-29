
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient.server;

import java.util.*;

import colabbs.ColaServer;
import colabbs.DATA.BOARD.*;
import colabbs.bbsclient.*;
import colabbs.bbstp.MultiModuleCmd;
import colabbs.bbstp.board.BBSTPListBoards;
import colabbs.bbstp.board.BBSTPBoardItem;

/**
 * 用以處理討論區列表功能之外掛程式
 */
public class ServerBoardLister implements Runnable
{
  private ClientUser myClient=null;
  private BBSTPListBoards myCmd=null;

  public ServerBoardLister(ClientUser theClient,BBSTPListBoards theCmd)
  {
    myClient=theClient;
    myCmd=theCmd;
  }

  public synchronized static void doBoardList(ClientUser me,BBSTPListBoards theCmd)
  {
    System.out.println(((MultiModuleCmd)theCmd).getID()+" do board list....");
    (new Thread(new ServerBoardLister(me,(BBSTPListBoards)theCmd))).start();
  }

  public static void startup()
  {
    System.out.println("initializing client board command table....");
    try
    {
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPListBoards"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerBoardLister").getMethod("doBoardList",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.board.BBSTPListBoards")}),null));
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    ServerPostList.startup();
  }

	public boolean haveNew(BoardItem bi)
  {
  	if(myClient.UserPassItem==null)
    	return true;
  	if(myClient.UserPassItem.BRC==null)
    	return true;
		Vector thebrc=(Vector)myClient.UserPassItem.BRC.get(bi.Name);
		if(thebrc!=null&&thebrc.contains(new Long(bi.filetime)))
			return false;
		return true;
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
    System.out.println("Starting loading boardrc file....");
	/**
	 * 注意!!先暫時被拿掉
	 */
    //myClient.LoadBrc();
	//
    System.out.println("Starting sending board list....");
    Vector list = ColaServer.BList.getVisibleList(myClient.UFD.Perm);
// 		Vector list = ColaServer.BList.getSortedEGroupList(/*EGroupNum*/(short)-1, myClient.UFD.Perm, 1);
    int max=list.size();
    BoardItem bi=null;

    for(int i=0;i<max;i++)
    {
			bi = (BoardItem)list.elementAt(i);
      myClient.replyCmd(myCmd,new BBSTPBoardItem(bi.Name,bi.Title,bi.BM,bi.Level,
      	bi.State,haveNew(bi),bi.Anonymous,bi.AnonyDefault,bi.NoZap,bi.SaveMode,
        bi.JunkBoard));
    }
  }
}