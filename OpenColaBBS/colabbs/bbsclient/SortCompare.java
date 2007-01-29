package colabbs.bbsclient;

public interface SortCompare
{
	//由大到小: O1 > O2 為 1
	//由小到大: O1 < O2 為 1
	public abstract int compare(Object O1, Object O2);
}
