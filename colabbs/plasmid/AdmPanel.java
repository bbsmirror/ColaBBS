
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.plasmid;

import java.awt.*;

import colabbs.ShutDown;
import java.awt.event.*;
/**
 * 本地端管理站務用
 */

public class AdmPanel extends Frame
{
  Button button1 = new Button();
  TextArea textArea1 = new TextArea();

  public AdmPanel()
  {
    try
    {
      jbInit();
      this.setSize(new Dimension(350,250));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    button1.setLabel("關站");
    button1.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    textArea1.setText("感謝您使用可樂電子佈告欄系統\n若您想要結束本程式關閉您的站臺，請按下方之按鈕");
    this.add(button1, BorderLayout.SOUTH);
    this.add(textArea1, BorderLayout.CENTER);
  }

  public static void startup()
  {
    (new AdmPanel()).setVisible(true);
  }

  void button1_actionPerformed(ActionEvent e)
  {
		(new ShutDown(0)).start();
  }
}
