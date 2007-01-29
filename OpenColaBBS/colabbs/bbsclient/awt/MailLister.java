
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:

package colabbs.bbsclient.awt;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.awt.event.*;
import java.text.*;
import java.util.Date;

import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.UniModule;
import colabbs.bbsclient.CmdItem;
import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.BBSTPDirItemCount;
import colabbs.bbstp.mail.BBSTPSendMail;
import colabbs.bbstp.mail.BBSTPMailSize;
import colabbs.bbstp.mail.BBSTPMailList;
import colabbs.bbstp.mail.BBSTPMailItem;
import colabbs.bbstp.mail.BBSTPMailCount;
import colabbs.bbstp.mail.BBSTPMailItemCount;
import colabbs.bbstp.mail.BBSTPSendMailStatus;

public class MailLister extends UniModule
{
//  private static DateFormatter1=new SimpleDateFormat ("EEE MMM dd kk':'mm");
  private static SimpleDateFormat DateFormatter1=new SimpleDateFormat();
  private int itemCount=0;
  private int mode=1;
  private int range=50;
  private Vector myMailList=new Vector();
  private Vector listedMail=new Vector();
//******
  List list1 = new List();
  Panel panel1 = new Panel();
  Button button1 = new Button();
  FlowLayout flowLayout1 = new FlowLayout();
  Button replyButton2 = new Button();
  Panel panel2 = new Panel();
  Label label1 = new Label();
  FlowLayout flowLayout2 = new FlowLayout();
  TextField textField1 = new TextField();
  Checkbox checkbox1 = new Checkbox();
  Checkbox checkbox2 = new Checkbox();
  CheckboxGroup checkboxGroup1 = new CheckboxGroup();
  Panel panel6 = new Panel();
  BorderLayout borderLayout2 = new BorderLayout();
  Panel panel7 = new Panel();
  Panel panel8 = new Panel();
  BorderLayout borderLayout3 = new BorderLayout();
  Panel panel9 = new Panel();
  Label label7 = new Label();
  Checkbox checkbox3 = new Checkbox();
  CheckboxGroup checkboxGroup2 = new CheckboxGroup();
  Checkbox checkbox4 = new Checkbox();
  Checkbox checkbox5 = new Checkbox();
  TextField textField7 = new TextField();
  Checkbox checkbox6 = new Checkbox();
  Panel panel10 = new Panel();
  GridLayout gridLayout1 = new GridLayout();
  Label label8 = new Label();
  Button button3 = new Button();
  Panel panel11 = new Panel();
  BorderLayout borderLayout4 = new BorderLayout();
  Panel panel12 = new Panel();
  Label label9 = new Label();
  Panel panel13 = new Panel();
  BorderLayout borderLayout5 = new BorderLayout();
  Button newMailButton = new Button();
	Panel mailList = new Panel();
	BorderLayout borderLayout6 = new BorderLayout();
	Panel mailViewer = new Panel();
	BorderLayout borderLayout7 = new BorderLayout();
	CardLayout cardLayout1 = new CardLayout();
	TextViewer myArticle = new TextViewer();
	Panel panel5 = new Panel();
	Button button5 = new Button();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel14 = new Panel();
	FlowLayout flowLayout3 = new FlowLayout();
	Label label2 = new Label();
	Button replyButton1 = new Button();
	Button button7 = new Button();
	Button button8 = new Button();
	Panel mailEditor = new Panel();
	SendMailPanel myMailEditor = new SendMailPanel();
	Panel panel15 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Button button9 = new Button();
	Button button10 = new Button();

  public MailLister(ConnectionManager theConnection)
  {
    super(theConnection);
    try
    {
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItem"),new CmdItem(this.getClass().getMethod("addMailItem",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItem")}),this));
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount"),new CmdItem(this.getClass().getMethod("setMailItemCount",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount")}),this));
      jbInit();
      myMailEditor.setMyConnection(theConnection);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    doList();
  }

  private void jbInit() throws Exception
  {
    this.setLayout(cardLayout1);
    button1.setLabel("讀此封信件");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
    panel1.setLayout(flowLayout1);
    replyButton2.setLabel("回此封信件");
		replyButton2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				replyButton2_actionPerformed(e);
			}
		});
    label1.setText("搜尋信件:");
    panel2.setLayout(flowLayout2);
    textField1.setColumns(14);
    textField1.addTextListener(new java.awt.event.TextListener()
    {

      public void textValueChanged(TextEvent e)
      {
        textField1_textValueChanged(e);
      }
    });
    checkbox1.setCheckboxGroup(checkboxGroup1);
    checkbox1.setLabel("寄件者");
    checkbox2.setCheckboxGroup(checkboxGroup1);
    checkbox2.setLabel("信件主題");
    checkboxGroup1.setSelectedCheckbox(checkbox1);
    panel6.setLayout(borderLayout2);
    panel8.setLayout(borderLayout3);
    label7.setText("列示方式:");
    checkbox3.setCheckboxGroup(checkboxGroup2);
    checkbox3.setLabel("未讀信件");
    checkbox3.setState(true);
    checkbox4.setCheckboxGroup(checkboxGroup2);
    checkbox4.setLabel("最新");
    checkbox4.setState(true);
    checkbox5.setCheckboxGroup(checkboxGroup2);
    checkbox5.setLabel("最舊");
    textField7.setColumns(5);
    textField7.setText("50");
    checkbox6.setCheckboxGroup(checkboxGroup2);
    checkbox6.setLabel("全部");
    panel10.setLayout(gridLayout1);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(0);
    label8.setText("封");
    button3.setLabel("更新");
    button3.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button3_actionPerformed(e);
      }
    });
    panel11.setLayout(borderLayout4);
    panel13.setLayout(borderLayout5);
    newMailButton.setLabel("寫新信件");
		newMailButton.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				newMailButton_actionPerformed(e);
			}
		});
    list1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				list1_actionPerformed(e);
			}
		});
		mailList.setLayout(borderLayout6);
		mailViewer.setLayout(borderLayout7);
		button5.setLabel("回信件列表");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		label9.setFont(new java.awt.Font("Monospaced", 0, 12));
		list1.setFont(new java.awt.Font("Monospaced", 0, 12));
		panel14.setLayout(flowLayout3);
		flowLayout3.setAlignment(FlowLayout.LEFT);
		flowLayout3.setHgap(0);
		flowLayout3.setVgap(0);
		panel14.setBackground(new java.awt.Color(157, 185, 200));
		label2.setFont(new java.awt.Font("Monospaced", 0, 12));
		label2.setText("未 日    期 寄       件       者 標      題");
		replyButton1.setLabel("回信");
		replyButton1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				replyButton1_actionPerformed(e);
			}
		});
		button7.setLabel("讀上一封");
		button7.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button7_actionPerformed(e);
			}
		});
		button8.setLabel("讀下一封");
		button8.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button8_actionPerformed(e);
			}
		});
		mailEditor.setLayout(borderLayout1);
		button9.setLabel("寄出");
		button9.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button9_actionPerformed(e);
			}
		});
		button10.setLabel("取消");
		button10.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button10_actionPerformed(e);
			}
		});
		this.add(mailList, "mailList");
		mailList.add(panel11, BorderLayout.CENTER);
		panel11.add(panel6, BorderLayout.CENTER);
		panel6.add(list1, BorderLayout.CENTER);
		panel6.add(panel7, BorderLayout.WEST);
		panel6.add(panel12, BorderLayout.EAST);
		mailList.add(panel8, BorderLayout.NORTH);
		panel8.add(panel2, BorderLayout.NORTH);
		panel2.add(label1, null);
		panel2.add(checkbox1, null);
		panel2.add(checkbox2, null);
		panel2.add(textField1, null);
		panel8.add(panel9, BorderLayout.CENTER);
		panel9.add(label7, null);
		panel9.add(checkbox3, null);
		panel9.add(checkbox6, null);
		panel9.add(panel10, null);
		panel10.add(checkbox5, null);
		panel10.add(checkbox4, null);
		panel9.add(textField7, null);
		panel9.add(label8, null);
		panel9.add(button3, null);
		panel8.add(panel13, BorderLayout.SOUTH);
		panel13.add(panel3, BorderLayout.WEST);
		panel13.add(panel4, BorderLayout.EAST);
		panel13.add(panel14, BorderLayout.CENTER);
		panel14.add(label2, null);
		panel14.add(label9, null);
		mailList.add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, null);
		panel1.add(replyButton2, null);
		panel1.add(newMailButton, null);
		this.add(mailViewer, "mailViewer");
		mailViewer.add(myArticle, BorderLayout.CENTER);
		mailViewer.add(panel5, BorderLayout.SOUTH);
		panel5.add(button5, null);
		panel5.add(button7, null);
		panel5.add(button8, null);
		panel5.add(replyButton1, null);
		this.add(mailEditor, "mailEditor");
		mailEditor.add(panel15, BorderLayout.SOUTH);
		panel15.add(button9, null);
		panel15.add(button10, null);
		mailEditor.add(myMailEditor, BorderLayout.CENTER);
  }

  public void doList()
  {
    try
    {
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItem"),new CmdItem(this.getClass().getMethod("addMailItem",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItem")}),this));
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount"),new CmdItem(this.getClass().getMethod("setMailItemCount",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount")}),this));
      myConnection.sendCmd(this,new BBSTPMailCount());
      initMode();
      myConnection.sendCmd(this,new BBSTPMailList(mode,range));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public void setMailItemCount(BBSTPMailItemCount pic)
  {
    itemCount=((BBSTPDirItemCount)pic).Count;
    if(itemCount>=0)
      label9.setText("(您信箱中共有 "+itemCount+" 封信件)");
    else
      label9.setText("(您信箱目前沒有信件)");
    doLayout();
  }

  public void addMailItem(BBSTPMailItem pi)
  {
    myMailList.addElement(pi);
    listedMail.addElement(pi);
    list1.add((pi.Read?"   ":"ˇ ")+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+" "+ClientUtils.byte2String(ClientUtils.Cut(pi.sender,20))+" "+ClientUtils.byte2String(pi.title));
  }

  void textField1_textValueChanged(TextEvent e)
  {
    String check=textField1.getText();

    if(checkbox1.getState())
    {
      int max=myMailList.size();
	  	//for IE!!
  		list1.deselect(list1.getSelectedIndex());
    	//
      listedMail.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPMailItem pi=(BBSTPMailItem)myMailList.elementAt(i);
        String me=ClientUtils.byte2String(pi.sender);
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
            listedMail.addElement(pi);
				    list1.add((pi.Read?"   ":"ˇ ")+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+" "+ClientUtils.byte2String(ClientUtils.Cut(pi.sender,20))+" "+ClientUtils.byte2String(pi.title));
          }
        }
      }
    }
    else
    {
      int max=myMailList.size();
	  	//for IE!!
  		list1.deselect(list1.getSelectedIndex());
    	//
      listedMail.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPMailItem pi=(BBSTPMailItem)myMailList.elementAt(i);
        if(ClientUtils.byte2String(pi.title).indexOf(check)>=0)
        {
          listedMail.addElement(pi);
			    list1.add((pi.Read?"   ":"ˇ ")+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+" "+ClientUtils.byte2String(ClientUtils.Cut(pi.sender,20))+" "+ClientUtils.byte2String(pi.title));
        }
      }
    }
  }

  public void initMode()
  {
    try
    {
      if(checkbox3.getState())
      {
        mode=4;
      }
      else if(checkbox4.getState())
      {
        mode=1;
        range=Integer.parseInt(textField7.getText());
      }
      else if(checkbox5.getState())
      {
        mode=2;
        range=Integer.parseInt(textField7.getText());
      }
      else if(checkbox6.getState())
      {
        mode=3;
      }
    }
    catch(NumberFormatException e){}
  }

/*  void list1_itemStateChanged(ItemEvent e)
  {
    BBSTPMailItem pi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());

    if(pi!=null)
    {
      textField2.setText(pi.sender);
      textField3.setText(pi.title);
      textField4.setText(DateFormatter1.format(new Date(pi.filetime)));
      textField5.setText(pi.Mark?"是":"否");
      textField6.setText(pi.Read?"是":"否");
    }
  }*/

  void button3_actionPerformed(ActionEvent e)
  {
  	//for IE!!
//    list1.deselect(list1.getSelectedIndex());
    //
    myMailList.removeAllElements();
    listedMail.removeAllElements();
    list1.removeAll();
    doList();
  }

	void list1_actionPerformed(ActionEvent e)
	{
    ((CardLayout)getLayout()).show(this,"mailViewer");
    BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
    myArticle.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi.index);
	}

	void button5_actionPerformed(ActionEvent e)
	{
    ((CardLayout)getLayout()).show(this,"mailList");
	}

	void button1_actionPerformed(ActionEvent e)
	{
  	if(list1.getSelectedIndex()!=-1)
    {
	    ((CardLayout)getLayout()).show(this,"mailViewer");
  	  BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
    	myArticle.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi.index);
    }
	}

	void button9_actionPerformed(ActionEvent e)
	{
  	doSendMail();
    ((CardLayout)getLayout()).show(this,"mailList");
	}

	void replyButton1_actionPerformed(ActionEvent e) //回信
	{
  	if(list1.getSelectedIndex()!=-1)
    {
  	  BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
    	myMailEditor.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi);
      myMailEditor.setReceiver(ClientUtils.byte2String(mi.sender));
      myMailEditor.setTitle(ClientUtils.byte2String(mi.title));
      myMailEditor.setSender(myConnection.getUserName());
      myMailEditor.setSignatureNumber(myConnection.getSignatureNumber());
	    ((CardLayout)getLayout()).show(this,"mailEditor");
    }
	}

	void replyButton2_actionPerformed(ActionEvent e) //回此封信
	{
  	if(list1.getSelectedIndex()!=-1)
    {
  	  BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
      //add line start mark each line for data....
      String orgstr=myArticle.readData(),newstr="",bufstr;
      BufferedReader br=new BufferedReader(new CharArrayReader(orgstr.toCharArray()));
      try
      {
	      while((bufstr=br.readLine())!=null)
  	    {
//        	if(bufstr.indexOf("作者")!=-1)
//          	sender=bufstr.substring(bufstr.indexOf("作者"));
//    	    newstr=newstr+bufstr;
      		if(bufstr.length()==0)
        		break;
	      }
  	    newstr=newstr+"※ 在 "+ClientUtils.byte2String(mi.sender)+" 的來信中提到: \n";
    	  while((bufstr=br.readLine())!=null)
      	  newstr=newstr+": "+bufstr+"\n";
	    	myMailEditor.setMailBody(newstr);
  	    //
    	  myMailEditor.setReceiver(ClientUtils.byte2String(mi.sender));
      	myMailEditor.setTitle(ClientUtils.byte2String(mi.title));
	      myMailEditor.setSender(myConnection.getUserName());
        myMailEditor.setSignatureNumber(myConnection.getSignatureNumber());
		    ((CardLayout)getLayout()).show(this,"mailEditor");
      }
      catch(IOException e1){}
      finally
      {
      	try
        {
	      	if(br!=null)
  	      	br.close();
        }
        catch(IOException e1){}
      }
    }
	}

	void newMailButton_actionPerformed(ActionEvent e) //寫新信
	{
  	myMailEditor.setReceiver("");
    myMailEditor.setTitle("");
    myMailEditor.setMailBody("");
    myMailEditor.setSignatureNumber(myConnection.getSignatureNumber());
  	myMailEditor.setSender(myConnection.getUserName());
    ((CardLayout)getLayout()).show(this,"mailEditor");
	}

  private void doSendMail()
  {
  	try
    {
    	Socket mySocket=new Socket(myConnection.getAddress(),5254);
      OutputStream os=mySocket.getOutputStream();
      ObjectOutputStream oos=new ObjectOutputStream(os);
      InputStream is=mySocket.getInputStream();
      ObjectInputStream ois=new ObjectInputStream(is);

      oos.writeObject(new Login(myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord()));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
	    	byte[] mailBody=ClientUtils.string2ByteArray(myMailEditor.getMailBody());
        String receiver=myMailEditor.getReceiver();
        if(receiver.indexOf(' ')!=-1)
        	receiver=receiver.substring(0,receiver.indexOf(' '));
      	oos.writeObject(new BBSTPSendMail(receiver,ClientUtils.string2Byte(myMailEditor.getTitle()),myMailEditor.getSignature()));
      	oos.writeObject(new BBSTPMailSize(mailBody.length));
        os.write(mailBody);
        System.out.println("File size : "+mailBody.length);
	      BBSTPSendMailStatus sms=(BBSTPSendMailStatus)ois.readObject();
        switch(sms.status)
        {
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

	void button10_actionPerformed(ActionEvent e)
	{
    ((CardLayout)getLayout()).show(this,"mailList");
	}

	void button7_actionPerformed(ActionEvent e) //上一封
	{
		if(list1.getSelectedIndex()>0)
    {
    	list1.select(list1.getSelectedIndex()-1);
	    BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
  	  myArticle.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi.index);
    }
    else
	    ((CardLayout)getLayout()).show(this,"mailList");
	}

	void button8_actionPerformed(ActionEvent e) //下一封
	{
		if(list1.getSelectedIndex()!=-1&&list1.getSelectedIndex()<list1.getItemCount()-1)
    {
    	list1.select(list1.getSelectedIndex()+1);
	    BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
  	  myArticle.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi.index);
    }
    else
	    ((CardLayout)getLayout()).show(this,"mailList");
	}
}
