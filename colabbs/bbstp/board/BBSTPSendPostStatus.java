
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.board;

import colabbs.bbstp.UniModuleReply;

/**
 * 0:寄信成功
 * 1:權限不合
 * 2:系統錯誤
 */
public class BBSTPSendPostStatus implements UniModuleReply
{
	public int status=0;

	public BBSTPSendPostStatus(int theStatus)
	{
  	status=theStatus;
	}
} 