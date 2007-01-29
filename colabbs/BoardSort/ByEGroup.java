package colabbs.BoardSort;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.UTILS.SORT.*;

public class ByEGroup implements SortCompare
{
	public int compare(Object O1, Object O2)
	{
		if (((BoardItem)O1).EGroup < ((BoardItem)O2).EGroup)
			return 1;
		else
			return -1;
	}
}
