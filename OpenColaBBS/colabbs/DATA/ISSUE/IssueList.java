package colabbs.DATA.ISSUE;

import java.util.*;

/** 
 * 進站畫面 by WilliamWey
 * 1999/09/07
 * 
 * 進站畫面的主體
 * 
 */
public class IssueList
{
	private Vector IList = null;
	
	private Random R;

	public IssueList()
	{
		IList = new Vector();
		clear();
		R = new Random();
	}
	
	public void add(IssueItem ii)
	{
		IList.addElement(ii);
	}
	
	public void add(String Text)
	{
		IList.addElement(new IssueItem(Text));
	}

	public void clear()
	{
		IList.removeAllElements();
	}
	
	public String getString()
	{
		return getItem().Text; 
	}

	public IssueItem getItem()
	{
		int S = IList.size();
		if (S == 0)
			return new IssueItem();
		int n = R.nextInt();
		if (n < 0)
			n += Integer.MAX_VALUE + 1;
		n %= S;
		return (IssueItem)IList.elementAt(n);
	}
}
