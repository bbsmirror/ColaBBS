package colabbs.record;

import colabbs.bbstp.BBSTPItem;
import colabbs.bbstp.mail.BBSTPMailItem;

import colabbs.Consts;
import colabbs.UTILS.*;
import colabbs.Prompt;

public class MailType extends ViewFileType
{
	public boolean Read=false,Mark=false;
	String sender=null;

	public MailType() 
	{
		super();
	}

	public MailType(byte thedata[])
	{
		b=thedata;
	}

	public MailType(String thefilename, String thesender, String thetitle)
	{
		filename=thefilename;
		sender=thesender;
		title=thetitle;
	}

	public MailType(String thefilename, String thesender, String thetitle, boolean r, boolean m, boolean l)
	{
		filename=thefilename;
		sender=thesender;
		title=thetitle;
		Read=r;
		Mark=m;
		Link=l;
	}

	public byte[] getRecordBytes()
	{
		if(b==null)
		{
			b=new byte[256];
			STRING.getBytes(filename, 0, Consts.StrLen, b, 0);
			STRING.getBytes(sender, 0, Consts.StrLen, b, Consts.StrLen);
			STRING.getBytes(title, 0, Consts.StrLen, b, Consts.StrLen * 2);
			
			/*if(filename!=null)
			filename.getBytes(0,filename.length(),b,0);
			if(sender!=null)
			sender.getBytes(0,sender.length(),b,Consts.StrLen);
			if(title!=null)
			title.getBytes(0,title.length(),b,2*Consts.StrLen);*/
			
			if(Read)
				b[244]|=Consts.FileRead;
			if(Mark)
				b[244]|=Consts.FileMarked;
			if(Link)
				b[244]|=Consts.FileLink;
		}

		/*	if(title!=null&&title.length()>3&&title.substring(0,4).equals("Re: "))
		CmpTitle=title.substring(4);
		else
		{
		CmpTitle=new String(title);
		title=Prompt.Msgs[162]+title;
		}*/

		return b;
	}

	public String getRecordString()
	{
		char ReadFlag;
		
		if(b!=null)
		{
			//		try
			{
				if(sender==null)
				{
					sender=new String(b,0,Consts.StrLen,Consts.StrLen);
					if(sender.indexOf(' ')>=0)
						sender=sender.substring(0,sender.indexOf(' '));
					if(sender.indexOf(0)>=0)
						sender=sender.substring(0,sender.indexOf(0));
				}
				if(title==null)
				{
					title=new String(b,0,2*Consts.StrLen,Consts.StrLen);
					if(title.indexOf(0)!=-1)
						title=title.substring(0,title.indexOf(0));
				}
			}
			/*		catch(java.io.UnsupportedEncodingException e)
			{
			e.printStackTrace();
			}*/
			if((b[244]&Consts.FileRead)!=0)
				Read=true;
			else
				Read=false;
			if((b[244]&Consts.FileMarked)!=0)
				Mark=true;
			else
				Mark=false;
			if((b[244]&Consts.FileLink)!=0)
				Link=true;
			else
				Link=false;
		}
		String CmpTitle;

		if(Read)
		{
			if(Mark)
				ReadFlag='m';
			else
				ReadFlag=' ';
			if(Link)
				ReadFlag='c';
		}
		else
		{
			if(Mark)
				ReadFlag='M';
			else
				ReadFlag='N';
			if(Link)
				ReadFlag='C';
		}

		if(title!=null&&title.length()>3&&title.substring(0,4).equals("Re: "))
			CmpTitle=title.substring(4);
		else
		{
			CmpTitle=new String(title);
			title=Prompt.Msgs[162]+title;
		}

		if(LastTitle!=null&&CmpTitle.equals(LastTitle))
			return ReadFlag+" "+STRING.Cut(sender,20)+"[1;32m"+STRING.Cut(title,52);
		else
			return ReadFlag+" "+STRING.Cut(sender,20)+STRING.Cut(title,52);
	}


	public String getPoster()
	{
		return getSender();
	}

	public String getSender()
	{
		if(sender!=null)
			return sender;
		if(b!=null)
		{
			sender=new String(b,0,Consts.StrLen,Consts.StrLen);

			if(sender.indexOf(0)!=-1)
				sender=sender.substring(0,sender.indexOf(0));
			return sender;
		}
		return null;
	}

	public int getSize()
	{
		return 256;
	}

	public void invertMark()
	{
		if(b!=null)
		{
			b[244]^=Consts.FileMarked;
		}
		else
		{
			Mark^=true;
		}
	}

	public boolean isDeleted() 
	{
		return false;
	}

	public boolean isMark()
	{
		if(b!=null&&(b[244]&Consts.FileMarked)!=0)
			Mark=true;
		else
			Mark=false;
		return Mark;
	}

	public boolean isRangeDeletible()
	{
		return !isMark();
	}

	public boolean isRead()
	{
		if(b==null)
			return true;
		if((b[244]&Consts.FileRead)!=0)
			Read=true;
		else
			Read=false;
		return Read;
	}

	public static String myMailHome(String bbshome, String myID)
	{
		return bbshome+"mail"+java.io.File.separator+Character.toUpperCase(myID.charAt(0))+java.io.File.separator+myID+java.io.File.separator;
	}

	public void setDeleted() 
	{
	}

	public void setMark(boolean m)
	{
		if(b!=null)
		{
			if(true)
				b[244]|=Consts.FileMarked;
			else
				b[244]&=~Consts.FileMarked;
		}
		else
		{
			Mark=m;
		}
	}

	public void setRead(boolean r)
	{
		if(b!=null)
		{
			if(true)
				b[244]|=Consts.FileRead;
			else
				b[244]&=~Consts.FileRead;
		}
		else
		{
			Read=r;
		}
	}

	public void setRecord(byte[] data)
	{
		b=data;
		/*	if((b[244]&Consts.FileRead)!=0)
		Read=true;
		if((b[244]&Consts.FileMarked)!=0)
		Mark=true;
		if((b[244]&Consts.FileLink)!=0)
		Link=true;*/
		filename=null;
		sender=null;
		title=null;
	}
  
  public BBSTPItem getBBSTPItem()
  {
    return new BBSTPMailItem(getSender(),getTitle(),getFileTime(),isLink(),isMark(),isRead());
  }
}