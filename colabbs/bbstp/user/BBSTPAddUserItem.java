
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.user;

import colabbs.bbstp.BBSTPItem;
import colabbs.bbstp.UniModuleReply;

public class BBSTPAddUserItem extends BBSTPItem implements UniModuleReply
{
  public String userID=null;
  public String userNick=null;
  public String userMode=null;
//Friend.get(new Integer(ColaServer.BBSUsers[ColaServer.SortedUser[ThisPage[i]]].uid)) //好友說明或nickname
  public String userHome=null;
  public int userPageMode=0;
  public int userMsgMode=0;
  public int count=0; //for Client to counting user duplicate. 

  public BBSTPAddUserItem(String theID,String theNick,String theHome,int thePageMode,int theMsgMode)
  {
    userID=theID;
    userNick=theNick;
//    userMode=theMode;
    userHome=theHome;
    userPageMode=thePageMode;
		userMsgMode=theMsgMode;
  }
}