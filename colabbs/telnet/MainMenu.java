package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.MENU.*;

public final class MainMenu extends ListMenu
{
	public MenuItems MenuBuf;

	public MainMenu(int pidbuf)
	{
		MenuFlag=true;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
	}
	
	public void DoMenu()
	{
		int inkey=0;
		byte echobuf[]=new byte[1];
		String OrgMode=User.usermode;
		MainMenuItem ItemBuf;
		LinkList MenuPath=new LinkList();

		BBS.SetUserMode(pid,Modes.MainMenu);
		//		PrintAcBar();

		MenuBuf=ColaServer.BBSMenus.SearchMenu("TOPMENU");
		if(MenuBuf==null)
		{
			User.sends(Prompt.Msgs[89]);
			return;
		}

		ListMax=MenuBuf.GetNum(pid)-1;
		//MaxRow=24-MenuBuf.y;
		MaxRow=23-MenuBuf.y;

		EchoStr=Prompt.Msgs[90];
		CleanStr="  ";
		StartX=MenuBuf.x-EchoStr.length();
		StartY=MenuBuf.y;
		if(User.hasNewMail())
		{
			int locate=0;
			if((locate=MenuBuf.Goto(pid,"!Mail"))!=-1)
			{
				//				HideCursor();
				cursor=locate;
			}
		}

MenuLoop:
		while(inkey>=0)
		{
			inMenu = true;
			if(ReDrawFlag)
			{
				User.Clear();
				PrintAcBar();
				MenuBuf.Dump(pid,now);
			}
			ReDrawFlag=true;
			//ReDrawFlag=false;

			switch((inkey=ReadKey(true)))
			{
			case Keys.Left:
				{
					MenuItems BackBuf=MenuBuf;

					MenuBuf=(MenuItems)MenuPath.del1();
					if(MenuBuf==null)
					{
						int locate;

						MenuBuf=ColaServer.BBSMenus.SearchMenu("TOPMENU");
						//                            if((locate=MenuBuf.Where(pid,'G'))!=-1)
						if((locate=MenuBuf.Goto(pid,"@LeaveBBS"))!=-1)
						{
							HideCursor();
							if((cursor/MaxRow)==(locate/MaxRow))
								ReDrawFlag=false;
							cursor=locate;
						}
						break;
					}
					else
					{
						cursor=MenuBuf.Goto(pid,"!"+BackBuf.MenuName);
						if(cursor<0)
							cursor=0;
					}

					//                        User.Clear();

					//                        MenuBuf.Dump(pid,0);

					ListMax=MenuBuf.GetNum(pid)-1;
					MaxRow=23-MenuBuf.y;
					StartX=MenuBuf.x-EchoStr.length();
					StartY=MenuBuf.y;
					now=0;
				
					//                        cursor=0;
					//						User.Clear();
					//						PrintAcBar();
					break;
				}
			case Keys.Enter:
			case Keys.Right:
				// '!'¬O¿ï³æ  '@'¬O¥\¯à......
				inMenu = false;
				ItemBuf=MenuBuf.ItemAt(pid,now+cursor);
				if(ItemBuf.FuncName.charAt(0)=='!')
				{
					if(ItemBuf.FuncName.substring(1).equals(".."))
					{
						MenuItems BackBuf=MenuBuf;

						MenuBuf=(MenuItems)MenuPath.del1();
						cursor=0;
						if(MenuBuf==null)
						{
							MenuBuf=ColaServer.BBSMenus.SearchMenu("TOPMENU");
							break;
						}
						else
						{
							cursor=MenuBuf.Goto(pid,"!"+BackBuf.MenuName);
							//								System.out.println(cursor);
							if(cursor<0)
								cursor=0;
						}
					}
					else
					{
						MenuPath.ladditem(MenuBuf);
						MenuBuf=ColaServer.BBSMenus.SearchMenu(ItemBuf.FuncName.substring(1));
						if(MenuBuf==null)
						{
							User.move(1,2);
							User.sends(Prompt.Msgs[20]);
							MenuBuf=(MenuItems)MenuPath.del1();
							break;
						}
						cursor=0;
					}

					//                        User.Clear();

					//                        MenuBuf.Dump(pid,0);

					ListMax=MenuBuf.GetNum(pid)-1;
					MaxRow=23-MenuBuf.y;
					StartX=MenuBuf.x-EchoStr.length();
					StartY=MenuBuf.y;
					now = cursor - cursor % MaxRow;
					cursor = cursor % MaxRow;
				}
				else if(ItemBuf.FuncName.charAt(0)=='@')
				{
					String cmpbuf=ItemBuf.FuncName.substring(1);
					//Add by WilliamWey
					String parabuf;
					int seeks = cmpbuf.indexOf("@");
					if (seeks == -1)
						parabuf = new String("");
					else
					{
						parabuf = cmpbuf.substring(seeks + 1).trim();
						cmpbuf = cmpbuf.substring(0, seeks).trim();
					}
					//

					if(cmpbuf.equals("LeaveBBS"))
						break MenuLoop;
					else if(cmpbuf.equals("ShowLogins"))
					{
						User.Clear();
						ListUsers ListBuf=new ListUsers(pid);
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("BoardsAll"))
					{
						ListBoards ListBuf=new ListBoards(pid,(short)-1);
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("NewsNode"))
					{
						User.Clear();
						NodeList ListBuf=new NodeList(pid);
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("NewsFeed"))
					{
						User.Clear();
						NewsFeedList ListBuf=new NewsFeedList(pid);
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("ReadNew"))
					{
						ListBoards ListBuf=new ListBoards(pid,(short)-1);
						ListBuf.setNewMode(true);
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("EGroups"))
					{
						ListBoards ListBuf=new ListBoards(pid,(short)(ItemBuf.EName.charAt(0)-'0'));
						ListBuf.DoList();
						ListBuf=null;
					}
					else if(cmpbuf.equals("ReadMail"))
					{
						User.Clear();
						User.DirListBuf=new MailList(pid);
						User.DirListBuf.DoList();
						User.DirListBuf=null;
					}
					else if(cmpbuf.equals("SetFriend"))
					{
						User.Clear();
						(new FriendList(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,"friends",ColaServer.INI.Maxfriends,pid)).DoList();
					}
					else if(cmpbuf.equals("SetBlack"))
					{
						User.Clear();
						(new FriendList(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,"blacks",ColaServer.INI.Maxblacks,pid)).DoList();
					}
					else if(cmpbuf.equals("SetInfo"))
					{
						User.Clear();
						User.SetInfo();
					}
					else if(cmpbuf.equals("SetPager"))
					{
						User.UFD.UserDefine^=Consts.AllPager;
						User.move(1,24);
						User.clrtoeol();
						if((User.UFD.UserDefine&Consts.AllPager)==0)
							User.sends(Prompt.Msgs[91]);
						else
							User.sends(Prompt.Msgs[92]);
						TIME.Delay(1000);
						User.printendline();
						//                            User.SetPager();
					}
					else if(cmpbuf.equals("Notepad"))
					{
						User.Clear();
						User.ansimore(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad",0,true);
						User.getch();
					}
					else if(cmpbuf.equals("ShowLicense"))
					{
						User.Clear();
						User.ansimore(ColaServer.INI.BBSHome+"License.txt");
						User.move(1,24);
						User.clrtoeol();
						User.PressAnyKey();
					}
					else if(cmpbuf.equals("ShowVersion"))
					{
						User.Clear();
						User.ansimore(ColaServer.INI.BBSHome+"Copyright.txt");
						User.move(1,24);
						User.clrtoeol();
						User.PressAnyKey();
					}
					else if(cmpbuf.equals("LookMsg"))
					{
						User.Clear();
						User.ansimore(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator+"Messages", -1, false);
						User.move(1,24);
						User.clrtoeol();
						User.PressAnyKey();
					}
					else if(cmpbuf.equals("ChUserPwd"))
						User.ChUserPwd();
					else if(cmpbuf.equals("ChPwd"))
						User.ChPwd();
					else if(cmpbuf.equals("EditUFiles"))
						User.EditUFiles();
					else if(cmpbuf.equals("SendMail"))
						User.DoMail();
					else if(cmpbuf.equals("QueryUser"))
						User.DoQuery();
					else if(cmpbuf.equals("UserDefine"))
						User.UFD.UserDefine=TelnetUtils.UserDef(User.UFD.UserDefine,pid);
					else if(cmpbuf.equals("EnterChat"))
						User.telnetEnterChat();
					else if(cmpbuf.equals("FillForm"))
						User.FillForm();
					else if(cmpbuf.equals("CheckForm"))
						User.CheckForm();
					else if(cmpbuf.equals("ModifyLevel"))
						User.ChUserPerm();
					else if(cmpbuf.equals("ModifyInfo"))
						User.ModifyInfo();
					else if(cmpbuf.equals("KickUser"))
						User.KickUser();
					else if(cmpbuf.equals("DelUser"))
						User.telnetDelUser();
					else if(cmpbuf.equals("NewBoard"))
						User.telnetNewBoard();
					else if(cmpbuf.equals("EditBoard"))
						User.EditBoard();
					else if(cmpbuf.equals("DelBoard"))
						User.telnetDelBoard();
					else if(cmpbuf.equals("SendMsg"))
						User.SendMsg(-1,null);
					else if(cmpbuf.equals("Talk"))
						User.Talk(-1);
					else if(cmpbuf.equals("Cloak"))
						User.Visible=!User.Visible;
					else if(cmpbuf.equals("ShutDown"))
					{
						User.move(1, 24);
						User.clrtoeol();
						User.sends(Prompt.Msgs[472]);
						if (User.MakeSure() == 'Y')
							(new ShutDown(0)).start();
						else
							User.printendline();
					}
					else if(cmpbuf.equals("SendEMailAll"))
						User.sendEMailAll();
					//Add by WilliamWey
					else if(cmpbuf.equals("SendMsgToFriends"))
						User.SendMsgToFriends(null);
					else if(cmpbuf.equals("SendMsgToAll"))
						User.SendMsgToAll(null);
					else if(cmpbuf.equals("RunPlasmid"))
						User.RunPlasmid(parabuf);
					else if(cmpbuf.equals("ViewFile"))
						User.ViewFile(parabuf);
					else if(cmpbuf.equals("EditFile"))
						User.EditFile(parabuf);
					//

					//User.Clear();

					//                        MenuBuf.Dump(pid,0);
				}
				//					PrintAcBar();
				break;
			case -1:
				break;
			default:
				int locate;

				if(((char)inkey>='a'&&(char)inkey<='z')||((char)inkey>='A'&&(char)inkey<='Z')||((char)inkey>='0'&&(char)inkey<='9'))
				{
					ReDrawFlag=false;
					if((locate=MenuBuf.Where(pid,(char)inkey))!=-1)
					{
						HideCursor();
						if(now / MaxRow != locate / MaxRow)
							ReDrawFlag=true;
						now = locate - locate % MaxRow;
						cursor = locate % MaxRow;
					}
				}
				break;
			}
		}
		BBS.SetUserMode(pid,OrgMode);
	}
	
	public void PrintAcBar()
	{
		int i;
		int oldx=User.ScX,oldy=User.ScY;

		//User.move(52,2);
		//User.sends(Prompt.Msgs[86]+Utils.Cut(User.UFD.UserLevel,16)+"[33m]");
		if(ColaServer.INI.ActBoardBar)
		{
			User.move(1,3);
			User.sends(Prompt.Msgs[87]);
			for (i = 4; i <= ColaServer.INI.MaxActBoard + 3; i++)
			{
				User.move(1, i);
				User.clrtoeol();
			}				
			User.move(1,ColaServer.INI.MaxActBoard+4);
			User.sends(Prompt.Msgs[88]);
		}
		else
		{
			for (i = 3; i <= ColaServer.INI.MaxActBoard + 4; i++)
			{
				User.move(1, i);
				User.clrtoeol();
			}				
		}
		
		User.move(1, 4);
		User.sends("[m");
		User.sends(ColaServer.ACBoard.getString());
		User.sends("[m");
		User.move(oldx, oldy);
		User.flush();
	}
	
	public void ReDraw()
	{
		int oldx=User.ScX,oldy=User.ScY;

		MenuBuf.Dump(pid,0);
		PrintAcBar();
		User.move(oldx,oldy);
	}
}