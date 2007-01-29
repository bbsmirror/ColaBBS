
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.awt;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.board.BBSTPPostItem;
import colabbs.bbstp.board.BBSTPReadPost;
import colabbs.bbstp.board.BBSTPPostSize;
import colabbs.bbsclient.ClientUtils;

public class SendPostPanel extends Panel
{
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel1 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Panel panel6 = new Panel();
	Panel panel7 = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	GridLayout gridLayout2 = new GridLayout();
	Label label1 = new Label();
	TextField textBoardName = new TextField();
	Panel panel8 = new Panel();
	Panel panel9 = new Panel();
	Panel panel10 = new Panel();
	BorderLayout borderLayout4 = new BorderLayout();
	TextArea textArticle = new TextArea();
	Label label2 = new Label();
	Label label3 = new Label();
	TextField textTitle = new TextField();
	FlowLayout flowLayout2 = new FlowLayout();
	Panel panel12 = new Panel();
	Choice choice1 = new Choice();
	Label label4 = new Label();
	Panel panel11 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	CheckboxGroup checkboxGroup1 = new CheckboxGroup();
	Checkbox checkbox1 = new Checkbox();
	Checkbox checkbox2 = new Checkbox();
	Label label5 = new Label();
	CheckboxGroup checkboxGroup2 = new CheckboxGroup();
	Checkbox checkbox3 = new Checkbox();
	Checkbox checkbox4 = new Checkbox();
//	private String boardName;
//	private String title;
	private int signature;
	private int signatureNumber;
	private boolean localSave;
	private boolean anonymous;
//	private boolean anonymousBoard;

	public SendPostPanel()
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

  public void ReadPost(String Addr,int theType,String UserName,String PassWord,String BoardName,BBSTPPostItem pi)
  {
  	try
    {
    	textArticle.setText("資料傳輸中，請稍候....");
    	Socket mySocket=new Socket(Addr,5254);
      OutputStream os=mySocket.getOutputStream();
      ObjectOutputStream oos=new ObjectOutputStream(os);
      InputStream is=mySocket.getInputStream();
      ObjectInputStream ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadPost(BoardName,pi.index));
				BBSTPPostSize ps=(BBSTPPostSize)ois.readObject();
        byte buffer[]=new byte[ps.size];
        int readed=is.read(buffer);
        System.out.println("File size : "+buffer.length+","+readed+" bytes readed.");
//        for(int i=0;i<buffer.length;i++)
//        	System.out.println((int)buffer[i]);
        String data=new String(buffer,ClientUtils.myEncoding);
	      //add line start mark each line for data....
  	    String newstr="",bufstr;
    	  BufferedReader br=new BufferedReader(new CharArrayReader(data.toCharArray()));
        try
        {
	      	while((bufstr=br.readLine())!=null)
		      {
//	        	if(bufstr.indexOf("作者")!=-1)
//  	        	sender=bufstr.substring(bufstr.indexOf("作者"));
//  		      newstr=newstr+bufstr;
    		  	if(bufstr.length()==0)
      		  	break;
		      }
  		    newstr=newstr+"※ 在 "+pi.poster+" 的大作中提到: \n";
    		  while((bufstr=br.readLine())!=null)
      		  newstr=newstr+": "+bufstr+"\n";
	      	//
//	        textMailBody.setText(ClientUtils.CutAnsiCode(newstr));
	        textArticle.setText(ClientUtils.CutAnsiCode(newstr));
        }
	      finally
  	    {
    	  	if(br!=null)
      	  	br.close();
	      }
      }
      ois.close();
      oos.close();
      mySocket.close();
      System.gc();
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
  }

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		panel1.setLayout(borderLayout2);
		panel3.setLayout(borderLayout3);
		panel6.setLayout(gridLayout1);
		gridLayout1.setRows(4);
		panel7.setLayout(gridLayout2);
		gridLayout2.setRows(4);
		label1.setText("討論區");
		panel8.setLayout(borderLayout4);
		textBoardName.setEditable(false);
		label2.setText("標題");
		label3.setText("簽名檔");
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout2.setVgap(0);
		panel12.setLayout(flowLayout2);
		label4.setText("存檔模式");
		panel11.setLayout(flowLayout1);
		checkbox1.setCheckboxGroup(checkboxGroup1);
		checkbox1.setLabel("站內");
		checkbox1.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				checkbox1_itemStateChanged(e);
			}
		});
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		checkbox2.setCheckboxGroup(checkboxGroup1);
		checkbox2.setLabel("轉信");
		checkbox2.setState(true);
		checkbox2.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				checkbox2_itemStateChanged(e);
			}
		});
		label5.setText("暱名");
		checkbox3.setCheckboxGroup(checkboxGroup2);
		checkbox3.setLabel("是");
		checkbox3.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				checkbox3_itemStateChanged(e);
			}
		});
		checkbox4.setCheckboxGroup(checkboxGroup2);
		checkbox4.setLabel("否");
		checkbox4.setState(true);
		checkbox4.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				checkbox4_itemStateChanged(e);
			}
		});
		choice1.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				choice1_itemStateChanged(e);
			}
		});
		this.add(panel1, BorderLayout.NORTH);
		panel1.add(panel2, BorderLayout.WEST);
		panel1.add(panel3, BorderLayout.CENTER);
		panel3.add(panel6, BorderLayout.WEST);
		panel6.add(label1, null);
		panel6.add(label2, null);
		panel6.add(label3, null);
		panel6.add(label4, null);
		panel3.add(panel7, BorderLayout.CENTER);
		panel7.add(textBoardName, null);
		panel7.add(textTitle, null);
		panel7.add(panel12, null);
		panel12.add(choice1, null);
		panel7.add(panel11, null);
		panel11.add(checkbox1, null);
		panel11.add(checkbox2, null);
		panel11.add(label5, null);
		panel11.add(checkbox3, null);
		panel11.add(checkbox4, null);
		panel1.add(panel4, BorderLayout.EAST);
		panel1.add(panel5, BorderLayout.NORTH);
		this.add(panel8, BorderLayout.CENTER);
		panel8.add(panel10, BorderLayout.WEST);
		panel8.add(panel9, BorderLayout.EAST);
		panel8.add(textArticle, BorderLayout.CENTER);
	}

	public void setArticle(String newArticle)
	{
		textArticle.setText(newArticle);
	}

	public String getArticle()
	{
		return textArticle.getText();
	}

	public void setBoardName(String newBoardName)
	{
		textBoardName.setText(newBoardName);
	}

	public void setTitle(String newTitle)
	{
		textTitle.setText(newTitle);
	}

	public String getTitle()
	{
		return textTitle.getText();
	}

	public void setSignature(int newSignature)
	{
		signature = newSignature;
	}

	public int getSignature()
	{
		return signature;
	}

	public void setSignatureNumber(int newSignatureNumber)
	{
  	choice1.removeAll();
		signatureNumber = newSignatureNumber;
    choice1.add("不使用任何簽名檔");
    for(int i=1;i<=signatureNumber;i++)
	    choice1.add("第 "+i+" 個");
	}

	public void setLocalSave(boolean newLocalSave)
	{
		localSave = newLocalSave;
		checkbox1.setState(localSave);
		checkbox2.setState(!localSave);
	}

	public boolean isLocalSave()
	{
		return localSave;
	}

	public void setAnonymous(boolean newAnonymous)
	{
		anonymous = newAnonymous;
		checkbox3.setState(!anonymous);
		checkbox4.setState(anonymous);
	}

	public boolean isAnonymous()
	{
		return anonymous;
	}

	void checkbox1_itemStateChanged(ItemEvent e)
	{
		localSave=checkbox1.getState();
	}

	void checkbox2_itemStateChanged(ItemEvent e)
	{
		localSave=checkbox1.getState();
	}

	void checkbox3_itemStateChanged(ItemEvent e)
	{
		anonymous = checkbox3.getState();
	}

	void checkbox4_itemStateChanged(ItemEvent e)
	{
		anonymous = checkbox3.getState();
	}

	void choice1_itemStateChanged(ItemEvent e)
	{
  	if(choice1.getSelectedIndex()!=-1)
			signature = choice1.getSelectedIndex();
	}

	public void setAnonymousBoard(boolean newAnonymousBoard)
	{
//		anonymousBoard = newAnonymousBoard;
    if(newAnonymousBoard)
    {
			label5.setVisible(true);
      checkbox3.setVisible(true);
      checkbox4.setVisible(true);
    }
    else
    {
			label5.setVisible(false);
      checkbox3.setVisible(false);
      checkbox4.setVisible(false);
    }
	}
}