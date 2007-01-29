package colabbs;

import java.text.*;
import java.util.*;

public class DATE
{
	public Locale SysLocale;
	public TimeZone SysTimeZone;
	
	public SimpleDateFormat DateFormatter1;
	public SimpleDateFormat DateFormatter2;
	public SimpleDateFormat DateFormatter3;
	public SimpleDateFormat DateFormatter4;
	
	public DATE()
	{
		SysTimeZone = TimeZone.getTimeZone(ColaServer.INI.MyTimeZone);
		SysLocale = ColaServer.INI.myLocale;
		DateFormatter1 = new SimpleDateFormat("EEE MMM dd kk':'mm", Locale.ENGLISH);
		DateFormatter1.setTimeZone(SysTimeZone);
		DateFormatter2 = new SimpleDateFormat("EEE MMM dd kk':'mm':'ss yyyy", Locale.ENGLISH);
		DateFormatter2.setTimeZone(SysTimeZone);
		DateFormatter3 = new SimpleDateFormat("yy'/'MM'/'dd", Locale.ENGLISH);
		DateFormatter3.setTimeZone(SysTimeZone);
		DateFormatter4 = new SimpleDateFormat("kk':'mm", Locale.ENGLISH);
		DateFormatter4.setTimeZone(SysTimeZone); 
	}
	
	public Calendar getNow()
	{
		return Calendar.getInstance(SysTimeZone, SysLocale);
	}
	
	public Calendar getCalendar()
	{
		return Calendar.getInstance(SysTimeZone, SysLocale);
	}
}
