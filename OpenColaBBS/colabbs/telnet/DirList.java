package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.BBSUser;
import colabbs.Prompt;
import colabbs.Consts;
import colabbs.UTILS.*;
import colabbs.ColaServer;
import colabbs.telnet.TelnetUser;
import colabbs.telnet.ListMenu;
import colabbs.telnet.LineEdit;
import colabbs.telnet.Keys;
import colabbs.Perm;
import colabbs.record.*;

public abstract class DirList extends ListMenu
{
	protected boolean digest=false;
	protected int lastnow=-1;
	protected String CurrentPath;
	protected String MyDir;
	public String OldMode;
	protected RecordType rt=null;
	protected Object lockObj=null;
	protected abstract void DelRecord(int index);
	protected abstract void DelRecordRange(int start,int end);
	protected abstract void DoHelp();
	
	public void DoList()
	{
		int i,InKey;
		RecordHandler rh=null;

		int L = (int)RecordHandler.recordNumber(rt, CurrentPath, MyDir);
		if(L < 0)
		{
			NoDir();
			if(L < 0)
			{
				BBS.SetUserMode(pid, OldMode);
				return;
			}
		}
		if (ListMax == -1)
			ListMax = L;
		else if (ListMax > L)
			ListMax = L;
		cursor = ListMax%MaxRow;
		now = ListMax - cursor;

DirListLoop:
		while(true)
		{
			if(lastnow!=now)
			{
				synchronized(lockObj)
				{
					try
					{
						if(RecordHandler.recordNumber(rt,CurrentPath,MyDir)<0)
						{
							NoDir();
							if(RecordHandler.recordNumber(rt,CurrentPath,MyDir)<0)
							{
								BBS.SetUserMode(pid,OldMode);
								return;
							}
						}
						
						rh=new RecordHandler(CurrentPath,MyDir,false);
						ListMax=(int)rh.recordNumber(rt);
						if(now+cursor>ListMax)
						{
							cursor=ListMax%MaxRow;
							now=ListMax-cursor;
						}

						((TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
						rh.setIndex(now,rt);
						
						for(i=now;i<now+MaxRow;i++)
						{
							if(!rh.nextElement(rt))
								break;
							ColaServer.BBSUsers[pid].sends("  "+STRING.CutLeft(""+(i+1),4)+recordTag(rt)+rt.getRecordString()+"[m\r\n");
						}
						for(;i<now+MaxRow;i++)
						{
							((TelnetUser)ColaServer.BBSUsers[pid]).move(1,i-now+4);
							((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
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
			else
				HideCursor();
			lastnow=now;

			switch((InKey=ReadKey(true)))
			{
			case 'h':
				DoHelp();
				((TelnetUser)ColaServer.BBSUsers[pid]).PressAnyKey();
				((TelnetUser)ColaServer.BBSUsers[pid]).Clear();
				DrawTitle();
				lastnow=-1;
				break;
			case 'd':
				if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					break;
				if(HasDelPerm(rt))
				{
					((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
					((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[17]);
					if(((TelnetUser)ColaServer.BBSUsers[pid]).MakeSure()!='Y')
					{
						((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
						break;
					}
					DelRecord(now+cursor);
					((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
					lastnow=-1;
				}
				break;
			case 'D':
				if(HasRangeDelPerm())
				{
					int StartNum,EndNum;
					StringBuffer NumBuf=new StringBuffer();

					try
					{
						((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
						((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
						ColaServer.BBSUsers[pid].sends(Prompt.Msgs[18]);
						(((TelnetUser)ColaServer.BBSUsers[pid]).GetData=new LineEdit(NumBuf,5 ,pid ,true)).DoEdit();
						((TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
						StartNum=Integer.parseInt(NumBuf.toString());
						((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
						ColaServer.BBSUsers[pid].sends(Prompt.Msgs[19]);
						NumBuf.setLength(0);
						(((TelnetUser)ColaServer.BBSUsers[pid]).GetData=new LineEdit(NumBuf,5 ,pid ,true)).DoEdit();
						((TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;

						EndNum=Integer.parseInt(NumBuf.toString());
						if(StartNum>EndNum)
							break;
						((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
						ColaServer.BBSUsers[pid].sends(Prompt.Msgs[17]);
						if(((TelnetUser)ColaServer.BBSUsers[pid]).MakeSure()!='Y')
							break;
						StartNum--;
						if(StartNum>ListMax)
							StartNum=ListMax;
						EndNum--;
						if(EndNum>ListMax)
							EndNum=ListMax;
						DelRecordRange(StartNum,EndNum);
						lastnow=-1;
					}
					catch(NumberFormatException e)
					{
						((TelnetUser)ColaServer.BBSUsers[pid]).Bell();
						((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
						((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
						ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
						TIME.Delay(1000);
					}
					finally
					{
						((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
					}
				}
				break;
			case 'F':
				String FEMAIL = ((TelnetUser)ColaServer.BBSUsers[pid]).ForwardEMail;
				if ((ColaServer.BBSUsers[pid].UFD.Perm & Perm.LoginOK) == 0)
					break;
				if (!RecordHandler.getRecord(now + cursor, lockObj, rt, CurrentPath, MyDir))
					break;
				
				if (FEMAIL == null)
					FEMAIL = ColaServer.BBSUsers[pid].UFD.Email;
				
				((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[460]);
				ColaServer.BBSUsers[pid].sends(FEMAIL);
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[461]);
				
				String ToWhere = null;
				StringBuffer T = new StringBuffer();
				
				switch (((TelnetUser)ColaServer.BBSUsers[pid]).MakeSure())
				{
				case 'Y':
					ToWhere = FEMAIL;
					break;
				case 'N':
					((TelnetUser)ColaServer.BBSUsers[pid]).move(1, 24);
					((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[462]);
					(((TelnetUser)ColaServer.BBSUsers[pid]).GetData=new LineEdit(T, 60, pid, true)).DoEdit();
					((TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;	
					if(T.length()!=0)
						ToWhere = T.toString();
					break;
				default:
				}
				
				if (ToWhere != null)
				{
					((TelnetUser)ColaServer.BBSUsers[pid]).ForwardEMail = ToWhere;
					((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
					((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();						
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[464]);
					ColaServer.BBSUsers[pid].sends(ToWhere);
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[465]);
					ColaServer.BBSUsers[pid].flush();
					synchronized(lockObj)
					{
						RecordHandler rh1 = null;
						rh1 = new RecordHandler(rt, CurrentPath, MyDir);

						if(!rh1.getRecord(now + cursor, rt))
							break;

						rh1.close();
					}
					ColaServer.BBSUsers[pid].MailFile(ToWhere, CurrentPath, rt.deleteBody(), ((ViewFileType)rt).getTitle(), ColaServer.BBSUsers[pid].UFD.ID, true);
					
					((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
					((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[466]);
				}
				else
				{
					((TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
					((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();						
					ColaServer.BBSUsers[pid].sends(Prompt.Msgs[463]);
				}
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(300);
				((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
				
				break;
				//case 'e':
			case 'q':
			case Keys.Left:
				exitList();
				if(digest)
				{
					digest=false;
					MyDir=Consts.DotDir;
					lastnow=-1;
					break;
				}
				break DirListLoop;
			case 0:
				break;
			case -1:
				break DirListLoop;
			default:
				if(OtherFunction(InKey))
					lastnow=-1;
				break;
			}
		}
		BBS.SetUserMode(pid,OldMode);
	}
	
	protected abstract void DrawTitle();
	protected abstract boolean HasDelPerm(RecordType t);
	protected abstract boolean HasRangeDelPerm();
	protected abstract void NoDir();
	protected abstract boolean OtherFunction(int OtherKey);
	protected abstract String recordTag(RecordType t);
	
	//
	protected abstract void exitList();
	//
	
	public void ReDraw()
	{
		DrawTitle();
		lastnow=-1;
	}
}