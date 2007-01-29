package colabbs.System; 

import java.io.*;
import java.util.*;

public class ServicePlasmid_List
{
	public Vector list = new Vector();
	
	public void init()
	{
		ServicePlasmid_Item pi;
		Enumeration e = list.elements();
		while (e.hasMoreElements())
		{
			pi = (ServicePlasmid_Item)e.nextElement();
			pi.init();
		}
	}
	
	public boolean LoadFile(String FileName)
	{
		File INIFile = new File(FileName);
		if (!INIFile.exists())
			return false;
		
		RandomAccessFile PlasmidFile = null;
		
		try
		{
			PlasmidFile = new RandomAccessFile(FileName, "r");
			String tmp;
			while((tmp = PlasmidFile.readLine()) != null)
			{
				tmp = tmp.trim();
				if (tmp.length() != 0)
					if (tmp.charAt(0) != '#' && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
						list.addElement(new ServicePlasmid_Item(tmp));
			}
			PlasmidFile.close();
			PlasmidFile = null;
		}
		catch (Exception e)
		{
			try
			{
				if (PlasmidFile != null)
					PlasmidFile.close();
				PlasmidFile = null;
			}
			catch (Exception e1)
			{
				
			}
		}
		
		return true;
	}	
}
