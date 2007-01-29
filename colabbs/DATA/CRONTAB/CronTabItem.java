package colabbs.DATA.CRONTAB;

import java.lang.reflect.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;

public class CronTabItem implements Runnable
{
	/*private long min = 0;
	private int hour = 0;
	private int day = 0;
	private int month = 0;
	private byte week = 0;*/
	
	private CronTab_Time myTime = null;
	
	private Method myMethod = null;
	private Object myObj = null;
	private Object args[] = null;
	
	public CronTabItem(CronTab_Time theTime, Method theMethod, Object theObject, Object theargs[])
	{
		myTime = theTime;
		
		myMethod = theMethod;
		myObj = theObject;
		args = theargs;		
	}
	
	/**
	 * This method was writen by yhwu.
	 * @param themin long
	 * @param thehour int
	 * @param theday int
	 * @param themonth int
	 * @param theweek byte
	 * @param theMethod java.lang.reflect.Method
	 * @param theObject java.lang.Object
	 * @param theargs java.lang.Object[]
	 */
	//public CronTabItem(long themin, int thehour, int theday, int themonth, byte theweek, Method theMethod, Object theObject, Object theargs[])
	public CronTabItem(long themin, long thehour, long theday, long themonth, long theweek, Method theMethod, Object theObject, Object theargs[])
	{
		/*min = themin;
		hour = thehour;
		day = theday;
		month = themonth;
		week = theweek;*/
		
		myTime = new CronTab_Time(theweek, themonth, theday, thehour, themin);
		
		myMethod = theMethod;
		myObj = theObject;
		args = theargs;
	}

	public CronTabItem(int themin, int thehour, int theday, int themonth, int theweek, Method theMethod, Object theObject, Object theargs[])
	{
		myTime = new CronTab_Time(theweek, themonth, theday, thehour, themin);
		
		myMethod = theMethod;
		myObj = theObject;
		args = theargs;		
	}
	
	public CronTabItem(int themin, int thehour, int theday, int themonth, int theweek, Method theMethod, Object theargs[])
	{
		myTime = new CronTab_Time(theweek, themonth, theday, thehour, themin);
		
		myMethod = theMethod;
		args = theargs;		
	}
	
	public CronTabItem(BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek, Method theMethod, Object theargs[])
	{
		myTime = new CronTab_Time(theweek, themonth, theday, thehour, themin);
		
		myMethod = theMethod;
		args = theargs;		
		
		/*min = bitset2long(themin, 0, 59);
		hour = (int)bitset2long(thehour, 0, 23);
		day = (int)bitset2long(theday, 1, 31);
		month = (int)bitset2long(themonth, 1, 12);
		week = (byte)bitset2long(theweek, 0, 6);*/
	}
	
	public CronTabItem(BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek,Method theMethod,Object theObject,Object theargs[])
	{
		myTime = new CronTab_Time(theweek, themonth, theday, thehour, themin);
		
		myMethod = theMethod;
		myObj = theObject;
		args = theargs;
		
		/*min = bitset2long(themin, 0, 59);
		hour = (int)bitset2long(thehour, 0, 23);
		day = (int)bitset2long(theday, 1, 31);
		month = (int)bitset2long(themonth, 1, 12);
		week = (byte)bitset2long(theweek, 0, 6);*/
	}
	
	/**
	 * This method was writen by yhwu.
	 * @param themin java.util.BitSet
	 * @param thehour java.util.BitSet
	 * @param theday java.util.BitSet
	 * @param themonth java.util.BitSet
	 * @param theweek java.util.BitSet
	 * @param theMethod java.lang.reflect.Method
	 * @param theargs java.lang.Object[]
	 */
	/*public CronTabItem(BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek, Method theMethod, Object theargs[])
	{
		myMethod = theMethod;
		args = theargs;
		
		long tmpl = 1;
		int tmpi = 1;
		byte tmpb = 1;
		
		for(int i = 0; i < 60; i++)
		{
			if (themin.get(i))
				min |= tmpl;
			tmpl <<= 1;
		}
		
		for(int i = 0; i < 24; i++)
		{
			if (thehour.get(i))
				hour |= tmpi;
			tmpi <<= 1;
		}
		
		tmpi = 1;
		for(int i = 1; i <= 31; i++)
		{
			if (theday.get(i))
				day |= tmpi;
			tmpi <<= 1;
		}
		
		tmpi = 1;
		for(int i=1;i<=12;i++)
		{
			if(themonth.get(i))
				month|=tmpi;
			tmpi<<=1;
		}
		
		for(int i=1;i<=7;i++)
		{
			if(theweek.get(i))
				week|=tmpb;
			tmpb<<=1;
		}
	}*/
	
	/**
	 * Create a CronTabItem. All postive index bits out of range will be ignored.
	 * Negtive index will throws IndexOutOfBoundException.
	 * @param themin java.util.BitSet 60 bits for active minutes, range 0~59.
	 * @param thehour java.util.BitSet 24 bits for active hours, range 0~23.
	 * @param theday java.util.BitSet 31 bits for active days, range 1~31.
	 * @param themonth java.util.BitSet 12 bits for active months, range 1~12.
	 * @param theweek java.util.BitSet 7 bits for active week, range 1~7.
	 */
	/*public CronTabItem(BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek,Method theMethod,Object theObject,Object theargs[])
	{
		myMethod=theMethod;
		myObj=theObject;
		args=theargs;
		
		long tmpl=1;
		int tmpi=1;
		byte tmpb=1;
		
		for(int i=0;i<60;i++)
		{
			if(themin.get(i))
				min|=tmpl;
			tmpl<<=1;
		}
		
		for(int i=0;i<24;i++)
		{
			if(thehour.get(i))
				hour|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=31;i++)
		{
			if(theday.get(i))
				day|=tmpi;
			tmpi<<=1;
		}
		
		tmpi=1;
		for(int i=1;i<=12;i++)
		{
			if(themonth.get(i))
				month|=tmpi;
			tmpi<<=1;
		}
		
		for(int i=1;i<=7;i++)
		{
			if(theweek.get(i))
				week|=tmpb;
			tmpb<<=1;
		}
	}*/
	
	/**
	 * This method was writen by yhwu.
	 * @param nmin long
	 * @param nhour int
	 * @param nday int
	 * @param nmonth int
	 * @param nweek byte
	 */
	public void checkTime(long nmin, int nhour, int nday, int nmonth, byte nweek)
	{
		//if(((nmin & min) != 0) && ((nhour & hour) != 0) && ((nday & day) != 0) && ((nmonth & month) != 0) && ((nweek & week) != 0))
		if (myTime.checkTime(nmin, nhour, nday, nmonth, nweek))
		{
			Thread theThread = new Thread(CronTab.myGroup, this);
			theThread.setPriority((int)(theThread.getPriority() / 2));//reduce the priority to half
			theThread.start();
		}
	}
	
	public void checkTime(Calendar time)
	{
		if (myTime.checkTime(time))
		{
			Thread theThread = new Thread(CronTab.myGroup, this);
			theThread.setPriority((int)(theThread.getPriority() / 2));//reduce the priority to half
			theThread.start();			
		}
	}
	
	/**
	 * This method was writen by yhwu.
	 */
	public void run()
	{
		try
		{
			myMethod.invoke(myObj, args);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * This method was writen by yhwu.
	 * @param pid int
	 */
	public static void telnetSetCron(BitSet themin, BitSet thehour, BitSet theday, BitSet themonth, BitSet theweek,int pid)
	{
		String outbuf;
		StringBuffer Ans=new StringBuffer();
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Clear();
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,6);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends("Work minutes:\r\n");

		outbuf=themin.toString();
		//	((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,7);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
		if(outbuf.length()>80)
		{
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
		}
		else
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,10);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends("Work hours:\r\n");
		//	((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,11);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
		outbuf=thehour.toString();
		if(outbuf.length()>80)
		{
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
		}
		else
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,13);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends("Work days:\r\n");
		//	((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,14);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
		outbuf=theday.toString();
		if(outbuf.length()>80)
		{
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
		}
		else
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,16);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends("Work months:\r\n");
		//	((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,17);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(themonth.toString()+"\r\n");
		
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,19);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends("Work week days:\r\n");
		//	((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,20);
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
		((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(theweek.toString()+"\r\n");
		
		while(true)
		{
			
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[393]);
			if((((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=new colabbs.telnet.LineEdit(Ans,2,pid,true)).DoEdit()<0)
				return;
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
			if(Ans.length()==0)
				break;
			try
			{
				if(Ans.length()==1&&(Ans.charAt(0)=='a'||Ans.charAt(0)=='A'))
					for(int i=0;i<60;i++)
						themin.set(i);
				else
				{
					int temp=Integer.parseInt(Ans.toString());
					if(temp>=0&&temp<60)
					{
						BitSet bt=new BitSet(60);
						bt.set(temp);
						themin.xor(bt);
					}
				}
			}
			catch(NumberFormatException e)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Bell();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(1000);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			}
			outbuf=themin.toString();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,7);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			if(outbuf.length()>80)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
			}
			else
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		}
		
		while(true)
		{
			
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[394]);
			if((((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=new colabbs.telnet.LineEdit(Ans,2,pid,true)).DoEdit()<0)
				return;
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
			if(Ans.length()==0)
				break;
			try
			{
				if(Ans.length()==1&&(Ans.charAt(0)=='a'||Ans.charAt(0)=='A'))
					for(int i=0;i<24;i++)
						thehour.set(i);
				else
				{
					int temp=Integer.parseInt(Ans.toString());
					if(temp>=0&&temp<24)
					{
						BitSet bt=new BitSet(24);
						bt.set(temp);
						thehour.xor(bt);
					}
				}
			}
			catch(NumberFormatException e)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Bell();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(1000);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			}
			outbuf=thehour.toString();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,11);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			if(outbuf.length()>80)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
			}
			else
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		}
		
		while(true)
		{
			
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[395]);
			if((((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=new colabbs.telnet.LineEdit(Ans,2,pid,true)).DoEdit()<0)
				return;
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
			if(Ans.length()==0)
				break;
			try
			{
				if(Ans.length()==1&&(Ans.charAt(0)=='a'||Ans.charAt(0)=='A'))
					for(int i=1;i<32;i++)
						theday.set(i);
				else
				{
					int temp=Integer.parseInt(Ans.toString());
					if(temp>0&&temp<32)
					{
						BitSet bt=new BitSet(32);
						bt.set(temp);
						theday.xor(bt);
					}
				}
			}
			catch(NumberFormatException e)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Bell();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(1000);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			}
			outbuf=theday.toString();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,14);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			if(outbuf.length()>80)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(0,80)+"\r\n");
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf.substring(80)+"\r\n");
			}
			else
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(outbuf+"\r\n");
		}
		
		while(true)
		{
			
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[396]);
			if((((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=new colabbs.telnet.LineEdit(Ans,2,pid,true)).DoEdit()<0)
				return;
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
			if(Ans.length()==0)
				break;
			try
			{
				if(Ans.length()==1&&(Ans.charAt(0)=='a'||Ans.charAt(0)=='A'))
					for(int i=1;i<13;i++)
						themonth.set(i);
				else
				{
					int temp=Integer.parseInt(Ans.toString());
					if(temp>0&&temp<13)
					{
						BitSet bt=new BitSet(13);
						bt.set(temp);
						themonth.xor(bt);
					}
				}
			}
			catch(NumberFormatException e)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Bell();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(1000);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			}
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,17);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(themonth.toString()+"\r\n");
		}
		
		while(true)
		{
			
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,4);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			ColaServer.BBSUsers[pid].sends(Prompt.Msgs[397]);
			if((((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=new colabbs.telnet.LineEdit(Ans,1,pid,true)).DoEdit()<0)
				return;
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).GetData=null;
			if(Ans.length()==0)
				break;
			try
			{
				if(Ans.length()==1&&(Ans.charAt(0)=='a'||Ans.charAt(0)=='A'))
					for(int i=0;i<7;i++)
						theweek.set(i);
				else
				{
					int temp=Integer.parseInt(Ans.toString());
					if(temp>=0&&temp<7)
					{
						BitSet bt=new BitSet(7);
						bt.set(temp);
						theweek.xor(bt);
					}
				}
			}
			catch(NumberFormatException e)
			{
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).Bell();
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,24);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
				ColaServer.BBSUsers[pid].sends(Prompt.Msgs[16]);
				ColaServer.BBSUsers[pid].flush();
				TIME.Delay(1000);
				((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).printendline();
			}
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).move(1,20);
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).clrtoeol();
			((colabbs.telnet.TelnetUser)ColaServer.BBSUsers[pid]).sends(theweek.toString()+"\r\n");
		}
	}
}