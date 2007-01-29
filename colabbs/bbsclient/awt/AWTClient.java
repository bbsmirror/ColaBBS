package colabbs.bbsclient.awt;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.lang.reflect.*;

import colabbs.bbsclient.*;
import colabbs.bbstp.*;
//import com.borland.jbcl.layout.*;

public class AWTClient extends Applet
{
// My own field
	public String UserName;
  public String PassWord;
  private static URL myCodeBase=null;
  private Toolkit myToolkit=null;
  private Vector AllFrames=new Vector();
  private Vector allPlasmids=new Vector();
  private BBSCQ myBBSCQ=new BBSCQ();
  private Image myLogo=null;
  private ConnectionManager myConnect=new ConnectionManager();
// end of my own field.
  boolean isStandalone = false;
  BorderLayout borderLayout1 = new BorderLayout();
  Panel panel3 = new Panel();
  BorderLayout borderLayout2 = new BorderLayout();
  Panel panel1 = new Panel();
  BorderLayout borderLayout3 = new BorderLayout();
  Label label2 = new Label();
  Panel funcPanel = new Panel();
  AppletLoginPanel appletLoginPanel1 = new AppletLoginPanel();
  CardLayout cardLayout1 = new CardLayout();
  Panel loginokpanel = new Panel();
  BorderLayout borderLayout5 = new BorderLayout();
  ImagePanel logoPanel1 = new ImagePanel();
	Panel panel2 = new Panel();
	FlowLayout flowLayout1 = new FlowLayout();
	Button button5 = new Button();
	Button button7 = new Button();
	Button button6 = new Button();
	Panel panel4 = new Panel();
	CardLayout cardLayout2 = new CardLayout();
	Menu menu1 = new Menu();
	MenuItem menuItem1 = new MenuItem();
	MenuItem menuItem2 = new MenuItem();
	MenuItem menuItem3 = new MenuItem();
	PopupMenu popupMenu1 = new PopupMenu();

  //Get a parameter value
  public String getParameter(String key, String def)
  {
    return isStandalone ? System.getProperty(key, def) :
      (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public AWTClient()
  {
  }

  //Initialize the applet
  public void init()
  {
  	if(myCodeBase==null)
    	myCodeBase=getCodeBase();
    myToolkit=getToolkit();
    try
    {
      jbInit();
//*******************************************
      // My init....
      initCmdTable();
      myBBSCQ.setStartMenu(popupMenu1);
      new LabelStatusBar(label2);
      System.out.println(myCodeBase);
      if(myCodeBase!=null)
      {
//        myLogo=this.getImage(myCodeBase,"icons/ActColaBBS.GIF");
				myLogo=myToolkit.getImage(new URL(myCodeBase,"icons/ActColaBBS.GIF"));
        initClientPlasmid((new URL(myCodeBase,"ClientPlasmid.INI")).openStream());
        myBBSCQ.setColaIcon(myToolkit.getImage(new URL(myCodeBase,"icons/ColaIconSmall.GIF")));
      }
      appletLoginPanel1.setMyLogo(myLogo);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception
  {
    this.setSize(new Dimension(600,450));
    this.setLayout(borderLayout1);
    panel3.setLayout(borderLayout2);
    panel1.setLayout(borderLayout3);
    panel3.setBackground(new java.awt.Color(157, 185, 200));
    label2.setBackground(new java.awt.Color(174, 168, 217));
    label2.setText("請輸入您的帳號(勿用中文)和密碼以登入系統，參觀請用guest");
    funcPanel.setLayout(cardLayout1);
    appletLoginPanel1.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        appletLoginPanel1_actionPerformed(e);
      }
    });
    funcPanel.setBackground(new java.awt.Color(208, 227, 211));
    loginokpanel.setLayout(borderLayout5);
		panel2.setLayout(flowLayout1);
		flowLayout1.setAlignment(FlowLayout.LEFT);
		flowLayout1.setHgap(0);
		flowLayout1.setVgap(0);
		panel2.setBackground(new java.awt.Color(157, 185, 200));
		button5.setLabel("個人機密檔案");
		button5.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button5_actionPerformed(e);
			}
		});
		button7.setLabel("揮一揮衣袖");
		button7.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				menuItem3_actionPerformed(e);
			}
		});
		button6.setLabel("選項");
		button6.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				button6_actionPerformed(e);
			}
		});
		panel4.setLayout(cardLayout2);
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
		this.add(panel1, BorderLayout.CENTER);
    panel1.add(panel3, BorderLayout.EAST);
    panel1.add(label2, BorderLayout.SOUTH);
    panel1.add(funcPanel, BorderLayout.CENTER);
    funcPanel.add(appletLoginPanel1, "appletLoginPanel1");
    funcPanel.add(loginokpanel, "loginokpanel");
		loginokpanel.add(panel2, BorderLayout.NORTH);
		panel2.add(button5, null);
		panel2.add(button6, null);
		panel2.add(button7, null);
		loginokpanel.add(panel4, BorderLayout.CENTER);
		panel4.add(logoPanel1, "logoPanel1");
		menu1.add(menuItem1);
		menu1.add(menuItem2);
		popupMenu1.addSeparator();
		popupMenu1.add(menuItem3);
		popupMenu1.add(menu1);
  }

  //Start the applet
  public void start()
  {
  }

  //Stop the applet
  public void stop()
  {
  }

  //Destroy the applet
  public void destroy()
  {
  }

  //Get Applet information
  public String getAppletInfo()
  {
    return "Cola BBS Client Applet AWT Version 1.0 by yhwu at infoX (C)1999-1999";
  }

  //Get parameter info
  public String[][] getParameterInfo()
  {
    return null;
  }

  //Main method
  public static void main(String[] args)
  {
    AWTClient applet = new AWTClient();
    applet.isStandalone = true;
    Frame frame = new Frame();
    frame.setTitle("Applet Frame");
    frame.add(applet, BorderLayout.CENTER);
    InputAddressDialog iaf=new InputAddressDialog(frame);
    iaf.setVisible(true);
//    myCodeBase=new URL("file:/C:/JBuilder3/myprojects/ColaBBS/"); //for debug!
		if(iaf.getAddress()==null)
    	return;
		try
    {
	    myCodeBase=new URL("http://"+iaf.getAddress()+"/");
    }
    catch(Exception e)
    {
    	myCodeBase=applet.getCodeBase();
    }
    applet.init();
    applet.start();
    frame.setSize(600,470);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
	  class MainFrameListener extends WindowAdapter
  	{
    	private AWTClient myClient=null;
  		private Frame myFrame=null;

	  	public MainFrameListener(AWTClient theClient,Frame theFrame)
  	  {
      	myClient=theClient;
    		myFrame=theFrame;
	    }
  	  public void windowClosing(WindowEvent e)
    	{
      	if(myClient.myBBSCQ.isVisible())
	    		myFrame.dispose();
        else
        	System.exit(0);
	    }
  	}
    frame.addWindowListener(new MainFrameListener(applet,frame));
/*    frame.addWindowListener(new java.awt.event.WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
      	frame.dispose();
      }
    });*/
  }

  void appletLoginPanel1_actionPerformed(ActionEvent e)
  {
//    System.out.println("appletLoginPanel1_actionPerformed");
  	if(e.getActionCommand().length()==0)
	  {
      label2.setText("請輸入您的帳號(勿用中文)和密碼以登入系統");
  		return;
	  }

		UserName=appletLoginPanel1.getUserName();
    PassWord=appletLoginPanel1.getPassWord();
//    myConnect.setAddress("127.0.0.1"); //for debug!
    myConnect.setAddress(myCodeBase.getHost());
    myConnect.setClientType(1); //1:Applet
    myConnect.setUserName(UserName);
    myConnect.setPassWord(PassWord);
    myConnect.setNewID(appletLoginPanel1.isNewID());
    myConnect.setEmail(appletLoginPanel1.getEmail());
    myConnect.connect(); //若已有連線則強迫中斷重連!
//  		mySwingChat.myCodeBase=myCodeBase.toString();
//	  	mySwingChat=new SwingChat(oos,getJTextField1().getText());
//  		mySwingChat.setVisible(true);
  }

  public void checkLoginState(/*ConnectionManager theConnection,*/LoginState theCmd)
  {
  	class TabModuleListener implements ActionListener
    {
    	private String myName;
    	public TabModuleListener(String theName)
      {
      	myName=theName;
      }

    	public void actionPerformed(ActionEvent e)
      {
      	((CardLayout)panel4.getLayout()).show(panel4,myName);
      }
    }
/*    btnBoards.setEnabled(true);
    btnUsers.setEnabled(true);
    btnSetup.setEnabled(true);*/
    switch(theCmd.State)
    {
      case 0:
        StatusBar.setMessage("登入成功");
	      myBBSCQ.addConnectionManager(myConnect);
	      myBBSCQ.setVisible(true);
        appletLoginPanel1.setEnabled(false);
        if(myCodeBase!=null)
        {
        	try
          {
	          logoPanel1.setMyImage(myToolkit.getImage(new URL(myCodeBase,"icons/infoX.gif")));
          }
          catch(MalformedURLException e){}
        }
        ((CardLayout)funcPanel.getLayout()).show(funcPanel,"loginokpanel");
        //*********************
        try
        {
        	panel2.removeAll();
					for (Enumeration e = allPlasmids.elements() ; e.hasMoreElements() ;)
          {
          	String myName=(String)e.nextElement();
            Constructor myConstructor = Class.forName(myName).getConstructor(new Class[]{Class.forName("colabbs.bbsclient.ConnectionManager")});
            Object myObject=myConstructor.newInstance(new Object[]{myConnect});
            if(myObject instanceof TabModule)
            {
	            Button myButton=new Button(((TabModule)myObject).getTabName());
              myButton.addActionListener(new TabModuleListener(myName));
              panel2.add(myButton);
            }
            panel4.add((Panel)myObject,myName);
          }
//          panel2.add(button4); //will delete latter!
          panel2.add(button5); //will delete latter!
          panel2.add(button6); //will delete latter!
          panel2.add(button7);
          panel2.doLayout();
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
      	private Frame closeFrame=null;

        public FrameCloseListener(Frame theFrame)
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
            Frame myFrame=new Frame(myName);
            myFrame.setSize(600,430);
            myFrame.setLayout(new BorderLayout());
            myFrame.add(tmp,"Center");
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
      	private Frame myFrame=null;
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
            	myFrame=new Frame(myName);
	            myFrame.setSize(600,430);
  	          myFrame.setLayout(new BorderLayout());
    	        myFrame.add(myObject,"Center");
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
      	      myFrame.show();
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
  	MakeSureDialog makeSure=new MakeSureDialog(new Frame(),"離開");
    makeSure.setMessage("您確定要離開嗎?");
    makeSure.show();
    if(makeSure.isSure())
    {
			for (Enumeration allFrame = AllFrames.elements();allFrame.hasMoreElements();)
         ((Frame)allFrame.nextElement()).dispose();
      AllFrames.removeAllElements();
    	myBBSCQ.setVisible(false);
      myBBSCQ.removeConnectionManager(myConnect);
	    myConnect.close();
  	  myConnect.myCmdTable=new CmdTable();
    	initCmdTable();
	    appletLoginPanel1.setEnabled(true);
  	  appletLoginPanel1.clear();
	  	((CardLayout)panel4.getLayout()).show(panel4,"logoPanel1");
	    ((CardLayout)funcPanel.getLayout()).show(funcPanel,"appletLoginPanel1");
			//
      panel2.removeAll();
			panel2.add(button5, null);
			panel2.add(button6, null);
			panel2.add(button7, null);
      panel4.removeAll();
			panel4.add(logoPanel1, "logoPanel1");
    }
  }

/*	void button1_actionPerformed(ActionEvent e)
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
  	((CardLayout)panel4.getLayout()).show(panel4,"colabbs.bbsclient.awt.UserFunction");
	}

	void button4_actionPerformed(ActionEvent e)
	{
		//chat
  	((CardLayout)panel4.getLayout()).show(panel4,"colabbs.bbsclient.awt.ChatFunction");
	}*/

	void button5_actionPerformed(ActionEvent e)
	{
		//xfile
	}

	void button6_actionPerformed(ActionEvent e)
	{
		//setup
	}
}
