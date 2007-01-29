
//Title:        Cola BBS System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       yhwu
//Company:      infoX
//Description:

package colabbs.bbsclient.awt;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;

//import colabbs.bbsclient.FunctionModule;
import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.board.BBSTPBoardItem;
import colabbs.bbstp.board.BBSTPPostItem;
import colabbs.bbstp.board.BBSTPPostSize;
import colabbs.bbstp.board.BBSTPSendPost;
import colabbs.bbstp.board.BBSTPSendPostStatus;
import colabbs.bbsclient.TabModule;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.MultiModule;
import colabbs.bbsclient.ConnectionManager;

public class BoardFunction extends MultiModule implements TabModule
{
	//
  BorderLayout borderLayout1 = new BorderLayout();
  CheckboxGroup checkboxGroup1 = new CheckboxGroup();
  Panel mainPanel = new Panel();
  CardLayout cardLayout1 = new CardLayout();
  BoardLister boardLister1 = null;
  PostLister postLister1 = null;
	Panel boardPanel = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	Panel ViewArticle = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	TextViewer myArticle = new TextViewer();
	Panel panel1 = new Panel();
	Button button1 = new Button();
	Panel postEditor = new Panel();
	BorderLayout borderLayout3 = new BorderLayout();
	SendPostPanel myPostEditor = new SendPostPanel();
	Panel panel2 = new Panel();
	Button button2 = new Button();
	Button button3 = new Button();
	Button button4 = new Button();
	Button button5 = new Button();
	Button replyButton = new Button();

  public BoardFunction(ConnectionManager theConnection)
  {
    super(theConnection);
    myName="討論區視窗";
    boardLister1=new BoardLister(theConnection);
    postLister1=new PostLister(theConnection);
    try
    {
      jbInit();
      doLayout();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
		this.setBackground(SystemColor.control);
		this.setEnabled(true);
    mainPanel.setLayout(cardLayout1);
    boardPanel.setLayout(gridLayout1);
		gridLayout1.setColumns(1);
		gridLayout1.setRows(2);
		boardLister1.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				boardLister1_itemStateChanged(e);
			}
		});
		ViewArticle.setLayout(borderLayout2);
		button1.setLabel("回討論區列表");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button1_actionPerformed(e);
			}
		});
		postEditor.setLayout(borderLayout3);
		button2.setLabel("確定發表文章");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button2_actionPerformed(e);
			}
		});
		button3.setLabel("取消發表文章");
		button3.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button3_actionPerformed(e);
			}
		});
		postLister1.addArticleCmdListener(new colabbs.bbsclient.awt.ArticleCmdListener()
		{

			public void ReadArticle(ArticleCmdEvent e)
			{
				postLister1_ReadArticle(e);
			}

			public void SendNewArticle(ArticleCmdEvent e)
			{
				postLister1_SendNewArticle(e);
			}

			public void ReplyArticle(ArticleCmdEvent e)
			{
				postLister1_ReplyArticle(e);
			}
		});
		button4.setLabel("上一篇");
		button4.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button4_actionPerformed(e);
			}
		});
		button5.setLabel("下一篇");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		replyButton.setLabel("回此篇文章");
		replyButton.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				replyButton_actionPerformed(e);
			}
		});
		this.add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(boardPanel, "boardPanel");
		boardPanel.add(boardLister1, null);
		boardPanel.add(postLister1, null);
		mainPanel.add(ViewArticle, "ViewArticle");
		ViewArticle.add(myArticle, BorderLayout.CENTER);
		ViewArticle.add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, null);
		panel1.add(button4, null);
		panel1.add(button5, null);
		panel1.add(replyButton, null);
		mainPanel.add(postEditor, "postEditor");
		postEditor.add(myPostEditor, BorderLayout.CENTER);
		postEditor.add(panel2, BorderLayout.SOUTH);
		panel2.add(button2, null);
		panel2.add(button3, null);
  }

  public static MenuItem getFunctionItem()
  {
    return (new MenuItem("開新討論區視窗"));
  }

  public String getMyClassName()
  {
    return getClass().getName();
  }

	synchronized void boardLister1_itemStateChanged(ItemEvent e)
	{
    postLister1.doList(boardLister1.getBoardName());

	}

	void button1_actionPerformed(ActionEvent e)
	{
    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"boardPanel");
	}

	public String getTabName()
	{
		//TODO: implement this colabbs.bbsclient.TabModule method;
    return "七嘴八舌討論區";
	}

	void button3_actionPerformed(ActionEvent e) //取消
	{
    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"boardPanel");
	}

	void button2_actionPerformed(ActionEvent e) //確定
	{
  	doSendPost();
    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"boardPanel");
	}

	void replyButton_actionPerformed(ActionEvent e) //回文章
	{
  	if(postLister1.getSelectedIndex()!=-1)
    {
  	  BBSTPPostItem pi=postLister1.getSelectedItem();
    	BBSTPBoardItem bi=boardLister1.getSelectedItem();
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
  	    newstr=newstr+"※ 在 "+pi.poster+" 的來信中提到: \n";
    	  while((bufstr=br.readLine())!=null)
      	  newstr=newstr+": "+bufstr+"\n";
	    	myPostEditor.setArticle(newstr);
    	  myPostEditor.setBoardName(boardLister1.getBoardName());
      	myPostEditor.setTitle(ClientUtils.byte2String(pi.title));
        //設簽名檔數
	      if(bi.Anonymous)
		      myPostEditor.setAnonymous(bi.AnonyDefault);
    	  myPostEditor.setLocalSave(!bi.SaveMode);
	      myPostEditor.setSignatureNumber(myConnection.getSignatureNumber());
		    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"postEditor");
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

	void newPostButton_actionPerformed(ActionEvent e) //寫新文章
	{
   	BBSTPBoardItem bi=boardLister1.getSelectedItem();
  	myPostEditor.setBoardName(boardLister1.getBoardName());
    myPostEditor.setAnonymousBoard(bi.Anonymous);
   	myPostEditor.setArticle("");;
    myPostEditor.setTitle("");
    //設簽名檔數
    if(bi.Anonymous)
	    myPostEditor.setAnonymous(bi.AnonyDefault);
    myPostEditor.setLocalSave(!bi.SaveMode);
    myPostEditor.setSignatureNumber(myConnection.getSignatureNumber());
    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"postEditor");
	}

  private void doSendPost()
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
	    	byte[] postBody=ClientUtils.string2ByteArray(myPostEditor.getArticle());
      	oos.writeObject(new BBSTPSendPost(boardLister1.getBoardName(),ClientUtils.string2Byte(myPostEditor.getTitle()),myPostEditor.getSignature(),myPostEditor.isLocalSave(),myPostEditor.isAnonymous()));
      	oos.writeObject(new BBSTPPostSize(postBody.length));
        os.write(postBody);
        System.out.println("File size : "+postBody.length);
	      BBSTPSendPostStatus sps=(BBSTPSendPostStatus)ois.readObject();
        switch(sps.status)
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

	void postLister1_ReadArticle(ArticleCmdEvent e) //讀此篇文章
	{
  	if(postLister1.getSelectedIndex()!=-1)
    {
	    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"ViewArticle");
  	  myArticle.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),boardLister1.getBoardName(),postLister1.getSelectedIndex());
    }
	}

	void postLister1_ReplyArticle(ArticleCmdEvent e) //回此篇文章
	{
  	if(postLister1.getSelectedIndex()!=-1)
    {
  	  BBSTPPostItem pi=postLister1.getSelectedItem();
    	BBSTPBoardItem bi=boardLister1.getSelectedItem();
    	myPostEditor.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),boardLister1.getBoardName(),pi);
      myPostEditor.setBoardName(boardLister1.getBoardName());
      myPostEditor.setTitle(ClientUtils.byte2String(pi.title));
      //設簽名檔數
      if(bi.Anonymous)
	      myPostEditor.setAnonymous(bi.AnonyDefault);
      myPostEditor.setLocalSave(!bi.SaveMode);
      myPostEditor.setSignatureNumber(myConnection.getSignatureNumber());
	    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"postEditor");
    }
	}

	void postLister1_SendNewArticle(ArticleCmdEvent e)
	{
  	if(boardLister1.getBoardName().length()!=0)
    {
    	BBSTPBoardItem bi=boardLister1.getSelectedItem();
	  	myPostEditor.setBoardName(boardLister1.getBoardName());
      myPostEditor.setAnonymousBoard(bi.Anonymous);
	   	myPostEditor.setArticle("");;
  	  myPostEditor.setTitle("");
      if(bi.Anonymous)
	      myPostEditor.setAnonymous(bi.AnonyDefault);
      myPostEditor.setLocalSave(!bi.SaveMode);
      myPostEditor.setSignatureNumber(myConnection.getSignatureNumber());

  	  ((CardLayout)mainPanel.getLayout()).show(mainPanel,"postEditor");
    }
	}

	void button4_actionPerformed(ActionEvent e) //上一篇
	{
   	if(postLister1.last())
	 	  myArticle.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),boardLister1.getBoardName(),postLister1.getSelectedIndex());
    else
	    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"boardPanel");
	}

	void button5_actionPerformed(ActionEvent e) //下一篇
	{
   	if(postLister1.next())
	 	  myArticle.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),boardLister1.getBoardName(),postLister1.getSelectedIndex());
    else
	    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"boardPanel");
	}
}
