
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.user;

import colabbs.bbstp.UniModuleCmd;

/**
 * tid: �N�� talk request �� id ��
 * talkMode: �N��w talk �����A @see BBSTPTalkRequest
 * UserID: �N��I�s�̪� ID
 */
public class BBSTPTalkCheck implements UniModuleCmd
{
	public int tid=0;
  public int talkMode=0;
	public String UserID=null;

	public BBSTPTalkCheck(int thetid,int theTalkMode,String theUserID)
	{
  	tid=thetid;
    talkMode=theTalkMode;
    UserID=theUserID;
	}
}