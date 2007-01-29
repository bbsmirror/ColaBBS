package colabbs.WW;

import java.util.*;
import java.io.*;

public class CharsetTable
{
	public final static Hashtable Table = new Hashtable();
	
	public void add(String CharsetKey, String CharsetMapto)
	{
		Table.put(CharsetKey.toLowerCase().trim(), CharsetMapto.trim());
	}
	public boolean exist(String CharsetKey)
	{
		return Table.containsKey(CharsetKey.toLowerCase());
	}
	public boolean LoadFile(String FileName)
	{
		File INIFile = new File(FileName);
		if (!INIFile.exists())
			return false;
		
		RandomAccessFile FilterFile = null;
		try
		{
			FilterFile = new RandomAccessFile(FileName, "r");
			String tmp;
			int seeks;
			String Key, Mapto;
			while((tmp = FilterFile.readLine()) != null)
			{
				tmp.trim();
				if (tmp.length() != 0)
					if (tmp.substring(0, 1) != "#")
						if ((seeks = tmp.indexOf(' ')) != -1)
						{
							Key = tmp.substring(0, seeks);
							Mapto = tmp.substring(seeks);
							add(Key, Mapto);
						}
			}
			FilterFile.close();
			FilterFile = null;
		}
		catch (Exception e)
		{
			try
			{
				if (FilterFile != null)
					FilterFile.close();
				FilterFile = null;
			}
			catch (Exception e1)
			{
				
			}
		}		
		
		return true;
	}
	public String query(String CharsetKey) throws Exception 
	{
		return (String)Table.get(CharsetKey.toLowerCase());
	}
}