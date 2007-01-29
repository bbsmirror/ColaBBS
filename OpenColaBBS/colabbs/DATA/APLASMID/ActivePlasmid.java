package colabbs.DATA.APLASMID;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.FILE.*;

public class ActivePlasmid
{
	private ActivePlasmidItem Plasmid[];
	private int Plasmids = 0;	
	private static Hashtable Table = new Hashtable();
	
	public void add(String PlasmidName, ActivePlasmidItem ActivePlasmid)
	{
		Table.put(PlasmidName.toLowerCase().trim(), ActivePlasmid);
	}
	public boolean exist(String PlasmidName)
	{
		return Table.containsKey(PlasmidName.toLowerCase());
	}
	
	public boolean LoadFile(String FileName)
	{
		INIFile F = new INIFile(FileName);
		if (!F.exist())
			return false;
		
		Plasmids = F.getLines();
		if (Plasmids == -1)
			return false;
		Plasmid = new ActivePlasmidItem[Plasmids];
		
		if (!F.open())
			return false;
		
		String tmp;
		int i = 0;
		while ((tmp = F.readLine()) != null)
		{
			try
			{
				Plasmid[i] = new ActivePlasmidItem(tmp);
			}
			catch (Exception e)
			{
				Plasmids--;
				break;
			}
			add(tmp, Plasmid[i]);
			i++;
		}
		F.close();
		
		return true;
	}
	
	/*public boolean LoadFile(String FileName) throws Exception
	{
		File INIFile = new File(FileName);
		if (!INIFile.exists())
			return false;
		
		RandomAccessFile PlasmidFile = new RandomAccessFile(FileName, "r");
		String tmp;
		while((tmp = PlasmidFile.readLine()) != null)
		{
			tmp = tmp.trim();
			if (tmp.length() != 0)
				if (tmp.charAt(0) != '#' && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
					Plasmids++;
		}
		Plasmid = new ActivePlasmidItem[Plasmids];
		PlasmidFile.seek(0);
		for(int i = 0;i < Plasmids; i++)
		{
			tmp = PlasmidFile.readLine().trim();
			if (tmp.length() != 0)
				if (tmp.charAt(0) != '#' && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
				{
					Plasmid[i] = new ActivePlasmidItem(tmp);
					add(tmp, Plasmid[i]);
				}
		}
		PlasmidFile.close();
		
		//int R;
		//for (int i = 0; i < Plasmids; i++)
		//	R = Plasmid[i].init();
		
		return true;
	}*/

	public void Run(String PlasmidName, BBSUser who) throws Exception 
	{
			((ActivePlasmidItem)Table.get(PlasmidName.toLowerCase())).Go(who);
	}

	public void Run(String PlasmidName, BBSUser who, String Param) throws Exception 
	{
			((ActivePlasmidItem)Table.get(PlasmidName.toLowerCase())).Go(who, Param);
	}
}