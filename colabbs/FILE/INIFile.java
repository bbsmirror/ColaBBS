package colabbs.FILE;

import java.io.*;

public class INIFile
{
	File INIFILE;
	RandomAccessFile INIFILE_RAF = null;
	
	public INIFile(String FileName)
	{
		INIFILE = new File(FileName);
	}
	
	public boolean exist()
	{
		return INIFILE.exists();
	}
	
	public int getLines()
	{
		if (!exist())
			return -1;
		if (!open())
			return -1;
		
		int c = 0;
		while (readLine() != null)
			c++;
		
		close();
		return c;
	}
	
	public boolean open()
	{
		if (!exist())
			return false;
		
		try
		{
			INIFILE_RAF = new RandomAccessFile(INIFILE, "r");
		}
		catch (Exception e)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean close()
	{
		try
		{
			if (INIFILE_RAF != null)
				INIFILE_RAF.close();
			INIFILE_RAF = null;
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	public String readLine()
	{
		if (INIFILE_RAF == null)
			return null;
		
		try
		{
			String tmp;
			while((tmp = INIFILE_RAF.readLine()) != null)
			{
				tmp = tmp.trim();
				if (tmp.length() != 0)
					if (tmp.charAt(0) != '#' && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
						return tmp;
			}
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
}
