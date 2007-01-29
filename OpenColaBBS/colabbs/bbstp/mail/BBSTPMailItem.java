
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.mail;

import colabbs.bbstp.BBSTPViewFileItem;

public class BBSTPMailItem extends BBSTPViewFileItem
{
	public boolean Read=false,Mark=false;
	public String sender=null;

  public BBSTPMailItem(String theSender,String theTitle,long thefiletime,boolean theLink,boolean theMark,boolean theRead)
  {
    super(theTitle,thefiletime,theLink);
    sender=theSender;
    Mark=theMark;
    Read=theRead;
  }
}