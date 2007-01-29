package colabbs.DATA.CRONTAB;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;

public class CronTab extends Thread
{
	//public static Vector myTable=null;
	public static ThreadGroup myGroup=new ThreadGroup("CronTab");
	public Hashtable myTable=null;
	//可能會用完喔!!
	public long CID = -1;
	//詭異, 為什麼要用 static 呢?
	//public ThreadGroup myGroup=new ThreadGroup("CronTab");

	public CronTab()
	{
		super("CronTab");
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(Runnable target)
	{
		super(target);
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(Runnable target, String name)
	{
		super(target, name);
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(String name)
	{
		super(name);
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(ThreadGroup group, Runnable target)
	{
		super(group, target);
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(ThreadGroup group, Runnable target, String name)
	{
		super(group, target, name);
		myTable = new Hashtable();
		start();
	}
	
	public CronTab(ThreadGroup group, String name)
	{
		super(group, name);
		myTable = new Hashtable();
		start();
	}
	
	public long add(CronTabItem theItem)
	{
		synchronized(myTable)
		{
			//myTable.addElement(theItem);
			CID++;
			myTable.put(new Long(CID), theItem);
			return CID;
		}		
	}
	
	public void remove(long xCID)
	{
		synchronized(myTable)
		{
			myTable.remove(new Long(xCID));
		}
	}
	
	public void run()
	{
		long nmin=0;
		int nhour=0,nday=0,nmonth=0;
		byte nweek=(byte)0;
		
		while(true)
		{
			synchronized(myTable)
			{
				Enumeration Items=myTable.elements();
				//Calendar now = Calendar.getInstance(TimeZone.getTimeZone(ColaServer.INI.MyTimeZone), ColaServer.INI.myLocale);
				Calendar now = ColaServer.SysDATE.getNow();
				while(Items.hasMoreElements())
					((CronTabItem)Items.nextElement()).checkTime(now);
			}
			TIME.Delay(60000);
		}

	}
}