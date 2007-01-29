package colabbs.DATA.VOTE;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;

public class VoteList
{
	public int voting = 0;
	public long deadline = 0;
	
	public String Path = new String("");
	public Vector list = new Vector();
			
	public VoteItem getVote(String xPath)
	{
		return new VoteItem(xPath);
	}
	
	private void finddeadline()
	{
		int i;
		deadline = 0;
		VoteItem vi;
		for (i = 0; i < list.size(); i++)
		{
			vi = (VoteItem)list.elementAt(i);
			if (vi.deadline > deadline)
			{
				voting = vi.voting;
				deadline = vi.deadline;
			}
		}
	}
	
	public VoteItem newVote(String id, String title, int style, long deadline, boolean revotable, int choice, int select, String[] choices)
	{
		VoteItem vi = new VoteItem();
		vi.bmid = id;
		vi.title = title;
		vi.style = style;
		vi.deadline = deadline;
		vi.revotable = revotable;
		vi.choice = choice;
		vi.select = select;
		vi.choices = choices;
		vi.SaveFile(Path + System.currentTimeMillis() / 1000 + File.separator);
		
		list.addElement(vi);
		finddeadline();
		
		return vi;
	}
	
	public void removeVote(VoteItem vi)
	{
		vi.deletedIt();
	}
	
	public void Load(BoardItem bi)
	{
		int i;
		Path = bi.getPath() + "Vote" + File.separator;
		File VL = new File(Path);
		String[] vl = VL.list();
		if (vl == null)
			return;
		File V;
		VoteItem vi = null;		
		for (i = 0; i < vl.length; i++)
		{
			V = new File(Path + vl[i] + File.separator);
			if (V.isDirectory())
			{
				if ((vi = getVote(V.getAbsolutePath())) != null)
				{
					list.addElement(vi);
				}
			}
		}
		finddeadline();
	}
}
