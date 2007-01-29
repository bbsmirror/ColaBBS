package colabbs.bbsclient.swing;

import java.awt.*;
//import colabbs.bbsclient.FunctionModule;
import colabbs.bbsclient.ConnectionManager;
import colabbs.bbsclient.CmdTableItem;
import colabbs.bbsclient.MultiModule;
import colabbs.bbsclient.awt.ArticleCmdEvent;
import colabbs.bbsclient.awt.ArticleCmdListener;
import java.awt.event.*;

public class SwingBoardFunction extends MultiModule
{
	//
  BorderLayout borderLayout1 = new BorderLayout();
  CheckboxGroup checkboxGroup1 = new CheckboxGroup();
  Panel mainPanel = new Panel();
  CardLayout cardLayout1 = new CardLayout();
  SwingBoardLister boardLister1 = null;
  SwingPostLister postLister1 = null;
	Panel boardPanel = new Panel();
	GridLayout gridLayout1 = new GridLayout();
	Panel ViewArticle = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	SwingTextViewer myArticle = new SwingTextViewer();
	Panel panel1 = new Panel();
	Button button1 = new Button();

  public SwingBoardFunction(ConnectionManager theConnection)
  {
    super(theConnection);
    myName="討論區視窗";
    boardLister1=new SwingBoardLister(theConnection);
    postLister1=new SwingPostLister(theConnection);
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
		postLister1.addArticleCmdListener(new colabbs.bbsclient.awt.ArticleCmdListener()
		{

			public void ReadArticle(ArticleCmdEvent e)
			{
				postLister1_ReadArticle(e);
			}

			public void SendNewArticle(ArticleCmdEvent e)
			{
			}

			public void ReplyArticle(ArticleCmdEvent e)
			{
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

	void postLister1_ReadArticle(ArticleCmdEvent e)
	{
    ((CardLayout)mainPanel.getLayout()).show(mainPanel,"ViewArticle");
    myArticle.ReadPost(myConnection.getAddress(),myConnection.getClientType(),myConnection.getUserName(),myConnection.getPassWord(),boardLister1.getBoardName(),postLister1.getSelectedIndex());
	}
}
