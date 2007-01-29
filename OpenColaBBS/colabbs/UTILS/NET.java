package colabbs.UTILS;

import java.net.*;

public final class NET
{
	/**
	 * 傳回 InetAddress 的 HostName
	 * WilliamWey 990709
	 * 
	 * 因為 InetAddress.getHostName() 傳回來的時候, 如果 Address 沒有 DomainName, MSJVM 會傳回 電腦名稱.
	 * 
	 */
	public static String GetHostName(InetAddress ia)
	{
		String tmp = ia.getHostName();
		if (tmp.indexOf('.') == -1)
			tmp = ia.getHostAddress();
		return tmp;
	}
}
