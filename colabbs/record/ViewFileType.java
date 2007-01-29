package colabbs.record;

import colabbs.bbstp.BBSTPItem;
import colabbs.bbstp.BBSTPViewFileItem;

import colabbs.Consts;

public abstract class ViewFileType implements RecordType,BBSTPItemType
{
	protected boolean Link=false;
	protected byte b[]=null;
	protected String title=null,filename=null;
	public String LastTitle=null;

	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public String deleteBody()
	{
		if(filename!=null)
			return filename;
		if(b!=null)
		{
			filename=new String(b,0,0,Consts.StrLen-2);
			if(filename.indexOf(0)!=-1)
				filename=filename.substring(0,filename.indexOf(0));
			//			filename=filename.substring(0,filename.indexOf(0));
			//			filename=filename.substring(2,filename.lastIndexOf('.'));
			return filename;
		}
		return null;
	}

	public String getTitle()
	{
		if(title!=null)
			return title;
		if(b!=null)
		{
			title=new String(b,0,2*Consts.StrLen,Consts.StrLen);

			if(title.indexOf(0)!=-1)
				title=title.substring(0,title.indexOf(0));
			return title;
		}
		return null;
	}

	public void invertLink()
	{
		if(b!=null)
		{
			b[244]^=Consts.FileLink;
		}
		else
		{
			Link^=true;
		}
	}

	public boolean isLink()
	{
		/*	if((b[244]&Consts.FileRead)!=0)
		Read=true;
		if((b[244]&Consts.FileMarked)!=0)
		Mark=true;*/
		if((b[244]&Consts.FileLink)!=0)
			Link=true;
		else
			Link=false;
		return Link;
	}
	//Add by WilliamWey
	public boolean isRangeDeletible()
	{
		return true;
	}

	public void setLink(boolean l)
	{
		if(b!=null)
		{
			if(true)
				b[244]|=Consts.FileLink;
			else
				b[244]&=~Consts.FileLink;
		}
		else
		{
			Link=l;
		}
	}

	public void setTitle(String theTitle)
	{
		title=theTitle;
		if(b!=null)
		{
			for(int i=0;i<Consts.StrLen;i++)
				b[2*Consts.StrLen+i]=0;
			title.getBytes(0,title.length(),b,2*Consts.StrLen);
		}
	}

	public abstract String getPoster();

  public void setRecord(BBSTPItem bbstpi)
  {
    b=null;
    Link=((BBSTPViewFileItem)bbstpi).Link;
    title=((BBSTPViewFileItem)bbstpi).title;
//    filetime=((BBSTPViewFileItem)bbstpi).filetime;
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
}
