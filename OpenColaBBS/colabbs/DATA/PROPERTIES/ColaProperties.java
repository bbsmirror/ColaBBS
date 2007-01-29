package colabbs.DATA.PROPERTIES;

import java.io.*;
import java.util.*;

import colabbs.FILE.*;

public class ColaProperties
{	
	protected Properties pro = null;
	protected String desciption = null;
	
	private LogFile logfile = null;
	
	public ColaProperties()
	{
		pro = new Properties();
		desciption = new String();
	}
	
	public ColaProperties(ColaProperties default_pro)
	{
		pro = new Properties(default_pro.pro);
		desciption = default_pro.desciption;
	}	
	
	public ColaProperties(String xdesciption)
	{
		desciption = xdesciption;
	}

	public ColaProperties(LogFile xlogfile)
	{
		logfile = xlogfile;
	}

	public ColaProperties(String xdesciption, LogFile xlogfile)
	{
		desciption = xdesciption;
		logfile = xlogfile;
	}

	public ColaProperties(ColaProperties default_pro, LogFile xlogfile)
	{
		pro = new Properties(default_pro.pro);
		desciption = default_pro.desciption;
		logfile = xlogfile;
	}	
		
	public void set(String key, String value)
	{
		pro.put(key.toLowerCase(), value);
	}
	
	public void remove(String key)
	{
		pro.remove(key.toLowerCase());
	}
	
	public boolean exist(String key)
	{
		return pro.containsKey(key.toLowerCase());
	}
	
	public String get(String key)
	{
		return get(key, "");
	}
	
	public String get(String key, String default_value)
	{
		if (pro.containsKey(key.toLowerCase()))
			return (String)pro.get(key.toLowerCase());
		else
			return default_value;
	}
	
	public int getSize()
	{
		return pro.size();
	}
	
	public void clear()
	{
		pro.clear();
	}
	
	public void LoadFile(String FileName)
	{
		try
		{
			pro.load(new FileInputStream(FileName));
		}
		catch (Exception e)
		{
			if (logfile != null)
				logfile.WriteException(" ColaProperties 讀取 " + FileName + " 發生錯誤", e);
			else
				System.err.println(" ColaProperties 讀取 " + FileName + " 發生錯誤: " + e.getMessage());
		}
	}
	
	public void SaveFile(String FileName)
	{
		try
		{
			pro.save(new FileOutputStream(FileName, false), desciption);
		}
		catch (Exception e)
		{
			if (logfile != null)
				logfile.WriteException(" ColaProperties 回存 " + FileName + " 發生錯誤", e);
			else
				System.err.println(" ColaProperties 回存 " + FileName + " 發生錯誤: " + e.getMessage());
		}
	}
	
	public Vector getList()
	{
		Vector tmp = new Vector(pro.size());
		Enumeration e = pro.propertyNames();
		String key;
		while (e.hasMoreElements())
		{
			key = (String)e.nextElement();
			tmp.addElement(new String[] {key, get(key)});
		}
		return tmp;
	}
}
