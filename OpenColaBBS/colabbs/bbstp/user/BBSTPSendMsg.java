
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.user;

import colabbs.bbstp.UniModuleCmd;

public class BBSTPSendMsg implements UniModuleCmd
{
	public String userID=null;
  public String myMsg=null;

	public BBSTPSendMsg(String theID,String theMsg)
	{
  	userID=theID;
    myMsg=theMsg;
	}
}