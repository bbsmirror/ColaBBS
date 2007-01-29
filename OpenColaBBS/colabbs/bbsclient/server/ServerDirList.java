
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
 * ¥Î¥H³B²z¬YºØ¦Cªí¥\¯à¤§¥~±¾µ{¦¡
 */
public class ServerDirList implements Runnable
{
  protected int start=0,end=0;
  protected int mode=0;
  protected int range=0;
  protected ClientUser myClient=null;
  protected BBSTPDirList myCmd=null;
//  protected BBSTPDirItem myItem=null;
  protected Object lockObj=null;
  protected String CurrentPath=null;
  protected String MyDir=null;
  protected RecordType rt=null;
  protected static PostType myPostType=new PostType();
  protected static MailType myMailType=new MailType();

  public ServerDirList(ClientUser theClient,BBSTPDirList theCmd/*,BBSTPDirItem theItem*/,Object theObj,String thePath,String theDir,RecordType thert)
  {
    myClient=theClient;
    myCmd=theCmd;
//    myItem=theItem;
    lockObj=theObj;
    CurrentPath=thePath;
    MyDir=theDir;
    rt=thert;
    mode=theCmd.mode;
    range=theCmd.range;
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
          BBSTPDirItem theItem=(BBSTPDirItem)((BBSTPItemType)rt).getBBSTPItem();
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