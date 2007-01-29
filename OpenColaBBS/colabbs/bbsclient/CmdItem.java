
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient;

import java.lang.reflect.*;

/**
 * 代表一個命令及其對應處理函式的類別
 */
public class CmdItem
{
  public Method myMethod=null;
  public Object myObject=null;
  public static Class Args[]=null;

  static
  {
    try
    {
      Args=new Class[]{Class.forName("java.lang.Object")};
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public CmdItem(Method theMethod,Object theObject)
  {
    myMethod=theMethod;
    myObject=theObject;
  }
} 
