package colabbs.record;

import java.util.*;

import colabbs.bbstp.BBSTPItem;

import colabbs.*;
import colabbs.UTILS.*;

public class AnnounceType extends ViewFileType
{
	String poster=null;

	public AnnounceType() 
	{
		super();
	}
	
	public AnnounceType(byte thedata[])
	{
		b=thedata;
	}
	
	public AnnounceType(String thefilename, String theposter, String thetitle)
	{
		filename=thefilename;
		poster=theposter;
		title=thetitle;
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
			/*if(filename!=null)
				filename.getBytes(0,filename.length(),b,0);
			if(poster!=null)
				poster.getBytes(0,poster.length(),b,Consts.StrLen);
			if(title!=null)
				title.getBytes(0,title.length(),b,2*Consts.StrLen);*/
			
			if(Link)
				b[244]|=Consts.FileLink;
		}
		return b;
	}
	
	public String getRecordString()
	{
		char ReadFlag;

		if(b!=null)
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


		String FNBuf=new String(filename);
		if(FNBuf.lastIndexOf('.')!=-1)
			FNBuf=FNBuf.substring(2,FNBuf.lastIndexOf('.'));
		
		if(filename.charAt(0)=='D')
			return " "+Prompt.Msgs[368]+" [m"+STRING.Cut(title,44)+STRING.Cut(poster,14)+"["+ColaServer.SysDATE.DateFormatter3.format(new Date(Long.parseLong(FNBuf)*1000L))+"]";
		else
			return " "+Prompt.Msgs[369]+" [m"+STRING.Cut(title,44)+STRING.Cut(poster,14)+"["+ColaServer.SysDATE.DateFormatter3.format(new Date(Long.parseLong(FNBuf)*1000L))+"]";
	}
	
	public int getSize()
	{
		return 256;
	}
	
	public boolean isDeleted() {
		return false;
	}
	
	public boolean isRangeDeletible()
	{
		return true;
	}
	
	public static String myBoardHome(String bbshome, String name)
	{
		return bbshome+"boards"+java.io.File.separator+name+java.io.File.separator;
	}
	
	public void setDeleted()
	{
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

  public void setRecord(BBSTPItem bbstpi)
  {
    super.setRecord(bbstpi);
  }

  public BBSTPItem getBBSTPItem()
  {
    return null;
  }
}