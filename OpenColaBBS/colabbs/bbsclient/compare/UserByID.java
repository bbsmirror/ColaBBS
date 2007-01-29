
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.compare;

import colabbs.bbsclient.SortCompare;
import colabbs.bbstp.user.BBSTPAddUserItem;

public class UserByID implements SortCompare
{
	public int compare(Object O1, Object O2)
	{
		//TODO: implement this colabbs.bbsclient.SortCompare method;
  	return ((BBSTPAddUserItem)O2).userID.compareTo(((BBSTPAddUserItem)O1).userID);
	}
}