package colabbs.bbsclient.server;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.bbstp.*;
import colabbs.bbstp.chat.*;
import colabbs.bbstp.board.*;
import colabbs.bbsclient.*;
import colabbs.record.*;

import colabbs.bbstp.user.BBSTPTalkCheck;
import colabbs.bbstp.user.BBSTPTalkRequestAbort;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbstp.user.BBSTPRemoveUserItem;
import colabbs.bbstp.user.BBSTPChangeState;
import colabbs.bbstp.user.BBSTPReceiveMsg;

import colabbs.DATA.BOARD.*;
import colabbs.DATA.USERFILEDATA.*;
import colabbs.UTILS.*;

/**
 * 伺服器端代表客戶端的類別
 * (記得要加寄認證信的動作!!)
 */
public final class ClientUser extends BBSUser
{
	public static CmdTable myCmdTable=new CmdTable();
  private Hashtable chatNickTable=null,reverseNickTable=null;
public ClientUser(int userpid)
{
	super(userpid);
}
	public void addChatNick(String theNick,MultiModuleCmd theCmd)
  {
  	if(chatNickTable==null)
    	chatNickTable=new Hashtable();
    if(reverseNickTable==null)
    	reverseNickTable=new Hashtable();
    chatNickTable.put(new Integer(theCmd.getID()),theNick);
    reverseNickTable.put(theNick,theCmd);
  }

  public void removeChatNick(String theNick)
  {
  	if(chatNickTable!=null)
    {
    	MultiModuleCmd tmp=(MultiModuleCmd)reverseNickTable.get(theNick);
      if(tmp!=null)
      {
      	reverseNickTable.remove(theNick);
	    	chatNickTable.remove(tmp);
      }
    }
  }

/*	public String getBBSID()
  {
  	return UFD.ID;
  }*/

/*	public void addChatNick(String theNick,MultiModuleCmd theCmd)
  {
  	if(chatNickTable==null)
    	chatNickTable=new Hashtable();
    chatNickTable.put(new Integer(theCmd.getID()),theNick);
  }

  public void removeChatNick(String Nick)
  {
  	if(chatNickTable!=null)
    {
    	chatNickTable.remove(Nick);
    }
  }*/

	public String getChatNick(MultiModuleCmd theCmd)
  {
  	if(chatNickTable!=null)
	  	return (String)chatNickTable.get(new Integer(theCmd.getID()));
    return null;
  }

/**
 * 接受 ChatRoom 送過來的訊息
 * @param Msg java.lang.String
 */
public void addChatMsg(int mid,String Msg)
{
	System.out.println("addChatMsg");
  ChatAddMessage cam=new ChatAddMessage(Msg);
  cam.setID(mid);
  sendCmd(cam);
/*	if(oos!=null)
	{
		try
		{
			synchronized(ColaServer.BBSUsers[pid].os)
			{
      	ChatAddMessage cam=new ChatAddMessage(Msg);
        cam.setID(mid);
				oos.writeObject(cam);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
/**
 * 接受 ChatRoom 送過來表示改變包廂的訊息
 * @param theroom java.lang.String
 */
public void changeRoom(int mid,String theroom)
{
	ChatEnterRoom cer=new ChatEnterRoom(theroom);
  cer.setID(mid);
  sendCmd(cer);
/*	if(oos!=null)
	{
		try
		{
			synchronized(ColaServer.BBSUsers[pid].os)
			{
      	ChatEnterRoom cer=new ChatEnterRoom(theroom);
        cer.setID(mid);
				oos.writeObject(cer);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}
/**
 * 接受 ChatRoom 送過來表示改變話題的訊息
 * @param newTopic java.lang.String
 */
public void changeTopic(int mid,String newTopic)
{
	ChatSetTopic cst=new ChatSetTopic(newTopic);
  cst.setID(mid);
  sendCmd(cst);
/*	if(oos!=null)
	{
		try
		{
			synchronized(ColaServer.BBSUsers[pid].os)
			{
      	ChatSetTopic cst=new ChatSetTopic(newTopic);
        cst.setID(mid);
				oos.writeObject(cst);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}*/
}

/*public void GetMsg(String Msg)
{

}*/
/**
 * 接受別人送過來的訊息
 * @param Msg java.lang.String
 */
public void GetMsg(String senderID,String Msg)
{
  sendCmd(new BBSTPReceiveMsg(senderID,Msg));
/*	try
	{
		synchronized(ColaServer.BBSUsers[pid].os)
		{
			ColaServer.BBSUsers[pid].os.writeInt(35);
			ColaServer.BBSUsers[pid].os.writeInt(Msg.length());
			ColaServer.BBSUsers[pid].os.writeChars(Msg);
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}*/
}
public void run()
{
	boolean loginok=false/*,chatonly=true*/;

	try
	{
		Object cmd=null;
		while(ois!=null)
		{
			LastSig=new Date();
			cmd=ois.readObject();
			try
			{
				if(loginok)
				{
          if(myCmdTable!=null)
          {
            CmdItem myItem=(CmdItem)myCmdTable.getItem(cmd);
            if(myItem!=null)
            {
              if(myItem.myObject!=null)
                myItem.myMethod.invoke(myItem.myObject,new Object[]{this,cmd});
              else
                myItem.myMethod.invoke(this,new Object[]{this,cmd});
            }
          }
				}
				else
				{
          if(cmd instanceof Login)
          {
            Login myLogin=(Login)cmd;

			      	UserPassItem=ColaServer.UFDList.getPass(myLogin.UserName);

      				//Change by WilliamWey
			      	//if(idbuf.toString().equalsIgnoreCase("new")&&ColaServer.INI.LoginAsNew)
      				if(myLogin.newID)
			      		//
      				{
			      		if(ColaServer.INI.LoginAsNew)
      					{
                  if(!NewUser(myLogin.UserName,myLogin.PassWord))
                    return;
		      				UFD.Perm=(int)Perm.Basic;
					      	UFD.UserDefine=~(int)0;
                  UFD.Email=myLogin.Email;
                  //記得加寄認證信的動作!!
      					}
			      		else
                {
      						oos.writeObject(new LoginState(4));
        					return;
                }
			      	}
      				else if(UserPassItem==null) //No this id.
			      	{
//      					sends(Prompt.Msgs[42]);
//			      		attempts++;
    						oos.writeObject(new LoginState(1));
      					return;
			      	}
      				else if(UserPassItem.IDItem.equalsIgnoreCase("SYSOP"))
			      	{
			      		if(checkpasswd( myLogin.PassWord, UserPassItem.PassWordItem ))  //測試密碼
      					{
			      			UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
      						UFD.Perm=~(int)0;
			      			uid=UserPassItem.uid;
			      		}
      					else
			      		{
      						ColaServer.BBSlog.Write(from+" answer incorrect password for SYSOP....");
      						oos.writeObject(new LoginState(2));
                  return;
			      		}
      				}
			      	else if(!UserPassItem.IDItem.equalsIgnoreCase("guest"))
      				{
      					if(checkpasswd( myLogin.PassWord, UserPassItem.PassWordItem ))  //測試密碼
			      		{
      						UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
			      			uid=UserPassItem.uid;
			      		}
      					else
			      		{
      						ColaServer.BBSlog.Write(from+" answer incorrect password for "+UserPassItem.IDItem);
      						oos.writeObject(new LoginState(2));
                  return;
			      		}
      				}
			      	else
      				{
			      		if(ColaServer.INI.UseGuest)
      					{
			      			UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
						      uid=UserPassItem.uid;
			      		}
                else
                {
      						oos.writeObject(new LoginState(3));
  			      		return;
                }
      					//
      				}
//			      	sends(Prompt.Msgs[223]);

      			ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" : "+UFD.ID+" login in Client!");
			      ColaServer.ReSortAddUser(pid);

//            chatID=null;
      			LoginTime=new Date();
//			      MsgMode=false;
//      			ReMsg=false;
//			      TalkRequest=false;
//      			TalkMode=false;
//			      Ansi=true;
      			ReDrawMode=0;
            //counting signatures
            int SigNum=0,SigLines=0;

						if((new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator,"signatures")).exists())
						{
							FileInputStream SigBuf=null;
							BufferedReader SigFile=null;

							try
							{
								SigBuf=new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"signatures");
								SigFile=new BufferedReader(new InputStreamReader(SigBuf,colabbs.ColaServer.INI.Encoding));
								while(SigFile.readLine()!=null)
									SigLines++;
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
							finally
							{
								try
								{
									if(SigBuf!=null)
									{
										SigBuf.close();
										SigBuf=null;
									}
									if(SigFile!=null)
									{
										SigFile.close();
										SigFile=null;
									}
								}
								catch(IOException e)
								{
									e.printStackTrace();
								}
							}
						}
						SigNum=(SigLines+ColaServer.INI.MaxSigLines-1)/ColaServer.INI.MaxSigLines;

      			UFD.NumLogins++;

			      if((UFD.Flags[0]&Consts.UserFlag_Cloak&0xff)!=0)
      				Visible=false;
			      else
      				Visible=true;
            loginok=true;
//            chatonly=false;
						oos.writeObject(new LoginState(0,UFD.Perm,SigNum));
          }
//					else if(cmd instanceof ChatLogin)
//					{
//						int code;
//						ChatLogin cl=(ChatLogin)cmd;

/*						if(loginok)
						{
							if((code=ColaServer.myChat.enterChat(cl.Nick,UFD.ID,"",this))<=0)
							{
								oos.writeObject(new ChatLoginState("Login Error"));
								return; //while bbsclient complete this will change to break!!
							}
							else
							{
								chatID=cl.Nick;
								chatloginok=true;
								oos.writeObject(new ChatLoginState(true,"Welcome"));
								oos.writeObject(new ChatActions(ColaServer.myChat.Verb1_1,ColaServer.myChat.Verb1_2,ColaServer.myChat.Verb2,ColaServer.myChat.Verb3));
							}
						}*/
//            if((code=ColaServer.myChat.enterChat(cl.Nick,null,"",this))<=0)
//						{
//							oos.writeObject(new ChatLoginState("Login Chat Error"));
//							return;
//						}
//						else
//						{
//              chatID=cl.Nick;
//							chatonly=true;
//              loginok=true;
//							oos.writeObject(new ChatLoginState(true,"Welcome"));
//							oos.writeObject(new ChatActions(ColaServer.myChat.Verb1_1,ColaServer.myChat.Verb1_2,ColaServer.myChat.Verb2,ColaServer.myChat.Verb3));
//						}
//					}
	  		}
			}
			catch(ClassCastException e)
			{
				e.printStackTrace();
			}
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	finally
	{
/*		if(loginok&&chatID!=null)
    {
			ColaServer.myChat.exitChat(chatID);
      chatID=null;
    }*/
		try
		{
			if(ois!=null)
			{
				ois.close();
				ois=null;
			}
			if(oos!=null)
			{
				oos.close();
				oos=null;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
      if(loginok/*&&!chatonly*/)
  			GoodBye();
		}
	}
}
/**
 * This method was writen by yhwu.
 */
public void SaveBrc()
{
}
/**
 * 送出 byte 陣列資料到客戶端
 * @param b byte[]
 * @param userpid int
 */
public void sends(byte b[])
{
//	fastsends(b);
}
/**
 * 送出一個 int 資料到客戶端
 * @param data int
 * @param userpid int
 */
public void sends(int data)
{
/*	try
	{
		if(ColaServer.BBSUsers[pid].os!=null)
		{
			ColaServer.BBSUsers[pid].os.writeInt(data);
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
		ColaServer.BBSUsers[pid].stop();
	}*/
}
/**
 * 送出一個 String 資料到客戶端
 * @param data int
 * @param userpid int
 */
	public void sends(String b)
	{
/*		try
		{
			if(ColaServer.BBSUsers[pid].os!=null)
			{
				ColaServer.BBSUsers[pid].os.writeBytes(b);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			ColaServer.BBSUsers[pid].GoodBye();
			ColaServer.BBSUsers[pid].stop();
		}*/
	}

/**
 * 測試使用者是否看得到 BoardBuf 討論區
 */
	boolean Visible(BoardItem BoardBuf)
	{
		if((BoardBuf.Level&Consts.PostMask)==0&&!BBS.HasOnePerm(pid,BoardBuf.Level))
			return false;
		if(BoardBuf.Name.length()==0)
			return false;
		return true;
	}

/**
 * 回應客戶端的命令
 */
  public synchronized void replyCmd(Object source,Object theCmd)
  {
    if(source instanceof MultiModuleCmd)
    {
      int dest=((MultiModuleCmd)source).getID();
      if(theCmd instanceof MultiModuleReply)
        ((MultiModuleReply)theCmd).setID(dest);
    }
    sendCmd(theCmd);
  }

/**
 * 送出一個命令到客戶端
 */
  public void sendCmd(Object theObj)
  {
    if(oos!=null)
    {
    	synchronized(oos)
      {
	      try
  	    {
    	    oos.writeObject(theObj);
      	}
	      catch(IOException e)
  	    {
    	    e.printStackTrace();
      	}
      }
    }
  }

	private boolean NewUser(String NewID,String NewPassWord) throws IOException
	{
//		byte writebuf[]=new byte[Consts.UserLen];

		if (ColaServer.UFDList.isFull())
    {
			oos.writeObject(new LoginState(5));
			return false;
    }

//		sends(ColaServer.registerbuf);  //註冊畫面
/*			if(ValidID(NewID))
			{
				if(ColaServer.UFDList.exist(NewID))
          return false
			}
			else
        return false;*/
		if(ColaServer.UFDList.exist(NewID))
    {
      oos.writeObject(new LoginState(6));
      return false;
    }
		NewPassWord=genpasswd(NewPassWord);

		File PathBuf=new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(NewID.charAt(0))+File.separator+NewID+File.separator);
		if(!PathBuf.exists())
			PathBuf.mkdirs();
		PathBuf=new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(NewID.charAt(0))+File.separator+NewID+File.separator);
		if(!PathBuf.exists())
			PathBuf.mkdirs();

		UFD = new UserFileData();
		UFD.ID = NewID;
		UFD.PassWord = NewPassWord;
		UFD.Perm = ColaServer.INI.OrgPerm;
		ColaServer.UFDList.newUFD(UFD);
		UserPassItem = ColaServer.UFDList.getPass(NewID);
		ColaServer.UFDList.login(UFD);
		return true;
	}

/**
 * 有使用者上線時之對應動作
 */
	public void AddOnlineUser(BBSUser theUser)
  {
  	int pagemode=0,msgmode=0;
    if((theUser.UFD.UserDefine&Consts.AllPager)==0)
    {
    	pagemode=2; //All close!
	    if((theUser.UFD.UserDefine&Consts.FriendPager)!=0)
    		pagemode=1; //only friend!
    }

    if((theUser.UFD.UserDefine&Consts.AllMsgPager)==0)
    {
    	msgmode=2; //All close!
    	if((theUser.UFD.UserDefine&Consts.FriendMsgPager)!=0)
    		msgmode=1; //only friend!
    }
  	sendCmd(new BBSTPAddUserItem(theUser.UFD.ID,theUser.UFD.NickName,theUser.Home,pagemode,msgmode));
  }

/**
 * 有使用者下線時之對應動作
 */
	public void RemoveOnlineUser(BBSUser theUser)
  {
  	sendCmd(new BBSTPRemoveUserItem(theUser.UFD.ID));
  }

/**
 * 有使用者改變狀態時之對應動作
 */
	public void UserStateChanged(BBSUser theUser)
  {
		sendCmd(new BBSTPChangeState(theUser.UFD.ID,theUser.usermode));
  }

/**
 * 有使用者呼叫 talk 時之對應動作
 */
  public void doTalkRequest(TalkUser tu,int talkMode)
  {
  	int thetid=TalkPair.getTID();
    TalkPair.putTalkPair(thetid,new TalkPair(talkMode,tu));
		sendCmd(new BBSTPTalkCheck(thetid,talkMode,tu.getUFD().ID));
    GetMsg(tu.getUFD().ID,Prompt.Msgs[316]);
  }
/**
 * 使用者放棄呼叫 talk 時之對應動作
 */
//  public void cancelTalkRequest(int theid)
  public void cancelTalkRequest(TalkUser tu)
  {
//		sendCmd(new BBSTPTalkRequestAbort(theid,TalkPair.getTalkUser(theid).getUFD().ID));
  }
}
