package colabbs.UTILS;

public final class TIME
{
	public static void Delay(long delaytime)
	{
		try
		{
			Thread.sleep(delaytime);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
