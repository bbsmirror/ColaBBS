package colabbs;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.telnet.*;
import colabbs.net.*;
import colabbs.record.*;
import colabbs.UTILS.*;
import colabbs.DATA.USERFILEDATA.*;

import colabbs.chatroom.*;

public abstract class BBSUser extends Thread implements ChatClient
{
	public boolean UserLogout;
	public boolean MsgMode;
	public boolean ReMsg=false;
	public boolean MsgGetKey=false;
	public boolean TalkRequest;
	public boolean TalkMode;
	public boolean Visible=true;
	public boolean Ansi=true;
	public boolean MultiLogin=false;
	public int pid;
	public int replyID=-1;
	public int MsgID=-1;
	public int RequestAns,RequestID;
	public int ReDrawMode=0;
	public int SigNum=0;
	public int MsgNum=0;
	public String chatID=null,chatTopic=null,room=null;
	public String usermode=Modes.Login;
	public String CurrentBoard="sysop";
	public String Home;
	public String SilkRoad=null;
	public String CopyTitle=null;
	public File CopyPad=null;
	public Date LoginTime;
	public Date LastSig;
	public Socket BBSSocket;
	public PassItem UserPassItem;
	public DataOutputStream os;
	public DataInputStream is;
	public InetAddress from;
	public UserFileData UFD;
	public int uid;

	public String[] getSignFile(int num)
	{		
		File F;
		F = new File(ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator + UFD.ID + File.separator, "signatures");
		if (!F.exists())
			return null;
		
		String tmp[] = new String[ColaServer.INI.MaxSigLines];
		String SigTemp[] = null;
		int i, j;
		DataInputStream dis = null;
		try
		{
			dis = new DataInputStream(new FileInputStream(F));
			for(i = 0; i < (int)((num - 1) * ColaServer.INI.MaxSigLines); i++)
				dis.readLine();
			
			for(i=0;i<ColaServer.INI.MaxSigLines;i++)
				tmp[i] = dis.readLine();
			
			for (i = ColaServer.INI.MaxSigLines - 1; i >= 0; i--)
			{
				if (tmp[i] != null && !tmp[i].trim().equalsIgnoreCase(""))
				{
					SigTemp = new String[i + 1];
					for (j = 0; j <= i; j++)
						SigTemp[j] = tmp[j];
					break;
				}
			}
			dis.close();
			dis = null;
		}
		catch (Exception e)
		{
			try
			{
				if (dis != null)
					dis.close();
			}
			catch (Exception e1)
			{
			}
		}
		
		return SigTemp;
	}
	
	public BBSUser(int userpid)
	{
		super();
		UserLogout=false;
		pid=userpid;
	}
	public boolean checkpasswd(String p1,String p2)
	{
		return ColaServer.Crypter.DoCrypt(p1,p2).equals(p2);
	}

	public void ChUserPerm(int PermBuf)
	{
		UFD.resetPerm(PermBuf);
		ColaServer.UFDList.save(UFD);
	}

	public static boolean doInternetMail (String from,String to,InputStream is,String TitleBuf)
	{
		int locate;
		boolean successflag=false;
		if((locate=to.indexOf('@'))==-1)
			return false;
		SMTPClient smtpc=new SMTPClient();
		successflag=smtpc.sendMail(is,to,from,TitleBuf);
		ColaServer.BBSlog.Write(from+" Send mail to "+to+" : "+TitleBuf+" -> "+successflag);
		return successflag;
	}
	
	public void fastsends(byte b[])
	{
		int i;
		boolean CtrlCode=false;
		byte buf[];

		buf=new byte[b.length];
		try
		{
			if(os!=null)
			{
				os.write(b);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			GoodBye();
			stop();
		}
	}

	public void flush() 
	{
		try
		{
			os.flush();
		}
		catch(Exception e){}
	}
	
	public String genpasswd(String pw)
	{
		char saltc[]=new char[2],c;
		int i,salt;

		salt = NUMBER.getIntRnd(65536)*9;
		saltc[0] = (char)(salt&077);
		saltc[1] = (char)((salt>>>6)&077);
		for(i=0;i<2;i++)
		{
			c = (char)(saltc[i]+'.');
			if(c > '9')
				c += 7 ;
			if(c > 'Z')
				c += 6 ;
			saltc[i] = c ;
		}
		return ColaServer.Crypter.DoCrypt(pw,new String(saltc));
	}
	
//	public abstract void GetMsg(String Msg);
	/**
	 * ·sªº
	 */
	public abstract void GetMsg(String senderID,String Msg);
	//
	
	public synchronized void GoodBye()
	{
		try
		{
			if(LoginTime!=null)
			{
				try
				{
					File MsgFile=new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID,"Messages");
					if(MsgFile.exists())
					{
						int tries;
						
						if((UFD.UserDefine&Consts.BackupMsg)!=0)
							MailFile(UFD.ID,ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(UFD.ID.charAt(0))+File.separator+UFD.ID,"Messages",Prompt.Msgs[334],null,false);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					ColaServer.ReSortRmUser(pid);
					LoginTime=null;
				}
			}
			if(UFD!=null&&UFD.ID!=null)
				ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" : "+UFD.ID+" logout....");
			if(os!=null)
				os.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(is!=null)
				{
					is.close();
					is=null;
				}
				if(os!=null)
				{
					os.close();
					os=null;
				}
				if(BBSSocket!=null)
				{
					BBSSocket.close();
					BBSSocket=null;
				}
			}	
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				/*if(UserPassItem!=null&&UserPassItem.BRCflag&&UserPassItem.BRC!=null&&(BBS.IfOnlineSort(UFD.ID)==-1))
					SaveBrc();*/
				if (UFD != null && UFD.brclist != null && (BBS.IfOnlineSort(UFD.ID)==-1))
					UFD.brclist.SaveFile();
				UserLogout=true;
			}
		}	
	}
	public void KickOut()
	{
		try
		{
			if(os!=null)
				os.flush();
		}		
		catch( IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(is!=null)
				{
					is.close();
					is=null;
				}	
				if(os!=null)
				{
					os.close();
					os=null;
				}	
				if(BBSSocket!=null)
				{
					BBSSocket.close();
					BBSSocket=null;
				}	

				ColaServer.BBSlog.Write(pid+" timeout kicked by someone....");
				UserLogout=true;
			}
			catch( IOException e)
			{
				e.printStackTrace();
			}
		}	
		UserLogout=true;
	}
	public void LogoutSave()
	{	
		byte writebuf[]=new byte[Consts.UserLen];

		UFD.Stay+=((new Date()).getTime()-LoginTime.getTime());

		if(!Visible)
			UFD.Flags[0] = (byte)(UFD.Flags[0] | Consts.UserFlag_Cloak & 0xff);
		else
			UFD.Flags[0] = (byte)(UFD.Flags[0] & (~Consts.UserFlag_Cloak) & 0xff);

		ColaServer.UFDList.save(UFD);
		
		ColaServer.UFDList.logout(UFD.ID);
	}
	
	public void MailFile(String OwnBuf,String Path,String Filename,String TitleBuf,String MailSender,boolean copymail)
	{
		StringBuffer Ans=new StringBuffer();
		String SaveFileName;
		RandomAccessFile inputfile=null,outputfile=null;

		if(OwnBuf.indexOf('@')!=-1)
		{
			if(!ColaServer.INI.InternetMail)
				return;
			boolean successflag=false;
			try
			{
				inputfile=new RandomAccessFile(Path+File.separator+Filename,"r");
				int len=(int)inputfile.length();
				byte temp[]=new byte[len];
				inputfile.read(temp);
				ByteArrayInputStream bis=new ByteArrayInputStream(temp);
				successflag=doInternetMail(MailSender,OwnBuf,bis,TitleBuf);
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
			if(successflag)
			{
				if(copymail)
				{
					return;
				}
				else
				{
					int tries=0;
					while(tries<10&&!(new File(Path,Filename)).delete())
					{
						TIME.Delay(100);
						tries++;
					}
				}
				return;
			}
			else
			{
				if(MailSender.indexOf('@')!=-1)
				{
					OwnBuf=MailSender.substring(0,MailSender.indexOf('@'));
					if(OwnBuf.indexOf(' ')!=-1)
						OwnBuf=OwnBuf.substring(0,OwnBuf.indexOf(' '));
				}
				else
					OwnBuf=MailSender;
				TitleBuf="[Mail failure!]"+TitleBuf;
				//Mark by WilliamWey
				//¦b¥¿±`±¡ªp¤U, À³¸Ó¤£·|µo¥Í mailtemp ªºÀÉ®×±H¤£¥X¥h°ÝÃD§a!!
				//copymail=false;
				//
			}
		}
		else
		{
			if(!ColaServer.UFDList.exist(OwnBuf))
				return;
			else
				OwnBuf=ColaServer.UFDList.getPass(OwnBuf).IDItem;
		}	
		
		ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" user "+UFD.ID+" mail to "+OwnBuf);
		
		{
			char appendChar='A';
			String tmpPath=ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(OwnBuf.charAt(0))+File.separator+OwnBuf+File.separator;
			do
			{
				SaveFileName="M."+((int)((new Date()).getTime()/1000))+"."+appendChar;
				appendChar++;
			}while((new File(tmpPath,SaveFileName)).exists());
		}
		byte writebuf[]=new byte[256];

		try
		{
			String MailOwner=null;
			
			if(MailSender==null)
				MailOwner=UFD.ID+" ("+UFD.NickName+")";
			else
				MailOwner=MailSender;
			if(copymail)
			{
				inputfile=new RandomAccessFile(Path+File.separator+Filename,"r");
				outputfile=new RandomAccessFile(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(OwnBuf.charAt(0))+File.separator+OwnBuf+File.separator+SaveFileName,"rw");
				byte databuf[]=new byte[(int)inputfile.length()];

				outputfile.writeBytes(Prompt.Msgs[46]+STRING.Cut(ColaServer.BBSUsers[pid].UFD.ID+" ("+ColaServer.BBSUsers[pid].UFD.NickName+")",73)+"\r\n");
				outputfile.writeBytes(Prompt.Msgs[65]+STRING.Cut(TitleBuf,73)+"[m\r\n");
				outputfile.writeBytes(Prompt.Msgs[271]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),73)+"[m\r\n");
				outputfile.writeBytes(Prompt.Msgs[237]);
				outputfile.writeBytes("\r\n");
				inputfile.read(databuf);
				outputfile.write(databuf);
			}
			else
			{
				int tries=0;
				while(tries<10&&!(new File(Path,Filename)).renameTo(new File(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(OwnBuf.charAt(0))+File.separator+OwnBuf+File.separator+SaveFileName)))
				{
					TIME.Delay(100);
					tries++;
				}	
			}	
			RecordHandler.append(ColaServer.UFDList.getPass(OwnBuf),new MailType(SaveFileName,MailOwner,TitleBuf),MailType.myMailHome(ColaServer.INI.BBSHome,OwnBuf),Consts.DotDir);
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
				{
					inputfile.close();
					inputfile=null;
				}	
				if(outputfile!=null)
				{
					outputfile.close();
					outputfile=null;
				}	
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}	
	}

	//public abstract void SaveBrc();
	public abstract void sends(byte b[]);
	public abstract void sends(String b);
	public boolean ValidID(StringBuffer str)
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
		if(str.toString().equalsIgnoreCase("new"))
			return false;
		else
			return true;
	}

	public boolean hasNewMail()
	{
		MailType mt = new MailType();
		
		int len = (int)RecordHandler.recordNumber(mt, ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator + UFD.ID+File.separator, Consts.DotDir);
		
		//Changerd by WilliamWey for «H¥ó¦pªG¬O -1 ¤~¬O¨S¦³«H¥ó
		if (len == -1)
			return false;
		
		RecordHandler.getRecord(len, ColaServer.UFDList.getPass(UFD.ID), mt, ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator + UFD.ID + File.separator, Consts.DotDir);
		return !mt.isRead();
	}

	public static boolean hasNewMail(UserFileData ufd)
	{
		MailType mt = new MailType();
		int len = (int)RecordHandler.recordNumber(mt, ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(ufd.ID.charAt(0)) + File.separator+ufd.ID + File.separator, Consts.DotDir);
		
		//Changerd by WilliamWey for «H¥ó¦pªG¬O -1 ¤~¬O¨S¦³«H¥ó
		if (len == -1)
			return false;

		RecordHandler.getRecord(len, ColaServer.UFDList.getPass(ufd.ID), mt, ColaServer.INI.BBSHome + "mail" + File.separator + Character.toUpperCase(ufd.ID.charAt(0)) + File.separator + ufd.ID + File.separator, Consts.DotDir);
		return !mt.isRead();
	}

	/**
	 * ·sªºªF¦è
	 */
	public ObjectOutputStream oos=null;
	public ObjectInputStream ois=null;
	/**
	 * ·sªº for ChatRoom
	 */
	public String getBBSID()
	{
		if(UFD!=null&&UFD.ID!=null)
			return UFD.ID;
		return "guest";
	}  
  /** ¦³·s¨Ï¥ÎªÌ¤W¯¸®É Server ºÝªº¹ïÀ³µ¦²¤ */
	public abstract void AddOnlineUser(BBSUser theUser);

  /** ¦³¨Ï¥ÎªÌ¤U¯¸®É Server ºÝªº¹ïÀ³µ¦²¤ */
	public abstract void RemoveOnlineUser(BBSUser theUser);

  /** ¦³¨Ï¥ÎªÌª¬ºAÅÜ§ó®É Server ºÝªº¹ïÀ³µ¦²¤ */
	public abstract void UserStateChanged(BBSUser theUser);
  
  /** ¦³¤H©I¥s talk ®É Server ºÝªº¹ïÀ³µ¦²¤ */
  public abstract void doTalkRequest(TalkUser theTalkUser,int talkMode);

  /** ¦³¤H°±¤î©I¥s talk ®É Server ºÝªº¹ïÀ³µ¦²¤ */
  public abstract void cancelTalkRequest(TalkUser theTalkUser);
}
