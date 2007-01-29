
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

public class MakeSureDialog extends Dialog
{
	Panel panel1 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel2 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Button button1 = new Button();
	Button button2 = new Button();
	Panel panel3 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	Panel panel6 = new Panel();
	private boolean sure;
	Panel panel7 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Panel panel8 = new Panel();
	FlowLayout flowLayout2 = new FlowLayout();
	Label label1 = new Label();
	private String message;

	public MakeSureDialog(Frame frame, String title, boolean modal)
	{
		super(frame, title, modal);
		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		try
		{
			jbInit();
			add(panel1);
	    Toolkit myToolkit=this.getToolkit();
		  setLocation((myToolkit.getScreenSize().width+getSize().width)/2,(myToolkit.getScreenSize().height+getSize().height)/2);
//			pack();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public MakeSureDialog(Frame frame)
	{
		this(frame, "", false);
	}

	public MakeSureDialog(Frame frame, boolean modal)
	{
		this(frame, "", modal);
	}

	public MakeSureDialog(Frame frame, String title)
	{
		this(frame, title, true);
	}

	void jbInit() throws Exception
	{
		panel1.setLayout(borderLayout1);
		panel2.setLayout(flowLayout1);
		button1.setLabel("½T©w");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		button2.setLabel("¨ú®ø");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		panel3.setLayout(borderLayout2);
		flowLayout1.setAlignment(FlowLayout.RIGHT);
		panel7.setLayout(borderLayout3);
		panel4.setLayout(flowLayout2);
		label1.setBackground(SystemColor.control);
		panel1.add(panel2, BorderLayout.SOUTH);
		panel2.add(button1, null);
		panel2.add(button2, null);
		panel1.add(panel4, BorderLayout.CENTER);
		panel4.add(label1, null);
		panel1.add(panel5, BorderLayout.WEST);
		panel1.add(panel3, BorderLayout.EAST);
		panel3.add(panel6, BorderLayout.EAST);
		panel3.add(panel7, BorderLayout.CENTER);
		panel1.add(panel8, BorderLayout.NORTH);
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
    sure=false;
		dispose();
	}

	public boolean isSure()
	{
		return sure;
	}

	public void setMessage(String newMessage)
	{
		message = newMessage;
    label1.setText(message);
    pack();
    doLayout();
	}

	void button1_actionPerformed(ActionEvent e)
	{
    sure=true;
		dispose();
	}

	void button2_actionPerformed(ActionEvent e)
	{
    cancel();
	}
}
