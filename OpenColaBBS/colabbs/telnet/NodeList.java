package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.CRONTAB.*;

public class NodeList extends DirList 
{
	public NodeList(int pidbuf)
	{
		CurrentPath=ColaServer.INI.BBSHome;
		MyDir=Consts.NewsNode;
		
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[337];
		CleanStr="[m  ";
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		OldMode=User.usermode;
		rt=new NewsNodeType();
		lockObj=Consts.NewsNode;
		
		BBS.SetUserMode(pid,Modes.Setup);
		DrawTitle();
	}

	protected void DelRecord(int index)
	{
		DelRecordRange(index,index);
	}
	
	protected void DelRecordRange(int start,int end)
	{
		RecordHandler.deleteRange(lockObj,rt,CurrentPath,MyDir,start,end);
	}
	
	protected void DoHelp()
	{
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"nodelisthelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		User.printtitle(Prompt.Msgs[377]);
		User.move(1,2);
		User.sends(Prompt.Msgs[379]);
		User.sends(Prompt.Msgs[380]);
	}
	
	protected boolean HasDelPerm(RecordType t)
	{
		if(BBS.HasOnePerm(pid,Perm.OBoards))
			return true;
		return false;
	}
	
	protected boolean HasRangeDelPerm()
	{
		if(BBS.HasOnePerm(pid,Perm.OBoards))
			return true;
		return false;
	}
	
	protected void NoDir()
	{
		User.move(1,4);
		User.sends(Prompt.Msgs[381]);
		switch(User.MakeSure())
		{
		case 'A':
			OtherFunction((int)'a');
			break;
		default:
			break;
		}
		//		User.move(1,23);
		//		User.PressAnyKey();
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		StringBuffer Ans=new StringBuffer();
		
		switch(OtherKey)
		{
		case 'a':
		case 'A':
			try
			{
				short theport=-1;
				BitSet min=new BitSet(60),hour=new BitSet(24),day=new BitSet(31),month=new BitSet(12),week=new BitSet(7);
				String thebbsname=null,theaddr=null,thecmd=null;
				
				User.Clear();
				User.move(1,2);
				User.sends(Prompt.Msgs[389]);
				(User.GetData=new LineEdit(Ans,Consts.NameLen-1,pid,true)).DoEdit();
				User.GetData=null;
				if(Ans.length()==0)
					return true;
				thebbsname=Ans.toString();

				Ans.setLength(0);
				User.sends(Prompt.Msgs[390]);
				(User.GetData=new LineEdit(Ans,Consts.StrLen-1,pid,true)).DoEdit();
				User.GetData=null;
				theaddr=Ans.toString();

				while(true)
				{
					Ans.setLength(0);
					User.move(1,6);
					User.sends(Prompt.Msgs[391]);
					if((User.GetData=new LineEdit(Ans,5,pid,true)).DoEdit()<0)
						return true;
					User.GetData=null;
					try
					{
						short temp=Short.parseShort(Ans.toString());
						theport=temp;
						break;
					}
					catch(NumberFormatException e)
					{
						((colabbs.telnet.TelnetUser)User).Bell();
						((colabbs.telnet.TelnetUser)User).move(1,24);
						((colabbs.telnet.TelnetUser)User).clrtoeol();
						User.sends(Prompt.Msgs[16]);
						User.flush();
						TIME.Delay(1000);
						((colabbs.telnet.TelnetUser)User).printendline();
					}
				}
				
				Ans.setLength(0);
				User.sends(Prompt.Msgs[392]);
				(User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit();
				User.GetData=null;
				thecmd=Ans.toString();

				CronTabItem.telnetSetCron(min,hour,day,month,week,pid);
				RecordHandler.append(lockObj,new NewsNodeType(thebbsname,theaddr,theport,thecmd,min,hour,day,month,week),CurrentPath,MyDir);
			}
			finally
			{
				DrawTitle();
			}
			return true;
		case 'e':
		case 'E':
			try
			{
				short theport=-1;
				BitSet min=new BitSet(60),hour=new BitSet(24),day=new BitSet(31),month=new BitSet(12),week=new BitSet(7);
				String thebbsname=null,theaddr=null,thecmd=null;

				NewsNodeType nnt=new NewsNodeType();
				RecordHandler.getRecord(now+cursor,lockObj,nnt,CurrentPath,MyDir);
				
				User.Clear();
				User.move(1,2);
				Ans=new StringBuffer(nnt.getBBSName());
				User.sends(Prompt.Msgs[389]);
				(User.GetData=new LineEdit(Ans,Consts.NameLen-1,pid,true)).DoEdit();
				User.GetData=null;
				if(Ans.length()==0)
					return true;
				thebbsname=Ans.toString();

				Ans=new StringBuffer(nnt.getAddress());
				User.sends(Prompt.Msgs[390]);
				(User.GetData=new LineEdit(Ans,Consts.StrLen-1,pid,true)).DoEdit();
				User.GetData=null;
				theaddr=Ans.toString();

				Ans=new StringBuffer(""+nnt.getPort());
				while(true)
				{
					User.move(1,6);
					User.sends(Prompt.Msgs[391]);
					if((User.GetData=new LineEdit(Ans,5,pid,true)).DoEdit()<0)
						return true;
					User.GetData=null;
					try
					{
						short temp=Short.parseShort(Ans.toString());
						theport=temp;
						break;
					}
					catch(NumberFormatException e)
					{
						((colabbs.telnet.TelnetUser)User).Bell();
						((colabbs.telnet.TelnetUser)User).move(1,24);
						((colabbs.telnet.TelnetUser)User).clrtoeol();
						User.sends(Prompt.Msgs[16]);
						User.flush();
						TIME.Delay(1000);
						((colabbs.telnet.TelnetUser)User).printendline();
					}
				}
				
				Ans=new StringBuffer(nnt.getCommand());
				User.sends(Prompt.Msgs[392]);
				(User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit();
				User.GetData=null;
				thecmd=Ans.toString();

				nnt.getTime(min,hour,day,month,week);

				CronTabItem.telnetSetCron(min,hour,day,month,week,pid);
				RecordHandler.update(now+cursor,lockObj,new NewsNodeType(thebbsname,theaddr,theport,thecmd,min,hour,day,month,week),CurrentPath,MyDir);
			}
			finally
			{
				DrawTitle();
			}
			return true;
		default:
			break;
		}
		return false;
	}

	protected String recordTag(RecordType t)
	{
		return "  ";
	}
	
	protected void exitList()
	{
	}
}