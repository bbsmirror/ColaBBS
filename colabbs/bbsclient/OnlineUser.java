
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient;

import colabbs.bbsclient.compare.UserByID;
import colabbs.bbstp.user.BBSTPChangeState;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbstp.user.BBSTPRemoveUserItem;
import colabbs.bbstp.user.BBSTPUserItemCount;

import java.util.*;

/**
 * 管理客戶端上線使用者的類別
 */
public class OnlineUser
{
	private static UserByID myUserByID=new UserByID();
  private ConnectionManager myConnection=null;
	private int myOnlineUserCount=0;
  private int theOnlineUserCount=0;
	private Hashtable myOnlineUser=new Hashtable();
	private transient Vector onlineUserListeners;

	public OnlineUser(ConnectionManager theConnection)
	{
  	myConnection=theConnection;
	}

  public synchronized void addOnlineUser(BBSTPAddUserItem aui)
  {
//  	System.out.println("AddOnlineUser:"+aui.userID);
  	BBSTPAddUserItem tmp=(BBSTPAddUserItem)myOnlineUser.get(aui.userID);
  	if(tmp!=null)
    	tmp.count++;
    else
    {
    	aui.count=1;
      myOnlineUserCount++;
	  	myOnlineUser.put(aui.userID,aui);
      fireAddOnlineUser(new OnlineUserEvent(this,aui,myConnection));
    }
  }

  public synchronized void removeOnlineUser(BBSTPRemoveUserItem rui)
  {
//  	System.out.println("RemoveOnlineUser:"+rui.userID);
  	BBSTPAddUserItem tmp=(BBSTPAddUserItem)myOnlineUser.get(rui.userID);
  	if(tmp!=null)
    {
    	tmp.count--;
      if(tmp.count==0)
      {
        fireRemoveOnlineUser(new OnlineUserEvent(this,rui,myConnection));
	      myOnlineUserCount--;
		  	myOnlineUser.remove(tmp.userID);
      }
    }
  }

  public synchronized void changeUserState(BBSTPChangeState cs)
  {
  	BBSTPAddUserItem tmp=(BBSTPAddUserItem)myOnlineUser.get(cs.userID);
  	if(tmp!=null)
    {
    	tmp.userMode=cs.userState;
      this.fireUserStateChanged(new OnlineUserEvent(this,cs,myConnection));
    }
  }

  public synchronized BBSTPAddUserItem getUserItem(String userID)
  {
  	return (BBSTPAddUserItem)myOnlineUser.get(userID);
  }

  public synchronized Vector getOnlineUsers()
  {
		Vector vl = new Vector();
		Enumeration e = myOnlineUser.elements();
		while (e.hasMoreElements())
			vl.addElement(e.nextElement());

		ClientUtils.QuickSort(vl, myUserByID);

		return vl;
  }

  public synchronized void setUserItemCount(BBSTPUserItemCount uic)
  {
  	theOnlineUserCount=uic.Count;
  }

	public synchronized void removeOnlineUserListener(OnlineUserListener l)
	{
		if(onlineUserListeners != null && onlineUserListeners.contains(l))
		{
			Vector v = (Vector) onlineUserListeners.clone();
			v.removeElement(l);
			onlineUserListeners = v;
		}
	}

	public synchronized void addOnlineUserListener(OnlineUserListener l)
	{
		Vector v = onlineUserListeners == null ? new Vector(2) : (Vector) onlineUserListeners.clone();
		if(!v.contains(l))
		{
			v.addElement(l);
			onlineUserListeners = v;
		}
	}

	protected void fireAddOnlineUser(OnlineUserEvent e)
	{
		if(onlineUserListeners != null)
		{
			Vector listeners = onlineUserListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((OnlineUserListener) listeners.elementAt(i)).AddOnlineUser(e);
			}
		}
	}

	protected void fireRemoveOnlineUser(OnlineUserEvent e)
	{
		if(onlineUserListeners != null)
		{
			Vector listeners = onlineUserListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((OnlineUserListener) listeners.elementAt(i)).RemoveOnlineUser(e);
			}
		}
	}

	protected void fireUserStateChanged(OnlineUserEvent e)
	{
		if(onlineUserListeners != null)
		{
			Vector listeners = onlineUserListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((OnlineUserListener) listeners.elementAt(i)).UserStateChanged(e);
			}
		}
	}
}
