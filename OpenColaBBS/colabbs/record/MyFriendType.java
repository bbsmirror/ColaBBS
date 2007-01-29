package colabbs.record;

public class MyFriendType implements RecordType
{
	byte b[]=null;
	String ID=null;

	public MyFriendType() 
	{
	}
	
	public MyFriendType(byte data[])
	{
		b=data;
	}
	
	public MyFriendType(String theID)
	{
		ID=theID;
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
		if(ID!=null)
			return ID;
		if(b!=null)
		{
			ID=new String(b,0,0,13);

			if(ID.indexOf(0)!=-1)
				ID=ID.substring(0,ID.indexOf(0));
			return ID;
		}
		return null;
	}

	public boolean equals(Object t)
	{
		return deleteBody().equals(((RecordType)t).deleteBody());
	}

	public byte[] getRecordBytes()
	{
		if(b==null&&ID!=null)
		{
			b=new byte[13];
			if(ID!=null)
				ID.getBytes(0,ID.length(),b,0);
		}
		return b;
	}

	public String getRecordString()
	{
		if(ID!=null)
			return ID;
		if(b!=null)
		{
			ID=new String(b,0,0,13);

			if(ID.indexOf(0)!=-1)
				ID=ID.substring(0,ID.indexOf(0));
			return ID;
		}
		return null;
	}

	public int getSize() 
	{
		return 13;
	}

	public boolean isDeleted()
	{
		return false;
	}
	
	public boolean isRangeDeletible()
	{
		return true;
	}

	public void setDeleted()
	{
	}

	public void setRecord(byte[] data)
	{
		b=data;
		ID=null;
	}
}