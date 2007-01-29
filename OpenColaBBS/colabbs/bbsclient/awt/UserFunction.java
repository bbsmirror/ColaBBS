
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.awt;

import java.awt.*;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.TabModule;
import colabbs.bbsclient.MultiModule;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbstp.user.BBSTPReceiveMsg;

public class UserFunction extends MultiModule implements TabModule
{
	private UserLister userLister1=null;
  //
	BorderLayout borderLayout1 = new BorderLayout();

	public UserFunction(ConnectionManager theConnection)
	{
  	super(theConnection);
		try
		{
  	  myName="使用者列表視窗";
    	userLister1=new UserLister(theConnection);
			jbInit();
      synchronized(theConnection)
      {
      	myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPReceiveMsg"),new CmdItem(getClass().getMethod("doReceiveMsg",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPReceiveMsg")}),this));
      }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

  public static MenuItem getFunctionItem()
  {
    return (new MenuItem("開新使用者列表視窗"));
  }

  public String getMyClassName()
  {
    return getClass().getName();
  }

	public UserFunction()
	{
		try 
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
    this.setBackground(SystemColor.control);
		this.setLayout(borderLayout1);
		this.add(userLister1, BorderLayout.CENTER);
	}

  public void doReceiveMsg(BBSTPReceiveMsg rm)
  {
//  	System.out.println("receive:"+rm.myMsg);
  	ReceiveMsgDialog rmd=new ReceiveMsgDialog(rm,myConnection);
    rmd.show();
    rmd.toFront();
  }

	public String getTabName()
	{
		//TODO: implement this colabbs.bbsclient.TabModule method;
    return "第一次親密接觸";
	}
}