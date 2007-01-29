
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp;

public class MultiModuleCmdAdapter implements MultiModuleCmd
{
  protected int myID=0;

	public MultiModuleCmdAdapter()
	{
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