
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs;

import java.io.*;

import colabbs.DATA.USERFILEDATA.*;

public interface TalkUser
{
	public void sendMessage(byte theMessage);
	public void sendNewLine();
	public void sendBackSpace();
	public void sendQuit();
	public UserFileData getUFD();
	public void doTalkReply(TalkUser tu,int theReplyNumber,String theReplyString);
	public boolean stillWaiting();
}
