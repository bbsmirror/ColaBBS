
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.board;

import colabbs.bbstp.BBSTPDirList;

public class BBSTPPostList extends BBSTPDirList
{
  public String myBoardName=null;

  public BBSTPPostList(String theBoardName,int theMode,int theRange)
  {
    super(theMode,theRange);
    myBoardName=theBoardName;
  }
}
