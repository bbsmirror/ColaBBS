
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

public class MessageBox extends Dialog
{
	Panel panel1 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel2 = new Panel();
	Button button1 = new Button();
	Panel panel3 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Label label1 = new Label();

	public MessageBox(Frame frame, String title,String theMsg, boolean modal)
	{
		super(frame, title, modal);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			add(panel1);
      label1.setText(theMsg);
			pack();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

  public MessageBox(String theMsg)
  {
  	this(new Frame(),"系統訊息",theMsg,true);
  }

/*	public MessageBox(Frame frame)
	{
		this(frame, "", false);
	}

	public MessageBox(Frame frame, boolean modal)
	{
		this(frame, "", modal);
	}

	public MessageBox(Frame frame, String title)
	{
		this(frame, title, false);
	}*/

	void jbInit() throws Exception
	{
		panel1.setLayout(borderLayout1);
		button1.setLabel("確定");
		panel3.setLayout(flowLayout1);
		panel1.add(panel2, BorderLayout.SOUTH);
		panel2.add(button1, null);
		panel1.add(panel3, BorderLayout.CENTER);
		panel3.add(label1, null);
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
} 