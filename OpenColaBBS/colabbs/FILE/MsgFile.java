package colabbs.FILE;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;

public class MsgFile
{
	private String[] msg;
	private int msgs = 0;
	
	public MsgFile()
	{
		
	}
	
	public static String parserMsg(String xmsg, Object[] param)
	{
		int seeks;
		int l = xmsg.length();
		int i;
		char c1, c2;
		int p, x, lr;
		String tmp;
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < l; i++)
		{
			if ((c1 = xmsg.charAt(i)) == '%')
			{
				p = -1;
				x = -1;
				lr = 1;
here:
				for (i++; i < l; i++)
				{
					c2 = xmsg.charAt(i);
					if (c2 == '%')
					{
						sb.append(c2);
						break;
					}
					else if (c2 >= '0' && c2 <= '9')
					{
						if (p == -1)
							p = 0;
						p = p * 10 + c2 - '0';
					}
					else if (c2 == ':')
					{
						x = 0;
						for (i++; i < l; i++)
						{
							c2 = xmsg.charAt(i);
							if (c2 == '-' && x == 0)
								lr = -1;
							else if (c2 >= '0' && c2 <= '9')
								x = x * 10 + c2 - '0';
							else 
								break here;
						}
					}
					else 
						break;
				}
				p = p - 1;
				if (param.length > p && p >= -1)
				{
					if (p == -1)
						tmp = "";
					else
						tmp = (String)param[p];
					
					if (x == -1)
						sb.append(tmp);
					else if (lr == 1)
						sb.append(STRING.Cut(tmp, x));
					else
						sb.append(STRING.CutLeft(tmp, x));
				}
			}
			else
				sb.append(c1);				
		}
		return sb.toString();
	}
	
	public String getMsg(int n, Object[] param)
	{
		return parserMsg(getMsg(n), param);
	}
	
	public String getMsg(int n)
	{
		if (n < 1 || n > msgs)
			return "";
		return msg[n - 1];
	}
	
	public boolean LoadFile(String xFileName)
	{
		File F = new File(xFileName);
		if (!F.exists())
			return false;
		
		String tmp;
		char c;
		int l;
		Vector v = new Vector();
		//BufferedReader br = null;
		RandomAccessFile br = null;
		try
		{
			//br = new BufferedReader(new FileReader(F));
			br = new RandomAccessFile(F, "r");
			while ((tmp = br.readLine()) != null)
			{
				l = tmp.length();
				c = tmp.charAt(l - 1);
				if (c == 10 || c == 13)
				{
					tmp = tmp.substring(0, tmp.length() - 1);
					c = tmp.charAt(l - 2);
					if (c == 10 || c == 13)
						tmp = tmp.substring(0, tmp.length() - 1);
				}
				v.addElement(tmp);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
					br.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		msgs = v.size();
		msg = new String[msgs];
		for (int i = 0; i < msgs; i++)
			msg[i] = (String)v.elementAt(i);
		
		return true;
	}
}
