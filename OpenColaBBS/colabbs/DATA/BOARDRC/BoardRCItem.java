package colabbs.DATA.BOARDRC;

import java.util.*;

import colabbs.DATA.BOARD.BoardItem;

public class BoardRCItem
{
	protected BoardItem board;
	protected Vector rclist = new Vector();
	
	public BoardRCItem(BoardItem bi)
	{
		board = bi;
	}

	//¤w¾\Åª¤å³¹
	private long getArticleId(String FileName)
	{
		int seeks = FileName.lastIndexOf(".");
		return Long.parseLong(FileName.substring(2, seeks).trim()) * 1000 + (int)(FileName.charAt(seeks + 1) - 'A');
	}
	
	public boolean isArticleViewed(String FileName)
	{
		return isArticleViewed(getArticleId(FileName));
	}
	
	public boolean isArticleViewed(long id)
	{
		return rclist.contains(new Long(id));
	}
	
	public void setArticleViewed(String FileName)
	{
		setArticleViewed(getArticleId(FileName));
	}
	
	public void setArticleViewed(long id)
	{
		//rclist.addElement(new Long(id));
		rclist.insertElementAt(new Long(id), 0);
	}
	
	public void setArticleNotViewedAll()
	{
		rclist.removeAllElements();
	}
	//	
}
