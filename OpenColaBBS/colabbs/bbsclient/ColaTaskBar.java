
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient;

import java.awt.*;
import java.awt.event.*;

public class ColaTaskBar extends Window
{
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel1 = new Panel();
	Button button1 = new Button();
	Panel panel2 = new Panel();
	ScrollPane scrollPane1 = new ScrollPane();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel3 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	FlowLayout flowLayout2 = new FlowLayout();
	Panel panel5 = new Panel();
	BorderLayout borderLayout4 = new BorderLayout();

	public ColaTaskBar()
	{
  	super(new Frame());
    Toolkit myToolkit=this.getToolkit();
	  setLocation(0,0);
    if(ClientUtils.isMS())
	    setSize(myToolkit.getScreenSize().width,57);
    else
	    setSize(myToolkit.getScreenSize().width,37);
    setBackground(SystemColor.control);
    show();
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

  public void paint(Graphics g)
  {
  	// don't know why need call this method!!
		if(isVisible())
	    validate();
    g.setColor(Color.white);
    g.drawLine(1,1,1,getBounds().height-2);
    g.drawLine(1,1,getBounds().width-2,1);
    g.setColor(Color.black);
    if(ClientUtils.isMS())
    {
  	  g.drawLine(1,getBounds().height-19,getBounds().width-1,getBounds().height-19);
    	g.drawLine(getBounds().width-1,1,getBounds().width-1,getBounds().height-19);
    }
    else
    {
  	  g.drawLine(1,getBounds().height-1,getBounds().width-1,getBounds().height-1);
    	g.drawLine(getBounds().width-1,1,getBounds().width-1,getBounds().height-1);
    }
  }

  public Insets getInsets()
  {
  	if(ClientUtils.isMS())
	  	return new Insets(3,3,20,3);
  	return new Insets(3,3,3,3);
  }

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		button1.setLabel("¡@¶}©l");
		panel2.setLayout(borderLayout2);
		panel3.setBounds(new Rectangle(108, 5, 63, 16));
		panel3.setLayout(flowLayout1);
		panel1.setLayout(flowLayout2);
		panel5.setLayout(borderLayout4);
		this.add(panel2, BorderLayout.CENTER);
		panel2.add(scrollPane1, BorderLayout.CENTER);
		scrollPane1.add(panel3, null);
		this.add(panel5, BorderLayout.WEST);
		panel5.add(panel1, BorderLayout.NORTH);
		panel1.add(button1, null);
	}


}