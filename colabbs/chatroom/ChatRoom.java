package colabbs.chatroom;

import java.io.*;
import java.util.*;

import colabbs.ColaServer;
import colabbs.BBSUser;
import colabbs.Prompt;
import colabbs.UTILS.*;
//import colabbs.ColaServer; // the following two is for Chinese BBS Name (:
//import colabbs.INIReader;
//import colabbs.telnet.TelnetUser;

public class ChatRoom
{
	public String BBSName="Cola Chat Room";
	public Hashtable Verb1_1=null,Verb1_2=null,Verb2=null,Verb3=null;
	private Hashtable allPeople = new Hashtable(),allRoom = new Hashtable();
	private Vector Help=null,OpHelp=null;
	private String Note[]=new String[3];
	public ChatRoom()
	{
		super();

		int section=1;
		String ReadBuffer=null;

		
		allRoom.put("main",new RoomObj("main",Prompt.Msgs[411],""));
		DataInputStream theINI=null;
		try
		{
			StringTokenizer st=null;
			String cmd=null;
			
			theINI=new DataInputStream(new FileInputStream("ChatRoom.INI"));
			if(theINI==null)
				return;
			Help=new Vector();
			OpHelp=new Vector();
			Verb1_1=new Hashtable();
			Verb1_2=new Hashtable();
			Verb2=new Hashtable();
			Verb3=new Hashtable();
			while((ReadBuffer=theINI.readLine())!=null)
			{
				
				if(ReadBuffer.indexOf('#')>=0)
					ReadBuffer=ReadBuffer.substring(0,ReadBuffer.indexOf('#'));
				if(ReadBuffer.length()==0)
					continue;
				if(ReadBuffer.length()>=2&&ReadBuffer.substring(0,2).equals("%%"))
				{
					section++;
					continue;
				}
				else if(section>=3&&ReadBuffer.charAt(0)=='%')
				{
					Note[section-3]=ReadBuffer.substring(1);
					continue;
				}
				switch(section)
				{
					case 1:
						Help.addElement(ReadBuffer);
						break;
					case 2:
						OpHelp.addElement(ReadBuffer);
						break;
					case 3:
						st=new StringTokenizer(ReadBuffer);
						cmd=st.nextToken();
						Verb1_1.put(cmd,st.nextToken());
						if(st.hasMoreTokens())
							Verb1_2.put(cmd,st.nextToken());
/*						System.out.println(st.nextToken());
						System.out.println(st.nextToken());
						if(st.hasMoreTokens())
							System.out.println(st.nextToken());*/
						break;
					case 4:
						st=new StringTokenizer(ReadBuffer);
						cmd=st.nextToken();
						Verb2.put(cmd,st.nextToken());
//						System.out.println(st.nextToken());
//						System.out.println(st.nextToken());
						break;
					case 5:
						st=new StringTokenizer(ReadBuffer);
						cmd=st.nextToken();
						Verb3.put(cmd,st.nextToken());
//						System.out.println(st.nextToken());
//						System.out.println(st.nextToken());
						break;
				}
			}
/*					switch(cmd.getClass().getName().hashCode())
					{
						case 2061823598: //colabbs.bbstp.chat.ChatPostMsg
						{
							ChatPostMsg cpm=(ChatPostMsg)cmd;
							ColaServer.myChat.postMsg(chatID,cpm.Message);
							System.out.println(cpm.Message);
							break;
						}
						case 62815826: //colabbs.bbstp.chat.ChatChangechatID
						{
							ChatChangeNick ccn=(ChatChangeNick)cmd;
							if(ColaServer.myChat.changeNick(chatID,ccn.newName)>=0)
								chatID=ccn.newName;
							break;
						}
						case -1650299624: //colabbs.bbstp.chat.ChatEnterRoom
						{
							ChatEnterRoom cer=(ChatEnterRoom)cmd;
							ColaServer.myChat.enterRoom(chatID,cer.Room);
							break;
						}
						case -1193272087: //colabbs.bbstp.chat.ChatEnterRoom
						{
							ChatSetTopic cst=(ChatSetTopic)cmd;
							ColaServer.myChat.setTopic(chatID,cst.newTopic);
							break;
						}
						case 2061823246: //colabbs.bbstp.chat.ChatHelps
						{
							ChatHelps ch=(ChatHelps)cmd;
							switch(ch.mode)
							{
								case 1:
									ColaServer.myChat.doHelp(chatID);
									break;
								case 2:
									ColaServer.myChat.doOpHelp(chatID);
									break;
								case 3:
									ColaServer.myChat.doActionHelp(chatID);
									break;
							}
							break;
						}
						case -1193273372: //colabbs.bbstp.chat.ChatSendNote
						{
							ChatSendNote csn=(ChatSendNote)cmd;
							ColaServer.myChat.sendNote(chatID,csn.Target,csn.Note);
							break;
						}
						case -1193267002: //colabbs.bbstp.chat.ChatListUser & colabbs.bbstp.chat.ChatKickUser
							if(cmd instanceof ChatListUser)
							{
								ChatListUser clu=(ChatListUser)cmd;
								switch(clu.mode)
								{
									case 1:
										ColaServer.myChat.fullListUser(chatID,clu.Room);
										break;
									case 2:
										ColaServer.myChat.listUser(chatID,clu.Room);
										break;
									case 3:
										ColaServer.myChat.listRoom(chatID);
										break;
								}
							}
							else if(cmd instanceof ChatKickUser)
							{
								ChatKickUser cku=(ChatKickUser)cmd;
								ColaServer.myChat.kickUser(chatID,cku.Nick);
							}
							break;
						case -1193256671: //colabbs.bbstp.chat.ChatDoAction
						{
							ChatDoAction cda=(ChatDoAction)cmd;
							ColaServer.myChat.doAction(chatID,cda.Arg1,cda.Arg2);
							break;
						}
						case 2061823211: //colabbs.bbstp.chat.ChatSetOP
						{
							ChatSetOP csop=(ChatSetOP)cmd;
							ColaServer.myChat.setOP(chatID,csop.newOP);
							break;
						}
						case 62824959: //colabbs.bbstp.chat.ChatInviteUser
						{
							ChatInviteUser ciu=(ChatInviteUser)cmd;
							ColaServer.myChat.inviteUser(chatID,ciu.Nick);
							break;
						}
						case -954086084: //colabbs.bbstp.chat.ChatChangeRoomProperties
						{
							ChatChangeRoomProperties ccrp=(ChatChangeRoomProperties)cmd;
							if(ccrp.lock)
								ColaServer.myChat.setLockRoom(chatID);
							if(ccrp.hide)
								ColaServer.myChat.setHideRoom(chatID);
							break;
						}
						case 2061823638: //colabbs.bbstp.chat.ChatLogout
							ColaServer.myChat.exitChat(chatID);
							chatID=null;
							chatloginok=false;
							return; //while bbsclient complete this will change to break!!
						default:
							break;
					}*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(theINI!=null)
				{
					theINI.close();
					theINI=null;
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	private void broadcastMsg(String nick,String msg)
	{
		synchronized(allPeople)
		{
			ChatUser me=(ChatUser)allPeople.get(nick);
			if(me != null)
			{
				RoomObj ro=(RoomObj)allRoom.get(me.room);
        if(ro!=null&&ro.recording)
        {
        	RandomAccessFile myRecorder=null;
        	try
          {
	        	myRecorder=new RandomAccessFile(ColaServer.INI.BBSHome+"mailtemp"+File.separator+ro.name,"rw");
  	        myRecorder.seek(myRecorder.length());
    	      myRecorder.writeBytes(STRING.Cut(nick + ":",10) + msg+"\r\n");
          }
          catch(IOException e)
          {
          }
          finally
          {
          	try
            {
	          	if(myRecorder!=null)
  	          {
			          myRecorder.close();
      	        myRecorder=null;
        	    }
            }
            catch(IOException e){}
          }
        }

				Enumeration items = allPeople.elements();
				while(items.hasMoreElements())
				{
					ChatUser thisUser=(ChatUser)items.nextElement();
					if(thisUser!=null&&thisUser.room!=null&&thisUser.room.equals(me.room))
						thisUser.myClient.addChatMsg(thisUser.mid,STRING.Cut(nick + ":",10) + msg);
				}
//				for(int i=0;i<msg.length();i++)
//					System.out.println(""+(int)msg.charAt(i));
//				System.out.println(nick + ":" + msg);
			}
		}
	}
/**
 * This method was writen by yhwu.
 * @return int
 * @param oldName java.lang.String
 * @param newName java.lang.String
 */
public int changeNick(String oldName, String newName)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(oldName),tmp=(ChatUser)allPeople.get(newName);
		
		if(me==null)
			return -2;
		if(tmp!=null)
			return -1;
		sysMsg(me.room,"[32m "+oldName +" [1;36m"+ Prompt.Msgs[430]+" [32m"+newName);
//		sysMsg(me.room,id + " ¤w¸gÂ÷¶}¤F");
		allPeople.remove(oldName);
		allPeople.put(newName,me);
		me.nick=newName;
		RoomObj ro=(RoomObj)allRoom.get(me.room);
		if(ro.op.equalsIgnoreCase(oldName)&&ro!=null)
			ro.op=newName;
	}
	return 0;
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param cmd java.lang.String
 * @param msg java.lang.String
 * in verb type 1 "" for all people.
 */
public void doAction(String nick,String cmd, String msg)
{
	String str1=null,str2="";
	if((str1=(String)Verb1_1.get(cmd))!=null)
	{
		str2=(String)Verb1_2.get(cmd);
		if(str2==null)
			str2="";
		if(msg.equals(""))
		{
			msg=Prompt.Msgs[453];
		}
		else
		{
			synchronized(allPeople)
			{
				boolean match=false;
				ChatUser me=(ChatUser)allPeople.get(nick);
			
				if(me != null)
				{
					Enumeration items = allPeople.elements();
					while(items.hasMoreElements())
					{
						ChatUser thisUser=(ChatUser)items.nextElement();
						if(thisUser!=null&&thisUser.room!=null&&thisUser.room.equals(me.room)&&thisUser.nick!=null&&thisUser.nick.equalsIgnoreCase(msg))
						{
							msg=thisUser.nick;
							match=true;
						}
					}
				}
				if(!match)
				{
					sysMsg(nick,Prompt.Msgs[452]);
				}
			}
		}
	}
	else if((str1=(String)Verb2.get(cmd))!=null);
	else if((str1=(String)Verb3.get(cmd))!=null);
	else
		return;
	postAction(nick,"[1;36m"+nick+" [33m"+str1+" [1;32m"+msg+" [33m"+str2);
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void doActionHelp(String nick)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me==null)
			return;
			
		me.myClient.addChatMsg(me.mid,Note[0]);
		Enumeration items = Verb1_1.keys();
		while(items.hasMoreElements())
		{
			String tmp="";
			for(int i=0;i<8;i++)
				if(items.hasMoreElements())
					tmp+=STRING.Cut((String)items.nextElement(),10);
				else
					break;
			me.myClient.addChatMsg(me.mid,tmp);
		}
		
		me.myClient.addChatMsg(me.mid,Note[1]);
		items = Verb2.keys();
		while(items.hasMoreElements())
		{
			String tmp="";
			for(int i=0;i<8;i++)
				if(items.hasMoreElements())
					tmp+=STRING.Cut((String)items.nextElement(),10);
				else
					break;
			me.myClient.addChatMsg(me.mid,tmp);
		}
		
		me.myClient.addChatMsg(me.mid,Note[2]);
		items = Verb3.keys();
		while(items.hasMoreElements())
		{
			String tmp="";
			for(int i=0;i<8;i++)
				if(items.hasMoreElements())
					tmp+=STRING.Cut((String)items.nextElement(),10);
				else
					break;
			me.myClient.addChatMsg(me.mid,tmp);
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void doHelp(String nick)
{
		synchronized(allPeople)
		{
			ChatUser me=(ChatUser)allPeople.get(nick);
			if(me==null)
				return;
			Enumeration items = Help.elements();
			while(items.hasMoreElements())
				me.myClient.addChatMsg(me.mid,(String)items.nextElement());
		}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void doOpHelp(String nick)
{
		synchronized(allPeople)
		{
			ChatUser me=(ChatUser)allPeople.get(nick);
			if(me==null)
				return;
			Enumeration items = OpHelp.elements();
			while(items.hasMoreElements())
				me.myClient.addChatMsg(me.mid,(String)items.nextElement());
		}
}
/**
 * This method was created by yhwu.
 * @param theNick java.lang.String
 * @param theID java.lang.String
 * @param theFrom java.lang.String
 * @param theClient colabbs.chatroom.ChatClient
 * @param mid int
 */

	public int enterChat(String theNick,String theID,String theFrom,ChatClient theClient,int mid)
	{
//		System.out.println("ChatClient = "+theClient);
		synchronized(allPeople)
		{
			if(allPeople.size() >= 50)
			//too many people
				return -1;

			if(allPeople.get(theNick) != null)
			//existed nick
				return -2;

			if(theID==null)
			{
				ChatUser tmp=new ChatUser(theNick,"guest",theFrom,"",theClient,mid);
				tmp.guest=true;
				allPeople.put(theNick,tmp);
			}
			else
				allPeople.put(theNick,new ChatUser(theNick,theID,theFrom,"",theClient,mid));
		}
		theClient.addChatMsg(mid,"[1;31m"+Prompt.Msgs[413]+" [37m"+Prompt.Msgs[414]+"[32m"+BBSName+"[37m"+Prompt.Msgs[415]+"[36m "+allRoom.size()+"[37m "+Prompt.Msgs[416]+" [1;31m"+Prompt.Msgs[413]);
		theClient.addChatMsg(mid,"[1;31m"+Prompt.Msgs[413]+" [37m"+Prompt.Msgs[417]+"[36m "+allPeople.size()+" [37m"+Prompt.Msgs[418]+" [1;31m"+Prompt.Msgs[413]);
		enterRoom(theNick,"main");
//		sysMsg("main","½ÐÅwªï " + id + " ³o¦ì·s¥ë¦ñ¥[¤J");
		return 14;
	}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param room java.lang.String
 */
public void enterRoom(String nick, String room)
{
	ChatUser cu=null;
	synchronized(allPeople)
	{
		cu=(ChatUser)allPeople.get(nick);
	}
	if(cu==null)
		return;
	synchronized(allRoom)
	{
		exitRoom(nick,cu.room);
		RoomObj ro=(RoomObj)allRoom.get(room);
		if(ro==null)
		{
			ro=new RoomObj(room,Prompt.Msgs[411],nick);
			allRoom.put(room,ro);
		}
		else if(ro.lock&&!ro.inviteList.contains(nick))
		{
			cu.myClient.addChatMsg(cu.mid,"[1;31m"+Prompt.Msgs[459]);
			return;
		}
		else if(ro.inviteList.contains(nick))
		{
			ro.inviteList.removeElement(nick);
		}
		cu.myClient.changeRoom(cu.mid,room);
		cu.myClient.changeTopic(cu.mid,ro.title);
		ro.count++;
		cu.room=room;
	}
	sysMsg(room,"[37m[[36m"+nick+"[37m] " + Prompt.Msgs[419]+"[35m "+room+" [37m"+Prompt.Msgs[420]);
}
	public void exitChat(String nick)
	{
		synchronized(allPeople)
		{
			ChatUser me=(ChatUser)allPeople.get(nick);
			if(me==null)
				return;
//			sysMsg(me.room,"[0m"+nick +" [1;36m"+ Prompt.Msgs[421]+"[0m");
			exitRoom(nick,me.room);
//			sysMsg(me.room,nick + " ¤w¸gÂ÷¶}¤F");
			allPeople.remove(nick);
		}
	}
/**
 * This method was writen by yhwu.
 * @param id java.lang.String
 * @param room java.lang.String
 */
public void exitRoom(String nick,String room)
{
	if(room.length()==0)
		return;
	synchronized(allRoom)
	{
		RoomObj ro=(RoomObj)allRoom.get(room);
		if(ro!=null)
		{
			if(ro.op.equalsIgnoreCase(nick))
      {
      	if(ro.recording)
        {
	      	ro.recording=false;
        	mailRecord(ro.name,ro.op);
        }
				ro.op="";
      }
			sysMsg(room,"[0m"+nick +" [1;36m"+ Prompt.Msgs[421]+"[0m");
			ro.count--;
			if(ro.count==0&&!room.equals("main"))
				allRoom.remove(room);
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param room java.lang.String
 */
public void fullListUser(String nick,String room)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			Enumeration items = allPeople.elements();
			while(items.hasMoreElements())
			{
				ChatUser thisUser=(ChatUser)items.nextElement();
				if(thisUser!=null&&thisUser.room!=null&&(room==null||thisUser.room.equals(room)))
					me.myClient.addChatMsg(thisUser.mid,STRING.Cut(thisUser.nick,8) + Prompt.Msgs[422]+STRING.Cut(thisUser.ID,13) + Prompt.Msgs[422]+STRING.Cut(thisUser.room,13) + Prompt.Msgs[422]+STRING.Cut(thisUser.from,40));
			}
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param source java.lang.String
 * @param target java.lang.String
 */
public void inviteUser(String source, String target)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(source),he=(ChatUser)allPeople.get(target);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(source)&&ro!=null&&he!=null)
			{
				if(!ro.inviteList.contains(target))
					ro.inviteList.addElement(target);
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[427] +" [32m"+ target);
				he.myClient.addChatMsg(me.mid,"[1;32m"+ target+" [31m"+Prompt.Msgs[449] +"[33m "+me.room +" [1;31m"+Prompt.Msgs[420]);
			}
			else if(he==null)
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[413] +" [1;32m"+ target+" [1;33m"+Prompt.Msgs[431] +" [1;31m"+Prompt.Msgs[413]);
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
}
/**
 * This method was writen by yhwu.
 * @return int
 * @param source java.lang.String
 * @param target java.lang.String
 */
public int kickUser(String source, String target)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(source),he=(ChatUser)allPeople.get(target);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(source)&&ro!=null&&he!=null)
			{
				sysMsg(me.room,"[1;31m "+target+" "+Prompt.Msgs[454]);
				if(me.room.equals("main"))
				{
					he.myClient.changeRoom(he.mid,"");
					enterRoom(target,"");
				}
				else
				{
					he.myClient.changeRoom(he.mid,"main");
					enterRoom(target,"main");
				}
			}
			else if(he==null)
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[413] +" [1;32m"+ target+" [1;33m"+Prompt.Msgs[431] +" [1;31m"+Prompt.Msgs[413]);
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
	return 0;
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param room java.lang.String
 */
public void listRoom(String nick)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			Enumeration items = allRoom.elements();
			while(items.hasMoreElements())
			{
				RoomObj ro=(RoomObj)items.nextElement();
				if(!ro.hide)
					me.myClient.addChatMsg(me.mid," "+STRING.Cut(ro.name,12) + Prompt.Msgs[422]+STRING.Cut(""+ro.count,4) + Prompt.Msgs[422]+STRING.Cut(ro.title,40));
			}
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param room java.lang.String
 */
public void listUser(String nick, String room)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			Enumeration items = allPeople.elements();

ListLoop:
			while(items.hasMoreElements())
			{
				String tmp="";
				for(int i=0;i<2;i++)
				{
					if(items.hasMoreElements())
					{
						ChatUser thisUser=(ChatUser)items.nextElement();
						if(thisUser!=null&&thisUser.room!=null&&(room==null||thisUser.room.equals(room)))
							tmp+=" "+STRING.Cut(thisUser.nick,9)+" "+STRING.Cut(thisUser.ID,13)+ Prompt.Msgs[422];
						else if(i==0)
							continue ListLoop;
						else
						{
							i--;
							continue;
						}
					}
					else
						tmp+="                        "+ Prompt.Msgs[422];
				}
				if(items.hasMoreElements())
				{
					ChatUser thisUser=(ChatUser)items.nextElement();
					if(thisUser!=null&&thisUser.room!=null&&(room==null||thisUser.room.equals(room)))
						tmp+=" "+STRING.Cut(thisUser.nick,9)+" "+STRING.Cut(thisUser.ID,13);
				}
				else
					tmp+="                        ";
				me.myClient.addChatMsg(me.mid,tmp);
			}
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param msg java.lang.String
 */
public void postAction(String nick,String msg)
{
		synchronized(allPeople)
		{
			ChatUser me=(ChatUser)allPeople.get(nick);
			if(me==null)
				return;
			Enumeration items = allPeople.elements();
			while(items.hasMoreElements())
			{
				ChatUser thisUser=(ChatUser)items.nextElement();
				if(thisUser!=null&&thisUser.room!=null&&thisUser.room.equals(me.room))
					thisUser.myClient.addChatMsg(thisUser.mid,msg);
			}
		}
}
	public void postMsg(String nick,String msg)
	{
		broadcastMsg(nick,msg);
	}
/**
 * This method was writen by yhwu.
 * @return int
 * @param source java.lang.String
 * @param target java.lang.String
 * @param Msg java.lang.String
 */
public int sendNote(String source, String target, String Msg)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(source),he=(ChatUser)allPeople.get(target);
		if(me != null)
		{
			if(he!=null)
			{
				he.myClient.addChatMsg(he.mid," "+source+" "+Prompt.Msgs[428]+" "+Msg);
				me.myClient.addChatMsg(me.mid," "+Prompt.Msgs[429]+" "+target+" "+Msg);
			}
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[413] +" [1;32m"+ target+" [1;33m"+Prompt.Msgs[455] +" [1;31m"+Prompt.Msgs[413]);
		}
	}
	return 0;
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void setHideRoom(String nick)
{
	ChatUser cu=null;
	synchronized(allPeople)
	{
		cu=(ChatUser)allPeople.get(nick);
	}
	if(cu==null)
		return;
	synchronized(allRoom)
	{
		RoomObj ro=(RoomObj)allRoom.get(cu.room);
		if(ro!=null)
		{
			if(ro.op.equalsIgnoreCase(nick))
			{
				if(ro.hide)
				{
					ro.hide=false;
					sysMsg(cu.room,"[1;37m"+nick+" [31m"+Prompt.Msgs[458]+" [m");
				}
				else
				{
					ro.hide=true;
					sysMsg(cu.room,"[1;37m"+nick+" [31m"+Prompt.Msgs[457]+" [m");
				}
			}
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void setLockRoom(String nick)
{
	ChatUser cu=null;
	synchronized(allPeople)
	{
		cu=(ChatUser)allPeople.get(nick);
	}
	if(cu==null)
		return;
	synchronized(allRoom)
	{
		RoomObj ro=(RoomObj)allRoom.get(cu.room);
		if(ro!=null)
		{
			if(ro.op.equalsIgnoreCase(nick))
			{
				if(ro.lock)
				{
					ro.lock=false;
					sysMsg(cu.room,"[1;37m"+nick+" [31m"+Prompt.Msgs[456]+" [m");
				}
				else
				{
					ro.lock=true;
					sysMsg(cu.room,"[1;37m"+nick+" [31m"+Prompt.Msgs[455]+" [m");
				}
			}
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param source java.lang.String
 * @param target java.lang.String
 */
public void setOP(String source, String target)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(source),he=(ChatUser)allPeople.get(target);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(source)&&ro!=null&&he!=null)
			{
				ro.op=target;
				sysMsg(me.room,"[1;37m"+source+" [31m"+Prompt.Msgs[432]+" [1;32m"+target);
			}
			else if(he==null)
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[413] +" [1;32m"+ target+" [1;33m"+Prompt.Msgs[431] +" [1;31m"+Prompt.Msgs[413]);
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 * @param theTitle java.lang.String
 */
public void setTopic(String nick, String theTitle)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(nick)&&ro!=null)
			{
				ro.title=theTitle;
				Enumeration items = allPeople.elements();
				while(items.hasMoreElements())
				{
					ChatUser thisUser=(ChatUser)items.nextElement();
					if(thisUser!=null&&thisUser.room!=null&&thisUser.room.equals(me.room))
						thisUser.myClient.changeTopic(thisUser.mid,theTitle);
				}
				sysMsg(me.room,"[1;37m"+nick+" [31m"+Prompt.Msgs[472]+" [1;36m"+theTitle);
			}
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
}

/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void startRecord(String nick)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(nick)&&ro!=null)
			{
      	if(!ro.recording)
        	ro.recording=true;
        me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[473]+"[m");
			}
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
}
/**
 * This method was writen by yhwu.
 * @param room java.lang.String
 */
public void endRecord(RoomObj ro)
{
//	RoomObj ro=(RoomObj)allRoom.get(me.room);
  if(ro!=null&&ro.op!=null&&ro.op.length()>0)
  {
  	if(!ro.recording)
    {
    	ro.recording=false;
      mailRecord(ro.name,ro.op);
    }
  }
}
/**
 * This method was writen by yhwu.
 * @param nick java.lang.String
 */
public void endRecord(String nick)
{
	synchronized(allPeople)
	{
		ChatUser me=(ChatUser)allPeople.get(nick);
		if(me != null)
		{
			RoomObj ro=(RoomObj)allRoom.get(me.room);
			if(ro.op.equalsIgnoreCase(nick)&&ro!=null)
			{
      	if(!ro.recording)
        {
        	ro.recording=false;
          mailRecord(ro.name,ro.op);
        }
        me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[474]+"[m");
			}
			else
				me.myClient.addChatMsg(me.mid,"[1;31m"+Prompt.Msgs[450]+"[m");
		}
	}
}

private void mailRecord(String roomname,String roomop)
{
	synchronized(allPeople)
	{
		Object me=((ChatUser)allPeople.get(roomop)).myClient;
		if(me != null&&me instanceof BBSUser)
		{
			((BBSUser)me).MailFile(((BBSUser)me).UFD.ID,ColaServer.INI.BBSHome+"mailtemp",roomname,Prompt.Msgs[475],null,false);
		}
	}
}
/**
 * This method was created in VisualAge.
 * @param room java.lang.String
 * @param msg java.lang.String
 */
private void sysMsg(String room,String msg)
{
	synchronized(allPeople)
	{
				RoomObj ro=(RoomObj)allRoom.get(room);
        if(ro!=null&&ro.recording)
        {
        	RandomAccessFile myRecorder=null;
        	try
          {
	        	myRecorder=new RandomAccessFile(ColaServer.INI.BBSHome+"mailtemp"+File.separator+ro.name,"rw");
  	        myRecorder.seek(myRecorder.length());
    	      myRecorder.writeBytes("[1;31m"+Prompt.Msgs[413] +" "+ msg+"[1;31m "+Prompt.Msgs[413]+"\r\n");
          }
          catch(IOException e)
          {
          }
          finally
          {
          	try
            {
	          	if(myRecorder!=null)
  	          {
			          myRecorder.close();
      	        myRecorder=null;
        	    }
            }
            catch(IOException e){}
          }
        }
		if(room!= null)
		{
			Enumeration items = allPeople.elements();
			while(items.hasMoreElements())
			{
				ChatUser thisUser=(ChatUser)items.nextElement();
				if(thisUser.room.equals(room))
				{//may treat all client the same (:
//					if(thisUser.myClient instanceof TelnetUser)
						thisUser.myClient.addChatMsg(thisUser.mid,"[1;31m"+Prompt.Msgs[413] +" "+ msg+"[1;31m "+Prompt.Msgs[413]);
//					else
//						thisUser.myClient.addChatMsg(Prompt.Msgs[413] +" "+ msg +" "+Prompt.Msgs[413]);
				}
			}
		}
	}
}
}