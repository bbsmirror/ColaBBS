
//Title:
//Version:
//Copyright:
//Author:
//Company:
//Description:

package colabbs.bbsclient.awt;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

//import colabbs.BoardItem;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.UniModule;
import colabbs.bbsclient.ConnectionManager;

import colabbs.bbstp.board.*;

public class BoardLister extends UniModule
{
	private Thread lastTimer=null;
  private Vector myBoardList=null;
  private Vector listedBoards=new Vector();
//***************
  Panel panel1 = new Panel();
  Label label1 = new Label();
  FlowLayout flowLayout1 = new FlowLayout();
  TextField textField1 = new TextField();
  Panel panel3 = new Panel();
  BorderLayout borderLayout2 = new BorderLayout();
  Checkbox checkbox1 = new Checkbox();
  Checkbox checkbox2 = new Checkbox();
  Panel panel10 = new Panel();
  java.awt.List lstBoard = new java.awt.List();
  Panel panel6 = new Panel();
  CheckboxGroup checkboxGroup1 = new CheckboxGroup();
  FlowLayout flowLayout3 = new FlowLayout();
  Panel panel5 = new Panel();
  BorderLayout borderLayout4 = new BorderLayout();
  Panel panel9 = new Panel();
  Panel panel11 = new Panel();
	BorderLayout borderLayout5 = new BorderLayout();
	BorderLayout borderLayout1 = new BorderLayout();
	private transient Vector itemListeners;
	private String boardName=null;
	Panel panel2 = new Panel();
	Label label2 = new Label();
	FlowLayout flowLayout2 = new FlowLayout();

  public BoardLister(ConnectionManager theConnection)
  {
    super(theConnection);
//    postLister1=new PostLister(myConnection);
    try
    {
      jbInit();
	    myConnection.myCmdTable.registerCmd(Class.forName("colabbs.bbstp.board.BBSTPBoardItem"),new CmdItem(this.getClass().getMethod("addBoardItem",new Class[]{Class.forName("colabbs.bbstp.board.BBSTPBoardItem")}),this));
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    doList();
  }

  public synchronized void doList()
  {
    try
    {
      myBoardList=(Vector)myConnection.getData("BoardLister_myBoardList");
      if(myBoardList==null)
      {
        myBoardList=new Vector();
        myConnection.setData("BoardLister_myBoardList",myBoardList);
        myConnection.sendCmd(this,new BBSTPListBoards());
      }
      else
      {
        int max=myBoardList.size();
        for(int i=0;i<max;i++)
        {
          BBSTPBoardItem bi=(BBSTPBoardItem)myBoardList.elementAt(i);
			    lstBoard.add((bi.HaveNew?"ˇ ":"   ")+ClientUtils.byte2String(ClientUtils.Cut(bi.CGroup,6))+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.byte2String(ClientUtils.Cut(bi.BM, 20)) + " " + ClientUtils.byte2String(bi.Title));
          listedBoards.addElement(bi);
        }
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    this.setLayout(borderLayout1);
    label1.setText("搜尋討論區");
    panel1.setLayout(flowLayout1);
    textField1.setColumns(10);
		textField1.addActionListener(new java.awt.event.ActionListener()
		{

			public void actionPerformed(ActionEvent e)
			{
				textField1_actionPerformed(e);
			}
		});
    textField1.addTextListener(new java.awt.event.TextListener()
    {

      public void textValueChanged(TextEvent e)
      {
        textField1_textValueChanged(e);
      }
    });
    panel3.setLayout(borderLayout2);
    checkbox1.setCheckboxGroup(checkboxGroup1);
    checkbox1.setLabel("英文版名");
    checkbox1.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkbox1_itemStateChanged(e);
      }
    });
    checkbox2.setCheckboxGroup(checkboxGroup1);
    checkbox2.setLabel("中文版名");
    checkbox2.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkbox2_itemStateChanged(e);
      }
    });
    panel10.setLayout(borderLayout4);
    checkboxGroup1.setSelectedCheckbox(checkbox1);
    panel6.setLayout(flowLayout3);
    panel5.setLayout(borderLayout5);
		lstBoard.addItemListener(new java.awt.event.ItemListener()
		{

			public void itemStateChanged(ItemEvent e)
			{
				lstBoard_itemStateChanged(e);
			}
		});
		lstBoard.setFont(new java.awt.Font("Monospaced", 0, 12));
		label2.setFont(new java.awt.Font("Monospaced", 0, 12));
		label2.setText("新 分 類  討論區英文名稱    版                主 討 論 區 中 文 名 稱");
		panel2.setLayout(flowLayout2);
		flowLayout2.setAlignment(FlowLayout.LEFT);
		flowLayout2.setHgap(0);
		flowLayout2.setVgap(0);
		panel2.setBackground(new java.awt.Color(157, 185, 200));
		this.add(panel3, BorderLayout.CENTER);
		panel3.add(panel10, BorderLayout.CENTER);
		panel10.add(panel5, BorderLayout.CENTER);
		panel5.add(lstBoard, BorderLayout.CENTER);
		panel5.add(panel2, BorderLayout.NORTH);
		panel2.add(label2, null);
		panel10.add(panel9, BorderLayout.WEST);
		panel10.add(panel11, BorderLayout.EAST);
		panel3.add(panel6, BorderLayout.SOUTH);
		this.add(panel1, BorderLayout.NORTH);
		panel1.add(label1, null);
		panel1.add(checkbox1, null);
		panel1.add(checkbox2, null);
		panel1.add(textField1, null);
  }

  void checkbox2_itemStateChanged(ItemEvent e)
  {
    if(e.getStateChange()==ItemEvent.SELECTED)
    {
      textField1.setText("");
    }
  }

  void checkbox1_itemStateChanged(ItemEvent e)
  {
    if(e.getStateChange()==ItemEvent.SELECTED)
    {
      textField1.setText("");
    }
  }

  public synchronized void addBoardItem(BBSTPBoardItem bi)
  {
  	System.out.println("addBoardItem"+bi.CGroup);
//    bi.BM=ClientUtils.byte2String(bi.BM);
    myBoardList.addElement(bi);
    listedBoards.addElement(bi);
    lstBoard.add((bi.HaveNew?"ˇ ":"   ")+ClientUtils.byte2String(ClientUtils.Cut(bi.CGroup,6))+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.byte2String(ClientUtils.Cut(bi.BM, 20)) + " " + ClientUtils.byte2String(bi.Title));
//    lstBoard.add(bi.Name+bi.Title);
  }

  synchronized void textField1_textValueChanged(TextEvent e)
  {
    String check=textField1.getText();

  	//for IE!!
		for(int i=lstBoard.getItemCount()-1;i>=0;i--)
	    lstBoard.remove(i);
    //
//    lstBoard.removeAll();
    listedBoards.removeAllElements();
    if(checkbox1.getState())
    {
      int max=myBoardList.size();
      for(int i=0;i<max;i++)
      {
        BBSTPBoardItem bi=(BBSTPBoardItem)myBoardList.elementAt(i);
        String me=bi.Name;
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
				    lstBoard.add((bi.HaveNew?"ˇ ":"   ")+ClientUtils.byte2String(ClientUtils.Cut(bi.CGroup,6))+" "+ClientUtils.Cut(me, 18)+ClientUtils.byte2String(ClientUtils.Cut(bi.BM, 20)) + " " + ClientUtils.byte2String(bi.Title));
            listedBoards.addElement(bi);
          }
        }
      }
    }
    else if(checkbox2.getState())
    {
      int max=myBoardList.size();
      for(int i=0;i<max;i++)
      {
        BBSTPBoardItem bi=(BBSTPBoardItem)myBoardList.elementAt(i);
        String me=ClientUtils.byte2String(bi.Title);
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
				    lstBoard.add((bi.HaveNew?"ˇ ":"   ")+ClientUtils.byte2String(ClientUtils.Cut(bi.CGroup,6))+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.byte2String(ClientUtils.Cut(bi.BM, 20)) + " " + ClientUtils.byte2String(bi.Title));
            listedBoards.addElement(bi);
          }
        }
      }
    }
  }

/*  void button1_actionPerformed(ActionEvent e)
  {
    System.out.println("post list....");
    String boardName="";
    if(checkbox1.getState())
    {
      if(lstBoard.getSelectedIndex()==-1)
      {
        if(listedBoards.size()==1)
          boardName=((BBSTPBoardItem)listedBoards.elementAt(0)).Name;
      }
      else
        boardName=((BBSTPBoardItem)listedBoards.elementAt(lstBoard.getSelectedIndex())).Name;
    }
    else
    {
      if(lstBoard.getSelectedIndex()==-1)
      {
        if(listedBoards.size()==1)
          boardName=((BBSTPBoardItem)listedBoards.elementAt(0)).Name;
      }
      else
        boardName=((BBSTPBoardItem)listedBoards.elementAt(lstBoard.getSelectedIndex())).Name;
    }
    if(boardName.length()==0)
      return;
//    postLister1.doList(boardName);
//    ((CardLayout)getLayout()).show(this,"postPanel");
  }

  void lstBoard_actionPerformed(ActionEvent e)
  {
    button1_actionPerformed(e);
  }

  void lstBoard_itemStateChanged(ItemEvent e)
  {
    int theIndex=lstBoard.getSelectedIndex();
    BBSTPBoardItem bi=(BBSTPBoardItem)listedBoards.elementAt(theIndex);
    BBSTPBoardItem bi=null;
    if(checkbox1.getState())
    {
      bi=(BBSTPBoardItem)myBoardList.elementAt(lstEnglish.getSelectedIndex());
    }
    else
    {
      bi=(BBSTPBoardItem)myBoardList.elementAt(lstChinese.getSelectedIndex());
    }
    if(bi!=null)
    {
      textField2.setText(bi.Name);
      textField3.setText(bi.Title);
      textField4.setText(bi.BM);
      textField5.setText(bi.CGroup);
      textField6.setText(bi.Anonymous?"是":"否");
      textField7.setText(bi.JunkBoard?"是":"否");
      lstChinese.select(theIndex);
    }
  }*/

  class Timer extends Thread
  {
   	public boolean keepRun=true;
    private ItemEvent myE=null;
   	private long myTime=0;

   	public Timer(long theTime,ItemEvent theE)
    {
     	super();
      myTime=theTime;
      myE=theE;
      start();
    }

    public void run()
    {
	 	  ClientUtils.Delay(myTime);
      if(keepRun)
		    fireItemStateChanged(myE);
    }
  }

  void lstBoard_itemStateChanged(ItemEvent e)
  {
  	try
    {
	    if(lstBoard.getSelectedIndex()==-1)
  	  {
    		if(listedBoards.size()==1)
      		boardName=((BBSTPBoardItem)listedBoards.elementAt(0)).Name;
	      else
  	    	return;
    	}
	    else
  	  	boardName=((BBSTPBoardItem)listedBoards.elementAt(lstBoard.getSelectedIndex())).Name;
    	if(boardName.length()==0)
      	return;
      if(lastTimer!=null)
      	((Timer)lastTimer).keepRun=false;
      lastTimer=new Timer(200,e);
    }
    finally
    {
    }
  }

	public synchronized void removeItemListener(ItemListener l)
	{
		if(itemListeners != null && itemListeners.contains(l))
		{
			Vector v = (Vector) itemListeners.clone();
			v.removeElement(l);
			itemListeners = v;
		}
	}

	public synchronized void addItemListener(ItemListener l)
	{
		Vector v = itemListeners == null ? new Vector(2) : (Vector) itemListeners.clone();
		if(!v.contains(l))
		{
			v.addElement(l);
			itemListeners = v;
		}
	}

	protected void fireItemStateChanged(ItemEvent e)
	{
		if(itemListeners != null)
		{
			Vector listeners = itemListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((ItemListener) listeners.elementAt(i)).itemStateChanged(e);
			}
		}
	}

	public String getBoardName()
	{
		return boardName;
	}

  public BBSTPBoardItem getSelectedItem()
  {
  	if(lstBoard.getSelectedIndex()!=-1)
  	  	return (BBSTPBoardItem)listedBoards.elementAt(lstBoard.getSelectedIndex());
  	return null;
  }

	synchronized void textField1_actionPerformed(ActionEvent e)
	{
		lstBoard_itemStateChanged(new ItemEvent(lstBoard,ItemEvent.ITEM_STATE_CHANGED,this,ItemEvent.SELECTED));
	}

/*  void lstChinese_itemStateChanged(ItemEvent e)
  {
    int theIndex=lstChinese.getSelectedIndex();
    BBSTPBoardItem bi=(BBSTPBoardItem)listedBoards.elementAt(theIndex);

    if(bi!=null)
    {
      textField2.setText(bi.Name);
      textField3.setText(bi.Title);
      textField4.setText(bi.BM);
      textField5.setText(bi.CGroup);
      textField6.setText(bi.Anonymous?"是":"否");
      textField7.setText(bi.JunkBoard?"是":"否");
      lstBoard.select(theIndex);
    }
  }

  void lstChinese_actionPerformed(ActionEvent e)
  {
//    lstEnglish.isSelected()
    button1_actionPerformed(e);
  }*/
  public String getMyClassName()
  {
    return getClass().getName();
  }

}
