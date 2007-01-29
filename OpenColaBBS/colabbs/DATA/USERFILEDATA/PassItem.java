package colabbs.DATA.USERFILEDATA;

import java.net.*;
import java.io.*;
import java.util.*;

public final class PassItem
{
	
	public int uid;
	public String IDItem, PassWordItem;
	
	/**
	 * 先拿回來用用
	 */
	public boolean BRCflag = false;
	public Hashtable BRC = null;
	//

	public PassItem(int uidbuf, String IDbuf, String PWDbuf)
	{
		uid = uidbuf;
		IDItem = IDbuf;
		PassWordItem = PWDbuf;
	}
	
	public PassItem(UserFileData ufd)
	{
		uid = ufd.uid;
		IDItem = ufd.ID;
		PassWordItem = ufd.PassWord;
	}
}