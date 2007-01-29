package colabbs.UTILS.SORT;

import java.util.*;

public class Sort
{
	public static void InterChange(Object[] array, int i, int j)
	{
		Object tmp = array[i];
		array[i] = array[j];
		array[j] = array[i];
	}

	public static void InterChange(Vector list, int i, int j)
	{
		Object tmp = list.elementAt(i);
		list.setElementAt(list.elementAt(j), i);
		list.setElementAt(tmp, j);
	}
	
	public static void QuickSort(Object[] list, SortCompare C)
	{
		QuickSort(list, 0, list.length - 1, C);
	}
	
	public static void QuickSort(Object[] list, int left, int right, SortCompare C)
	{
		int i, j;
		Object p;
		Object tmp;
		if (left < right)
		{
			i = left;
			j = right + 1;
			p = list[left];
			do
			{
				while (i < right)
				{
					i++;
					if (C.compare(list[i], p) < 0)
						break;
				}
				while (j > left)
				{
					j--;
					if (C.compare(list[j], p) > 0)
						break;
				}
				if (i < j) 
					InterChange(list, i, j);
			} while (i < j);			
			InterChange(list, left, j);
			
			QuickSort(list, left, j - 1, C);
			QuickSort(list, j + 1, right, C);
		}		
	}
	
	public static void QuickSort(Vector list, SortCompare C)
	{
		QuickSort(list, 0, list.size() - 1, C);
	}
	
	public static void QuickSort(Vector list, int left, int right, SortCompare C)
	{
		int i, j;
		Object p;
		Object tmp;
		if (left < right)
		{
			i = left;
			j = right + 1;
			p = list.elementAt(left);
			do
			{
				while (i < right)
				{
					i++;
					if (C.compare(list.elementAt(i), p) < 0)
						break;
				}
				while (j > left)
				{
					j--;
					if (C.compare(list.elementAt(j), p) > 0)
						break;
				}
				if (i < j) 
					InterChange(list, i, j);
			} while (i < j);			
			InterChange(list, left, j);
			
			QuickSort(list, left, j - 1, C);
			QuickSort(list, j + 1, right, C);
		}
	}
}
