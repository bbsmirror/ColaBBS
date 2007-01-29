
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient;

import java.awt.Panel;

public class UniModule extends Panel implements FunctionModule
{
  protected String myName="";
  protected ConnectionManager myConnection=null;

  public void setConnection(ConnectionManager theConnection)
  {
    myConnection=theConnection;
  }

  public UniModule()
  {
  }

  public UniModule(ConnectionManager theConnection)
  {
    myConnection=theConnection;
  }

  public String getMyName()
  {
    return myName;
  }

  public ConnectionManager getConnection()
  {
    return myConnection;
  }
}
