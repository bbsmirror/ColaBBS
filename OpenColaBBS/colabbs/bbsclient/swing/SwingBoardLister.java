package colabbs.bbsclient.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

//import colabbs.BoardItem;
import colabbs.bbsclient.ClientUtils;
import colabbs.bbsclient.CmdItem;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.UniModule;
import colabbs.bbsclient.ConnectionManager;

import colabbs.bbstp.board.*;

public class SwingBoardLister extends UniModule
{
	private Thread lastTimer=null;
  private Vector myBoardList=null;
  private Vector listedBoards=new Vector();
  private DefaultListModel lstBoardList = new DefaultListModel();
//***************
  JPanel panel1 = new JPanel();
  JLabel label1 = new JLabel();
  FlowLayout flowLayout1 = new FlowLayout();
  JTextField textField1 = new JTextField();
  JPanel panel3 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JCheckBox checkbox1 = new JCheckBox();
  JCheckBox checkbox2 = new JCheckBox();
  JPanel panel10 = new JPanel();
  JList lstBoard = new JList(lstBoardList);
  JPanel panel6 = new JPanel();
  ButtonGroup checkboxGroup1 = new ButtonGroup();
  FlowLayout flowLayout3 = new FlowLayout();
  JPanel panel5 = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JPanel panel9 = new JPanel();
  JPanel panel11 = new JPanel();
	BorderLayout borderLayout5 = new BorderLayout();
	BorderLayout borderLayout1 = new BorderLayout();
	private transient Vector itemListeners;
	private String boardName=null;
	JPanel panel2 = new JPanel();
	JLabel label2 = new JLabel();
	FlowLayout flowLayout2 = new FlowLayout();

  public SwingBoardLister(ConnectionManager theConnection)
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
			    lstBoardList.addElement((bi.HaveNew?"ˇ ":"   ")+ClientUtils.Cut(bi.CGroup,4)+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.Cut(bi.BM, 20) + " " + bi.Title);
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
    lstBoard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    lstBoard.setSelectedIndex(0);
    lstBoard.addListSelectionListener(new ListSelectionListener()
    {

      public void valueChanged(ListSelectionEvent e)
      {
        lstBoard_itemStateChanged(e);
      }

    });
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

    textField1.getDocument().addDocumentListener(new DocumentListener()
    {

      public void changedUpdate(DocumentEvent e){
      }

      public void insertUpdate(DocumentEvent e){
        textField1_textValueChanged();
      }

      public void removeUpdate(DocumentEvent e){
        textField1_textValueChanged();
      }

    });

    panel3.setLayout(borderLayout2);
    checkboxGroup1.add(checkbox1);
    checkbox1.setText("英文版名");
    checkbox1.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkbox1_itemStateChanged(e);
      }
    });
    checkboxGroup1.add(checkbox2);
    checkbox2.setText("中文版名");
    checkbox2.addItemListener(new java.awt.event.ItemListener()
    {

      public void itemStateChanged(ItemEvent e)
      {
        checkbox2_itemStateChanged(e);
      }
    });
    panel10.setLayout(borderLayout4);
    checkbox1.setSelected(true);
    panel6.setLayout(flowLayout3);
    panel5.setLayout(borderLayout5);
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
    bi.Title=ClientUtils.byte2String(bi.Title);
    bi.CGroup=ClientUtils.byte2String(bi.CGroup);
    bi.BM=ClientUtils.byte2String(bi.BM);
    myBoardList.addElement(bi);
    listedBoards.addElement(bi);
    lstBoardList.addElement((bi.HaveNew?"ˇ ":"   ")+ClientUtils.Cut(bi.CGroup,4)+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.Cut(bi.BM, 20) + " " + bi.Title);
//    lstBoard.add(bi.Name+bi.Title);
  }

  synchronized void textField1_textValueChanged()
  {
    String check=textField1.getText();

    lstBoard.removeAll();
    listedBoards.removeAllElements();
    if(checkbox1.getSelectedObjects() != null)
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
				    lstBoardList.addElement((bi.HaveNew?"ˇ ":"   ")+ClientUtils.Cut(bi.CGroup,4)+" "+ClientUtils.Cut(me, 18)+ClientUtils.Cut(bi.BM, 20) + " " + bi.Title);
            listedBoards.addElement(bi);
          }
        }
      }
    }
    else if(checkbox2.getSelectedObjects() != null)
    {
      int max=myBoardList.size();
      for(int i=0;i<max;i++)
      {
        BBSTPBoardItem bi=(BBSTPBoardItem)myBoardList.elementAt(i);
        String me=bi.Title;
        if(check.length()<=me.length())
        {
          if(me.substring(0,check.length()).equalsIgnoreCase(check))
          {
				    lstBoardList.addElement((bi.HaveNew?"ˇ ":"   ")+ClientUtils.Cut(bi.CGroup,4)+" "+ClientUtils.Cut(bi.Name, 18)+ClientUtils.Cut(bi.BM, 20) + " " + me);
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

  void lstBoard_itemStateChanged(ListSelectionEvent e)
  {
    if(e.getValueIsAdjusting() == true)
      return;
  	//for IE!!
  	if(lstBoardList.size()==0)
    	return;
    //
  	class Timer extends Thread
    {
    	public boolean keepRun=true;
      private Object myE=null;
    	private long myTime=0;
      private Hashtable hashtable = new Hashtable();

    	public Timer(long theTime,ItemEvent theE)
      {
      	super();
        myTime=theTime;
        myE=theE;
        createEventTable();
        start();
      }

      public Timer(long theTime,ListSelectionEvent theE)
      {
      	super();
        myTime=theTime;
        myE=theE;
        createEventTable();
        start();
      }

      public void run()
      {
	  	  ClientUtils.Delay(myTime);
        if(keepRun){
          int eindex = ((Integer)hashtable.get(myE.getClass())).intValue();
          switch(eindex){
            case 0:
  			      fireItemStateChanged((ItemEvent)myE);
              break;
            case 1:
              fireItemStateChanged((ListSelectionEvent)myE);
              break;
            default:
          }
        }
      }

      private void createEventTable(){
        hashtable.put((new ItemEvent(null, 0, null, 0)).getClass(), new Integer(0));
        hashtable.put((new ListSelectionEvent(null, 0, 0, false)).getClass(), new Integer(1));
      }
    }

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

  public synchronized void removeItemListener(ListSelectionEvent l)
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

  public synchronized void addItemListener(ListSelectionEvent l)
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

  protected void fireItemStateChanged(ListSelectionEvent e)
	{
		if(itemListeners != null)
		{
			Vector listeners = itemListeners;
			int count = listeners.size();
			for (int i = 0; i < count; i++)
			{
				((ListSelectionListener) listeners.elementAt(i)).valueChanged(e);
			}
		}
	}

	public String getBoardName()
	{
		return boardName;
	}

	synchronized void textField1_actionPerformed(ActionEvent e)
	{
		lstBoard_itemStateChanged(new ListSelectionEvent(lstBoard, 0, 1, false));
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
