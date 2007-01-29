package colabbs.net;

import java.io.*;
import java.net.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;
import colabbs.DATA.VOTE.*;

/**
 * This type was writen by yhwu.
 */
public class SMTPClient
{
//	String Address=null,Receiver=null;
//	String sender=null,title=null; //for head
	
	private Socket theClient=null; //for connect
	private DataOutputStream pw=null;
	private DataInputStream br=null;
/**
 * This method was writen by yhwu.
 */
public SMTPClient()
{
}
/**
 * This method was writen by yhwu.
 */
public boolean connect(String Address)
{
	try
	{
//		System.out.println("SMTPClient connect to "+Address);
		String rbuf=null;
		
		//Changed by WilliamWey
		//theClient=new Socket(Address,25);
		theClient=new Socket(ColaServer.INI.SMTPAddress, ColaServer.INI.SMTPPort);
		//
		
		pw=new DataOutputStream(new BufferedOutputStream(theClient.getOutputStream()));
		br=new DataInputStream(theClient.getInputStream());
		rbuf=getResponse();
		if(rbuf.charAt(0)!='2')
		{
			if(pw!=null)
			{
				pw.close();
				pw=null;
			}
			if(br!=null)
			{
				br.close();
				br=null;
			}
			if(theClient!=null)
			{
				theClient.close();
				theClient=null;
			}
			ColaServer.BBSlog.Write("Sending mail connect error: "+rbuf);
			return false;
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 * @param thegroup java.lang.String
 */
private boolean Data(InputStream is,String sender,String title)
{
	DataInputStream dis=null;
	String rbuf=null;
	try
	{
		pw.writeBytes("Data\r\n");
		rbuf=getResponse();
		if(rbuf.charAt(0)!='3'||rbuf.charAt(0)!='2')
			ColaServer.BBSlog.Write("Sending mail command 'Data' error: "+rbuf);
		try
		{
			dis=new DataInputStream(is);
			if(dis==null)
				return false;
			pw.writeBytes("From: "+sender+"\r\n");
			pw.writeBytes("Subject: "+title+"\r\n");
			pw.writeBytes("Content-Type: text/plain\r\n");
			pw.writeBytes("Content-Transfer-Encoding: 8-bit\r\n");
			pw.writeBytes("\r\n\r\n");
			//the mail body.....
//							System.out.println("post file : "+thept.deleteBody());
			
			String buf=null;
			
			//Remark by WilliamWey
			//Cut ±¼¤Ï¦Ó©_©Ç
			/*while((buf=dis.readLine())!=null) // cut the mail head of bbs....
				if(buf.length()==0)
					break;*/
			//
			while((buf = dis.readLine()) != null)
			{
				if(buf.length()>0&&buf.charAt(0)=='.')
					pw.write((byte)'.');
				pw.writeBytes(buf + "\r\n");
			}
			pw.writeBytes(".\r\n");
			getResponse();
			//System.out.println(getResponse());
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
			return false;
		}
		finally
		{
			if(dis!=null)
			{
				dis.close();
				dis=null;
			}
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 * @return java.lang.String
 */
private String getResponse()
{
	String rbuf=null;
	StringBuffer clientdata=new StringBuffer();
	int r=0;

	try
	{
		pw.flush();

ReplyLoop:	
		while((r=br.read())!=-1)
		{
			switch(r)
			{
				case 10:
					break;
				case 13:
					rbuf=clientdata.toString();
					clientdata.setLength(0);
					break ReplyLoop;
				default:
					clientdata.append((char)r);
					break;
			}
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
	}
//	System.out.println("Server response: "+rbuf);
	return rbuf;
}
/**
 * This method was writen by yhwu.
 * @param thegroup java.lang.String
 */
private boolean Helo()
{
	String rbuf=null;
	try
	{
		pw.writeBytes("Helo <"+ColaServer.INI.BBSAddress+">\r\n");
		rbuf=getResponse();
		//System.out.println(rbuf);
		if(rbuf.charAt(0)!='2')
		{
			ColaServer.BBSlog.Write("Sending mail command 'Helo' error: "+rbuf);
			return false;
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 * @param thegroup java.lang.String
 */
private boolean MailFrom(String theSender)
{
	String rbuf=null;
	try
	{
		//pw.writeBytes("Mail from: <"+theSender+">\r\n");
		if (theSender.endsWith(">"))
			pw.writeBytes("Mail from: "+theSender+"\r\n");
		else
			pw.writeBytes("Mail from: <"+theSender+">\r\n");
		
		rbuf=getResponse();
		//System.out.println(rbuf);
		if(rbuf.charAt(0)!='2')
		{
			ColaServer.BBSlog.Write("Sending mail command 'Mail from' error: "+rbuf);
			return false;
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 */
private void quit()
{
	String rbuf=null;
	try
	{
//		pw.println("quit");
		pw.writeBytes("quit\r\n");
		rbuf=getResponse();
		//System.out.println(rbuf);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * This method was writen by yhwu.
 * @param thegroup java.lang.String
 */
private boolean RcptTo(String theReceiver)
{
	String rbuf=null;
	try
	{
		//pw.writeBytes("Rcpt to: <"+theReceiver+">\r\n");
		if (theReceiver.endsWith(">"))
			pw.writeBytes("Rcpt to: "+theReceiver+"\r\n");
		else
			pw.writeBytes("Rcpt to: <"+theReceiver+">\r\n");
		rbuf=getResponse();
		//System.out.println(rbuf);
		if(rbuf.charAt(0)!='2')
		{
			ColaServer.BBSlog.Write("Sending mail command 'Rcpt to' error: "+rbuf);
			return false;
		}
	}
	catch(IOException e)
	{
		e.printStackTrace();
		return false;
	}
	return true;
}
/**
 * This method was writen by yhwu.
 * @return boolean
 * @param is java.io.InputStream
 * @param receiver java.lang.String
 * @param sender java.lang.String
 * @param title java.lang.String
 */
public synchronized boolean sendMail(InputStream is, String receiver ,String sender, String title)
{
	if(receiver.indexOf('@')==-1)
		return false;
		
	String user=receiver.substring(0,receiver.indexOf('@')),Addr=receiver.substring(receiver.indexOf('@')+1);
	if(!connect(Addr))
		return false;
	if(!Helo())
		return false;
	if(sender.indexOf('@')==-1)
	{
		if(!MailFrom(sender+".bbs@"+ColaServer.INI.BBSAddress))
			return false;
		if(!RcptTo(receiver))
			return false;
		if(!Data(is,sender+".bbs@"+ColaServer.INI.BBSAddress,title))
			return false;
	}
	else
	{
		if(!MailFrom(sender))
			return false;
		if(!RcptTo(receiver))
			return false;
		if(!Data(is,sender,title))
			return false;
	}
	quit();
	return true;
}
}