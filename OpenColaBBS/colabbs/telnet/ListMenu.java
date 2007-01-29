package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;
import colabbs.DATA.MENU.*;

public class ListMenu
{
	protected boolean MenuFlag=false;
	protected int now=0,cursor=0,pid,ListMax,StartX,StartY,MaxRow,last;
	protected String EchoStr,CleanStr;

	protected TelnetUser User = null;
	
	public boolean inMenu = false;	
	public boolean ReDrawFlag=true;

	public void HideCursor()
	{
		User.move(StartX,last+StartY);
		User.sends(CleanStr);
		last=cursor;
	}
	
	public int ReadKey(boolean echo)
	{
		int inkey;

ReadKeyLoop:
		while(true)
		{
			if(MenuFlag)
			{
				MainMenuItem ItemBuf;

				ItemBuf=User.BBSMainMenu.MenuBuf.ItemAt(pid,now+cursor);
				User.move(1,2);
				User.sends("[m                                        ");
				User.move(2,2);
				User.sends(Prompt.Msgs[14]+ItemBuf.EName+"[1;36m]");
				User.move(52,2);
				User.sends(Prompt.Msgs[86]+STRING.Cut(User.UFD.UserLevel,16)+"[33m]");
				User.move(80,24);
			}
			if(echo)
			{
				User.move(StartX,cursor+StartY);
				User.sends("[1;36m"+EchoStr);
				User.move(80,24);
			}
			last=cursor;

			switch((inkey=User.getch()))
			{
			case -1:
				return -1;
			case '$':
				cursor=ListMax%MaxRow;
				now=ListMax-cursor;
				break ReadKeyLoop;
			case Keys.Left:
				return Keys.Left;
			case 'k':
				if(MenuFlag)
					return 'k';
			case Keys.Down:
				if(cursor<MaxRow-1&&(now+cursor)<ListMax)
				{
					cursor++;
					break;
				}
				cursor=0; //¤S¬G·N¤£break
				//                case 'P':
				//                    if(MenuFlag)
				//                        return 'P';
			case Keys.PgDn:
				now+=MaxRow;
				if(now>ListMax)
					now=0;
				if(cursor>ListMax-now)
					cursor=ListMax%MaxRow;
				if(ListMax<MaxRow)
					break;
				break ReadKeyLoop;
			case Keys.Space:
				now+=MaxRow;
				if(now>ListMax)
				{
					cursor=ListMax%MaxRow;
					now=ListMax-ListMax%MaxRow;
				}
				if(cursor>ListMax-now)
					cursor=ListMax%MaxRow;
				if(ListMax<MaxRow)
					break;
				break ReadKeyLoop;
			case 'j':
				if(MenuFlag)
					return 'j';
			case Keys.Up:
				if(cursor>0)
				{
					cursor--;
					break;
				}
				if(ListMax<(MaxRow-1))
				{
					cursor=ListMax; //¤S¬G·N¤£break;
					ReDrawFlag = true;
				}
				else
					cursor=MaxRow-1; //¤S¬G·N¤£break;
				//                case 'N':
				//                    if(MenuFlag)
				//                        return 'N';
			case Keys.PgUp:
				now-=MaxRow;
				if(now<0)
				{
					now=ListMax-ListMax%MaxRow;
					if(cursor>ListMax-now)
						cursor=ListMax%MaxRow;
				}
				if(ListMax<MaxRow)
					break;
				break ReadKeyLoop;
			default:
				if(!MenuFlag&&inkey>=(int)'0'&&inkey<=(int)'9')
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
						if(GotoNum>ListMax)
							GotoNum=ListMax;
						if(GotoNum<0)
							GotoNum=0;
						cursor=GotoNum%MaxRow;
						now=GotoNum-cursor;
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
				User.move(StartX,last+StartY);
				User.sends("[m"+CleanStr);
				User.move(80,24);
			}
			else
				break;
		}

		if(echo)
		{
			User.move(StartX,last+StartY);
			User.sends("[m"+CleanStr);
			User.move(80,24);
		}

		return 0;
	}
}