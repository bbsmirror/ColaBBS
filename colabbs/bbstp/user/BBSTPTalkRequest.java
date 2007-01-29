
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.user;

import colabbs.bbstp.UniModuleCmd;
//import colabbs.bbstp.UniModuleReply;
/**
 * talkMode: 0-�@����,1-�y�����,2-���T�|ĳ (1�P2�����Ӥ��\��)
 * UserID: ����ѹ�H���b��, Server �n�t�d�� receiver �令 pagger
 */
public class BBSTPTalkRequest implements UniModuleCmd
{
	public int talkMode=0;
	public String UserID=null;

	public BBSTPTalkRequest(int theMode,String theUserID)
  {
  	talkMode=theMode;
  	UserID=theUserID;
  }
}