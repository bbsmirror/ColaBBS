package colabbs.bbsclient.swing;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;
import javax.swing.*;
import javax.swing.event.*;

import colabbs.bbsclient.*;
import colabbs.bbstp.*;
//import com.borland.jbcl.layout.*;

public class SwingClient extends JApplet
{
// My own field
	public String UserName;
  public String PassWord;
  private Vector AllFrames=new Vector();
  private Vector allPlasmids=new Vector();
  private SwingBBSCQ myBBSCQ=new SwingBBSCQ();
  private Image myLogo=null;
  private ConnectionManager myConnect=new ConnectionManager();
// end of my own field.
  boolean isStandalone = false;
  JTabbedPane jTabbedPane = new JTabbedPane();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel panel3 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel label2 = new JLabel();
  JPanel funcPanel = new JPanel();
  SwingLoginPanel swingLoginPanel1 = new SwingLoginPanel();
  CardLayout cardLayout1 = new CardLayout();
  JPanel loginokpanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  ImagePanel logoPanel1 = new ImagePanel();
//JPanel panel2 = new JPanel();
	FlowLayout flowLayout1 = new FlowLayout();
	JButton button1 = new JButton();
	JButton button2 = new JButton();
	JButton button3 = new JButton();
	JButton button4 = new JButton();
	JButton button5 = new JButton();
	JButton button7 = new JButton();
	JButton button6 = new JButton();
//JPanel panel4 = new JPanel();
	CardLayout cardLayout2 = new CardLayout();
	Menu menu1 = new Menu();
	MenuItem menuItem1 = new MenuItem();
	MenuItem menuItem2 = new MenuItem();
	MenuItem menuItem3 = new MenuItem();
	PopupMenu popupMenu1 = new PopupMenu();

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public SwingClient() {
  }

  //Initialize the applet
  public void init() {
    try  {
      jbInit();
  //*******************************************
      // My init....
      initCmdTable();
      myBBSCQ.setStartMenu(popupMenu1);
      new SwingLabelStatusBar(label2);
      System.out.println(getCodeBase());
      if(getCodeBase()!=null)
      {
        myLogo=this.getImage(getCodeBase(),"icons/ActColaBBS.GIF");
        initClientPlasmid((new URL(getCodeBase(),"ClientPlasmid.INI")).openStream());
        myBBSCQ.setColaIcon(this.getImage(getCodeBase(),"icons/ColaIconSmall.GIF"));
      }
      swingLoginPanel1.setMyLogo(myLogo);
    }
    catch(Exception e)  {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    this.setSize(new Dimension(600,450));
    this.getContentPane().setLayout(borderLayout1);
    panel3.setLayout(borderLayout2);
    panel1.setLayout(borderLayout3);
    panel3.setBackground(new java.awt.Color(157, 185, 200));
    label2.setBackground(new java.awt.Color(174, 168, 217));
    label2.setText("請輸入您的帳號(勿用中文)和密碼以登入系統，參觀請用guest");
    funcPanel.setLayout(cardLayout1);
    swingLoginPanel1.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        swingLoginPanel1_actionPerformed(e);
      }
    });
    funcPanel.setBackground(new java.awt.Color(208, 227, 211));
    loginokpanel.setLayout(borderLayout5);
//		panel2.setLayout(flowLayout1);
		button1.setText("七嘴八舌討論區");
		button1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button1_actionPerformed(e);
			}
		});
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		button2.setText("電子郵件傳情意");
		button2.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button2_actionPerformed(e);
			}
		});
		button3.setText("第一次親密接觸");
		button3.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button3_actionPerformed(e);
			}
		});
//		panel2.setBackground(new java.awt.Color(157, 185, 200));
		button4.setText("全民開講聊天室");
		button4.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button4_actionPerformed(e);
			}
		});
		button5.setText("個人機密檔案");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button5_actionPerformed(e);
			}
		});
		button7.setText("揮一揮衣袖");
		button7.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				menuItem3_actionPerformed(e);
			}
		});
		button6.setText("選項");
		button6.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				//button6_actionPerformed(e);
			}
		});
//		panel4.setLayout(cardLayout2);
		menu1.setLabel("說明");
		menuItem1.setLabel("說明主題");
		menuItem2.setLabel("關於");
		menuItem3.setLabel("登出");
		menuItem3.addActionListener(new java.awt.event.ActionListener()
    {

      public void actionPerformed(ActionEvent e)
      {
        menuItem3_actionPerformed(e);
      }
    });
		this.getContentPane().add(panel1, BorderLayout.CENTER);
    panel1.add(panel3, BorderLayout.EAST);
    panel1.add(label2, BorderLayout.SOUTH);
    panel1.add(funcPanel, BorderLayout.CENTER);
    funcPanel.add(swingLoginPanel1.panel1, "swingLoginPanel1");
    funcPanel.add(loginokpanel, "loginokpanel");
/*		loginokpanel.add(panel2, BorderLayout.NORTH);
		panel2.add(button1, null);
		panel2.add(button2, null);
		panel2.add(button3, null);
		panel2.add(button4, null);
		panel2.add(button5, null);
		panel2.add(button6, null);
		panel2.add(button7, null);
*/
		loginokpanel.add(jTabbedPane, BorderLayout.CENTER);
		jTabbedPane.add(logoPanel1, "LOGO");
		menu1.add(menuItem1);
		menu1.add(menuItem2);
		popupMenu1.addSeparator();
		popupMenu1.add(menuItem3);
		popupMenu1.add(menu1);
  }

  //Start the applet
  public void start() {
  }

  //Stop the applet
  public void stop() {
  }

  //Destroy the applet
  public void destroy() {
  }

  //Get Applet information
  public String getAppletInfo() {
    return "Cola BBS Client Applet AWT Version 1.0 by yhwu at infoX (C)1999-1999";
  }

  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }

  //Main method
  public static void main(String[] args)
  {
    SwingClient applet = new SwingClient();
    applet.isStandalone = true;
    JFrame frame = new JFrame();
    frame.setTitle("Applet Frame");
    frame.getContentPane().add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(600,470);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
    frame.addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        System.exit(0);
      }
    });
  }


  void swingLoginPanel1_actionPerformed(ActionEvent e)
  {
//    System.out.println("swingLoginPanel1_actionPerformed");
  	if(e.getActionCommand().length()==0)
	  {
      label2.setText("請輸入您的帳號(勿用中文)和密碼以登入系統");
  		return;
	  }

		UserName=swingLoginPanel1.getUserName();
    PassWord=swingLoginPanel1.getPassWord();
    myConnect.setAddress("127.0.0.1"); //for debug!
//    myConnect.setAddress(getCodeBase().getHost());
    myConnect.setClientType(1); //1:Applet
    myConnect.setUserName(UserName);
    myConnect.setPassWord(PassWord);
    myConnect.setNewID(swingLoginPanel1.isNewID());
    myConnect.connect(); //若已有連線則強迫中斷重連!
//  		mySwingChat.myCodeBase=getCodeBase().toString();
//	  	mySwingChat=new SwingChat(oos,getJTextField1().getText());
//  		mySwingChat.setVisible(true);
  }

  public void checkLoginState(/*ConnectionManager theConnection,*/LoginState theCmd)
  {
/*    btnBoards.setEnabled(true);
    btnUsers.setEnabled(true);
    btnSetup.setEnabled(true);*/
    switch(theCmd.State)
    {
      case 0:
        StatusBar.setMessage("登入成功");
	      myBBSCQ.setVisible(true);
        swingLoginPanel1.setEnabled(false);
        if(getCodeBase()!=null)
        {
          logoPanel1.setMyImage(this.getImage(getCodeBase(),"icons/infoX.gif"));
        }
        ((CardLayout)funcPanel.getLayout()).show(funcPanel,"loginokpanel");
        //*********************
        try
        {
					for (Enumeration e = allPlasmids.elements() ; e.hasMoreElements() ;)
          {
          	String myName=(String)e.nextElement();
            Constructor myConstructor = Class.forName(myName).getConstructor(new Class[]{Class.forName("colabbs.bbsclient.ConnectionManager")});
            jTabbedPane.add((Panel)myConstructor.newInstance(new Object[]{myConnect}),myName);
          }
          // ~~~~~~~~~~~ 測試區 ~~~~~~~~~~~~~~~~~~
          jTabbedPane.setTitleAt(1, "七嘴八舌討論區");
          jTabbedPane.setTitleAt(2, "電子郵件傳情意");
          // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        }
        catch(Exception e1)
        {
        	e1.printStackTrace();
        }
        break;
      case 1:
        StatusBar.setMessage("查無此帳號");
        break;
      case 2:
        StatusBar.setMessage("密碼輸入錯誤");
        break;
      case 3:
        StatusBar.setMessage("對不起，本站不支援guest帳號!");
        break;
      case 4:
        StatusBar.setMessage("對不起，本站目前不提供新使用者註冊!");
        break;
      case 5:
        StatusBar.setMessage("對不起，本站註冊人數已滿!");
        break;
      case 6:
        StatusBar.setMessage("對不起，這個帳號已經有人使用!");
        break;
    }
//    System.out.println(theCmd.State);
  }

  public void initCmdTable()
  {
    System.out.println("initializing command table....");
    try
    {
      myConnect.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.LoginState"),new CmdItem(this.getClass().getMethod("checkLoginState",new Class[]{/*Class.forName("colabbs.bbsclient.ConnectionManager"),*/Class.forName("colabbs.bbstp.LoginState")}),this));
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
    catch(NoSuchMethodException e)
    {
      e.printStackTrace();
    }
  }

  public void initClientPlasmid(InputStream is)
  {
		try
		{
    	class FrameCloseListener extends WindowAdapter
      {
      	private JFrame closeFrame=null;

        public FrameCloseListener(JFrame theFrame)
        {
        	closeFrame=theFrame;
        }

        public void windowClosing(WindowEvent e)
        {
        	if(closeFrame!=null)
          {
          	AllFrames.removeElement(closeFrame);
            closeFrame.dispose();
          }
        }
      }

      class MultiModuleListener implements java.awt.event.ActionListener
      {
        private Class myModuleClass=null;

        public MultiModuleListener(Class theModuleClass)
        {
          myModuleClass=theModuleClass;
        }

        public void actionPerformed(ActionEvent e)
        {
          try
          {
            Constructor myConstructor = myModuleClass.getConstructor(new Class[]{Class.forName("colabbs.bbsclient.ConnectionManager")});
            MultiModule tmp=(MultiModule)myConstructor.newInstance(new Object[]{myConnect});
            String myName=tmp.getMyName();
            JFrame myFrame=new JFrame(myName);
            myFrame.setSize(600,430);
            myFrame.setLayout(new BorderLayout());
            myFrame.getContentPane().add(tmp,"Center");
            myFrame.addWindowListener(new FrameCloseListener(myFrame));
            AllFrames.addElement(myFrame);
            myFrame.show();
//            System.out.println(myName);
/*            funcWins.put(myName,tmp);

            funcPanel.add(myName,tmp);
            funcPanel.doLayout();
            ((CardLayout)funcPanel.getLayout()).show(funcPanel,myName);
            lstWindows.add(myName);
            lstWindows.select(lstWindows.getItemCount()-1);
            if(lstWindows.getSelectedIndex()!=-1)
            {
              btnTearOff.setEnabled(true);
              btnCloseWin.setEnabled(true);
            }
            else
            {
              btnTearOff.setEnabled(false);
              btnCloseWin.setEnabled(false);
            }*/
          }
          catch(Exception e1)
          {
            e1.printStackTrace();
          }
        }
      }

      class UniModuleListener implements java.awt.event.ActionListener
      {
      	private JFrame myFrame=null;
        private Class myModuleClass=null;
        private UniModule myObject=null;

        public UniModuleListener(Class theModuleClass)
        {
          myModuleClass=theModuleClass;
        }

        public void actionPerformed(ActionEvent e)
        {
          try
          {
            if(myObject==null)
            {
              Constructor myConstructor = myModuleClass.getConstructor(new Class[]{Class.forName("colabbs.bbsclient.ConnectionManager")});
              myObject=(UniModule)myConstructor.newInstance(new Object[]{myConnect});
            }
            String myName=myObject.getMyName();
            if(myFrame==null)
            {
            	myFrame=new JFrame(myName);
	            myFrame.setSize(600,430);
  	          myFrame.getContentPane().setLayout(new BorderLayout());
    	        myFrame.getContentPane().add(myObject,"Center");
	            myFrame.addWindowListener(new WindowAdapter()
  	          {
						    public void windowClosing(WindowEvent e)
						    {
        	      	if(myFrame!=null)
          	      {
	          	    	myFrame.setVisible(false);
                	}
	              }
  	          });
	            AllFrames.addElement(myFrame);
      	      myFrame.setVisible(true);
            }
            else
            {
            	myFrame.toFront();
            }
/*
            funcWins.put(myName,myObject);
            funcPanel.add(myName,myObject);
            funcPanel.doLayout();
            ((CardLayout)funcPanel.getLayout()).show(funcPanel,myName);
            lstWindows.add(myName);
            lstWindows.select(lstWindows.getItemCount()-1);
            if(lstWindows.getSelectedIndex()!=-1)
            {
              btnTearOff.setEnabled(true);
              btnCloseWin.setEnabled(true);
            }
            else
            {
              btnTearOff.setEnabled(false);
              btnCloseWin.setEnabled(false);
            }*/
          }
          catch(Exception e1)
          {
            e1.printStackTrace();
          }
        }
      }

  	  Class[] ArgVoidClass=new Class[0];
  	  Object[] ArgVoidObject=new Object[0];
      Class theMultiModuleClass=Class.forName("colabbs.bbsclient.FunctionModule");
      Class theUniModuleClass=Class.forName("colabbs.bbsclient.UniModule");

			BufferedReader in=new BufferedReader(new InputStreamReader(is));
			System.out.println("Loading client plasmid....");
			String name=null;
			do
			{
				name=in.readLine();
				try
				{
					if(name!=null)
					{
						name = name.trim();
						if (name.charAt(0) != '#')
						{
							Class myClass=Class.forName(name);
//							System.out.println(name);
							MenuItem myItem=(MenuItem)myClass.getMethod("getFunctionItem",ArgVoidClass).invoke(null,ArgVoidObject);
              if(theMultiModuleClass.isAssignableFrom(myClass))
              {
//                System.out.println("colabbs.bbsclient.MultiModule");
                myItem.addActionListener(new MultiModuleListener(myClass));
              }
              else if(theUniModuleClass.isAssignableFrom(myClass))
              {
//                System.out.println("colabbs.bbsclient.UniModule");
                myItem.addActionListener(new UniModuleListener(myClass));
              }
//*************************************************
              popupMenu1.insert(myItem,0);
//*************************************************
							allPlasmids.addElement(name);
						}
					}
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			}while(name!=null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    catch(ClassNotFoundException e)
    {
			e.printStackTrace();
    }
  }

  void menuItem3_actionPerformed(ActionEvent e)
  {
  	SwingMakeSureDialog makeSure=new SwingMakeSureDialog(new Frame(),"離開");
    makeSure.setMessage("您確定要離開嗎?");
    makeSure.show();
    if(makeSure.isSure())
    {
			for (Enumeration allFrame = AllFrames.elements();allFrame.hasMoreElements();)
         ((Frame)allFrame.nextElement()).dispose();
      AllFrames.removeAllElements();
	    myConnect.close();
  	  myConnect.myCmdTable=new CmdTable();
    	initCmdTable();
	    swingLoginPanel1.setEnabled(true);
  	  swingLoginPanel1.clear();
    	myBBSCQ.setVisible(false);
	  	//((CardLayout)panel4.getLayout()).show(panel4,"logoPanel1");
      jTabbedPane.setSelectedIndex(0);
	    ((CardLayout)funcPanel.getLayout()).show(funcPanel,"swingLoginPanel1");
    }
  }
  /*
	void button1_actionPerformed(ActionEvent e)
	{
  	((CardLayout)panel4.getLayout()).show(panel4,"colabbs.bbsclient.awt.BoardFunction");
	}

	void button2_actionPerformed(ActionEvent e)
	{
  	((CardLayout)panel4.getLayout()).show(panel4,"colabbs.bbsclient.awt.MailFunction");
	}

	void button3_actionPerformed(ActionEvent e)
	{
		//talk
	}

	void button4_actionPerformed(ActionEvent e)
	{
		//chat
	}

	void button5_actionPerformed(ActionEvent e)
	{
		//xfile
	}

	void button6_actionPerformed(ActionEvent e)
	{
		//setup
	}
  */
}
