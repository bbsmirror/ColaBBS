
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

import colabbs.bbstp.user.BBSTPSendMsg;
import colabbs.bbstp.user.BBSTPReceiveMsg;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.ConnectionManager;

public class ReceiveMsgDialog extends Dialog
{
	String sender;
	ConnectionManager myConnection=null;
//
	Panel panel1 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Button button1 = new Button();
	Button button2 = new Button();
	TextArea textArea1 = new TextArea();
	Panel panel6 = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	TextArea textArea2 = new TextArea();
	Panel panel7 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Label label1 = new Label();
	Panel panel8 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Label label2 = new Label();

	public ReceiveMsgDialog(Frame frame, String title, boolean modal,BBSTPReceiveMsg rm,ConnectionManager theConnection)
	{
		super(frame, title, modal);
    myConnection=theConnection;
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			add(panel1);
			pack();
      sender=rm.fromID;
      label1.setText("收到 "+rm.fromID+" 送來的簡訊:");
      textArea1.setText(ClientUtils.byte2String(rm.myMsg));
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

  public  ReceiveMsgDialog(BBSTPReceiveMsg rm,ConnectionManager theConnection)
  {
  	this(new Frame(),"收到簡訊",false,rm,theConnection);
  }

	void jbInit() throws Exception
	{
		panel1.setLayout(borderLayout1);
		panel5.setLayout(flowLayout1);
		button1.setLabel("關閉");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		button2.setLabel("回覆簡訊");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		panel2.setLayout(gridLayout1);
		gridLayout1.setRows(2);
		gridLayout1.setVgap(5);
		panel7.setLayout(borderLayout2);
		panel8.setLayout(borderLayout3);
		label2.setText("回覆訊息:");
		textArea1.setEditable(false);
		panel1.add(panel2, BorderLayout.CENTER);
		panel2.add(panel7, null);
		panel7.add(textArea1, BorderLayout.CENTER);
		panel7.add(label1, BorderLayout.NORTH);
		panel2.add(panel8, null);
		panel8.add(textArea2, BorderLayout.CENTER);
		panel8.add(label2, BorderLayout.NORTH);
		panel1.add(panel4, BorderLayout.WEST);
		panel1.add(panel5, BorderLayout.SOUTH);
		panel5.add(button1, null);
		panel5.add(button2, null);
		panel1.add(panel3, BorderLayout.EAST);
		panel1.add(panel6, BorderLayout.NORTH);
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

	void button1_actionPerformed(ActionEvent e)
	{
    cancel();
	}

	void button2_actionPerformed(ActionEvent e)
	{
  	myConnection.sendCmd(this,new BBSTPSendMsg(sender,ClientUtils.string2Byte(textArea2.getText())));
    cancel();
	}
}