package colabbs.telnet.DATA.TBOARDRC;

import java.util.*;

import colabbs.DATA.BOARD.BoardItem;

/*
���ǪF�����ӬO���� colabbs.DATA ��������T
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

	//�����i���e��
	public boolean isWelcomeViewed()
	{
		return !(WelcomeTime < board.getWelcomeFileTime());
	}
	
	public void setWelcomeViewed()
	{
		WelcomeTime = board.getWelcomeFileTime();
	}
	//
	
	//�����ثe�Q�װϦ�m
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
