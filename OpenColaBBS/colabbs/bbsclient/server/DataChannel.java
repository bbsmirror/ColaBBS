
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.server;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;

import colabbs.record.*;

import colabbs.*;
import colabbs.telnet.Keys;
import colabbs.telnet.TelnetUser;
import colabbs.bbsclient.server.ClientUser;
import colabbs.bbsclient.CmdTable;
import colabbs.bbsclient.CmdItem;
import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.board.BBSTPReadPost;
import colabbs.bbstp.board.BBSTPSendPost;
import colabbs.bbstp.board.BBSTPPostSize;
import colabbs.bbstp.board.BBSTPSendPostStatus;
import colabbs.bbstp.mail.BBSTPReadMail;
import colabbs.bbstp.mail.BBSTPSendMail;
import colabbs.bbstp.mail.BBSTPMailSize;
import colabbs.bbstp.mail.BBSTPSendMailStatus;
import colabbs.bbstp.user.BBSTPTalkCheck;
import colabbs.bbstp.user.BBSTPTalkReply;
import colabbs.bbstp.user.BBSTPTalkRequest;

import colabbs.DATA.BOARD.*;
import colabbs.DATA.USERFILEDATA.*;
import colabbs.UTILS.*;

/**
 * ¥Nªí¤@­Ó¤w¸g«Ø¥ß°_ªº DataChannel ¤§Ãþ§O
 */
public class DataChannel extends Thread implements TalkUser
{
	private Date LastSig=null;
  private PassItem UserPassItem=null;
	private UserFileData UFD;
  private boolean waiting=false;
	private int uid;
  private byte inkey=0;
  private TalkUser RequestUser=null;
	public static CmdTable myCmdTable=new CmdTable();
	public Socket mySocket=null;
  public InetAddress from=null;
  public String Home=null;
  public InputStream is=null;
  public OutputStream os=null;
  public ObjectInputStream ois=null;
  public ObjectOutputStream oos=null;

	public DataChannel()
	{
	}

  public void finalize()
  {
  	ClientDataServer.AllDataChannels.removeElement(this);
  }

	public boolean checkpasswd(String p1,String p2)
	{
		return ColaServer.Crypter.DoCrypt(p1,p2).equals(p2);
	}

  public void run()
  {
		boolean loginok=false;

		try
		{
			Object cmd=null;
			while(ois!=null)
			{
				LastSig=new Date();
				cmd=ois.readObject();
				try
				{
					if(loginok)
					{
  	        if(myCmdTable!=null)
    	      {
      	      CmdItem myItem=(CmdItem)myCmdTable.getItem(cmd);
        	    if(myItem!=null)
          	  {
            	  if(myItem.myObject!=null)
              	  myItem.myMethod.invoke(myItem.myObject,new Object[]{this,cmd});
	              else
  	              myItem.myMethod.invoke(this,new Object[]{this,cmd});
    	        }
      	    }
            return;
					}
					else
					{
    	      if(cmd instanceof Login)
      	    {
        	    Login myLogin=(Login)cmd;

			      	UserPassItem=ColaServer.UFDList.getPass(myLogin.UserName);

      				if(UserPassItem==null) //No this id.
			      	{
    						oos.writeObject(new LoginState(1));
					    	oos.flush();
      					return;
			      	}
      				else if(UserPassItem.IDItem.equalsIgnoreCase("SYSOP"))
			      	{
			      		if(checkpasswd( myLogin.PassWord, UserPassItem.PassWordItem ))  //´ú¸Õ±K½X
      					{
			      			UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
      						UFD.Perm=~(int)0;
			      			uid=UserPassItem.uid;
			      		}
      					else
			      		{
      						ColaServer.BBSlog.Write(from+" answer incorrect password for SYSOP....");
      						oos.writeObject(new LoginState(2));
						    	oos.flush();
                  return;
			      		}
      				}
			      	else if(!UserPassItem.IDItem.equalsIgnoreCase("guest"))
      				{
      					if(checkpasswd( myLogin.PassWord, UserPassItem.PassWordItem ))  //´ú¸Õ±K½X
			      		{
      						UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
			      			uid=UserPassItem.uid;
			      		}
      					else
			      		{
      						ColaServer.BBSlog.Write(from+" answer incorrect password for "+UserPassItem.IDItem);
      						oos.writeObject(new LoginState(2));
						    	oos.flush();
                  return;
			      		}
      				}
			      	else
      				{
			      		if(ColaServer.INI.UseGuest)
      					{
			      			UFD = ColaServer.UFDList.login(UserPassItem.IDItem);
						      uid=UserPassItem.uid;
			      		}
                else
                {
      						oos.writeObject(new LoginState(3));
						    	oos.flush();
  			      		return;
                }
      					//
      				}
							oos.writeObject(new LoginState(0));
				    	oos.flush();
              loginok=true;
  	        }
		  		}
				}
				catch(ClassCastException e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(ois!=null)
				{
					ois.close();
					ois=null;
				}
				if(oos!=null)
				{
					oos.close();
					oos=null;
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
			}
		}
  }

  /** ±H¯¸¤º«H¥Î¡A±q BBSUser «þ¹L¨Óªº */
	private static void MailFile(DataChannel theChannel,String OwnBuf,String Path,String Filename,String TitleBuf,String MailSender,boolean copymail)
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
				successflag=BBSUser.doInternetMail(MailSender,OwnBuf,bis,TitleBuf);
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
				//Add by WilliamWey
				//copymail ¯u¥¿ªº²[¸q¬O?
				if(copymail)
				{
					return;
				}
				//
				else
				{
					int tries=0;
					while(tries<10&&!(new File(Path,Filename)).delete())
					{
						TIME.Delay(100);
						tries++;
					}
				}
				//
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
				copymail=false;
			}
		}
		else
		{
			if(!ColaServer.UFDList.exist(OwnBuf))
				return;
			else
				OwnBuf=ColaServer.UFDList.getPass(OwnBuf).IDItem;
		}

		ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" user "+theChannel.UFD.ID+" mail to "+OwnBuf);

    SaveFileName=createFile(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(OwnBuf.charAt(0))+File.separator+OwnBuf+File.separator);
		byte writebuf[]=new byte[256];

		try
		{
			String MailOwner=null;

			if(MailSender==null)
				MailOwner=theChannel.UFD.ID+" ("+theChannel.UFD.NickName+")";
			else
				MailOwner=MailSender;
			if(copymail)
			{
				inputfile=new RandomAccessFile(Path+File.separator+Filename,"r");
				outputfile=new RandomAccessFile(ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(OwnBuf.charAt(0))+File.separator+OwnBuf+File.separator+SaveFileName,"rw");
				byte databuf[]=new byte[(int)inputfile.length()];

				outputfile.writeBytes(Prompt.Msgs[46]+STRING.Cut(theChannel.UFD.ID+" ("+theChannel.UFD.NickName+")",73)+"\r\n");
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

	private static void AddList(DataChannel theClient,String BoardName,String filename)
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
		synchronized(theClient.UserPassItem.BRC)
		{
			Vector theBrc=(Vector)theClient.UserPassItem.BRC.get(BoardName);
			if(theBrc==null)
			{
				theBrc=new Vector();
				theClient.UserPassItem.BRC.put(BoardName,theBrc);
			}
			int brcnum=theBrc.size();
			theClient.UserPassItem.BRCflag=true;
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
	}

	private static String createFile(String thePath)
	{
		char appendChar='A';
		String SaveFileName;
		// search the file name for create, just for safe....
		appendChar='A'-1; //for creat file and refresh board time....
		long theTime=-1;

		theTime=((new Date()).getTime()/1000);
		do
		{
			appendChar++;
			SaveFileName="M."+theTime+"."+appendChar;
		}while((new File(thePath,SaveFileName)).exists());
		return SaveFileName;
	}

  public static void ReadPost(DataChannel theChannel,BBSTPSendPost sp)
  {
    BoardItem BoardBuf=null;
		BoardBuf = ColaServer.BList.get(sp.BoardName);

    if(!BBS.CheckOnePerm(theChannel.UFD.Perm,BoardBuf.Level)) //­YÅv­­¤£¦X¡A«h°e¥X¿ù»~°T®§
    {
    	try
      {
	    	theChannel.oos.writeObject(new BBSTPSendPostStatus(1));
	    	theChannel.oos.flush();
      }
      catch(IOException e){}
    	return;
    }
    PostType pt=new PostType();
  	String CurrentPath=ColaServer.INI.BBSHome+"boards"+File.separator+BoardBuf.Name+File.separator;

		if(!BoardBuf.JunkBoard)
    	theChannel.UFD.NumPosts++;
    RandomAccessFile fos=null;
    try
    {
      BBSTPPostSize theSize=(BBSTPPostSize)theChannel.ois.readObject();
      byte buffer[]=new byte[theSize.size];
//      theChannel.is.read(buffer);
      for(int c=0;c<theSize.size;c++)
      	buffer[c]=(byte)theChannel.is.read();
  	  String SaveFileName=createFile(CurrentPath);
    	fos=new RandomAccessFile(CurrentPath+SaveFileName,"rw");
      //header
      if(sp.Title!=null)
			{
				if(BoardBuf!=null)
				{
					if(BoardBuf.AnonyDefault&&sp.anonymous)
						fos.writeBytes(Prompt.Msgs[46]+STRING.Cut("Anonymous"+Prompt.Msgs[242],46)+Prompt.Msgs[183]+STRING.Cut(BoardBuf.Name,20)+"[m\r\n");
//					else if(EditArticle)
//						TargetFile.writeBytes(Prompt.Msgs[46]+STRING.Cut(OwnBuf,46)+Prompt.Msgs[183]+STRING.Cut(Board.Name,20)+"[m\r\n");
					else
						fos.writeBytes(Prompt.Msgs[46]+STRING.Cut(theChannel.UFD.ID+" ("+theChannel.UFD.NickName+")",46)+Prompt.Msgs[183]+STRING.Cut(BoardBuf.Name,20)+"[m\r\n");
				}
				else
					fos.writeBytes(Prompt.Msgs[46]+STRING.Cut(theChannel.UFD.ID+" ("+theChannel.UFD.NickName+")",73)+"\r\n");
				fos.writeBytes(Prompt.Msgs[65]+STRING.Cut(sp.Title,73)+"[m\r\n");

				fos.writeBytes(Prompt.Msgs[271]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),73)+"[m\r\n");
				fos.writeBytes(Prompt.Msgs[237]);
				fos.writeBytes("\r\n");
			}
      //body
      fos.write(buffer);
      //add signature
			if(sp.signature!=0&&(new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theChannel.UFD.ID.charAt(0))+File.separator+theChannel.UFD.ID+File.separator,"signatures")).exists())
			{
				int i;
        String ReadBuffer=null;
				DataInputStream SigFile=new DataInputStream(new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theChannel.UFD.ID.charAt(0))+File.separator+theChannel.UFD.ID+File.separator+"signatures"));

				fos.writeBytes("\r\n--\r\n");
				for(i=0;i<(int)((sp.signature-1)*ColaServer.INI.MaxSigLines);i++)
					SigFile.readLine();
				for(i=0;i<ColaServer.INI.MaxSigLines;i++)
				{
					ReadBuffer=SigFile.readLine();
					if(ReadBuffer!=null)
						fos.writeBytes(ReadBuffer);
				}
			}
      //add tag
			try
			{
      	fos.writeBytes("\r\n--\r\n");
        if(BoardBuf!=null&&BoardBuf.Anonymous&&sp.anonymous)
        {
      		fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
					fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[51]);
        }
				else if(ColaServer.INI.PrintOrg||BoardBuf==null)
				{
					fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
					fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m[FROM: "+theChannel.Home+"][m");
				}
				else
					fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
      }
			catch(NumberFormatException e){}

	    ColaServer.BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+theChannel.UFD.ID+" posted "+sp.Title);
	    if(BoardBuf.Anonymous&&sp.anonymous)
  	  	RecordHandler.append(BoardBuf,new PostType(SaveFileName,"Anonymous",sp.Title,sp.localSave?(byte)'L':(byte)'S'),CurrentPath,".DIR");
    	else
    		RecordHandler.append(BoardBuf,new PostType(SaveFileName,theChannel.UFD.ID,sp.Title,sp.localSave?(byte)'L':(byte)'S'),CurrentPath,".DIR");
    	AddList(theChannel,BoardBuf.Name,SaveFileName);

	    if(SaveFileName.lastIndexOf(".")>=0)//refresh the filetime of board
  	  {
    		int addnum=(int)(SaveFileName.charAt(SaveFileName.lastIndexOf('.')+1)-'A');
      	SaveFileName=SaveFileName.substring(2,SaveFileName.lastIndexOf("."));
//      	BoardBuf.filetime=0;
	      try
  	    {
    	  	BoardBuf.filetime=(new Long(SaveFileName)).longValue();
	    	  BoardBuf.filetime=BoardBuf.filetime*1000+addnum;
	      }
  	    catch(NumberFormatException e){}
    	}
    	theChannel.oos.writeObject(new BBSTPSendPostStatus(0));
    	theChannel.oos.flush();
    }
    catch(Exception e)
    {
    	try
      {
	    	theChannel.oos.writeObject(new BBSTPSendPostStatus(2));
	    	theChannel.oos.flush();
      }
      catch(IOException e1){}
    }
    finally
    {
    	try
      {
				if(fos!=null)
  	    	fos.close();
      }
      catch(IOException e){}
    }
  }

  public static void ReadMail(DataChannel theChannel,BBSTPSendMail sm)
  {
  	if(!BBS.CheckOnePerm(theChannel.UFD.Perm,Perm.Post))//­YÅv­­¤£¦X¡A«h°e¥X¿ù»~°T®§
    {
    	try
      {
	    	theChannel.oos.writeObject(new BBSTPSendMailStatus(1));
	    	theChannel.oos.flush();
      }
      catch(IOException e){}
    	return;
    }
    if(sm.Receiver.indexOf('@')!=-1) //­Y¤£¤©³\±H«H¨ì Internet¡A«h°e¥X¿ù»~°T®§
    {
    	if(!ColaServer.INI.InternetMail)
      {
      	try
        {
		    	theChannel.oos.writeObject(new BBSTPSendMailStatus(2));
		    	theChannel.oos.flush();
        }
	      catch(IOException e){}
      	return;
      }
    }
    RandomAccessFile fos=null;
    String CurrentPath=ColaServer.INI.BBSHome+"mailtemp"+File.separator;
    String SaveFileName=null;
    try
    {
  	  BBSTPMailSize theSize=(BBSTPMailSize)theChannel.ois.readObject();
    	byte buffer[]=new byte[theSize.size];
//	    theChannel.is.read(buffer);
      for(int c=0;c<theSize.size;c++)
      	buffer[c]=(byte)theChannel.is.read();
  	  SaveFileName=createFile(CurrentPath);
    	fos=new RandomAccessFile(CurrentPath+SaveFileName,"rw");
      //add header
      fos.writeBytes(Prompt.Msgs[46]+STRING.Cut(theChannel.UFD.ID+" ("+theChannel.UFD.NickName+")",73)+"\r\n");
      fos.writeBytes(Prompt.Msgs[65]+STRING.Cut(sm.Title,73)+"[m\r\n");
      fos.writeBytes(Prompt.Msgs[271]+STRING.Cut(ColaServer.SysDATE.DateFormatter2.format(new Date()),73)+"[m\r\n");
      fos.writeBytes(Prompt.Msgs[237]);
      fos.writeBytes("\r\n");
      //mail body
	    fos.write(buffer);
      //add signature
			if(sm.signature!=0&&(new File(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theChannel.UFD.ID.charAt(0))+File.separator+theChannel.UFD.ID+File.separator,"signatures")).exists())
			{
				int i;
        String ReadBuffer=null;
				DataInputStream SigFile=new DataInputStream(new FileInputStream(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theChannel.UFD.ID.charAt(0))+File.separator+theChannel.UFD.ID+File.separator+"signatures"));

				fos.writeBytes("\r\n--\r\n");
				for(i=0;i<(int)((sm.signature-1)*ColaServer.INI.MaxSigLines);i++)
					SigFile.readLine();
				for(i=0;i<ColaServer.INI.MaxSigLines;i++)
				{
					ReadBuffer=SigFile.readLine();
					if(ReadBuffer!=null)
						fos.writeBytes(ReadBuffer);
				}
			}
      //add tag
			try
			{
				fos.writeBytes("\r\n--\r\n");
				fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m"+Prompt.Msgs[158]+ColaServer.INI.BBSName+" "+ColaServer.INI.BBSAddress+Prompt.Msgs[388]+"[m");
				fos.writeBytes("[m[1;3"+(NUMBER.getIntRnd(7)+1)+"m[FROM: "+theChannel.Home+"][m");
			}
			catch(NumberFormatException e){}
    }
    catch(Exception e){}
    finally
    {
    	try
      {
				if(fos!=null)
        {
  	    	fos.close();
          if(SaveFileName!=null)
			    	MailFile(theChannel,sm.Receiver,CurrentPath,SaveFileName,sm.Title,theChannel.UFD.ID,false);
        }
	    	theChannel.oos.writeObject(new BBSTPSendMailStatus(0));
	    	theChannel.oos.flush();
      }
      catch(IOException e)
      {
      	try
        {
		    	theChannel.oos.writeObject(new BBSTPSendMailStatus(3));
		    	theChannel.oos.flush();
        }
	      catch(IOException e1){}
    	}
    }
  }

	/**
   * °e¥X¸ê®Æ¨ì Client ºÝ
   */
  public static void SendPost(DataChannel theChannel,BBSTPReadPost rp)
  {
    BoardItem BoardBuf=null;
    PostType pt=new PostType();
		BoardBuf = ColaServer.BList.get(rp.BoardName);

  	String CurrentPath=ColaServer.INI.BBSHome+"boards"+File.separator+BoardBuf.Name+File.separator;

  	if(!RecordHandler.getRecord(rp.index,BoardBuf,pt,CurrentPath,".DIR"))
    	return;

    ViewFileType vft=(ViewFileType)pt;

    if(!vft.isLink())
    {
    	File article=new File(CurrentPath,pt.deleteBody());
      FileInputStream fis=null;
      try
      {
      	//send article body following....
/*        RandomAccessFile fis=new RandomAccessFile(article,"r");
        int flen=(int)article.length();
        byte buffer[]=new byte[flen];

        fis.read(buffer);
        theChannel.oos.writeObject(new BBSTPPostSize(flen));
        theChannel.oos.flush();
        theChannel.os.write(buffer);
        theChannel.os.flush();*/
        fis=new FileInputStream(article);
        int flen=(int)article.length();
        byte buffer[]=new byte[flen];

        fis.read(buffer);
        theChannel.oos.writeObject(new BBSTPPostSize(flen));
        theChannel.oos.flush();
        theChannel.os.write(buffer);
        theChannel.os.flush();
        AddList(theChannel,BoardBuf.Name,pt.deleteBody());
      }
      catch(FileNotFoundException e)
      {
//				DelRecord(rp.index);
				return;
      }
      catch(IOException e)
      {
      	return;
      }
      finally
      {
      	try
        {
	      	if(fis!=null)
  	      	fis.close();
        }
        catch(IOException e){}
      }
/*    	if(article.exists())
      {
      	//send article body following....
        FileInputStream fis=new FileInputStream(article);
        int flen=article.length();
        byte buffer[]=new byte[flen];
        fis.read(buffer);
        oos.write(buffer);
      }
			else
			{
				DelRecord(rp.index);
				return;
			}*/
		}
		else
		{
    	File article=new File(pt.deleteBody());
      FileInputStream fis=null;
      try
      {
      	//send linked article body following....
				//((TelnetUser)ColaServer.BBSUsers[pid]).ansimore(rt.deleteBody());
        fis=new FileInputStream(article);
        int flen=(int)article.length();
        byte buffer[]=new byte[flen];

        fis.read(buffer);
        theChannel.oos.writeObject(new BBSTPPostSize(flen));
        theChannel.oos.flush();
        theChannel.os.write(buffer);
        theChannel.os.flush();
        AddList(theChannel,BoardBuf.Name,pt.deleteBody());
      }
      catch(FileNotFoundException e)
      {
//				DelRecord(rp.index);
				return;
      }
      catch(IOException e)
      {
      	return;
      }
      finally
      {
      	try
        {
	      	if(fis!=null)
  	      	fis.close();
        }
        catch(IOException e){}
      }
/*			if((new File(pt.deleteBody())).exists())
      {
      	//send linked article body following....
				//((TelnetUser)ColaServer.BBSUsers[pid]).ansimore(rt.deleteBody());
        FileInputStream fis=new FileInputStream(article);
        int flen=article.length();
        byte buffer[]=new byte[flen];
        fis.read(buffer);
        oos.write(buffer);
      }
			else
			{
				DelRecord(rp.index);
				return;
			}*/
		}
  }

  public static void SendMail(DataChannel theChannel,BBSTPReadMail rm)
  {
    MailType mt=new MailType();

		String CurrentPath=ColaServer.INI.BBSHome+"mail"+File.separator+Character.toUpperCase(theChannel.UFD.ID.charAt(0))+File.separator+theChannel.UFD.ID+File.separator;

  	if(!RecordHandler.getRecord(rm.index,theChannel.UserPassItem,mt,CurrentPath,".DIR"))
    	return;

    ViewFileType vft=(ViewFileType)mt;

    if(!vft.isLink())
    {
    	File article=new File(CurrentPath,mt.deleteBody());
      FileInputStream fis=null;
      try
      {
      	//send article body following....
        fis=new FileInputStream(article);
        int flen=(int)article.length();
        byte buffer[]=new byte[flen];

        fis.read(buffer);
        theChannel.oos.writeObject(new BBSTPMailSize(flen));
        theChannel.oos.flush();
        theChannel.os.write(buffer);
        theChannel.os.flush();
      }
      catch(FileNotFoundException e)
      {
//				DelRecord(rp.index);
				return;
      }
      catch(IOException e)
      {
      	return;
      }
      finally
      {
      	try
        {
	      	if(fis!=null)
  	      	fis.close();
        }
        catch(IOException e){}
      }
/*    	if(article.exists())
      {
      	//send article body following....
        FileInputStream fis=new FileInputStream(article);
        int flen=article.length();
        byte buffer[]=new byte[flen];
        fis.read(buffer);
        oos.write(buffer);
      }
			else
			{
				DelRecord(rp.index);
				return;
			}*/
		}
		else
		{
    	File article=new File(mt.deleteBody());
      FileInputStream fis=null;
      try
      {
      	//send linked article body following....
				//((TelnetUser)ColaServer.BBSUsers[pid]).ansimore(rt.deleteBody());
        fis=new FileInputStream(article);
        int flen=(int)article.length();
        byte buffer[]=new byte[flen];

        fis.read(buffer);
        theChannel.oos.writeObject(new BBSTPMailSize(flen));
        theChannel.oos.flush();
        theChannel.os.write(buffer);
        theChannel.os.flush();
      }
      catch(FileNotFoundException e)
      {
				return;
      }
      catch(IOException e)
      {
      	return;
      }
      finally
      {
      	try
        {
	      	if(fis!=null)
  	      	fis.close();
        }
        catch(IOException e){}
      }
		}
  }

  public static void TalkRequest(DataChannel theChannel,BBSTPTalkRequest tr)
  {
  	int index=BBS.IfOnline(tr.UserID);
    System.out.println("Paging "+index);
    if(index!=-1) //¦pªG¦b½u¤W¦^À³¥Ñ¤U­±¨ç¦¡­t³d (:
    {
//      BBSTPTalkCheck tc=new BBSTPTalkCheck(theid,tr.talkMode,tr.UserID);
      ColaServer.BBSUsers[index].doTalkRequest(theChannel,tr.talkMode);
      theChannel.waiting=true;
      theChannel.RequestUser=null;
      while(theChannel.waiting)
      {
				try
				{
					if(theChannel.is.available()!=0)
					{
//						int inkey;
						if((theChannel.inkey=(byte)theChannel.is.read())==(byte)3||theChannel.inkey==(byte)-1)
						{
							ColaServer.BBSUsers[index].cancelTalkRequest(theChannel);
              break;
						}
					}
					TIME.Delay(300);
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
	    theChannel.waiting=false;
      if(theChannel.RequestUser!=null)
	      theChannel.doTalk(theChannel.RequestUser);
/*    	if(ColaServer.BBSUsers[index] instanceof ClientUser)
      {
		  	((ClientUser)ColaServer.BBSUsers[index]).
      }
      else if(ColaServer.BBSUsers[index] instanceof TelnetUser)
      {
      }*/
  	}
    else //¦pªG¤£¦b½u¤Wª½±µµ¹¤©¦^À³
    {
    	try
      {
	    	theChannel.oos.writeObject(new BBSTPTalkReply(-1,-1,(String)null));
	    	theChannel.oos.flush();
      }
      catch(IOException e){}
    }
  }

  public static void TalkReply(DataChannel theChannel,BBSTPTalkReply tr)
  {
  	TalkUser tu=(TalkUser)TalkPair.getTalkUser(tr.tid);
    if(tu==null)
    	return;
    TalkPair.removeTalkPair(tr.tid);
//    System.out.println("talk reply: reply number="+tr.ReplyNumber+",waiting="+tu.stillWaiting());
    if(tu.stillWaiting())
    {
    	tu.doTalkReply(theChannel,tr.ReplyNumber,tr.ReplyString);
    	if(tr.ReplyNumber==0)
      	theChannel.doTalk(tu);
    }
  }

  public void doTalkReply(TalkUser tu,int theReplyNumber,String theReplyString)
  {
  	try
    {
		 	oos.writeObject(new BBSTPTalkReply(-1,theReplyNumber,theReplyString));
		 	oos.flush();

      waiting=false;
      RequestUser=tu;

      if(theReplyNumber!=0)
     		tu.sendQuit();
    }
    catch(IOException e)
    {
    	tu.sendQuit();
    }
  }

  public void doTalk(TalkUser tu)
  {
  	BufferedInputStream bis=new BufferedInputStream(is);
//  	byte buf=0;
    try
    {
  		while((inkey=(byte)bis.read())!=(byte)-1)
    	{
//      	System.out.println(""+inkey);
    		switch(inkey)
	      {
//        	case 3:
//          case 4:
//          case -1: //Quit
//          	tu.sendQuit();
//          	break;
		    	case 8:
          	tu.sendBackSpace();
          	break;
          case 10:
          	break;
      		case 13:
          	tu.sendNewLine();
	      		break;
		      default:
          	tu.sendMessage(inkey);
  		    	break;
      	}
      }
    }
    catch(Exception e){}
    finally
    {
    	sendQuit();
    	tu.sendQuit();
    }
  }

	public void sendMessage(byte theMessage)
	{
		//TODO: implement this colabbs.TalkUser method;
    try
    {
	    os.write(theMessage);
    }
    catch(Exception e)
    {
    	sendQuit();
    }
	}

	public void sendNewLine()
	{
		//TODO: implement this colabbs.TalkUser method;
    try
    {
	    os.write('\r');
	    os.write('\n');
    }
    catch(Exception e)
    {
    	sendQuit();
    }
	}

	public void sendBackSpace()
	{
		//TODO: implement this colabbs.TalkUser method;
    try
    {
	    os.write(8);
    }
    catch(Exception e)
    {
    	sendQuit();
    }
	}

	public void sendQuit()
	{
		//TODO: implement this colabbs.TalkUser method;
    try
    {
      if(ois!=null)
      {
      	ois.close();
        ois=null;
      }
      if(oos!=null)
      {
      	oos.close();
        oos=null;
      }
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
    }
    catch(IOException e){}
	}

  public UserFileData getUFD()
  {
  	return UFD;
  }

  public boolean stillWaiting()
  {
  	return waiting;
  }
}
