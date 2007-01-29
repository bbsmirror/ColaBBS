package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;

public final class MailList extends ViewFileList
{
	public MailList(int pidbuf)
	{
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[58];
		CleanStr="[m  ";
		MyDir=Consts.DotDir;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		OldMode=User.usermode;
		BBS.SetUserMode(pid,Modes.MailList);

		DrawTitle();
		lockObj=User.UserPassItem;
		//		lockObj=ColaServer.UserPassWords.get(User.UFD.ID.toUpperCase());
		rt=new MailType();
		//		ItemSize=256;
		//«H¥ó¦Cªí¨ÃÁÙ¨S¦³¬ö¿ý¥Ø«e´å¼Ð¦ì¸m
		ListMax = -1;
		CurrentPath=ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator;
		//        System.out.println(CurrentPath+Consts.DotDir);
	}
	
	protected void DelRecord(int index)
	{
		DelRecordRange(index,index);
	}
	
	protected void DelRecordRange(int start,int end)
	{
		RecordHandler.deleteRange(lockObj,rt,CurrentPath,MyDir,start,end);
		/*		int i,tries;
		byte readbuf[]=new byte[ItemSize];
		File DelFile,DirFile=new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,Consts.DotDir),LockFile=new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,Consts.Deleted);
		RandomAccessFile Source=null;
		RandomAccessFile Target=null;
		
		tries=0;
		while(tries<10&&DirFile.exists()&&LockFile.exists()&&!LockFile.delete())
		{
		Utils.Delay(100);
		tries++;
		}	
		
		tries=0;
		while(tries<10&&DirFile.exists()&&!DirFile.renameTo(LockFile))
		{
		Utils.Delay(100);
		tries++;
		}	
		try
		{
		Source=new RandomAccessFile(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator+Consts.Deleted,"r");
		Target=new RandomAccessFile(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator+Consts.DotDir,"rw");

		for(i=0;i<(int)(Source.length()/ItemSize);i++)
		{
		if(i<start||i>end)
		{
		Source.read(readbuf);
		Target.write(readbuf);
		}
		else
		{
		Source.read(readbuf);
		if((readbuf[244]&Consts.FileLink)==0)
		{
		String FNBuf=new String(readbuf,0,0,Consts.StrLen-2);

		if(FNBuf.indexOf(0)!=-1)
		FNBuf=FNBuf.substring(0,FNBuf.indexOf(0));

		DelFile=new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,FNBuf);
		
		tries=0;
		while(tries<10&&DelFile.exists()&&!DelFile.delete())
		{
		Utils.Delay(100);
		tries++;
		}	
		}
		}
		}
		}
		catch(IOException e)
		{
		e.printStackTrace();
		//			ColaServer.BBSlog.println("Exception #117"+e);
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
		if(Source!=null)
		{
		Source.close();
		Source=null;
		}	
		}
		catch(IOException e)
		{
		e.printStackTrace();
		//				ColaServer.BBSlog.println("Closing file Exception #103"+e);
		}
		}
		tries=0;
		while(tries<10&&LockFile.exists()&&!LockFile.delete())
		{
		Utils.Delay(100);
		tries++;
		}	*/
	}
	
	protected void DoHelp()
	{
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"mailreadhelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		User.printtitle(Prompt.Msgs[69]);
		User.move(1,2);
		User.sends(Prompt.Msgs[70]);
		User.sends(Prompt.Msgs[71]);
		//        User.sends("[1;44m  ½s¸¹  ¥Z µn ªÌ     ¤é    ´Á  ¼Ð  ÃD                                 [¤@¯ë¼Ò¦¡][m\r\n",pid);
	}
	
	protected boolean HasDelPerm(RecordType t)
	{
		return true;
	}
	
	protected boolean HasRangeDelPerm()
	{
		return true;
	}
	
	protected void NoDir()
	{
		User.move(1,4);
		User.sends(Prompt.Msgs[59]);
		User.PressAnyKey();
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		switch(OtherKey)
		{
		case '[':
			ThreadLast();
			break;
		case ']':
			ThreadNext();
			break;
		case 'm':
			if(!HasRangeDelPerm())
				break;
			synchronized(lockObj)
			{
				RecordHandler rh=null;
				
				try
				{
					rh=new RecordHandler(CurrentPath,MyDir);

					if(!rh.getRecord(now+cursor,rt))
						break;
					
					if(!((ViewFileType)rt).isLink())
					{
						((MailType)rt).invertMark();
						rh.update(now+cursor,rt);
					}

					User.move(1,cursor+4);
					User.clrtoeol();
					User.sends("  "+STRING.CutLeft(""+(now+cursor+1),4)+recordTag(rt)+rt.getRecordString()+"[0m\r\n");
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
		case 'A': //Search Author Bottom-Up
			if (SearchAuthorLast() != 0)
			{
				User.move(1, 24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[471]);
				User.getch();
			}
			User.printendline();
			break;
		case 'a': //Search Author Top-Down
			if (SearchAuthorNext() != 0)
			{
				User.move(1, 24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[471]);
				User.getch();
			}
			User.printendline();
			break;
			//
			
		case Keys.Enter:
		case Keys.Right:
			enterFunction();
			break;
			//Add by WilliamWey
			/*case 'F':
			if ((User.UFD.Perm & Perm.LoginOK) == 0)
			break;
			if(!RecordHandler.getRecord(now + cursor, lockObj, rt, CurrentPath, MyDir))
			break;
			
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[17],pid);
			
			String ToWhere;
			StringBuffer T = new StringBuffer();
			
			switch (User.MakeSure())
			{
			case 'Y':
			User.printendline();
			ToWhere = User.UFD.Email;
			break;
			case 'N':
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[30],pid);
			(User.GetData=new LineEdit(T, 50, pid, true)).DoEdit();
			User.GetData=null;	
			if(T.length()!=0)
			{
			ToWhere = T.toString();
			User.printendline();
			}
			else
			{
			User.move(1,24);
			User.clrtoeol();						
			User.sends(Prompt.Msgs[30],pid);
			return false;
			}
			break;
			default:
			User.printendline();
			return false;
			}				

			synchronized(lockObj)
			{
			RecordHandler rh=null;
			rh = new RecordHandler(new MailType(), CurrentPath, MyDir);

			MailType mt = new MailType();
			if(!rh.getRecord(now + cursor, mt))
			break;

			User.MailFile(ToWhere, CurrentPath, ((ViewFileType)mt).deleteBody(), mt.getTitle(), User.UFD.ID, true);
			}					
			break;
			//*/
		default:
			break;
		}
		return false;
	}
	
	protected int ReadEnd(RecordType t)
	{
		int InKey;

		((MailType)t).setRead(true);
		RecordHandler.update(now+cursor,lockObj,t,CurrentPath,Consts.DotDir);

		User.move(1,24);
		
		User.sends(Prompt.Msgs[60]);
		InKey=User.getch();
		switch(InKey)
		{
		case -1:
			return -1;
		case 'r':
		case 'R':
			boolean InternetMailFlag=false;
			char QuoteMode='Y';
			String OwnFullBuf=((MailType)t).getSender();
			String OwnBuf=new String(OwnFullBuf);
			String Title=((ViewFileType)t).getTitle();
			if(Title.length()<4||(Title.length()>3&&!Title.substring(0,4).equals("Re: ")))
				Title="Re: "+Title;
			StringBuffer TitleBuf=new StringBuffer(Title);
			StringBuffer Ans=new StringBuffer();
			String SaveFileName,ModeBuf;

			if(!BBS.HasOnePerm(pid,Perm.Post))
				break;
			ModeBuf=User.usermode;
			BBS.SetUserMode(pid,Modes.Mail);

			if(OwnFullBuf.indexOf('@')!=-1)
			{
				if(!ColaServer.INI.InternetMail)
					return -1;
				InternetMailFlag=true;
			}
			else
			{
				if(OwnFullBuf.indexOf((int)' ')!=-1)
					OwnBuf=OwnFullBuf.substring(0,OwnFullBuf.indexOf((int)' '));
				if(OwnFullBuf.indexOf(0)!=-1)
					OwnBuf=OwnFullBuf.substring(0,OwnFullBuf.indexOf(0));
			}
			User.Clear();
			while(true)
			{
				Ans.setLength(0);
				User.move(1,20);
				User.clrtoeol();
				User.sends(Prompt.Msgs[61]+OwnBuf+"[m\r\n");
				User.clrtoeol();
				if(TitleBuf.length()!=0)
					User.sends(Prompt.Msgs[24]+TitleBuf.toString()+"[m\r\n");
				else
					User.sends(Prompt.Msgs[25]);
				User.clrtoeol();
				User.sends(Prompt.Msgs[26]+User.SigNum+Prompt.Msgs[160]);
				if(TitleBuf.length()==0)
				{
					User.clrtoeol();
					User.sends(Prompt.Msgs[30]);
					if((User.GetData=new LineEdit(TitleBuf,50,pid,true)).DoEdit()<0)
						return -1;
					User.GetData=null;
					continue;
				}
				User.clrtoeol();
				User.sends(Prompt.Msgs[31]);
				User.sends(Prompt.Msgs[34]+User.UFD.signature+Prompt.Msgs[68]+QuoteMode+"[m]: ");
				if((User.GetData=new LineEdit(Ans,2,pid,true)).DoEdit()<0)
					return -1;
				User.GetData=null;
				if(Ans.length()==0)
					break;
				if(Ans.charAt(0)>='0'&&Ans.charAt(0)<='9')
				{
					try
					{
						User.UFD.signature=(byte)Integer.parseInt(Ans.toString());
					}
					catch(NumberFormatException e)
					{
						User.Bell();
					}
					finally
					{
						if(User.UFD.signature>User.SigNum)
							User.UFD.signature=0;
						continue;
					}
				}
				switch(Ans.charAt(0))
				{
				case 't':
				case 'T':
					TitleBuf.setLength(0);
					break;
				case 'y':
				case 'Y':
					QuoteMode='Y';
					break;
				case 'n':
				case 'N':
					QuoteMode='N';
					break;
				case 'r':
				case 'R':
					QuoteMode='R';
					break;
				case 'a':
				case 'A':
					QuoteMode='A';
					break;
				}
			}
			if(QuoteMode!='N')
				//SaveFileName=(User.CurrentEditor=new Editor(TitleBuf.toString(),new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,t.deleteBody()),ColaServer.INI.BBSHome+"mailtemp"+File.separator,OwnFullBuf,pid,QuoteMode,User.UFD.signature)).DoEdit();
				SaveFileName=(User.CurrentEditor = new Editor(pid)).DoEdit(TitleBuf.toString(),new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(User.UFD.ID.charAt(0))+File.separator+User.UFD.ID+File.separator,t.deleteBody()),ColaServer.INI.BBSHome+"mailtemp"+File.separator,OwnFullBuf,QuoteMode,User.UFD.signature);
			else
				SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(TitleBuf.toString(),ColaServer.INI.BBSHome+"mailtemp"+File.separator,User.UFD.signature);
			User.CurrentEditor=null;
			System.gc();
			if(SaveFileName!=null)
			{
				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[434]);
				User.flush();
				User.MailFile(OwnBuf,ColaServer.INI.BBSHome+"mailtemp"+File.separator,SaveFileName,TitleBuf.toString(),User.UFD.ID,false);
			}
			BBS.SetUserMode(pid,ModeBuf);

			break;
		default:
			return InKey;
		}
		return -1;
	}

	protected String recordTag(RecordType t)
	{
		return "";
	}
}