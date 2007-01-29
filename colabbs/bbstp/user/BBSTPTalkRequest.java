
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
 * talkMode: 0-一般聊天,1-語音對談,2-視訊會議 (1與2為未來之功能)
 * UserID: 欲聊天對象的帳號, Server 要負責把 receiver 改成 pagger
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