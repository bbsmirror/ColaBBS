package colabbs.DATA.BOARDRC;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.USERFILEDATA.*;

import colabbs.UTILS.FILE.*;

public class BoardRCList
{
	/*問題
	當 BoardRC 遇上討論區版名被改變的時候, 就會出現問題了, 所以對應的時候, 應該是拿討論區的 SerialNumber 來儲存才對!!
	*/
	private UserFileData UFD;
	private Hashtable brclist = new Hashtable();

	public BoardRCList(UserFileData ufd)
	{
		UFD = ufd;
	}
	
	public BoardRCItem getBRC(String BoardName)
	{
		return getBRC(ColaServer.BList.get(BoardName));
	}

	public BoardRCItem getBRC(BoardItem bi)
	{
		BoardRCItem brci = (BoardRCItem)brclist.get(bi.Name.trim().toLowerCase());
		if (brci == null)
		{
			brci = new BoardRCItem(bi);
			addBRC(bi, brci);
		}
		return brci;
	}

	public void addBRC(BoardItem bi, BoardRCItem brci)
	{
		brclist.put(bi.Name.trim().toLowerCase(), brci);
	}
	
	private String getBoardRCFileName()
	{
		return ColaServer.INI.BBSHome + "home" + File.separator + Character.toUpperCase(UFD.ID.charAt(0)) + File.separator + UFD.ID + File.separator + ".boardrc";
	}
	
	public void LoadFile()
	{
		RandomAccessFile raf = null;
		byte buf[] = new byte[16];
		int seeks = 0;
		int size;
		BoardItem bi;
		BoardRCItem brci;
		String bn;
		int n;
		
		try
		{
			File F = new File(getBoardRCFileName());
			if (!F.exists())
				return;
			raf = new RandomAccessFile(F, "r");
			size = (int)raf.length();
			while (seeks < size)
			{
				raf.read(buf);
				n = buf[15];
				bn = new String(buf, 0, 15);
				if (bn.indexOf(0) != -1)
					bn = bn.substring(0, bn.indexOf(0));

				bi = ColaServer.BList.get(bn);
				brci = new BoardRCItem(bi);
				addBRC(bi, brci);
				for (int i = 0; i < n; i++)
					brci.setArticleViewed(raf.readLong());
				seeks += 16 + n * 8;
			}
			raf.close();
			raf = null;
		}
		catch (Exception e)
		{
			try
			{
				if (raf != null)
					raf.close();
				raf = null;
			}
			catch (Exception e1)
			{
				
			}
		}
	}
	
	public void SaveFile()
	{
		RandomAccessFile raf = null;
		byte buf[];
		BoardItem bi;
		BoardRCItem brci;
		int n;
		Enumeration list;
		
		synchronized(this)
		{
			try
			{
				File F = new File(getBoardRCFileName());
				if (F.exists())
					if (!FILES.deleteFile(F))
						return;
									
				raf = new RandomAccessFile(F, "rw");
				list = brclist.elements();
				while (list.hasMoreElements())
				{
					brci = (BoardRCItem)list.nextElement();
					n = brci.rclist.size() & 0xff;
					if (n > 0)
					{
						buf = new byte[16];						
						if(brci.board.Name.length() < 15)
							brci.board.Name.getBytes(0, brci.board.Name.length(), buf, 0);
						else
							brci.board.Name.getBytes(0, 15, buf, 0);
						buf[15] = (byte)n;
						raf.write(buf);
						for (int i = 0; i < n; i++)
							raf.writeLong(((Long)(brci.rclist.elementAt(i))).longValue());
					}
				}
				raf.close();
				raf = null;
			}
			catch (Exception e)
			{
				try
				{
					if (raf != null)
						raf.close();
					raf = null;
				}
				catch (Exception e1)
				{
					
				}				
			}
		}
	}
}
