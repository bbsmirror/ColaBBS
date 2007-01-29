package colabbs.net;

import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;

import colabbs.bbstp.MultiModuleCmd;
import colabbs.bbstp.MultiModuleReply;
import colabbs.bbstp.Login;
import colabbs.bbstp.chat.*;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.CmdTable;
import colabbs.chatroom.ChatClient;
/**
 * 代表聊天室的一個使用者的類別
 */
public class ChatServerUser implements ChatClient
{
	private ObjectOutputStream oos = null;
	private ObjectInputStream ois = null;
  private Hashtable chatNickTable=null;
	public static CmdTable myCmdTable=null;
public ChatServerUser()
{
	super();
}
public ChatServerUser(InputStream is, OutputStream os)
{
	try
	{
		ois=new ObjectInputStream(is);
		oos=new ObjectOutputStream(os);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}

	public String getBBSID()
  {
  	return "guest";
  }

	public void addChatNick(String theNick,MultiModuleCmd theCmd)
  {
  	System.out.println("put "+theNick);
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
  }

	public String getChatNick(MultiModuleCmd theCmd)
  {
  	if(chatNickTable!=null)
	  	return (String)chatNickTable.get(new Integer(theCmd.getID()));
    return null;
  }
/**
 * 接受一句其它使用者說的話
 */
public void addChatMsg(int mid,String newMsg)
{
	if(oos!=null)
	{
		try
		{
    	ChatAddMessage cam=new ChatAddMessage(newMsg);
      cam.setID(mid);
			oos.writeObject(cam);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
/**
 * 進入別的包廂的事件
 */
public void changeRoom(int mid,String room)
{
	if(oos!=null)
	{
		try
		{
    	ChatEnterRoom cer=new ChatEnterRoom(room);
      cer.setID(mid);
			oos.writeObject(cer);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
/**
 * 有人改變話題的事件
 */
public void changeTopic(int mid,String newTopic)
{
	if(oos!=null)
	{
		try
		{
    	ChatSetTopic cst=new ChatSetTopic(newTopic);
      cst.setID(mid);
			oos.writeObject(cst);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

public static void initCmdTable(CmdTable theTable)
{
    System.out.println("initializing client chat command table....");
    try
    {
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatLogin"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doChatLogin",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatLogin")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatPostMsg"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doPostMsg",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatPostMsg")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatChangeNick"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doChangeNick",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatChangeNick")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatEnterRoom"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doEnterRoom",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatEnterRoom")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatSetTopic"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doSetTopic",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatSetTopic")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatHelps"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doChatHelp",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatHelps")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatSendNote"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doSendNote",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatSendNote")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatListUser"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doListUser",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatListUser")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatKickUser"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doKickUser",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatKickUser")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatDoAction"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doAction",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatDoAction")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatSetOP"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doSetOp",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatSetOP")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatInviteUser"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doInviteUser",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatInviteUser")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatChangeRoomProperties"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doChangeRoomProperties",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatChangeRoomProperties")}),null));
      theTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatLogout"),new CmdItem(Class.forName("colabbs.net.ChatServerUser").getMethod("doExitChat",new Class[]{Class.forName("colabbs.chatroom.ChatClient"),Class.forName("colabbs.bbstp.chat.ChatLogout")}),null));
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

public static void doPostMsg(ChatClient me,ChatPostMsg cpm)
{
	System.out.println("doPostMsg");
  ChatServer.myChat.postMsg(me.getChatNick(cpm),cpm.Message);
}

public static void doChangeNick(ChatClient me,ChatChangeNick ccn)
{
	String oldNick=me.getChatNick(ccn);

	if(ChatServer.myChat.changeNick(oldNick,ccn.newName)>=0)
  {
  	me.removeChatNick(oldNick);
  	me.addChatNick(ccn.newName,ccn);
  }
}

public static void doEnterRoom(ChatClient me,ChatEnterRoom cer)
{
	ChatServer.myChat.enterRoom(me.getChatNick(cer),cer.Room);
}

public static void doSetTopic(ChatClient me,ChatSetTopic cst)
{
	ChatServer.myChat.setTopic(me.getChatNick(cst),cst.newTopic);
}


public static void doChatHelp(ChatClient me,ChatHelps ch)
{
	switch(ch.mode)
	{
		case 1:
			ChatServer.myChat.doHelp(me.getChatNick(ch));
      break;
    case 2:
    	ChatServer.myChat.doOpHelp(me.getChatNick(ch));
      break;
    case 3:
    	ChatServer.myChat.doActionHelp(me.getChatNick(ch));
      break;
  }
}


public static void doSendNote(ChatClient me,ChatSendNote csn)
{
	ChatServer.myChat.sendNote(me.getChatNick(csn),csn.Target,csn.Note);
}


public static void doListUser(ChatClient me,ChatListUser clu)
{
	switch(clu.mode)
  {
  	case 1:
    	ChatServer.myChat.fullListUser(me.getChatNick(clu),clu.Room);
      break;
    case 2:
    	ChatServer.myChat.listUser(me.getChatNick(clu),clu.Room);
      break;
    case 3:
    	ChatServer.myChat.listRoom(me.getChatNick(clu));
      break;
  }
}


public static void doKickUser(ChatClient me,ChatKickUser cku)
{
	ChatServer.myChat.kickUser(me.getChatNick(cku),cku.Nick);
}


public static void doAction(ChatClient me,ChatDoAction cda)
{
	ChatServer.myChat.doAction(me.getChatNick(cda),cda.Arg1,cda.Arg2);
}


public static void doSetOp(ChatClient me,ChatSetOP csop)
{
	ChatServer.myChat.setOP(me.getChatNick(csop),csop.newOP);
}


public static void doInviteUser(ChatClient me,ChatInviteUser ciu)
{
	ChatServer.myChat.inviteUser(me.getChatNick(ciu),ciu.Nick);
}


public static void doChangeRoomProperties(ChatClient me,ChatChangeRoomProperties ccrp)
{
	if(ccrp.lock)
  	ChatServer.myChat.setLockRoom(me.getChatNick(ccrp));
  if(ccrp.hide)
  	ChatServer.myChat.setHideRoom(me.getChatNick(ccrp));
}


public static void doExitChat(ChatClient me,ChatLogout cl)
{
	String myNick=me.getChatNick(cl);
  me.removeChatNick(myNick);
	ChatServer.myChat.exitChat(myNick);
}

public static void doChatLogin(ChatClient me,ChatLogin cl)
{
	int code;
//  ChatLogin cl=(ChatLogin)cmd;

  if((code=ChatServer.myChat.enterChat(cl.Nick,me.getBBSID(),"",me,cl.getID()))<=0)
	{
		switch(code)
    {
    	case -1:
      	me.replyCmd(cl,new ChatLoginState("Chat Room is Full!"));
        break;
      case -2:
      	me.replyCmd(cl,new ChatLoginState("Another people use this nick name!"));
        break;
      default:
      	me.replyCmd(cl,new ChatLoginState("Login Error!"));
        break;
    }
//    return -1;
  }
  else
  {
//  	System.out.println(cl.Nick+"Enter Chat....");
  	me.addChatNick(cl.Nick,cl);
    me.replyCmd(cl,new ChatLoginState(true,"Welcome"));
    me.replyCmd(cl,new ChatActions(ChatServer.myChat.Verb1_1,ChatServer.myChat.Verb1_2,ChatServer.myChat.Verb2,ChatServer.myChat.Verb3));
  }
}

  public void replyCmd(Object source,Object theCmd)
  {
    if(source instanceof MultiModuleCmd)
    {
      int dest=((MultiModuleCmd)source).getID();
      if(theCmd instanceof MultiModuleReply)
        ((MultiModuleReply)theCmd).setID(dest);
    }
    sendCmd(theCmd);
  }

  public void sendCmd(Object theObj)
  {
    if(oos!=null)
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
/**
 * 處理聊天室的功能
 * @return int
 */
public int chatShell()
{
	boolean loginok=false;

	try
	{
		Object cmd=null;
		while(ois!=null)
		{
//			LastSig=new Date();
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
          	loginok=true;
          }
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
  	if(chatNickTable!=null)
    {
    	Enumeration chatUsers=chatNickTable.elements();
      while(chatUsers.hasMoreElements())
      {
      	String theNick=(String)chatUsers.nextElement();
				ChatServer.myChat.exitChat(theNick);
      }
    }
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
	}
	return 0;
}
/*public int chatShell()
{
	boolean loginok=false;

	try
	{
		Object cmd=null;
		while(objInput!=null)
		{
			cmd=objInput.readObject();
			try
			{
				if(loginok&&ID!=null&&Nick!=null)
				{
					switch(cmd.getClass().getName().hashCode())
					{
						case 2061823598: //colabbs.bbstp.chat.ChatPostMsg
						{
							ChatPostMsg cpm=(ChatPostMsg)cmd;
							ChatServer.myChat.postMsg(Nick,cpm.Message);
							System.out.println(cpm.Message);
							break;
						}
						case 62815826: //colabbs.bbstp.chat.ChatChangeNick
						{
							ChatChangeNick ccn=(ChatChangeNick)cmd;
							if(ChatServer.myChat.changeNick(Nick,ccn.newName)>=0)
								Nick=ccn.newName;
							break;
						}
						case -1650299624: //colabbs.bbstp.chat.ChatEnterRoom
						{
							ChatEnterRoom cer=(ChatEnterRoom)cmd;
							ChatServer.myChat.enterRoom(Nick,cer.Room);
							break;
						}
						case -1193272087: //colabbs.bbstp.chat.ChatEnterRoom
						{
							ChatSetTopic cst=(ChatSetTopic)cmd;
							ChatServer.myChat.setTopic(Nick,cst.newTopic);
							break;
						}
						case 2061823246: //colabbs.bbstp.chat.ChatHelps
						{
							ChatHelps ch=(ChatHelps)cmd;
							switch(ch.mode)
							{
								case 1:
									ChatServer.myChat.doHelp(Nick);
									break;
								case 2:
									ChatServer.myChat.doOpHelp(Nick);
									break;
								case 3:
									ChatServer.myChat.doActionHelp(Nick);
									break;
							}
							break;
						}
						case -1193273372: //colabbs.bbstp.chat.ChatSendNote
						{
							ChatSendNote csn=(ChatSendNote)cmd;
							ChatServer.myChat.sendNote(Nick,csn.Target,csn.Note);
							break;
						}
						case -1193267002: //colabbs.bbstp.chat.ChatListUser & colabbs.bbstp.chat.ChatKickUser
							if(cmd instanceof ChatListUser)
							{
								ChatListUser clu=(ChatListUser)cmd;
								switch(clu.mode)
								{
									case 1:
										ChatServer.myChat.fullListUser(Nick,clu.Room);
										break;
									case 2:
										ChatServer.myChat.listUser(Nick,clu.Room);
										break;
									case 3:
										ChatServer.myChat.listRoom(Nick);
										break;
								}
							}
							else if(cmd instanceof ChatKickUser)
							{
								ChatKickUser cku=(ChatKickUser)cmd;
								ChatServer.myChat.kickUser(Nick,cku.Nick);
							}
							break;
						case -1193256671: //colabbs.bbstp.chat.ChatDoAction
						{
							ChatDoAction cda=(ChatDoAction)cmd;
							ChatServer.myChat.doAction(Nick,cda.Arg1,cda.Arg2);
							break;
						}
						case 2061823211: //colabbs.bbstp.chat.ChatSetOP
						{
							ChatSetOP csop=(ChatSetOP)cmd;
							ChatServer.myChat.setOP(Nick,csop.newOP);
							break;
						}
						case 62824959: //colabbs.bbstp.chat.ChatInviteUser
						{
							ChatInviteUser ciu=(ChatInviteUser)cmd;
							ChatServer.myChat.inviteUser(Nick,ciu.Nick);
							break;
						}
						case -954086084: //colabbs.bbstp.chat.ChatChangeRoomProperties
						{
							ChatChangeRoomProperties ccrp=(ChatChangeRoomProperties)cmd;
							if(ccrp.lock)
								ChatServer.myChat.setLockRoom(Nick);
							if(ccrp.hide)
								ChatServer.myChat.setHideRoom(Nick);
							break;
						}
						case 2061823638: //colabbs.bbstp.chat.ChatLogout
							ChatServer.myChat.exitChat(Nick);
							return 0;
						default:
							break;
					}
				}
				else
				{
					if(cmd instanceof ChatLogin)
					{
						int code;
						ChatLogin cl=(ChatLogin)cmd;

						if((code=ChatServer.myChat.enterChat(cl.Nick,cl.ID,"",this))<=0)
						{
							switch(code)
							{
								case -1:
									objOutput.writeObject(new ChatLoginState("Chat Room is Full!"));
									break;
								case -2:
									objOutput.writeObject(new ChatLoginState("Another people use this nick name!"));
									break;
								default:
									objOutput.writeObject(new ChatLoginState("Login Error!"));
									break;
							}
							return -1;
						}
						else
						{
							Nick=cl.Nick;
							ID=cl.ID;
							loginok=true;
							objOutput.writeObject(new ChatLoginState(true,"Welcome"));
							objOutput.writeObject(new ChatActions(ChatServer.myChat.Verb1_1,ChatServer.myChat.Verb1_2,ChatServer.myChat.Verb2,ChatServer.myChat.Verb3));
						}
					}
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
		try
		{
			objInput.close();
			objOutput.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	return 0;
}*/
}