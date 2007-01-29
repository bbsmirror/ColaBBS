package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;

public class NewsFeedList extends DirList {

	public NewsFeedList(int pidbuf)
	{
		CurrentPath=ColaServer.INI.BBSHome;
		MyDir=Consts.NewsFeed;
		
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[337];
		CleanStr="[m  ";
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		OldMode=User.usermode;
		rt=new NewsFeedType();
		lockObj=Consts.NewsFeed;
		
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
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"newsfeedhelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		User.printtitle(Prompt.Msgs[377]);
		User.move(1,2);
		User.sends(Prompt.Msgs[383]);
		User.sends(Prompt.Msgs[384]);
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
		User.sends(Prompt.Msgs[385]);
		switch(User.MakeSure())
		{
		case 'A':
			OtherFunction((int)'a');
			break;
		default:
			break;
		}
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		switch(OtherKey)
		{
		case 'a':
		case 'A':
			try
			{
				byte themode=3;
				StringBuffer Ans=new StringBuffer();
				String theboardname=null,thebbsname=null,thenewsgroup=null;
				
				User.Clear();
				User.move(1,2);
				User.sends(Prompt.Msgs[398]);
				(User.GetData=new LineEdit(Ans,Consts.NameLen-1,pid,true)).DoEdit();
				User.GetData=null;
				if(Ans.length()==0)
					return true;
				theboardname=Ans.toString();

				Ans.setLength(0);
				User.sends(Prompt.Msgs[389]);
				(User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit();
				User.GetData=null;
				thebbsname=Ans.toString();

				Ans.setLength(0);
				User.sends(Prompt.Msgs[399]);
				(User.GetData=new LineEdit(Ans,Consts.StrLen-1,pid,true)).DoEdit();
				User.GetData=null;
				thenewsgroup=Ans.toString();
				
				User.sends(Prompt.Msgs[402]);
				switch(User.MakeSure())
				{
				case '0':
					themode=0;
					break;
				case '1':
					themode=1;
					break;
				case '2':
					themode=2;
					break;
				default:
					themode=3;
					break;
				}

				RecordHandler.append(lockObj,new NewsFeedType(theboardname,thebbsname,thenewsgroup,themode),CurrentPath,MyDir);
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
				byte themode=3;
				//					int themax=-1,themin=-1;
				StringBuffer Ans=null;
				//					String theboardname=null,thebbsname=null,thenewsgroup=null;

				NewsFeedType nft=new NewsFeedType();
				RecordHandler.getRecord(now+cursor,lockObj,nft,CurrentPath,MyDir);
				
				User.Clear();
				User.move(1,2);
				Ans=new StringBuffer(nft.getBoardName());
				User.sends(Prompt.Msgs[398]);
				(User.GetData=new LineEdit(Ans,Consts.NameLen,pid,true)).DoEdit();
				User.GetData=null;
				if(Ans.length()==0)
					return true;
				nft.setBoardName(Ans.toString());
				//					theboardname=Ans.toString();

				Ans=new StringBuffer(nft.getBBSName());
				User.sends(Prompt.Msgs[389]);
				(User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit();
				User.GetData=null;
				nft.setBBSName(Ans.toString());
				//					thebbsname=Ans.toString();
				
				Ans=new StringBuffer(nft.getNewsGroup());
				User.sends(Prompt.Msgs[399]);
				(User.GetData=new LineEdit(Ans,Consts.StrLen-1,pid,true)).DoEdit();
				User.GetData=null;
				nft.setNewsGroup(Ans.toString());
				//					thenewsgroup=Ans.toString();

				Ans=new StringBuffer(""+nft.getMax());
				while(true)
				{
					User.move(1,6);
					User.sends(Prompt.Msgs[400]);
					if((User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit()<0)
						return true;
					User.GetData=null;
					try
					{
						int temp=Integer.parseInt(Ans.toString());
						nft.setMax(temp);
						//							themax=temp;
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
				
				Ans=new StringBuffer(""+nft.getMin());
				while(true)
				{
					User.move(1,7);
					User.sends(Prompt.Msgs[401]);
					if((User.GetData=new LineEdit(Ans,10,pid,true)).DoEdit()<0)
						return true;
					User.GetData=null;
					try
					{
						int temp=Integer.parseInt(Ans.toString());
						nft.setMin(temp);
						//							themin=temp;
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
				
				User.sends(Prompt.Msgs[402]);
				switch(User.MakeSure())
				{
				case '0':
					themode=0;
					break;
				case '1':
					themode=1;
					break;
				case '2':
					themode=2;
					break;
				case '3':
					themode=3;
					break;
				default:
					themode=nft.getMode();
					break;
				}
				nft.setMode(themode);
				
				RecordHandler.update(now+cursor,lockObj,nft,CurrentPath,MyDir);
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
		return " ";
	}
	
	protected void exitList()
	{
	}
}