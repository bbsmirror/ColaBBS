
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.awt;


import colabbs.bbsclient.TabModule;
import colabbs.bbsclient.MultiModule;
import colabbs.bbsclient.ConnectionManager;
import java.awt.*;

public class ChatFunction extends MultiModule implements TabModule
{
	BorderLayout borderLayout1 = new BorderLayout();
	ChatPanel chatPanel1 = new ChatPanel();

	public ChatFunction(ConnectionManager theConnection)
	{
  	super(theConnection);
		try
		{
			jbInit();
	    myName="��ѫǵ���";
  	  chatPanel1.setMyConnection(theConnection);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getTabName()
	{
		//TODO: implement this colabbs.bbsclient.TabModule method;
    return "�����}����ѫ�";
	}

  public String getMyClassName()
  {
  	return getClass().getName();
  }

  public static MenuItem getFunctionItem()
  {
    return (new MenuItem("�}�s��ѫǵ���"));
  }

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		this.add(chatPanel1, BorderLayout.CENTER);
	}
}