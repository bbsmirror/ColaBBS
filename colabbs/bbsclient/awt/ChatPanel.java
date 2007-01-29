
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.awt;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.Enumeration;

import colabbs.bbsclient.*;
import colabbs.bbstp.chat.*;

public class ChatPanel extends UniModule
{
	private Hashtable Verb1_1 = null, Verb1_2 = null, Verb2 = null, Verb3 = null;
	private colabbs.bbsclient.ConnectionManager myConnection;
	CardLayout cardLayout1 = new CardLayout();
	Panel panelLogin = new Panel();
	Panel panelChat = new Panel();
	Label label1 = new Label();
	GridLayout gridLayout1 = new GridLayout();
	Panel panel1 = new Panel();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	Panel panel6 = new Panel();
	Panel panel7 = new Panel();
	TextField textChatNick = new TextField();
	Button buttonEnterChat = new Button();
	Button buttonClear = new Button();
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel8 = new Panel();
	Panel panel9 = new Panel();
	Panel panel10 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel11 = new Panel();
	Panel panel12 = new Panel();
	TextField textRoom = new TextField();
	Label label2 = new Label();
	FlowLayout flowLayout1 = new FlowLayout();
	Label label3 = new Label();
	TextField textTopic = new TextField();
	BorderLayout borderLayout3 = new BorderLayout();
	Panel panel13 = new Panel();
	Panel panel14 = new Panel();
	FlowLayout flowLayout2 = new FlowLayout();
	FlowLayout flowLayout3 = new FlowLayout();
	Panel panel15 = new Panel();
	Panel panel16 = new Panel();
	Panel panel17 = new Panel();
	BorderLayout borderLayout4 = new BorderLayout();
	TextArea textChat = new TextArea();
	BorderLayout borderLayout5 = new BorderLayout();
	Panel panel18 = new Panel();
	Panel panel19 = new Panel();
	Label label4 = new Label();
	TextField textNick = new TextField();
	FlowLayout flowLayout4 = new FlowLayout();
	Label label5 = new Label();
	BorderLayout borderLayout6 = new BorderLayout();
	TextField textSay = new TextField();
	Panel panel20 = new Panel();
	Panel panel21 = new Panel();
	FlowLayout flowLayout5 = new FlowLayout();
	FlowLayout flowLayout6 = new FlowLayout();
	Panel panel22 = new Panel();
	Panel panel23 = new Panel();
	Panel panel24 = new Panel();
	Button button1 = new Button();
	Button button2 = new Button();
	Button button3 = new Button();
	Button button4 = new Button();
	Button button5 = new Button();
	Button button6 = new Button();
	Panel panelChatMain = new Panel();
	CardLayout cardLayout2 = new CardLayout();
	Panel panelAction = new Panel();
	Panel panelSendMessage = new Panel();
	Panel panelOPFunction = new Panel();
	BorderLayout borderLayout7 = new BorderLayout();
	Checkbox checkbox1 = new Checkbox();
	Checkbox checkbox2 = new Checkbox();
	Checkbox checkbox3 = new Checkbox();
	CheckboxGroup checkboxGroup1 = new CheckboxGroup();
	Panel panel26 = new Panel();
	CardLayout cardLayout3 = new CardLayout();
	Panel panelAction1 = new Panel();
	Panel panelAction2 = new Panel();
	Panel panelAction3 = new Panel();
	BorderLayout borderLayout9 = new BorderLayout();
	Panel panel27 = new Panel();
	Panel panelActionPre1 = new Panel();
	java.awt.List list1 = new java.awt.List();
	BorderLayout borderLayout10 = new BorderLayout();
	Panel panel29 = new Panel();
	Panel panel30 = new Panel();
	Panel panel31 = new Panel();
	BorderLayout borderLayout11 = new BorderLayout();
	Panel panel32 = new Panel();
	Panel panelActionPre2 = new Panel();
	java.awt.List list2 = new java.awt.List();
	BorderLayout borderLayout12 = new BorderLayout();
	Panel panel34 = new Panel();
	Panel panel35 = new Panel();
	Panel panel36 = new Panel();
	BorderLayout borderLayout13 = new BorderLayout();
	Panel panel37 = new Panel();
	Panel panelActionPre3 = new Panel();
	java.awt.List list3 = new java.awt.List();
	BorderLayout borderLayout14 = new BorderLayout();
	Panel panel39 = new Panel();
	Panel panel40 = new Panel();
	Panel panel41 = new Panel();
	BorderLayout borderLayout15 = new BorderLayout();
	Panel panel42 = new Panel();
	Button buttonDoAction = new Button();
	Label label6 = new Label();
	TextField textMsgNick = new TextField();
	Label label7 = new Label();
	TextField textSendMsg = new TextField();
	Button buttonDoSendMessage = new Button();
	BorderLayout borderLayout8 = new BorderLayout();
	Panel panel25 = new Panel();
	Panel panel43 = new Panel();
	GridLayout gridLayout2 = new GridLayout();
	GridLayout gridLayout3 = new GridLayout();
	Label label8 = new Label();
	Panel panel44 = new Panel();
	Checkbox checkboxLock = new Checkbox();
	Label label9 = new Label();
	Panel panel45 = new Panel();
	Checkbox checkboxHide = new Checkbox();
	Label label10 = new Label();
	Panel panel46 = new Panel();
	TextField textInvite = new TextField();
	Label label11 = new Label();
	Panel panel47 = new Panel();
	TextField textKick = new TextField();
	Label label12 = new Label();
	Panel panel48 = new Panel();
	TextField textChangeOP = new TextField();
	Label label13 = new Label();
	Panel panel49 = new Panel();
	Button buttonStartRecord = new Button();
	Button buttonEndRecord = new Button();
	Panel panel50 = new Panel();
	Button buttonOPOK = new Button();
	Button buttonOPCancel = new Button();
	Button buttonApply = new Button();
	Label labelAction11 = new Label();
	FlowLayout flowLayout7 = new FlowLayout();
	TextField textActionNick = new TextField();
	Label labelAction12 = new Label();
	Label labelAction2 = new Label();
	FlowLayout flowLayout8 = new FlowLayout();
	TextField textActionSay = new TextField();
	Label labelAction3 = new Label();
	Button buttonActionCancel = new Button();
	Panel panel28 = new Panel();

	public ChatPanel()
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
		this.setLayout(cardLayout1);
		label1.setText("請輸入您的聊天代號:");
		panelLogin.setLayout(gridLayout1);
		gridLayout1.setColumns(3);
		gridLayout1.setRows(3);
		textChatNick.setColumns(10);
		textChatNick.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonEnterChat_actionPerformed(e);
			}
		});
		buttonEnterChat.setLabel("確定");
		buttonEnterChat.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonEnterChat_actionPerformed(e);
			}
		});
		buttonClear.setLabel("清除");
		buttonClear.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonClear_actionPerformed(e);
			}
		});
		panelChat.setLayout(borderLayout1);
		panel10.setLayout(borderLayout2);
		label2.setText("包廂");
		textRoom.setColumns(8);
		textRoom.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				textRoom_actionPerformed(e);
			}
		});
		panel11.setLayout(flowLayout1);
		label3.setText("話題");
		textTopic.setColumns(30);
		textTopic.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				textTopic_actionPerformed(e);
			}
		});
		panel12.setLayout(borderLayout3);
		panel13.setLayout(flowLayout2);
		flowLayout2.setVgap(2);
		panel14.setLayout(flowLayout3);
		flowLayout3.setVgap(2);
		panel9.setLayout(borderLayout4);
		textChat.setBackground(Color.black);
		textChat.setForeground(Color.white);
		textChat.setEditable(false);
		panel8.setLayout(borderLayout5);
		label4.setText("代號");
		textNick.setColumns(8);
		textNick.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				textNick_actionPerformed(e);
			}
		});
		panel18.setLayout(flowLayout4);
		label5.setText("發言");
		panel19.setLayout(borderLayout6);
		panel20.setLayout(flowLayout5);
		flowLayout5.setVgap(2);
		panel21.setLayout(flowLayout6);
		flowLayout6.setVgap(2);
		textSay.setBackground(Color.black);
		textSay.setForeground(Color.white);
		textSay.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				textSay_actionPerformed(e);
			}
		});
		panel23.setLayout(cardLayout2);
		button1.setLabel("離開聊天室");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		button2.setLabel("包廂列表");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		button3.setLabel("聊天者列表");
		button3.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button3_actionPerformed(e);
			}
		});
		button4.setLabel("社交動作");
		button4.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button4_actionPerformed(e);
			}
		});
		button5.setLabel("悄悄話");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		button6.setLabel("管理指令");
		button6.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button6_actionPerformed(e);
			}
		});
		panelAction.setLayout(borderLayout7);
		checkbox1.setCheckboxGroup(checkboxGroup1);
		checkbox1.setLabel("第一類動詞");
		checkbox1.setState(true);
		checkbox2.setCheckboxGroup(checkboxGroup1);
		checkbox2.setLabel("第二類動詞");
		checkbox3.setCheckboxGroup(checkboxGroup1);
		checkbox3.setLabel("第三類動詞");
		panel26.setLayout(cardLayout3);
		panelAction1.setLayout(borderLayout9);
		panel27.setLayout(borderLayout10);
		panelAction2.setLayout(borderLayout11);
		panel32.setLayout(borderLayout12);
		panelAction3.setLayout(borderLayout13);
		panel37.setLayout(borderLayout14);
		panelChatMain.setLayout(borderLayout15);
		buttonDoAction.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonDoAction_actionPerformed(e);
			}
		});
		buttonDoAction.setLabel("確定");
		label6.setText("送紙條給:");
		label7.setText("內容");
		buttonDoSendMessage.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonDoSendMessage_actionPerformed(e);
			}
		});
		buttonDoSendMessage.setLabel("確定");
		textMsgNick.setColumns(8);
		panelOPFunction.setLayout(borderLayout8);
		panel25.setLayout(gridLayout2);
		panel43.setLayout(gridLayout3);
		gridLayout2.setRows(8);
		gridLayout3.setRows(8);
		label8.setText("改變鎖定狀態");
		checkboxLock.setLabel("是");
		label9.setText("改變隱藏狀態");
		label10.setText("邀請使用者");
		label11.setText("趕出使用者");
		label12.setText("讓出老大權力給");
		label13.setText("錄音");
		checkboxHide.setLabel("是");
		textInvite.setColumns(20);
		textKick.setColumns(20);
		textChangeOP.setColumns(20);
		buttonStartRecord.setLabel("啟動");
		buttonEndRecord.setLabel("結束");
		buttonOPOK.setLabel("確定");
		buttonOPOK.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonOPOK_actionPerformed(e);
			}
		});
		buttonOPCancel.setLabel("取消");
		buttonOPCancel.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonOPCancel_actionPerformed(e);
			}
		});
		buttonApply.setLabel("套用");
		buttonApply.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonApply_actionPerformed(e);
			}
		});
		panelActionPre1.setLayout(flowLayout7);
		flowLayout7.setAlignment(FlowLayout.LEFT);
		textActionNick.setColumns(8);
		panelActionPre2.setLayout(flowLayout8);
		flowLayout8.setAlignment(FlowLayout.LEFT);
		textActionSay.setColumns(30);
		buttonActionCancel.setLabel("取消");
		buttonActionCancel.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				buttonActionCancel_actionPerformed(e);
			}
		});
		list1.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				list1_itemStateChanged(e);
			}
		});
		list2.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				list2_itemStateChanged(e);
			}
		});
		list3.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				list3_itemStateChanged(e);
			}
		});
		textSendMsg.setColumns(40);
		this.add(panelLogin, "panelLogin");
		panelLogin.add(panel7, null);
		panelLogin.add(panel6, null);
		panelLogin.add(panel5, null);
		panelLogin.add(panel4, null);
		panelLogin.add(panel3, null);
		panel3.add(label1, null);
		panel3.add(textChatNick, null);
		panel3.add(buttonEnterChat, null);
		panel3.add(buttonClear, null);
		panelLogin.add(panel2, null);
		panelLogin.add(panel1, null);
		this.add(panelChat, "panelChat");
		panelChat.add(panel23, BorderLayout.CENTER);
		panel27.add(panel29, BorderLayout.NORTH);
		panel23.add(panelChatMain, "panelChatMain");
		panelChatMain.add(panel9, BorderLayout.CENTER);
		panel9.add(panel16, BorderLayout.WEST);
		panel9.add(panel17, BorderLayout.EAST);
		panel9.add(textChat, BorderLayout.CENTER);
		panelChatMain.add(panel8, BorderLayout.SOUTH);
		panel8.add(panel18, BorderLayout.WEST);
		panel18.add(label4, null);
		panel18.add(textNick, null);
		panel8.add(panel19, BorderLayout.CENTER);
		panel19.add(label5, BorderLayout.WEST);
		panel19.add(textSay, BorderLayout.CENTER);
		panel19.add(panel20, BorderLayout.SOUTH);
		panel19.add(panel21, BorderLayout.NORTH);
		panel19.add(panel22, BorderLayout.EAST);
		panelChatMain.add(panel10, BorderLayout.NORTH);
		panel10.add(panel11, BorderLayout.WEST);
		panel11.add(label2, null);
		panel11.add(textRoom, null);
		panel10.add(panel12, BorderLayout.CENTER);
		panel12.add(label3, BorderLayout.WEST);
		panel12.add(textTopic, BorderLayout.CENTER);
		panel12.add(panel13, BorderLayout.SOUTH);
		panel12.add(panel14, BorderLayout.NORTH);
		panel12.add(panel15, BorderLayout.EAST);
		panel23.add(panelAction, "panelAction");
		panelAction.add(panel26, BorderLayout.CENTER);
		panel26.add(panelAction1, "panelAction1");
		panelAction1.add(panel27, BorderLayout.WEST);
		panel27.add(list1, BorderLayout.CENTER);
		panel27.add(panel29, BorderLayout.SOUTH);
		panel27.add(panel30, BorderLayout.WEST);
		panel27.add(panel31, BorderLayout.SOUTH);
		panelAction1.add(panelActionPre1, BorderLayout.CENTER);
		panelActionPre1.add(labelAction11, null);
		panelActionPre1.add(textActionNick, null);
		panelActionPre1.add(labelAction12, null);
		panel26.add(panelAction2, "panelAction2");
		panelAction2.add(panel32, BorderLayout.WEST);
		panel32.add(list2, BorderLayout.CENTER);
		panel32.add(panel34, BorderLayout.SOUTH);
		panel32.add(panel35, BorderLayout.WEST);
		panel32.add(panel36, BorderLayout.NORTH);
		panelAction2.add(panelActionPre2, BorderLayout.CENTER);
		panelActionPre2.add(labelAction2, null);
		panelActionPre2.add(textActionSay, null);
		panel26.add(panelAction3, "panelAction3");
		panelAction3.add(panel37, BorderLayout.WEST);
		panel37.add(list3, BorderLayout.CENTER);
		panel37.add(panel39, BorderLayout.SOUTH);
		panel37.add(panel40, BorderLayout.WEST);
		panel37.add(panel41, BorderLayout.NORTH);
		panelAction3.add(panelActionPre3, BorderLayout.CENTER);
		panelActionPre3.add(labelAction3, null);
		panelAction.add(panel42, BorderLayout.NORTH);
		panel42.add(buttonApply, null);
		panel42.add(buttonDoAction, null);
		panel42.add(buttonActionCancel, null);
		panel42.add(checkbox1, null);
		panel42.add(checkbox2, null);
		panel42.add(checkbox3, null);
		panel23.add(panelSendMessage, "panelSendMessage");
		panelSendMessage.add(label6, null);
		panelSendMessage.add(textMsgNick, null);
		panelSendMessage.add(panel28, null);
		panel28.add(label7, null);
		panel28.add(textSendMsg, null);
		panel28.add(buttonDoSendMessage, null);
		panel23.add(panelOPFunction, "panelOPFunction");
		panelOPFunction.add(panel25, BorderLayout.WEST);
		panel25.add(label8, null);
		panel25.add(label9, null);
		panel25.add(label10, null);
		panel25.add(label11, null);
		panel25.add(label12, null);
		panel25.add(label13, null);
		panelOPFunction.add(panel43, BorderLayout.CENTER);
		panel43.add(panel44, null);
		panel44.add(checkboxLock, null);
		panel43.add(panel45, null);
		panel45.add(checkboxHide, null);
		panel43.add(panel46, null);
		panel46.add(textInvite, null);
		panel43.add(panel47, null);
		panel47.add(textKick, null);
		panel43.add(panel48, null);
		panel48.add(textChangeOP, null);
		panel43.add(panel49, null);
		panel49.add(buttonStartRecord, null);
		panel49.add(buttonEndRecord, null);
		panelOPFunction.add(panel50, BorderLayout.SOUTH);
		panel50.add(buttonOPOK, null);
		panel50.add(buttonOPCancel, null);
//		panel26.add(panel27, "panelAction1");
//		panel26.add(panelActionPre1, "panelAction2");
		panelChat.add(panel24, BorderLayout.NORTH);
		panel24.add(button1, null);
		panel24.add(button2, null);
		panel24.add(button3, null);
		panel24.add(button4, null);
		panel24.add(button5, null);
		panel24.add(button6, null);
	}

  public void finalize()
  {
    if(textChatNick.getText().length()!=0)
    {
	  	myConnection.sendCmd(this,new ChatLogout());
    }
  }

	public void setMyConnection(colabbs.bbsclient.ConnectionManager newMyConnection)
	{
		myConnection = newMyConnection;
    try
    {
	    newMyConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatLoginState"),new CmdItem(this.getClass().getMethod("doEnterChat",new Class[]{Class.forName("colabbs.bbstp.chat.ChatLoginState")}),this));
	    newMyConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatAddMessage"),new CmdItem(this.getClass().getMethod("doAddMessage",new Class[]{Class.forName("colabbs.bbstp.chat.ChatAddMessage")}),this));
	    newMyConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatEnterRoom"),new CmdItem(this.getClass().getMethod("doEnterRoom",new Class[]{Class.forName("colabbs.bbstp.chat.ChatEnterRoom")}),this));
	    newMyConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatSetTopic"),new CmdItem(this.getClass().getMethod("doSetTopic",new Class[]{Class.forName("colabbs.bbstp.chat.ChatSetTopic")}),this));
	    newMyConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.chat.ChatActions"),new CmdItem(this.getClass().getMethod("doChatActions",new Class[]{Class.forName("colabbs.bbstp.chat.ChatActions")}),this));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
	}

	void buttonClear_actionPerformed(ActionEvent e)
	{
    textChatNick.setText("");
	}

	void buttonEnterChat_actionPerformed(ActionEvent e)
	{
		myConnection.sendCmd(this,new ChatLogin(ClientUtils.string2Byte(textChatNick.getText())));
	}

  public void doEnterChat(ChatLoginState cls)
  {
  	if(cls.Result)
    {
    	textNick.setText(textChatNick.getText());
	    ((CardLayout)getLayout()).show(this,"panelChat");
    }
    else
    {
			(new MessageBox(cls.Message)).setVisible(true);
    }
  }

  public void doAddMessage(ChatAddMessage cam)
  {
//  	System.out.println("doAddMessage");
  	textChat.append(ClientUtils.CutAnsiCode(ClientUtils.byte2String(cam.Message))+"\n");
  }

  public void doEnterRoom(ChatEnterRoom cer)
  {
//  	System.out.println("doEnterRoom");
  	textRoom.setText(ClientUtils.byte2String(cer.Room));
  }

  public void doSetTopic(ChatSetTopic cst)
  {
//  	System.out.println("doSetTopic");
  	textTopic.setText(ClientUtils.byte2String(cst.newTopic));
  }

  public void doChatActions(ChatActions ca)
  {
  	Verb1_1=ca.Verb1_1;
    Verb1_2=ca.Verb1_2;
    Verb2=ca.Verb2;
    Verb3=ca.Verb3;

		Enumeration tmp=Verb1_1.keys();
		while(tmp.hasMoreElements())
			list1.add((String)tmp.nextElement());
		tmp=Verb2.keys();
		while(tmp.hasMoreElements())
			list2.add((String)tmp.nextElement());
		tmp=Verb3.keys();
		while(tmp.hasMoreElements())
			list3.add((String)tmp.nextElement());
  }

	void textRoom_actionPerformed(ActionEvent e)
	{
		myConnection.sendCmd(this,new ChatEnterRoom(ClientUtils.string2Byte(textRoom.getText())));
	}

	void textTopic_actionPerformed(ActionEvent e)
	{
		myConnection.sendCmd(this,new ChatSetTopic(ClientUtils.string2Byte(textTopic.getText())));
	}

	void textNick_actionPerformed(ActionEvent e)
	{
		myConnection.sendCmd(this,new ChatChangeNick(ClientUtils.string2Byte(textNick.getText())));
	}

	void textSay_actionPerformed(ActionEvent e)
	{
		myConnection.sendCmd(this,new ChatPostMsg(ClientUtils.string2Byte(textSay.getText())));
    textSay.setText("");
	}

	void button1_actionPerformed(ActionEvent e) //quit
	{
  	myConnection.sendCmd(this,new ChatLogout());
    textChatNick.setText("");
    ((CardLayout)getLayout()).show(this,"panelLogin");
	}

	void button2_actionPerformed(ActionEvent e) //包廂列表
	{
		myConnection.sendCmd(this,new ChatListUser(3,null));
	}

	void button3_actionPerformed(ActionEvent e) //聊天者列表
	{
		myConnection.sendCmd(this,new ChatListUser(1,null));
	}

	void button4_actionPerformed(ActionEvent e) //社交動作
	{
//		myConnection.sendCmd(this,);
    cardLayout2.show(panel23,"panelAction");
	}

	void button5_actionPerformed(ActionEvent e) //悄悄話
	{
//		myConnection.sendCmd(this,);
    cardLayout2.show(panel23,"panelSendMessage");
	}

	void button6_actionPerformed(ActionEvent e) //管理者指令
	{
//		myConnection.sendCmd(this,);
    cardLayout2.show(panel23,"panelOPFunction");
	}

	void buttonDoAction_actionPerformed(ActionEvent e)
	{
  	if(checkbox1.getState()&&list1.getSelectedIndex()!=-1)
    {
    	myConnection.sendCmd(this,new ChatDoAction(list1.getSelectedItem(),textActionNick.getText()));
    }
  	if(checkbox2.getState())
    {
    	myConnection.sendCmd(this,new ChatDoAction(list2.getSelectedItem(),textActionSay.getText()));
    }
  	if(checkbox3.getState())
    {
    	myConnection.sendCmd(this,new ChatDoAction(list3.getSelectedItem(),""));
    }
    cardLayout2.show(panel23,"panelChatMain");
	}

	void buttonDoSendMessage_actionPerformed(ActionEvent e)
	{
  	myConnection.sendCmd(this,new ChatSendNote(ClientUtils.string2Byte(textMsgNick.getText()),ClientUtils.string2Byte(textSendMsg.getText())));
    cardLayout2.show(panel23,"panelChatMain");
	}

	void buttonOPOK_actionPerformed(ActionEvent e)
	{
  	myConnection.sendCmd(this,new ChatChangeRoomProperties(checkboxLock.getState(),checkboxHide.getState()));
    if(textChangeOP.getText().length()!=0)
    	myConnection.sendCmd(this,new ChatSetOP(ClientUtils.string2Byte(textChangeOP.getText())));
    if(textInvite.getText().length()!=0)
    	myConnection.sendCmd(this,new ChatInviteUser(ClientUtils.string2Byte(textInvite.getText())));
    if(textKick.getText().length()!=0)
    	myConnection.sendCmd(this,new ChatKickUser(ClientUtils.string2Byte(textKick.getText())));
    checkboxLock.setState(false);
    checkboxHide.setState(false);
    textChangeOP.setText("");
    textInvite.setText("");
    textKick.setText("");
    cardLayout2.show(panel23,"panelChatMain");
	}

	void list1_itemStateChanged(ItemEvent e)
	{
  	if(list1.getSelectedIndex()!=-1)
    {
			labelAction11.setText((String)Verb1_1.get(list1.getSelectedItem()));
			labelAction12.setText((String)Verb1_2.get(list1.getSelectedItem()));
      panelActionPre1.doLayout();
    }
	}

	void list2_itemStateChanged(ItemEvent e)
	{
  	if(list2.getSelectedIndex()!=-1)
    {
			labelAction2.setText((String)Verb2.get(list2.getSelectedItem()));
      panelActionPre2.doLayout();
    }
	}

	void list3_itemStateChanged(ItemEvent e)
	{
  	if(list3.getSelectedIndex()!=-1)
    {
			labelAction3.setText((String)Verb3.get(list3.getSelectedItem()));
      panelActionPre3.doLayout();
    }
	}

	void buttonApply_actionPerformed(ActionEvent e)
	{
  	if(checkbox1.getState()&&list1.getSelectedIndex()!=-1)
    {
    	myConnection.sendCmd(this,new ChatDoAction(list1.getSelectedItem(),textActionNick.getText()));
    }
  	if(checkbox2.getState())
    {
    	myConnection.sendCmd(this,new ChatDoAction(list2.getSelectedItem(),textActionSay.getText()));
    }
  	if(checkbox3.getState())
    {
    	myConnection.sendCmd(this,new ChatDoAction(list3.getSelectedItem(),""));
    }
	}

	void buttonActionCancel_actionPerformed(ActionEvent e)
	{
    cardLayout2.show(panel23,"panelChatMain");
	}

	void buttonOPCancel_actionPerformed(ActionEvent e)
	{
    cardLayout2.show(panel23,"panelChatMain");
	}
}
