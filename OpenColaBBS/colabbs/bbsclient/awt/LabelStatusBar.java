
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient.awt;

import java.awt.*;

import colabbs.bbsclient.StatusBar;

public class LabelStatusBar extends StatusBar
{
  private Label myLabel=null;
  public LabelStatusBar(Label theLabel)
  {
    myLabel=theLabel;
    super.sysStatusBar=this;
  }
  public void implementSetMessage(String theMsg)
  {
    if(myLabel!=null)
      myLabel.setText(theMsg);
  }
  public String implementGetMessage()
  {
    if(myLabel!=null)
      return myLabel.getText();
    return "";
  }
}