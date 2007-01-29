
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.board;

import colabbs.bbstp.UniModuleReply;

public class BBSTPPostSize implements UniModuleReply
{
	public int size=0;
	public BBSTPPostSize(int theSize)
	{
  	size=theSize;
	}
} 