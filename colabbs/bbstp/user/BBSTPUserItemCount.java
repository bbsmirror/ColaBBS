
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.user;

import colabbs.bbstp.UniModuleReply;

public class BBSTPUserItemCount implements UniModuleReply
{
  public int Count=0;

  public BBSTPUserItemCount(int theCount)
  {
    Count=theCount;
  }
}