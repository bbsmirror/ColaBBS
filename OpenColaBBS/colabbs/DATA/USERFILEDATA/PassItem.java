package colabbs.DATA.USERFILEDATA;

import java.net.*;
import java.io.*;
import java.util.*;

public final class PassItem
{
	
	public int uid;
	public String IDItem, PassWordItem;
	
	/**
	 * �����^�ӥΥ�
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