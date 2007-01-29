
//Title:        Cola BBS System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       yhwu
//Company:      infoX
//Description:

package colabbs.bbsclient.awt;

import java.awt.*;
//import colabbs.bbsclient.FunctionModule;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.MultiModule;
import colabbs.bbsclient.TabModule;

public class MailFunction extends MultiModule implements TabModule
{
  BorderLayout borderLayout1 = new BorderLayout();
  Panel mainPanel = new Panel();
  CardLayout cardLayout1 = new CardLayout();
  MailLister mailLister1 = null;

/*  public MailFunction()
  {
    try
    {
      jbInit();
      doLayout();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void setConnection(ConnectionManager theConnection)
  {
    super.setConnection(theConnection);
    mailLister1=new MailLister(theConnection);
    mainPanel.add(mailLister1, "mailLister1");
  }*/

  public MailFunction(ConnectionManager theConnection)
  {
    super(theConnection);
    myName="信件功能視窗";
    mailLister1=new MailLister(theConnection);
    try
    {
      jbInit();
      doLayout();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    this.setBackground(SystemColor.control);
		this.setEnabled(true);
    mainPanel.setLayout(cardLayout1);
    this.add(mainPanel, BorderLayout.CENTER);
    mainPanel.add(mailLister1, "mailLister1");
  }

  public static MenuItem getFunctionItem()
  {
    return (new MenuItem("開新信件功能視窗"));
  }

  public String getMyClassName()
  {
    return getClass().getName();
  }

	public String getTabName()
	{
		//TODO: implement this colabbs.bbsclient.TabModule method;
    return "電子郵件傳情意";
	}
}
