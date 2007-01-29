
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient;

import java.util.*;

public interface OnlineUserListener extends EventListener
{

	public void AddOnlineUser(OnlineUserEvent e);

	public void RemoveOnlineUser(OnlineUserEvent e);

	public void UserStateChanged(OnlineUserEvent e);
} 