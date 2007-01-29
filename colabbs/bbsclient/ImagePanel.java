
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient;

import java.awt.*;

public class ImagePanel extends Panel
{
  BorderLayout borderLayout1 = new BorderLayout();
  private java.awt.Image myImage=null;

  public ImagePanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public ImagePanel(Image theImage)
  {
    try
    {
      jbInit();
      myImage=theImage;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
  }

  public void setMyImage(java.awt.Image newMyImage)
  {
    myImage = newMyImage;
  }

  public java.awt.Image getMyImage()
  {
    return myImage;
  }

  public void paint(java.awt.Graphics g)
  {
    if(myImage!=null)
      g.drawImage(myImage,0,0,this);
  }
}

