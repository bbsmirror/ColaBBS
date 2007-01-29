package colabbs.DATA.IPACCESS;

import java.net.*;

import colabbs.UTILS.*;

public class IPAccess_Item
{
	private final static int STYLE_NONE = 0;
	private final static int STYLE_IP = 1;
	private final static int STYLE_HOSTNAME = 2;
	private int Style = STYLE_NONE;
	
	public int Access = -1;
	
	private String HostName;
	private byte IP[];
	private byte NetMask[];
	
	public IPAccess_Item(int access, String hostname)
	{
		setIP(access, hostname);
	}
	
	public IPAccess_Item(int access, String ip, String netmask)
	{
		setIP(access, ip, netmask);
	}
	
	public IPAccess_Item(int access, InetAddress ip, InetAddress netmask)
	{
		setIP(access, ip, netmask);
	}
	
	public boolean setIP(int access, String hostname)
	{
		Style = STYLE_HOSTNAME;
		Access = access;
		HostName = hostname.trim().toLowerCase();
		return true;
	}
	
	public boolean setIP(int access, String ip, String netmask)
	{
		try
		{
			if (!setIP(access, InetAddress.getByName(ip), InetAddress.getByName(netmask)))
				Style = STYLE_NONE;
		}
		catch (Exception e)
		{
			Style = STYLE_NONE;
		}
		return (Style != STYLE_NONE);
	}
	
	public boolean setIP(int access, InetAddress ip, InetAddress netmask)
	{
		Style = STYLE_IP;
		Access = access;
		IP = ip.getAddress();
		NetMask = netmask.getAddress();
		return true;
	}
	
	private boolean isIP(String hostname)
	{
		return (hostname.trim().toLowerCase().endsWith(HostName));
	}
	
	private boolean isIP(InetAddress ip)
	{
		byte tmp[] = ip.getAddress();
		for (int i = 0; i < 4; i++)
			if ((IP[i] & NetMask[i]) != (tmp[i] & NetMask[i]))
				return false;
		return true;		
	}
	
	public boolean checkIP(InetAddress ip)
	{
		switch (Style)
		{
		case STYLE_IP:
			return isIP(ip);
		case STYLE_HOSTNAME:
			return isIP(NET.GetHostName(ip));
		}
		return false;
	}
	
	public boolean checkIP(String hostname)
	{
		switch (Style)
		{
		case STYLE_IP:
			return isIP(hostname);
		case STYLE_HOSTNAME:
			try 
			{
				if (isIP(InetAddress.getByName(hostname)))
					return true;
			}
			catch (Exception e)
			{
				return false;
			}
		}
		return false;
	}
}
