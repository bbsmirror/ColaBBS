
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
 * tid: �N�� talk request �� id ��
 * ReplyNumber: -1 - �ϥΪ̤��b�u�W,-2 - ���A�L�k����,0-����,�䥦��ܩڵ��z�ѤΨ�N�X
 * ReplyString: �Y�ۤv��J�z�Ѯɦ���N��z��
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