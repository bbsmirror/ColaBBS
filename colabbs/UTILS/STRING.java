package colabbs.UTILS;

import java.util.*;
import colabbs.*;

public final class STRING
{
	public static String[] SToken(String tmp)
	{
		StringTokenizer st = new StringTokenizer(tmp, " ");
		int n = st.countTokens();
		String[] R = new String[n];
		for (int i = 0; i < n; i++)
			R[i] = st.nextToken();
		return R;
	}
	
	private static String AsciiToChineseString(String s) 
	{
		char[] orig = s.toCharArray();
		byte[] dest = new byte[orig.length];
		for (int i = 0; i < orig.length; i++)
			dest[i] = (byte)(orig[i] & 0xFF);
		try
		{
			sun.io.ByteToCharConverter toChar = sun.io.ByteToCharConverter.getConverter(ColaServer.INI.Encoding);
			return new String(toChar.convertAll(dest));
		}
		catch (Exception e) 
		{
			System.out.println(e);
			return s;
		}
	}
	
	private static String ChineseStringToAscii(String s) 
	{
		try 
		{
			sun.io.CharToByteConverter toByte = sun.io.CharToByteConverter.getConverter(ColaServer.INI.Encoding);
			byte[] orig = toByte.convertAll(s.toCharArray());
			char[] dest = new char[orig.length];
			for (int i = 0; i < orig.length; i++)
				dest[i] = (char)(orig[i] & 0xFF);
			return new String(dest);
		}
		catch (Exception e) 
		{
			System.out.println(e);
			return s;
		}
	}
	
	public static String String_U2A(String inStr)
	{
		return new String(inStr.getBytes(), 0);
	}
	
	public static String String_A2U(String inStr)
	{
		char buf[] = inStr.toCharArray();
		int l = buf.length;
		byte bbuf[] = new byte[l];
		for (int i = 0; i < l; i++)
			bbuf[i] = (byte)buf[i];
		return new String(bbuf);
	}
	
	public static String Space(int leng)
	{
		String buf = "                                                                                ";
		String tmp = "";
		if (leng <= 0)
			return tmp;
		while (leng >= 80)
		{
			tmp += buf;
			leng -= 80;
		}
		return tmp + buf.substring(0, leng);
	}
	
	public static String Left(String what, int leng)
	{
		String tmp;
		int n = leng - what.length();
		tmp = what + Space(n);
		
		return tmp.substring(0, leng);
	}
	
	public static String Right(String what, int leng)
	{
		String tmp;
		int n = leng - what.length();
		tmp = Space(n) + what;
		
		return tmp.substring(tmp.length() - leng);
	}
	
	public static String Cut(String org, int len)
	{
		return Left(org, len);
	}
	
	public static String CutLeft(String org, int len)
	{
		return Right(org, len);
	}
	
	public static void getBytes(String org, int begin, int maxlen, byte target[], int target_begin)
	{
		if (org != null)
		{
			int n = org.length() % maxlen;
			if (n == 0)
				n = maxlen;
			org.getBytes(begin, n, target, target_begin);
		}
	}
}
