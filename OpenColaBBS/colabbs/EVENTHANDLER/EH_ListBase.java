package colabbs.EVENTHANDLER;

import java.util.*;

public class EH_ListBase
{
	public Hashtable list = new Hashtable();
	
	public void add(EH_List ehl)
	{
		list.put(ehl.Name.toLowerCase(), ehl);
	}
	
	public void clear()
	{
		list.clear();
	}
	
	public boolean exist(String name)
	{
		return list.containsKey(name.trim().toLowerCase());
	}
	
	public void doHandle(String name, Object event)
	{
		if (!exist(name))
			return;
		EH_List ehl = (EH_List)list.get(name.trim().toLowerCase());
		ehl.doHandle(event);
	}
}
