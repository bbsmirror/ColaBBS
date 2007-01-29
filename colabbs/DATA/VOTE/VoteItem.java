package colabbs.DATA.VOTE;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.DATA.PROPERTIES.*;

public class VoteItem
{
	public final static int VOTE_STYLE_VOTE = 0;
	public final static int VOTE_STYLE_QUIZ = 1;
	public final static int VOTE_STYLE_DEFAULT = VOTE_STYLE_VOTE;
	
	public final static int VOTE_VOTING_NO = 0;
	public final static int VOTE_VOTING_YES = 1;
	public final static int VOTE_VOTING_RESULT = 2;
	//public final static int VOTE_VOTING_DELETED = 3;
		
	public String bmid = "";	//開投票的 id
	public String title = "";	//標題
	public long deadline = 0;	//開票時間
	public boolean revotable = false;	//是否可以更動選項
	public int style = VOTE_STYLE_DEFAULT;	//型態
	public int voting = VOTE_VOTING_NO;	//是否正在舉行投票
	public int choice = 0;
	public int select = 0;
	public boolean deleted = false; //刪除狀態?
	
	public String Path = null;
	
	public String[] choices;	

	public VoteItem()
	{
		
	}
	
	public void SaveFile(String xPath)
	{
		File F = new File(xPath);
		if (!F.exists())
			F.mkdirs();
		ColaProperties cp = new ColaProperties();
		cp.set("bmid", bmid);
		cp.set("title", title);
		cp.set("deadline", new String().valueOf(deadline));
		cp.set("revotable", new String().valueOf(revotable));
		cp.set("style", new String().valueOf(style));
		cp.set("choice", new String().valueOf(choice));
		cp.set("select", new String().valueOf(select));
		cp.SaveFile(xPath + "Vote.INI");
		cp.clear();
		int i;
		for (i = 1; i <= choice; i++)
			cp.set("C" + i, choices[i - 1]);
		cp.SaveFile(xPath + "Choices.INI");
	}
	
	public VoteItem(String xPath)
	{
		ColaProperties cp = new ColaProperties();
		cp.LoadFile(xPath + "Vote.INI");
		
		Path = xPath;
		bmid = cp.get("bmid");
		title = cp.get("title");
		deadline = Long.parseLong(cp.get("deadline"));
		revotable = Boolean.valueOf(cp.get("revotable")).booleanValue();
		style = Integer.parseInt(cp.get("style"));
		voting = isVoting();

		choice = Integer.parseInt(cp.get("choice"));
		select = Integer.parseInt(cp.get("select"));

		choices = new String[choice];
		
		ColaProperties cp1 = new ColaProperties();
		cp1.LoadFile(xPath + "Choices.INI");
		
		int i;
		for (i = 0; i < choice; i++)
			choices[i] = cp1.get("C" + i);
	}
	
	public String getWelcomeFile()
	{
		return Path + ".WELCOME";
	}
	
	public String getResultFile()
	{
		return Path + ".RESULT";
	}
	
	public boolean isVoted(String id)
	{
		File F = new File(Path + id.toLowerCase());
		return F.exists();
	}

	public void getVoted(String id, String selects, String msg[])
	{
		int i;
		ColaProperties cp = new ColaProperties();
		cp.LoadFile(Path + id.toLowerCase());
		selects = cp.get("SelectChoice");
		
		msg = new String[3];
		
		for (i = 0; i < 3; i++)
			msg[i] = cp.get("Msg" + i, "");
	}
	
	public void voteIt(String id, String selects, String msg[])
	{
		int i;
		ColaProperties cp = new ColaProperties();
		cp.set("SelectChoice", selects);
		for (i = 0; i < 3; i++)
			cp.set("Msg" + i, msg[i]);
		cp.SaveFile(Path + id.toLowerCase());
	}
	
	public boolean undeleetdIt()
	{
		boolean flag = true;
		
		File F = new File(Path + ".DELETED");
		
		if (!F.exists())
		{
			deleted = false;
			return flag;
		}
		
		try
		{
			F.delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			flag = false;			
		}
		finally
		{
			if (F.exists())
				flag = false;
		}
		
		deleted = flag;
		
		return flag;
	}
	
	public boolean deletedIt()
	{
		boolean flag = true;
		if (isDeleted())
			return flag;
		
		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(Path + ".DELETED", "rw");
			raf.writeLong((new Date()).getTime());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			flag = false;
		}
		finally
		{
			try
			{
				if (raf != null)
					raf.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				flag = false;
			}
		}
		
		deleted = flag;
		
		return flag;
	}
	
	public boolean isDeleted()
	{
		deleted = (new File(Path + ".DELETED")).exists();
		return deleted;
	}
	
	public boolean equals(VoteItem vi)
	{
		return vi.Path.equalsIgnoreCase(Path);
	}
	
	public int isVoting()
	{
		long now = System.currentTimeMillis();

		if (deadline - now > 0)
			return VOTE_VOTING_YES;

		if (deadline - now >= -86400 * 7 * 1000L)
			return VOTE_VOTING_RESULT;
		
		return VOTE_VOTING_NO;
	}
}
