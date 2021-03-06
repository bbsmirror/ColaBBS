package colabbs.bbsclient;

import java.io.*;
import java.util.Vector;
import java.util.TimeZone;
import java.util.Locale;
import java.text.SimpleDateFormat;
/**
 * 跟 Client 端相關的一些工具函式
 */
public class ClientUtils
{
	private static boolean msNotTested=true;
	private static boolean isMS=false;
	private final static String buf="                                                                                ";
  public static TimeZone MyTimeZone=TimeZone.getDefault();
	public static SimpleDateFormat PostDateFormatter,MailDateFormatter;
  public static String myEncoding="Big5";

  static
  {
/*		DateFormatter1=new SimpleDateFormat ("EEE MMM dd kk':'mm",Locale.ENGLISH);
		DateFormatter1.setTimeZone(TimeZone.getTimeZone(MyTimeZone));
		DateFormatter2=new SimpleDateFormat ("EEE MMM dd kk':'mm':'ss yyyy",Locale.ENGLISH);
		DateFormatter2.setTimeZone(TimeZone.getTimeZone(MyTimeZone));*/
		PostDateFormatter=new SimpleDateFormat ("yy'/'MM'/'dd",Locale.ENGLISH);
		PostDateFormatter.setTimeZone(MyTimeZone);
/*		DateFormatter4=new SimpleDateFormat ("kk':'mm",Locale.ENGLISH);
		DateFormatter4.setTimeZone(TimeZone.getTimeZone(MyTimeZone));*/
  }


/**
 * 將 org 這個字串的長度變為 len
 */
	public static String Cut(String org,int len)
	{
		if(org.length()<len)
		{
			int i=len-org.length();

			return org+buf.substring(0,i);
		}
		else
			return org.substring(0,len);
	}

/**
 * 將 org 這個字串的長度變為 len，並靠右對齊
 */
	public static String CutLeft(String org,int len)
	{
		if(org.length()<len)
		{
			int i=len-org.length();

			return buf.substring(0,i)+org;
		}
		else
			return org.substring(org.length()-len);
	}

/**
 * 測試此 JavaVM 是否為 Microsoft 製造
 */
  public static boolean isMS()
  {
  	if(msNotTested)
    {
	    String vendor = System.getProperty("java.vendor");
    	if(vendor.indexOf("Microsoft")!=-1)
  	  	isMS=true;
  	  else
      	isMS=false;
      msNotTested=false;
    }
    return isMS;
  }
/**
 * 將一個 ASCII 的字串轉成 Unicode
 * @return java.lang.String
 * @param org java.lang.String
 */
	public static String byte2String(String org)
	{
		try
		{
			int len=org.length();
			byte tmp[]=new byte[len];
			for(int i=0;i<len;i++)
				tmp[i]=(byte)(org.charAt(i)&0xff);
			return new String(tmp,myEncoding);
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}
/**
 * 將一個 Unicode 的字串轉成 ASCII
 * @return java.lang.String
 * @param org java.lang.String
 */
	public static String string2Byte(String org)
	{
		try
		{
			byte tmp1[]=org.getBytes(myEncoding);
			int len=tmp1.length;
			char tmp2[]=new char[len];
			for(int i=0;i<len;i++)
				tmp2[i]=(char)(tmp1[i]&0xff);
			return new String(tmp2);
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

/**
 * 將一個 Unicode 的字串轉成 ASCII byte 陣列
 * @return java.lang.String
 * @param org java.lang.String
 */
	public static byte[] string2ByteArray(String org)
	{
		try
		{
			return org.getBytes(myEncoding);
		}
		catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return null;
	}

/**
 * 將 ANSI 控制碼移除
 */
	public static String CutAnsiCode(String Str)
	{
		char charbuf;
		short AnsiStart,AnsiEnd,i;

//		AnsiNum=0;
		if(Str.length()<3)
			return Str;
		String StrBuf=Str.toString();

		while((AnsiStart=(short)StrBuf.indexOf("["))!=(short)-1)
		{
			AnsiEnd=AnsiStart;
			for(i=(short)(AnsiStart+2);i<(short)StrBuf.length();i++)
			{
				charbuf=Character.toUpperCase(StrBuf.charAt((int)i));
				if(charbuf>='A'&&charbuf<='Z')
				{
					AnsiEnd=(short)i;
					break;
				}
		 	}
			if(AnsiStart!=AnsiEnd)
			{
//				LineCode[AnsiNum]=new AnsiCode(AnsiStart,StrBuf.substring(AnsiStart,AnsiEnd+1));
//				AnsiNum++;
				StrBuf=StrBuf.substring(0,AnsiStart)+StrBuf.substring(AnsiEnd+1);
	 		}
			else
				break;
		}
		return StrBuf;
	}
  
/**
 * 延遲 delaytime miliseconds
 */
	public static void Delay(long delaytime)
	{
		try
		{
			Thread.sleep(delaytime);
		}
		catch(InterruptedException e){}
	}
// copy from colabbs.Sort by WilliamWey (:
/**
 * 交換陣列兩元素位置
 */
	private static void InterChange(Object[] array, int i, int j)
	{
		Object tmp = array[i];
		array[i] = array[j];
		array[j] = array[i];
	}

/**
 * 交換Vector兩元素位置
 */
	private static void InterChange(Vector list, int i, int j)
	{
		Object tmp = list.elementAt(i);
		list.setElementAt(list.elementAt(j), i);
		list.setElementAt(tmp, j);
	}

/**
 * 排序一陣列
 */
	public static void QuickSort(Object[] list, SortCompare C)
	{
		QuickSort(list, 0, list.length - 1, C);
	}

/**
 * 排序一陣列
 */
	public static void QuickSort(Object[] list, int left, int right, SortCompare C)
	{
		int i, j;
		Object p;
		Object tmp;
		if (left < right)
		{
			i = left;
			j = right + 1;
			p = list[left];
			do
			{
				while (i < right)
				{
					i++;
					if (C.compare(list[i], p) < 0)
						break;
				}
				while (j > left)
				{
					j--;
					if (C.compare(list[j], p) > 0)
						break;
				}
				if (i < j)
					InterChange(list, i, j);
			} while (i < j);
			InterChange(list, left, j);

			QuickSort(list, left, j - 1, C);
			QuickSort(list, j + 1, right, C);
		}
	}

/**
 * 排序一 Vector
 */
	public static void QuickSort(Vector list, SortCompare C)
	{
		QuickSort(list, 0, list.size() - 1, C);
	}

/**
 * 排序一 Vector
 */
	public static void QuickSort(Vector list, int left, int right, SortCompare C)
	{
		int i, j;
		Object p;
		Object tmp;
		if (left < right)
		{
			i = left;
			j = right + 1;
			p = list.elementAt(left);
			do
			{
				while (i < right)
				{
					i++;
					if (C.compare(list.elementAt(i), p) < 0)
						break;
				}
				while (j > left)
				{
					j--;
					if (C.compare(list.elementAt(j), p) > 0)
						break;
				}
				if (i < j) 
					InterChange(list, i, j);
			} while (i < j);			
			InterChange(list, left, j);
			
			QuickSort(list, left, j - 1, C);
			QuickSort(list, j + 1, right, C);
		}
	}
}