
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.mail;

import colabbs.bbstp.UniModuleReply;
/**
 * 0:寄信成功
 * 1:權限不合
 * 2:本站不接受發送 InternetMail 要求
 * 3:系統錯誤
 */
public class BBSTPSendMailStatus implements UniModuleReply
{
	public int status=0;

	public BBSTPSendMailStatus(int theStatus)
	{
  	status=theStatus;
	}
} 