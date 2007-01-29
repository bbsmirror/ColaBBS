package colabbs.WW;

import java.io.*;

public class NewsFilter
{
	private NewsFilterItem Filter[];
	private int Filters = 0;
	
	public NewsFilter()
	{
	}
	public int Go(NewsArticle NA) throws Exception
	{
		if (Filters == 0)
			return NewsFilterReturnMode.Normal;
		int R;
		for (int i = 0; i < Filters; i++)
		{
			R = Filter[i].Go(NA);
			switch (R)
			{
			case NewsFilterReturnMode.Normal:
				break;
			case NewsFilterReturnMode.Cancle:
				return NewsFilterReturnMode.Cancle;
			case NewsFilterReturnMode.Forced:
				return NewsFilterReturnMode.Forced;
			default:
			}
		}
		return NewsFilterReturnMode.Normal;
	}
	
	public boolean LoadFile(String FileName)
	{
		File INIFile = new File(FileName);
		if (!INIFile.exists())
			return false;
		
		RandomAccessFile FilterFile = null;
		try
		{
			FilterFile = new RandomAccessFile(FileName, "r");
			String tmp;
			while((tmp = FilterFile.readLine()) != null)
			{
				tmp = tmp.trim();
				if (tmp.length() != 0)
					if (tmp.substring(0, 1) != "#" && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
						Filters++;
			}
			Filter = new NewsFilterItem[Filters];
			FilterFile.seek(0);
			for(int i = 0;i < Filters; i++)
			{
				tmp = FilterFile.readLine().trim();
				if (tmp.length() != 0)
					if (tmp.substring(0, 1) != "#" && tmp.charAt(0) != 13 && tmp.charAt(0) != 10)
						Filter[i] = new NewsFilterItem(tmp);
			}
			FilterFile.close();
				FilterFile = null;
		}
		catch (Exception e)
		{
			try
			{
				if (FilterFile != null)
					FilterFile.close();
				FilterFile = null;
			}
			catch (Exception e1)
			{
				
			}
		}		
		
		int R;
		for (int i = 0; i < Filters; i++)
			R = Filter[i].init();
		
		return true;
	}
}