package colabbs.bbsclient.swing;

import java.awt.*;
import javax.swing.*;

import colabbs.bbsclient.StatusBar;

public class SwingLabelStatusBar extends StatusBar
{
  private JLabel myLabel=null;
  public SwingLabelStatusBar(JLabel theLabel)
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
