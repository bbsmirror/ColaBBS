package colabbs.telnet.DATA.TBOARDRC;

import java.util.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;

public class TBoardRCList
{
	/*問題
	當 BoardRC 遇上討論區版名被改變的時候, 就會出現問題了, 所以對應的時候, 應該是拿討論區的 SerialNumber 來儲存才對!!
	*/
	private Hashtable tbrclist = new Hashtable();
	
	public TBoardRCItem getTBRC(String BoardName)
	{
		return getTBRC(ColaServer.BList.get(BoardName));
	}

	public TBoardRCItem getTBRC(BoardItem bi)
	{
		TBoardRCItem tbrci = (TBoardRCItem)tbrclist.get(bi.Name.trim().toLowerCase());
		if (tbrci == null)
		{
			tbrci = new TBoardRCItem(bi);
			addTBRC(bi, tbrci);
		}
		return tbrci;
	}

	public void addTBRC(BoardItem bi, TBoardRCItem tbrci)
	{
		tbrclist.put(bi.Name.trim().toLowerCase(), tbrci);
	}
}
