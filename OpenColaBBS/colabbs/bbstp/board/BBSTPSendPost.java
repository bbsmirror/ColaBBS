
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.board;

import colabbs.bbstp.UniModuleCmd;

public class BBSTPSendPost implements UniModuleCmd
{
	public String BoardName,Title;
  public int signature=0;
  public boolean localSave=true;
  public boolean anonymous=false;

	public BBSTPSendPost(String theBoard,String theTitle,int theSignature,boolean theLocalSave,boolean theAnonymous)
	{
  	BoardName=theBoard;
    Title=theTitle;
    signature=theSignature;
    localSave=theLocalSave;
    anonymous=theAnonymous;
	}
}