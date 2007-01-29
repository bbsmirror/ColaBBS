
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.board;

import colabbs.record.PostType;
import colabbs.record.RecordType;
import colabbs.bbstp.BBSTPViewFileItem;

public class BBSTPPostItem extends BBSTPViewFileItem
{
	public String BoardName=null;
	public boolean Mark=false,Digest=false;
//	public boolean Link=false;
	public byte deliverTag=(byte)'L';
	public String poster=null;
//	public String title=null;
//  public long filetime=0L;
//	public String LastTitle=null;

  public BBSTPPostItem(String thePoster,String theTitle,long thefiletime,byte theDeliverTag,boolean theLink,boolean theMark,boolean theDigest)
  {
    super(theTitle,thefiletime,theLink);
    poster=thePoster;
//    title=theTitle;
//    filetime=thefiletime;
    deliverTag=theDeliverTag;
//    Link=theLink;
    Mark=theMark;
    Digest=theDigest;
  }
}
