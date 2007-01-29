package colabbs.bbsclient.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.IconWindow;

public class SwingBBSCQ extends JFrame implements Runnable{
  private Thread myThread=null;
  private IconWindow myIconWin=new IconWindow();

  JPanel panel1 = new JPanel();
	JButton button1 = new JButton();
	BorderLayout borderLayout2 = new BorderLayout();
	JComboBox choice1 = new JComboBox();
	JList list1 = new JList();
	java.awt.Image colaIcon;
	JButton button2 = new JButton();
	private PopupMenu startMenu=null;

  public SwingBBSCQ()
	{
  	Toolkit myToolkit=this.getToolkit();
    setLocation(myToolkit.getScreenSize().width-141,myToolkit.getScreenSize().height-281);

  	setSize(140,240);
		try
		{
			jbInit();
			list1.setBackground(SystemColor.control);

      choice1.addItem("只列出上站好友");
      choice1.addItem("列出所有好友");
			myIconWin.addMouseListener(new java.awt.event.MouseAdapter()
			{
				public void mouseClicked(MouseEvent e)
				{
			  	if(startMenu!=null)
	  				startMenu.show(panel1,button1.getBounds().x,button1.getBounds().y);
				}
			});
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

  public void show()
  {
  	super.show();
		if(myThread==null)
    {
	    myThread=new Thread(this);
      myThread.start();
    }
  }

	private void jbInit() throws Exception
	{
  	if(ClientUtils.isMS())
			button1.setText("  ola");
  	else
			button1.setText("     ola");
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
		button2.setText("呼叫器狀態");
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
		});
		this.getContentPane().add(panel1, BorderLayout.SOUTH);
		panel1.add(button1, BorderLayout.WEST);
		panel1.add(button2, BorderLayout.CENTER);
		this.getContentPane().add(list1, BorderLayout.CENTER);
		this.getContentPane().add(choice1, BorderLayout.NORTH);
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

	public void run()
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
          if(!myIconWin.isShowing())
	  	  		myIconWin.show();
          else
          	myIconWin.toFront();
	  	  }
        Thread.sleep(100);
	    }
    }
    catch(InterruptedException e){}
	}

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
    panel1.add(startMenu);
	}

	public PopupMenu getStartMenu()
	{
		return startMenu;
	}

  public static void main(String args[]){
    JFrame bbscq = new SwingBBSCQ();
    bbscq.pack();
    bbscq.setVisible(true);
  }
}