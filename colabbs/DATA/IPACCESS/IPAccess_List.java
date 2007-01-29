package colabbs.DATA.IPACCESS;

import java.io.*;
import java.net.*;
import java.util.*;

import colabbs.FILE.*;
import colabbs.UTILS.*;

public class IPAccess_List
{
	private Vector list = new Vector();
	
	private int DefaultAccess = -1;
	
	private String[] AccessList = new String[] {"+", "-"};
	
	public IPAccess_List(int default_access)
	{
		DefaultAccess = default_access;
	}
	
	public IPAccess_List(int default_access, String[] access_list)
	{
		DefaultAccess = default_access;
		AccessList = access_list;
	}
	
	private int find_access(String what)
	{
		for (int i = 0; i < AccessList.length; i++)
			if (AccessList[i].equals(what))
				return i;
		return -1;
	}
	
	public boolean LoadFile(String FileName)
	{
		clear();
		INIFile F = new INIFile(FileName);
		if (!F.exist())
			return false;
		
		if (!F.open())
			return false;
		
		String tmp;
		String st[];
		int Access;
		String HostName, IP, NetMask;
		while ((tmp = F.readLine()) != null)
		{
			st = STRING.SToken(tmp);
			if (st.length > 2)
			{
				Access = find_access(st[0]);
				if (Access == -1)
					break;
				if (st[1].equalsIgnoreCase("ip"))
				{
					IP = st[2];
					if (st.length > 3)
						NetMask = st[3];
					else
						NetMask = "255.255.255.255";
					AddItem(new IPAccess_Item(Access, IP, NetMask));
				}
				else if (st[1].equalsIgnoreCase("name"))
				{
					HostName = st[2];
					AddItem(new IPAccess_Item(Access, HostName));
				}						
			}			
		}
		F.close();

		return true;
	}
	
/*	public boolean LoadFile(String FileName) throws Exception
	{
		File F = new File(FileName);
		if (!F.exists())
			return false;
		
		RandomAccessFile raf = new RandomAccessFile(FileName, "r");
		String tmp;
		String st[];
		int Access;
		String HostName, IP, NetMask;
		while((tmp = raf.readLine()) != null)
		{
			tmp = tmp.trim();
			if (tmp.length() != 0)
			{
				if (tmp.charAt(0) != '#' && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
				{
					st = STRING.SToken(tmp);
					if (st.length > 2)
					{
						Access = find_access(st[0]);
						if (Access == -1)
							break;
						if (st[1].equalsIgnoreCase("ip"))
						{
							IP = st[2];
							if (st.length > 3)
								NetMask = st[3];
							else
								NetMask = "255.255.255.255";
							AddItem(new IPAccess_Item(Access, IP, NetMask));
						}
						else if (st[1].equalsIgnoreCase("name"))
						{
							HostName = st[2];
							AddItem(new IPAccess_Item(Access, HostName));
						}						
					}
				}
			}
		}
		raf.close();

		return true;
	}*/
	
	public void clear()
	{
		list.removeAllElements();
	}
	
	public void AddItem(IPAccess_Item ipai)
	{
		list.addElement(ipai);
	}
	
	public int checkIP(String HostName)
	{
		Enumeration e = list.elements();
		IPAccess_Item ipai;
		while (e.hasMoreElements())
		{
			ipai = (IPAccess_Item)e.nextElement();
			if (ipai.checkIP(HostName))
				return ipai.Access;
		}
		return DefaultAccess;
	}
	
	public int checkIP(InetAddress ip)
	{
		Enumeration e = list.elements();
		IPAccess_Item ipai;
		while (e.hasMoreElements())
		{
			ipai = (IPAccess_Item)e.nextElement();
			if (ipai.checkIP(ip))
				return ipai.Access;
		}
		return DefaultAccess;		
	}
}
