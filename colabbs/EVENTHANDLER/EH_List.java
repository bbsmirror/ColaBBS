package colabbs.EVENTHANDLER;

import java.util.*;

public class EH_List
{
	public String Name = "";
	private Vector list = new Vector();
							
	public EH_List(String name)
	{
		Name = name.trim();
	}
	
	public void clear()
	{
		list.removeAllElements();
	}
	
	public void add(EH_Item ehi)
	{
		list.addElement(ehi);
	}
	
	public void doHandle(Object event)
	{
		Enumeration e;
		e = list.elements();
		EH_Item ehi;
		while (e.hasMoreElements())
		{
			ehi = (EH_Item)e.nextElement();
			ehi.Handle(event);
		}
	}
}
