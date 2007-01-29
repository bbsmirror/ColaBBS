package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.VOTE.*;
import colabbs.DATA.BOARDRC.*;

import colabbs.telnet.DATA.TBOARDRC.*;

public final class PostList extends ViewFileList
{
	private BoardItem Board;

	private BoardRCItem boardrc = null;
	private TBoardRCItem tboardrc = null;
	
	public PostList(BoardItem BoardBuf,int pidbuf)
	{
		StartX=1;
		StartY=4;
		MaxRow=20;
		EchoStr=Prompt.Msgs[57];
		CleanStr="[m  ";
		MyDir=Consts.DotDir;
		Board=BoardBuf;
		pid=pidbuf;
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		OldMode=User.usermode;
		BBS.SetUserMode(pid,Modes.PostList);

		boardrc = User.UFD.brclist.getBRC(Board);
		tboardrc = User.tbrclist.getTBRC(Board);
		ListMax = tboardrc.getCurrentPosition();
		
		//DrawTitle();
		rt=new PostType();
		lockObj=BoardBuf;
		CurrentPath=ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator;
	}

	public void Welcome()
	{
		Welcome(false);
	}
	
	//enforce ¬°¬O§_±j¨îÆ[¬Ý¶iª©µe­±, ¥Î¦b TAB Áä¤W­±.
	public void Welcome(boolean enforce)
	{	
		if (Board.hasWelcome())
		{
			if (enforce || !tboardrc.isWelcomeViewed())
			{
				File WelcomeFile = Board.getWelcomeFile();
				User.ansimore(WelcomeFile);
				User.PressAnyKey();
				tboardrc.setWelcomeViewed();
			}
		}
	}
	
	public static boolean adddigest(Object theObj,String SourceName,String TargetPath,String OwnBuf,String Title)
	{
		RandomAccessFile Target=null,SourceFile=null;
		String SaveFileName;
		byte buf[];

		SaveFileName="G"+SourceName.substring(1);
		try
		{
			SourceFile=new RandomAccessFile(TargetPath+SourceName,"r");
			Target=new RandomAccessFile(TargetPath+SaveFileName,"rw");
			buf=new byte[(int)SourceFile.length()];
			SourceFile.read(buf);
			Target.write(buf);
		}	
		catch(IOException e)
		{
			e.printStackTrace();
		}	
		finally
		{
			try
			{
				if(SourceFile!=null)
				{
					SourceFile.close();
					SourceFile=null;
				}	
				if(Target!=null)
				{
					Target.close();
					Target=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		System.gc();
		RecordHandler.append(theObj,new PostType(SaveFileName,OwnBuf,Title),TargetPath,Consts.DigestDir);
		return true;
	}
	
	/*private void AddList(String filename)
	{
		int index=0,i,addnum=0;
		long filetime=0;
		
		if(filename.lastIndexOf(".")<0)
			return;
		else
		{
			addnum=(int)(filename.charAt(filename.lastIndexOf('.')+1)-'A');
			filename=filename.substring(2,filename.lastIndexOf("."));
		}
		try
		{
			filetime=(new Long(filename)).longValue();
		}
		catch(NumberFormatException e){}
		filetime=filetime*1000+addnum;
		synchronized(User.UserPassItem.BRC)
		{
			Vector theBrc=(Vector)User.UserPassItem.BRC.get(Board.Name);
			if(theBrc==null)
			{
				theBrc=new Vector();
				User.UserPassItem.BRC.put(Board.Name,theBrc);
			}
			int brcnum=theBrc.size();
			User.UserPassItem.BRCflag=true;
			{
				for(index=0;index<brcnum;index++)
				{
					if(filetime==((Long)theBrc.elementAt(index)).longValue())
						return;
					if(filetime>((Long)theBrc.elementAt(index)).longValue())
						break;
				}
				if(index<ColaServer.INI.MaxBoardRC)
				{
					theBrc.insertElementAt(new Long(filetime),index);
					if(theBrc.size()>ColaServer.INI.MaxBoardRC)
						theBrc.setSize(ColaServer.INI.MaxBoardRC);
				}
			}
		}
	}*/
	
	protected void DelRecord(int index)
	{
		if(digest)
			return;
		DelRecordRange(index, index, true);
	}
	
	protected void DelRecordRange(int start,int end)
	{
		DelRecordRange(start, end, false);
	}
	
	protected void DelRecordRange(int start,int end, boolean EnforceDelete)
	{
		if(digest)
			return;
		if(end==ListMax)
		{
			if(start>1)
				if(RecordHandler.getRecord(start-1,lockObj,rt,CurrentPath,MyDir))
				{
					if(rt.deleteBody().lastIndexOf(".")>=0)
					{
						String filename=rt.deleteBody();
						
						int addnum=(int)(filename.charAt(filename.lastIndexOf('.')+1)-'A');
						filename=filename.substring(2,filename.lastIndexOf("."));
						Board.filetime=0;
						try
						{
							Board.filetime=(new Long(filename)).longValue();
						}
						catch(NumberFormatException e){}
						Board.filetime=Board.filetime*1000+addnum;
					}
				}
		}
		RecordHandler.deleteRange(lockObj,rt,CurrentPath,MyDir,start,end, EnforceDelete);
	}
	
	//Add by WilliamWey
	//´ú¸Õ¥Î, µ{¦¡¥»¨­¨S¦³¥Î¨ì
	protected void DelBatch(Vector dellist, boolean EnforceDelete)
	{
		RecordHandler.deleteBatchRemove(lockObj,rt,CurrentPath,MyDir,dellist, EnforceDelete);
	}
	//
	
	protected void DoHelp()
	{
		User.ansimore(ColaServer.INI.BBSHome+"help"+File.separator+"mainreadhelp");
	}
	
	protected void DrawTitle()
	{
		User.move(1,1);
		if(Board.BM==null)
			User.printtitle(Prompt.Msgs[52]);
		else if(Board.BM.length()==0)
			User.printtitle(Prompt.Msgs[52]);
		else
			User.printtitle(Prompt.Msgs[53]+Board.BM);
		User.move(1,2);
		User.sends(Prompt.Msgs[54]);
		if(digest)
			User.sends(Prompt.Msgs[374]);
		else
			User.sends(Prompt.Msgs[55]);
	}
	
	protected boolean HasDelPerm(RecordType t)
	{
		StringTokenizer BMBuf=new StringTokenizer(Board.BM);

		if(BBS.HasOnePerm(pid,Perm.OBoards))
			return true;
		while(BMBuf.hasMoreElements())
			if(User.UFD.ID.equalsIgnoreCase(BMBuf.nextToken()))
				return true;
		if(User.UFD.ID.equalsIgnoreCase(((PostType)t).getPoster()))
		{
			PostType pt = (PostType)t;
			return !(pt.isDigest() || pt.isMark());
		}
		return false;
	}
	
	protected boolean HasRangeDelPerm()
	{
		StringTokenizer BMBuf=new StringTokenizer(Board.BM);

		if(BBS.HasOnePerm(pid,Perm.OBoards))
			return true;
		while(BMBuf.hasMoreElements())
			if(User.UFD.ID.equalsIgnoreCase(BMBuf.nextToken())&&BBS.HasOnePerm(pid,Perm.BM))
				return true;
		return false;
	}
	
	/*private boolean New(String filename)
	{
		int index=0,addnum=0;
		long filetime=0;

		if(filename.lastIndexOf(".")<0)
			return false;
		else
		{
			addnum=(int)(filename.charAt(filename.lastIndexOf('.')+1)-'A');
			filename=filename.substring(2,filename.lastIndexOf("."));
		}
		try
		{
			filetime=(new Long(filename)).longValue();
		}
		catch(NumberFormatException e){}
		filetime=filetime*1000+addnum;
		Vector thebrc=(Vector)User.UserPassItem.BRC.get(Board.Name);
		if(thebrc != null && thebrc.size() != 0)
		{
			Long tmpl=(Long)thebrc.elementAt(thebrc.size()-1);
			if(tmpl.longValue()>filetime)
				return false;
			return !thebrc.contains(new Long(filetime));
		}
		return true;
	}*/
	
	protected void NoDir()
	{
		if(digest)
		{
			digest=false;
			MyDir=Consts.DotDir;
			return;
		}	
		User.move(1,4);
		User.sends(Prompt.Msgs[22]);
		switch(User.MakeSure())
		{
		case 'P':
			OtherFunction((int)'p'-(int)'a'+1);
			break;
		case 'Q':
		default:
			break;
		}
	}
	
	protected boolean OtherFunction(int OtherKey)
	{
		boolean Anony=Board.AnonyDefault;
		StringBuffer Ans=new StringBuffer(),Title=null;

		if(!Board.Anonymous)
			Anony=false;
		switch(OtherKey)
		{
		case ((int)'c'-(int)'a'+1):
			if(!BBS.HasOnePerm(pid,Perm.Post))
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				break;
			Ans.setLength(0);
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[403]);
			if((User.GetData=new LineEdit(Ans,18,pid,true)).DoEdit()<0)
				return true;
			User.GetData=null;
			if(Ans.length()==0)
			{
				//Add by WilliamWey for ­«¦L³Ì«á¤@¦æ
				User.printendline();
				//
				break;
			}
			//search the board item....
			String bn = Ans.toString();
			BoardItem found;
			if ((found = ColaServer.BList.get(bn)) == null)
				break;
			
			byte deliverTag=(byte)'L';
			String newFileName=null;
			char appendChar='A'-1; //for creat file and refresh board time....
			long theTime=-1;
			
			theTime=((new Date()).getTime()/1000);
			do
			{
				appendChar++;
				newFileName="M."+theTime+"."+appendChar;
			}while((new File(CurrentPath,newFileName)).exists());
			
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[36]+(char)Board.DefaultSaveMode()+"]: [m");
			switch(User.MakeSure())
			{
			case 'L':
				deliverTag=(byte)'L';
				break;
			case 'S':
				deliverTag=(byte)'S';
				break;
			default:
				deliverTag=Board.DefaultSaveMode();
				break;
			}
			ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+User.UFD.ID+" cross-posted "+((PostType)rt).getTitle());
			//					AddList(SaveFileName);
			RecordHandler.append(found,new PostType(newFileName,User.UFD.ID,Prompt.Msgs[407]+((PostType)rt).getTitle(),deliverTag),ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator,Consts.DotDir);
			//copy the article....
			RandomAccessFile target=null,source=null;
			
			try
			{
				source=new RandomAccessFile(CurrentPath+rt.deleteBody(),"rw");
				target=new RandomAccessFile(ColaServer.INI.BBSHome+"boards"+File.separator+found.Name+File.separator+newFileName,"rw");
				byte buffer[]=new byte[(int)source.length()];
				//						LinkType ptr;

				target.writeBytes(Prompt.Msgs[46]+STRING.Cut(User.UFD.ID+" ("+User.UFD.NickName+")",46)+Prompt.Msgs[183]+STRING.Cut(found.Name,20)+"[m\r\n");
				target.writeBytes(Prompt.Msgs[65]+STRING.Cut(Prompt.Msgs[407]+((PostType)rt).getTitle(),73)+"[m\r\n");
				target.writeBytes(Prompt.Msgs[271]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),73)+"[m\r\n");
				target.writeBytes(Prompt.Msgs[237]);
				target.writeBytes("\r\n");
				target.writeBytes(Prompt.Msgs[404]+" "+Board.Name+" "+Prompt.Msgs[405]+"\r\n");
				source.read(buffer);
				target.write(buffer);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
			finally
			{
				try
				{
					if(target!=null)
					{
						target.close();
						target=null;
					}
					if(source!=null)
					{
						source.close();
						source=null;
					}
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			
			//refresh the filetime of board
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[406]);
			
			found.filetime=theTime*1000+(appendChar-'A');

			break;
		case ((int)'g'-(int)'a'+1):
		case '`':
			digest^=true;
			if(digest)
				MyDir=Consts.DigestDir;
			else
				MyDir=Consts.DotDir;
			DrawTitle();
			return true;
		case 'g':
			if(digest)
				break;
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				break;
			if(((PostType)rt).isDigest())
				rmdigest(Board.Name,CurrentPath,"G"+rt.deleteBody().substring(1));
			else
				adddigest(Board.Name,rt.deleteBody(),CurrentPath,((PostType)rt).getPoster(),((PostType)rt).getTitle());
			((PostType)rt).invertDigest();
			RecordHandler.update(now+cursor,lockObj,rt,CurrentPath,MyDir);
			User.move(1,cursor+4);
			User.clrtoeol();
			User.sends("  "+STRING.CutLeft(""+(now+cursor+1),4)+recordTag(rt)+rt.getRecordString()+"[m\r\n");

			break;
		case Keys.Tab:
			Welcome(true);
			DrawTitle();
			return true;
		case 'W':
			if(digest)
				break;
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!HasRangeDelPerm())
				break;
			(new Editor(pid)).DoEdit(ColaServer.INI.BBSHome + "boards" + File.separator + Board.Name + File.separator + ".Welcome".toString());
			DrawTitle();
			return true;
		case 'x':
			{
				String TitleBuf=Board.Title;

				if(TitleBuf.lastIndexOf(' ')!=-1)
					TitleBuf=TitleBuf.substring(TitleBuf.lastIndexOf(' '));
				if(HasRangeDelPerm())
					(new AnnounceList(TitleBuf,ColaServer.INI.BBSHome+"man"+File.separator+"boards"+File.separator+Board.Name+File.separator,pid,User.UFD.Perm,0)).DoList();
				else
					(new AnnounceList(TitleBuf,ColaServer.INI.BBSHome+"man"+File.separator+"boards"+File.separator+Board.Name+File.separator,pid,User.UFD.Perm&(~Perm.BM),0)).DoList();
				DrawTitle();
				return true;
			}
		case 'I':
			if(!HasRangeDelPerm())
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				break;
			//becareful! the lock object is not suitable...
			if(User.SilkRoad==null)
				AnnounceList.additem(this,new File(CurrentPath,rt.deleteBody()),ColaServer.INI.BBSHome+"man"+File.separator+"boards"+File.separator+Board.Name+File.separator,User.UFD.ID,((PostType)rt).getTitle());
			else
				AnnounceList.additem(this,new File(CurrentPath,rt.deleteBody()),User.SilkRoad,User.UFD.ID,((PostType)rt).getTitle());

			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[373]);
			TIME.Delay(500);
			User.printendline();
			break;
		case 'T':
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				break;
			if(!((PostType)rt).getPoster().equalsIgnoreCase(User.UFD.ID)&&!BBS.HasOnePerm(pid,Perm.SYSOP))
				break;
			Title=new StringBuffer(((PostType)rt).getTitle());

			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[30]);
			(User.GetData=new LineEdit(Title,50,pid,true)).DoEdit();
			User.GetData=null;

			if(Title.length()!=0)
			{
				((PostType)rt).setTitle(Title.toString());
				RecordHandler.update(now+cursor,lockObj,rt,CurrentPath,MyDir);
				User.move(1,cursor+4);
				User.clrtoeol();
				User.sends("  "+STRING.CutLeft(""+(now+cursor+1),4)+recordTag(rt)+rt.getRecordString()+"[m\r\n");
			}
			User.printendline();
			break;
		case 'E':  //¦pªG¦³Âà«H¥i¯à­n°ecancel message....
			if(User.UFD.ID.equalsIgnoreCase("guest"))
				break;
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				break;
			if(!((PostType)rt).getPoster().equalsIgnoreCase(User.UFD.ID)&&!HasRangeDelPerm())
				break;
			else
			{
				String SaveFileName;
				Title=new StringBuffer(((PostType)rt).getTitle());

				User.move(1,24);
				User.clrtoeol();
				User.sends(Prompt.Msgs[30]);
				(User.GetData=new LineEdit(Title,50,pid,true)).DoEdit();
				User.GetData=null;
				if(Title.length()==0)
				{
					User.printendline();
					break;
				}
				if(((PostType)rt).getPoster().equalsIgnoreCase(User.UFD.ID))
				{
					User.move(1,24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[34]+User.UFD.signature+Prompt.Msgs[35]);
					(User.GetData=new LineEdit(Ans,2,pid,true)).DoEdit();
					User.GetData=null;
					if(Ans.length()!=0&&Ans.charAt(0)>='0'&&Ans.charAt(0)<='9')
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
						}
					}
					SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(Title.toString(),new File(ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,rt.deleteBody()),ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator+rt.deleteBody(),((PostType)rt).getPoster(),'E',User.UFD.signature,false,Board);
				}
				else
				{
					SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(Title.toString(),new File(ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,rt.deleteBody()),ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator+rt.deleteBody(),((PostType)rt).getPoster(),'E',(byte)0,false,Board);
				}
				User.CurrentEditor=null;
				System.gc();
				if(SaveFileName!=null)
				{
					ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+User.UFD.ID+" changed "+((PostType)rt).getTitle());
					((PostType)rt).setTitle(Title.toString());
					RecordHandler.update(now+cursor,lockObj,rt,CurrentPath,MyDir);
					User.printendline();
				}
			}
			DrawTitle();
			return true;
		case ((int)'p'-(int)'a'+1):
			if(digest)
				break;
			if(!BBS.HasOnePerm(pid,Board.Level))
				break;
			String SaveFileName,ModeBuf;

			ModeBuf=User.usermode;
			BBS.SetUserMode(pid,Modes.Post);

			User.Clear();
			while(true)
			{
				Ans.setLength(0);
				User.move(1,20);
				User.clrtoeol();
				User.sends(Prompt.Msgs[23]+Board.Name+Prompt.Msgs[39]);
				User.clrtoeol();
				if(Title!=null)
					User.sends(Prompt.Msgs[24]+Title.toString()+"[m\r\n");
				else
					User.sends(Prompt.Msgs[25]);
				User.clrtoeol();
				User.sends(Prompt.Msgs[26]+User.SigNum+Prompt.Msgs[27]);
				if(Board.Anonymous)
					User.sends(Prompt.Msgs[28]);
				else
					User.sends(Prompt.Msgs[29]);
				if(Title==null)
				{
					User.clrtoeol();
					User.sends(Prompt.Msgs[30]);
					Title=new StringBuffer();
					if((User.GetData=new LineEdit(Title,50,pid,true)).DoEdit()<0)
						return true;
					User.GetData=null;
					continue;
				}
				else if(Title.length()==0)
				{
					DrawTitle();
					BBS.SetUserMode(pid,ModeBuf);
					return true;
				}
				User.clrtoeol();
				User.sends(Prompt.Msgs[31]);
				if(Board.Anonymous)
				{
					if(Anony)
						User.sends(Prompt.Msgs[32]);
					else
						User.sends(Prompt.Msgs[33]);
				}
				User.sends(Prompt.Msgs[34]+User.UFD.signature+Prompt.Msgs[35]);
				if((User.GetData=new LineEdit(Ans,2,pid,true)).DoEdit()<0)
					return true;
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
					Title=null;
					break;
				case 's':
				case 'S':
					if(Board.Anonymous)
						Anony=!Anony;
					break;
				}
			}
			SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(Title.toString(),ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,User.UFD.signature,Anony,Board);
			User.CurrentEditor=null;
			System.gc();
			if(SaveFileName!=null)
			{
				deliverTag=(byte)'L';

				if(!Anony)
				{
					User.move(1,24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[36]+(char)Board.DefaultSaveMode()+"]: [m");
					switch(User.MakeSure())
					{
					case 'L':
						deliverTag=(byte)'L';
						break;
					case 'S':
						deliverTag=(byte)'S';
						break;
					default:
						deliverTag=Board.DefaultSaveMode();
						break;
					}
				}
				if(!Board.JunkBoard)
					User.UFD.NumPosts++;

				ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+User.UFD.ID+" posted "+Title.toString());
				//AddList(SaveFileName);
				boardrc.setArticleViewed(SaveFileName);
				if(Anony)
					RecordHandler.append(Board,new PostType(SaveFileName,"Anonymous",Title.toString(),deliverTag),CurrentPath,MyDir);
				else
					RecordHandler.append(Board,new PostType(SaveFileName,User.UFD.ID,Title.toString(),deliverTag),CurrentPath,MyDir);
				
				if(SaveFileName.lastIndexOf(".")>=0)//refresh the filetime of board
				{
					int addnum=(int)(SaveFileName.charAt(SaveFileName.lastIndexOf('.')+1)-'A');
					SaveFileName=SaveFileName.substring(2,SaveFileName.lastIndexOf("."));
					Board.filetime=0;
					try
					{
						Board.filetime=(new Long(SaveFileName)).longValue();
					}
					catch(NumberFormatException e){}
					Board.filetime=Board.filetime*1000+addnum;
				}
			}
			DrawTitle();
			BBS.SetUserMode(pid,ModeBuf);
			return true;
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
						((PostType)rt).invertMark();
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
			/*		case 'M':	//ºÞ²z§ë²¼
			if(!HasRangeDelPerm())
			break;
			(new ListVote(pid, ListVote.MODE_MANAGE, Board.vote)).DoList();
			DrawTitle();
			return true;
			case 'V':	//§ë²¼
			if(!Utils.HasOnePerm(pid, Perm.Post))
			break;
			(new ListVote(pid, ListVote.MODE_VOTE, Board.vote)).DoList();
			DrawTitle();
			return true;
			case 'R':	//Æ[¬Ý§ë²¼µ²ªG
			(new ListVote(pid, ListVote.MODE_REPORT, Board.vote)).DoList();
			DrawTitle();
			return true;*/
		case Keys.Enter:
		case Keys.Right:
			enterFunction();
			break;
		default:
			break;
		}
		return false;
	}
	
	protected void exitList()
	{
		tboardrc.setCurrentPosition(now + cursor);
	}
	
	public static boolean postFile(BoardItem bi, String Path, String FileName, String Poster, String Title, byte tag, boolean copy)
	{
		return false;
	}
	
	protected int ReadEnd(RecordType t)
	{
		int InKey;
		PostType pt=(PostType)t;

		//AddList(pt.deleteBody());
		boardrc.setArticleViewed(pt.deleteBody());

		User.move(1,24);
		User.sends(Prompt.Msgs[37]);
		InKey=User.getch();
		if(digest)
			return -1;
		switch(InKey)
		{
		case -1:
			return -1;
		case 'R':
		case 'r':
			boolean Anony=Board.AnonyDefault;
			char QuoteMode='Y';
			String Title=pt.getTitle();
			if(Title.length()<4||(Title.length()>3&&!Title.substring(0,4).equals("Re: ")))
				Title="Re: "+Title;
			StringBuffer TitleBuf=new StringBuffer(Title);
			StringBuffer Ans=new StringBuffer();
			String SaveFileName,ModeBuf;

			if(!Board.Anonymous)
				Anony=false;
			if(!BBS.HasOnePerm(pid,Board.Level))
				break;
			ModeBuf=User.usermode;
			BBS.SetUserMode(pid,Modes.Post);

			User.Clear();
			while(true)
			{
				Ans.setLength(0);
				User.move(1,20);
				User.clrtoeol();
				User.sends(Prompt.Msgs[23]+Board.Name+Prompt.Msgs[39]);
				User.clrtoeol();
				if(TitleBuf.length()!=0)
					User.sends(Prompt.Msgs[24]+TitleBuf.toString()+"[m\r\n");
				else
					User.sends(Prompt.Msgs[25]);
				User.clrtoeol();
				User.sends(Prompt.Msgs[26]+User.SigNum+Prompt.Msgs[43]);
				if(Board.Anonymous)
					User.sends(Prompt.Msgs[28]);
				else
					User.sends(Prompt.Msgs[29]);
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
				if(Board.Anonymous)
				{
					if(Anony)
						User.sends(Prompt.Msgs[32]);
					else
						User.sends(Prompt.Msgs[33]);
				}
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
				case 's':
				case 'S':
					if(Board.Anonymous)
						Anony=!Anony;
					break;
				}
			}
			if(QuoteMode!='N')
				SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(TitleBuf.toString(),new File(ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,t.deleteBody()),ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,pt.getPoster(),QuoteMode,User.UFD.signature,Anony,Board);
			else
				SaveFileName=(User.CurrentEditor=new Editor(pid)).DoEdit(TitleBuf.toString(),ColaServer.INI.BBSHome+"boards"+File.separator+Board.Name+File.separator,User.UFD.signature,Anony,Board);
			User.CurrentEditor=null;
			System.gc();
			if(SaveFileName!=null)
			{
				byte deliverTag=(byte)'L';

				User.move(1,24);
				User.clrtoeol();
				if(!Anony)
				{
					User.sends(Prompt.Msgs[36]+(char)Board.DefaultSaveMode()+"]: [m");
					switch(User.MakeSure())
					{
					case 'L':
						deliverTag=(byte)'L';
						break;
					case 'S':
						deliverTag=(byte)'S';
						break;
					default:
						deliverTag=Board.DefaultSaveMode();
						break;
					}
				}

				if(!Board.JunkBoard)
					User.UFD.NumPosts++;

				ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" user "+User.UFD.ID+" post article : "+TitleBuf.toString());
				//AddList(SaveFileName);
				boardrc.setArticleViewed(SaveFileName);
					
				if(Anony)
					RecordHandler.append(Board,new PostType(SaveFileName,"Anonymous",TitleBuf.toString(),deliverTag),CurrentPath,MyDir);
				else
					RecordHandler.append(Board,new PostType(SaveFileName,User.UFD.ID,TitleBuf.toString(),deliverTag),CurrentPath,MyDir);
				
				if(SaveFileName.lastIndexOf(".")>=0)//refresh the filetime of board
				{
					int addnum=(int)(SaveFileName.charAt(SaveFileName.lastIndexOf('.')+1)-'A');
					SaveFileName=SaveFileName.substring(2,SaveFileName.lastIndexOf("."));
					Board.filetime=0;
					try
					{
						Board.filetime=(new Long(SaveFileName)).longValue();
					}
					catch(NumberFormatException e){}
					Board.filetime=Board.filetime*1000+addnum;
				}
				
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
		char ReadFlag;
		
		//if(New(t.deleteBody()))
		if (!boardrc.isArticleViewed(t.deleteBody()))
			ReadFlag='N';
		else
			ReadFlag=' ';
		if(((PostType)t).isDigest())
		{
			if(ReadFlag=='N')
				ReadFlag='G';
			else
				ReadFlag='g';
		}
		if(((PostType)t).isMark())
		{
			switch(ReadFlag)
			{
			case ' ':
				ReadFlag='m';
				break;
			case 'N':
				ReadFlag='M';
				break;
			case 'g':
				ReadFlag='b';
				break;
			case 'G':
				ReadFlag='B';
				break;
			}
		}
		return ReadFlag+" ";
	}
	
	public synchronized static void rmdigest(Object theObj,String DelPath,String DelName)
	{
		RecordHandler.delete(theObj,new PostType(DelName,null,null),DelPath,Consts.DigestDir);
	}
}