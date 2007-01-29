
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.board;

import colabbs.bbstp.BBSTPDirCount;

public class BBSTPPostCount extends BBSTPDirCount
{
  public String myBoardName=null;

  public BBSTPPostCount(String theBoardName)
  {
    myBoardName=theBoardName;
  }
}
