package colabbs.telnet;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.UTILS.FILE.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.USERFILEDATA.*;

import colabbs.telnet.DATA.TBOARDRC.*;

/**
 * ·sªº
 */
import colabbs.bbstp.MultiModuleCmd;
import colabbs.bbstp.chat.ChatLogin;

public final class TelnetUser extends BBSUser implements TalkUser
{
	private static int issuex=-1,issuey=-1;
	public LineEdit GetData;
	public DirList DirListBuf=null;
	public int ScX=1,ScY=1,chatY=2;
	public MainMenu BBSMainMenu;
	public Editor CurrentEditor=null;
	public int EscArg;
	
	public int BoardSortNum = 0;
	public BoardItem BoardListCurrent = null;
	
	public String SearchTitle = new String();
	public String SearchAuthor = new String();
	public String ForwardEMail = null;
	
	//add by WilliamWey for Lock
	public boolean InputDirectMode = false;
	//
	
	public TBoardRCList tbrclist = new TBoardRCList();

	public int SignFileCacheNum = -1;
	public String[] SignFileCache = null;
	
	public String[] getSignFile(int num)
	{
		if (num == SignFileCacheNum)
			return SignFileCache;
		
		SignFileCacheNum = num;
		SignFileCache = super.getSignFile(num);
			
		return SignFileCache;
	}		
	
	public TelnetUser(int userpid) {
		super(userpid);
	}

	
	public int ansimore(File F)
	{
		return ansimore(F.getAbsolutePath(), -1, false);
	}
	
	public int ansimore(String fname)
	{
		return ansimore(fname,-1,false);
	}
	
	public int ansimore(String fname,int stopbytes,boolean NotePad)
	{
		long alllen=0;
		RandomAccessFile more=null;

		try
		{
			File morefile=new File(fname);

			if(morefile.exists())
			{
				more=new RandomAccessFile(fname,"r");
				LinkList LineLocate=new LinkList();
				FileLocateType locatebuf;
				String morebuf=new String();
				short alllines=1;
				short lines=1;
				long here=0,lastbuf=0;

				Clear();
				alllen=more.length();

				alllines=1;

				if(NotePad)
				{
					LineLocate.ladditem(new FileLocateType((here=more.getFilePointer())));
					if(here+stopbytes>=alllen)
					{
						//						more.close();
						return (int)-1;
					}
					lines++;
					alllines++;
					sends(Prompt.Msgs[240]);
				}
AnsiMoreLoop:
				while((morebuf=more.readLine())!=null)
				{
					LineLocate.ladditem(new FileLocateType((here=more.getFilePointer())));
					if(here+stopbytes>alllen)
						break;
					if(((morebuf.length()>=7&&(morebuf.substring(0,7).equals(Prompt.Msgs[48]))||(morebuf.length()>=8&&morebuf.substring(0,8).equals(Prompt.Msgs[38]))))||(morebuf.length()>=3&&morebuf.substring(0,3).equals("==>"))||(morebuf.length()>=5&&morebuf.substring(0,5).equals(Prompt.Msgs[49])))
						sends("[1;33m");
					else if((morebuf.length()!=0)&&(morebuf.charAt(0)==':'||morebuf.charAt(0)=='>'))
						sends("[0;36m");
					else
						sends("[m");
					sends(morebuf+"\r\n");
					lines++;
					alllines++;
					if(lines>23&&here!=alllen)
					{
						boolean clearflag=false;
						int i;

						move(1,24);
						sends(Prompt.Msgs[241]+STRING.CutLeft(""+(int)(here*100/alllen),3)+Prompt.Msgs[161]);
AnsiMoreReadKeyLoop:
						while(true)
						{
							switch(getch())
							{
							case -1:
								return -1;
							case '?':
							case 'h':
							case 'H':
								ansimore("help"+File.separator+"morehelp");
								alllines++;
								PressAnyKey();
							case Keys.Up:
							case 'k':
								long backbuf[];

								backbuf=new long[24];
								if(alllines==24)
									break;
								if(NotePad&&alllines==25)
								{
									ScrollDown();
									move(1,1);
									sends(Prompt.Msgs[240]);
									alllines--;
									//                                        break;
								}
								for(i=0;i<24;i++)
								{
									locatebuf=(FileLocateType)LineLocate.del1();
									if(locatebuf!=null)
										backbuf[i]=locatebuf.number;
									else if(i!=0)
										backbuf[i]=backbuf[i-1];
									else
										backbuf[i]=0;
								}
								if(alllines!=24)
								{
									locatebuf=(FileLocateType)LineLocate.del1();

									if(locatebuf!=null)
									{
										here=locatebuf.number;
										if(here!=-1)
											more.seek(here);
										LineLocate.ladditem(new FileLocateType(more.getFilePointer()));
										alllines--;
									}
									else
									{
										more.seek(0);
										LineLocate.ladditem(new FileLocateType(more.getFilePointer()));
										alllines=24;
									}
									clearflag=true;

									morebuf=more.readLine();
									ScrollDown();
									move(1,1);
									if(((morebuf.length()>=7&&(morebuf.substring(0,7).equals(Prompt.Msgs[48]))||(morebuf.length()>=8&&morebuf.substring(0,8).equals(Prompt.Msgs[38]))))||(morebuf.length()>=3&&morebuf.substring(0,3).equals("==>"))||(morebuf.length()>=5&&morebuf.substring(0,5).equals(Prompt.Msgs[49])))
										sends("[1;33m");
									else if((morebuf.length()!=0)&&(morebuf.charAt(0)==':'||morebuf.charAt(0)=='>'))
										sends("[0;36m");
									else
										sends("[m");
									sends(morebuf+"\r\n");
								}
								for(i=23;i>0;i--)
									LineLocate.ladditem(new FileLocateType(backbuf[i]));
								more.seek(backbuf[1]);
								here=backbuf[1];
								move(1,24);
								sends(Prompt.Msgs[241]+STRING.CutLeft(""+(int)(here*100/alllen),3)+Prompt.Msgs[161]);
								break;
							case Keys.Enter:
							case Keys.Down:
							case 'j':
								if((morebuf=more.readLine())!=null)
								{
									LineLocate.ladditem(new FileLocateType((here=more.getFilePointer())));
									move(1,24);
									clrtoeol();
									if(((morebuf.length()>=7&&(morebuf.substring(0,7).equals(Prompt.Msgs[48]))||(morebuf.length()>=8&&morebuf.substring(0,8).equals(Prompt.Msgs[38]))))||(morebuf.length()>=3&&morebuf.substring(0,3).equals("==>"))||(morebuf.length()>=5&&morebuf.substring(0,5).equals(Prompt.Msgs[49])))
										sends("[1;33m");
									else if((morebuf.length()!=0)&&(morebuf.charAt(0)==':'||morebuf.charAt(0)=='>'))
										sends("[0;36m");
									else
										sends("[m");
									sends(morebuf+"\r\n");
									sends(Prompt.Msgs[241]+STRING.CutLeft(""+(int)(here*100/alllen),3)+Prompt.Msgs[161]);
									alllines++;
									break;
								}
								break;
							case Keys.PgDn:
							case Keys.Space:
							case Keys.Right:
								break AnsiMoreReadKeyLoop;
							case Keys.PgUp:
								if(alllines==24)
									break;
								for(i=0;i<46;i++)
									LineLocate.del1();
								locatebuf=(FileLocateType)LineLocate.del1();

								if(locatebuf!=null)
								{
									here=locatebuf.number;
									more.seek(here);
									LineLocate.ladditem(new FileLocateType(more.getFilePointer()));
									alllines-=46;
								}
								else
								{
									more.seek(0);
									alllines=1;
								}
								break AnsiMoreReadKeyLoop;
							case Keys.Left:
							case 'q':
							case 'Q':
								break AnsiMoreLoop;
							}
						}
						if(clearflag)
							Clear();
						else
						{
							move(1,24);
							clrtoeol();
						}
						lines=1;
					}
				}
				if(NotePad)
				{
					if(lines==24)
					{
						move(1,24);
						clrtoeol();
					}
					sends(Prompt.Msgs[243]);
				}
				else if(alllines<24)
				{
					for(;lines<24;lines++)
					{
						move(1,lines);
						clrtoeol();
					}
					move(1,24);
					clrtoeol();
				}

				//				more.close();
			}
			else
				return -1;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(more!=null)
				{
					more.close();
					more=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}	
		return (int)alllen;
	}

	public void Bell()
	{
		byte bellbuf[]=new byte[2];
		bellbuf[0]=7;
		bellbuf[1]=7;
		sends(bellbuf);
		try
		{
			os.flush();
		}
		catch(Exception e){}
	}
	public String BoardComplete(String Msg)
	{
		int MyBoardNum=0;
		Vector BoardVector;
		BoardItem BoardBuf;
		
		BoardVector = ColaServer.BList.getVisibleList(UFD.Perm);

		boolean none,onlyone,firsttime=false;
		int inkey,num=0,locate;
		StringBuffer namebuf=new StringBuffer(),LastID=null;
		Enumeration namelist=BoardVector.elements();

		move(1,2);
		clrtoeol();
		sends(Msg+namebuf.toString());
		onlyone=false;
		while((inkey=getch())!=Keys.Enter)
		{
			if(inkey<0)
				return null;
			if(inkey==' ')
			{
				int min,lines;

				if(firsttime&&!onlyone)
					namelist=BoardVector.elements();

				firsttime=false;
				locate=0;
				num=0;
				lines=0;

				if(!namelist.hasMoreElements())
				{
					if(onlyone)
					{
						namebuf=LastID;
						move(1,2);
						clrtoeol();
						sends(Msg+namebuf.toString());
						continue;
					}
				}

				move(1,3);
				//sends("================================== All  Users ==================================\r\n");
				sends("================================== All Boards ==================================\r\n");
				clrtoeol();
				while(namelist.hasMoreElements())
				{
					BoardBuf=(BoardItem)namelist.nextElement();
					if(BoardBuf.Name.length()>=namebuf.length())
						min=namebuf.length();
					else
						continue;
					if(BoardBuf.Name.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						sends(STRING.Cut(BoardBuf.Name,19));
						locate++;
						if(locate>3)
						{
							newline();
							clrtoeol();
							locate=0;
							lines++;
						}
						num++;
						if(num>=80)
						{
							clrtoeol();
							sends(Prompt.Msgs[261]);
							break;
						}
					}
				}
				for(;lines<19;lines++)
				{
					newline();
					clrtoeol();
				}
				if(!namelist.hasMoreElements())
				{
					newline();
					clrtoeol();
					namelist=BoardVector.elements();
				}
				continue;
			}
			if((inkey>='A'&&inkey<='Z')||(inkey>='a'&&inkey<='z')||(inkey>='0'&&inkey<='9')||(inkey=='_')||(inkey==Keys.BackSpace))
			{
				int min,lines;

				if(inkey==Keys.BackSpace)
				{
					if(namebuf.length()>0)
						namebuf.setLength(namebuf.length()-1);
					else
						continue;
				}
				else
					namebuf.append((char)inkey);
				namelist=BoardVector.elements();
				none=true;
				onlyone=true;
				firsttime=true;
				while(namelist.hasMoreElements())
				{
					BoardBuf=(BoardItem)namelist.nextElement();
					if(BoardBuf.Name.length()>=namebuf.length())
						min=namebuf.length();
					else
						continue;
					if(BoardBuf.Name.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						if(onlyone)
							LastID=new StringBuffer(BoardBuf.Name);
						if(!none)
							onlyone=false;
						none=false;
					}
				}
				if(none)
				{
					onlyone=false;
					namebuf.setLength(namebuf.length()-1);
					continue;
				}
			}
			move(1,2);
			clrtoeol();
			sends(Msg+namebuf.toString());
		}
		return namebuf.toString();
	}

	public void CheckForm()
	{
		UserFileData ufd;
		RandomAccessFile newreg=null,regdata=null,skipreg=null;
		File newregfile=new File(ColaServer.INI.BBSHome,"NewRegister");
		File newreglock=new File(ColaServer.INI.BBSHome,"NewRegister.lock");
		int tries;
		
		try
		{
			if(!newregfile.exists()||newregfile.length()==0)
				return;
			tries=0;
			while(tries<10&&newreglock.exists()&&!newreglock.delete())
			{
				TIME.Delay(100);
				tries++;
			}	
			tries=0;
			while(tries<10&&newregfile.exists()&&!newregfile.renameTo(newreglock))
			{
				TIME.Delay(100);
				tries++;
			}	

			newreg=new RandomAccessFile(ColaServer.INI.BBSHome+"NewRegister.lock","r");
			regdata=new RandomAccessFile(ColaServer.INI.BBSHome+"Register","rw");
			String RegUserID;
			long newnum=newreg.length()/8,i,uidbuf;
			byte writebuf[]=new byte[Consts.UserLen];

			regdata.seek(regdata.length());
			
			sends("[m");

			for(i=0;i<newnum;i++)
			{
				uidbuf=newreg.readLong();
				RegUserID=DumpUserInfo(uidbuf);
				sends(Prompt.Msgs[214]);
				switch(MakeSure())
				{
				case 'Y':
					ufd = ColaServer.UFDList.get((int)uidbuf);
					ufd = ColaServer.UFDList.get(ufd.ID);
					ufd.resetPerm(ColaServer.INI.RegPerm);
					ColaServer.UFDList.save(ufd);
					ufd = null;

					regdata.write(writebuf);
					ColaServer.NewRegList.remove(new Long(uidbuf));
					MailFile(RegUserID,ColaServer.INI.BBSHome+"etc","s_fill",Prompt.Msgs[155],null,true);
					break;
				case 'N':
					ColaServer.NewRegList.remove(new Long(uidbuf));
					MailFile(RegUserID,ColaServer.INI.BBSHome+"etc","f_fill",Prompt.Msgs[152],null,true);
					break;
				default:
					skipreg=new RandomAccessFile(ColaServer.INI.BBSHome+"NewRegister","rw");
					skipreg.seek(skipreg.length());
					skipreg.writeLong(uidbuf);
					skipreg.close();
					skipreg = null;
					break;
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(newreg!=null)
				{
					newreg.close();
					newreg=null;
				}	
				if(regdata!=null)
				{
					regdata.close();
					regdata=null;
				}	
				if(skipreg!=null)
				{
					skipreg.close();
					skipreg=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			tries=0;
			while(tries<10&&newreglock.exists()&&!newreglock.delete())
			{
				TIME.Delay(500);
				tries++;
			}	
		}	
	}
	public void ChPwd()
	{
		String pwd;
		StringBuffer pwdbuf=new StringBuffer(),newpwd=new StringBuffer();

		Clear();
		sends(Prompt.Msgs[312]);
		GetData=new LineEdit(pwdbuf,Consts.PassLen,pid,false);
		GetData.DoEdit();
		GetData=null;
		if(!checkpasswd(pwdbuf.toString(),UFD.PassWord))
		{
			sends(Prompt.Msgs[223]);
			PressReturn();
			return;
		}
		pwdbuf.setLength(0);
		sends(Prompt.Msgs[211]);
		GetData=new LineEdit(pwdbuf,Consts.PassLen,pid,false);
		GetData.DoEdit();
		pwd=genpasswd(pwdbuf.toString());
		pwdbuf.setLength(0);
		sends(Prompt.Msgs[212]);
		GetData=new LineEdit(pwdbuf,Consts.PassLen,pid,false);
		GetData.DoEdit();
		if(!checkpasswd(pwdbuf.toString(),pwd))
		{
			sends(Prompt.Msgs[213]);
			PressReturn();
			return;
		}
		sends(Prompt.Msgs[313]);
		if(MakeSure()!='Y')
		{
			sends(Prompt.Msgs[284]);
			PressReturn();
			return;
		}
		
		ColaServer.UFDList.changerPassWord(UFD, pwd);
	}
	
	public void ChUserPerm()
	{
		int PermBuf;
		long uid;
		String UserBuf;

		UserBuf=NameComplete(Prompt.Msgs[98]);
		if(!ColaServer.UFDList.exist(UserBuf))
			return;
		Clear();
		
		UserFileData ufd = ColaServer.UFDList.get(UserBuf);
		ufd.Perm = SetPerm(ufd.Perm);
		ColaServer.UFDList.save(ufd);
	}
	
	public void ChUserPwd()
	{
		int onlinebuf;
		String chid,pwd;
		StringBuffer pwdbuf=new StringBuffer(),newpwd=new StringBuffer();
		PassItem ChPassItem;

		Clear();
		chid=NameComplete(Prompt.Msgs[311]);
		if(!ColaServer.UFDList.exist(chid))
			return;
		Clear();
		sends(Prompt.Msgs[211]);
		GetData=new LineEdit(pwdbuf,Consts.PassLen,pid, false);
		GetData.DoEdit();
		GetData=null;
		pwd=genpasswd(pwdbuf.toString());
		pwdbuf.setLength(0);
		sends(Prompt.Msgs[212]);
		GetData=new LineEdit(pwdbuf,Consts.PassLen,pid, false);
		GetData.DoEdit();
		if(!checkpasswd(pwdbuf.toString(),pwd))
		{
			sends(Prompt.Msgs[213]);
			PressReturn();
			return;
		}
		ChPassItem=ColaServer.UFDList.getPass(chid.toString());
		sends(Prompt.Msgs[313]);
		if(MakeSure()!='Y')
		{
			sends(Prompt.Msgs[284]);
			PressReturn();
			return;
		}
		ColaServer.UFDList.changerPassWord(chid, pwd);		
	}
	
	public void Clear()
	{
		boolean tmpansi=Ansi;

		Ansi=true;
		sends("[m[2J[0;0H");
		ScX=1;
		ScY=1;
		Ansi=tmpansi;
	}
	
	public void clrtoeol()
	{
		boolean tmpansi=Ansi;

		Ansi=true;
		move(1,ScY);
		sends("[K");
		move(1,ScY);
		Ansi=tmpansi;
	}
	
	public void DoMail()
	{
		DoMail(null);
	}
	
	public void DoMail(String OwnBuf)
	{
		//		boolean InternetMailFlag=false;
		char QuoteMode='Y';
		String FNBuf=null;
		//        String OwnBuf=null;
		StringBuffer TitleBuf=new StringBuffer(Prompt.Msgs[262]);
		StringBuffer Ans=new StringBuffer();
		String SaveFileName,ModeBuf;
		RandomAccessFile DirFile=null;

		ModeBuf=usermode;
		BBS.SetUserMode(pid,Modes.Mail);

		if(OwnBuf==null)
			OwnBuf=NameComplete(Prompt.Msgs[263]);
		if(OwnBuf.indexOf('@')==-1)
		{
			if(!ColaServer.UFDList.exist(OwnBuf))
				return;
			else
				OwnBuf=ColaServer.UFDList.getPass(OwnBuf).IDItem;
		}	
		else
		{
			if(!ColaServer.INI.InternetMail)
				return;
			//			InternetMailFlag=true;
		}	
		Clear();

		move(1,23);
		clrtoeol();
		sends(Prompt.Msgs[30]);
		(GetData=new LineEdit(TitleBuf,50,pid, true)).DoEdit();
		GetData=null;
		if(TitleBuf.length()==0)
			return;
		while(true)
		{
			Ans.setLength(0);
			move(1,20);
			clrtoeol();
			sends(Prompt.Msgs[264]+OwnBuf+"[m\r\n");
			clrtoeol();
			sends(Prompt.Msgs[24]+TitleBuf.toString()+"[m\r\n");
			clrtoeol();
			sends(Prompt.Msgs[26]+SigNum+Prompt.Msgs[267]);
			clrtoeol();
			sends(Prompt.Msgs[31]);
			sends(Prompt.Msgs[34]+UFD.signature+Prompt.Msgs[35]);
			(GetData=new LineEdit(Ans,2,pid,true)).DoEdit();
			GetData=null;
			if(Ans.length()==0)
				break;
			if(Ans.charAt(0)>='0'&&Ans.charAt(0)<='9')
			{
				try
				{
					UFD.signature=(byte)Integer.parseInt(Ans.toString());
				}
				catch(NumberFormatException e)
				{
					Bell();
				}
				finally
				{
					if(UFD.signature>SigNum)
						UFD.signature=0;
					continue;
				}
			}
			if(Ans.charAt(0)=='t'||Ans.charAt(0)=='T')
			{
				move(1,23);
				clrtoeol();
				sends(Prompt.Msgs[30]);
				(GetData=new LineEdit(TitleBuf,50,pid, true)).DoEdit();
				GetData=null;
				if(TitleBuf.length()==0)
					return;
				continue;
			}
		}
		SaveFileName=(CurrentEditor=new Editor(pid)).DoEdit(TitleBuf.toString(),ColaServer.INI.BBSHome+"mailtemp"+File.separator, UFD.signature);
		CurrentEditor=null;
		System.gc();
		if(SaveFileName!=null)
		{
			String MailOwner=UFD.ID+" ("+UFD.NickName+")";
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[434]);
			flush();
			MailFile(OwnBuf,ColaServer.INI.BBSHome+"mailtemp"+File.separator,SaveFileName,TitleBuf.toString(),MailOwner,false);
		}
		BBS.SetUserMode(pid,ModeBuf);
	}
	public void DoQuery()
	{
		String QueryBuf;

		QueryBuf=NameComplete(Prompt.Msgs[322]);
		if(!ColaServer.UFDList.exist(QueryBuf))
			return;
		Clear();
		printtitle(Prompt.Msgs[387],false);
		DoQuery(QueryBuf);
		move(1,24);
		clrtoeol();
		PressAnyKey();
	}
	
	public void DoQuery(long uid)
	{
		DoQuery(ColaServer.UFDList.get((int)uid));
	}
	
	public void DoQuery(String id)
	{
		DoQuery(ColaServer.UFDList.get(id));
	}
	
	public void DoQuery(UserFileData QueryBuf)
	{
		int i;
		String QRBuf=null;

		if(QueryBuf.ID.length()==0)
			return;

		int index=0;
		File PLANFile=new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(QueryBuf.ID.charAt(0))+File.separator+QueryBuf.ID,"PLANS");

		if((index=BBS.IfOnline(QueryBuf.ID))!=-1)
		{
			sends("[m\r\n[1;32m"+ColaServer.BBSUsers[index].UFD.ID+" [m([1;37m"+ColaServer.BBSUsers[index].UFD.NickName+Prompt.Msgs[184]+ColaServer.BBSUsers[index].UFD.NumLogins+Prompt.Msgs[185]+ColaServer.BBSUsers[index].UFD.NumPosts+Prompt.Msgs[186]);

			sends(Prompt.Msgs[187]+ColaServer.SysDATE.DateFormatter2.format(new Date(ColaServer.BBSUsers[index].UFD.LastLogin))+Prompt.Msgs[188]+ColaServer.BBSUsers[index].UFD.LastHost+Prompt.Msgs[189]);
			//				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[187]+(new Date(ColaServer.BBSUsers[index].UFD.LastLogin)).toString()+Prompt.Msgs[188]+ColaServer.BBSUsers[index].UFD.LastHost+Prompt.Msgs[189]);
			//Add by WilliamWey Áô§Î®É­ÔÅã¥Ü¤£¦b¯¸¤W
			if (!ColaServer.BBSUsers[index].Visible && !BBS.HasOnePerm(pid, Perm.SeeCloak))
				sends(Prompt.Msgs[438] + Prompt.Msgs[440] + Prompt.Msgs[439]);
			else
				sends(Prompt.Msgs[438] + ColaServer.BBSUsers[index].usermode + Prompt.Msgs[439]);			
			//
			//Add by WilliamWey for show new mail in Query
			if (ColaServer.BBSUsers[index].hasNewMail())
				sends(Prompt.Msgs[441]);
			else
				sends(Prompt.Msgs[442]);
			//
		}
		else
		{
			sends("[m\r\n[1;32m"+QueryBuf.ID+" [m([37m"+QueryBuf.NickName+Prompt.Msgs[184]+QueryBuf.NumLogins+Prompt.Msgs[185]+QueryBuf.NumPosts+Prompt.Msgs[186]);

			sends(Prompt.Msgs[187]+ColaServer.SysDATE.DateFormatter2.format(new Date(QueryBuf.LastLogin))+Prompt.Msgs[188]+QueryBuf.LastHost+Prompt.Msgs[189]);
			//				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[187]+(new Date(QueryBuf.LastLogin)).toString()+Prompt.Msgs[188]+QueryBuf.LastHost+Prompt.Msgs[189]);

			//Add by WilliamWey for show new mail in Query
			sends(Prompt.Msgs[438] + Prompt.Msgs[440] + Prompt.Msgs[439]);
			if (BBSUser.hasNewMail(QueryBuf))
				sends(Prompt.Msgs[441]);
			else
				sends(Prompt.Msgs[442]);
			//
		}
		//            System.out.println(index);
		
		
		if(PLANFile.exists())
		{
			RandomAccessFile PLAN=null;
			try
			{
				PLAN=new RandomAccessFile(PLANFile,"r");

				sends(Prompt.Msgs[190]);

				for(i=0;(i<ColaServer.INI.MaxQueryLines)&&((QRBuf=PLAN.readLine())!=null);i++)
					sends(QRBuf+"\r\n");
				//				PLAN.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
				//				ColaServer.BBSlog.Write("Exception #136"+e);
			}
			finally
			{
				try
				{
					if(PLAN!=null)
					{
						PLAN.close();
						PLAN=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
					//					ColaServer.BBSlog.Write("Closing file Exception #120"+e);
				}
			}
		}
		else
			sends(Prompt.Msgs[191]);
	}

	private void DoUserList()
	{
		ListUsers listbuf=new ListUsers(pid);
		listbuf.DoList();
		Clear();
	}
	public String DumpUserInfo(long uid)
	{
		UserFileData DumpBuf = ColaServer.UFDList.get((int)uid);

		Clear();
		sends(Prompt.Msgs[287]+DumpBuf.ID+"\r\n");
		sends(Prompt.Msgs[288]+DumpBuf.NickName+"\r\n");
		sends(Prompt.Msgs[289]+DumpBuf.RealName+"\r\n");
		sends(Prompt.Msgs[290]+DumpBuf.School+"\r\n");
		sends(Prompt.Msgs[291]+DumpBuf.Tel+"\r\n");
		sends(Prompt.Msgs[292]+DumpBuf.Address+"\r\n");
		sends(Prompt.Msgs[293]+DumpBuf.Email+"\r\n");
		sends(Prompt.Msgs[294]+DumpBuf.URL+"\r\n");
		sends(Prompt.Msgs[295]+DumpBuf.UserLevel+"\r\n");
		sends(Prompt.Msgs[296]);
		switch(DumpBuf.Sex)
		{
		case 1:
			sends(Prompt.Msgs[297]);
			break;
		case 2:
			sends(Prompt.Msgs[298]);
			break;
		case 3:
			sends(Prompt.Msgs[299]);
			break;
		case 4:
			sends(Prompt.Msgs[300]);
			break;
		case 5:
			sends(Prompt.Msgs[301]);
			break;
		case 6:
			sends(Prompt.Msgs[302]);
			break;
		case 7:
			sends(Prompt.Msgs[303]);
			break;
		case 8:
			sends(Prompt.Msgs[304]);
			break;
		case 9:
			sends(Prompt.Msgs[305]);
			break;
		}
		sends(Prompt.Msgs[306]);
		switch(DumpBuf.BloodType)
		{
		case 1:
			sends(" A\r\n");
			break;
		case 2:
			sends(" B\r\n");
			break;
		case 3:
			sends(" AB\r\n");
			break;
		case 4:
			sends(" O\r\n");
			break;
		}
		sends(Prompt.Msgs[150]+DumpBuf.NumLogins+"\r\n");
		sends(Prompt.Msgs[147]+DumpBuf.NumPosts+"\r\n");
		sends(Prompt.Msgs[283]+(int)(DumpBuf.Stay/3600000)+Prompt.Msgs[21]+(new Date(DumpBuf.Stay)).getMinutes()+Prompt.Msgs[286]+"\r\n");
		//		sends(Prompt.Msgs[101]+(new Date(DumpBuf.Birthday)).toString()+"\r\n");
		sends(Prompt.Msgs[101]+ColaServer.SysDATE.DateFormatter2.format(new Date(DumpBuf.Birthday))+"\r\n");
		return DumpBuf.ID;
	}
	public void EditBoard()
	{
		StringBuffer sb;
		String BoardName, BoardNameNew = null;
		BoardItem bi, biorg;
		boolean ENameCh = false;
		
		Clear();
		sends(Prompt.Msgs[143]);
		BoardName = BoardComplete(Prompt.Msgs[144]);
		
		if ((biorg = ColaServer.BList.get(BoardName)) == null)
			return;
		
		bi = biorg.copyOut();
		BoardName = bi.Name;
		
		sb = new StringBuffer((char)bi.EGroup + bi.Title);
		sends(Prompt.Msgs[110]);
		(GetData = new LineEdit(sb, 60, pid, true)).DoEdit();
		GetData = null;
		bi.Title = sb.toString();
		if (bi.Title.length() > 0)
		{
			bi.EGroup = (byte)bi.Title.charAt(0);
			bi.Title = bi.Title.substring(1);
		}
		else
			bi.EGroup = (byte)'0';
		
		sb = new StringBuffer(bi.BM);
		sends(Prompt.Msgs[111]);
		(GetData = new LineEdit(sb, 58, pid, true)).DoEdit();
		GetData = null;
		bi.BM = sb.toString();
		
		sends(Prompt.Msgs[112]);
		if (MakeSure() == 'Y')
		{
			sends(Prompt.Msgs[113]);
			if(MakeSure() == 'P')
			{
				Clear();
				sends(Prompt.Msgs[114]);
				bi.Level = SetPerm(bi.Level) | Consts.PostMask;
			}
			else
			{
				Clear();
				sends(Prompt.Msgs[115]);
				bi.Level = SetPerm(bi.Level) & (~Consts.PostMask);
			}
			Clear();
		}
		/*else
			bi.Level &= (~Consts.AnonyFlag);*/
		
		sends(Prompt.Msgs[116]);
		if ((bi.Anonymous = (MakeSure() == 'Y')))
		{
			sends(Prompt.Msgs[117]);
			bi.AnonyDefault = !(MakeSure() == 'N');
		}
		
		sends(Prompt.Msgs[118]);
		bi.NoZap = (MakeSure() == 'N');

		sends(Prompt.Msgs[119]);
		bi.SaveMode = (MakeSure() != 'N');
		
		sends(Prompt.Msgs[120]);
		bi.JunkBoard = (MakeSure() == 'Y');
		
		sends(Prompt.Msgs[148]);
		if(MakeSure() == 'Y')
		{
			sb = new StringBuffer(bi.Name);
			sends(Prompt.Msgs[149]);
			(GetData = new LineEdit(sb, 18, pid, true)).DoEdit();
			GetData = null;
			if (!ValidBoardName(sb))
			{
				sends(Prompt.Msgs[109]);
				PressAnyKey();
				return;
			}
			BoardNameNew = sb.toString().trim();
			if (BoardNameNew.length() != 0 && !BoardNameNew.equalsIgnoreCase(bi.Name) && ColaServer.BList.get(BoardNameNew) == null)
				ENameCh=true;
		}
		
		sends(Prompt.Msgs[163]);
		if (MakeSure() != 'Y')
		{
			sends(Prompt.Msgs[164]);
			return;
		}
		
		biorg.copyIn(bi);
		
		if (ENameCh)
		{	
			File ENameOld = new File(ColaServer.INI.BBSHome + "boards" + File.separator + bi.Name + File.separator);
			File ENameNew = new File(ColaServer.INI.BBSHome + "boards" + File.separator + BoardNameNew + File.separator);

			if (!ENameNew.exists())			
				new DelTree(ENameNew.getName());
			
			if (!ENameNew.exists() && ENameOld.exists())
			{
				int tries=0;
				while(tries < 10 && ENameOld.exists() && !ENameOld.renameTo(ENameNew))
				{
					tries++;
					TIME.Delay(100);
				}	
			}
			else
			{
				sends(Prompt.Msgs[151]);
				PressAnyKey();
				return;
			}

			ENameOld = new File(ColaServer.INI.BBSHome + "man" + File.separator + "boards" + File.separator + bi.Name + File.separator);
			ENameNew = new File(ColaServer.INI.BBSHome + "man" + File.separator + "boards" + File.separator + BoardNameNew + File.separator);
			
			if (!ENameNew.exists())			
				new DelTree(ENameNew.getName());
			
			if (!ENameNew.exists() && ENameOld.exists())
			{
				int tries=0;
				while(tries < 10 && ENameOld.exists() && !ENameOld.renameTo(ENameNew))
				{
					tries++;
					TIME.Delay(100);
				}	
			}
			ColaServer.BList.renameTo(biorg, BoardNameNew);
		}
		else
			ColaServer.BList.save(biorg);
		
		sends(Prompt.Msgs[165]);
	}
	
	public void EditUFiles()
	{
		Clear();
		sends(Prompt.Msgs[199]);
		sends(Prompt.Msgs[200]);
		sends(Prompt.Msgs[201]);
		sends(Prompt.Msgs[202]);
		sends(Prompt.Msgs[203]);
		switch(MakeSure())
		{
		case '1':
			(CurrentEditor = new Editor(pid)).DoEdit(ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator+UFD.ID + File.separator + "PLANS");
			CurrentEditor=null;
			System.gc();
			break;
		case '2':
			(CurrentEditor = new Editor(pid)).DoEdit(ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator+UFD.ID + File.separator + "signatures");
			CurrentEditor=null;
			FileInputStream SigBuf=null;
			DataInputStream SigFile=null;
			
			try
			{
				int siglines=0;
				if((new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator,"signatures")).exists())
				{
					SigBuf=new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"signatures");
					SigFile=new DataInputStream(SigBuf);
					while(SigFile.readLine()!=null)
						siglines++;
				}
				SigNum=(siglines+ColaServer.INI.MaxSigLines-1)/ColaServer.INI.MaxSigLines;
				SignFileCacheNum = -1;
				SignFileCache = null;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(SigBuf!=null)
					{
						SigBuf.close();
						SigBuf=null;
					}	
					if(SigFile!=null)
					{
						SigFile.close();
						SigFile=null;
					}	
				}
				catch(IOException e)
				{
					ColaServer.BBSlog.Write("Closing file Exception #20"+e);
				}
			}	
			System.gc();
			break;
		default:
			break;
		}
	}
	
	public synchronized void FillForm()
	{
		RandomAccessFile newreg=null;
		
		move(1,24);
		clrtoeol();
		if(UFD.ID.equalsIgnoreCase("guest"))
			return;
		if(UFD.Perm!=Perm.Basic)
			return;
		if(ColaServer.NewRegList.containsKey(new Long(UserPassItem.uid)))
			return;
		sends(Prompt.Msgs[285]);
		if(MakeSure()!='Y')
			return;
		Clear();
		SetInfo();
		sends(Prompt.Msgs[285]);
		if(MakeSure()!='Y')
			return;

		try
		{
			Long RegBuf=new Long(UserPassItem.uid);

			ColaServer.NewRegList.put(RegBuf,RegBuf);

			newreg=new RandomAccessFile(ColaServer.INI.BBSHome+"NewRegister","rw");
			newreg.seek(newreg.length());
			newreg.writeLong(UserPassItem.uid);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(newreg!=null)
				{
					newreg.close();
					newreg=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}	
	}
	public int getch()
	{
		int key;

		if(TalkRequest&&!TalkMode&&(GetData==null||(!GetData.Busy)))
		{
			int tmpx=ScX,tmpy=ScY;

			DoTalk(RequestUser);
			move(tmpx,tmpy);
		}
		else
		{
			key=getkey();
			return key;
		}
		return 0;
	}

	public int getkey()
	{
		int inkey,NowMsg=MsgNum;
		RandomAccessFile MsgFile=null;

		try
		{
			if(os!=null)
				os.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

GetKeyLoop:
		while(true)
		{
			try
			{
				int last;

				LastSig=new Date();
				if(is==null)
					return -1;
				else
					inkey=is.read();
				
				if(TalkMode&&LastSig!=null&&LastSig.getMinutes()!=(new Date()).getMinutes())
				{
					boolean ansitmp=Ansi;
					
					Ansi=true;
					move(1,24);
					sends(Prompt.Msgs[246]+STRING.Cut(ColaServer.SysDATE.DateFormatter1.format(new Date()),16)+"[m");
					Ansi=ansitmp;
				}	
				if(MsgMode==true && !InputDirectMode)       //®ø±¼°T®§~~~
				{
					boolean ansitemp=Ansi;

					switch(inkey)
					{
					case -1:
						return -1;
					case 'R'-'A'+1:
						MsgMode=false;
						MsgID=replyID;
						//MsgID = Utils.IfOnline(replyID);
						ReplyMsg();
						continue;
					case 27:
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						switch(inkey)
						{
						case -1:
							return -1;
						case (int)'[':
						case (int)'O':
							LastSig=new Date();
							if(is==null)
								return -1;
							else
								inkey=is.read();
							if( inkey >= (int)'A' && inkey <= (int)'D' )
							{
								switch(inkey)
								{
								case -1:
									return -1;
								case 'A':
									try
									{
										MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages","r");
										String MsgBuf,MsgIDBuf;

										do
										{
											NowMsg--;
											if(NowMsg<0)
											{
												NowMsg=0;
												continue GetKeyLoop;
											}
											MsgFile.seek(NowMsg*ColaServer.MsgLen);
											MsgBuf=MsgFile.readLine();
										}while(MsgBuf.length()>=7&&MsgBuf.charAt(7)=='2');
										if(MsgBuf.indexOf(' ')>=0)
											MsgIDBuf=MsgBuf.substring(12,MsgBuf.indexOf(' '));
										else if(MsgBuf.length()>=24)
											MsgIDBuf=MsgBuf.substring(12,24);
										else if(MsgBuf.length()>=12)
											MsgIDBuf=MsgBuf.substring(12,24);
										else
											MsgIDBuf="";
										MsgID=BBS.IfOnline(MsgIDBuf);
										replyID = MsgID;
										GetMsg(MsgIDBuf,MsgBuf);
									}
									catch(IOException e)
									{
										e.printStackTrace();
										continue;
									}
									catch(NullPointerException e)
									{
										e.printStackTrace();
										e.printStackTrace();
										continue;
									}
									finally
									{
										try
										{
											if(MsgFile!=null)
											{
												MsgFile.close();
												MsgFile=null;
											}	
										}
										catch(IOException e)
										{
											e.printStackTrace();
										}
									}	
									continue;
								case 'B':
									try
									{
										MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages","r");
										String MsgBuf,MsgIDBuf;
										
										MsgNum=(int)(((new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator,"Messages")).length()/ColaServer.MsgLen));

										do
										{
											NowMsg++;
											if(NowMsg>=MsgNum)
											{
												NowMsg=MsgNum-1;
												continue GetKeyLoop;
											}
											MsgFile.seek(NowMsg*ColaServer.MsgLen);
											MsgBuf=MsgFile.readLine();
										}while(MsgBuf.length()>=7&&MsgBuf.charAt(7)=='2');
										if(MsgBuf.indexOf(' ')>=0)
											MsgIDBuf=MsgBuf.substring(12,MsgBuf.indexOf(' '));
										else if(MsgBuf.length()>=24)
											MsgIDBuf=MsgBuf.substring(12,24);
										else if(MsgBuf.length()>=12)
											MsgIDBuf=MsgBuf.substring(12,24);
										else
											MsgIDBuf="";
										MsgID=BBS.IfOnline(MsgIDBuf);
										replyID = MsgID;
										GetMsg(MsgIDBuf,MsgBuf);
									}
									catch(IOException e)
									{
										e.printStackTrace();
										continue;
									}
									catch(NullPointerException e)
									{
										e.printStackTrace();
										continue;
									}
									finally
									{
										try
										{
											if(MsgFile!=null)
											{
												MsgFile.close();
												MsgFile=null;
											}	
										}
										catch(IOException e)
										{
											e.printStackTrace();
										}
									}	
									continue;
								}
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return Keys.Up + (inkey - (int)'A');
							}
							else if(inkey>=(int)'P'&&inkey<=(int)'S')
							{
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return Keys.F1+(inkey-(int)'P');
							}
							else if(inkey>=(int)'t'&&inkey<=(int)'v')
							{
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return Keys.F5+(inkey-(int)'t');
							}
							else if(inkey==(int)'l')
							{
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return Keys.F8;
							}
							else if(inkey>=(int)'w'&&inkey<=(int)'z')
							{
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return Keys.F9+(inkey-(int)'w');
							}
							else if( inkey < (int)'1' || inkey > (int)'6' )
							{
								TIME.Delay(500);
								Ansi=true;
								TelnetUtils.ReDraw(pid);
								MsgMode=false;
								Ansi=ansitemp;
								return inkey;  //¬G·N¨Sbreak
							}
							else
								return inkey;
						case (int)'1':
						case (int)'4':
							last=inkey;
							LastSig=new Date();
							if(is==null)
								return -1;
							else
								inkey=is.read();

							TIME.Delay(500);
							Ansi=true;
							TelnetUtils.ReDraw(pid);
							MsgMode=false;
							Ansi=ansitemp;

							if(inkey==(int)'~')
								return Keys.Home+ (last - (int)'1');
							return inkey;
						case (int)'@':
							TIME.Delay(500);
							Ansi=true;
							TelnetUtils.ReDraw(pid);
							MsgMode=false;
							Ansi=ansitemp;
							return Keys.Ins;
						case (int)'v':
							TIME.Delay(500);
							Ansi=true;
							TelnetUtils.ReDraw(pid);
							MsgMode=false;
							Ansi=ansitemp;
							return Keys.PgUp;
						default:
							TIME.Delay(500);
							Ansi=true;
							TelnetUtils.ReDraw(pid);
							MsgMode=false;
							Ansi=ansitemp;
							EscArg=inkey;
							return Keys.Esc;
						}
					}
					TIME.Delay(500);
					Ansi=true;
					TelnetUtils.ReDraw(pid);
					MsgMode=false;
					Ansi=ansitemp;
				}
				//´ú¸Õ¥Î....
				//                if(inkey>=(int)'a'&&inkey<=(int)'z')
				//                    System.out.println(pid+"key:"+(char)inkey+" ");
				//                else
				//                    System.out.println(pid+"key:"+inkey+" ");

				if(inkey==10||inkey==0)  //¤£ª¾¹D¬°Ô£·|¦³0¥X²{:(
				{
					if(TalkMode)
						return 10;
					else
						continue;
				}
				while(inkey==255)     //³oÃä¦b³B²zTELNET commands
				{
					LastSig=new Date();
					if(is==null||inkey==-1)
						return -1;
					else
						inkey=is.read();
					switch(inkey)
					{
					case 250:
						while(true)
						{
							LastSig=new Date();
							if(is==null)
								return -1;
							else
								inkey=is.read();
							if(inkey==255)
							{
								LastSig=new Date();
								if(is==null)
									return -1;
								else
									inkey=is.read();
								if(inkey==240)
									break;
							}
							if(inkey==-1)
								return -1;
						}
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						if(inkey==-1)
							return -1;
						break;
					case 241: //Nop
					case 243: //BRK
					case 244: //IP
					case 245: //AO
					case 246: //AYT
					case 247: //EC
					case 248: //EL
					case 249: //GA signal
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						if(inkey==-1)
							return -1;
						break;
					case 251: //WILL
					case 252: //WON'T
					case 253: //DO
					case 254: //DON'T
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							is.read();
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						if(inkey==-1)
							return -1;
						break;
					}
				}
				switch(inkey)
				{
					/*					case 'F'-'A'+1:
					if((UFD!=null)&&(DirListBuf==null)&&(usermode!=Modes.MailList))
					{
					boolean ansitemp=Ansi;
					int oldx=ScX,oldy=ScY;

					Ansi=true;
					Clear();
					DirListBuf=new MailList(pid);
					DirListBuf.DoList();
					DirListBuf=null;
					Ansi=ansitemp;
					Utils.ReDraw(pid);
					move(oldx,oldy);
					}
					break;
					case 'L'-'A'+1:
					if(UFD!=null)
					{
					int oldx=ScX,oldy=ScY;

					Utils.ReDraw(pid);
					move(oldx,oldy);
					}
					break;
					case 'B'-'A'+1:
					if((UFD!=null)&&(usermode!=Modes.List))
					{
					boolean ansitemp=Ansi;
					int oldx=ScX,oldy=ScY;

					Ansi=true;
					(new ListUsers(pid)).DoList();
					Ansi=ansitemp;
					Utils.ReDraw(pid);
					move(oldx,oldy);
					}
					break;*/
				case 'R'-'A'+1:
					if(!ReMsg/*&&(MsgID!=-1)&&MsgNum!=0*/ && !InputDirectMode)
					{
						try
						{
							//								MsgFile=new DataInputStream(new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages"));
							MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages","r");
							String MsgBuf,MsgIDBuf;
							
							NowMsg=(int)(MsgFile.length()/ColaServer.MsgLen);
							MsgNum=NowMsg;
							//								if(NowMsg==1)
							//									break;

							//								NowMsg=MsgNum+1;
							do
							{
								//									MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages","rw");
								NowMsg--;
								if(NowMsg<0)
								{
									//										MsgFile.close();
									continue GetKeyLoop;
								}
								MsgFile.seek(NowMsg*ColaServer.MsgLen);
								MsgBuf=MsgFile.readLine();
								if(MsgBuf==null)
									break;
							}while(MsgBuf.length()>=7&&MsgBuf.charAt(7)=='2');
							
							if(MsgBuf.indexOf(' ')>=0)
								MsgIDBuf=MsgBuf.substring(12,MsgBuf.indexOf(' '));
							else if(MsgBuf.length()>=24)
								MsgIDBuf=MsgBuf.substring(12,24);
							else if(MsgBuf.length()>=12)
								MsgIDBuf=MsgBuf.substring(12,24);
							else
								MsgIDBuf="";
							MsgID=BBS.IfOnline(MsgIDBuf);
							replyID = MsgID;
							GetMsg(MsgIDBuf,MsgBuf);
						}
						catch(IOException e)
						{
							e.printStackTrace();
							continue;
						}
						catch(NullPointerException e)
						{
							e.printStackTrace();
							continue;
						}
						finally
						{
							try
							{
								if(MsgFile!=null)
								{
									MsgFile.close();
									MsgFile=null;
								}	
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
						}	
						continue;
					}
					continue;
				case 127:
					return Keys.BackSpace;
				case 27:
					LastSig=new Date();
					if(is==null)
						return -1;
					else
						inkey=is.read();
					switch(inkey)
					{
					case (int)'[':
					case (int)'O':
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						if( inkey >= (int)'A' && inkey <= (int)'D' )
							return Keys.Up + (inkey - (int)'A');
						else if(inkey>=(int)'P'&&inkey<=(int)'S')
							return Keys.F1+(inkey-(int)'P');
						else if(inkey>=(int)'t'&&inkey<=(int)'v')
							return Keys.F5+(inkey-(int)'t');
						else if(inkey==(int)'l')
							return Keys.F8;
						else if(inkey>=(int)'w'&&inkey<=(int)'z')
							return Keys.F9+(inkey-(int)'w');
						else if( inkey < (int)'1' || inkey > (int)'6' )
							return inkey;  //¬G·N¨Sbreak
					case (int)'1':
					case (int)'4':
						last=inkey;
						LastSig=new Date();
						if(is==null)
							return -1;
						else
							inkey=is.read();
						if(inkey==(int)'~')
							return Keys.Home+ (last - (int)'1');
						return inkey;
					case (int)'@':
						return Keys.Ins;
					case (int)'v':
						return Keys.PgUp;
					default:
						EscArg=inkey;
						return Keys.Esc;
					}
				case 22:
					return Keys.PgDn;
				default:
					break;
				}
				return inkey;
			}
			catch( IOException e)
			{
				e.printStackTrace();
				return -1;
			}
		}
	}

	/**
	 * ³B²z±µ¨ì§O¤H°T®§ªº°Ê§@
	 */
	public void GetMsg(String senderID,String Msg)
	{
		int oldx,oldy;
		boolean ansitemp=Ansi;

		oldx=ScX;
		oldy=ScY;
		Ansi=true;

		move(1,24);
		sends("[0;1;44;36m"+STRING.Cut(senderID,Consts.IDLen)+Msg);

		MsgMode=true;
		Ansi=ansitemp;
		move(oldx,oldy);
		try
		{
			os.flush();
		}
		catch(Exception e){}
	}

/*	public void GetMsg(String Msg)
	{
		int oldx,oldy;
		boolean ansitemp=Ansi;

		oldx=ScX;
		oldy=ScY;
		Ansi=true;

		move(1,24);
		sends(Msg);

		MsgMode=true;
		Ansi=ansitemp;
		move(oldx,oldy);
		try
		{
			os.flush();
		}
		catch(Exception e){}
	}*/

	public void KickUser()
	{
		int UserBuf;

		UserBuf = OnlineNameComplete(Prompt.Msgs[93]);

		if(UserBuf == -1)
			return;

		sends(Prompt.Msgs[94] + ColaServer.BBSUsers[UserBuf].UFD.ID + Prompt.Msgs[95]);
		if(MakeSure() != 'Y')
		{
			sends(Prompt.Msgs[96] + ColaServer.BBSUsers[UserBuf].UFD.ID + Prompt.Msgs[97]);
			PressReturn();
			return;
		}
		ColaServer.BBSUsers[UserBuf].KickOut();
	}
	
	public char MakeSure()
	{
		StringBuffer AskBuf = new StringBuffer();

		GetData = new LineEdit(AskBuf, 1, pid, true);
		if (GetData.DoEdit()<0)
			return 0;
		GetData = null;
		if (AskBuf != null && AskBuf.length() > 0)
			return Character.toUpperCase(AskBuf.charAt(0));
		return 0;
	}
	
	public void ModifyInfo()
	{
		UserFileData SetUFD;
		String UserBuf;
		long uid;

		UserBuf = NameComplete(Prompt.Msgs[98]);
		if (!ColaServer.UFDList.exist(UserBuf))
			return;
		Clear();
		PassItem pi = ColaServer.UFDList.getPass(UserBuf);
		uid = pi.uid;
		DumpUserInfo(uid);
		sends(Prompt.Msgs[467]);
		if (MakeSure() != 'Y')
		{
			sends(Prompt.Msgs[100]);
			PressReturn();
			return;
		}
		SetUFD = ColaServer.UFDList.get(pi.IDItem);
		Clear();
		SetInfo(SetUFD, uid);
	}
	
	public void move(int x,int y)
	{
		boolean tmpansi = Ansi;

		Ansi = true;
		sends("[" + y + ";" + x + "H");
		ScX = x;
		ScY = y;
		Ansi = tmpansi;
	}
	
	public String NameComplete(String Msg)
	{
		boolean none,onlyone,firsttime=false;
		int inkey,num=0,locate;
		StringBuffer namebuf=new StringBuffer(),LastID=null,Match=null;
		Enumeration namelist=ColaServer.UFDList.getPassList();
		PassItem PassBuf=null;

		move(1,2);
		clrtoeol();
		sends(Msg+namebuf.toString());
		onlyone=false;
		while((inkey=getch())!=Keys.Enter)
		{
			if(inkey<0)
				return null;
			if(inkey==' ')
			{
				int min,lines;

				if(firsttime&&!onlyone)
					namelist=ColaServer.UFDList.getPassList();

				firsttime=false;
				locate=0;
				num=0;
				lines=0;
				if(!namelist.hasMoreElements())
				{
					if(onlyone)
					{
						if(Match!=null)
							namebuf=Match;
						else
							namebuf=LastID;
						move(1,2);
						clrtoeol();
						sends(Msg+namebuf.toString());
						continue;
					}
				}

				move(1,3);
				sends("[m================================== All  Users ==================================\r\n");
				clrtoeol();
				while(namelist.hasMoreElements())
				{
					PassBuf=(PassItem)namelist.nextElement();
					if(PassBuf.IDItem.length()>=namebuf.length())
						min=namebuf.length();
					else
						continue;
					if(PassBuf.IDItem.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						sends(STRING.Cut(PassBuf.IDItem,19));
						locate++;
						if(locate>3)
						{
							newline();
							clrtoeol();
							locate=0;
							lines++;
						}
						num++;
						if(num>=80)
						{
							clrtoeol();
							sends(Prompt.Msgs[261]);
							break;
						}
					}
				}
				for(;lines<19;lines++)
				{
					newline();
					clrtoeol();
				}
				if(!namelist.hasMoreElements())
				{
					newline();
					clrtoeol();
					namelist=ColaServer.UFDList.getPassList();
				}
				continue;
			}
			if((inkey>='A'&&inkey<='Z')||(inkey>='a'&&inkey<='z')||(inkey>='0'&&inkey<='9')||(inkey=='_')||(inkey==Keys.BackSpace))
			{
				int min,lines;

				if(inkey==Keys.BackSpace)
				{
					if(namebuf.length()>0)
						namebuf.setLength(namebuf.length()-1);
					else
						continue;
				}
				else
					namebuf.append((char)inkey);
				namelist=ColaServer.UFDList.getPassList();
				Match=null;
				none=true;
				onlyone=true;
				firsttime=true;
				while(namelist.hasMoreElements())
				{
					PassBuf=(PassItem)namelist.nextElement();
					if(PassBuf.IDItem.length()>=namebuf.length())
						min=namebuf.length();
					else
						continue;
					if(PassBuf.IDItem.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						if(PassBuf.IDItem.length()==namebuf.length())
							Match=new StringBuffer(PassBuf.IDItem);
						if(onlyone)
							LastID=new StringBuffer(PassBuf.IDItem);
						if(!none)
							onlyone=false;
						none=false;
					}
				}
				if(none)
				{
					onlyone=false;
					namebuf.setLength(namebuf.length()-1);
					continue;
				}
			}
			move(1,2);
			clrtoeol();
			sends(Msg+namebuf.toString());
		}
		return namebuf.toString();
	}
	
	public void newline()
	{
		byte enterbuf[]=new byte[2];

		enterbuf[0]=(byte)13;
		enterbuf[1]=(byte)10;
		sends(enterbuf);
	}
	
	private boolean NewUser()
	{
		byte writebuf[]=new byte[Consts.UserLen];
		String NewID,NewPassWord;
		StringBuffer inputbuf;

		sends(Prompt.Msgs[204]);

		if (ColaServer.UFDList.isFull())
		{
			sends(Prompt.Msgs[205]);
			sends(Prompt.Msgs[206]);
			return false;
		}

		sends(ColaServer.registerbuf);  //µù¥Uµe­±
		int ERRTime = 0;
		while(true)
		{
			ERRTime++;
			if (ERRTime > 3)
				return false;
			sends(Prompt.Msgs[208]);
			inputbuf = new StringBuffer();
			GetData=new LineEdit(inputbuf,Consts.IDLen, pid,true);
			if(GetData.DoEdit()<0)
				return false;
			GetData=null;
			if(ValidID(inputbuf))
			{
				if(ColaServer.UFDList.exist(inputbuf.toString()))
				{
					sends(Prompt.Msgs[209]);
					continue;
				}
				break;
			}
			else
				sends(Prompt.Msgs[210]);
		}
		NewID=inputbuf.toString();
		while(true)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[211]);
			GetData=new LineEdit(inputbuf,Consts.PassLen, pid,false);
			if(GetData.DoEdit()<0)
				return false;
			GetData=null;
			NewPassWord=genpasswd(inputbuf.toString());
			inputbuf.setLength(0);
			sends(Prompt.Msgs[212]);
			GetData=new LineEdit(inputbuf,Consts.PassLen,pid ,false);
			if(GetData.DoEdit()<0)
				return false;
			GetData=null;
			if(!ColaServer.Crypter.DoCrypt(inputbuf.toString(),NewPassWord).equals(NewPassWord))
			{
				sends(Prompt.Msgs[213]);
				continue;
			}
			break;
		}
		
		/*String pb = ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(NewID.charAt(0)) + File.separator + NewID +  File.separator;
		File PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
		PathBuf.mkdirs();
		
		pb = ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(NewID.charAt(0)) + File.separator + NewID +  File.separator; 
		PathBuf = new File(pb);
		if (PathBuf.exists())
			new DelTree(pb, false);
		PathBuf.mkdirs();*/
		
		/*UFD = new UserFileData();
		UFD.ID = NewID;
		UFD.PassWord = NewPassWord;
		UFD.Perm = ColaServer.INI.OrgPerm;
		ColaServer.UFDList.newUFD(UFD);
		UserPassItem = ColaServer.UFDList.getPass(NewID);*/
		UFD = ColaServer.UFDList.newUser(NewID, NewPassWord);
		UserPassItem = ColaServer.UFDList.getPass(NewID);
		ColaServer.UFDList.login(UFD);
		
		return true;
	}
	
	private int OnlineNameComplete(String Msg)
	{
		boolean none,onlyone,firsttime=false;
		int inkey,num=0,locate,index,answer,match=-1;
		StringBuffer namebuf=new StringBuffer(),LastID=null;
		PassItem PassBuf=null;
		Hashtable MyBlack=null;
		RandomAccessFile LoadFile=null;

		move(1,2);
		clrtoeol();
		sends(Msg+namebuf.toString());
		onlyone=false;
		index=0;
		answer=-1;

		try
		{
			int i;
			byte readbuf[];
			String TestID;
			String None="";

			if((new File(ColaServer.INI.BBSHome+File.separator+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID,"Myblacks")).exists())
			{
				LoadFile=new RandomAccessFile(ColaServer.INI.BBSHome+File.separator+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Myblacks","r");
				readbuf=new byte[13];

				if(LoadFile.length()!=0)
				{
					MyBlack=new Hashtable((int)(LoadFile.length()/13));

					for(i=0;i<(LoadFile.length()/13);i++)
					{
						LoadFile.read(readbuf);
						TestID=new String(readbuf,0,0,13);
						if(TestID.indexOf(0)!=-1)
							TestID=TestID.substring(0,TestID.indexOf(0));

						MyBlack.put(new Integer(ColaServer.UFDList.getPass(TestID).uid),None);
					}
				}
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(LoadFile!=null)
				{
					LoadFile.close();
					LoadFile=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}			

		while((inkey=getch())!=Keys.Enter)
		{
			if(inkey<0)
				return -1;
			if(inkey==' ')
			{
				int min,lines;

				if(firsttime&&!onlyone)
					index=0;

				firsttime=false;
				locate=0;
				num=0;
				lines=0;
				if(index>=ColaServer.onlineuser)
				{
					if(onlyone)
					{
						namebuf=LastID;
						move(1,2);
						clrtoeol();
						sends(Msg+namebuf.toString());
						continue;
					}
				}

				move(1,3);
				sends("================================= Online Users =================================\r\n");
				clrtoeol();
				while(index<ColaServer.onlineuser)
				{
					if(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID.length()>=namebuf.length())
						min=namebuf.length();
					else
						continue;

					if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[index]].Visible||(MyBlack!=null&&MyBlack.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[index]].uid)))))
					{
						index++;
						continue;
					}

					if(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						sends(STRING.Cut(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID,19));
						locate++;
						if(locate>3)
						{
							newline();
							clrtoeol();
							locate=0;
							lines++;
						}
						num++;
						if(num>=80)
						{
							clrtoeol();
							sends(Prompt.Msgs[261]);
							break;
						}
					}
					index++;
				}
				for(;lines<19;lines++)
				{
					newline();
					clrtoeol();
				}
				if(index>=ColaServer.onlineuser)
				{
					newline();
					clrtoeol();
					index=0;
				}
				continue;
			}
			answer=-1;
			if((inkey>='A'&&inkey<='Z')||(inkey>='a'&&inkey<='z')||(inkey>='0'&&inkey<='9')||(inkey=='_')||(inkey==Keys.BackSpace))
			{
				int min,lines;

				if(inkey==Keys.BackSpace)
				{
					if(namebuf.length()>0)
						namebuf.setLength(namebuf.length()-1);
					else
						continue;
				}
				else
					namebuf.append((char)inkey);
				index=0;
				match=-1;
				none=true;
				onlyone=true;
				firsttime=true;
				while(index<ColaServer.onlineuser)
				{
					if(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID.length()>=namebuf.length())
						min=namebuf.length();
					else
					{
						index++;
						continue;
					}

					if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(!ColaServer.BBSUsers[ColaServer.SortedUser[index]].Visible||(MyBlack!=null&&MyBlack.containsKey(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[index]].uid)))))
					{
						index++;
						continue;
					}

					if(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID.substring(0,min).equalsIgnoreCase(namebuf.toString().substring(0,min)))
					{
						if(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID.length()==namebuf.length())
							match=index;
						answer=index;
						if(onlyone)
							LastID=new StringBuffer(ColaServer.BBSUsers[ColaServer.SortedUser[index]].UFD.ID);
						if(!none)
							onlyone=false;
						none=false;
					}
					index++;
				}
				if(none)
				{
					onlyone=false;
					namebuf.setLength(namebuf.length()-1);
					continue;
				}
			}
			move(1,2);
			clrtoeol();
			sends(Msg+namebuf.toString());
		}
		if(answer!=-1)
		{
			if(match!=-1)
				return ColaServer.SortedUser[match];
			else
				return ColaServer.SortedUser[answer];
		}	
		return -1;
	}
	
	public void PressAnyKey()
	{
		sends(Prompt.Msgs[235]);
		getch();
	}
	
	public void PressReturn()
	{
		int key;

		sends(Prompt.Msgs[244]);
		while((key=getch())!=Keys.Enter)
			if(key<=0)
				return;
	}
	
	public void printendline()
	{
		int stayhr,staymin,oldx=ScX,oldy=ScY;
		Date now=new Date();
		char pch='p',mch='m';

		if((UFD.UserDefine&Consts.FriendPager)!=0)
		{
			if((UFD.UserDefine&Consts.AllPager)!=0)
				pch='P';
			else
				pch='p';
		}
		if((UFD.UserDefine&Consts.FriendMsgPager)!=0)
		{
			if((UFD.UserDefine&Consts.AllMsgPager)!=0)
				mch='M';
			else
				mch='m';
		}

		stayhr=(now.getDate()-LoginTime.getDate())*24+now.getHours()-LoginTime.getHours();
		staymin=now.getMinutes()-LoginTime.getMinutes();
		if(staymin<0)
		{
			staymin+=60;
			stayhr--;
		}

		move(1,24);

		sends(Prompt.Msgs[246]+STRING.Cut(ColaServer.SysDATE.DateFormatter1.format(now),16)+Prompt.Msgs[247]+pch+mch+Prompt.Msgs[248]+STRING.Cut(UFD.ID,13)+Prompt.Msgs[249]+STRING.CutLeft(""+stayhr,2)+"[33m:[36m"+STRING.CutLeft(""+staymin,2)+"[33m]              [m");
		move(oldx,oldy);
	}
	
	public void printtitle(String title)
	{
		int oldx=ScX,oldy=ScY;

		if(hasNewMail())
			sends("[1;33;44m"+title+"[5;37m"+STRING.CutLeft(Prompt.Msgs[309],40-title.length()+Prompt.Msgs[309].length()/2)+"[m[1;33;44m"+STRING.CutLeft(Prompt.Msgs[154]+ CurrentBoard,38-Prompt.Msgs[309].length()/2)+"] [m\r\n");
		else
			sends("[1;33;44m"+title+STRING.CutLeft(ColaServer.INI.BBSName,40-title.length()+ColaServer.INI.BBSName.length()/2)+STRING.CutLeft(Prompt.Msgs[154]+CurrentBoard,38-ColaServer.INI.BBSName.length()/2)+"] [m\r\n");
		printendline();
	}
	
	public void printtitle(String title,boolean endline)
	{
		int oldx=ScX,oldy=ScY;
		if(hasNewMail())
			sends("[1;33;44m"+title+"[5;37m"+STRING.CutLeft(Prompt.Msgs[309],40-title.length()+Prompt.Msgs[309].length()/2)+"[m[1;33;44m"+STRING.CutLeft(Prompt.Msgs[154]+CurrentBoard,38-Prompt.Msgs[309].length()/2)+"] [m\r\n");
		else
			sends("[1;33;44m"+title+STRING.CutLeft(ColaServer.INI.BBSName,40-title.length()+ColaServer.INI.BBSName.length()/2)+STRING.CutLeft(Prompt.Msgs[154]+CurrentBoard,38-ColaServer.INI.BBSName.length()/2)+"] [m\r\n");
		if(endline)
			printendline();
		move(oldx,oldy);
	}
	
	public void printtitle(String script,String title)
	{
		int oldx=ScX,oldy=ScY;
		if(hasNewMail())
			sends("[1;33;44m"+title+"[5;37m"+STRING.CutLeft(Prompt.Msgs[309],40-title.length()+Prompt.Msgs[309].length()/2)+"[m[1;33;44m"+STRING.CutLeft(Prompt.Msgs[154]+CurrentBoard,38-Prompt.Msgs[309].length()/2)+"] [m\r\n");
		else
			sends("[1;33;44m"+title+STRING.CutLeft(ColaServer.INI.BBSName,40-title.length()+ColaServer.INI.BBSName.length()/2)+STRING.CutLeft(Prompt.Msgs[154]+CurrentBoard,38-ColaServer.INI.BBSName.length()/2)+"] [m\r\n");
		printendline();
		move(oldx,oldy);
	}
	
	private void ReplyMsg()
	{
		boolean ansitemp=Ansi;
		StringBuffer msgbuf=new StringBuffer();
		String OrgMode=usermode;
		int oldx,oldy;

		BBS.SetUserMode(pid,Modes.ReplyMsg);
		Ansi=true;
		oldx=ScX;
		oldy=ScY;

		ReMsg=true;
		if(MsgID==-1||ColaServer.BBSUsers[MsgID].UserLogout==true)
		{
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[217]);
			BBS.SetUserMode(pid,OrgMode);
			move(oldx,oldy);
			Ansi=ansitemp;
			ReMsg=false;
			return;
		}
		if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[MsgID].UFD.UserDefine&Consts.AllMsgPager)==0)
		{
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[216]);
			try
			{
				os.flush();
			}
			catch(Exception e){}
			TIME.Delay(1000);
			printendline();
			BBS.SetUserMode(pid,OrgMode);
			move(oldx,oldy);
			Ansi=ansitemp;
			ReMsg=false;
			return;
		}
		move(1,24);
		clrtoeol();
		sends(Prompt.Msgs[222]+ColaServer.BBSUsers[MsgID].UFD.ID+": ");

		GetData=new LineEdit(msgbuf,50,pid ,true);
		GetData.DoEdit();
		GetData=null;
		if(msgbuf.equals(""))
		{
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[219]);
			try
			{
				os.flush();
			}
			catch(Exception e){}
			TIME.Delay(1000);
		}
		else
		{
			Date now=new Date();

			SendMsg(MsgID,msgbuf.toString());
		}
		ReMsg=false;
		printendline();
		BBS.SetUserMode(pid,OrgMode);
		Ansi=ansitemp;
		move(oldx,oldy);
	}
	
	public void run()
	{
		try
		{
			Clear();

			sends(Prompt.Msgs[124]+STRING.Cut(ColaServer.INI.BBSName+Prompt.Msgs[175]+"[32m   ([31mCola[33mBBS [35mby [36m\u00a7\u0064\u00ad\u005e\u00bb\u00a8[32m)   [37m"+System.getProperty("os.name")+" "+System.getProperty("os.arch")+" "+System.getProperty("os.version"),113)+"[m\r\n");
			//				sends("[1;33m[44m\u00a5\u00bb\u00b5\u007b\u00a6\u00a1\u00aa\u00a9\u00c5\u0076\u00c4\u00dd\u00a9\u00f3\u00b5\u007b\u00a6\u00a1\u00b5\u006f\u00ae\u0069\u00aa\u00cc [32m\u00a5\u00e6\u00b3\u0071\u00a4\u006a\u00be\u00c7\u00b8\u00ea\u00ac\u00ec\u00a8\u0074 [36m\u00a7\u0064\u00ad\u005e\u00bb\u00a8 [37mmailto://u8523003@cc.nctu.edu.tw [m\r\n");
			sends("[1;33m[44m Copyright(C) by [36mYing-haur Wu [32mAll rights reserved. [37mat infoX Studio and NCTUCIS  [m\r\n");

			/*if(issuex!=-1)
			{
				//fastsends(ColaServer.issuebuf);  //¶i¯¸µe?
				fastsends(ColaServer.Issues.getString().getBytes());
				move(issuex,issuey);
			}
			else
			{*/
				//sends(ColaServer.issuebuf);  //¶i¯¸µe?
				sends(ColaServer.Issues.getString());
				issuex=ScX;
				issuey=ScY;
			//}

			sends(Prompt.Msgs[218]+ColaServer.INI.BBSName+"[1;32m["+ColaServer.INI.BBSAddress+Prompt.Msgs[79]+ColaServer.onlineuser+Prompt.Msgs[166]);

			int attempts=0;

			while(true)
			{
				StringBuffer idbuf=new StringBuffer();

				if(attempts>=ColaServer.INI.LoginAttempts)
				{
					os.flush();
					is.close();
					os.close();
					BBSSocket.close();

					ColaServer.BBSlog.Write(pid+" login failure from "+from);
					UserLogout=true;
					return;
				}
				sends(Prompt.Msgs[75]);
				idbuf.setLength(0);
				if((GetData=new LineEdit(idbuf,Consts.IDLen, pid,true)).DoEdit()<0)
					return;
				GetData=null;

				//  ³oÃä­n°µ´M§ä¨Ï¥ÎªÌªº°Ê§@!!
				UserPassItem=ColaServer.UFDList.getPass(idbuf.toString());

				//Change by WilliamWey
				//if(idbuf.toString().equalsIgnoreCase("new")&&ColaServer.INI.LoginAsNew)
				if(idbuf.toString().equalsIgnoreCase("new"))
					//
				{
					if(ColaServer.INI.LoginAsNew)
					{
						if(NewUser())
						{
							UFD.Perm=(int)Perm.Basic;
							UFD.UserDefine=~(int)0;
							break;
						}
						attempts=ColaServer.INI.LoginAttempts;
					}
					else
						sends(Prompt.Msgs[193]);
					continue;
				}
				else if(UserPassItem==null)
				{
					sends(Prompt.Msgs[42]);
					attempts++;
					continue;
				}
				else if(UserPassItem.IDItem.equalsIgnoreCase("SYSOP"))
				{
					idbuf.setLength(0);

					sends(Prompt.Msgs[266]);
					(GetData=new LineEdit(idbuf,Consts.PassLen,pid ,false)).DoEdit();
					GetData=null;
					if(checkpasswd( idbuf.toString(), UserPassItem.PassWordItem ))  //´ú¸Õ±K½X
					{
						UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
						UFD.Perm=~(int)0;
						uid=UserPassItem.uid;
						break;
					}
					else
					{
						ColaServer.BBSlog.Write(from+" answer incorrect password for SYSOP....");
					}	
				}
				else if(!UserPassItem.IDItem.equalsIgnoreCase("guest"))
				{
					idbuf.setLength(0);

					sends(Prompt.Msgs[266]);
					(GetData=new LineEdit(idbuf,Consts.PassLen, pid,false)).DoEdit();
					GetData=null;
					if(checkpasswd( idbuf.toString(), UserPassItem.PassWordItem ))  //´ú¸Õ±K½X
					{
						UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
						uid=UserPassItem.uid;
						break;
					}
					else
					{
						ColaServer.BBSlog.Write(from+" answer incorrect password for "+UserPassItem.IDItem);
					}
				}
				else
				{
					if(ColaServer.INI.UseGuest)
					{
						UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
						uid=UserPassItem.uid;
						break;
					}
					//Add by WilliamWey
					sends(Prompt.Msgs[447]);
					continue;
					//
				}
				sends(Prompt.Msgs[223]);
				attempts++;
			}
			
			ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" : "+UFD.ID+" login!");
			if (UFD.brclist == null)
				UFD.loadBoardRC();
			ColaServer.ReSortAddUser(pid);

			ColaServer.EHList.doHandle("TELNET_LOGIN", this);
			
			LoginTime=new Date();
			MsgMode=false;
			ReMsg=false;
			TalkRequest=false;
			TalkMode=false;
			Ansi=true;
			ReDrawMode=0;
			attempts=0;
			
			UFD.NumLogins++;
			UFD.LastLogin=(new Date()).getTime();
			UFD.LastHost = NET.GetHostName(from);
			
			if((UFD.Flags[0]&Consts.UserFlag_Cloak&0xff)!=0)
				Visible=false;
			else
				Visible=true;

			if((new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator,"signatures")).exists())
			{
				FileInputStream SigBuf=null;
				BufferedReader SigFile=null;
				
				try
				{
					SigBuf=new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"signatures");
					SigFile=new BufferedReader(new InputStreamReader(SigBuf,colabbs.ColaServer.INI.Encoding));
					while(SigFile.readLine()!=null)
						attempts++;
				}	
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(SigBuf!=null)
						{
							SigBuf.close();
							SigBuf=null;
						}	
						if(SigFile!=null)
						{
							SigFile.close();
							SigFile=null;
						}	
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			SigNum=(attempts+ColaServer.INI.MaxSigLines-1)/ColaServer.INI.MaxSigLines;
			if(ColaServer.INI.UseNotepad)
			{
				int linetemp=0;

				switch(UFD.NoteMode)
				{
				case 2:
					Clear();
					if((UFD.LastLogin/1000)<ColaServer.NowNote.longValue())
						UFD.NoteLine=0;
					if((linetemp=ansimore(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad",UFD.NoteLine,true))>0)
					{
						UFD.NoteLine=linetemp;
						getch();
					}
					break;
				case 1:
					if((ansimore(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad",0,true))>0)
					{
						getch();
					}
					break;
				default:
					break;
				}
			}

			//Add by William for ¤W¯¸«áªº¤@°ïµe­±
			int lvn = ColaServer.LoginView.list.size();
			int lvi;
			String lvFN;
			for(lvi = 0; lvi < lvn; lvi++)
			{
				lvFN = ColaServer.INI.BBSHome + (String)ColaServer.LoginView.list.elementAt(lvi);
				if ((new File(lvFN)).exists())
				{
					Clear();
					ansimore(lvFN);
					PressAnyKey();
				}
			}
			lvFN = null;
			//
			
			if(!BBS.HasOnePerm(pid,Perm.LoginOK)&&!UFD.ID.equalsIgnoreCase("guest"))
			{
				Clear();
				ansimore(ColaServer.INI.BBSHome+"etc"+File.separator+"beginner");
				PressAnyKey();
			}

			TelnetUserLogin();
			if(BBS.HasOnePerm(pid,Perm.Account))
			{
				File newregfile=new File(ColaServer.INI.BBSHome,"NewRegister");

				if(newregfile.exists()&&newregfile.length()!=0)
				{
					Clear();
					sends(Prompt.Msgs[310]+"\r\n");
					PressReturn();
				}
			}

			BBSMainMenu=new MainMenu(pid);

BBSMenuLoop:
			while(true)
			{
				int SYSOPindex;

				//Add by WilliamWey
				usermode = Modes.Logout;
				//
				BBSMainMenu.DoMenu();
				Clear();
				sends(Prompt.Msgs[63]+ColaServer.INI.BBSName+Prompt.Msgs[41]);
				sends(Prompt.Msgs[265]);
				sends(Prompt.Msgs[62]);
				if(BBS.HasOnePerm(pid,Perm.Post))
					sends(Prompt.Msgs[40]);
				sends(Prompt.Msgs[50]);
				sends(Prompt.Msgs[67]);
				switch(MakeSure())
				{
				case '2':
					break;
				case '1':
					//Add by WilliamWey
					usermode = Modes.MailToSysop;
					//
					char whichsysop;

					Clear();
					sends(Prompt.Msgs[269]);
					sends("   ============ =============\r\n");
					for(SYSOPindex=0;SYSOPindex<ColaServer.SYSOPS1.length;SYSOPindex++)
						sends("[m[[33m"+(char)(SYSOPindex+'1')+"[m]"+STRING.Cut(ColaServer.SYSOPS1[SYSOPindex],Consts.IDLen)+ColaServer.SYSOPS2[SYSOPindex]+"\r\n");
					sends("[m[[33m"+(char)(SYSOPindex+'1')+Prompt.Msgs[221]);
					sends(Prompt.Msgs[64]+(char)(SYSOPindex+'1')+"[36m]:");
					if((whichsysop=MakeSure())>=(SYSOPindex+'1')||(whichsysop==0))
						break BBSMenuLoop;
					DoMail(ColaServer.SYSOPS1[(whichsysop-'1')]);
					break;
				case '3':
					//Add by WilliamWey
					usermode = Modes.WriteNote;
					//
					if(BBS.HasOnePerm(pid,Perm.Post))
					{
						int i;
						StringBuffer Messages[]=new StringBuffer[3];

						Clear();
						sends(Prompt.Msgs[268]);
						for(i=0;i<3;i++)
						{
							Messages[i]=new StringBuffer();
							sends("\r\n:");
							(GetData=new LineEdit(Messages[i],76, pid,true)).DoEdit();
							GetData=null;
							if(Messages[i].length()==0)
								break;
						}
						if(Messages[0].length()==0)
							break;
						synchronized(ColaServer.NowNote)
						{
							int tries;
							RandomAccessFile NotePad=null,OldNote=null;
							File NoteLock=new File(ColaServer.INI.BBSHome+"etc","notepad.L");
							File NoteFile=new File(ColaServer.INI.BBSHome+"etc","notepad");
							
							try
							{
								byte NoteWriteBuf[];

								if(NoteFile.exists())
								{
									tries=0;
									while(tries<10&&NoteLock.exists()&&!NoteLock.delete())
									{
										TIME.Delay(100);
										tries++;
									}	
									tries=0;
									while(tries<10&&NoteFile.exists()&&(!NoteFile.renameTo(NoteLock)))
									{
										TIME.Delay(100);
										tries++;
									}	
									OldNote=new RandomAccessFile(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad.L","r");
									NoteWriteBuf=new byte[(int)OldNote.length()];
									OldNote.read(NoteWriteBuf);
								}
								else
									NoteWriteBuf=null;

								NotePad=new RandomAccessFile(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad","rw");

								NotePad.writeBytes(Prompt.Msgs[66]+STRING.Cut(UFD.ID,Consts.IDLen)+"[37m"+STRING.Cut(" ("+UFD.NickName+")",32-Consts.IDLen)+Prompt.Msgs[47]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),19)+Prompt.Msgs[44]);
								//										NotePad.writeBytes(Prompt.Msgs[66]+STRING.Cut(UFD.ID,Consts.IDLen)+"[37m"+STRING.Cut(" ("+UFD.NickName+")",32-Consts.IDLen)+Prompt.Msgs[47]+STRING.Cut((new Date()).toString(),19)+Prompt.Msgs[44]);
								for(i=0;i<3;i++)
									if(Messages[i]!=null&&Messages[i].length()!=0)
										NotePad.writeBytes(Prompt.Msgs[45]+STRING.Cut(Messages[i].toString(),76)+Prompt.Msgs[157]);
								NotePad.writeBytes(Prompt.Msgs[156]);
								if(NoteWriteBuf!=null)
									NotePad.write(NoteWriteBuf);
							}
							catch(IOException e)
							{
								e.printStackTrace();
							}
							finally
							{
								try
								{
									if(NotePad!=null)
									{
										NotePad.close();
										NotePad=null;
									}	
									if(OldNote!=null)
									{
										OldNote.close();
										OldNote=null;
									}	
								}
								catch(IOException e)
								{
									e.printStackTrace();
								}
								tries=0;
								while(tries<10&&NoteLock.exists()&&!NoteLock.delete())
								{
									TIME.Delay(100);
									tries++;
								}	
								System.gc();
							}
						}
					}
				default:
					break BBSMenuLoop;
				}
			}
			Clear();
			DumpUserInfo(UserPassItem.uid);
			LogoutSave();
			sends(Prompt.Msgs[76]+ColaServer.INI.BBSName+Prompt.Msgs[245]);
			PressReturn();
			sends("[m");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			GoodBye();
		}
	}
	
	public void RunPlasmid(String PlasmidName)
	{
		try
		{
			int seeks = PlasmidName.indexOf("@");
			String Param = null;
			if (seeks != -1)
			{
				Param = PlasmidName.substring(seeks + 1).trim();
				PlasmidName = PlasmidName.substring(0, seeks).trim();
			}
			
			if (ColaServer.APlasmid.exist(PlasmidName))
			{
				if (Param == null)
					ColaServer.APlasmid.Run(PlasmidName, (BBSUser)this);
				else
					ColaServer.APlasmid.Run(PlasmidName, (BBSUser)this, Param);
			}
		}
		catch(Exception e)
		{
			System.out.print("RunPlasmid [");
			System.out.print(PlasmidName);
			System.out.print("] Error:");
			System.out.println(e.toString());
		}
	}
	
	public void ViewFile(String FileName)
	{
		if ((new File(ColaServer.INI.BBSHome + FileName)).exists())
		{
			ansimore(ColaServer.INI.BBSHome + FileName);
			PressAnyKey();
		}
	}
	
	public void EditFile(String FileName)
	{
		(CurrentEditor = new Editor(pid)).DoEdit(ColaServer.INI.BBSHome + FileName);
		CurrentEditor = null;			
	}
	
	/*public void SaveBrc()
	{
		if(!UserPassItem.BRCflag||UserPassItem.BRC==null)
			return;
		
		synchronized(UserPassItem.BRC)
		{
			File BrcFile=new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID,".boardrc");
			RandomAccessFile brc=null;
			
			try
			{
				try
				{
				}
				finally
				{
					byte databuf[];
					int looptimes;
					Enumeration list=UserPassItem.BRC.keys();
					
					int tries=0;
					while(tries<10&&BrcFile.exists()&&!BrcFile.delete())
					{
						TIME.Delay(100);
						tries++;
					}	

					brc=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+".boardrc","rw");
					
					while(list.hasMoreElements())
					{
						String BoardName=(String)list.nextElement();
						Vector brctemp=(Vector)UserPassItem.BRC.get(BoardName);
						byte brcnum=(byte)(brctemp.size()&0xff);
						if(brcnum>0)
						{
							databuf=new byte[16];

							if(BoardName.length()<15)
								BoardName.getBytes(0,BoardName.length(),databuf,0);
							else
								BoardName.getBytes(0,15,databuf,0);
							databuf[15]=brcnum;
							brc.write(databuf);
							for(looptimes=0;looptimes<brcnum;looptimes++)
								brc.writeLong(((Long)brctemp.elementAt(looptimes)).longValue());
						}
					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return;
			}
			finally
			{
				try
				{
					if(brc!=null)
					{
						brc.close();
						brc=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				UserPassItem.BRCflag=false;
				UserPassItem.BRC=null;
			}
		}
	}*/

	public void sendEMailAll() 
	{
		char QuoteMode='Y';
		String FNBuf=null;
		StringBuffer TitleBuf=new StringBuffer(Prompt.Msgs[262]);
		StringBuffer Ans=new StringBuffer();
		String SaveFileName,ModeBuf;

		ModeBuf=usermode;
		BBS.SetUserMode(pid,Modes.Mail);

		Clear();

		move(1,23);
		clrtoeol();
		sends(Prompt.Msgs[30]);
		(GetData=new LineEdit(TitleBuf,50, pid,true)).DoEdit();
		GetData=null;
		if(TitleBuf.length()==0)
			return;
		while(true)
		{
			Ans.setLength(0);
			move(1,21);
			clrtoeol();
			sends(Prompt.Msgs[264]+"All User's Email[m\r\n");
			clrtoeol();
			sends(Prompt.Msgs[24]+TitleBuf.toString()+"[m\r\n");
			clrtoeol();
			sends(Prompt.Msgs[26]+SigNum+Prompt.Msgs[267]);
			clrtoeol();
			sends(Prompt.Msgs[31]);
			sends(Prompt.Msgs[34]+UFD.signature+Prompt.Msgs[35]);
			if((GetData=new LineEdit(Ans,2, pid,true)).DoEdit()<0)
				return;
			GetData=null;
			if(Ans.length()==0)
				break;
			if(Ans.charAt(0)>='0'&&Ans.charAt(0)<='9')
			{
				try
				{
					UFD.signature=(byte)Integer.parseInt(Ans.toString());
				}
				catch(NumberFormatException e)
				{
					Bell();
				}
				finally
				{
					if(UFD.signature>SigNum)
						UFD.signature=0;
					continue;
				}
			}
			if(Ans.charAt(0)=='t'||Ans.charAt(0)=='T')
			{
				move(1,23);
				clrtoeol();
				sends(Prompt.Msgs[30]);
				(GetData=new LineEdit(TitleBuf,50, pid,true)).DoEdit();
				GetData=null;
				if(TitleBuf.length()==0)
					return;
				continue;
			}
		}
		SaveFileName=(CurrentEditor=new Editor(pid)).DoEdit(TitleBuf.toString(),ColaServer.INI.BBSHome+"mailtemp"+File.separator,UFD.signature);
		CurrentEditor=null;
		System.gc();
		if(SaveFileName!=null)
		{
			String MailOwner=UFD.ID+".bbs@"+ColaServer.INI.BBSAddress;
			RandomAccessFile inputfile=null;
			inputfile=null;
			byte temp[]=null;
			
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[434]);
			flush();
			try
			{
				inputfile=new RandomAccessFile(ColaServer.INI.BBSHome+"mailtemp"+File.separator+SaveFileName,"r");
				int len=(int)inputfile.length();
				temp=new byte[len];
				inputfile.read(temp);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(inputfile!=null)
						inputfile.close();
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
			}
			if(temp!=null)
			{
				for(Enumeration allUsers=ColaServer.UFDList.getPassList();allUsers.hasMoreElements();)
				{
					UserFileData tmpUFD = ColaServer.UFDList.get(((PassItem)allUsers.nextElement()).uid);
					if(tmpUFD.Email.indexOf('@')!=-1&&tmpUFD.Email.indexOf(".bbs@")==-1)
					{
						ByteArrayInputStream bis=new ByteArrayInputStream(temp);
						doInternetMail(MailOwner,tmpUFD.Email,bis,TitleBuf.toString());
					}
				}
			}
			
			int tries=0;
			while(tries<10&&!(new File(ColaServer.INI.BBSHome+"mailtemp",SaveFileName)).delete())
			{
				TIME.Delay(100);
				tries++;
			}	
		}
		BBS.SetUserMode(pid,ModeBuf);
	}

	public void SendMsg(int index, String Msg)
	{
		SendMsg(index, Msg, true);
	}

	public void SendMsg(int index,String Msg, boolean EchoFlag)
	{
		int oldx,oldy;
		String OrgMode=usermode;

		BBS.SetUserMode(pid,Modes.SendMsg);
		oldx=ScX;
		oldy=ScY;

		if(index==-1)
		{
			StringBuffer pidbuf=new StringBuffer();
			int i;

			index=OnlineNameComplete(Prompt.Msgs[215]);
			if(index==-1)
			{
				move(oldx,oldy);
				return;
			}
			if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllMsgPager)==0)
			{
				if(((ColaServer.BBSUsers[index].UFD.UserDefine&Consts.FriendMsgPager)==0)||(!FriendList.isFriend(UserPassItem.IDItem,UFD.ID,ColaServer.BBSUsers[index].UFD.ID)))
				{
					move(1,24);
					clrtoeol();
					sends(Prompt.Msgs[216]);
					try
					{
						os.flush();
					}
					catch(Exception e){}
					TIME.Delay(1000);
					printendline();
					BBS.SetUserMode(pid,OrgMode);
					move(oldx,oldy);
					return;
				}
			}
		}

		if(ColaServer.BBSUsers[index].UserLogout==true)
		{
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[217]);
			try
			{
				os.flush();
			}
			catch(Exception e){}
			TIME.Delay(1000);
			printendline();
			BBS.SetUserMode(pid,OrgMode);
			move(oldx,oldy);
			return;
		}
		if(Msg==null)
		{
			StringBuffer msgbuf=new StringBuffer();

			BBS.SetUserMode(pid,Modes.KeyMsg);

			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[196]);

			GetData=new LineEdit(msgbuf,50, pid ,true);
			GetData.DoEdit();
			GetData=null;
			Msg=msgbuf.toString();
		}
		if(Msg.equals(""))
		{
			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[219]);
			try
			{
				os.flush();
			}
			catch(Exception e){}
			TIME.Delay(1000);
		}
		else
		{
			Date now=new Date();
			RandomAccessFile MsgFile=null,OwnMsgFile=null;

			ColaServer.BBSUsers[index].replyID=pid; //¼È®É´ú¸Õ¥Î
			//			ColaServer.BBSUsers[index].GetMsg("[0;1;44;36m"+STRING.Cut(UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.CutLeft(""+now.getHours(),2)+":"+STRING.CutLeft(""+now.getMinutes(),2)+"[33m):[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]);
			///			((TelnetUser)ColaServer.BBSUsers[index]).GetMsg("[0;1;44;36m"+STRING.Cut(UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.Cut(ColaServer.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]);
			if(ColaServer.BBSUsers[index] instanceof TelnetUser)
				ColaServer.BBSUsers[index].GetMsg(UFD.ID,"[0;1;44;36m"+STRING.Cut(UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]);
			else
				ColaServer.BBSUsers[index].GetMsg(UFD.ID,STRING.Cut(Msg,50));
//				ColaServer.BBSUsers[index].GetMsg(UFD.ID,STRING.Cut(UFD.ID,Consts.IDLen)+"("+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"):"+STRING.Cut(Msg,50));
			if(ColaServer.BBSUsers[index] instanceof TelnetUser)
				((TelnetUser)ColaServer.BBSUsers[index]).Bell();

			try
			{
				MsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(ColaServer.BBSUsers[index].UFD.ID.charAt(0))+File.separator+ColaServer.BBSUsers[index].UFD.ID+File.separator+"Messages","rw");

				MsgFile.seek(MsgFile.length());
				//				MsgFile.writeBytes("[0;1;44;36m"+STRING.Cut(UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.CutLeft(""+now.getHours(),2)+":"+STRING.CutLeft(""+now.getMinutes(),2)+"[33m):[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]+"\r\n");
				MsgFile.writeBytes("[0;1;44;36m"+STRING.Cut(UFD.ID,Consts.IDLen)+"[33m([36m"+STRING.Cut(ColaServer.SysDATE.DateFormatter4.format(now),5)+"[33m):[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]+"\r\n");
				//				MsgFile.close();
				//Add by WilliamWey
				//				MsgFile.close(); //remove by yhwu because close at finally
				//
				ColaServer.BBSUsers[index].MsgNum++;
				if(index!=pid)
				{
					OwnMsgFile=new RandomAccessFile(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID+File.separator+"Messages","rw");

					OwnMsgFile.seek(OwnMsgFile.length());
					//					OwnMsgFile.writeBytes("[0;1;42;36m SendTo [1;33m"+STRING.Cut(ColaServer.BBSUsers[index].UFD.ID,Consts.IDLen)+"[44;33m[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]+"\r\n");
					OwnMsgFile.writeBytes("[0;1;42;36m SendTo [1;33m"+STRING.Cut(ColaServer.BBSUsers[index].UFD.ID,Consts.IDLen)+"[44;33m[1;37;44m"+STRING.Cut(Msg,50)+Prompt.Msgs[159]+"\r\n");
					//					OwnMsgFile.close();
					//Add by WilliamWey
					//					OwnMsgFile.close();	//remove by yhwu because close at finally
					//
					MsgNum++;

					//Add by WilliamWey
					if (EchoFlag)
					{
						move(1,24);
						clrtoeol();
						sends(Prompt.Msgs[220]);
					}	
					//
					try
					{
						os.flush();
					}
					catch(Exception e){}
				}
				if(ColaServer.BBSUsers[index] instanceof TelnetUser)
				{
					if(ColaServer.BBSUsers[index].MsgGetKey)
						((TelnetUser)ColaServer.BBSUsers[index]).getch();
					else if(index!=pid)
						ColaServer.BBSUsers[index].suspend();
					TIME.Delay(1000);
					if(!ColaServer.BBSUsers[index].MsgGetKey&&index!=pid)
						ColaServer.BBSUsers[index].resume();
				}
			}
			catch(IOException e)
			{
				move(1,24);
				clrtoeol();
				sends(Prompt.Msgs[323]+e);
				try
				{
					os.flush();
				}
				catch(Exception e1){}

				TIME.Delay(1000);
				printendline();
				BBS.SetUserMode(pid,OrgMode);
				move(oldx,oldy);
				return;
			}
			finally
			{
				try
				{
					if(MsgFile!=null)
					{
						MsgFile.close();
						MsgFile=null;
					}	
					if(OwnMsgFile!=null)
					{
						OwnMsgFile.close();
						OwnMsgFile=null;
					}	
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		printendline();
		BBS.SetUserMode(pid,OrgMode);
		move(oldx,oldy);
		try
		{
			os.flush();
		}
		catch(Exception e){}
	}
	
	public void SendMsgToAll(String Msg)
	{
		if(Msg == null)
		{
			StringBuffer msgbuf = new StringBuffer();

			BBS.SetUserMode(pid, Modes.KeyMsg);

			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[196]);

			GetData = new LineEdit(msgbuf, 50, pid, true);
			GetData.DoEdit();
			GetData = null;
			Msg = msgbuf.toString();
		}
		if(Msg.equals(""))
		{
			move(1, 24);
			clrtoeol();
			sends(Prompt.Msgs[219]);
			try
			{
				os.flush();
			}
			catch(Exception e) {}
			TIME.Delay(100);
		}
		else
		{
			for (int i = 0; i < ColaServer.onlineuser; i++)
				if (ColaServer.SortedUser[i] != pid)
					SendMsg(ColaServer.SortedUser[i], Msg, false);
			move(1, 24);
			clrtoeol();
			sends(Prompt.Msgs[220]);
			TIME.Delay(100);
			printendline();
			return;
		}
	}
	//Add by WilliamWey
	public void SendMsgToFriends(String Msg)
	{
		if(Msg == null)
		{
			StringBuffer msgbuf = new StringBuffer();

			BBS.SetUserMode(pid, Modes.KeyMsg);

			move(1,24);
			clrtoeol();
			sends(Prompt.Msgs[196]);

			GetData = new LineEdit(msgbuf, 50, pid, true);
			GetData.DoEdit();
			GetData = null;
			Msg = msgbuf.toString();
		}
		if(Msg.equals(""))
		{
			move(1, 24);
			clrtoeol();
			sends(Prompt.Msgs[219]);
			try
			{
				os.flush();
			}
			catch(Exception e) {}
			TIME.Delay(1000);
		}
		else
		{
			for (int i = 0; i < ColaServer.onlineuser; i++)
			{
				if (ColaServer.SortedUser[i] != pid && FriendList.isFriend(UserPassItem.IDItem, UFD.ID, ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID))
					SendMsg(ColaServer.SortedUser[i], Msg, false);
			}
			move(1, 24);
			clrtoeol();
			sends(Prompt.Msgs[220]);
			TIME.Delay(1000);
			printendline();
			return;
		}
	}

	public void ScrollDown()
	{
		ScrollDown(1,24);
	}
	
	public void ScrollDown(int start,int end)
	{
		boolean tempansi = Ansi;
		int tmpx = ScX, tmpy = ScY;

		Ansi = true;
		sends("[" + start + ";" + end + "r[" + start + ";1HM[1;24r");
		move(tmpx, tmpy);
		Ansi = tempansi;
	}
	
	public void ScrollUp()
	{
		ScrollUp(1, 24);
	}
	
	public void ScrollUp(int start,int end)
	{
		boolean tempansi = Ansi;
		int tmpx = ScX,tmpy = ScY;

		Ansi = true;
		sends("[" + start + ";" + end + "r[" + end + ";1HD[1;24r");
		move(tmpx,tmpy);
		Ansi = tempansi;
	}
	
	public void sends(byte b[])
	{
		int i;
		boolean CtrlCode=false;
		byte buf[];

		buf=new byte[b.length];
		for(i=0;i<b.length;i++)
		{
			buf[i]=b[i];
			if(buf[i]=='\n')
			{
				ScX=1;
				ScY++;
			}
			else if(buf[i]=='\010')
				ScX--;
			else if(buf[i]=='\033')
			{
				if(Ansi)
					CtrlCode=true;
				else
					buf[i]=(byte)'*';
			}
			else if((!CtrlCode)&&(buf[i]&(byte)0xe0)!=0)
				ScX++;
			if((buf[i]>='a'&&buf[i]<='z')||(buf[i]>='A'&&buf[i]<='Z'))
				CtrlCode=false;
		}
		try
		{
			if(os != null)
			{
				os.write(buf);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			GoodBye();
			stop();
		}
	}
	
	public void sends(String b)
	{
		int i;
		boolean CtrlCode=false;
		byte buf[];

		buf=new byte[b.length()];
		for(i=0;i<b.length();i++)
		{
			buf[i]=(byte)b.charAt(i);
			if(buf[i]=='\n')
			{
				ScX=1;
				ScY++;
			}
			else if(buf[i]=='\010')
				ScX--;
			else if(buf[i]=='\033')
			{
				if(Ansi)
					CtrlCode=true;
				else
					buf[i]=(byte)'*';
			}
			else if((!CtrlCode)&&(buf[i]&(byte)0xe0)!=0)
				ScX++;
			if((buf[i]>='a'&&buf[i]<='z')||(buf[i]>='A'&&buf[i]<='Z'))
				CtrlCode=false;
		}
		try
		{
			if(os != null)
			{
				os.write(buf);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			GoodBye();
			stop();
		}
	}
	public void SetInfo()
	{
		File Subscribe=new File(ColaServer.INI.BBSHome+File.separator+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID,".Subscribe");

		if((new File(ColaServer.INI.BBSHome+"ColaInfo",".Groups")).exists())
		{
			Clear();
			sends(Prompt.Msgs[349]);
			sends(Prompt.Msgs[350]);
			sends(Prompt.Msgs[351]);
			sends(Prompt.Msgs[352]);
			sends(Prompt.Msgs[353]);

		}
		else
		{
			Clear();
			sends(Prompt.Msgs[349]);
			sends(Prompt.Msgs[350]);
			sends(Prompt.Msgs[351]);
			sends(Prompt.Msgs[353]);
		}
		switch(MakeSure())
		{
		case '1':
			SetInfo(UFD,UserPassItem.uid);
			break;
		case '2':
			ChPwd();
			break;
		/*case '3':
			if((new File(ColaServer.INI.BBSHome+"ColaInfo",".Groups")).exists())
			{
				GroupList ListGroup=new GroupList(pid);
				ListGroup.DoList();
			}
			break;*/
		}
	}
	public void SetInfo(UserFileData SetUFD,long uidbuf)
	{
		StringBuffer inputbuf;

		String OldEmail=new String(SetUFD.Email);

		inputbuf=new StringBuffer(SetUFD.NickName);
		sends(Prompt.Msgs[272]);
		GetData=new LineEdit(inputbuf,Consts.NameLen, pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.NickName=inputbuf.toString();

		inputbuf=new StringBuffer(SetUFD.RealName);
		sends(Prompt.Msgs[273]);
		GetData=new LineEdit(inputbuf,Consts.NameLen, pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.RealName=inputbuf.toString();

		inputbuf=new StringBuffer(SetUFD.School);
		sends(Prompt.Msgs[274]);
		GetData=new LineEdit(inputbuf,20,pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.School=inputbuf.toString();

		inputbuf=new StringBuffer(SetUFD.Tel);
		sends(Prompt.Msgs[275]);
		GetData=new LineEdit(inputbuf,20,pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.Tel=inputbuf.toString();

		inputbuf=new StringBuffer(SetUFD.Address);
		sends(Prompt.Msgs[276]);
		GetData=new LineEdit(inputbuf,Consts.StrLen-1, pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.Address=inputbuf.toString();

		do
		{
			String emailcheckbuf;

			inputbuf=new StringBuffer(SetUFD.Email);
			sends(Prompt.Msgs[277]);
			GetData=new LineEdit(inputbuf,Consts.StrLen-1, pid,true);
			GetData.DoEdit();
			GetData=null;
			if(inputbuf.length()==0)
				break;
			emailcheckbuf=inputbuf.toString();
			if(emailcheckbuf.indexOf('@')>=0)
			{
				if(emailcheckbuf.indexOf(';')>=0)
					continue;
				if(emailcheckbuf.indexOf('|')>=0)
					continue;
				emailcheckbuf=emailcheckbuf.substring(0,emailcheckbuf.indexOf('@'));
				if(emailcheckbuf.indexOf('.')<0)
				{
					SetUFD.Email=inputbuf.toString();
					break;
				}
				continue;
			}
		}while(SetUFD.Email.length()==0);

		inputbuf=new StringBuffer(SetUFD.URL);
		sends(Prompt.Msgs[278]);
		GetData=new LineEdit(inputbuf,Consts.StrLen-1,pid,true);
		GetData.DoEdit();
		GetData=null;
		SetUFD.URL=inputbuf.toString();

		do
		{
			inputbuf=new StringBuffer(""+SetUFD.NoteMode);
			sends(Prompt.Msgs[279]);
			GetData=new LineEdit(inputbuf,1, pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				SetUFD.NoteMode=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(SetUFD.NoteMode>0&&SetUFD.NoteMode<=3)
					break;
				else
					SetUFD.NoteMode=0;
			}
			catch(NumberFormatException e){}
		}while(SetUFD.NoteMode==0);
		do
		{
			inputbuf=new StringBuffer(""+SetUFD.Sex);
			sends(Prompt.Msgs[280]);
			GetData=new LineEdit(inputbuf,1,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				SetUFD.Sex=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(SetUFD.Sex>0&&SetUFD.Sex<=9)
					break;
				else
					SetUFD.Sex=0;
			}
			catch(NumberFormatException e){}
		}while(SetUFD.Sex==0);

		do
		{
			inputbuf=new StringBuffer(""+SetUFD.BloodType);
			sends(Prompt.Msgs[281]);
			GetData=new LineEdit(inputbuf,1,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				SetUFD.BloodType=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(SetUFD.BloodType>0&&SetUFD.BloodType<=9)
					break;
				else
					SetUFD.BloodType=0;
			}
			catch(NumberFormatException e){}
		}while(SetUFD.BloodType==0);
		do
		{
			Date buf=new Date(SetUFD.Birthday);
			int year=buf.getYear(),month=buf.getMonth(),day=buf.getDate(),hour=buf.getHours();

			inputbuf=new StringBuffer(""+(year+1900));
			sends(Prompt.Msgs[282]);
			move(22,ScY);
			GetData=new LineEdit(inputbuf,5,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				year=(new Integer(inputbuf.toString())).intValue();
				if(year<1900)
					continue;
				year-=1900;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(42,ScY);
			inputbuf=new StringBuffer(""+(month+1));
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				month=(new Integer(inputbuf.toString())).intValue();
				if(month<1||month>12)
					continue;
				month--;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(47,ScY);
			inputbuf=new StringBuffer(""+day);
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				day=(new Integer(inputbuf.toString())).intValue();
				if(day<=0||day>31)
					continue;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(52,ScY);
			inputbuf=new StringBuffer(""+hour);
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				hour=(new Integer(inputbuf.toString())).intValue();
				if(hour<0||hour>=24)
					continue;
				//                hour--;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			SetUFD.Birthday=(new Date(year,month,day,hour,0)).getTime();
			break;
		}while(SetUFD.Birthday==0);
		sends(Prompt.Msgs[99]);
		if(MakeSure()!='Y')
		{
			sends(Prompt.Msgs[284]);
			PressReturn();
			return;
		}
		if(!SetUFD.Email.equals(OldEmail)&&!BBS.HasOnePerm(pid,Perm.SYSOP))
			if(SetUFD.Email.indexOf(".bbs@")==-1)
			{
				//User Justify....
				int code=NUMBER.getIntRnd(999999999);
				
				sends("\r\n"+Prompt.Msgs[386]+"\r\n");
				
				RandomAccessFile inputfile=null;
				try
				{
					os.flush();
					
					inputfile=new RandomAccessFile(ColaServer.INI.BBSHome+"etc"+File.separator+"mailcheck","r");
					int len=(int)inputfile.length();
					byte temp[]=new byte[len];
					inputfile.read(temp);
					
					ByteArrayInputStream bis=new ByteArrayInputStream(temp);
					doInternetMail("SYSOP",SetUFD.Email,bis,"[ColaBBS]"+SetUFD.ID+"("+uidbuf+"-"+code+")"+"[User Justify]");
					//ColaServer.MailCheck.put(new Integer((int)uidbuf),new Integer(code));
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
				finally
				{
					try
					{
						if(inputfile!=null)
							inputfile.close();
					}
					catch(IOException e1)
					{
						e1.printStackTrace();
					}
				}
				SetUFD.Perm&=~(ColaServer.INI.RegPerm);
				SetUFD.Perm|=Perm.Basic;
			}
		ColaServer.UFDList.save(SetUFD);
	}
	
	public int SetPerm(int OldPerm)
	{
		int i;

		move(1,5);
		sends(Prompt.Msgs[125]);
		sends(Prompt.Msgs[126]);
		sends(Prompt.Msgs[127]);
		sends(Prompt.Msgs[128]);
		sends(Prompt.Msgs[129]);
		sends(Prompt.Msgs[130]);
		sends(Prompt.Msgs[131]);
		sends(Prompt.Msgs[132]);
		sends(Prompt.Msgs[133]);
		sends(Prompt.Msgs[134]);
		sends(Prompt.Msgs[135]);
		sends(Prompt.Msgs[136]);
		sends(Prompt.Msgs[137]);
		sends(Prompt.Msgs[138]);
		sends(Prompt.Msgs[139]);
		sends(Prompt.Msgs[140]);
		sends(Prompt.Msgs[141]);
		sends(Prompt.Msgs[142]);
		for(i=0;i<32;i++)
		{
			if(i<16)
				move(34,7+i);
			else
				move(74,i-9);
			if(((OldPerm>>>i)&1)==0)
				sends("[1;32mOFF");
			else
				sends("[1;32m ON");
		}

		while(true)
		{
			int choice;

			move(19,23);
			sends(" ");
			move(19,23);
			choice=(int)MakeSure();
			if(choice==0)
				break;
			choice-=(int)'A';
			if(choice>=32||choice<0)
				continue;

			if(choice<16)
				move(34,7+choice);
			else
				move(74,choice-9);
			OldPerm^=(1<<choice);
			if(((OldPerm>>>choice)&1)==0)
				sends("[1;32mOFF");
			else
				sends("[1;32m ON");
		}

		return OldPerm;
	}
	
	public void telnetDelBoard ()
	{
		int Locate=-1;
		String BoardName;

		Clear();
		sends(Prompt.Msgs[167]);
		BoardName=BoardComplete(Prompt.Msgs[168]);
		if(BoardName==null||BoardName.length()==0)
			return;
		if((Locate=checkBoard(pid, BoardName))>=0)
		{
			sends(Prompt.Msgs[171]);
			if(MakeSure()!='Y')
			{
				sends(Prompt.Msgs[172]);
				PressAnyKey();
				return;
			}
			DelBoard(pid,Locate,BoardName);
		}
		//¥[¤@­Ó°Ý­n¤£­n§RºëµØ°Ïªº´£¥Ü
		delAnnounce(pid,BoardName);
		PressAnyKey();
		return;
	}
	
	public void telnetDelUser()
	{
		String UserBuf;

		UserBuf=NameComplete(Prompt.Msgs[93]);
		if(!ColaServer.UFDList.exist(UserBuf))
			return;
		Clear();	
		sends(Prompt.Msgs[102]);
		if(MakeSure()!='Y')
		{
			sends(Prompt.Msgs[103]);
			PressReturn();
			return;
		}
		ColaServer.UFDList.deleteUser(UserBuf);
		ColaServer.BBSUsers[pid].sends(Prompt.Msgs[104]+UserBuf+Prompt.Msgs[169]);
		PressReturn();
	}

	public void telnetNewBoard()
	{
		String BoardName;
		StringBuffer InputBuf=new StringBuffer();

		Clear();
		sends(Prompt.Msgs[105]);
		sends(Prompt.Msgs[106]);
		(GetData=new LineEdit(InputBuf,18, pid,true)).DoEdit();
		GetData=null;
		BoardName=InputBuf.toString();
		
		if (ColaServer.BList.exist(BoardName))
		{
			sends(Prompt.Msgs[107]);
			PressAnyKey();
			return;
		}
		
		BoardItem bi = new BoardItem();
		bi.Name = BoardName;
		
		if(!ValidBoardName(InputBuf))
		{
			sends(Prompt.Msgs[109]);
			PressAnyKey();
			return;
		}

		InputBuf.setLength(0);
		sends(Prompt.Msgs[110]);
		(GetData=new LineEdit(InputBuf,60, pid,true)).DoEdit();
		GetData=null;
		bi.Title = InputBuf.toString();
		if (bi.Title.length() > 0)
		{
			bi.EGroup = (byte)bi.Title.charAt(0);
			bi.Title = bi.Title.substring(1);
		}
		else
			bi.EGroup = (byte)'0';

		InputBuf.setLength(0);
		sends(Prompt.Msgs[111]);
		(GetData=new LineEdit(InputBuf,Consts.BmLen-2, pid,true)).DoEdit();
		GetData=null;
		bi.BM = InputBuf.toString();
		

		sends(Prompt.Msgs[112]);
		if(MakeSure()=='Y')
		{
			sends(Prompt.Msgs[113]);
			if(MakeSure()=='P')
			{
				Clear();
				sends(Prompt.Msgs[114]);
				bi.Level = SetPerm(bi.Level) | Consts.PostMask;
			}
			else
			{
				Clear();
				sends(Prompt.Msgs[115]);
				bi.Level = SetPerm(bi.Level) & (~Consts.PostMask);
			}
		}
		sends(Prompt.Msgs[116]);
		if((bi.Anonymous = (MakeSure() == 'Y')))
		{
			sends(Prompt.Msgs[117]);
			bi.Anonymous = (MakeSure() != 'N');
		}	

		sends(Prompt.Msgs[118]);
		bi.NoZap = !(MakeSure()=='N');
		
		sends(Prompt.Msgs[119]);
		bi.SaveMode = (MakeSure()!='N');
		
		sends(Prompt.Msgs[120]);
		bi.JunkBoard = (MakeSure()=='Y');
		
		sends(Prompt.Msgs[121]);
		if (MakeSure()!='Y')
		{
			sends(Prompt.Msgs[122]);
			PressAnyKey();
			return;
		}
		//      ¥H¤U¬°¦¨¥ßºëµØ°Ï³¡¤À
		(new File(ColaServer.INI.BBSHome+"man"+File.separator+"boards"+File.separator+BoardName+File.separator)).mkdirs();
		//      ¥H¤W¬°¦¨¥ßºëµØ°Ï³¡¤À

		ColaServer.BList.newBoard(bi);
		(new File(ColaServer.INI.BBSHome+"boards"+File.separator+BoardName+File.separator)).mkdirs();
		sends(Prompt.Msgs[123]);
		PressAnyKey();
	}

	private void TelnetUserLogin()
	{
		StringBuffer inputbuf=new StringBuffer();

		Clear();
		if(UFD.NickName.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[250]);
			GetData=new LineEdit(inputbuf,Consts.NameLen, pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.NickName=inputbuf.toString();
		}
		if(UFD.RealName.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[251]);
			GetData=new LineEdit(inputbuf,Consts.NameLen, pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.RealName=inputbuf.toString();
		}
		if(UFD.School.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[252]);
			GetData=new LineEdit(inputbuf,20,pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.School=inputbuf.toString();
		}
		if(UFD.Tel.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[253]);
			GetData=new LineEdit(inputbuf,20,pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.Tel=inputbuf.toString();
		}
		if(UFD.Address.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[254]);
			GetData=new LineEdit(inputbuf,Consts.StrLen-1,pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.Address=inputbuf.toString();
		}
		while(UFD.Email.length()==0)
		{
			String emailcheckbuf;

			inputbuf.setLength(0);
			sends(Prompt.Msgs[255]);
			GetData=new LineEdit(inputbuf,Consts.StrLen-1,pid,true);
			GetData.DoEdit();
			GetData=null;
			if(inputbuf.length()==0)
				break;
			emailcheckbuf=inputbuf.toString();
			if(emailcheckbuf.indexOf('@')>=0)
			{
				if(emailcheckbuf.indexOf(';')>=0)
					continue;
				if(emailcheckbuf.indexOf('|')>=0)
					continue;
				emailcheckbuf=emailcheckbuf.substring(0,emailcheckbuf.indexOf('@'));
				if(emailcheckbuf.indexOf('.')<0)
				{
					UFD.Email=inputbuf.toString();
					break;
				}
				continue;
			}
		}
		if(UFD.URL.length()==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[256]);
			GetData=new LineEdit(inputbuf,Consts.StrLen-1,pid,true);
			GetData.DoEdit();
			GetData=null;
			UFD.URL=inputbuf.toString();
		}
		while(UFD.NoteMode==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[257]);
			GetData=new LineEdit(inputbuf,1,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				UFD.NoteMode=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(UFD.NoteMode>0&&UFD.NoteMode<=3)
					break;
				else
					UFD.NoteMode=0;
			}
			catch(NumberFormatException e){}
		}
		while(UFD.Sex==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[258]);
			GetData=new LineEdit(inputbuf,1,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				UFD.Sex=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(UFD.Sex>0&&UFD.Sex<=9)
					break;
				else
					UFD.Sex=0;
			}
			catch(NumberFormatException e){}
		}
		while(UFD.BloodType==0)
		{
			inputbuf.setLength(0);
			sends(Prompt.Msgs[259]);
			GetData=new LineEdit(inputbuf,1,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				UFD.BloodType=(byte)(new Integer(inputbuf.toString()).intValue()&0xff);
				if(UFD.BloodType>0&&UFD.BloodType<=9)
					break;
				else
					UFD.BloodType=0;
			}
			catch(NumberFormatException e){}
		}
		while(UFD.Birthday==0)
		{
			int year,month,day,hour;

			inputbuf.setLength(0);
			sends(Prompt.Msgs[260]);
			move(22,ScY);
			GetData=new LineEdit(inputbuf,5,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				year=(new Integer(inputbuf.toString())).intValue();
				if(year<1900)
					continue;
				year-=1900;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(42,ScY);
			inputbuf.setLength(0);
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				month=(new Integer(inputbuf.toString())).intValue();
				if(month<=0||month>12)
					continue;
				month--;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(47,ScY);
			inputbuf.setLength(0);
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				day=(new Integer(inputbuf.toString())).intValue();
				if(day<=0||day>31)
					continue;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			move(52,ScY);
			inputbuf.setLength(0);
			GetData=new LineEdit(inputbuf,2,pid,true);
			GetData.DoEdit();
			GetData=null;
			try
			{
				hour=(new Integer(inputbuf.toString())).intValue();
				if(hour<0||hour>=24)
					continue;
				//                hour--;
			}
			catch(NumberFormatException e)
			{
				continue;
			}
			UFD.Birthday=(new Date(year,month,day,hour,0)).getTime();
			break;
		}
		
		//UFD.LastHost = Utils.GetHostName(from);
		ColaServer.UFDList.save(UFD);
	}
	
	//From Admin.java
	public static synchronized int checkBoard(int pid, String BoardName)
	{
		int Locate=-1;
		StringBuffer InputBuf=new StringBuffer();
		RandomAccessFile Boards=null;

		try
		{
			byte checkbuf[]=new byte[18];
			int i;
			String BoardNameBuf;
			Boards=new RandomAccessFile(ColaServer.INI.BBSHome+Consts.Boards,"r");

			for(i=0;i<(int)(Boards.length()/256);i++)
			{
				Boards.read(checkbuf);
				BoardNameBuf=new String(checkbuf,0);
				if(BoardNameBuf.indexOf(0)!=-1)
					BoardNameBuf=BoardNameBuf.substring(0,BoardNameBuf.indexOf(0));
				if(BoardNameBuf.equals(BoardName))
				{
					Locate=i;
					break;
				}
				else
					Boards.skipBytes(238);
			}
			if(i==(int)(Boards.length()/256))
			{
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[145]);
				return -1;
			}
		}
		catch(IOException e)
		{
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[170]+e+"\r\n");
			return -1;
		}
		finally
		{
			try
			{
				if(Boards!=null)
				{
					Boards.close();
					Boards=null;
				}	
			}
			catch(IOException e){}
		}
		return Locate;
	}

	public static boolean delAnnounce(int pid,String BoardName) {
		new DelTree(ColaServer.INI.BBSHome+"man"+File.separator+BoardName+File.separator);
		return true;
	}

	public static synchronized boolean DelBoard(int pid,int Locate,String BoardName)
	{		
		RandomAccessFile Boards=null;
		
		try
		{
			ColaServer.BList.deleteBoard(BoardName);
			new DelTree(ColaServer.INI.BBSHome+"boards"+File.separator+BoardName+File.separator);
			ColaServer.BList.LoadFile();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[174]);
		}
		/*catch(IOException e)
		{
		ColaServer.BBSUsers[pid].sends(Prompt.Msgs[170]+e+"\r\n");
		return false;
		}*/
		finally
		{
			try
			{
				if(Boards!=null)
				{
					Boards.close();
					Boards=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
				return false;
			}	
		}
		return true;
	}
	
	public static boolean ValidBoardName(StringBuffer str)
	{
		int i;
		char buf;

		if(str.length()==0)
			return false;
		for(i=0;i<str.length();i++)
		{
			buf=Character.toUpperCase(str.charAt(i));
			if((buf<'A'||buf>'Z')&&(buf<'0'||buf>'9')&&(buf!='_'))
				return false;
		}
		return true;
	}
	//End From Admin.java
	
	/**
	 * ·sªº, for ChatRoom
	 */
	public String getChatNick(MultiModuleCmd mmc)
	{
		return chatID;
	}
	
  public void addChatNick(String theNick,MultiModuleCmd mmc)
  {
  }
  public void removeChatNick(String theNick)
  {
  	chatID=null;
  }
  public void replyCmd(Object source,Object theCmd)
  {
  }
	public void addChatMsg(int mid,String Msg)
	{
		int oldx=ScX,oldy=ScY;
		chatY++;
		if(chatY>=22)
		{
			ScrollUp(3,21);
			chatY=21;
		}
		move(1,chatY);
		sends("[m"+Msg+"[0;34;47m");
		move(oldx,oldy);
		try
		{
			os.flush();
		}
		catch(IOException e){}
	}
	public void changeRoom(int mid,String theroom)
	{
		int oldx=ScX,oldy=ScY;
		room=theroom;
		move(9,1);
		sends("[1;32;44m"+STRING.Cut(room,10));
		move(oldx,oldy);
	}
	public void changeTopic(int mid,String newTopic)
	{
		int oldx=ScX,oldy=ScY;
		chatTopic=newTopic;
		move(27,1);
		sends("[1;36;44m"+STRING.Cut(chatTopic,53)+"[m");
		move(oldx,oldy);
	}

	/**
	 * ¶i²á¤Ñ«Ç
	 */
	public void telnetEnterChat()
	{
		//	String room="main";
		StringBuffer chatBuf=new StringBuffer();
		String OrgMode=usermode;

		room="main";
		chatID=UFD.ID;
		chatTopic=Prompt.Msgs[411];
		chatBuf.setLength(0);
		move(1,24);
		clrtoeol();
		sends(Prompt.Msgs[408]);
		if((GetData=new LineEdit(chatBuf,8, pid,true)).DoEdit()<0)
			return;
		GetData=null;
		if(chatBuf.length()!=0)
		{
			if(chatBuf.length()>8)
				chatID=chatBuf.toString().substring(0,8);
			else
				chatID=chatBuf.toString();
		}
		else if(chatID.length()>8)
			chatID=chatID.substring(0,8);
		Clear();
		move(1,1);
		sends("[1;33;44m "+STRING.Cut(Prompt.Msgs[409],6)+"[32m "+STRING.Cut(room,10)+" [1;33m"+STRING.Cut(Prompt.Msgs[410],6)+" [1;36m"+STRING.Cut(chatTopic,53)+"\r\n");
		sends("[0m"+Prompt.Msgs[412]);
		move(1,22);
		sends("[0m"+Prompt.Msgs[412]);
		chatY=2;
		if(ColaServer.myChat.enterChat(chatID,UFD.ID,Home,this,-1)<0)
			return;
		BBS.SetUserMode(pid,Modes.Chat);
		try
		{
			chatBuf.setLength(0);
			while(true)
			{
				move(1,23);
				sends("[0m"+STRING.Cut(chatID+":",10));
				if((GetData=new LineEdit(chatBuf,68, pid,true)).DoEdit()<0)
					return;
				GetData=null;
				if(chatBuf.length()==0)
					continue;
				if(chatBuf.charAt(0)=='/')
				{
					if(chatBuf.length()==1)
					{
					}
					else
					{
						StringTokenizer st=new StringTokenizer(chatBuf.toString());
						String Arg=null;
						if(!st.hasMoreTokens())
							continue;
						st.nextToken();
						switch(chatBuf.charAt(1))
						{
						case 'j':
							if(st.hasMoreTokens())
							{
								Arg=st.nextToken();
								ColaServer.myChat.enterRoom(chatID,Arg);
								room=Arg;
								move(1,1);
								sends("[1;33;44m "+STRING.Cut(Prompt.Msgs[409],6)+"[32m "+STRING.Cut(room,10));
							}
							chatBuf.setLength(0);
							break;
						case 'n':
							if(st.hasMoreTokens())
							{
								Arg=st.nextToken();
								switch(ColaServer.myChat.changeNick(chatID,Arg))
								{
								case 0:
									chatID=Arg;
									break;
								}
							}
							chatBuf.setLength(0);
							break;
						case '/':
							if(chatBuf.length()>2)
							{
								Arg=chatBuf.toString().substring(2);
								if(Arg.indexOf(' ')!=-1)
									Arg=Arg.substring(0,Arg.indexOf(' '));
								if(st.hasMoreTokens())
									ColaServer.myChat.doAction(chatID,Arg,st.nextToken());
								else
									ColaServer.myChat.doAction(chatID,Arg,"");
							}
							else if(chatBuf.length()==2)
								ColaServer.myChat.doActionHelp(chatID);
							chatBuf.setLength(0);
							break;
						case 'a':
							if(st.hasMoreTokens())
								ColaServer.myChat.postAction(chatID,"[1;36m"+chatID+" [33m"+st.nextToken());
							chatBuf.setLength(0);
							break;
						case 'l':
							addChatMsg(-1,"[1;33;44m"+Prompt.Msgs[423]+"[m");
							if(st.hasMoreTokens())
								ColaServer.myChat.fullListUser(chatID,st.nextToken());
							else
								ColaServer.myChat.fullListUser(chatID,null);
							chatBuf.setLength(0);
							break;
						case 'w':
							addChatMsg(-1,"[1;33;44m"+Prompt.Msgs[425]+"[m");
							if(st.hasMoreTokens())
								ColaServer.myChat.listUser(chatID,st.nextToken());
							else
								ColaServer.myChat.listUser(chatID,null);
							chatBuf.setLength(0);
							break;
						case 'm':
							if(st.hasMoreTokens())
							{
								String target=st.nextToken();
								if(target.length()>8)
									target=target.substring(0,8);
								if(st.hasMoreTokens())
									ColaServer.myChat.sendNote(chatID,target,st.nextToken());
							}
							chatBuf.setLength(0);
							break;
						case 'r':
							addChatMsg(-1,"[1;33;44m"+Prompt.Msgs[424]+"[m");
							ColaServer.myChat.listRoom(chatID);
							chatBuf.setLength(0);
							break;
						case 'h':
							if(st.hasMoreTokens())
							{
								if(st.nextToken().equalsIgnoreCase("op"))
									ColaServer.myChat.doOpHelp(chatID);
							}
							else
								ColaServer.myChat.doHelp(chatID);
							chatBuf.setLength(0);
							break;
						case 'f': // the following is op command
							if(st.hasMoreTokens())
							{
								String tmp=st.nextToken();
								for(int i=0;i<tmp.length();i++)
								{
									if(tmp.charAt(i)=='s')
										ColaServer.myChat.setHideRoom(chatID);
									else if(tmp.charAt(i)=='l')
										ColaServer.myChat.setLockRoom(chatID);
								}
							}
							chatBuf.setLength(0);
							break;
						case 'i':
							if(st.hasMoreTokens())
								ColaServer.myChat.inviteUser(chatID,st.nextToken());
							chatBuf.setLength(0);
							break;
						case 'k':
							if(st.hasMoreTokens())
								ColaServer.myChat.kickUser(chatID,st.nextToken());
							chatBuf.setLength(0);
							break;
						case 'o':
							if(st.hasMoreTokens())
								ColaServer.myChat.setOP(chatID,st.nextToken());
							chatBuf.setLength(0);
							break;
						case 't':
							if(st.hasMoreTokens())
								ColaServer.myChat.setTopic(chatID,st.nextToken());
							chatBuf.setLength(0);
							break;
						case 's':
            	ColaServer.myChat.startRecord(chatID);
							chatBuf.setLength(0);
							break;
/*						case 's': // the following is sysop command
							chatBuf.setLength(0);
							break;*/
						case 'c':  // the following is local function.
							if(chatBuf.length()>2&&chatBuf.charAt(2)=='a')
							{
								if(st.hasMoreTokens())
								{
									String arg=st.nextToken();
									int index=BBS.IfOnline(arg);
									
									if(index!=-1)
										SendMsg(index,UFD.ID+" "+Prompt.Msgs[449]+" "+room+" "+Prompt.Msgs[420]);
									else
										addChatMsg(-1,arg+" "+Prompt.Msgs[426]);
								}
							}
							else
							{
								sends("[3;21r[21;1HDDDDDDDDDDDDDDDDDDD[1;24r");
								chatY=2;
							}
							chatBuf.setLength(0);
							break;
						case 'u':
							chatBuf.setLength(0);
							break;
						case 'p':
							UFD.UserDefine^=Consts.AllPager;
							if((UFD.UserDefine&Consts.AllPager)==0)
								addChatMsg(-1,Prompt.Msgs[91]);
							else
								addChatMsg(-1,Prompt.Msgs[92]);
							chatBuf.setLength(0);
							break;
            case 'd': // for TRPG.....
							if(st.hasMoreTokens())
							{
                Arg=st.nextToken();
                if(Arg.length()>9)
                  Arg=Arg.substring(0,9);
                ColaServer.myChat.postMsg(chatID,"[1;33m "+chatID+Prompt.Msgs[476]+Arg+Prompt.Msgs[477]+(NUMBER.getIntRnd(Integer.parseInt(Arg))+1)+Prompt.Msgs[478]);
							}
							chatBuf.setLength(0);
              break;
						case 'b':
							return;
						}
					}
				}
				else
				{
					ColaServer.myChat.postMsg(chatID,chatBuf.toString());
					chatBuf.setLength(0);
				}
			}
		}
		finally
		{
			ColaServer.myChat.exitChat(chatID);
			BBS.SetUserMode(pid,OrgMode);
		}
	}

	public boolean TalkWaiting=false;	
  public TalkUser RequestUser;	
  public void cancelTalkRequest(TalkUser tu)
  {
  	RequestAns=-1;
  	TalkWaiting=false;
//  	TalkPair.removeTalkPair(theid);
  }
/*	private void DoTalk(int index)
	{
		StringBuffer talkbuf=new StringBuffer();
		String OrgMode=usermode;
		int x=1,y=0;
		boolean ansitemp=Ansi;

		Ansi=true;

		TelnetUser TalkToUser = (TelnetUser)ColaServer.BBSUsers[index];
		boolean BackupIsInMenu = false;
		if (BBSMainMenu != null)
		{
			BackupIsInMenu = BBSMainMenu.inMenu;
			BBSMainMenu.inMenu = false;
		}
			
		if(!TalkMode)
		{
			int answer;

			Clear();
			sends(Prompt.Msgs[224]);
			sends(Prompt.Msgs[225]);
			sends(Prompt.Msgs[226]);
			sends(Prompt.Msgs[227]+ColaServer.BBSUsers[index].UFD.ID+"("+TalkToUser.UFD.NickName+Prompt.Msgs[228]);

WaitLoop:
			while(true)
			{
				answer=getkey();
				if(TalkRequest==false)
					return;
				switch(answer)
				{
				case 'Y':
				case 'y':
				case Keys.Enter:
					RequestAns=0;
					TalkRequest=false;
					TalkMode=true;
					break WaitLoop;
				case -1:
				case 'N':
				case 'n':
					RequestAns=1;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[229]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'B':
				case 'b':
					RequestAns=2;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[230]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'C':
				case 'c':
					RequestAns=3;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[231]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'D':
				case 'd':
					RequestAns=4;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[232]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'E':
				case 'e':
					RequestAns=5;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[233]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'F':
				case 'f':
					RequestAns=6;
					TalkToUser.sends(UFD.ID+Prompt.Msgs[234]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'M':
				case 'm':
					RequestAns=7;
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				}
			}
		}
		if (RequestAns != 0)
		{
			if (BBSMainMenu != null)
			{
				BBSMainMenu.inMenu = BackupIsInMenu;
				return;
			}
		}
		
		Clear();
		BBS.SetUserMode(pid,Modes.Talk+" "+STRING.Cut(TalkToUser.UFD.ID,13)+"[33m([37m"+STRING.Cut(TalkToUser.UFD.NickName,16)+"[33m)");
		move(1,12);
		sends("[1;46m[35m      [32m"+STRING.Cut(UFD.ID,13)+"[33m([37m"+STRING.Cut(UFD.NickName,16)+"[33m)[35m V.S. [32m"+STRING.Cut(TalkToUser.UFD.ID,13)+"[33m([37m"+STRING.Cut(TalkToUser.UFD.NickName,16)+"[33m)"+"      [m");
		printendline();
		move(1,1);
TalkLoop:
		while(true)
		{
			int inkey;
			byte echobuf[]=new byte[1];

			inkey=getch();
			if(TalkToUser.TalkMode!=true||TalkToUser.UserLogout)
			{
				TalkMode=false;
				BBS.SetUserMode(pid,OrgMode);
				TelnetUtils.ReDraw(pid);
				break;
			}
			switch(inkey)
			{
			case 3:
			case 4:
			case -1:
				Clear();
				move(1,24);
				sends(Prompt.Msgs[235]);

				TalkToUser.Clear();
				TalkToUser.move(1,24);
				TalkToUser.sends(Prompt.Msgs[235]);
				TalkToUser.flush();
				getch();
				break TalkLoop;
			case Keys.BackSpace:
			case Keys.Del:
				if(x>1)
					x--;
				echobuf[0]=32;
				move(x,y+13);
				sends(echobuf);
				move(x,y+13);
				TalkToUser.move(x,y+1);
				TalkToUser.sends(echobuf);
				TalkToUser.move(x,y+1);
				TalkToUser.flush();
				break;
			case Keys.Up:
			case Keys.Down:
			case Keys.Left:
			case Keys.Right:
				break;
			case Keys.Enter:
				if(y<10)
				{
					y++;
					x=1;
					move(1,y+13);
					clrtoeol();
					TalkToUser.move(1,y+1);
					TalkToUser.clrtoeol();
				}
				else
				{
					x=1;
					TalkToUser.ScrollUp(1,11);
					ScrollUp(13,23);
					move(1,y+13);
					TalkToUser.move(1,y+1);
				}
				TalkToUser.flush();
				continue;
			case 10:
				continue;
			default:
				echobuf[0]=(byte)inkey;
				move(x,y+13);
				sends(echobuf);
				TalkToUser.move(x,y+1);
				TalkToUser.sends(echobuf);
				x++;
				if(x>80)
				{
					if(y<10)
					{
						y++;
						x=1;
						move(1,y+13);
						clrtoeol();
						TalkToUser.move(1,y+1);
						TalkToUser.clrtoeol();
					}
					else
					{
						x=1;
						TalkToUser.ScrollUp(1,11);
						ScrollUp(13,23);
						move(1,y+13);
						TalkToUser.move(1,y+1);
					}
				}
				TalkToUser.flush();
				break;
			}
		}

		TalkMode=false;
		Ansi=ansitemp;
		BBS.SetUserMode(pid,OrgMode);
		TelnetUtils.ReDraw(pid);		

		if (BBSMainMenu != null)
			BBSMainMenu.inMenu = BackupIsInMenu;
	}*/
  /**
   * for TalkUser
   */
/*	public void Talk(int index)
	{
		int i;

		if(index==-1)
		{
			StringBuffer pidbuf=new StringBuffer();

			index=OnlineNameComplete(Prompt.Msgs[236]);
			if(index==-1)
				return;
			if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllPager)==0)
			{
				if(((ColaServer.BBSUsers[index].UFD.UserDefine&Consts.FriendPager)==0)||(!FriendList.isFriend(UserPassItem.IDItem,UFD.ID,ColaServer.BBSUsers[index].UFD.ID)))
				{
					move(1,24);
					clrtoeol();
					sends(Prompt.Msgs[216]);
					TIME.Delay(1000);
					printendline();
					return;
				}
			}
		}*/

		/*        if(!Utils.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllPager)==0)
		{
		move(1,24);
		clrtoeol();
		sends(Prompt.Msgs[216]);
		TIME.Delay(1000);
		printendline();
		return;
		}*/

/*		if(index==pid||ColaServer.BBSUsers[index]==null||ColaServer.BBSUsers[index].UserLogout==true||ColaServer.BBSUsers[index].LoginTime==null||ColaServer.BBSUsers[index].TalkMode)
			return;
		ColaServer.BBSUsers[index].TalkRequest=true;
		ColaServer.BBSUsers[index].RequestID=pid;
		((TelnetUser)ColaServer.BBSUsers[index]).Bell();
		SendMsg(index,Prompt.Msgs[316]);
		Clear();
		sends(Prompt.Msgs[238]+ColaServer.BBSUsers[index].UFD.ID+Prompt.Msgs[239]);
		TalkMode=true;
		do
		{
			if(ColaServer.BBSUsers[index].UserLogout==true)
			{
				PressAnyKey();
				TalkMode=false;
				TelnetUtils.ReDraw(pid);
				return;
			}
			try
			{
				if(is.available()!=0)
				{
					int inkey;
					if((inkey=getch())==3||inkey==-1)
					{
						ColaServer.BBSUsers[index].TalkRequest=false;
						TalkMode=false;
						TelnetUtils.ReDraw(pid);
						return;
					}
				}
				TIME.Delay(300);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				//				ColaServer.BBSlog.Write("Exception #42"+e);
			}
		}while(ColaServer.BBSUsers[index].TalkRequest==true);
		if(ColaServer.BBSUsers[index].RequestAns!=0)
		{
			PressAnyKey();
			TalkMode=false;
			TelnetUtils.ReDraw(pid);
			return;
		}
		DoTalk(index);
		TalkMode=false;
	}*/
	/**
	 * ©I¥s index ¨Ó²á¤Ñ
	 */
	public void Talk(int index)
	{
		int i;

		if(index==-1)
		{
			StringBuffer pidbuf=new StringBuffer();

			index=OnlineNameComplete(Prompt.Msgs[236]);
			if(index==-1)
				return;
			if(!BBS.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllPager)==0)
			{
				if(((ColaServer.BBSUsers[index].UFD.UserDefine&Consts.FriendPager)==0)||(!FriendList.isFriend(UserPassItem.IDItem,UFD.ID,ColaServer.BBSUsers[index].UFD.ID)))
				{
					move(1,24);
					clrtoeol();
					sends(Prompt.Msgs[216]);
					TIME.Delay(1000);
					printendline();
					return;
				}
			}
		}

		/*        if(!Utils.HasOnePerm(pid,Perm.SYSOP)&&(ColaServer.BBSUsers[index].UFD.UserDefine&Consts.AllPager)==0)
		{
		move(1,24);
		clrtoeol();
		sends(Prompt.Msgs[216]);
		Utils.Delay(1000);
		printendline();
		return;
		}*/
//		if(!(ColaServer.BBSUsers[index] instanceof TelnetUser))
//    	return;
		if(index==pid||ColaServer.BBSUsers[index]==null||ColaServer.BBSUsers[index].UserLogout==true||ColaServer.BBSUsers[index].LoginTime==null||(ColaServer.BBSUsers[index] instanceof TelnetUser&&((TelnetUser)ColaServer.BBSUsers[index]).TalkMode))
			return;
		Clear();
		sends(Prompt.Msgs[238]+ColaServer.BBSUsers[index].UFD.ID+Prompt.Msgs[239]);
		TalkMode=true;
    TalkWaiting=true;
//    int theid=TalkPair.getTID();
//    TalkPair.putTalkPair(theid,new TalkPair(0,this));
		ColaServer.BBSUsers[index].doTalkRequest(this,0);
    flush();
		while(TalkWaiting)
		{
			if(ColaServer.BBSUsers[index].UserLogout==true)
			{
				PressAnyKey();
        TalkWaiting=false;
				TalkMode=false;
//        TalkPair.removeTalkPair(theid);
				TelnetUtils.ReDraw(pid);
				return;
			}
			try //busy waiting...:(
			{
				if(is.available()!=0)
				{
					int inkey;
					if((inkey=getch())==3||inkey==-1)
					{
						ColaServer.BBSUsers[index].cancelTalkRequest(this);
            TalkWaiting=false;
						TalkMode=false;
//		        TalkPair.removeTalkPair(theid);
						TelnetUtils.ReDraw(pid);
						return;
					}
				}
				TIME.Delay(300);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				//				ColaServer.BBSlog.println("Exception #42"+e);
			}
		}
		if(RequestAns!=0)
		{
			PressAnyKey();
			TalkMode=false;
//	    TalkPair.removeTalkPair(theid);
			TelnetUtils.ReDraw(pid);
			return;
		}

		DoTalk(RequestUser);
//    TalkPair.removeTalkPair(theid);
		TalkMode=false;
	}

	private void DoTalk(TalkUser tu)
	{
		StringBuffer talkbuf=new StringBuffer();
		String OrgMode=usermode;
		int x=1,y=0;
		boolean ansitemp=Ansi;

		Ansi=true;
    talkX=1;
    talkY=0;

//		TalkUser TalkToUser = TalkPair.getTalkUser(thetid);
		boolean BackupIsInMenu = false;
		if (BBSMainMenu != null)
		{
			BackupIsInMenu = BBSMainMenu.inMenu;
			BBSMainMenu.inMenu = false;
		}

		if(!TalkMode)
		{
			int answer;

			Clear();
			sends(Prompt.Msgs[224]);
			sends(Prompt.Msgs[225]);
			sends(Prompt.Msgs[226]);
			sends(Prompt.Msgs[227]+tu.getUFD().ID+"("+tu.getUFD().NickName+Prompt.Msgs[228]);

WaitLoop:
			while(true)
			{
				answer=getkey();
				if(TalkRequest==false)
					return;
				switch(answer)
				{
				case 'Y':
				case 'y':
				case Keys.Enter:
        	tu.doTalkReply(this,0,"");
//					RequestAns=0;
 					TalkRequest=false;
					TalkMode=true;
					break WaitLoop;
				case -1:
				case 'N':
				case 'n':
        	tu.doTalkReply(this,1,"");
//					RequestAns=1;
//					tu.sends(UFD.ID+Prompt.Msgs[229]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'B':
				case 'b':
        	tu.doTalkReply(this,2,"");
//					RequestAns=2;
//					tu.sends(UFD.ID+Prompt.Msgs[230]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'C':
				case 'c':
        	tu.doTalkReply(this,3,"");
//					RequestAns=3;
//					tu.sends(UFD.ID+Prompt.Msgs[231]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'D':
				case 'd':
        	tu.doTalkReply(this,4,"");
//					RequestAns=4;
//					tu.sends(UFD.ID+Prompt.Msgs[232]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'E':
				case 'e':
        	tu.doTalkReply(this,5,"");
//					RequestAns=5;
//					tu.sends(UFD.ID+Prompt.Msgs[233]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'F':
				case 'f':
        	tu.doTalkReply(this,6,"");
//					RequestAns=6;
//					tu.sends(UFD.ID+Prompt.Msgs[234]);
					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				case 'M':
				case 'm':
        	tu.doTalkReply(this,7,"");
//					RequestAns=7;
//					TalkRequest=false;
					TelnetUtils.ReDraw(pid);
					break;
				}
			}
		}
		if (RequestAns != 0)
		{
			if (BBSMainMenu != null)
			{
				BBSMainMenu.inMenu = BackupIsInMenu;
				return;
			}
		}

		Clear();
		BBS.SetUserMode(pid,Modes.Talk+" "+STRING.Cut(tu.getUFD().ID,13)+"[33m([37m"+STRING.Cut(tu.getUFD().NickName,16)+"[33m)");
		move(1,12);
		sends("[1;46m[35m      [32m"+STRING.Cut(UFD.ID,13)+"[33m([37m"+STRING.Cut(UFD.NickName,16)+"[33m)[35m V.S. [32m"+STRING.Cut(tu.getUFD().ID,13)+"[33m([37m"+STRING.Cut(tu.getUFD().NickName,16)+"[33m)"+"      [m");
		printendline();
		move(1,1);
TalkLoop:
		while(TalkMode)
		{
			int inkey;
			byte echobuf[]=new byte[1];

			inkey=getch();
/*			if(tu.TalkMode!=true||tu.UserLogout)
			{
				TalkMode=false;
				Utils.SetUserMode(pid,OrgMode);
				TelnetUtils.ReDraw(pid);
				break;
			}*/
			switch(inkey)
			{
			case 3:
			case 4:
			case -1:
				Clear();
				move(1,24);
				sends(Prompt.Msgs[235]);
        flush();

        tu.sendQuit();
				getch();
/*				tu.Clear();
				tu.move(1,24);
				tu.sends(Prompt.Msgs[235]);
				tu.flush();
				getch();*/
				break TalkLoop;
			case Keys.BackSpace:
			case Keys.Del:
				if(x>1)
					x--;
				echobuf[0]=32;
				move(x,y+13);
				sends(echobuf);
				move(x,y+13);
        tu.sendBackSpace();
/*				tu.move(x,y+1);
				tu.sends(echobuf);
				tu.move(x,y+1);
				tu.flush();*/
				break;
			case Keys.Up:
			case Keys.Down:
			case Keys.Left:
			case Keys.Right:
				break;
			case Keys.Enter:
				if(y<10)
				{
					y++;
					x=1;
					move(1,y+13);
					clrtoeol();
//					tu.move(1,y+1);
//					tu.clrtoeol();
				}
				else
				{
					x=1;
					ScrollUp(13,23);
					move(1,y+13);
//					tu.ScrollUp(1,11);
//					tu.move(1,y+1);
				}
        tu.sendNewLine();
//				tu.flush();
				continue;
			case 10:
				continue;
			default:
				echobuf[0]=(byte)inkey;
				move(x,y+13);
				sends(echobuf);
        tu.sendMessage(echobuf[0]);
//				tu.move(x,y+1);
//				tu.sends(echobuf);
				x++;
				if(x>80)
				{
					if(y<10)
					{
						y++;
						x=1;
						move(1,y+13);
						clrtoeol();
//						tu.move(1,y+1);
//						tu.clrtoeol();
					}
					else
					{
						x=1;
//						tu.ScrollUp(1,11);
						ScrollUp(13,23);
						move(1,y+13);
//						tu.move(1,y+1);
					}
          tu.sendNewLine();
				}
//				tu.flush();
				break;
			}
		}

		TalkMode=false;
		Ansi=ansitemp;
		BBS.SetUserMode(pid,OrgMode);
		TelnetUtils.ReDraw(pid);

		if (BBSMainMenu != null)
			BBSMainMenu.inMenu = BackupIsInMenu;
	}

  private int talkX=1,talkY=0;

	public void sendMessage(byte theMessage)
	{
    byte echobuf[]=new byte[1];

    echobuf[0]=theMessage;
		move(talkX,talkY+1);
		sends(echobuf);
		talkX++;
		flush();
	}
 	public void sendQuit()
	{
		//TODO: implement this colabbs.TalkUser method;
    TalkMode=false;
		Clear();
		move(1,24);
		sends(Prompt.Msgs[235]);
		flush();
	} 
  public boolean stillWaiting()
  {
  	return TalkWaiting;
  }
	public void sendBackSpace()
	{
    byte echobuf[]=new byte[1];

		if(talkX>1)
			talkX--;
		echobuf[0]=32;
		move(talkX,talkY+1);
		sends(echobuf);
		move(talkX,talkY+1);
    flush();
	}

	public void sendNewLine()
	{
		//TODO: implement this colabbs.TalkUser method;
		if(talkY<10)
		{
			talkY++;
			talkX=1;
			move(1,talkY+1);
			clrtoeol();
		}
		else
		{
			talkX=1;
			ScrollUp(1,11);
			move(1,talkY+1);
		}
    flush();
	}
  public UserFileData getUFD()
  {
  	return UFD;
  }
	/**
	 * ³B²z¦³¨Ï¥ÎªÌ¤W¯¸ªº¨Æ¥ó
	 */
	public void AddOnlineUser(BBSUser theUser)
  {
  }

	/**
	 * ³B²z¦³¨Ï¥ÎªÌ¤U¯¸ªº¨Æ¥ó
	 */
	public void RemoveOnlineUser(BBSUser theUser)
  {
  }

	/**
	 * ³B²z¦³¨Ï¥ÎªÌ§ïÅÜª¬ºAªº¨Æ¥ó
	 */
	public void UserStateChanged(BBSUser theUser)
  {
  }

	/**
	 * ³B²z¦³¨Ï¥ÎªÌ©I¥s talk ªº¨Æ¥ó
	 */
  public void doTalkRequest(TalkUser theTalkUser,int talkMode)
  {
  	if(talkMode!=0)
    {
    	theTalkUser.sendQuit();
    }
    else
    {
    	TalkRequest=true;
	    RequestUser=theTalkUser;
			Bell();
			SendMsg(pid,Prompt.Msgs[316]);
    }
  }

	/**
	 * ³B²z¦³¨Ï¥ÎªÌ¦^À³ talk ­n¨Dªº¨Æ¥ó
	 */
  public void doTalkReply(TalkUser tu,int theReplyNumber,String theReplyString)
  {
  	switch(theReplyNumber)
    {
    	case 1:
				sends(tu.getUFD().ID+Prompt.Msgs[229]);
        break;
      case 2:
				sends(tu.getUFD().ID+Prompt.Msgs[230]);
				break;
      case 3:
				sends(tu.getUFD().ID+Prompt.Msgs[231]);
				break;
      case 4:
				sends(tu.getUFD().ID+Prompt.Msgs[232]);
				break;
      case 5:
				sends(tu.getUFD().ID+Prompt.Msgs[233]);
				break;
      case 6:
				sends(tu.getUFD().ID+Prompt.Msgs[234]);
				break;
      case 7:
				sends(tu.getUFD().ID+theReplyString);
      	break;
		}
    RequestAns=theReplyNumber;
  	RequestUser=tu;
    TalkWaiting=false;
  }
/*  public void doTalkReply(TalkUser tu,int theReplyNumber,String theReplyString)
  {
  	switch(theReplyNumber)
    {
    	case 1:
				sends(tu.getUFD().ID+Prompt.Msgs[229]);
        break;
      case 2:
				sends(tu.getUFD().ID+Prompt.Msgs[230]);
				break;
      case 3:
				sends(tu.getUFD().ID+Prompt.Msgs[231]);
				break;
      case 4:
				sends(tu.getUFD().ID+Prompt.Msgs[232]);
				break;
      case 5:
				sends(tu.getUFD().ID+Prompt.Msgs[233]);
				break;
      case 6:
				sends(tu.getUFD().ID+Prompt.Msgs[234]);
				break;
      case 7:
				sends(tu.getUFD().ID+theReplyString);
      	break;
		}
    RequestAns=theReplyNumber;
  	RequestUser=tu;
    TalkWaiting=false;
  }*/
/*  public void doTalkRequest(TalkUser theTalkUser,int talkMode)
  {
  	if(talkMode!=0)
    {
    	theTalkUser.sendQuit();
    }
    else
    {
    	TalkRequest=true;
	    RequestUser=theTalkUser;
			Bell();
			SendMsg(pid,Prompt.Msgs[316]);
    }
  }*/
}