
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.board;

import colabbs.bbstp.UniModuleCmd;

public class BBSTPReadPost implements UniModuleCmd
{
	public String BoardName;
  public int index=-1;

	public BBSTPReadPost(String theBoard,int theIndex)
	{
  	BoardName=theBoard;
    index=theIndex;
	}
} 