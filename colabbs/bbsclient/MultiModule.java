
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient;

import java.awt.Panel;

import colabbs.bbsclient.ConnectionManager;

public abstract class MultiModule extends Panel implements FunctionModule
{
  protected int myCounter=0;
  protected String myName="";
//  private static int counter=0;
  protected ConnectionManager myConnection=null;

  public MultiModule()
  {
  }

  public void setConnection(ConnectionManager theConnection)
  {
    myConnection=theConnection;
    myCounter=changeCounter(1,myConnection,getMyClassName());
  }

  public MultiModule(ConnectionManager theConnection)
  {
    myConnection=theConnection;
    myCounter=changeCounter(1,myConnection,getMyClassName());
  }

  public abstract String getMyClassName();

  protected static synchronized int changeCounter(int mode,ConnectionManager myConnection,String myClassName)
  {
    ModuleCounter counter=(ModuleCounter)myConnection.getData(myClassName+"_Counter");
    if(counter==null)
    {
      counter=new ModuleCounter();
      myConnection.setData(myClassName+"_Counter",counter);
    }
    switch(mode)
    {
      case 1:
        counter.myCounter++;
        break;
      case 2:
        counter.myCounter--;
        break;
      case 3:
        counter.myCounter=0;
        break;
    }
    return counter.myCounter;
  }

  public String getMyName()
  {
    return myName+":"+myCounter;
  }

  public ConnectionManager getConnection()
  {
    return myConnection;
  }
}
