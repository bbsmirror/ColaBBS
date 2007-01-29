
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
import java.util.Vector;
import java.util.Hashtable;
import colabbs.bbstp.user.BBSTPUserList;
import colabbs.bbstp.user.BBSTPAddUserItem;
import colabbs.bbstp.user.BBSTPRemoveUserItem;
import colabbs.bbsclient.*;

public class BBSCQ extends Frame implements OnlineUserListener
{
	private boolean topMost=false;
	private Hashtable ConnectionMap=new Hashtable();
	private Vector listedUser=new Vector();
  private Thread myThread=null;
  private IconWindow myIconWin=new IconWindow();
  //
	Panel panel1 = new Panel();
	Button button1 = new Button();
	BorderLayout borderLayout2 = new BorderLayout();
	Choice choice1 = new Choice();
	List list1 = new List();
	java.awt.Image colaIcon;
	Button button2 = new Button();
	private java.awt.PopupMenu startMenu=null;
	PopupMenu userMenu = new PopupMenu();
	MenuItem menuItem1 = new MenuItem();
	MenuItem menuItem2 = new MenuItem();
	Menu menu1 = new Menu();
	MenuItem menuItem3 = new MenuItem();
	MenuItem menuItem4 = new MenuItem();
	MenuItem menuItem5 = new MenuItem();
	MenuItem menuItem6 = new MenuItem();

	public BBSCQ()
	{
  	Toolkit myToolkit=this.getToolkit();
    setLocation(myToolkit.getScreenSize().width-141,myToolkit.getScreenSize().height-281);

  	setSize(140,240);
		try
		{
			jbInit();
//			list1.setBackground(SystemColor.control);
      list1.add(userMenu);

      choice1.add("列出線上使用者");
      choice1.add("只列出上站好友");
      choice1.add("列出所有好友");
			myIconWin.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
			  	if(startMenu!=null)
	  				startMenu.show(panel1,button1.getBounds().x,button1.getBounds().y);
				}
			});

/*      choice1.add("呼開訊開");
      choice1.add("呼友訊開");
      choice1.add("呼關訊開");
      choice1.add("呼開訊友");
      choice1.add("呼友訊友");
      choice1.add("呼關訊友");
      choice1.add("呼開訊關");
      choice1.add("呼友訊關");
      choice1.add("呼關訊關");*/
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

  public void insertMenuItem(MenuItem theItem)
  {
  	if(startMenu!=null)
	    startMenu.insert(theItem,0);
  }

  public void addConnectionManager(ConnectionManager theConnection)
  {
    synchronized(theConnection)
    {
    	OnlineUser myOnlineUsers=(OnlineUser)theConnection.getData("UserLister_myOnlineUsers");
    	try
      {
	  	  if(myOnlineUsers==null)
  	  	{
	  	  	myOnlineUsers=new OnlineUser(theConnection);
  	  	  theConnection.setData("UserLister_myOnlineUsers",myOnlineUsers);
	    	  theConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPAddUserItem"),new CmdItem(myOnlineUsers.getClass().getMethod("addOnlineUser",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPAddUserItem")}),myOnlineUsers));
  	    	theConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPRemoveUserItem"),new CmdItem(myOnlineUsers.getClass().getMethod("removeOnlineUser",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPRemoveUserItem")}),myOnlineUsers));
  	    	theConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPChangeState"),new CmdItem(myOnlineUsers.getClass().getMethod("changeUserState",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPChangeState")}),myOnlineUsers));
	  	    theConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.user.BBSTPUserItemCount"),new CmdItem(myOnlineUsers.getClass().getMethod("setUserItemCount",new Class[]{Class.forName("colabbs.bbstp.user.BBSTPUserItemCount")}),myOnlineUsers));
//    		theConnection.sendCmd(this,new BBSTPUserCount());
			    theConnection.sendCmd(this,new BBSTPUserList());
				}
        else
        {
        	Vector tmpUser=myOnlineUsers.getOnlineUsers();
	        int max=tmpUser.size();
  	      for(int i=0;i<max;i++)
    	    {
          	BBSTPAddUserItem aui=(BBSTPAddUserItem)tmpUser.elementAt(i);
            listedUser.addElement(aui);
				    ConnectionMap.put(aui,theConnection);
				    list1.add(aui.userID+"("+ClientUtils.byte2String(aui.userNick)+")");
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

  public void removeConnectionManager(ConnectionManager theConnection)
  {
    synchronized(theConnection)
    {
    	OnlineUser myOnlineUsers=(OnlineUser)theConnection.getData("UserLister_myOnlineUsers");
      Vector tmpUser=myOnlineUsers.getOnlineUsers();
      int max=tmpUser.size();
      for(int i=0;i<max;i++)
      {
      	BBSTPAddUserItem aui=(BBSTPAddUserItem)tmpUser.elementAt(i);
        listedUser.removeElement(aui);
        ConnectionMap.remove(aui);
        list1.remove(aui.userID+"("+ClientUtils.byte2String(aui.userNick)+")");
      }
      if(myOnlineUsers!=null)
	      myOnlineUsers.removeOnlineUserListener(this);
    }
  }

/*  public void setVisible(boolean value)
  {
  	if(value)
    {
    	super.setVisible(value);
			if(myThread==null)
  	  {
	  	  myThread=new Thread(this);
      	myThread.start();
	    }
    }
    else
    {
    	super.setVisible(value);
//	    list1.removeAll();
    }
  }*/

	private void jbInit() throws Exception
	{
  	if(ClientUtils.isMS())
			button1.setLabel("C ola");
  	else
			button1.setLabel("  C  ola");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		panel1.setLayout(borderLayout2);
		this.setTitle("BBSCQ");
		list1.setBackground(SystemColor.control);
		list1.addMouseListener(new java.awt.event.MouseAdapter()
		{

			public void mouseClicked(MouseEvent e)
			{
				list1_mouseClicked(e);
			}
		});
		button2.setLabel("呼叫器狀態");
		button1.addMouseListener(new java.awt.event.MouseAdapter()
		{

			public void mousePressed(MouseEvent e)
			{
				button1_mousePressed(e);
			}

			public void mouseReleased(MouseEvent e)
			{
				button1_mouseReleased(e);
			}

			public void mouseEntered(MouseEvent e)
			{
				button1_mouseEntered(e);
			}
		});
		menuItem1.setLabel("傳送簡訊");
		menuItem1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				menuItem1_actionPerformed(e);
			}
		});
		menuItem2.setLabel("一對一聊天");
		menu1.setLabel("加到列表");
		menuItem3.setLabel("設成好友");
		menuItem4.setLabel("設成壞人");
		menuItem5.setLabel("查詢詳細資料");
		menuItem6.setLabel("寄信");
		this.add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, BorderLayout.WEST);
		panel1.add(button2, BorderLayout.CENTER);
		this.add(list1, BorderLayout.CENTER);
		this.add(choice1, BorderLayout.NORTH);
		userMenu.add(menuItem1);
		userMenu.add(menuItem2);
		userMenu.add(menuItem5);
		userMenu.add(menuItem6);
		userMenu.add(menu1);
		menu1.add(menuItem3);
		menu1.add(menuItem4);
	}

	void button1_actionPerformed(ActionEvent e)
	{
  	if(startMenu!=null)
	  	startMenu.show(panel1,button1.getBounds().x,button1.getBounds().y);
//  	popupMenu1.show(panel1,button1.getBounds().x,button1.getBounds().y+button1.getBounds().height);
//    list1.add("我的好友:"+buttonCount);
//    buttonCount++;
	}

	public void setColaIcon(java.awt.Image newColaIcon)
	{
		colaIcon = newColaIcon;
    setIconImage(colaIcon);
    myIconWin.setMyIcon(colaIcon);
	}

	public java.awt.Image getColaIcon()
	{
		return colaIcon;
	}

/*	public void run()
	{
		//TODO: implement this java.lang.Runnable method;
    try
    {
	    while(true)
  	  {
	  	  if(button1.isShowing())
  	  	{
        	if(ClientUtils.isMS())
		  		  myIconWin.setLocation(button1.getLocationOnScreen().x,button1.getLocationOnScreen().y+(button1.getSize().height-19));
          else
		  		  myIconWin.setLocation(button1.getLocationOnScreen().x+5,button1.getLocationOnScreen().y+(button1.getSize().height-19));
          if(topMost&&isShowing())
          	this.toFront();
          if(!myIconWin.isShowing())
	  	  		myIconWin.show();
          else
          	myIconWin.toFront();
	  	  }
        Thread.sleep(100);
	    }
    }
    catch(InterruptedException e){}
	}*/

	void button1_mousePressed(MouseEvent e)
	{
//  	System.out.println("press");
    myIconWin.setDished(true);
	}

	void button1_mouseReleased(MouseEvent e)
	{
//  	System.out.println("release");
    myIconWin.setDished(false);
	}

	public void setStartMenu(java.awt.PopupMenu newStartMenu)
	{
		startMenu = newStartMenu;
//		startMenu.add(checkboxMenuItem1);
    panel1.add(startMenu);
	}

	public java.awt.PopupMenu getStartMenu()
	{
		return startMenu;
	}

	public void AddOnlineUser(OnlineUserEvent e)
	{
  	BBSTPAddUserItem theItem=(BBSTPAddUserItem)e.getCmdItem();
    ConnectionMap.put(theItem,e.getConnectionManager());
  	listedUser.addElement(theItem);
    list1.add(theItem.userID+"("+ClientUtils.byte2String(theItem.userNick)+")");
	}

	public void RemoveOnlineUser(OnlineUserEvent e)
	{
  	BBSTPRemoveUserItem rui=(BBSTPRemoveUserItem)e.getCmdItem();
    ConnectionManager myConnection=e.getConnectionManager();
    synchronized(myConnection)
    {
	  	OnlineUser myOnlineUsers=(OnlineUser)myConnection.getData("UserLister_myOnlineUsers");
  	  if(myOnlineUsers!=null)
    	{
      	BBSTPAddUserItem tmp=myOnlineUsers.getUserItem(rui.userID);
        if(tmp!=null)
        {
		  		listedUser.removeElement(tmp);
  		  	list1.remove(tmp.userID+"("+ClientUtils.byte2String(tmp.userNick)+")");
			    ConnectionMap.remove(tmp);
        }
      }
    }
	}

	public void UserStateChanged(OnlineUserEvent e)
	{
//  	AddItem(((BBSTPAddUserItem)e.getCmdItem()).userID);
	}

	void list1_mouseClicked(MouseEvent e)
	{
//  	System.out.println("Clicked:"+list1.getSelectedIndex());
  	if(list1.getSelectedIndex()!=-1&&userMenu!=null)
    {
    	userMenu.show(panel1,e.getX()-list1.getBounds().x,e.getY()-list1.getBounds().y);
//  		System.out.println("Clicked:"+list1.getSelectedIndex());
    }
	}

/*	void checkboxMenuItem1_itemStateChanged(ItemEvent e)
	{
		topMost=checkboxMenuItem1.getState();
	}*/

	void menuItem1_actionPerformed(ActionEvent e)
	{
  	int index=list1.getSelectedIndex();
  	if(index!=-1)
    {
    	Object tmp1=listedUser.elementAt(index);
      if(tmp1!=null)
      {
		  	ConnectionManager tmp2=(ConnectionManager)ConnectionMap.get(tmp1);
        if(tmp2!=null)
        {
        	SendMsgDialog smd=null;

			    synchronized(tmp2)
			    {
			    	OnlineUser tmp3=(OnlineUser)tmp2.getData("UserLister_myOnlineUsers");
			  	  if(tmp3!=null)
    		    {
							smd=new SendMsgDialog(this,tmp2);
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
	}

	void button1_mouseEntered(MouseEvent e)
	{
  	if(ClientUtils.isMS())
    	myIconWin.setLocation(button1.getLocationOnScreen().x,button1.getLocationOnScreen().y+(button1.getSize().height-19));
    else
    	myIconWin.setLocation(button1.getLocationOnScreen().x+5,button1.getLocationOnScreen().y+(button1.getSize().height-19));
    if(!myIconWin.isShowing())
    	myIconWin.show();
    else
    	myIconWin.toFront();
	}
}
