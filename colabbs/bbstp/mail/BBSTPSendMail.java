
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbstp.mail;

import colabbs.bbstp.UniModuleCmd;

public class BBSTPSendMail implements UniModuleCmd
{
	public String Receiver,Title;
  public int signature=0;

	public BBSTPSendMail(String theReceiver,String theTitle,int theSignature)
	{
  	Receiver=theReceiver;
    Title=theTitle;
    signature=theSignature;
	}
}