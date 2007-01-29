package colabbs.record;

import java.util.*;

import colabbs.Consts;
import colabbs.UTILS.*;

public class NewsNodeType implements RecordType
{
	boolean timeset=false;
	
	byte b[]=null;
	
	byte week = 0;
	short port = -1;
	int hour = 0,day = 0,month = 0;
	long min = 0;
	String BBSName=null,Address=null,Command=null;

	public NewsNodeType() {
	}

	public NewsNodeType(String theBBSName, String theAddress, short theport, String theCommand, BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek)
	{
		BBSName=theBBSName;
		Address=theAddress;
		port=theport;
		Command=theCommand;

		timeset=true;
		
		long tmpl=1;
		int tmpi=1;
		byte tmpb=1;
		
		for(int i=0;i<60;i++)
		{
			if(themin.get(i))
				min|=tmpl;
			tmpl<<=1;
		}
		
		for(int i=0;i<24;i++)
		{
			if(thehour.get(i))
				hour|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=31;i++)
		{
			if(theday.get(i))
				day|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=12;i++)
		{
			if(themonth.get(i))
				month|=tmpi;
			tmpi<<=1;
		}
		
		for(int i=0;i<7;i++)
		{
			if(theweek.get(i))
				week|=tmpb;
			tmpb<<=1;
		}
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

	public String getAddress()
	{
		if(Address!=null)
			return Address;
		if(b!=null)
		{
			Address=new String(b,0,40,80); //40-120(Consts.StrLen)

			if(Address.indexOf(0)!=-1)
				Address=Address.substring(0,Address.indexOf(0));
			return Address;
		}
		return null;
	}

	public String getBBSName()
	{
		if(BBSName!=null)
			return BBSName;
		if(b!=null)
		{
			BBSName=new String(b,0,0,40); //0-40(Consts.NameLen)

			if(BBSName.indexOf(0)!=-1)
				BBSName=BBSName.substring(0,BBSName.indexOf(0));
			return BBSName;
		}
		return null;
	}

	public String getCommand()
	{
		if(Command!=null)
			return Command;
		if(b!=null)
		{
			Command=new String(b,0,122,6); //122-132(6)

			if(Command.indexOf(0)!=-1)
				Command=Command.substring(0,Command.indexOf(0));
			return Command;
		}
		return null;
	}

	public short getPort()
	{
		if(port!=-1)
			return port;
		if(b!=null)
			port=NUMBER.GetShort(b,120);
		return port;
	}

	public byte[] getRecordBytes()
	{
		if(b==null)
		{
			b=new byte[153];
			STRING.getBytes(BBSName, 0, 40, b, 0);
			STRING.getBytes(Address, 0, 80, b, 40);
			/*if(BBSName!=null)
			BBSName.getBytes(0,BBSName.length(),b,0);
			if(Address!=null)
			Address.getBytes(0,Address.length(),b,40);*/
			if(port!=-1)
				NUMBER.PutNum(b,120,port);
			STRING.getBytes(Command, 0, 10, b, 122);
			/*if(Command!=null)
			Command.getBytes(0,Command.length(),b,122);*/
			if(timeset)
			{
				NUMBER.PutNum(b,132,min);
				NUMBER.PutNum(b,140,hour);
				NUMBER.PutNum(b,144,day);
				NUMBER.PutNum(b,148,month);
				b[152]=week;
			}
		}
		return b;
	}

	public String getRecordString()
	{
		if(b!=null)
		{
			if(BBSName==null)
			{
				BBSName=new String(b,0,0,40); //0-40(Consts.NameLen)
				if(BBSName.indexOf(0)!=-1)
					BBSName=BBSName.substring(0,BBSName.indexOf(0));
			}
			if(Address==null)
			{
				Address=new String(b,0,40,80); //40-120(Consts.StrLen)
				if(Address.indexOf(0)!=-1)
					Address=Address.substring(0,Address.indexOf(0));
			}
			if(port==-1)
			{
				port=NUMBER.GetShort(b,120);
			}
			if(Command==null)
			{
				Command=new String(b,0,122,6); //122-132(6)
				if(Command.indexOf(0)!=-1)
					Command=Command.substring(0,Command.indexOf(0));
			}
			/*		if(!timeset)
			{
			min =  Utils.GetLong(b,132); //132-140(8)
			hour = Utils.GetInt(b,140); //140-144(4)
			day = Utils.GetInt(b,144); //144-148(4)
			month = Utils.GetInt(b,152); //148-152(4)
			week = b[152]; //152-153(1)
			timeset=true;
			}*/
		}
		return STRING.Cut(BBSName,12)+" "+STRING.Cut(Address,38)+" "+STRING.Cut(""+port,6)+" "+Command;
	}

	public int getSize()
	{
		return 153;
	}

	public void getTime(BitSet themin,BitSet thehour,BitSet theday,BitSet themonth,BitSet theweek)
	{
		if(timeset)
		{
			long tmpl=1;
			int tmpi=1;
			byte tmpb=1;
			
			for(int i=0;i<60;i++)
			{
				if((min&tmpl)!=0)
					themin.set(i);
				else
					themin.clear(i);
				tmpl<<=1;
			}
			
			for(int i=0;i<24;i++)
			{
				if((hour&tmpi)!=0)
					thehour.set(i);
				else
					thehour.clear(i);
				tmpi<<=1;
			}
			
			tmpi=1;
			for(int i=1;i<=31;i++)
			{
				if((day&tmpi)!=0)
					theday.set(i);
				else
					theday.clear(i);
				tmpi<<=1;
			}
			
			tmpi=1;
			for(int i=1;i<=12;i++)
			{
				if((month&tmpi)!=0)
					themonth.set(i);
				else
					themonth.clear(i);
				tmpi<<=1;
			}
			
			for(int i=0;i<7;i++)
			{
				if((week&tmpb)!=0)
					theweek.set(i);
				else
					theweek.clear(i);
				tmpb<<=1;
			}
			return;
		}
		if(b!=null)
		{
			min =  NUMBER.GetLong(b,132); //132-140(8)
			hour = NUMBER.GetInt(b,140); //140-144(4)
			day = NUMBER.GetInt(b,144); //144-148(4)
			month = NUMBER.GetInt(b,148); //148-152(4)
			week = b[152]; //152-153(1)
			timeset=true;
			
			long tmpl=1;
			int tmpi=1;
			byte tmpb=1;
			
			for(int i=0;i<60;i++)
			{
				if((min&tmpl)!=0)
					themin.set(i);
				else
					themin.clear(i);
				tmpl<<=1;
			}
			
			for(int i=0;i<24;i++)
			{
				if((hour&tmpi)!=0)
					thehour.set(i);
				else
					thehour.clear(i);
				tmpi<<=1;
			}
			
			tmpi=1;
			for(int i=1;i<=31;i++)
			{
				if((day&tmpi)!=0)
					theday.set(i);
				else
					theday.clear(i);
				tmpi<<=1;
			}
			
			tmpi=1;
			for(int i=1;i<=12;i++)
			{
				if((month&tmpi)!=0)
					themonth.set(i);
				else
					themonth.clear(i);
				tmpi<<=1;
			}
			
			for(int i=0;i<7;i++)
			{
				if((week&tmpb)!=0)
					theweek.set(i);
				else
					theweek.clear(i);
				tmpb<<=1;
			}
		}
	}

	public boolean isDeleted()
	{
		return false;
	}

	public boolean isRangeDeletible()
	{
		return true;
	}

	public void setAddress(String theAddress)
	{
		Address=theAddress;
		if(b!=null)
		{
			for(int i=40;i<120;i++) //40-120(Consts.StrLen)
				b[i]=0;
			Address.getBytes(0,Address.length(),b,40);
		}
	}

	public void setBBSName(String theBBSName)
	{
		BBSName=theBBSName;
		if(b!=null)
		{
			for(int i=0;i<40;i++) //0-40(Consts.NameLen)
				b[i]=0;
			BBSName.getBytes(0,BBSName.length(),b,0);
		}
	}

	public void setCommand(String theCommand)
	{
		Command=theCommand;
		if(b!=null)
		{
			for(int i=122;i<132;i++) //122-132(6)
				b[i]=0;
			Command.getBytes(0,Command.length(),b,122);
		}
	}

	public void setDeleted()
	{
	}

	public void setPort(short theport)
	{
		port=theport;
		if(b!=null)
			NUMBER.PutNum(b,120,port);
	}

	public void setRecord(byte[] data)
	{
		b=data;
		BBSName=null;
		Address=null;
		Command=null;
		timeset=false;
		port=-1;
	}

	public void setTime(long themin,int thehour,int theday,int themonth,byte theweek)
	{
		timeset=true;
		min=themin;
		hour=thehour;
		day=theday;
		month=themonth;
		week=theweek;
		if(b!=null)
		{
			NUMBER.PutNum(b,132,min);
			NUMBER.PutNum(b,140,hour);
			NUMBER.PutNum(b,144,day);
			NUMBER.PutNum(b,148,month);
			b[152]=week;
		}
	}

	public void setTime(BitSet themin,BitSet thehour,BitSet theday,BitSet themonth,BitSet theweek)
	{
		timeset=true;
		long tmpl=1;
		int tmpi=1;
		byte tmpb=1;
		
		for(int i=0;i<60;i++)
		{
			if(themin.get(i))
				min|=tmpl;
			tmpl<<=1;
		}
		
		for(int i=0;i<24;i++)
		{
			if(thehour.get(i))
				hour|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=31;i++)
		{
			if(theday.get(i))
				day|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=12;i++)
		{
			if(themonth.get(i))
				month|=tmpi;
			tmpi<<=1;
		}
		
		for(int i=0;i<7;i++)
		{
			if(theweek.get(i))
				week|=tmpb;
			tmpb<<=1;
		}
		if(b!=null)
		{
			NUMBER.PutNum(b,132,min);
			NUMBER.PutNum(b,140,hour);
			NUMBER.PutNum(b,144,day);
			NUMBER.PutNum(b,148,month);
			b[152]=week;
		}
	}
}