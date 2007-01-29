
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient.server;

import colabbs.record.*;
import colabbs.Perm;
import colabbs.Consts;
import colabbs.ColaServer;
import colabbs.Prompt;
import colabbs.telnet.TelnetUser;
import colabbs.telnet.FriendList;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.server.ClientUser;
import colabbs.bbstp.user.BBSTPSendMsg;
import colabbs.bbstp.user.BBSTPReceiveMsg;
import colabbs.bbstp.user.BBSTPUserList;
import colabbs.bbstp.user.BBSTPUserCount;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbstp.user.BBSTPRemoveUserItem;
import colabbs.bbstp.user.BBSTPUserItemCount;

import java.io.*;
import java.util.*;

import colabbs.DATA.USERFILEDATA.*;
import colabbs.UTILS.*;

/**
 * ¥Î¥H³B²z¨Ï¥ÎªÌ¦Cªí¥\¯à¤§¥~±¾µ{¦¡
 */
public class ServerUserList implements Runnable
{
	private static String None="";
  protected ClientUser myClient=null;
  protected BBSTPUserList myCmd=null;

  public ServerUserList(ClientUser theUser,BBSTPUserList theCmd)
  {
    myClient=theUser;
    myCmd=theCmd;
  }

  public static void startup()
  {
    System.out.println("initializing client user command table....");
    try
    {
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPUserCount"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerUserList").getMethod("doUserCount",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.user.BBSTPUserCount")}),null));
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPUserList"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerUserList").getMethod("doUserList",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.user.BBSTPUserList")}),null));
      ClientUser.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPSendMsg"),new CmdItem(Class.forName("colabbs.bbsclient.server.ServerUserList").getMethod("doSendMsg",new Class[]{Class.forName("colabbs.bbsclient.server.ClientUser"),Class.forName("colabbs.bbstp.user.BBSTPSendMsg")}),null));
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

  public static Hashtable initBlack(String myID)
  {
		RecordHandler rh=null;
    Hashtable MyBlack=null;

		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(myID.charAt(0))+File.separator+myID,"Myblacks");
			MyFriendType mft=new MyFriendType();
			int flen=(int)rh.recordNumber(mft);
			if(flen>=0)
			{
		  	MyBlack=new Hashtable(flen);
				PassItem PassBuf=null;

				for(int i=0;i<=flen;i++)
				{
					rh.nextElement(mft);
					PassBuf=ColaServer.UFDList.getPass(mft.deleteBody());

					if(PassBuf!=null) //else delete the item?
						MyBlack.put(new Integer(PassBuf.uid),None);
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
    return MyBlack;
  }

/*  public static Hashtable initFriend(String myID)
  {
		RecordHandler rh=null;
    Hashtable Friend=null;

		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(myID.charAt(0))+File.separator+myID,"Myblacks");
			FriendType ft=new FriendType();
			int flen=(int)rh.recordNumber(mft);
			if(flen>=0)
			{
		  	Friend=new Hashtable(flen);
				PassItem PassBuf=null;

				for(int i=0;i<=flen;i++)
				{
					rh.nextElement(mft);
					PassBuf=ColaServer.UFDList.getPass(mft.deleteBody());

					if(PassBuf!=null) //else delete the item?
						Friend.put(new Integer(PassBuf.uid),None);
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
    return Friend;
  }*/

  public synchronized static void doSendMsg(ClientUser me,BBSTPSendMsg theCmd)
  {
  	StringBuffer pidbuf=new StringBuffer();
    int index;

    index=BBS.IfOnline(theCmd.userID);
    if(index==-1)
  	{
    	return;
    }
    if(!BBS.HasOnePerm(me.pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllMsgPager)==0)
    {
    	if(((ColaServer.BBSUsers[index].UFD.UserDefine&Consts.FriendMsgPager)==0)||(!FriendList.isFriend(me.UserPassItem.IDItem,me.UFD.ID,ColaServer.BBSUsers[index].UFD.ID)))
      {
					return;
			}
    }

		if(ColaServer.BBSUsers[index].UserLogout==true)
    {
			return;
    }
    if(theCmd.myMsg!=null&&!theCmd.myMsg.equals(""))
		{
			Date now=new Date();
			RandomAccessFile MsgFile=null/*,OwnMsgFile=null*/;

			ColaServer.BBSUsers[index].replyID=me.pid; //¼È®É´ú¸Õ¥Î
			try
			{
/*				if(index!=me.pid)
        {
					OwnMsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(me.UFD.ID.charAt(0))+File.separator+me.UFD.ID+File.separator+"Messages","rw");
          OwnMsgFile.seek(OwnMsgFile.length());
        }*/
        if(ColaServer.BBSUsers[index] instanceof TelnetUser)
  	    {
					MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(ColaServer.BBSUsers[index].UFD.ID.charAt(0))+File.separator+ColaServer.BBSUsers[index].UFD.ID+File.separator+"Messages","rw");

					MsgFile.seek(MsgFile.length());

    	  	String AllMsg=theCmd.myMsg.replace('\n',' ').replace('\r',' ');
      	  while(AllMsg.length()>50)
        	{
// cover too fast, so not send!
//						ColaServer.BBSUsers[index].GetMsg(me.UFD.ID,"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]);
						MsgFile.writeBytes("[0;1;44;36m"+STRING.Cut(me.UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]+"\r\n");
						AllMsg=AllMsg.substring(49);
						ColaServer.BBSUsers[index].MsgNum++;
/*						if(index!=me.pid)
							OwnMsgFile.writeBytes("[0;1;42;36m SendTo [1;33m"+STRING.Cut(ColaServer.BBSUsers[index].UFD.ID,Consts.IDLen)+"[44;33m[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]+"\r\n");*/
  	      }
          ColaServer.BBSUsers[index].GetMsg(me.UFD.ID,"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]);
          MsgFile.writeBytes("[0;1;44;36m"+STRING.Cut(me.UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]+"\r\n");
          ColaServer.BBSUsers[index].MsgNum++;
/*          if(index!=me.pid)
          	OwnMsgFile.writeBytes("[0;1;42;36m SendTo [1;33m"+STRING.Cut(ColaServer.BBSUsers[index].UFD.ID,Consts.IDLen)+"[44;33m[1;37;44m"+STRING.Cut(AllMsg,50)+Prompt.Msgs[159]+"\r\n");*/
					((TelnetUser)ColaServer.BBSUsers[index]).Bell();
					if(((TelnetUser)ColaServer.BBSUsers[index]).MsgGetKey)
						((TelnetUser)ColaServer.BBSUsers[index]).getch();
					else if(index!=me.pid)
						ColaServer.BBSUsers[index].suspend();
					TIME.Delay(1000);
					if(!((TelnetUser)ColaServer.BBSUsers[index]).MsgGetKey&&index!=me.pid)
						ColaServer.BBSUsers[index].resume();
      	}
        else
        	ColaServer.BBSUsers[index].GetMsg(me.UFD.ID,STRING.Cut(theCmd.myMsg,50));
			}
			catch(IOException e)
			{
				return;
			}
			finally
			{
				try
				{
					if(MsgFile!=null)
					{
						MsgFile.close();
						MsgFile=null;
					}
/*					if(OwnMsgFile!=null)
					{
						OwnMsgFile.close();
						OwnMsgFile=null;
					}*/
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
  }

  public synchronized static void doUserCount(ClientUser me,BBSTPUserCount theCmd)
  {
    Integer uidtemp=null;
    int count=0;

		Hashtable MyBlack=initBlack(me.UFD.ID);

    for(int i=0;i<ColaServer.onlineuser;i++)
    {
    	if(!BBS.HasOnePerm(me.pid,Perm.SeeCloak)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[i]].Visible))
      	continue;
      uidtemp=new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[i]].uid);
      if(!BBS.HasOnePerm(me.pid,Perm.SYSOP)&&MyBlack!=null&&MyBlack.containsKey(uidtemp))
      	continue;
      count++;
    }
    me.replyCmd(theCmd,new BBSTPUserItemCount(count));
  }

  public synchronized static void doUserList(ClientUser me,BBSTPUserList theCmd)
  {
    System.out.println("Do user list....");
    (new Thread(new ServerUserList(me,theCmd))).start();
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
		Hashtable MyBlack=initBlack(myClient.UFD.ID);
    Integer uidtemp=null;
//    int count=0;

		for(int i=0;i<ColaServer.onlineuser;i++)
    {
    	if(!BBS.HasOnePerm(myClient.pid,Perm.SeeCloak)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[i]].Visible))
      	continue;
      if(!BBS.HasOnePerm(myClient.pid,Perm.SYSOP)&&MyBlack!=null&&MyBlack.containsKey(uidtemp))
      	continue;
      myClient.AddOnlineUser(ColaServer.BBSUsers[ColaServer.SortedUser[i]]);
/*      int pagemode=0,msgmode=0;
      if((ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.UserDefine&Consts.AllPager)==0)
      {
      	pagemode=2; //All close!
        if((ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.UserDefine&Consts.FriendPager)!=0)
      		pagemode=1; //only friend!
      }

      if((ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.UserDefine&Consts.AllMsgPager)==0)
	    {
      	msgmode=2; //All close!
      	if((ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.UserDefine&Consts.FriendMsgPager)!=0)
      		msgmode=1; //only friend!
      }

      myClient.replyCmd(myCmd,new BBSTPAddUserItem(ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID,ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.NickName,ColaServer.BBSUsers[ColaServer.SortedUser[i]].Home,pagemode,msgmode));*/
    }
  }
}