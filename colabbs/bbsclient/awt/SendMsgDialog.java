
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.awt;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import colabbs.bbstp.user.BBSTPSendMsg;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.ConnectionManager;

public class SendMsgDialog extends Dialog
{
	private ConnectionManager myConnection=null;
	private Vector OnlineUsers=null;
//
	Panel panel1 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel2 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Button button1 = new Button();
	Button button2 = new Button();
	Button button3 = new Button();
	Panel panel3 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel4 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Panel panel5 = new Panel();
	Label label1 = new Label();
	BorderLayout borderLayout4 = new BorderLayout();
	Panel panel6 = new Panel();
	Panel panel7 = new Panel();
	Panel panel8 = new Panel();
	Panel panel9 = new Panel();
	BorderLayout borderLayout5 = new BorderLayout();
	Panel panel10 = new Panel();
	Panel panel11 = new Panel();
	Panel panel12 = new Panel();
	BorderLayout borderLayout6 = new BorderLayout();
	GridLayout gridLayout1 = new GridLayout();
	Checkbox checkbox1 = new Checkbox();
	Checkbox checkbox2 = new Checkbox();
	CheckboxGroup checkboxGroup1 = new CheckboxGroup();
	Choice choice1 = new Choice();
	Panel panel13 = new Panel();
	Panel panel14 = new Panel();
	Panel panel15 = new Panel();
	Panel panel16 = new Panel();
	BorderLayout borderLayout7 = new BorderLayout();
	TextArea textArea1 = new TextArea();
	Label label2 = new Label();
	Panel panel17 = new Panel();
	CardLayout cardLayout1 = new CardLayout();
	Choice choice2 = new Choice();

	public SendMsgDialog(Frame frame, String title, boolean modal,ConnectionManager theConnection)
	{
		super(frame, title, modal);
    myConnection=theConnection;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			add(panel1);
			pack();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public SendMsgDialog(Frame frame,ConnectionManager theConnection)
	{
		this(frame, "傳送簡訊", true,theConnection);
	}

/*	public SendMsgDialog(Frame frame, boolean modal)
	{
		this(frame, "傳送簡訊", modal);
	}

	public SendMsgDialog(Frame frame, String title)
	{
		this(frame, title, true);
	}*/

	void jbInit() throws Exception
	{
		panel1.setLayout(borderLayout1);
		panel2.setLayout(flowLayout1);
		button1.setLabel("送出簡訊");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		button2.setLabel("清除重寫");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		button3.setLabel("取消不送了");
		button3.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button3_actionPerformed(e);
			}
		});
		panel3.setLayout(borderLayout2);
		panel4.setLayout(borderLayout3);
		panel5.setLayout(borderLayout4);
		label1.setText("送簡訊給");
		panel8.setLayout(borderLayout5);
		panel9.setLayout(borderLayout6);
		panel12.setLayout(gridLayout1);
		gridLayout1.setRows(2);
		checkbox1.setBackground(SystemColor.control);
		checkbox1.setCheckboxGroup(checkboxGroup1);
		checkbox1.setLabel("好友");
		checkbox2.setBackground(SystemColor.control);
		checkbox2.setCheckboxGroup(checkboxGroup1);
		checkbox2.setLabel("線上使用者");
		checkboxGroup1.setSelectedCheckbox(checkbox1);
		panel13.setLayout(borderLayout7);
		label2.setText("簡訊內容");
		panel17.setLayout(cardLayout1);
		panel8.setBackground(SystemColor.control);
		panel9.setBackground(SystemColor.control);
		choice1.setBackground(SystemColor.control);
		choice2.setBackground(SystemColor.control);
		textArea1.setBackground(SystemColor.control);
		panel1.add(panel2, BorderLayout.SOUTH);
		panel2.add(button1, null);
		panel2.add(button2, null);
		panel2.add(button3, null);
		panel1.add(panel3, BorderLayout.CENTER);
		panel3.add(panel4, BorderLayout.NORTH);
		panel4.add(panel5, BorderLayout.NORTH);
		panel5.add(panel6, BorderLayout.SOUTH);
		panel5.add(panel7, BorderLayout.EAST);
		panel5.add(panel8, BorderLayout.WEST);
		panel8.add(panel10, BorderLayout.WEST);
		panel8.add(label1, BorderLayout.CENTER);
		panel5.add(panel9, BorderLayout.CENTER);
		panel9.add(panel12, BorderLayout.WEST);
		panel12.add(checkbox1, null);
		panel12.add(checkbox2, null);
		panel9.add(panel17, BorderLayout.CENTER);
		panel17.add(choice1, "choice1");
		panel17.add(choice2, "choice2");
		panel5.add(panel11, BorderLayout.NORTH);
		panel3.add(panel13, BorderLayout.CENTER);
		panel13.add(panel16, BorderLayout.NORTH);
		panel16.add(label2, null);
		panel13.add(panel15, BorderLayout.WEST);
		panel13.add(panel14, BorderLayout.EAST);
		panel13.add(textArea1, BorderLayout.CENTER);
	}

	protected void processWindowEvent(WindowEvent e)
	{
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
		{
			cancel();
		}
		super.processWindowEvent(e);
	}

	void cancel()
	{
		dispose();
	}

  public void setOnlineUsers(Vector theUsers)
  {
  	OnlineUsers=theUsers;
    for(Enumeration e=OnlineUsers.elements();e.hasMoreElements();)
    {
    	BBSTPAddUserItem tmp=(BBSTPAddUserItem)e.nextElement();
      choice1.add(tmp.userID+"("+ClientUtils.byte2String(tmp.userNick)+")");
    }
  }

  public void setFriend(Hashtable theFriend)
  {
  	if(OnlineUsers!=null)
    {
	    for(Enumeration e=OnlineUsers.elements();e.hasMoreElements();)
  	  {
	    	BBSTPAddUserItem tmp=(BBSTPAddUserItem)e.nextElement();
        if(theFriend.containsKey(tmp.userID))
	  	    choice2.add(tmp.userID+"("+ClientUtils.byte2String(tmp.userNick)+")");
    	}
    }
  }

  public void setReceiver(int index)
  {
  	choice1.select(index);
  }

	void button3_actionPerformed(ActionEvent e)
	{
    cancel();
	}

	void button2_actionPerformed(ActionEvent e)
	{
		textArea1.setText("");
	}

	void button1_actionPerformed(ActionEvent e)
	{
  	if(myConnection!=null)
    {
    	if(checkbox1.getState())
      {
      	if(choice1.getSelectedIndex()!=-1)
//		    	System.out.println(((BBSTPAddUserItem)OnlineUsers.elementAt(choice1.getSelectedIndex())).userID+":"+ClientUtils.string2Byte(textArea1.getText()));
			    	myConnection.sendCmd(this,new BBSTPSendMsg(((BBSTPAddUserItem)OnlineUsers.elementAt(choice1.getSelectedIndex())).userID,ClientUtils.string2Byte(textArea1.getText())));
      }
      else
      {
      }
    }
    cancel();
	}
}