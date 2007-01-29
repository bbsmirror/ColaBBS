package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.USERFILEDATA.*;

public final class ListUsers
{
	private Hashtable Friend,MyFriend,Black,MyBlack;
	private static String None="";
	private byte ListMode=0;
	private byte PageMode[]=new byte[20];
	private int ThisPage[]=new int[20];
	private int PageIndex=0;
	private int now=0,pid,last;
	private TelnetUser User = null;
	private String EchoStr,CleanStr;

	public ListUsers(int pidbuf)
	{
		int i;
		RandomAccessFile LoadFile=null;
		byte readbuf[];
		String TestID,NoteBuf;

		EchoStr=Prompt.Msgs[85];
		CleanStr="[m  ";
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		
		//		User.move(1,1);
		User.printtitle(Prompt.Msgs[82]);
		//		User.move(1,2);
		User.sends(Prompt.Msgs[83]);
		User.sends(Prompt.Msgs[84]);
		initFriends();
	}
	
	public void DoList()
	{
		boolean query=false,NoteMode=false;
		String sendbuf,OrgMode=User.usermode;

		BBS.SetUserMode(pid,Modes.List);

		now=0;
		DoPage();
		if(ThisPage[0]==-1)
		{
			BBS.SetUserMode(pid,OrgMode);
			return;
		}

DoListLoop:
		while(true)
		{
			if(query)
			{
				synchronized(ColaServer.SortedUser)
				{
					User.Clear();
					User.printtitle(Prompt.Msgs[387],false);
					User.DoQuery(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID);
					User.move(1,24);
					User.sends(Prompt.Msgs[192]);
				}	
			}
			else
			{
				int i;
				String Color="[m";
				User.move(1,4);
				for(i=0;i<20;i++)
				{
					switch(PageMode[i])
					{
					case -1:
						for(;i<20;i++)
						{
							User.clrtoeol();
							User.newline();
						}
						continue;
					case 0:
						Color="[1;37m";
						break;
					case 1:
						Color="[1;33m";
						break;
					case 2:
						Color="[1;32m";
						break;
					case 3:
						Color="[m";
						break;
					}
					synchronized(ColaServer.SortedUser)
					{
						if(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]]!=null&&ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UserLogout==false&&ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.ID!=null)
						{
							Date Now=new Date();
							int idlemin=0,idlehour=0;
							char pch=' ',mch=' ';

							if(!ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].Visible)
								Color="[1;34m";
							else if(Black!=null&&Black.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].uid)))
								Color="[1;31m";

							if((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.UserDefine&Consts.AllPager)==0)
							{
								if(((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.UserDefine&Consts.FriendPager)!=0)&&PageMode[i]<2)
									pch='O';
								else
									pch='*';
							}
							if((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.UserDefine&Consts.AllMsgPager)==0)
							{
								if(((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.UserDefine&Consts.FriendMsgPager)!=0)&&PageMode[i]<2)
									mch='O';
								else
									mch='*';
							}

							if(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig!=null)
							{
								idlehour=(Now.getDate()-ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig.getDate())*24+Now.getHours()-ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig.getHours();
								idlemin=Now.getMinutes()-ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig.getMinutes();
								if(idlemin<0)
								{
									idlemin+=60;
									idlehour--;
								}
							}
							//Remark by WilliamWey
							//if(idlehour*60+idlemin>ColaServer.INI.IdleTimeout)
							//	ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].KickOut();
							//

							if(NoteMode&&(PageMode[i]==0||PageMode[i]==2))
								User.sends("  "+STRING.CutLeft(""+(i+1),4)+" "+Color+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.ID,13)+STRING.Cut((String)Friend.get(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].uid)),17)+"[m"+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].Home,17)+pch+" "+mch+" "+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].usermode,17)+STRING.CutLeft(""+idlehour,2)+":"+STRING.CutLeft(""+idlemin,2)+"\r\n");
							else
								User.sends("  "+STRING.CutLeft(""+(i+1),4)+" "+Color+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.ID,13)+"[m"+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].UFD.NickName,17)+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].Home,17)+pch+" "+mch+" "+STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].usermode,17)+STRING.CutLeft(""+idlehour,2)+":"+STRING.CutLeft(""+idlemin,2)+"\r\n");

							//Add by WilliamWey
							if(!BBS.HasOnePerm(ColaServer.SortedUser[ThisPage[i]],Perm.PostMask)&&idlehour*60+idlemin>ColaServer.INI.IdleTimeout) // Modified by yhwu
								ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].KickOut();
							//
						}	
						else if(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]]!=null&&ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig!=null)
						{
							Date Now=new Date();

							if(Now.getMinutes()-ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].LastSig.getMinutes()>=3)
								ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].KickOut();
						}
					}	
				}
			}

			switch(ReadKey(!query))
			{
			case 'K':
				if(((!User.UFD.ID.equals(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID))||User.UFD.ID.equalsIgnoreCase("guest"))&&!BBS.HasOnePerm(pid,Perm.SYSOP))
					break;
				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[317]+ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID+Prompt.Msgs[318]);
				if(User.MakeSure()!='Y')
				{
					User.move(1,24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[319]+ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID+Prompt.Msgs[320]);
					TIME.Delay(1000);
					User.printendline();
					break;
				}
				ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].KickOut();
				User.move(1,24);
				User.clrtoeol();
				User.sends(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID+Prompt.Msgs[321]);
				TIME.Delay(1000);
				User.printendline();

				break;
			case 'h':
			case 'H':
				User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"userlisthelp");
				User.PressAnyKey();
				User.Clear();
				User.move(1,1);
				User.printtitle(Prompt.Msgs[82]);
				User.move(1,2);
				User.sends(Prompt.Msgs[83]);
				User.sends(Prompt.Msgs[84]);
				break;
			case 'q':
			case 'Q':
			case 'e':
			case 'E':
			case Keys.Left:
				if(!query)
				{
					User.Clear();
					break DoListLoop;
				}
				User.Clear();
				User.move(1,1);
				User.printtitle(Prompt.Msgs[82]);
				User.move(1,2);
				User.sends(Prompt.Msgs[83]);
				User.sends(Prompt.Msgs[84]);
				query=false;
				break;
			case 'w':
			case 'W':
				NoteMode=!NoteMode;
				break;
			case 'o':
			case 'O':
				if(!BBS.HasOnePerm(pid,Perm.Post))
					break;
				if(Friend==null||!Friend.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid)))
				{
					//Add by WilliamWey
					User.clrtoeol();
					User.move(1, 24);
					User.sends(Prompt.Msgs[443]);
					User.sends(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID);
					User.sends(Prompt.Msgs[444]);
					if (User.MakeSure() == 'Y')
					{
						User.clrtoeol();
						User.move(1,23);
						if(Friend==null)
							Friend=new Hashtable(10);
						Friend.put(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid),FriendList.AddFriend(ColaServer.INI.BBSHome+File.separator+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID,"friends",User.UFD.ID,ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID,pid));
						User.printendline();
						DoPage();
					}
					else
						User.printendline();							
					break;
				}
				User.move(1,24);
				User.sends(Prompt.Msgs[346]);
				TIME.Delay(1000);
				User.printendline();
				break;
			case 'd':
			case 'D':
				if(!BBS.HasOnePerm(pid,Perm.Post))
					break;
				User.move(1,24);
				if(Friend!=null&&Friend.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid)))
				{
					//Add by WilliamWey
					User.clrtoeol();
					User.move(1, 24);
					User.sends(Prompt.Msgs[445]);
					User.sends(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID);
					User.sends(Prompt.Msgs[446]);
					if (User.MakeSure() == 'Y')
					{
						User.clrtoeol();
						FriendList.DelFriend("friends",User.UFD.ID,ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID,true);
						Friend.remove(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid));
						User.sends(Prompt.Msgs[348]);
						TIME.Delay(1000);
						User.printendline();						
						DoPage();
					}
					else
						User.printendline();
					break;
				}
				User.sends(Prompt.Msgs[347]);
				TIME.Delay(1000);
				User.printendline();
				break;
			case Keys.Right:
			case Keys.Enter:
			case 'r':
				query=true;
				break;
			case 0:
				break;
			case -1:
				break DoListLoop;
			case 'm':
			case 'M':
				if(!BBS.HasOnePerm(pid,Perm.Post))
					break;
				User.DoMail(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.ID);
				User.move(1,1);
				User.printtitle(Prompt.Msgs[82]);
				User.move(1,2);
				User.sends(Prompt.Msgs[83]);
				User.sends(Prompt.Msgs[84]);
				break;
			case 's':
			case 'S':
				if(!BBS.HasOnePerm(pid,Perm.Page))
					break;
				if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.UserDefine&Consts.AllMsgPager)==0)
				{
					if(((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.UserDefine&Consts.FriendMsgPager)==0)||MyFriend==null||(!MyFriend.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid))))
					{
						User.move(1,24);
						User.clrtoeol();
						User.sends(Prompt.Msgs[216]);
						TIME.Delay(1000);
						User.printendline();
						break;
					}
				}
				User.SendMsg(ColaServer.SortedUser[ThisPage[PageIndex]],null);
				//ª`·N¥H«á¥i¯à­n§ï!!
				break;
			case 't':
			case 'T':
				if(!BBS.HasOnePerm(pid,Perm.Page))
					break;
				if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.UserDefine&Consts.AllPager)==0)
				{
					if(((ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].UFD.UserDefine&Consts.FriendPager)==0)||MyFriend==null||(!MyFriend.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[PageIndex]]].uid))))
					{
						User.move(1,24);
						User.clrtoeol();
						User.sends(Prompt.Msgs[216]);
						TIME.Delay(1000);
						User.printendline();
						break;
					}
				}
				User.Talk(ColaServer.SortedUser[ThisPage[PageIndex]]);
				break;
			}
		}
		BBS.SetUserMode(pid,OrgMode);
		System.gc();
	}
	private void DoPage()
	{
		boolean empty=false;
		Integer uidtemp;
		int pagei=0;

		synchronized(ColaServer.SortedUser)
		{
			pagei=0;
			while(pagei<20)
			{
				if(now>=ColaServer.onlineuser)
				{
					now=0;
					ListMode++;
					if(ListMode>=4)
					{
						if(pagei==0)
						{
							if(empty)
							{
								if(PageIndex>pagei)
									PageIndex=pagei-1;
								ThisPage[0]=-1;
								PageMode[0]=-1;
								ThisPage[19]=-1;
								PageMode[19]=-1;
								ListMode=0;
								break;
							}
							else
							{
								empty=true;
								ListMode=0;
							}
						}
						else
						{
							if(PageIndex>pagei)
								PageIndex=pagei-1;
							ThisPage[pagei]=-1;
							PageMode[pagei]=-1;
							ThisPage[19]=-1;
							PageMode[19]=-1;
							ListMode=0;
							break;
						}
					}
				}
				if(!BBS.HasOnePerm(pid,Perm.SeeCloak)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[now]].Visible))
				{
					now++;
					continue;
				}
				uidtemp=new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[now]].uid);
				if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&MyBlack!=null&&MyBlack.containsKey(uidtemp))
				{
					now++;
					continue;
				}
				switch(ListMode)
				{
				case 0:
					if(Friend!=null&&Friend.containsKey(uidtemp)&&MyFriend!=null&&MyFriend.containsKey(uidtemp))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei++;
					}
					break;
				case 1:
					if((Friend==null||(!Friend.containsKey(uidtemp)))&&MyFriend!=null&&MyFriend.containsKey(uidtemp))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei++;
					}
					break;
				case 2:
					if(Friend!=null&&Friend.containsKey(uidtemp)&&(MyFriend==null||(!MyFriend.containsKey(uidtemp))))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei++;
					}
					break;
				case 3:
					if((Friend==null||(!Friend.containsKey(uidtemp)))&&(MyFriend==null||(!MyFriend.containsKey(uidtemp))))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei++;
					}
					break;
				}
				now++;
			}
		}
	}
	private void HideCursor()
	{
		User.move(1,last+4);
		User.sends(CleanStr);
		last=PageIndex;
	}
	/**
	 * This method was writen by yhwu.
	 */
	private void initFriends()
	{
		RecordHandler rh=null;
		
		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID,"friends");
			FriendType ft=new FriendType();
			int flen=(int)rh.recordNumber(ft);
			if(flen>=0)
			{
				Friend=new Hashtable(ColaServer.INI.Maxfriends);
				PassItem PassBuf=null;

				for(int i=0;i<=flen;i++)
				{
					rh.nextElement(ft);
					PassBuf=ColaServer.UFDList.getPass(ft.deleteBody());

					if(PassBuf!=null) //else delete the item?
						Friend.put(new Integer(PassBuf.uid),ft.getNote());
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

		//            Black
		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID,"blacks");
			FriendType ft=new FriendType();
			int flen=(int)rh.recordNumber(ft);
			if(flen>=0)
			{
				Black=new Hashtable(ColaServer.INI.Maxfriends);
				PassItem PassBuf=null;

				for(int i=0;i<=flen;i++)
				{
					rh.nextElement(ft);
					PassBuf=ColaServer.UFDList.getPass(ft.deleteBody());

					if(PassBuf!=null) //else delete the item?
						Black.put(new Integer(PassBuf.uid),ft.getNote());
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
		//            MyFriend
		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID,"Myfriends");
			MyFriendType mft=new MyFriendType();
			int flen=(int)rh.recordNumber(mft);
			if(flen>=0)
			{
				MyFriend=new Hashtable(ColaServer.INI.Maxfriends);
				PassItem PassBuf=null;

				for(int i=0;i<=flen;i++)
				{
					rh.nextElement(mft);
					PassBuf=ColaServer.UFDList.getPass(mft.deleteBody());

					if(PassBuf!=null) //else delete the item?
						MyFriend.put(new Integer(PassBuf.uid),None);
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
		//            MyBlack
		try
		{
			rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID,"Myblacks");
			MyFriendType mft=new MyFriendType();
			int flen=(int)rh.recordNumber(mft);
			if(flen>=0)
			{
				MyBlack=new Hashtable(ColaServer.INI.Maxfriends);
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
	}
	private int ReadKey(boolean echo)
	{
		int inkey;

ReadKeyLoop:
		while(true)
		{
			if(echo)
			{
				User.move(1,PageIndex+4);
				User.sends(EchoStr);
				User.move(80,24);
			}
			last=PageIndex;

			switch((inkey=User.getch()))
			{
			case -1:
				return -1;
			case 'k':
			case Keys.Down:
				if(PageIndex<19&&ThisPage[PageIndex+1]!=-1)
				{
					PageIndex++;
					break;
				}
				if(PageIndex<19&&ThisPage[PageIndex+1]==-1)
					ListMode=0;
				PageIndex=0; //¤S¬G·N¤£break
			case Keys.PgDn:
				now=ThisPage[19]+1;
				DoPage();
				break ReadKeyLoop;
			case Keys.Space:
				if(PageIndex==19||ThisPage[PageIndex+1]==-1)
				{
					now=ThisPage[19]+1;
					DoPage();
					break ReadKeyLoop;
				}
				PageIndex=19;
				if(ThisPage[19]==-1)
				{
					int i;

					for(i=0;i<19;i++)
						if(ThisPage[i]==-1)
						{
							PageIndex=i-1;
							if(PageIndex<0)
								PageIndex=0;
							break;
						}
				}
				break;
			case 'j':
			case Keys.Up:
				if(PageIndex>0)
				{
					PageIndex--;
					break;
				}
				if(ThisPage[0]==0)
					ListMode=3;
				PageIndex=19; //¤S¬G·N¤£break
			case Keys.PgUp:
				now=ThisPage[0]-1;
				ReverseDoPage();
				break ReadKeyLoop;
			default:
				if(inkey>=(int)'0'&&inkey<=(int)'9')
				{
					int GotoNum;
					StringBuffer NumBuf=new StringBuffer(""+(inkey-(int)'0'));

					User.move(1,24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[15]);
					(User.GetData=new LineEdit(NumBuf,5,pid,true)).DoEdit();
					User.GetData=null;
					try
					{
						GotoNum=Integer.parseInt(NumBuf.toString());
						GotoNum--;
						if(GotoNum>19)
							GotoNum=19;
						PageIndex=GotoNum;
					}
					catch(NumberFormatException e)
					{
						User.Bell();
						User.move(1,24);
						User.clrtoeol();
						User.sends(Prompt.Msgs[16]);
						TIME.Delay(1000);
					}
					User.printendline();
					return 0;
				}
				return inkey;
			}
			if(echo)
			{
				User.move(1,last+4);
				User.sends("[m"+CleanStr);
				User.move(80,24);
			}
			else
				break;
		}

		if(echo)
		{
			User.move(1,last+4);
			User.sends("[m"+CleanStr);
			User.move(80,24);
		}

		return 0;
	}
	private void ReverseDoPage()
	{
		Integer uidtemp;
		int pagei=19;

		synchronized(ColaServer.SortedUser)
		{
			ListMode=PageMode[0];
			pagei=19;
			while(pagei>=0)
			{
				if(now<0)
				{
					now=ColaServer.onlineuser-1;
					ListMode--;
					if(ListMode<0)
					{
						if(pagei==19)
							ListMode=3;
						else
						{
							now=0;
							ListMode=0;
							PageIndex=18-pagei;
							DoPage();
							return;
						}
					}
				}
				if(!BBS.HasOnePerm(pid,Perm.SeeCloak)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[now]].Visible))
				{
					now--;
					continue;
				}
				uidtemp=new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[now]].uid);
				if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&MyBlack!=null&&MyBlack.containsKey(uidtemp))
				{
					now--;
					continue;
				}
				switch(ListMode)
				{
				case 0:
					if(Friend!=null&&Friend.containsKey(uidtemp)&&MyFriend!=null&&MyFriend.containsKey(uidtemp))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei--;
					}
					break;
				case 1:
					if((Friend==null||(!Friend.containsKey(uidtemp)))&&MyFriend!=null&&MyFriend.containsKey(uidtemp))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei--;
					}
					break;
				case 2:
					if(Friend!=null&&Friend.containsKey(uidtemp)&&(MyFriend==null||(!MyFriend.containsKey(uidtemp))))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei--;
					}
					break;
				case 3:
					if((Friend==null||(!Friend.containsKey(uidtemp)))&&(MyFriend==null||(!MyFriend.containsKey(uidtemp))))
					{
						ThisPage[pagei]=now;
						PageMode[pagei]=ListMode;
						pagei--;
					}
					break;
				}
				now--;
			}
			ListMode=PageMode[19];
		}
	}
}