
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp;

public class BBSTPDirList extends MultiModuleCmdAdapter
{
  public int mode=0;
  public int range=0;

  public BBSTPDirList(int theMode,int theRange)
  {
    mode=theMode;
    range=theRange;
  }
}
