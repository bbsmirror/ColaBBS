
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
 * 0:�H�H���\
 * 1:�v�����X
 * 2:�t�ο��~
 */
public class BBSTPSendPostStatus implements UniModuleReply
{
	public int status=0;

	public BBSTPSendPostStatus(int theStatus)
	{
  	status=theStatus;
	}
} 