package colabbs.bbsclient.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import colabbs.bbsclient.StatusBar;

public class SwingLoginPanel extends LoginPanel
{
  JPanel panel1 = new JPanel();
  JLabel label1 = new JLabel();
  JTextField textUserName = new JTextField();
  JLabel label2 = new JLabel();
  JPasswordField textPassWord = new JPasswordField();
  JButton button1 = new JButton();
  JButton button2 = new JButton();
  JCheckBox checkbox1 = new JCheckBox();
  private String passWord;
  private String userName;
  private boolean newID;
  JLabel label3 = new JLabel();
  JPasswordField textCheckPass = new JPasswordField();
	JPanel checkPanel = new JPanel();
	JPanel panel2 = new JPanel();
	CardLayout cardLayout1 = new CardLayout();
	JPanel clearPanel = new JPanel();

  public SwingLoginPanel()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    label1.setBounds(new Rectangle(67, 26, 91, 27));
    label1.setText("請輸入您的帳號");
    panel1.setLayout(null);
    textUserName.setBounds(new Rectangle(167, 27, 152, 25));
    textUserName.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    textUserName.getDocument().addDocumentListener(new DocumentListener()
    {
      public void changedUpdate(DocumentEvent e)
      {
      }

      public void insertUpdate(DocumentEvent e)
      {
        textUserName_textValueChanged();
      }

      public void removeUpdate(DocumentEvent e)
      {
        textUserName_textValueChanged();
      }

    });
    label2.setBounds(new Rectangle(67, 71, 77, 26));
    label2.setText("密碼");
    textPassWord.setBounds(new Rectangle(167, 70, 152, 28));
    textPassWord.setEchoChar('*');
    textPassWord.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    button1.setBounds(new Rectangle(66, 185, 93, 30));
    button1.setEnabled(false);
    button1.setText("登入");
    button1.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button1_actionPerformed(e);
      }
    });
    button2.setBounds(new Rectangle(222, 184, 91, 31));
    button2.setText("清除重寫");
    button2.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button2_actionPerformed(e);
      }
    });
    checkbox1.setBounds(new Rectangle(66, 157, 129, 23));
    checkbox1.setText("註冊新的帳號");
    checkbox1.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkbox1_itemStateChanged(e);
      }
    });
    label3.setBounds(new Rectangle(13, 0, 91, 34));
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
		panel2.setBounds(new Rectangle(55, 107, 269, 42));
		panel2.setLayout(cardLayout1);
		panel1.setEnabled(true);
    panel1.add(label1, null);
    panel1.add(label2, null);
    panel1.add(textUserName, null);
    panel1.add(textPassWord, null);
    panel1.add(button1, null);
    panel1.add(checkbox1, null);
    panel1.add(button2, null);
		panel1.add(panel2, null);
		panel2.add(clearPanel, "clearPanel");
		panel2.add(checkPanel, "checkPanel");
		checkPanel.add(textCheckPass, null);
		checkPanel.add(label3, null);
  }

  void button2_actionPerformed(ActionEvent e)
  {
    textUserName.setText("");
    textPassWord.setText("");
  }

  public void clear()
  {
    textUserName.setText("");
    textPassWord.setText("");
  }

  public void paint(java.awt.Graphics g)
  {
    if(getMyLogo()!=null)
      g.drawImage(getMyLogo(),50,250,panel1);
    g.drawString("HHHHHH", 200,200);
  }

  void textUserName_textValueChanged()
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
    if((checkbox1.getSelectedObjects() != null)&&!textCheckPass.getPassword().equals(textPassWord.getPassword()))
    {
      StatusBar.sysStatusBar.setMessage("密碼與確認密碼比對不同!");
      return;
    }
    userName=textUserName.getText();
    passWord=new String(textPassWord.getPassword());
    newID=(checkbox1.getSelectedObjects() != null);
    fireActionPerformed(new ActionEvent(panel1,ActionEvent.ACTION_PERFORMED,userName));
  }

  public void setPassWord(String newPassWord)
  {
    passWord = newPassWord;
  }

  public String getPassWord()
  {
    return passWord;
  }

  public void setUserName(String newUserName)
  {
    userName = newUserName;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setNewID(boolean newNewID)
  {
    newID = newNewID;
  }

  public boolean isNewID()
  {
    return newID;
  }

  void checkbox1_itemStateChanged(ItemEvent e)
  {
    if(checkbox1.getSelectedObjects() != null)
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
    //super.setEnabled(newEnabled);
  }

  public boolean isEnabled()
  {
    //return super.isEnabled();
    return true;
  }

  public static void main(String args[]){
    JFrame f = new JFrame("Test");
    SwingLoginPanel sw = new SwingLoginPanel();
    f.getContentPane().add(sw.panel1);
    f.pack();
    f.setVisible(true);
  }

}
