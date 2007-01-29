
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp;

import colabbs.record.RecordType;

public abstract class BBSTPDirItem extends BBSTPItem implements MultiModuleReply//, Cloneable
{
  protected int myID=0;
  public int index=0;

  public BBSTPDirItem()
  {
  }

//  public abstract void setData(RecordType thert);
  public void setIndex(int theIndex)
  {
    index=theIndex;
  }
  public Object clone()
  {
  	try
	  {
		  return super.clone();
  	}
	  catch(CloneNotSupportedException e)
  	{
	  	e.printStackTrace();
  	}
	  return null;
  }

  public int getID()
  {
    //TODO: implement this colabbs.bbstp.MultiModuleCmd method;
    return myID;
  }

  public void setID(int theID)
  {
    //TODO: implement this colabbs.bbstp.MultiModuleCmd method;
    myID=theID;
  }
}