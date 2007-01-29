package colabbs.UTILS;

import colabbs.*;
import colabbs.DATA.USERFILEDATA.*;

public class BBS
{
	public static boolean HasAllPerm(int pid,int PermBuf)
	{
		return (ColaServer.BBSUsers[pid].UFD.Perm&PermBuf)==PermBuf;
	}
	public static boolean HasOnePerm(int pid,int PermBuf)
	{
		if(PermBuf!=0)
			return (ColaServer.BBSUsers[pid].UFD.Perm&PermBuf)!=0;
		else
			return true;
	}
	public static int IfOnline(String TestID)
	{
		int i=0,l,r,cmp=0;

		if(ColaServer.onlineuser==0)
			return -1;

		synchronized(ColaServer.SortedUser)
		{
			l=0;
			r=ColaServer.onlineuser-1;
			i=(ColaServer.onlineuser-1)/2;

			do
			{
				cmp=TestID.toUpperCase().compareTo(ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID.toUpperCase());
				
				//Add by DarkSmile '98/3/6
				/*				if(cmp==0)
				{
				if(TestID.length()>ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID.length())
				cmp++;
				else
				cmp--;
				}	*/
				if(cmp==0)
					break;
				
				if(cmp<0)
				{
					r=i;
					i=(l+i)/2;
				}
				else if(cmp!=0)
				{
					l=i;
					i=(i+r+1)/2;
				}
			}while(cmp!=0&&l<i&&r>i);
			if(TestID.equalsIgnoreCase(ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID))
				return ColaServer.SortedUser[i];
		}
		return -1;
	}
	public static int IfOnlineSort(String TestID)
	{
		int i=0,l,r,cmp=0;

		if(ColaServer.onlineuser==0)
			return -1;

		synchronized(ColaServer.SortedUser)
		{
			l=0;
			r=ColaServer.onlineuser-1;
			i=(ColaServer.onlineuser-1)/2;

			do
			{
				cmp=TestID.toUpperCase().compareTo(ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID.toUpperCase());
				if(cmp<0)
				{
					r=i;
					i=(l+i)/2;
				}
				else if(cmp!=0)
				{
					l=i;
					i=(i+r+1)/2;
				}
			}while(cmp!=0&&l<i&&r>i);
			if(TestID.equalsIgnoreCase(ColaServer.BBSUsers[ColaServer.SortedUser[i]].UFD.ID))
				return i;
		}
		return -1;
	}
	public static void SetUserMode(int pid,String Mode)
	{
		ColaServer.BBSUsers[pid].usermode=Mode;
	}
	
	/**
	 * ·sªº
	 */
	public static boolean CheckOnePerm(int UserPerm,int PermBuf)
	{
		if(PermBuf!=0)
			return (UserPerm&PermBuf)!=0;
		else
			return true;
	}	
}
