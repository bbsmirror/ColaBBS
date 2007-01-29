package colabbs.WW;

import java.lang.reflect.*;

public class NewsFilterItem
{
	public String FilterClassName;
	public Class FilterClass;
	
	public NewsFilterItem(String ClassName) throws Exception
	{
		FilterClassName = ClassName;
		FilterClass = Class.forName(ClassName);
	}
	public int Go(NewsArticle NA) throws Exception
	{
		//System.out.println(FilterClassName);
		Class ClassArgu[] = new Class[] {NA.getClass()};
		Object MethodArgu[] = new Object[] {NA};
		Object result = FilterClass.getMethod("filter", ClassArgu).invoke(null, MethodArgu);
		return ((Integer)result).intValue();
	}
	public int init()
	{
		Class ClassArgu[] = new Class[] {};
		Object MethodArgu[] = new Object[] {};
		Method m;
		Object result;
		try
		{
			m = FilterClass.getMethod("init", ClassArgu);
			result = m.invoke(null, MethodArgu);
		}
		catch(NoSuchMethodException e)
		{
			return 0;
		}
		catch(Exception e)
		{
			System.out.print("NewsFilterItem [");
			System.out.print(FilterClassName);
			System.out.print("] Error:");
			System.out.println(e.toString());
			return -1;
		}
		return ((Integer)result).intValue();
	}
}