package colabbs.telnet;

import java.io.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.USERFILEDATA.*;

public class FriendList extends DirList
{
	private int MaxDefine=32;
	
	public FriendList(String ListPathBuf,String ListFileBuf,int MaxDefBuf,int pidbuf)
	{
		CurrentPath=ListPathBuf;
		MyDir=ListFileBuf;
		
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[337];
		CleanStr="[m  ";
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		OldMode=User.usermode;
		rt=new FriendType();
		lockObj=User.UserPassItem.IDItem;
		MaxDefine=MaxDefBuf;
		
		BBS.SetUserMode(pid,Modes.SetFriend);
		DrawTitle();
	}
	
	private void AddFriend()
	{
		String UserID;
		
		UserID=User.NameComplete(Prompt.Msgs[343]);
		if(UserID.length()==0||isFriend(UserID))
			return;
		if(!ColaServer.UFDList.exist(UserID))
			return;
		else
			UserID=ColaServer.UFDList.getPass(UserID).IDItem;
		User.move(1,2);
		AddFriend(CurrentPath,MyDir,User.UFD.ID,UserID,pid);
	}
	
	public static String AddFriend(String AddPath,String AddFile,String MyID,String HisID,int pid)
	{
		StringBuffer FriendNote=new StringBuffer();
		ColaServer.BBSUsers[pid].sends(Prompt.Msgs[344]);
		
		(((TelnetUser)ColaServer.BBSUsers[pid]).GetData=new LineEdit(FriendNote,15,pid,true)).DoEdit();
		((TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
		RecordHandler.append(ColaServer.BBSUsers[pid].UserPassItem.IDItem,new FriendType(HisID,FriendNote.toString()),AddPath,AddFile);
		RecordHandler.append(ColaServer.UFDList.getPass(HisID).IDItem,new MyFriendType(MyID),ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(HisID.charAt(0))+File.separator+HisID,"My"+AddFile);
		
		return FriendNote.toString();
	}
	
	public static void DelFriend(String AddFile,String MyID,String HisID,boolean ifrmme)
	{
		RecordHandler.delete(ColaServer.UFDList.getPass(HisID).IDItem,new FriendType(HisID,null),ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(MyID.charAt(0))+File.separator+MyID,AddFile);
		if(ifrmme)
			removeme(AddFile,MyID,HisID);
	}
	
	protected void DelRecord(int index)
	{
		DelRecordRange(index,index);
	}
	
	protected void DelRecordRange(int start,int end)
	{
		int delnum=-1;
		
		synchronized(lockObj)
		{
			RecordHandler rh=null;
			try
			{
				rh=new RecordHandler(CurrentPath,MyDir);
				rh.setIndex(start,rt);
				for(int i=start;i<=end;i++)
				{
					rh.nextElement(rt);
					removeme(MyDir,User.UFD.ID,rt.deleteBody());
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
		RecordHandler.deleteRange(lockObj,rt,CurrentPath,MyDir,start,end);
	}
	
	protected void DoHelp()
	{
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"friendshelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		User.printtitle(Prompt.Msgs[338]);
		User.move(1,2);
		User.sends(Prompt.Msgs[339]);
		User.sends(Prompt.Msgs[340]);
	}

	protected void enterFunction()
	{
		int InKey=0;
		
		try
		{
			while(true)
			{
				String QueryID;
				if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					break;

				User.Clear();

				User.printtitle(Prompt.Msgs[387],false);
				User.DoQuery(rt.deleteBody());

				InKey=ReadEnd(rt);
				switch(InKey)
				{
				case Keys.Up:
					cursor--;
					if(cursor<0)
					{
						now-=MaxRow;
						if(now<0)
						{
							now=0;
							cursor=0;
							return;
						}
						else
							cursor=MaxRow-1;
					}
					if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
						return;
					break;
				case Keys.Down:
				case Keys.Space:
					cursor++;
					if(cursor>=MaxRow)
					{
						now+=MaxRow;
						if(now>ListMax)
						{
							cursor=ListMax%MaxRow;
							now=ListMax-cursor;
						}
						else
							cursor=0;
					}
					else if((now+cursor)>ListMax)
					{
						cursor=ListMax%MaxRow;
						now=ListMax-cursor;
						return;
					}
					if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
						return;
					break;
				case 'q':
				case 'Q':
				case Keys.Left:
				default:
					return;
				}
			}
		}
		finally
		{
			DrawTitle();
			lastnow=-1;
		}
	}

	protected boolean HasDelPerm(RecordType t)
	{
		return true;
	}

	protected boolean HasRangeDelPerm()
	{
		return true;
	}
	public static boolean isFriend(Object theObj,String MyID,String HisID)
	{
		int delnum=-1;
		MyFriendType t=new MyFriendType();
		
		synchronized(theObj)
		{
			RecordHandler rh=null;
			try
			{
				rh=new RecordHandler(ColaServer.INI.BBSHome+File.separator+"home"+File.separator+Character.toUpperCase(MyID.charAt(0))+File.separator+MyID,"Myfriends");
				int len=(int)rh.recordNumber(t);
				for(int i=0;i<=len;i++)
				{
					rh.nextElement(t);
					if(t.getRecordString().equalsIgnoreCase(HisID))
					{
						delnum=i;
						return true;
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
		return false;
	}
	
	protected boolean isFriend(String UserID)
	{
		int delnum=-1;
		
		synchronized(lockObj)
		{
			RecordHandler rh=null;
			try
			{
				rh=new RecordHandler(CurrentPath,MyDir);
				int len=(int)rh.recordNumber(rt);
				for(int i=0;i<=len;i++)
				{
					rh.nextElement(rt);
					if(rt.deleteBody().equalsIgnoreCase(UserID))
					{
						delnum=i;
						return true;
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
		return false;
	}
	
	protected void NoDir()
	{
		User.move(1,4);
		User.sends(Prompt.Msgs[341]);
		
		if(User.MakeSure()=='A')
			AddFriend();
		ReDraw();
		cursor=0;
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		switch(OtherKey)
		{
		case 'm':
		case 'M':
			if(!BBS.HasOnePerm(pid,Perm.Post))
				break;
			else
			{
				RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir);
				User.DoMail(rt.deleteBody());
				DrawTitle();
				lastnow=-1;
			}
			break;
		case 'a':
		case 'A':
			if(ListMax>=MaxDefine)
			{
				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[345]);
				TIME.Delay(1000);
				User.printendline();
				break;
			}
			AddFriend();
			User.Clear();
			DrawTitle();
			lastnow=-1;
			break;
		case 'h':
			DoHelp();
			User.PressAnyKey();
			User.Clear();
			DrawTitle();
			lastnow=-1;
			break;
		case Keys.Enter:
		case Keys.Right:
			enterFunction();
			break;
		default:
			break;
		}
		return false;
	}
	protected int ReadEnd(RecordType t)
	{
		int InKey;
		
		User.move(1,24);
		User.sends(Prompt.Msgs[342]);
		InKey=User.getch();
		switch(InKey)
		{
		case -1:
			return -1;
		default:
			return InKey;
		}
	}

	protected String recordTag(RecordType t)
	{
		return " ";
	}
	
	public void ReDraw()
	{
		DrawTitle();
		lastnow=-1;
	}
	
	public static void removeme(String AddFile,String MyID,String HisID)
	{
		PassItem pi=ColaServer.UFDList.getPass(HisID);
		if(pi==null)
			return;
		RecordHandler.delete(pi.IDItem,new MyFriendType(MyID),ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(HisID.charAt(0))+File.separator+HisID,"My"+AddFile);
	}
	
	public static void removemeAll(String FileName,String UserID)
	{
		
		Object theObj=ColaServer.UFDList.getPass(UserID);
		if(theObj==null)
			return;
		synchronized(theObj)
		{
			RecordHandler rh=null;
			try
			{
				rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UserID.charAt(0))+File.separator+UserID,"My"+FileName);
				MyFriendType mft=new MyFriendType();
				int len=(int)rh.recordNumber(mft);
				if(len<0)
					return;
				for(int i=0;i<=len;i++)
				{
					rh.nextElement(mft);
					if(!mft.deleteBody().equals(UserID))
						removeme(FileName,UserID,mft.deleteBody());
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
	
	protected void exitList()
	{
	}
}