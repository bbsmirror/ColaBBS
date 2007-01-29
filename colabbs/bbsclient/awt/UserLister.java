
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.bbsclient.awt;

import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.BBSTPDirItemCount;
import colabbs.bbstp.user.BBSTPChangeState;
import colabbs.bbstp.user.BBSTPUserCount;
import colabbs.bbstp.user.BBSTPUserList;
import colabbs.bbstp.user.BBSTPUserItemCount;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbstp.user.BBSTPRemoveUserItem;
import colabbs.bbstp.user.BBSTPTalkRequest;
import colabbs.bbstp.user.BBSTPTalkCheck;
import colabbs.bbstp.user.BBSTPTalkReply;
import colabbs.bbstp.user.BBSTPTalkRequestAbort;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import colabbs.bbsclient.*;
import java.awt.event.*;

public class UserLister extends UniModule implements OnlineUserListener, Runnable
{
	private int requestTID=-1;
	private Socket mySocket=null;
	private OutputStream os=null;
  private ObjectOutputStream oos=null;
  private InputStream is=null;
  private ObjectInputStream ois=null;
  private int itemCount=0;
  private int mode=1;
  private Vector listedUser=new Vector();
//******
	CardLayout cardLayout1 = new CardLayout();
	Panel userListPanel = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel1 = new Panel();
	Button button1 = new Button();
	Button button2 = new Button();
	Button button3 = new Button();
	Panel panel2 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Label label1 = new Label();
	CheckboxGroup checkboxGroup1 = new CheckboxGroup();
	Checkbox checkbox1 = new Checkbox();
	Checkbox checkbox2 = new Checkbox();
	TextField textField1 = new TextField();
	Panel panel3 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	java.awt.List list1 = new java.awt.List();
	Panel panel6 = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	Label label2 = new Label();
	Panel panel7 = new Panel();
	Panel panel8 = new Panel();
	FlowLayout flowLayout2 = new FlowLayout();
	Label label3 = new Label();
	Panel talkRequestPanel = new Panel();
	Panel talkReplyPanel = new Panel();
	Panel talkPanel = new Panel();
	BorderLayout borderLayout4 = new BorderLayout();
	TalkPanel talkPanel1 = new TalkPanel();
	Panel panel9 = new Panel();
	Button button4 = new Button();
	BorderLayout borderLayout5 = new BorderLayout();
	Panel panel10 = new Panel();
	Button button5 = new Button();
	Panel panel11 = new Panel();
	Label labelPaging = new Label();
	Panel panel12 = new Panel();
	BorderLayout borderLayout6 = new BorderLayout();
	Button button6 = new Button();
	Panel panel13 = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	Label labelAsk = new Label();
	Panel panelAsk = new Panel();
	Panel panelAskChoice = new Panel();
	Choice choice1 = new Choice();
	GridLayout gridLayout2 = new GridLayout();
	Panel panelPaging = new Panel();
	Panel panelReMsg = new Panel();
	Label labelReMsg = new Label();

	public UserLister(ConnectionManager theConnection)
	{
    super(theConnection);
    synchronized(theConnection)
    {
    	OnlineUser myOnlineUsers=(OnlineUser)myConnection.getData("UserLister_myOnlineUsers");
    	try
      {
      	jbInit();
        choice1.add("沒問題，讓我們聊天吧！");
        choice1.add("抱歉，我現在很忙，不能跟你聊。");
        choice1.add("我現在很煩，不想跟別人聊天。");
        choice1.add("我有急事，我等一下再 Call 你。");
        choice1.add("請不要再 Page，我不想跟你聊。");
        choice1.add("我要離開了，下次在聊吧。");
        choice1.add("請寄一封信給我，我現在沒空。");
        myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPTalkCheck"),new CmdItem(getClass().getMethod("doTalkCheck",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPTalkCheck")}),this));
//        myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPTalkReply"),new CmdItem(getClass().getMethod("doTalkReply",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPTalkReply")}),this));
	  	  if(myOnlineUsers==null)
  	  	{
	  	  	myOnlineUsers=new OnlineUser(myConnection);
  	  	  myConnection.setData("UserLister_myOnlineUsers",myOnlineUsers);
	    	  myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPAddUserItem"),new CmdItem(myOnlineUsers.getClass().getMethod("addOnlineUser",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPAddUserItem")}),myOnlineUsers));
  	    	myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPRemoveUserItem"),new CmdItem(myOnlineUsers.getClass().getMethod("removeOnlineUser",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPRemoveUserItem")}),myOnlineUsers));
  	    	myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPChangeState"),new CmdItem(myOnlineUsers.getClass().getMethod("changeUserState",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPChangeState")}),myOnlineUsers));
	  	    myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPUserItemCount"),new CmdItem(myOnlineUsers.getClass().getMethod("setUserItemCount",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPUserItemCount")}),myOnlineUsers));
//    		myConnection.sendCmd(this,new BBSTPUserCount());
			    myConnection.sendCmd(this,new BBSTPUserList());
				}
        else
        {
			  	listedUser=myOnlineUsers.getOnlineUsers();
	        int max=listedUser.size();
  	      for(int i=0;i<max;i++)
    	    {
          	BBSTPAddUserItem aui=(BBSTPAddUserItem)listedUser.elementAt(i);
				    list1.add(getItemString(aui));
//				    list1.add(aui.userID+"("+ClientUtils.byte2String(aui.userNick)+")");
	        }
        }
  	  }
      catch(Exception e)
      {
      	e.printStackTrace();
      }
      myOnlineUsers.addOnlineUserListener(this);
    }
	}

  public void finalize()
  {
    synchronized(myConnection)
    {
	  	OnlineUser myOnlineUsers=(OnlineUser)myConnection.getData("UserLister_myOnlineUsers");
  	  if(myOnlineUsers!=null)
    	{
      	myOnlineUsers.removeOnlineUserListener(this);
      }
    }
  }

	private void jbInit() throws Exception
	{
		this.setLayout(cardLayout1);
		userListPanel.setLayout(borderLayout1);
		button1.setLabel("查詢使用者");
		button2.setLabel("一對一聊天");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		button3.setLabel("傳訊息");
		button3.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button3_actionPerformed(e);
			}
		});
		panel2.setLayout(flowLayout1);
		label1.setText("尋找使用者");
		checkbox1.setCheckboxGroup(checkboxGroup1);
		checkbox1.setLabel("帳號");
		checkbox2.setCheckboxGroup(checkboxGroup1);
		checkbox2.setLabel("暱稱");
		textField1.setColumns(20);
		panel3.setLayout(borderLayout2);
		panel6.setLayout(borderLayout3);
		panel8.setLayout(flowLayout2);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout2.setHgap(0);
		flowLayout2.setVgap(0);
		label2.setBackground(new java.awt.Color(157, 185, 200));
		label2.setFont(new java.awt.Font("Monospaced", 0, 12));
		label2.setText("使用者代號    使用者暱稱        聊  訊  使用者來源");
		list1.setFont(new java.awt.Font("Monospaced", 0, 12));
		talkPanel.setLayout(borderLayout4);
		button4.setLabel("結束聊天");
		button4.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button4_actionPerformed(e);
			}
		});
		talkRequestPanel.setLayout(borderLayout5);
		button5.setLabel("取消");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		labelPaging.setText("呼叫中，請稍候....");
		talkReplyPanel.setLayout(borderLayout6);
		button6.setLabel("確定");
		button6.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button6_actionPerformed(e);
			}
		});
		panel13.setLayout(gridLayout1);
		gridLayout1.setRows(2);
		labelAsk.setText("您要跟 聊天嗎?");
		panel11.setLayout(gridLayout2);
		gridLayout2.setRows(2);
		talkPanel1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				talkPanel1_actionPerformed(e);
			}
		});
		this.add(userListPanel, "userListPanel");
		userListPanel.add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, null);
		panel1.add(button2, null);
		panel1.add(button3, null);
		userListPanel.add(panel2, BorderLayout.NORTH);
		panel2.add(label1, null);
		panel2.add(checkbox1, null);
		panel2.add(checkbox2, null);
		panel2.add(textField1, null);
		userListPanel.add(panel3, BorderLayout.CENTER);
		panel3.add(panel4, BorderLayout.WEST);
		panel3.add(panel5, BorderLayout.EAST);
		panel3.add(list1, BorderLayout.CENTER);
		panel3.add(panel6, BorderLayout.NORTH);
		panel6.add(panel7, BorderLayout.WEST);
		panel6.add(panel8, BorderLayout.CENTER);
		panel8.add(label2, null);
		panel8.add(label3, null);
		this.add(talkRequestPanel, "talkRequestPanel");
		talkRequestPanel.add(panel10, BorderLayout.SOUTH);
		panel10.add(button5, null);
		talkRequestPanel.add(panel11, BorderLayout.CENTER);
		panel11.add(panelPaging, null);
		panelPaging.add(labelPaging, null);
		panel11.add(panelReMsg, null);
		panelReMsg.add(labelReMsg, null);
		this.add(talkReplyPanel, "talkReplyPanel");
		talkReplyPanel.add(panel12, BorderLayout.SOUTH);
		panel12.add(button6, null);
		talkReplyPanel.add(panel13, BorderLayout.CENTER);
		panel13.add(panelAsk, null);
		panelAsk.add(labelAsk, null);
		panel13.add(panelAskChoice, null);
		panelAskChoice.add(choice1, null);
		this.add(talkPanel, "talkPanel");
		talkPanel.add(talkPanel1, BorderLayout.CENTER);
		talkPanel.add(panel9, BorderLayout.SOUTH);
		panel9.add(button4, null);
	}

//auto refresh should not use this method!!
/*  public synchronized void doList()
  {
//	  myUserList.removeAllElements();
  	listedUser.removeAllElements();
    list1.removeAll();
    try
    {
      initMode();
//      myConnection.sendCmd(this,new BBSTPUserCount(mode));
      myConnection.sendCmd(this,new BBSTPUserList(mode));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }*/

  private String getItemString(BBSTPAddUserItem theItem)
  {
    String userPage=null,userMsg=null;
    switch(theItem.userPageMode)
    {
    	case 1:
      	userPage="友";
      	break;
      case 2:
      	userPage="關";
      	break;
    	default:
      	userPage="開";
        break;
    }
    switch(theItem.userMsgMode)
    {
    	case 1:
      	userMsg="友";
      	break;
      case 2:
      	userMsg="關";
      	break;
    	default:
      	userMsg="開";
        break;
    }
    return(ClientUtils.Cut(theItem.userID,13)+" "+ClientUtils.byte2String(ClientUtils.Cut(theItem.userNick,17))+" "+userPage+"  "+userMsg+"  "+ClientUtils.byte2String(ClientUtils.Cut(theItem.userHome,17)));
  }

  private void AddItem(BBSTPAddUserItem theItem)
  {
  	listedUser.addElement(theItem);
		list1.add(getItemString(theItem));
//    list1.add(theItem.userID+"("+ClientUtils.byte2String(theItem.userNick)+")");
  }

  private void RemoveItem(BBSTPRemoveUserItem theItem)
  {
    synchronized(myConnection)
    {
	  	OnlineUser myOnlineUsers=(OnlineUser)myConnection.getData("UserLister_myOnlineUsers");
  	  if(myOnlineUsers!=null)
    	{
      	BBSTPAddUserItem tmp=myOnlineUsers.getUserItem(theItem.userID);
        if(tmp!=null)
        {
		  		listedUser.removeElement(tmp);
  		  	list1.remove(getItemString(tmp));
//  		  	list1.remove(tmp.userID+"("+ClientUtils.byte2String(tmp.userNick)+")");
        }
      }
    }
  }

	public void AddOnlineUser(OnlineUserEvent e)
	{
  	AddItem((BBSTPAddUserItem)e.getCmdItem());
	}

	public void RemoveOnlineUser(OnlineUserEvent e)
	{
  	RemoveItem((BBSTPRemoveUserItem)e.getCmdItem());
	}

	public void UserStateChanged(OnlineUserEvent e)
	{
//  	AddItem(((BBSTPAddUserItem)e.getCmdItem()).userID);
	}

	void button2_actionPerformed(ActionEvent e)
	{
  	if(list1.getSelectedIndex()!=-1)
    {
      (new Thread(this)).start();
    }
	}

  public void doTalkCheck(BBSTPTalkCheck tc)
  {
  	if(tc.talkMode!=0)
    {
      myConnection.sendCmd(this,new BBSTPTalkReply(tc.tid,-1,""));
      return;
    }
    requestTID=tc.tid;
    labelAsk.setText("你要跟 "+tc.UserID+" 聊天嗎?");
    panelAsk.doLayout();
    talkPanel1.labelStatus.setText(myConnection.getUserName()+" VS. "+tc.UserID);
    ((CardLayout)getLayout()).show(this,"talkReplyPanel");
  }

	void button5_actionPerformed(ActionEvent e)
	{
	 	try
    {
    	if(ois!=null)
	      ois.close();
      if(oos!=null)
	   	  oos.close();
      if(is!=null)
      	is.close();
      if(os!=null)
      	os.close();
      if(mySocket!=null)
	      mySocket.close();
	  }
    catch(Exception e1)
   	{
   		e1.printStackTrace();
	  }
  	((CardLayout)getLayout()).show(this,"userListPanel");
	}

	public void run()
	{
		//TODO: implement this java.lang.Runnable method;
    	BBSTPAddUserItem theItem=(BBSTPAddUserItem)listedUser.elementAt(list1.getSelectedIndex());
			labelPaging.setText("呼叫 "+theItem.userID+" 中，請稍候....");
      panelPaging.doLayout();
  	  ((CardLayout)getLayout()).show(this,"talkRequestPanel");
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
	      	oos.writeObject(new BBSTPTalkRequest(0,theItem.userID));
					BBSTPTalkReply tr=(BBSTPTalkReply)ois.readObject();
          if(tr.ReplyNumber==0)
          {
				    ((CardLayout)getLayout()).show(this,"talkPanel");
				    talkPanel1.labelStatus.setText(myConnection.getUserName()+" VS. "+theItem.userID);
						talkPanel1.doTalk(is,os);
          }
          else
          {
          	switch(tr.ReplyNumber)
            {
            	case 1:
				        labelReMsg.setText("抱歉，我現在很忙，不能跟你聊。");
              	break;
            	case 2:
				        labelReMsg.setText("我現在很煩，不想跟別人聊天。");
              	break;
            	case 3:
				        labelReMsg.setText("我有急事，我等一下再 Call 你。");
              	break;
            	case 4:
				        labelReMsg.setText("請不要再 Page，我不想跟你聊。");
              	break;
            	case 5:
				        labelReMsg.setText("我要離開了，下次在聊吧。");
              	break;
            	case 6:
				        labelReMsg.setText("請寄一封信給我，我現在沒空。");
              	break;
            	default:
		          	labelReMsg.setText(theItem.userID+tr.ReplyString);
                break;
            }
            panelReMsg.doLayout();
          }
	      }
	    }
  	  catch(Exception e1)
    	{
    		e1.printStackTrace();
	    }
	}

	void talkPanel1_actionPerformed(ActionEvent e)
	{
  	((CardLayout)getLayout()).show(this,"userListPanel");
	}

	void button4_actionPerformed(ActionEvent e)
	{
	 	try
    {
    	talkPanel1.quitTalk();
    	if(ois!=null)
	      ois.close();
      if(oos!=null)
	   	  oos.close();
      if(is!=null)
      	is.close();
      if(os!=null)
      	os.close();
      if(mySocket!=null)
	      mySocket.close();
	  }
    catch(Exception e1)
   	{
   		e1.printStackTrace();
	  }
//  	((CardLayout)getLayout()).show(this,"userListPanel");
	}

	void button6_actionPerformed(ActionEvent e)
	{
  	if(requestTID!=-1)
    {
//	  	myConnection.sendCmd(this,new BBSTPTalkReply(requestTID,choice1.getSelectedIndex(),""));
	  	try
  	  {
	    	Socket mySocket=new Socket(myConnection.getAddress(),5254);
  	    os=mySocket.getOutputStream();
    	  oos=new ObjectOutputStream(os);
      	is=mySocket.getInputStream();
	      ois=new ObjectInputStream(is);

	      oos.writeObject(new Login(myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord()));
  	    LoginState ls=(LoginState)ois.readObject();
    	  if(ls.State==0)
      	{
	      	oos.writeObject(new BBSTPTalkReply(requestTID,choice1.getSelectedIndex(),""));
          if(choice1.getSelectedIndex()==0)
          {
				    ((CardLayout)getLayout()).show(this,"talkPanel");
						talkPanel1.doTalk(is,os);
          }
          else
				  	((CardLayout)getLayout()).show(this,"userListPanel");
	      }
	    }
  	  catch(Exception e1)
    	{
    		e1.printStackTrace();
	    }
    }
		else
	  	((CardLayout)getLayout()).show(this,"userListPanel");
	}

	void button3_actionPerformed(ActionEvent e)
	{
  	int index=list1.getSelectedIndex();
  	if(index!=-1)
    {
    	Object tmp1=listedUser.elementAt(index);
      if(tmp1!=null)
      {
      	SendMsgDialog smd=null;

        synchronized(myConnection)
			  {
        	OnlineUser tmp3=(OnlineUser)myConnection.getData("UserLister_myOnlineUsers");
          if(tmp3!=null)
    		  {
						smd=new SendMsgDialog(new Frame(),myConnection);
            Vector tmp4=tmp3.getOnlineUsers();
    		    smd.setOnlineUsers(tmp4);
						smd.setReceiver(tmp4.indexOf(tmp1));
          }
        }
        if(smd!=null)
        	smd.setVisible(true);
      }
    }
	}

/*  public void doTalkReply(BBSTPTalkReply tr)
  {
    ((CardLayout)getLayout()).show(this,"talkPanel");
		talkPanel1.doTalk(myConnection);
  }*/
}
