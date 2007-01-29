package colabbs.record;

import colabbs.UTILS.*;

public class NewsFeedType implements RecordType
{
	byte b[]=null;

	byte mode = -1;
	int max=-1,min=-1;
	long lasttime=-1;
	String BoardName=null,BBSName=null,NewsGroup=null;

	public NewsFeedType() {
		super();
	}

	public NewsFeedType(String theboardname, String thebbsname, String thenewsgroup)
	{
		BoardName=theboardname;
		BBSName=thebbsname;
		NewsGroup=thenewsgroup;
		max=0;
		min=0;
		mode=3;
	}

	public NewsFeedType(String theboardname, String thebbsname, String thenewsgroup,byte themode)
	{
		BoardName=theboardname;
		BBSName=thebbsname;
		NewsGroup=thenewsgroup;
		max=0;
		min=0;
		mode=themode;
	}

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
		return null;
	}

	public String getBBSName()
	{
		if(BBSName!=null)
			return BBSName;
		if(b!=null)
		{
			BBSName=new String(b,0,80,40); //80-120(Consts.NameLen)

			if(BBSName.indexOf(0)!=-1)
				BBSName=BBSName.substring(0,BBSName.indexOf(0));
			return BBSName;
		}
		return null;
	}

	public String getBoardName()
	{
		if(BoardName!=null)
			return BoardName;
		if(b!=null)
		{
			BoardName=new String(b,0,0,80); //0-80(Consts.NameLen)

			if(BoardName.indexOf(0)!=-1)
				BoardName=BoardName.substring(0,BoardName.indexOf(0));
			return BoardName;
		}
		return null;
	}

	public long getLastTime()
	{
		if(lasttime!=-1)
			return lasttime;
		if(b!=null)
			lasttime=NUMBER.GetLong(b,208);
		return lasttime;
	}

	public int getMax()
	{
		if(max!=-1)
			return max;
		if(b!=null)
			max=NUMBER.GetInt(b,200);
		return max;
	}

	public int getMin()
	{
		if(min!=-1)
			return min;
		if(b!=null)
			min=NUMBER.GetInt(b,204);
		return min;
	}

	public byte getMode()
	{
		if(mode!=-1)
			return mode;
		if(b!=null)
			mode=b[216];
		return mode;
	}

	public String getNewsGroup()
	{
		if(NewsGroup!=null)
			return NewsGroup;
		if(b!=null)
		{
			NewsGroup=new String(b,0,120,80); //120-200(Consts.StrLen)

			if(NewsGroup.indexOf(0)!=-1)
				NewsGroup=NewsGroup.substring(0,NewsGroup.indexOf(0));
			return NewsGroup;
		}
		return null;
	}

	public byte[] getRecordBytes()
	{
		if(b==null)
		{
			b=new byte[217];
			STRING.getBytes(BoardName, 0, 80, b, 0);
			STRING.getBytes(BBSName, 0, 40, b, 80);
			STRING.getBytes(NewsGroup, 0, 80, b, 120);
			/*if(BoardName!=null)
			BoardName.getBytes(0,BoardName.length(),b,0);
			if(BBSName!=null)
			BBSName.getBytes(0,BBSName.length(),b,80);
			if(NewsGroup!=null)
			NewsGroup.getBytes(0,NewsGroup.length(),b,120);*/
			if(max!=-1)
				NUMBER.PutNum(b,200,max);
			if(min!=-1)
				NUMBER.PutNum(b,204,min);
			if(lasttime!=-1)
				NUMBER.PutNum(b,208,lasttime);
			if(mode!=-1)
				b[216]=mode;
		}
		return b;
	}

	public String getRecordString()
	{
		if(b!=null)
		{
			if(BoardName==null)
			{
				BoardName=new String(b,0,0,80); //0-80(Consts.NameLen)
				if(BoardName.indexOf(0)!=-1)
					BoardName=BoardName.substring(0,BoardName.indexOf(0));
			}
			if(BBSName==null)
			{
				BBSName=new String(b,0,80,40); //80-120(Consts.StrLen)
				if(BBSName.indexOf(0)!=-1)
					BBSName=BBSName.substring(0,BBSName.indexOf(0));
			}
			if(NewsGroup==null)
			{
				NewsGroup=new String(b,0,120,80); //120-200
				if(NewsGroup.indexOf(0)!=-1)
					NewsGroup=NewsGroup.substring(0,NewsGroup.indexOf(0));
			}
			if(max==-1)
				max=NUMBER.GetInt(b,200);
			if(min==-1)
				min=NUMBER.GetInt(b,204);
			/*		if(lastindex==-1)
			lastindex=Utils.GetInt(b,208);
			if(lasttime==-1)
			lasttime=Utils.GetLong(b,212);*/
		}
		return STRING.Cut(BoardName,16)+" "+STRING.Cut(BBSName,10)+" "+STRING.Cut(""+max,10)+" "+STRING.Cut(""+min,10)+" "+STRING.Cut(NewsGroup,23);
	}

	public int getSize()
	{
		return 217;
	}

	public boolean isDeleted() 
	{
		return false;
	}

	public boolean isRangeDeletible()
	{
		return true;
	}

	public void setBBSName(String theBBSName)
	{
		BBSName=theBBSName;
		if(b!=null)
		{
			for(int i=80;i<120;i++) //80-120(Consts.NameLen)
				b[i]=0;
			BBSName.getBytes(0,BBSName.length(),b,80);
		}
	}

	public void setBoardName(String theBoardName)
	{
		BoardName=theBoardName;
		if(b!=null)
		{
			for(int i=0;i<80;i++) //0-80(Consts.NameLen)
				b[i]=0;
			BoardName.getBytes(0,BoardName.length(),b,0);
		}
	}

	public void setDeleted() 
	{
	}

	public void setLastTime(long thelasttime)
	{
		lasttime=thelasttime;
		if(b!=null)
			NUMBER.PutNum(b,208,lasttime);
	}

	public void setMax(int themax)
	{
		max=themax;
		if(b!=null)
			NUMBER.PutNum(b,200,max);
	}

	public void setMin(int themin)
	{
		min=themin;
		if(b!=null)
			NUMBER.PutNum(b,204,min);
	}

	public void setMode(byte themode)
	{
		mode=themode;
		if(b!=null)
			b[216]=mode;
	}

	public void setNewsGroup(String theNewsGroup)
	{
		NewsGroup=theNewsGroup;
		if(b!=null)
		{
			for(int i=120;i<200;i++) //120-200(Consts.StrLen)
				b[i]=0;
			NewsGroup.getBytes(0,NewsGroup.length(),b,120);
		}
	}

	public void setRecord(byte[] data)
	{
		b = data;
		BoardName = null;
		BBSName = null;
		NewsGroup = null;
		max = -1;
		min = -1;
		lasttime = -1;
		mode = -1;
	}
}