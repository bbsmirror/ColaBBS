
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.server;

import java.util.Hashtable;

import colabbs.TalkUser;

public class TalkPair
{
  private static int tidCount=0;
  public static Hashtable tidTable=new Hashtable();
	public int talkMode=0;
	public TalkUser User=null;

  public synchronized static int getTID()
  {
  	return tidCount++;
  }

  public synchronized static void putTalkPair(int tid,TalkPair tp)
  {
  	tidTable.put(new Integer(tid),tp);
  }

  public synchronized static TalkPair getTalkPair(int tid)
  {
  	return (TalkPair)tidTable.get(new Integer(tid));
  }

  public synchronized static TalkUser getTalkUser(int tid)
  {
  	TalkPair tp=(TalkPair)tidTable.get(new Integer(tid));
    if(tp!=null)
	  	return tp.User;
    else
    	return null; 
  }

  public synchronized static void removeTalkPair(int tid)
  {
  	tidTable.remove(new Integer(tid));
  }

	public TalkPair(int theTalkMode,TalkUser theUser)
	{
		talkMode=theTalkMode;
    User=theUser;
	}
} 