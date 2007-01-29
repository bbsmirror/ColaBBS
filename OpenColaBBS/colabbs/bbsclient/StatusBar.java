
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient;

public abstract class StatusBar
{
  public static StatusBar sysStatusBar=null;

  public static void setMessage(String theMsg)
  {
    if(sysStatusBar!=null)
      sysStatusBar.implementSetMessage(theMsg);
  }
  public static String getMessage(String theMsg)
  {
    if(sysStatusBar!=null)
      return sysStatusBar.implementGetMessage();
    return "";
  }
  public abstract void implementSetMessage(String theMsg);
  public abstract String implementGetMessage();
}