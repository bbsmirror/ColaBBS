
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

public class InputAddressDialog extends Dialog
{
	boolean cancel=true;
	Panel panel1 = new Panel();
	Label label1 = new Label();
	TextField textField1 = new TextField();
	Button button1 = new Button();
//	private String address;

	public InputAddressDialog(Frame frame, String title, boolean modal)
	{
		super(frame, title, modal);
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

	public InputAddressDialog(Frame frame)
	{
		this(frame, "請輸入站址", true);
	}

/*	public InputAddressDialog(Frame frame, boolean modal)
	{
		this(frame, "", modal);
	}

	public InputAddressDialog(Frame frame, String title)
	{
		this(frame, title, false);
	}*/

	void jbInit() throws Exception
	{
  	this.setBounds(100,100,330,220);
		panel1.setLayout(null);
		label1.setBounds(new Rectangle(44, 46, 230, 22));
		label1.setText("請輸入您欲連結的站址:");
		textField1.setBounds(new Rectangle(43, 73, 238, 25));
		textField1.setText("140.113.184.184");
		button1.setBounds(new Rectangle(133, 122, 62, 28));
		button1.setLabel("確定");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		panel1.add(label1, null);
		panel1.add(textField1, null);
		panel1.add(button1, null);
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

	public String getAddress()
	{
//		return address;
		if(cancel)
    	return null;
    else
			return textField1.getText();
	}

	void button1_actionPerformed(ActionEvent e)
	{
  	cancel=false;
    dispose();
	}
}