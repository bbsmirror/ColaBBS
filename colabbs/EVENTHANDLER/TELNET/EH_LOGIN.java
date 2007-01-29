package colabbs.EVENTHANDLER.TELNET;

import colabbs.EVENTHANDLER.*;
import colabbs.telnet.*;

public abstract class EH_LOGIN extends EH_Item
{
	public int Handle(Object event)
	{
		return Handle((TelnetUser)event);
	}
	
	public abstract int Handle(TelnetUser who);
}
