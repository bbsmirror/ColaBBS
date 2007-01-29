package colabbs.telnet.DATA.TBOARDRC;

import java.util.*;

import colabbs.DATA.BOARD.BoardItem;

/*
有些東西應該是移到 colabbs.DATA 內比較正確
*/
public class TBoardRCItem
{
	private BoardItem board;
	//private Vector rclist = new Vector();
	
	private long WelcomeTime = 0;
	
	private int CurrentPosition = -1;
	
	public TBoardRCItem(BoardItem bi)
	{
		board = bi;
	}

	//紀錄進版畫面
	public boolean isWelcomeViewed()
	{
		return !(WelcomeTime < board.getWelcomeFileTime());
	}
	
	public void setWelcomeViewed()
	{
		WelcomeTime = board.getWelcomeFileTime();
	}
	//
	
	//紀錄目前討論區位置
	public int getCurrentPosition()
	{
		int BoardLast = board.getPostNumber();
		if (CurrentPosition > BoardLast || CurrentPosition == -1)
		{
			CurrentPosition = BoardLast;
		}
		return CurrentPosition;
	}
	
	public void setCurrentPosition(int cp)
	{
		CurrentPosition = cp;
	}
	//
}
