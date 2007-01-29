package colabbs.BoardSort;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.UTILS.SORT.*;

public class ByBID implements SortCompare
{
	public int compare(Object O1, Object O2)
	{
		if (((BoardItem)O1).bid < ((BoardItem)O2).bid)
			return 1;
		else
			return -1;
	}
}
