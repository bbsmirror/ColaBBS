package colabbs.UTILS;

import java.net.*;

public final class NET
{
	/**
	 * �Ǧ^ InetAddress �� HostName
	 * WilliamWey 990709
	 * 
	 * �]�� InetAddress.getHostName() �Ǧ^�Ӫ��ɭ�, �p�G Address �S�� DomainName, MSJVM �|�Ǧ^ �q���W��.
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
