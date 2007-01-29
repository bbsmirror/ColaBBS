
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.user;

import colabbs.bbstp.UniModuleCmd;
import colabbs.bbstp.UniModuleReply;

/**
 * tid: 代表此 talk request 的 id 值
 * ReplyNumber: -1 - 使用者不在線上,-2 - 型態無法接受,0-接受,其它表示拒絕理由及其代碼
 * ReplyString: 若自己輸入理由時此欄代表理由
 */
public class BBSTPTalkReply implements UniModuleCmd,UniModuleReply
{
	public int tid=0;
	public int ReplyNumber=0;
  public String ReplyString=null;

	public BBSTPTalkReply(int thetid,int theReplyNumber,String theReplyString)
	{
  	tid=thetid;
  	ReplyNumber=theReplyNumber;
    ReplyString=theReplyString;
	}
}