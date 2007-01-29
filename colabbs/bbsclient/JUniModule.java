
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient;

import javax.swing.JPanel;

public abstract class JUniModule extends JPanel implements FunctionModule
{
  protected String myName="";
  protected ConnectionManager myConnection=null;

  public JUniModule(ConnectionManager theConnection)
  {
    myConnection=theConnection;
  }

  public ConnectionManager getConnection()
  {
    return myConnection;
  }

  public String getMyName()
  {
    return myName;
  }
}
