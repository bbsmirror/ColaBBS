package colabbs.telnet.DATA.TBOARDRC;

import java.util.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;

public class TBoardRCList
{
	/*���D
	�� BoardRC �J�W�Q�װϪ��W�Q���ܪ��ɭ�, �N�|�X�{���D�F, �ҥH�������ɭ�, ���ӬO���Q�װϪ� SerialNumber ���x�s�~��!!
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
