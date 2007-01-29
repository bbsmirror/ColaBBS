package colabbs.DATA.CRONTAB;

import java.util.*;

import colabbs.*;

/**
 * 調整過的 CronTable 時間表示法:
 * 分鐘: 0 ~ 59
 * 小時: 0 ~ 23
 * 日期: 1 ~ 31
 * 月份: 1 ~ 12
 * 星期: 0 ~ 6 (星期天為 0)
 */
public class CronTab_Time
{
	private long Week = 0;
	private long Month = 0;
	private long Day = 0;
	private long Hour = 0;
	private long Minute = 0;

	private final static long ALL_WEEK = 127;
	private final static long ALL_MONTH = 4095;
	private final static long ALL_DAY = 2147483647;
	private final static long ALL_HOUR = 16777215;
	private final static long ALL_MINUTE = 1152921504606846975L;
	
	public CronTab_Time(int xWeek, int xMonth, int xDay, int xHour, int xMinute)
	{
		Week = getNumber(xWeek, ALL_WEEK);
		Month = getNumber(xMonth - 1, ALL_MONTH);
		Day = getNumber(xDay - 1, ALL_DAY);
		Hour = getNumber(xHour, ALL_HOUR);
		Minute = getNumber(xMinute, ALL_MINUTE);
	}
	
	public CronTab_Time(long xWeek, long xMonth, long xDay, long xHour, long xMinute)
	{
		Week = xWeek;
		Month = xMonth;
		Day = xDay;
		Hour = xHour;
		Minute = xMinute;
	}
	
	public CronTab_Time(BitSet xWeek, BitSet xMonth, BitSet xDay, BitSet xHour, BitSet xMinute)
	{
		Week = getNumber(xWeek, 0, 7, ALL_WEEK);
		Month = getNumber(xMonth, 1, 12, ALL_MONTH);
		Day = getNumber(xDay, 1, 31, ALL_DAY);
		Hour = getNumber(xHour, 0, 24, ALL_HOUR);
		Minute = getNumber(xMinute, 0, 60, ALL_MINUTE);
	}
	
	public boolean checkTime()
	{
		//return checkTime(Calendar.getInstance(TimeZone.getTimeZone(ColaServer.INI.MyTimeZone), ColaServer.INI.myLocale));
		return checkTime(ColaServer.SysDATE.getNow());
	}
	
	public boolean checkTime(Calendar time)
	{
		//return checkTime(time.get(Calendar.DAY_OF_WEEK), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
		return checkTime(time.get(Calendar.DAY_OF_WEEK) - time.getFirstDayOfWeek(), time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH), time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE));
	}
	
	public boolean checkTime(Date time)
	{
		//Calendar tmp = Calendar.getInstance(TimeZone.getTimeZone(ColaServer.INI.MyTimeZone), ColaServer.INI.myLocale);
		Calendar tmp = ColaServer.SysDATE.getNow();
		tmp.setTime(time);
		return checkTime(tmp);
	}
	
	public boolean checkTime(int xWeek, int xMonth, int xDay, int xHour, int xMinute)
	{
		return checkTime(get2pN(xWeek), get2pN(xMonth - 1), get2pN(xDay - 1), get2pN(xHour), get2pN(xMinute));
	}
	
	public boolean checkTime(long xWeek, long xMonth, long xDay, long xHour, long xMinute)
	{
		return !((Minute & xMinute) == 0 || (Hour & xHour) == 0 || (Day & xDay) == 0 || (Month & xMonth) == 0 || (Week & xWeek) == 0);
	}	
	
	private long get2pN(int n)
	{
		return (1L << n);
	}
	
	private long getNumber(int n, long all)
	{
		if (n < 0)
			return all;
		return get2pN(n);
	}
	
	private long getNumber(BitSet bs, int begin, int leng, long all)
	{
		if (bs == null)
			return all;
		return bitset2long(bs, begin, leng);
	}
	
	private long bitset2long(BitSet bs, int begin, int leng)
	{
		long n = 0;
		for (int i = 0; i < leng; i++)
		{
			n <<= 1;
			if (bs.get(begin + i))
				n |= 1;
		}
		return n;
	}	

	public void SetAll_Week()
	{
		Week = ALL_WEEK;
	}
	
	public void SetAll_Month()
	{
		Month = ALL_MONTH;
	}
	
	public void SetAll_Day()
	{
		Day = ALL_DAY;
	}
	
	public void SetAll_Hour()
	{
		Hour = ALL_HOUR;
	}
	
	public void SetAll_Minute()
	{
		Minute = ALL_MINUTE;
	}	
}
