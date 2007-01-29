
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

public class TrayIcon implements Runnable
{
	private java.awt.Image myIcon;
  private PopupMenu myMenu;

	public TrayIcon(Image theIcon)
	{
  	myIcon=theIcon;
    (new Thread(this)).start();
	}

	public TrayIcon()
	{
    (new Thread(this)).start();
	}

  public void add(PopupMenu theMenu)
  {
  	myMenu=theMenu;
  }

	public java.awt.Image getMyIcon()
	{
		return myIcon;
	}

	public void setMyIcon(java.awt.Image newMyIcon)
	{
		myIcon = newMyIcon;
	}

	public void run()
	{
		//TODO: implement this java.lang.Runnable method;
    try
    {
      IconWindow tmp=new IconWindow();

    	while(true)
      {
	    	Thread.sleep(100);
      	if(tmp!=null)
        	tmp.toFront();;
      }

/*      tmp=new IconWindow();
    	while(true)
      {
        tmp.setVisible(true);
	    	Thread.sleep(500);
       	tmp.setVisible(false);
      }*/
    }
    catch(InterruptedException e)
    {
    }
	}

  class IconWindow extends Window
  {
  	private boolean firstShow=true;

  	public IconWindow()
    {
    	super(new Frame());
	    this.setSize(16,16);
			this.setBackground(SystemColor.control);
	    this.add(myMenu);
			this.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
					this_mouseClicked(e);
				}

				public void mousePressed(MouseEvent e)
				{
					this_mouseClicked(e);
				}

				public void mouseReleased(MouseEvent e)
				{
					this_mouseClicked(e);
				}
			});
	    Toolkit myToolkit=this.getToolkit();
      this.setLocation(myToolkit.getScreenSize().width-75,myToolkit.getScreenSize().height-20);
      this.setVisible(true);

	  	/** This hack is to workaround a bug on Solaris where the windows does not really show
			 *  the first time
			 *  It causes a side effect of MS JVM reporting IllegalArumentException: null source
	  	 *  fairly frequently - also happens if you use HeavyWeight JPopup, ie JComboBox
		   */
		  if(firstShow)
 	  	{
				this.setVisible(false);
				this.setVisible(true);
				firstShow = false;
	  	}
    }

		private void this_mouseClicked(MouseEvent e)
		{
  		if(e.isPopupTrigger())
	    {
  	  	myMenu.show(this,0,0);
    	}
		}

	  public void paint(Graphics g)
  	{
  		if(myIcon!=null)
		  	g.drawImage(myIcon,0,0,this);
  	}
  }
}
