package colabbs.bbsclient.swing;

import java.awt.*;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.UniModule;
import colabbs.bbsclient.CmdItem;
import colabbs.bbstp.BBSTPDirItemCount;
import colabbs.bbstp.mail.BBSTPMailList;
import colabbs.bbstp.mail.BBSTPMailItem;
import colabbs.bbstp.mail.BBSTPMailCount;
import colabbs.bbstp.mail.BBSTPMailItemCount;
import java.util.Vector;
import java.awt.event.*;
import java.text.*;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.*;

public class SwingMailLister extends UniModule
{
//  private static DateFormatter1=new SimpleDateFormat ("EEE MMM dd kk':'mm");
  private static SimpleDateFormat DateFormatter1=new SimpleDateFormat();
  private int itemCount=0;
  private int mode=1;
  private int range=50;
  private Vector myMailList=new Vector();
  private Vector listedMail=new Vector();
  private DefaultListModel list1Model = new DefaultListModel();
//******
  JList list1 = new JList(list1Model);
  JPanel panel1 = new JPanel();
  JButton button1 = new JButton();
  FlowLayout flowLayout1 = new FlowLayout();
  JButton button2 = new JButton();
  JPanel panel2 = new JPanel();
  JLabel label1 = new JLabel();
  FlowLayout flowLayout2 = new FlowLayout();
  JTextField textField1 = new JTextField();
  JCheckBox checkbox1 = new JCheckBox();
  JCheckBox checkbox2 = new JCheckBox();
  ButtonGroup checkboxGroup1 = new ButtonGroup();
  JPanel panel6 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel panel7 = new JPanel();
  JPanel panel8 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JPanel panel9 = new JPanel();
  JLabel label7 = new JLabel();
  JCheckBox checkbox3 = new JCheckBox();
  ButtonGroup checkboxGroup2 = new ButtonGroup();
  JCheckBox checkbox4 = new JCheckBox();
  JCheckBox checkbox5 = new JCheckBox();
  JTextField textField7 = new JTextField();
  JCheckBox checkbox6 = new JCheckBox();
  JPanel panel10 = new JPanel();
  GridLayout gridLayout1 = new GridLayout();
  JLabel label8 = new JLabel();
  JButton button3 = new JButton();
  JPanel panel11 = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel panel12 = new JPanel();
  JLabel label9 = new JLabel();
  JPanel panel13 = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JButton button4 = new JButton();
	JPanel mailList = new JPanel();
	BorderLayout borderLayout6 = new BorderLayout();
	JPanel mailViewer = new JPanel();
	BorderLayout borderLayout7 = new BorderLayout();
	CardLayout cardLayout1 = new CardLayout();
	SwingTextViewer myArticle = new SwingTextViewer();
	JPanel panel5 = new JPanel();
	JButton button5 = new JButton();

  public SwingMailLister(ConnectionManager theConnection)
  {
    super(theConnection);
    try
    {
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItem"),new CmdItem(this.getClass().getMethod("addMailItem",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItem")}),this));
      myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount"),new CmdItem(this.getClass().getMethod("setMailItemCount",new Class[]{Class.forName("colabbs.bbstp.mail.BBSTPMailItemCount")}),this));
      jbInit();
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
    button1.setText("讀此封信件");
    panel1.setLayout(flowLayout1);
    button2.setText("回此封信件");
    label1.setText("搜尋信件:");
    panel2.setLayout(flowLayout2);
    textField1.setColumns(14);
    textField1.getDocument().addDocumentListener(new DocumentListener()
    {
      public void changedUpdate(DocumentEvent e)
      {
      }

      public void insertUpdate(DocumentEvent e)
      {
        textField1_textValueChanged();
      }

      public void removeUpdate(DocumentEvent e)
      {
        textField1_textValueChanged();
      }

    });
    checkboxGroup1.add(checkbox1);
    checkbox1.setText("寄件者");
    checkbox1.setSelected(true);
    checkboxGroup1.add(checkbox2);
    checkbox2.setText("信件主題");
    panel6.setLayout(borderLayout2);
    panel8.setLayout(borderLayout3);
    label7.setText("列示方式:");
    checkboxGroup2.add(checkbox3);
    checkbox3.setText("未讀信件");
    checkbox3.setSelected(true);
    checkboxGroup2.add(checkbox4);
    checkbox4.setText("最新");
    checkbox4.setSelected(true);
    checkboxGroup2.add(checkbox5);
    checkbox5.setText("最舊");
    textField7.setColumns(5);
    textField7.setText("50");
    checkboxGroup2.add(checkbox6);
    checkbox6.setText("全部");
    panel10.setLayout(gridLayout1);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(0);
    label8.setText("封");
    button3.setText("更新");
    button3.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        button3_actionPerformed(e);
      }
    });
    panel11.setLayout(borderLayout4);
    panel13.setLayout(borderLayout5);
    button4.setText("寫新信件");
    list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    list1.addListSelectionListener(new ListSelectionListener()
    {

      public void valueChanged(ListSelectionEvent e)
      {
        list1_actionPerformed();
      }

    });
		mailList.setLayout(borderLayout6);
		mailViewer.setLayout(borderLayout7);
		button5.setText("回信件列表");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
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
		panel13.add(label9, BorderLayout.CENTER);
		mailList.add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, null);
		panel1.add(button2, null);
		panel1.add(button4, null);
		this.add(mailViewer, "mailViewer");
		mailViewer.add(myArticle, BorderLayout.CENTER);
		mailViewer.add(panel5, BorderLayout.SOUTH);
		panel5.add(button5, null);
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
      label9.setText("您信箱中共有 "+itemCount+" 封信件");
    else
      label9.setText("您信箱目前沒有信件");
    doLayout();
  }

  public void addMailItem(BBSTPMailItem pi)
  {
    pi.title=ClientUtils.byte2String(pi.title);
    pi.sender =ClientUtils.byte2String(pi.sender);
    myMailList.addElement(pi);
    listedMail.addElement(pi);
    list1Model.addElement(pi.title);
  }

  void textField1_textValueChanged()
  {
    String check=textField1.getText();

    if(checkbox1.getSelectedObjects() != null)
    {
      int max=myMailList.size();
      listedMail.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPMailItem pi=(BBSTPMailItem)myMailList.elementAt(i);
        String me=pi.sender;
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
            listedMail.addElement(pi);
            list1Model.addElement(pi.title);
          }
        }
      }
    }
    else
    {
      int max=myMailList.size();
      listedMail.removeAllElements();
      list1.removeAll();
      for(int i=0;i<max;i++)
      {
        BBSTPMailItem pi=(BBSTPMailItem)myMailList.elementAt(i);
        if(pi.title.indexOf(check)>=0)
        {
          listedMail.addElement(pi);
          list1Model.addElement(pi.title);
        }
      }
    }
  }

  public void initMode()
  {
    try
    {
      if(checkbox3.getSelectedObjects() != null)
      {
        mode=4;
      }
      else if(checkbox4.getSelectedObjects() != null)
      {
        mode=1;
        range=Integer.parseInt(textField7.getText());
      }
      else if(checkbox5.getSelectedObjects() != null)
      {
        mode=2;
        range=Integer.parseInt(textField7.getText());
      }
      else if(checkbox6.getSelectedObjects() != null)
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
    myMailList.removeAllElements();
    listedMail.removeAllElements();
    list1.removeAll();
    doList();
  }

	void list1_actionPerformed()
	{
    ((CardLayout)getLayout()).show(this,"mailViewer");
    BBSTPMailItem mi=(BBSTPMailItem)listedMail.elementAt(list1.getSelectedIndex());
    myArticle.ReadMail(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),mi.index);
	}

	void button5_actionPerformed(ActionEvent e)
	{
    ((CardLayout)getLayout()).show(this,"mailList");
	}
}
