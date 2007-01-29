package colabbs.record;

import colabbs.UTILS.*;

public class FriendType implements RecordType
{
	byte b[]=null;
	String ID=null,note=null;

	public FriendType() 
	{
	}

	public FriendType(byte[] data)
	{
		b=data;
	}

	public FriendType(String theID,String theNote)
	{
		ID=theID;
		note=theNote;
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

	public String getNote()
	{
		if(note!=null)
			return note;
		if(b!=null)
		{
			note=new String(b,0,13,15);
			if(note.indexOf(0)!=-1)
				note=note.substring(0,note.indexOf(0));
			return note;
		}
		return null;
	}

	public byte[] getRecordBytes()
	{
		if(b==null)
		{
			b=new byte[28];
			if(ID!=null)
				ID.getBytes(0,ID.length(),b,0);
			if(note!=null)
				note.getBytes(0,note.length(),b,13);
		}
		return b;
	}

	public String getRecordString()
	{
		if(b!=null)
		{
			if(ID==null)
			{
				ID=new String(b,0,0,13);
				if(ID.indexOf(0)!=-1)
					ID=ID.substring(0,ID.indexOf(0));
			}
			if(note==null)
			{
				note=new String(b,0,13,15);
				if(note.indexOf(0)!=-1)
					note=note.substring(0,note.indexOf(0));
			}
		}
		return STRING.Cut(ID,14)+STRING.Cut(note,15);
	}

	public int getSize() {
		return 28;
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
		note=null;
	}
}