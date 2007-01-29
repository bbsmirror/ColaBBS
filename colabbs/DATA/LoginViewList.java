package colabbs.DATA;

import java.io.*;
import java.util.*;

import colabbs.FILE.*;

public class LoginViewList
{
	public Vector list = new Vector();
	
	public LoginViewList()
	{
		
	}
	
	public boolean LoadFile(String FileName)
	{
		INIFile F = new INIFile(FileName);
		if (!F.exist())
			return false;
		
		if (!F.open())
			return false;
		
		String tmp;
		while ((tmp = F.readLine()) != null)
		{
			list.addElement(tmp);
		}
		F.close();
		
		return true;
	}
	
	/*public boolean LoadFile(String FileName) throws Exception
	{	
		File INIFile = new File(FileName);
		if (!INIFile.exists())
			return false;
		
		list.removeAllElements();
		RandomAccessFile raf = new RandomAccessFile(FileName, "r");
		String tmp;
		while((tmp = raf.readLine()) != null)
		{
			tmp = tmp.trim();
			if (tmp.length() != 0)
				if (tmp.substring(0, 1) != "#" && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
					list.addElement(tmp);
		}
		raf.close();
		
		return true;
	}*/
}
