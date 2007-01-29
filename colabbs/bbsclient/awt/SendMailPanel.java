
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
import colabbs.bbstp.mail.BBSTPMailItem;
import colabbs.bbstp.mail.BBSTPReadMail;
import colabbs.bbstp.mail.BBSTPMailSize;
import colabbs.bbsclient.ClientUtils;

public class SendMailPanel extends Panel
{
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel1 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Label label1 = new Label();
	Panel panel4 = new Panel();
	TextField textSender = new TextField();
	Panel panel5 = new Panel();
	Panel panel6 = new Panel();
	Panel panel7 = new Panel();
	Panel panel8 = new Panel();
	TextArea textMailBody = new TextArea();
	Panel panel10 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	BorderLayout borderLayout4 = new BorderLayout();
	Panel panel11 = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	GridLayout gridLayout2 = new GridLayout();
	Label label2 = new Label();
	TextField textReceiver = new TextField();
	Label label3 = new Label();
	TextField textTitle = new TextField();
	Label label4 = new Label();
	Panel panel12 = new Panel();
	FlowLayout flowLayout2 = new FlowLayout();
	Choice choice1 = new Choice();
	private int signature;
	private colabbs.bbsclient.ConnectionManager myConnection;
	private int signatureNumber;
//	private String receiver;
//	private String title;
//	private String mailBody;

	public SendMailPanel()
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

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		panel1.setLayout(borderLayout2);
		panel2.setLayout(borderLayout3);
		label1.setText("寄件者");
		textSender.setName("textSender");
		textSender.setEditable(false);
		textSender.setColumns(40);
		panel10.setLayout(flowLayout1);
		panel8.setLayout(borderLayout4);
		panel3.setLayout(gridLayout1);
		panel4.setLayout(gridLayout2);
		gridLayout1.setRows(4);
		gridLayout2.setRows(4);
		label2.setText("收件者");
		label3.setText("標題");
		label4.setText("簽名檔");
		panel12.setLayout(flowLayout2);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout2.setVgap(0);
		textReceiver.setName("textReceiver");
		textTitle.setName("textTitle");
		choice1.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				choice1_itemStateChanged(e);
			}
		});
		this.add(panel1, BorderLayout.NORTH);
		panel1.add(panel2, BorderLayout.CENTER);
		panel2.add(panel3, BorderLayout.WEST);
		panel3.add(label1, null);
		panel3.add(label2, null);
		panel3.add(label3, null);
		panel3.add(label4, null);
		panel2.add(panel4, BorderLayout.CENTER);
		panel4.add(textSender, null);
		panel4.add(textReceiver, null);
		panel4.add(textTitle, null);
		panel4.add(panel12, null);
		panel12.add(choice1, null);
		panel1.add(panel5, BorderLayout.EAST);
		panel1.add(panel6, BorderLayout.WEST);
		panel1.add(panel7, BorderLayout.NORTH);
		this.add(panel8, BorderLayout.CENTER);
		panel8.add(panel10, BorderLayout.WEST);
		panel8.add(textMailBody, BorderLayout.CENTER);
		panel8.add(panel11, BorderLayout.EAST);
	}

	public void setSignature(int newSignature)
	{
		signature = newSignature;
	}

	public int getSignature()
	{
		return signature;
	}

	public void setSender(String newSender)
	{
		textSender.setText(newSender);
	}

	public void setReceiver(String newReceiver)
	{
		textReceiver.setText(newReceiver);
	}

	public String getReceiver()
	{
		return textReceiver.getText();
	}

	public void setTitle(String newTitle)
	{
    textTitle.setText(newTitle);
	}

	public String getTitle()
	{
		return textTitle.getText();
	}

	public void setMailBody(String newMailBody)
	{
    textMailBody.setText(newMailBody);
	}

	public String getMailBody()
	{
		return textMailBody.getText();
	}

	public void setMyConnection(colabbs.bbsclient.ConnectionManager newMyConnection)
	{
		myConnection = newMyConnection;
	}

	public colabbs.bbsclient.ConnectionManager getMyConnection()
	{
		return myConnection;
	}

	public void setSignatureNumber(int newSignatureNumber)
	{
  	choice1.removeAll();
		signatureNumber = newSignatureNumber;
    choice1.add("不使用任何簽名檔");
    for(int i=1;i<=signatureNumber;i++)
	    choice1.add("第 "+i+" 個");
	}

  public void ReadMail(String Addr,int theType,String UserName,String PassWord,BBSTPMailItem mi)
  {
  	try
    {
    	textMailBody.setText("資料傳輸中，請稍候....");
    	Socket mySocket=new Socket(Addr,5254);
      OutputStream os=mySocket.getOutputStream();
      ObjectOutputStream oos=new ObjectOutputStream(os);
      InputStream is=mySocket.getInputStream();
      ObjectInputStream ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadMail(mi.index));
				BBSTPMailSize ms=(BBSTPMailSize)ois.readObject();
        byte buffer[]=new byte[ms.size];
        int readed=is.read(buffer);
        System.out.println("File size : "+buffer.length+","+readed+" bytes readed.");
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
  		    newstr=newstr+"※ 在 "+ClientUtils.byte2String(mi.sender)+" 的來信中提到: \n";
    		  while((bufstr=br.readLine())!=null)
      		  newstr=newstr+": "+bufstr+"\n";
	      	//
	        textMailBody.setText(ClientUtils.CutAnsiCode(newstr));
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

	void choice1_itemStateChanged(ItemEvent e)
	{
  	if(choice1.getSelectedIndex()!=-1)
			signature = choice1.getSelectedIndex();
	}
}
