
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient;

import java.util.*;

public class OnlineUserEvent extends EventObject
{
	private Object myCmd=null;
  private ConnectionManager myConnection=null;

	public OnlineUserEvent(Object source)
	{
		super(source);
    myCmd=null;
	}

	public OnlineUserEvent(Object source,Object theCmd,ConnectionManager theConnection)
	{
		super(source);
    myCmd=theCmd;
    myConnection=theConnection;
	}

  public ConnectionManager getConnectionManager()
  {
  	return myConnection;
  }

  public Object getCmdItem()
  {
  	return myCmd;
  }
}