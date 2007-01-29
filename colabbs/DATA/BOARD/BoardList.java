package colabbs.DATA.BOARD;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.BoardSort.*;
import colabbs.UTILS.SORT.*;

public class BoardList
{
	public String BoardsFile = new String();
	
	private Hashtable boardslist = null;
	
	public int BoardSortNums = 0;
	public SortCompare BoardSortCompare[];
	
	public void initSortCompare()
	{
		BoardSortNums = 3;
		BoardSortCompare = new SortCompare[BoardSortNums];
		BoardSortCompare[0] = new ByBID();
		BoardSortCompare[1] = new ByName();
		BoardSortCompare[2] = new ByEGroup();
	}
	
	public Vector getSortedEGroupList(short EGroupNum, int userperm, int sortcomparenum)
	{
		Vector vl = new Vector();
		Enumeration e = boardslist.elements();
		BoardItem bi;
		while (e.hasMoreElements())
		{
			bi = (BoardItem)e.nextElement();
			if (bi.visible(userperm) && (EGroupNum == -1 || ColaServer.INI.BoardGroups[EGroupNum].indexOf((int)bi.EGroup) != -1))
				vl.addElement(bi);
		}
		
		Sort.QuickSort(vl, BoardSortCompare[sortcomparenum]);
		
		return vl;
	}
	
	public BoardList()
	{
		BoardsFile = ColaServer.INI.BBSHome + Consts.Boards;
		initSortCompare();
	}
	
	public BoardList(String FileName)
	{
		BoardsFile = FileName;
		initSortCompare();
	}
	
	public boolean exist(String bn)
	{
		return boardslist.containsKey(bn.toLowerCase());
	}
	
	public synchronized void deleteBoard(String bn)
	{
		BoardItem bi;
		if ((bi = (BoardItem)boardslist.get(bn.toLowerCase())) == null)
			return;
		boardslist.remove(bn.toLowerCase());
		bi.Name = "";
		save(bi);
	}
	
	public synchronized void renameTo(BoardItem bi, String nbn)
	{
		boardslist.remove(bi.Name.toLowerCase());
		bi.Name = nbn;
		boardslist.put(nbn.toLowerCase(), bi);
		save(bi);
	}
	
	public synchronized BoardItem newBoard(BoardItem bi)
	{
		File F = new File(BoardsFile);
		
		int i;
		byte buf[] = new byte[Consts.BOARDS_FILENAME];
		int L = (int)(F.length() / Consts.BOARDS_LENGTH);
		String bn;
		RandomAccessFile raf = null;
			
		try
		{
			raf = new RandomAccessFile(F, "r");
				
			for (i = 0; i < L; i++)
			{
				raf.seek(Consts.BOARDS_LENGTH * i);
				raf.read(buf);
				bn = (new String(buf, 0, Consts.BOARDS_FILENAME_P, Consts.BOARDS_FILENAME)).trim();
				if (bn.length() == 0)
					break;
			}
			raf.close();
			
			bi.bid = i;
			boardslist.put(bi.Name.toLowerCase(), bi);
			save(bi);
			raf = null;
			
			return bi;
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("BoardList.newBoard:", e);
			try
			{
				ColaServer.ErrorMsg = e.getMessage();
				if (raf != null)
					raf.close();
				return null;
			}
			catch (Exception e1)
			{
				ColaServer.logfile.WriteException("BoardList.newBoard:", e1);
				return null;
			}
		}
	}
	
	public void save(String bn)
	{
		save((BoardItem)boardslist.get(bn.toLowerCase()));
	}
	
	public synchronized void save(BoardItem bi)
	{
		RandomAccessFile raf = null;
		try
		{
			raf = new RandomAccessFile(BoardsFile, "rw");
				
			raf.seek(Consts.BOARDS_LENGTH * bi.bid);
			raf.write(bi.toFileBuf());
			raf.close();
			raf = null;
		}
		catch (Exception e)
		{
			ColaServer.logfile.WriteException("BoardList.save:", e);
			try
			{
				ColaServer.ErrorMsg = e.getMessage();
				if (raf != null)
					raf.close();
			}
			catch (Exception e1)
			{
				ColaServer.logfile.WriteException("BoardList.save:", e1);
			}
		}
	}
	
	public int boards()
	{
		return boardslist.size();
	}
	
	public Vector getEGroupList(short EGroupNum, int userperm)
	{
		Vector vl = new Vector();
		Enumeration e = boardslist.elements();
		BoardItem bi;
		while (e.hasMoreElements())
		{
			bi = (BoardItem)e.nextElement();
			if (bi.visible(userperm) && (EGroupNum == -1 || ColaServer.INI.BoardGroups[EGroupNum].indexOf((int)bi.EGroup) != -1))
				vl.addElement(bi);
		}
		return vl;		
	}
	
	public Vector getVisibleList(int userperm)
	{
		Vector vl = new Vector();
		Enumeration e = boardslist.elements();
		BoardItem bi;
		while (e.hasMoreElements())
		{
			bi = (BoardItem)e.nextElement();
			if (bi.visible(userperm))
				vl.addElement(bi);
		}
		return vl;
	}
	
	public Enumeration getlist()
	{
		return boardslist.elements();
	}
	
	public BoardItem get(String bn)
	{
		return (BoardItem)boardslist.get(bn.toLowerCase());
	}
	
	public boolean LoadFile(String FileName)
	{
		BoardsFile = FileName;
		return LoadFile();
	}
	
	public boolean LoadFile()
	{
		File F = new File(BoardsFile);

		if(F.exists())
		{
			int i;
			byte buf[] = new byte[Consts.BOARDS_LENGTH];
			int L = (int)(F.length() / Consts.BOARDS_LENGTH);
			String bn;
			RandomAccessFile raf = null;
			
			try
			{
				raf = new RandomAccessFile(F, "r");
				
				boardslist = new Hashtable(L);
				for (i = 0; i < L; i++)
				{
					raf.seek(Consts.BOARDS_LENGTH * i);
					raf.read(buf);
					bn = (new String(buf, 0, Consts.BOARDS_FILENAME_P, Consts.BOARDS_FILENAME)).trim();
					if (bn.length() != 0)
						boardslist.put(bn.toLowerCase(), new BoardItem(i, buf));
				}				
				raf.close();
				raf = null;
			}
			catch (Exception e)
			{
				ColaServer.logfile.WriteException("BoardList.LoadFile:", e);
				try
				{
					ColaServer.ErrorMsg = e.getMessage();
					if (raf != null)
						raf.close();
					return false;
				}
				catch (Exception e1)
				{
					ColaServer.logfile.WriteException("BoardList.LoadFile:", e1);
					return false;
				}
			}
		}
		else
			boardslist = new Hashtable();
		
		return true;
	}
}
