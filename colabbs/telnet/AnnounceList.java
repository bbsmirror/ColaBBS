package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;

public final class AnnounceList extends ViewFileList
{
	private int Level=0,Mode=0;
	private String DefaultTitle=null;

	public AnnounceList(String PathBuf,int pidbuf,int levelbuf,int modebuf)
	{
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[361];
		CleanStr="[m  ";
		MyDir=Consts.DotDir;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		
		Level=levelbuf;
		Mode=modebuf;
		OldMode=ColaServer.BBSUsers[pid].usermode;
		BBS.SetUserMode(pid,Modes.AnnounceList);

		User.Clear();

		DrawTitle();
		rt=new AnnounceType();
		lockObj=Consts.AnnounceLock; //maybe lock others better?
		CurrentPath=PathBuf;
	}
	
	public AnnounceList(String TitleBuf,String PathBuf,int pidbuf,int levelbuf,int modebuf)
	{
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[361];
		CleanStr="[m  ";
		MyDir=Consts.DotDir;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		
		Level=levelbuf;
		Mode=modebuf;
		DefaultTitle=TitleBuf;
		OldMode=ColaServer.BBSUsers[pid].usermode;
		BBS.SetUserMode(pid,Modes.AnnounceList);

		User.Clear();

		DrawTitle();
		rt=new AnnounceType();
		lockObj=Consts.AnnounceLock; //maybe lock others better?
		CurrentPath=PathBuf;
	}

	public static synchronized boolean additem(Object theObj,File Source,String TargetPath,String OwnBuf,String Title)
	{
		RandomAccessFile Target=null,SourceFile=null;
		String SaveFileName;
		byte buf[];

		try
		{
			if(Source!=null)
			{
				char appendChar='A';
				do
				{
					SaveFileName="M."+((int)((new Date()).getTime()/1000))+"."+appendChar;
					appendChar++;
				}while((new File(TargetPath,SaveFileName)).exists());
				SourceFile=new RandomAccessFile(Source,"r");

				Target=new RandomAccessFile(TargetPath+SaveFileName,"rw");
				buf=new byte[(int)SourceFile.length()];
				SourceFile.read(buf);
				Target.write(buf);

				System.gc();
			}
			else
			{
				char appendChar='A';
				do
				{
					SaveFileName="D."+((int)((new Date()).getTime()/1000))+"."+appendChar;
					appendChar++;
				}while((new File(TargetPath+SaveFileName)).exists());
			}
			RecordHandler.append(theObj,new AnnounceType(SaveFileName,OwnBuf,Title),TargetPath,Consts.DotDir);
		}	
		catch(IOException e)
		{
			e.printStackTrace();
		}	
		finally
		{
			try
			{
				if(Target!=null)
				{
					Target.close();
					Target=null;
				}	
				if(SourceFile!=null)
				{
					SourceFile.close();
					SourceFile=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}	
		return true;
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
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"announcereadhelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		if(DefaultTitle!=null)
			User.printtitle(DefaultTitle,Prompt.Msgs[365]);
		else
			User.printtitle(Prompt.Msgs[365]);
		User.move(1,2);
		User.sends(Prompt.Msgs[363]);
		User.sends(Prompt.Msgs[364]);
	}

	protected void enterFunction()
	{
		int InKey=0;
		
		try
		{
			while(true)
			{
				User.Clear();

				if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					break;

				ViewFileType vft=(ViewFileType)rt;
				
				vft.LastTitle=vft.getTitle();
				if(vft.LastTitle!=null&&vft.LastTitle.length()>3&&vft.LastTitle.substring(0,4).equals("Re: "))
					vft.LastTitle=vft.LastTitle.substring(4);

				if(rt.deleteBody().charAt(0)=='M')
				{
					if((new File(CurrentPath,rt.deleteBody())).exists())
						User.ansimore(CurrentPath+rt.deleteBody());
					else
					{
						DelRecord(now+cursor);
						lastnow=-1;
						break;
					}
				}
				else if(rt.deleteBody().charAt(0)=='L')
				{
					if((new File(ColaServer.INI.BBSHome+rt.deleteBody())).exists())
					{
						User.Clear();
						(new AnnounceList(vft.getTitle(),ColaServer.INI.BBSHome+rt.deleteBody()+File.separator, pid,Level, Mode)).DoList();
					}	
					else
					{
						DelRecord(now+cursor);
					}
					lastnow=-1;
					break;
				}
				else
				{
					if((new File(CurrentPath+rt.deleteBody()+File.separator)).exists())
					{
						User.Clear();
						(new AnnounceList(vft.getTitle(),CurrentPath+rt.deleteBody()+File.separator, pid,Level, Mode)).DoList();
					}	
					else
					{
						DelRecord(now+cursor);
					}
					lastnow=-1;
					break;
				}

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
				case '[':
					if(ThreadLast()<0)
						return;
					break;
				case Keys.Down:
				case Keys.Space:
					/*					if(cursor<MaxRow-1&&(now+cursor)<ListMax)
					{
					cursor++;
					if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					return;
					break;
					}
					cursor=0; //¤S¬G·N¤£break*/
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
				case ']':
				case ((int)'x'-(int)'a'+1):
				case 'p':
					if(ThreadNext()<0)
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
		if((Level&Perm.BM)!=0||(Level&Perm.SYSOP)!=0)
			return true;
		return false;
	}
	
	protected boolean HasRangeDelPerm()
	{
		if((Level&Perm.BM)!=0||(Level&Perm.SYSOP)!=0)
			return true;
		return false;
	}
	
	protected void NoDir()
	{
		ListMax=0;
		if(now+cursor>ListMax)
		{
			cursor=ListMax%MaxRow;
			now=ListMax-cursor;
		}
		User.move(1,4);
		User.sends(Prompt.Msgs[360]);
		int InKey=0;

		while(true)
		{
			switch((InKey=ReadKey(true)))
			{
				//Remark by WilliamWey
				/*case 'f':
				OtherFunction((int)'f');
				break;
				case 'g':
				OtherFunction((int)'g');
				return;*/
				//Add by WilliamWey
			case 'f':
				OtherFunction((int)InKey);
				break;
			case 'i':
			case 'a':
			case 'n':	
			case 'g':
			case 'p':
				if (OtherFunction((int)InKey))
					return;
				break;
				//
			case 'h':
				DoHelp();
				User.PressAnyKey();
				User.Clear();
				DrawTitle();
				User.move(1,4);
				User.sends(Prompt.Msgs[360]);
				break;
			case Keys.Left:
			case 0:
			case -1:
				return;
			}
		}
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		StringBuffer Title=new StringBuffer(),Ans;
		RandomAccessFile /*DirFile=null,*/OrgFile=null,AppendFile=null;
		
		switch(OtherKey)
		{
		case 'f':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[372]);
			if(User.MakeSure()=='N')
			{
				User.printendline();
				break;
			}
			ColaServer.BBSUsers[pid].SilkRoad=new String(CurrentPath);
			break;
			//Changed by WilliamWey
			//À³¸Ó¬O i ¥\¯à§a, ¤£¹L¦n¹³¼È¦sÁÙ¨S¦³¼g¦n, ©Ò¥H¤£¯à°÷´ú
			//case 'a':	
		case 'i':
			//
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(User.CopyPad==null)
			{
				User.sends(Prompt.Msgs[370]);
				break;
			}
			try
			{
				if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir))
					break;
				
				OrgFile=new RandomAccessFile(User.CopyPad,"r");
				byte appendbuf[]=new byte[(int)OrgFile.length()];
				
				OrgFile.read(appendbuf);

				AppendFile=new RandomAccessFile(CurrentPath+rt.deleteBody(),"rw");
				AppendFile.seek(AppendFile.length());
				AppendFile.write(appendbuf);
			}	
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(OrgFile!=null)
					{
						OrgFile.close();
						OrgFile=null;
					}	
					if(AppendFile!=null)
					{
						AppendFile.close();
						AppendFile=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			return true;
		case 'p':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(User.CopyPad==null)
			{
				User.sends(Prompt.Msgs[370]);
				break;
			}
			if(User.CopyTitle==null)
				User.CopyTitle=Prompt.Msgs[262];
			AnnounceList.additem(lockObj,User.CopyPad,CurrentPath,User.UFD.ID,User.CopyTitle);
			return true;
		case 'c':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir))
				break;
			User.CopyTitle=((ViewFileType)rt).getTitle();
			User.CopyPad=new File(CurrentPath,rt.deleteBody());
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[371]);
			break;
		case 'm':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			else
			{
				int GotoNum,NowNum=now+cursor;
				StringBuffer NumBuf=new StringBuffer();

				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[367]);
				(User.GetData=new LineEdit(NumBuf,5, pid, true)).DoEdit();
				User.GetData=null;
				try
				{
					GotoNum=Integer.parseInt(NumBuf.toString());
					GotoNum--;
					if(GotoNum>ListMax)
						GotoNum=ListMax;
					if(GotoNum<0)
						GotoNum=0;
				}
				catch(NumberFormatException e)
				{
					User.Bell();
					User.move(1,24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[16]);
					TIME.Delay(1000);
					break;
				}
				User.printendline();
				if(NowNum==GotoNum)
					break;
				RecordHandler.moveRecord(lockObj,rt,CurrentPath,Consts.DotDir,NowNum,GotoNum);
			}
			DrawTitle();
			return true;
		case 'g':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			else
			{
				String SaveFileName;
				
				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[30]);
				(User.GetData=new LineEdit(Title,50, pid, true)).DoEdit();
				User.GetData=null;
				if(Title.length()==0)
				{
					User.printendline();
					break;
				}

				char appendChar='A';
				do
				{
					SaveFileName="D."+((int)((new Date()).getTime()/1000))+"."+appendChar;
					appendChar++;
				}while((new File(CurrentPath,SaveFileName)).exists());
				
				(new File(CurrentPath+SaveFileName)).mkdirs();
				if(SaveFileName!=null)
				{
					ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" "+User.UFD.ID+" add directory to announce "+Title.toString());
					RecordHandler.append(lockObj,new AnnounceType(SaveFileName,User.UFD.ID,Title.toString()),CurrentPath,Consts.DotDir);
				}
			}
			DrawTitle();
			return true;
		case 't':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir))
				break;
			else
			{
				Title=new StringBuffer(((ViewFileType)rt).getTitle());

				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[30]);
				(User.GetData=new LineEdit(Title,50, pid,true)).DoEdit();
				User.GetData=null;

				if(Title.length()!=0)
				{
					((ViewFileType)rt).setTitle(Title.toString());
					RecordHandler.update(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir);
					User.move(1,cursor+4);
					User.clrtoeol();
					User.sends("  "+STRING.CutLeft(""+(now+cursor+1),4)+recordTag(rt)+rt.getRecordString()+"[m\r\n");
				}
				User.sends(Prompt.Msgs[366]);
				TIME.Delay(500);
				User.printendline();
			}
			break;
		case 'e':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir))
				break;
			else
			{
				if(rt.deleteBody().charAt(0)=='D')
					break;
				Title=new StringBuffer(((ViewFileType)rt).getTitle());

				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[30]);
				(User.GetData=new LineEdit(Title,50, pid,true)).DoEdit();
				User.GetData=null;
				if(Title.length()==0)
				{
					User.printendline();
					break;
				}
				String SaveFileName=(User.CurrentEditor = new Editor(pid)).DoEdit(Title.toString(),new File(CurrentPath,rt.deleteBody()),CurrentPath+rt.deleteBody(),((AnnounceType)rt).getPoster(),'E',(byte)0);
				User.CurrentEditor=null;
				System.gc();
				if(SaveFileName!=null)
				{
					ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" "+User.UFD.ID+" changed "+((AnnounceType)rt).getTitle());
					((AnnounceType)rt).setTitle(Title.toString());
					RecordHandler.update(now+cursor,lockObj,rt,CurrentPath,Consts.DotDir);
					User.printendline();
				}
			}
			DrawTitle();
			return true;
		case 'a':
		case 'n':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			else
			{
				String SaveFileName;

				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[30]);
				(User.GetData=new LineEdit(Title,50,pid, true)).DoEdit();
				User.GetData=null;
				if(Title.length()==0)
				{
					User.printendline();
					break;
				}

				SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(Title.toString(),CurrentPath,(byte)0);
				User.CurrentEditor=null;
				System.gc();
				if(SaveFileName!=null)
				{
					ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" "+User.UFD.ID+" add file to announce "+Title.toString());
					RecordHandler.append(lockObj,new AnnounceType(SaveFileName,User.UFD.ID,Title.toString()),CurrentPath,Consts.DotDir);
				}
			}
			DrawTitle();
			return true;
		case '[':
			ThreadLast();
			break;
		case ']':
			ThreadNext();
			break;
		case '=':
			while(ThreadLast()==0);
			break;
			//Add by WilliamWey for Search for Title, Author
		case '?': //Search Title Bottom-Up
			if (SearchTitleLast() != 0)
			{
				User.move(1, 24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[471]);
				User.getch();
			}
			User.printendline();
			break;
		case '/': //Search Title Top-Down
			if (SearchTitleNext() != 0)
			{
				User.move(1, 24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[471]);
				User.getch();
			}
			User.printendline();
			break;
			/*			case 'A': //Search Author Bottom-Up
			if (SearchAuthorLast() != 0)
			{
			((TelnetUser)ColaServer.BBSUsers[pid]).move(1, 24);
			((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((TelnetUser)ColaServer.BBSUsers[pid]).sends(Prompt.Msgs[471]);
			((TelnetUser)ColaServer.BBSUsers[pid]).getch();
			}
			((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			break;
			case 'a': //Search Author Top-Down
			if (SearchAuthorNext() != 0)
			{
			((TelnetUser)ColaServer.BBSUsers[pid]).move(1, 24);
			((TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((TelnetUser)ColaServer.BBSUsers[pid]).sends(Prompt.Msgs[471]);
			((TelnetUser)ColaServer.BBSUsers[pid]).getch();
			}
			((TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			break;*/
			//
		case Keys.Enter:
		case Keys.Right:
			enterFunction();
			break;
		default:
			break;
		}
		//Changed by WilliamWey
		return false;
		//return true;
		//
	}
	protected int ReadEnd(RecordType t)
	{
		User.move(1,24);
		User.clrtoeol();
		User.PressAnyKey();
		return -1;
	}

	protected String recordTag(RecordType t)
	{
		return "";
	}
}