package colabbs.UTILS.FILE;

import java.io.*;

import colabbs.UTILS.*;

public class FILES
{
	public static boolean deleteFile(File F)
	{
		int times = 0;
		while (times < 10 && F.exists() && !F.delete())
		{
			TIME.Delay(100);
			times++;
		}
		return !F.exists();
	}
}
