package colabbs.record;

import colabbs.bbstp.BBSTPItem;
import colabbs.bbstp.board.BBSTPPostItem;

import colabbs.Consts;
import colabbs.UTILS.*;
import colabbs.Prompt;
import colabbs.ColaServer;

import java.util.*;
import java.io.*;

public class PostType extends ViewFileType
{
	public boolean Mark=false,Digest=false;
	public byte deliverTag=(byte)'L';
	String poster=null;

	public PostType() {
		super();
	}

	public PostType(byte thedata[])
	{
		b=thedata;
	}

	public PostType(String thefilename, String theposter, String thetitle)
	{
		filename=thefilename;
		poster=theposter;
		title=thetitle;
	}

	public PostType(String thefilename, String theposter, String thetitle, byte tag)
	{
		filename=thefilename;
		poster=theposter;
		title=thetitle;
		deliverTag=tag;
	}

	public boolean equals(Object t)
	{
		boolean case1=true,case2=true,case3=true;
		if(((RecordType)t).deleteBody()!=null&&!deleteBody().equals(((RecordType)t).deleteBody()))
			case1=false;
		if(((PostType)t).getPoster()!=null&&!getPoster().equals(((PostType)t).getPoster()))
			case2=false;
		if(((ViewFileType)t).getTitle()!=null&&!getTitle().equals(((ViewFileType)t).getTitle()))
			case3=false;
		if(case1&&case2&&case3)
			return true;
		return false;
	}

	public long getFileTime()
	{
		long temptime=-1;
		
		if(filename==null&&b!=null)
		{
			filename=new String(b,0,0,Consts.StrLen-2);
			if(filename.indexOf(0)!=-1)
				filename=filename.substring(0,filename.indexOf(0));
		}
		if(filename.indexOf('.')!=-1)
		{
			String tmp=filename.substring(filename.indexOf('.')+1,filename.lastIndexOf('.'));
			temptime=Long.parseLong(tmp);
		}
		return temptime*1000L+(filename.charAt(filename.length()-1)-'A');
	}

	public String getPoster()
	{
		if(poster!=null)
			return poster;
		if(b!=null)
		{
			poster=new String(b,0,Consts.StrLen,Consts.StrLen);

			if(poster.indexOf(0)!=-1)
				poster=poster.substring(0,poster.indexOf(0));
			return poster;
		}
		return null;
	}

	public byte[] getRecordBytes()
	{
		if(b==null)
		{
			b=new byte[256];
			STRING.getBytes(filename, 0, Consts.StrLen, b, 0);
			STRING.getBytes(poster, 0, Consts.StrLen, b, Consts.StrLen);
			STRING.getBytes(title, 0, Consts.StrLen, b, Consts.StrLen * 2);
			/*int n;
			if(filename!=null)
			{
			n = filename.length() % Consts.StrLen;
			if (n == 0)
			n = Consts.StrLen;
			filename.getBytes(0, n, b,0);
			}
			if(poster!=null)
			{
			n = poster.length() % Consts.StrLen;
			if (n == 0)
			n = Consts.StrLen;
			poster.getBytes(0,n,b,Consts.StrLen);
			}
			if(title!=null)
			{
			n = title.length() % Consts.StrLen;
			if (n == 0)
			n = Consts.StrLen;
			title.getBytes(0,n,b,2*Consts.StrLen);
			}*/
			
			if(Digest)
				b[244]|=Consts.FileDigest;
			if(Mark)
				b[244]|=Consts.FileMarked;
			if(Link)
				b[244]|=Consts.FileLink;
			b[Consts.StrLen-1]=deliverTag;
			b[Consts.StrLen-2]=deliverTag;
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
				if(filename==null)
				{
					filename=new String(b,0,0,Consts.StrLen-2);
					if(filename.indexOf(0)>=0)
						filename=filename.substring(0,filename.indexOf(0));
				}
				if(poster==null)
				{
					poster=new String(b,0,Consts.StrLen,Consts.StrLen);
					//				if(poster.indexOf(' ')>=0)
					//					poster=poster.substring(0,poster.indexOf(' '));
					if(poster.indexOf(0)>=0)
						poster=poster.substring(0,poster.indexOf(0));
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
			/*		if((b[244]&Consts.FileDigest)!=0)
			Digest=true;
			if((b[244]&Consts.FileMarked)!=0)
			Mark=true;
			if((b[244]&Consts.FileLink)!=0)
			Link=true;*/
		}

		/*	if(Read)
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
		}*/

		String CmpTitle;
		
		if(title!=null&&title.length()>3&&title.substring(0,4).equals("Re: "))
			CmpTitle=title.substring(4);
		else
		{
			CmpTitle=new String(title);
			title=Prompt.Msgs[162]+title;
		}

		/*	if(LastTitle!=null&&CmpTitle.equals(LastTitle))
		return ReadFlag+" "+STRING.Cut(poster,20)+"[1;32m"+STRING.Cut(title,52);
		else
		return ReadFlag+" "+STRING.Cut(poster,20)+STRING.Cut(title,52);*/
		
		String FNBuf=new String(filename);
		if(FNBuf.lastIndexOf('.')!=-1)
			FNBuf=FNBuf.substring(2,FNBuf.lastIndexOf('.'));

		/*String postbuf=null;
		if(poster.indexOf('@')==-1)
		postbuf=poster;
		else
		postbuf=poster.substring(0,poster.indexOf('@')+1);*/
		String postbuf;
		int p1 = poster.indexOf('@');
		int p2 = poster.indexOf('.');
		if (p1 == -1 && p2 == -1)
			postbuf = poster;
		else if (p1 == -1 || p2 < p1)
			postbuf = poster.substring(0, p2) + ".";
		else if (p2 == -1 || p1 < p2)
			postbuf = poster.substring(0, p1) + ".";
		else
			postbuf = poster;
		
		if(LastTitle!=null&&CmpTitle.equals(LastTitle))
			return STRING.Cut(postbuf,12)+" "+ColaServer.SysDATE.DateFormatter3.format(new Date(Long.parseLong(FNBuf)*1000L))+"  [1;32m"+STRING.Cut(title,49);
		else
			return STRING.Cut(postbuf,12)+" "+ColaServer.SysDATE.DateFormatter3.format(new Date(Long.parseLong(FNBuf)*1000L))+"  "+STRING.Cut(title,49);
		
		/*		if(New(FNBuf))
		ReadFlag='N';
		else
		ReadFlag=' ';
		if((EntryBuf[244]&Consts.FileDigest)!=0)
		{
		if(ReadFlag=='N')
		ReadFlag='G';
		else
		ReadFlag='g';
		}
		if((EntryBuf[244]&Consts.FileMarked)!=0)
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
		}*/
	}

	public int getSize()
	{
		return 256;
	}

	public byte getTag()
	{
		if(b!=null)
			deliverTag=b[Consts.StrLen-1];
		return deliverTag;
	}

	public void invertDigest()
	{
		if(b!=null)
		{
			b[244]^=Consts.FileDigest;
		}
		else
		{
			Digest^=true;
		}
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

	public boolean isDeleted() {
		return false;
	}

	public boolean isDigest()
	{
		if((b[244]&Consts.FileDigest)!=0)
			Digest=true;
		else
			Digest=false;
		return Digest;
	}

	public boolean isMark()
	{
		if((b[244]&Consts.FileMarked)!=0)
			Mark=true;
		else
			Mark=false;
		return Mark;
	}

	public boolean isRangeDeletible()
	{
		return !(isMark() || isDigest());
	}

	public static String myBoardHome(String bbshome, String name)
	{
		return bbshome+"boards"+java.io.File.separator+name+java.io.File.separator;
	}

	public void setDeleted() 
	{
	}

	public void setDigest(boolean d)
	{
		if(b!=null)
		{
			if(true)
				b[244]|=Consts.FileDigest;
			else
				b[244]&=~Consts.FileDigest;
		}
		else
		{
			Digest=d;
		}
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
		poster=null;
		title=null;
	}

  public BBSTPItem getBBSTPItem()
  {
    return new BBSTPPostItem(getPoster(),getTitle(),getFileTime(),deliverTag,isLink(),isMark(),isDigest());
  }
  
  public void setRecord(BBSTPItem bbstpi)
  {
    super.setRecord(bbstpi);

    Mark=((BBSTPPostItem)bbstpi).Mark;
    Digest=((BBSTPPostItem)bbstpi).Digest;
//    Link=pt.isLink();
    deliverTag=((BBSTPPostItem)bbstpi).deliverTag;
    poster=((BBSTPPostItem)bbstpi).poster;
//    title=pt.getTitle();
//    filetime=pt.getFileTime();
  }
}