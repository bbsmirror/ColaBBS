
//Title:        Cola BBS System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       yhwu
//Company:      infoX
//Description:

package colabbs.bbsclient.awt;

import java.awt.*;
import java.awt.event.*;

import colabbs.bbsclient.StatusBar;

public class AppletLoginPanel extends LoginPanel
{
  Label label1 = new Label();
  TextField textUserName = new TextField();
  Label label2 = new Label();
  TextField textPassWord = new TextField();
  Button button1 = new Button();
  Button button2 = new Button();
  Checkbox checkboxNewID = new Checkbox();
//  private String passWord;
  private String userName;
//  private boolean newID;
  Label label3 = new Label();
  TextField textCheckPass = new TextField();
	Panel checkPanel = new Panel();
	Panel panel2 = new Panel();
	CardLayout cardLayout1 = new CardLayout();
	Panel clearPanel = new Panel();
	Label label4 = new Label();
	TextField textEmail = new TextField();
//	private String email;

  public AppletLoginPanel()
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
    label1.setBounds(new Rectangle(66, 26, 86, 23));
    label1.setText("請輸入您的帳號");
    this.setLayout(null);
    textUserName.setBounds(new Rectangle(167, 27, 152, 25));
		textUserName.setText("guest");
    textUserName.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    textUserName.addTextListener(new java.awt.event.TextListener()
    {

      public void textValueChanged(TextEvent e)
      {
        textUserName_textValueChanged(e);
      }
    });
    label2.setBounds(new Rectangle(66, 71, 86, 23));
    label2.setText("密碼");
    textPassWord.setBounds(new Rectangle(167, 70, 152, 25));
    textPassWord.setEchoChar('*');
    textPassWord.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    button1.setBounds(new Rectangle(66, 214, 93, 30));
    button1.setEnabled(false);
    button1.setLabel("登入");
    button1.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    button2.setBounds(new Rectangle(222, 213, 91, 31));
    button2.setLabel("清除重寫");
    button2.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button2_actionPerformed(e);
      }
    });
    checkboxNewID.setBounds(new Rectangle(66, 186, 104, 23));
    checkboxNewID.setLabel("註冊新的帳號");
    checkboxNewID.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkboxNewID_itemStateChanged(e);
      }
    });
    label3.setBounds(new Rectangle(13, 0, 84, 27));
    label3.setText("確認密碼");
    textCheckPass.setEchoChar('*');
    textCheckPass.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    textCheckPass.setBounds(new Rectangle(112, 1, 152, 28));
		checkPanel.setLayout(null);
		panel2.setBounds(new Rectangle(55, 107, 273, 77));
		panel2.setLayout(cardLayout1);
		label4.setBounds(new Rectangle(13, 39, 84, 27));
		label4.setText("電子郵件信箱");
		textEmail.setBounds(new Rectangle(112, 39, 152, 28));
		textEmail.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
		textEmail.setEchoChar('*');
		this.add(label1, null);
    this.add(label2, null);
    this.add(textUserName, null);
    this.add(textPassWord, null);
		this.add(panel2, null);
		panel2.add(clearPanel, "clearPanel");
		panel2.add(checkPanel, "checkPanel");
		checkPanel.add(textCheckPass, null);
		checkPanel.add(label3, null);
		checkPanel.add(label4, null);
		checkPanel.add(textEmail, null);
		this.add(button1, null);
		this.add(checkboxNewID, null);
		this.add(button2, null);
  }

  void button2_actionPerformed(ActionEvent e)
  {
    textUserName.setText("");
    textPassWord.setText("");
    textCheckPass.setText("");
    textEmail.setText("");
  }

  public void clear()
  {
    textUserName.setText("");
    textPassWord.setText("");
    textCheckPass.setText("");
    textEmail.setText("");
  }

  public void paint(java.awt.Graphics g)
  {
    if(getMyLogo()!=null)
      g.drawImage(getMyLogo(),50,250,this);
  }

  void textUserName_textValueChanged(TextEvent e)
  {
    String theID=textUserName.getText();
    if(theID.length()!=0&&ValidID(theID))
      button1.setEnabled(true);
    else
      button1.setEnabled(false);
  }

  void button1_actionPerformed(ActionEvent e)
  {
    if(!isEnabled())
      return;
    userName=textUserName.getText();
    if(userName.length()==0||!ValidID(userName))
    {
      StatusBar.sysStatusBar.setMessage("錯誤的使用者帳號!");
      return;
    }
    if(checkboxNewID.getState()&&!textCheckPass.getText().equals(textPassWord.getText()))
    {
      StatusBar.sysStatusBar.setMessage("密碼與確認密碼比對不同!");
      return;
    }
    if(checkboxNewID.getState()&&(textEmail.getText().indexOf("@")==-1||textEmail.getText().indexOf(".bbs@")!=-1))
    {
      StatusBar.sysStatusBar.setMessage("請輸入您的電子郵件信箱(請勿使用其它電子佈告欄之信箱)!");
      return;
    }
    userName=textUserName.getText();
//    passWord=textPassWord.getText();
//    newID=checkboxNewID.getState();
    fireActionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,userName));
  }

  public void setPassWord(String newPassWord)
  {
//    passWord = newPassWord;
		textPassWord.setText(newPassWord);
  }

  public String getPassWord()
  {
//    return passWord;
		return textPassWord.getText();
  }

  public void setUserName(String newUserName)
  {
//    userName = newUserName;
		textUserName.setText(newUserName);
  }

  public String getUserName()
  {
//    return userName;
		return textUserName.getText();
  }

  public void setNewID(boolean newNewID)
  {
//    newID = newNewID;
		checkboxNewID.setState(newNewID);
  }

  public boolean isNewID()
  {
//    return newID;
		return checkboxNewID.getState();
  }

  void checkboxNewID_itemStateChanged(ItemEvent e)
  {
    if(checkboxNewID.getState())
    {
	  	((CardLayout)panel2.getLayout()).show(panel2,"checkPanel");
//      label3.setVisible(true);
//      textCheckPass.setVisible(true);
    }
    else
    {
  		((CardLayout)panel2.getLayout()).show(panel2,"clearPanel");
//      label3.setVisible(false);
//      textCheckPass.setVisible(false);
    }
  }

  public void setEnabled(boolean newEnabled)
  {
    super.setEnabled(newEnabled);
  }

  public boolean isEnabled()
  {
    return super.isEnabled();
  }

	void textEmail_actionPerformed(ActionEvent e)
	{

	}

	public void setEmail(String newEmail)
	{
//		email = newEmail;
		textEmail.setText(newEmail);
	}

	public String getEmail()
	{
//		return email;
		return textEmail.getText();
	}
}
