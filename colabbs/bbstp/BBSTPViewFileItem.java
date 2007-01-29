
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp;

public class BBSTPViewFileItem extends BBSTPDirItem
{
  public boolean Link=false;
  public String title=null;
  public long filetime=0L;

  public BBSTPViewFileItem(String theTitle,long thefiletime,boolean theLink)
  {
    title=theTitle;
    filetime=thefiletime;
    Link=theLink;
  }
}