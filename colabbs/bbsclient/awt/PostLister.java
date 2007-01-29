
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:

package colabbs.bbsclient.awt;

import java.awt.*;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.UniModule;
import colabbs.bbsclient.CmdItem;
import colabbs.bbstp.board.BBSTPPostList;
import colabbs.bbstp.board.BBSTPPostItem;
import colabbs.bbstp.board.BBSTPPostCount;
import colabbs.bbstp.BBSTPDirItemCount;
import colabbs.bbstp.board.BBSTPPostItemCount;
import java.awt.event.*;
import java.text.*;
import java.util.*;

public class PostLister extends UniModule
{
//  private static DateFormatter1=new SimpleDateFormat ("EEE MMM dd kk':'mm");
  private static SimpleDateFormat DateFormatter1=new SimpleDateFormat();
  private int itemCount=0;
  private int mode=1;
  private int range=50;
  private String myBoardName=null;
  private Vector myPostList=new Vector();
  private Vector listedPost=new Vector();
//******
  java.awt.List list1 = new java.awt.List();
  Panel panel1 = new Panel();
  FlowLayout flowLayout1 = new FlowLayout();
  Panel searchPanel = new Panel();
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
  Panel listMode = new Panel();
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
	private int selectedIndex;
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel3 = new Panel();
	GridLayout gridLayout2 = new GridLayout();
	Button button1 = new Button();
	Panel panel4 = new Panel();
	Panel ctrlPanel = new Panel();
	GridLayout gridLayout3 = new GridLayout();
	Button button2 = new Button();
	Label label1 = new Label();
	Label label2 = new Label();
	CardLayout cardLayout1 = new CardLayout();
	Panel panel13 = new Panel();
	Panel panel2 = new Panel();
	GridLayout gridLayout4 = new GridLayout();
	Label label3 = new Label();
	private transient Vector articleCmdListeners;
	Button button4 = new Button();
	Button button5 = new Button();
	Button button6 = new Button();
	BorderLayout borderLayout5 = new BorderLayout();
	Panel panel5 = new Panel();
	Panel panel9 = new Panel();
	FlowLayout flowLayout3 = new FlowLayout();
	Label label4 = new Label();
	BorderLayout borderLayout6 = new BorderLayout();
	Panel panel14 = new Panel();
	GridLayout gridLayout5 = new GridLayout();

  public PostLister(ConnectionManager theConnection)
  {
    super(theConnection);
    try
    {
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostItem"),new CmdItem(this.getClass().getMethod("addPostItem",new Class[]{Class.forName("colabbs.bbstp.board.BBSTPPostItem")}),this));
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostItemCount"),new CmdItem(this.getClass().getMethod("setPostItemCount",new Class[]{Class.forName("colabbs.bbstp.board.BBSTPPostItemCount")}),this));
      jbInit();
//      TextPanel.add(myArticle,"Center");
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    panel1.setLayout(flowLayout1);
    searchPanel.setLayout(flowLayout2);
    textField1.setColumns(20);
    textField1.addTextListener(new java.awt.event.TextListener()
    {

      public void textValueChanged(TextEvent e)
      {
        textField1_textValueChanged(e);
      }
    });
    checkbox1.setCheckboxGroup(checkboxGroup1);
    checkbox1.setLabel("作者");
    checkbox2.setCheckboxGroup(checkboxGroup1);
    checkbox2.setLabel("標題");
    checkboxGroup1.setSelectedCheckbox(checkbox1);
    panel6.setLayout(borderLayout2);
    panel8.setLayout(borderLayout3);
    checkbox3.setCheckboxGroup(checkboxGroup2);
    checkbox3.setLabel("未讀文章");
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
    label8.setText("篇");
    button3.setLabel("更新");
    button3.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button3_actionPerformed(e);
      }
    });
    panel11.setLayout(borderLayout4);
		panel3.setLayout(gridLayout2);
		gridLayout2.setColumns(1);
		gridLayout2.setRows(2);
		button1.setLabel("列示方式");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		panel4.setLayout(gridLayout3);
		gridLayout3.setColumns(1);
		gridLayout3.setRows(2);
		button2.setLabel("搜尋文章");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		label1.setText("列示:");
		label2.setText("搜尋:");
		ctrlPanel.setLayout(cardLayout1);
		panel2.setLayout(gridLayout4);
		gridLayout4.setColumns(1);
		gridLayout4.setRows(2);
		button4.setLabel("讀此篇文章");
		button4.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button4_actionPerformed(e);
			}
		});
		button5.setLabel("回此篇文章");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		button6.setLabel("發表新文章");
		button6.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button6_actionPerformed(e);
			}
		});
		list1.setFont(new java.awt.Font("Monospaced", 0, 12));
		list1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button4_actionPerformed(e);
			}
		});
		panel13.setLayout(borderLayout5);
		panel9.setLayout(flowLayout3);
		flowLayout3.setAlignment(FlowLayout.LEFT);
		flowLayout3.setHgap(0);
		flowLayout3.setVgap(0);
		label4.setFont(new java.awt.Font("Monospaced", 0, 12));
		label4.setText("作        者 發表日期 標            題");
		panel12.setLayout(borderLayout6);
		panel14.setLayout(gridLayout5);
		gridLayout5.setColumns(1);
		gridLayout5.setRows(3);
		panel13.setBackground(new java.awt.Color(157, 185, 200));
		this.add(panel8, BorderLayout.NORTH);
		panel8.add(panel4, BorderLayout.WEST);
		panel4.add(button1, null);
		panel4.add(button2, null);
		panel8.add(ctrlPanel, BorderLayout.CENTER);
		ctrlPanel.add(listMode, "listMode");
		listMode.add(label1, null);
		listMode.add(panel3, null);
		panel3.add(checkbox3, null);
		panel3.add(checkbox6, null);
		listMode.add(panel10, null);
		panel10.add(checkbox5, null);
		panel10.add(checkbox4, null);
		listMode.add(textField7, null);
		listMode.add(label8, null);
		listMode.add(button3, null);
		ctrlPanel.add(searchPanel, "searchPanel");
		searchPanel.add(label2, null);
		searchPanel.add(panel2, null);
		panel2.add(checkbox1, null);
		panel2.add(checkbox2, null);
		searchPanel.add(textField1, null);
		panel8.add(panel13, BorderLayout.SOUTH);
		panel13.add(panel5, BorderLayout.WEST);
		panel13.add(panel9, BorderLayout.CENTER);
		panel9.add(label4, null);
		panel9.add(label3, null);
		this.add(panel1, BorderLayout.SOUTH);
		this.add(panel11, BorderLayout.CENTER);
		panel11.add(panel6, BorderLayout.CENTER);
		panel6.add(list1, BorderLayout.CENTER);
		panel6.add(panel7, BorderLayout.WEST);
		panel6.add(panel12, BorderLayout.EAST);
		panel12.add(panel14, BorderLayout.NORTH);
		panel14.add(button4, null);
		panel14.add(button5, null);
		panel14.add(button6, null);
  }

  public synchronized void doList(String boardName)
  {
	  //for IE!!
//    list1.deselect(list1.getSelectedIndex());
    //
	  myPostList.removeAllElements();
  	listedPost.removeAllElements();
    list1.removeAll();
    myBoardName=boardName;
    try
    {
//      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostItem"),new CmdItem(this.getClass().getMethod("addPostItem",new Class[]{Class.forName("colabbs.bbstp.board.BBSTPPostItem")}),this));
//      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPPostItemCount"),new CmdItem(this.getClass().getMethod("setPostItemCount",new Class[]{Class.forName("colabbs.bbstp.board.BBSTPPostItemCount")}),this));
      myConnection.sendCmd(this,new BBSTPPostCount(boardName));
      initMode();
      myConnection.sendCmd(this,new BBSTPPostList(boardName,mode,range));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  public synchronized void setPostItemCount(BBSTPPostItemCount pic)
  {
    itemCount=((BBSTPDirItemCount)pic).Count;
    if(itemCount>=0)
      label3.setText("(討論區: "+myBoardName+"，共 "+itemCount+" 篇文章)");
    else
      label3.setText("(討論區: "+myBoardName+"，為新建討論區，目前沒有文章)");
    panel9.doLayout();
  }

  public synchronized void addPostItem(BBSTPPostItem pi)
  {
  	if(pi.BoardName.equals(myBoardName))
    {
  	  myPostList.addElement(pi);
    	listedPost.addElement(pi);
			list1.add(ClientUtils.Cut(pi.poster,13)+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+"  "+ClientUtils.byte2String(pi.title));
    }
//    list1.add(Utils.Cut(pi.poster,15)+Utils.Cut(pi.title));
  }

  void textField1_textValueChanged(TextEvent e)
  {
    String check=textField1.getText();

    if(checkbox1.getState())
    {
      int max=myPostList.size();
	  	//for IE!!
//  		list1.deselect(list1.getSelectedIndex());
    	//
      listedPost.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPPostItem pi=(BBSTPPostItem)myPostList.elementAt(i);
        String me=ClientUtils.byte2String(pi.poster);
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
            listedPost.addElement(pi);
						list1.add(ClientUtils.byte2String(ClientUtils.Cut(pi.poster,13))+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+"  "+ClientUtils.byte2String(pi.title));
          }
        }
      }
    }
    else
    {
      int max=myPostList.size();
	  	//for IE!!
//  		list1.deselect(list1.getSelectedIndex());
    	//
      listedPost.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPPostItem pi=(BBSTPPostItem)myPostList.elementAt(i);
        if(pi.title.indexOf(check)>=0)
        {
          listedPost.addElement(pi);
					list1.add(ClientUtils.Cut(pi.poster,13)+ClientUtils.PostDateFormatter.format(new Date(pi.filetime))+"  "+ClientUtils.byte2String(pi.title));
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
    BBSTPPostItem pi=(BBSTPPostItem)listedPost.elementAt(list1.getSelectedIndex());

    if(pi!=null)
    {
      textField2.setText(pi.poster);
      textField3.setText(pi.title);
      textField4.setText(DateFormatter1.format(new Date(pi.filetime)));
      textField5.setText(pi.Mark?"是":"否");
      textField6.setText(pi.Digest?"是":"否");
    }
  }*/

  void button3_actionPerformed(ActionEvent e)
  {
    if(myBoardName!=null)
    {
//      myPostList.removeAllElements();
//      listedPost.removeAllElements();
//      list1.removeAll();
      doList(myBoardName);
    }
  }

/*	void button1_actionPerformed(ActionEvent e)
	{
    ((CardLayout)getLayout()).show(this,"TextPanel");
    BBSTPPostItem pi=(BBSTPPostItem)listedPost.elementAt(list1.getSelectedIndex());
    myArticle.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),myBoardName,pi.index);
	}*/

  public boolean last()
  {
  	if(list1.getSelectedIndex()>0)
    {
	  	list1.select(list1.getSelectedIndex()-1);
      return true;
    }
    return false;
  }

  public boolean next()
  {
  	if(list1.getSelectedIndex()!=-1&&list1.getSelectedIndex()<list1.getItemCount()-1)
    {
	  	list1.select(list1.getSelectedIndex()+1);
      return true;
    }
    return false;
  }

	public int getSelectedIndex()
	{
  	if(list1.getSelectedIndex()!=-1)
    {
	    BBSTPPostItem pi=(BBSTPPostItem)listedPost.elementAt(list1.getSelectedIndex());
  	  selectedIndex=pi.index;
    }
    else
    	selectedIndex=-1;
		return selectedIndex;
	}

	public BBSTPPostItem getSelectedItem()
	{
  	if(list1.getSelectedIndex()!=-1)
    {
	    return (BBSTPPostItem)listedPost.elementAt(list1.getSelectedIndex());
    }
    return null;
	}

	void button1_actionPerformed(ActionEvent e)
	{
    ((CardLayout)ctrlPanel.getLayout()).show(ctrlPanel,"listMode");
	}

	void button2_actionPerformed(ActionEvent e)
	{
    ((CardLayout)ctrlPanel.getLayout()).show(ctrlPanel,"searchPanel");
	}

	public synchronized void removeArticleCmdListener(ArticleCmdListener l)
	{
		if(articleCmdListeners != null && articleCmdListeners.contains(l))
		{
			Vector v = (Vector) articleCmdListeners.clone();
			v.removeElement(l);
			articleCmdListeners = v;
		}
	}

	public synchronized void addArticleCmdListener(ArticleCmdListener l)
	{
		Vector v = articleCmdListeners == null ? new Vector(2) : (Vector) articleCmdListeners.clone();
		if(!v.contains(l))
		{
			v.addElement(l);
			articleCmdListeners = v;
		}
	}

	protected void fireReadArticle(ArticleCmdEvent e)
	{
		if(articleCmdListeners != null)
		{
			Vector listeners = articleCmdListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((ArticleCmdListener) listeners.elementAt(i)).ReadArticle(e);
			}
		}
	}

	protected void fireSendNewArticle(ArticleCmdEvent e)
	{
		if(articleCmdListeners != null)
		{
			Vector listeners = articleCmdListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((ArticleCmdListener) listeners.elementAt(i)).SendNewArticle(e);
			}
		}
	}

	protected void fireReplyArticle(ArticleCmdEvent e)
	{
		if(articleCmdListeners != null)
		{
			Vector listeners = articleCmdListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((ArticleCmdListener) listeners.elementAt(i)).ReplyArticle(e);
			}
		}
	}

	void button4_actionPerformed(ActionEvent e)
	{
    if(list1.getSelectedIndex()!=-1)
    	fireReadArticle(new ArticleCmdEvent(this));
	}

	void button5_actionPerformed(ActionEvent e)
	{
    if(list1.getSelectedIndex()!=-1)
    	fireReplyArticle(new ArticleCmdEvent(this));
	}

	void button6_actionPerformed(ActionEvent e)
	{
    fireSendNewArticle(new ArticleCmdEvent(this));
	}

}
