package colabbs.BoardSort;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.UTILS.SORT.*;

public class ByName implements SortCompare
{
	public int compare(Object O1, Object O2)
	{
		return (((BoardItem)O2).Name.compareTo(((BoardItem)O1).Name));
	}
}
