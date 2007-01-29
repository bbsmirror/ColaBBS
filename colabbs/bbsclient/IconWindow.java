
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient;

import java.awt.*;

public class IconWindow extends Window
{
	java.awt.Image myIcon=null;
	private boolean dished;

  public IconWindow()
  {
  	super(new Frame());
		setBackground(SystemColor.control);
    if(ClientUtils.isMS())
      setSize(16,37);
    else
	    setSize(16,16);
  }

	public IconWindow(Image theIcon)
	{
  	super(new Frame());
  	myIcon=theIcon;
		setBackground(SystemColor.control);
    if(ClientUtils.isMS())
      setSize(16,37);
    else
	    setSize(16,16);
	}

  public void paint(Graphics g)
  {
  	if(myIcon!=null)
    {
      if(ClientUtils.isMS())
      {
      	validate();
		    if(dished)
    		{
  	    	g.setColor(SystemColor.control);
    	  	g.drawLine(0,17,16,17);
      		g.setColor(SystemColor.controlLtHighlight);
	      	g.drawLine(0,18,16,18);
      		g.setColor(SystemColor.controlShadow);
    	  	g.drawLine(1,0,1,18);
  	    	g.setColor(SystemColor.controlDkShadow);
      		g.drawLine(0,0,0,18);
	        g.setColor(Color.red);
  		  }
    		else
	    	{
  	    	g.setColor(SystemColor.controlShadow);
    	  	g.drawLine(0,17,16,17);
      		g.setColor(SystemColor.controlDkShadow);
    	  	g.drawLine(0,18,16,18);
      		g.setColor(SystemColor.control);
    	  	g.drawLine(1,0,1,18);
	      	g.setColor(SystemColor.controlLtHighlight);
  	    	g.drawLine(0,0,0,18);
    	    g.setColor(Color.red);
		    }
      }
	    g.drawImage(myIcon,0,0,this);
    }
  }

	public void setMyIcon(java.awt.Image newMyIcon)
	{
		myIcon = newMyIcon;
    if(this.isShowing())
	    repaint();
	}

	public java.awt.Image getMyIcon()
	{
		return myIcon;
	}

	public void setDished(boolean newDished)
	{
		dished = newDished;
    if(!ClientUtils.isMS())
    	return;
    Graphics g=getGraphics();
    if(dished)
    {
    	g.setColor(SystemColor.control);
      g.drawLine(0,17,16,17);
    	g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(0,18,16,18);
      g.setColor(SystemColor.controlShadow);
      g.drawLine(1,0,1,18);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(0,0,0,18);
      g.setColor(Color.red);
    }
    else
    {
    	g.setColor(SystemColor.controlShadow);
      g.drawLine(0,17,16,17);
    	g.setColor(SystemColor.control);
      g.drawLine(1,0,1,18);
      g.setColor(SystemColor.controlDkShadow);
      g.drawLine(0,18,16,18);
      g.setColor(SystemColor.controlLtHighlight);
      g.drawLine(0,0,0,18);
      g.setColor(Color.red);
    }
	}

	public boolean isDished()
	{
		return dished;
	}
}