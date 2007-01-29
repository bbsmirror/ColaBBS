package colabbs.bbsclient.swing;

import java.awt.*;
//import colabbs.bbsclient.FunctionModule;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.MultiModule;
import javax.swing.*;
import javax.swing.event.*;

public class SwingMailFunction extends MultiModule
{
  BorderLayout borderLayout1 = new BorderLayout();
  ButtonGroup checkboxGroup1 = new ButtonGroup();
  JPanel mainPanel = new JPanel();
  CardLayout cardLayout1 = new CardLayout();
  SwingMailLister mailLister1 = null;

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

  public SwingMailFunction(ConnectionManager theConnection)
  {
    super(theConnection);
    myName="信件功能視窗";
    mailLister1=new SwingMailLister(theConnection);
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
}
