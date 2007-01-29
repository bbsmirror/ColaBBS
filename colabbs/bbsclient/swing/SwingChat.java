package colabbs.bbsclient.swing;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.util.*;
import colabbs.bbstp.Login;
import colabbs.bbstp.chat.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import colabbs.bbsclient.ClientUtils;
/**
 * This type was writen by yhwu.
 */
public class SwingChat extends javax.swing.JFrame implements java.awt.event.ActionListener, java.awt.event.WindowListener
{
	public static String myCodeBase="http://cola.twbbs.org/colabbs/";
	private ObjectOutputStream oos = null;
	private StyleContext sc = null;
	private DefaultStyledDocument doc = null;
	private Style theFGColors[] = new Style[8];
	private Style Normal;
	private SwingChatOPFunction opFunction = null;
	private SwingChatActionMenu actionMenu = null;
	private Hashtable Verb1_1 = null, Verb1_2 = null, Verb2 = null, Verb3 = null;
	private javax.swing.JButton ivjActionHelpButton = null;
	private javax.swing.JButton ivjActionsButton = null;
	private javax.swing.JButton ivjHelpButton = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JPanel ivjJFrameContentPane1 = null;
	private javax.swing.JLabel ivjJLabel3 = null;
	private javax.swing.JLabel ivjJLabel4 = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JPanel ivjJPanel2 = null;
	private javax.swing.JPanel ivjJPanel3 = null;
	private javax.swing.JPanel ivjJPanel4 = null;
	private javax.swing.JPanel ivjJPanel5 = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private javax.swing.JTextField ivjJTextField2 = null;
	private javax.swing.JTextField ivjJTextField3 = null;
	private javax.swing.JTextField ivjJTextField4 = null;
	private javax.swing.JTextField ivjJTextField5 = null;
	private javax.swing.JTextPane ivjJTextPane1 = null;
	private javax.swing.JToolBar ivjJToolBar1 = null;
	private javax.swing.JButton ivjListRoomButton = null;
	private javax.swing.JButton ivjListRoomUserButton = null;
	private javax.swing.JButton ivjListUserButton = null;
	private javax.swing.JButton ivjOPHelpButton = null;
	private javax.swing.JButton ivjRoomOPButton = null;
	private javax.swing.JButton ivjSendNoteButton = null;
/**
 * Constructor
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public SwingChat() {
	super();
	initialize();
}
/**
 * This method was writen by yhwu.
 * @param theoos java.io.ObjectOutputStream
 * @param theNick java.lang.String
 */
public SwingChat(ObjectOutputStream theoos, String theNick)
{
	super("Cola Chat Version 1.0");
	initialize();
	oos=theoos;
	try
	{
		oos.writeObject(new Login(2,"guest","",false,""));
		oos.writeObject(new ChatLogin(ClientUtils.string2Byte(theNick)));
		getJTextField5().setText(theNick);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	actionMenu=new SwingChatActionMenu(oos);
	opFunction=new SwingChatOPFunction(oos);
}
/**
 * SwingChat constructor comment.
 * @param title java.lang.String
 */
public SwingChat(String title)
{
	super(title);
	initialize();
}
/**
 * Comment
 */
public void actionHelpButton_ActionPerformed()
{
	try
	{
		if (oos != null)
			oos.writeObject(new ChatHelps(3));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == getJTextField3()) ) {
		connEtoC2(e);
	}
	if ((e.getSource() == getActionsButton()) ) {
		connEtoC3(e);
	}
	if ((e.getSource() == getSendNoteButton()) ) {
		connEtoC4(e);
	}
	if ((e.getSource() == getListUserButton()) ) {
		connEtoC5(e);
	}
	if ((e.getSource() == getListRoomUserButton()) ) {
		connEtoC6(e);
	}
	if ((e.getSource() == getListRoomButton()) ) {
		connEtoC7(e);
	}
	if ((e.getSource() == getRoomOPButton()) ) {
		connEtoC8(e);
	}
	if ((e.getSource() == getHelpButton()) ) {
		connEtoC9(e);
	}
	if ((e.getSource() == getOPHelpButton()) ) {
		connEtoC10(e);
	}
	if ((e.getSource() == getActionHelpButton()) ) {
		connEtoC11(e);
	}
	if ((e.getSource() == getJTextField4()) ) {
		connEtoC12(e);
	}
	if ((e.getSource() == getJTextField2()) ) {
		connEtoC13(e);
	}
	if ((e.getSource() == getJTextField5()) ) {
		connEtoC14(e);
	}
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void actionsButton_ActionPerformed()
{
	actionMenu.setVisible(true);
	return;
}
/**
 * connEtoC1:  (SwingChat.window.windowClosed(java.awt.event.WindowEvent) --> SwingChat.swingChat_WindowClosed()V)
 * @param arg1 java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.WindowEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.swingChat_WindowClosed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC10:  (OPHelpButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.oPHelpButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC10(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.oPHelpButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC11:  (ActionHelpButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.actionHelpButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC11(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.actionHelpButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC12:  (JTextField4.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.jTextField4_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC12(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField4_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC13:  (JTextField2.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.jTextField2_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC13(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField2_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC14:  (JTextField5.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.jTextField5_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC14(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField5_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (JTextField3.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.jTextField3_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField3_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (ActionsButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.actionsButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.actionsButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (SendNoteButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.sendNoteButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.sendNoteButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (ListUserButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.listUserButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.listUserButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC6:  (ListRoomUserButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.listRoomUserButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC6(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.listRoomUserButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC7:  (ListRoomButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.listRoomButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC7(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.listRoomButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC8:  (RoomOPButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.roomOPButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC8(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.roomOPButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC9:  (HelpButton.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChat.helpButton_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC9(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.helpButton_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * This method was writen by yhwu.
 * @param Msg java.lang.String
 */
private void doAddMsg(String Msg)
{
	int start=-1,end=0,len;
	Style theStyle=Normal;
	String cont=Msg;

	try
	{
		while((start=Msg.indexOf(27))!=-1)
		{
			if(start!=0)
				doc.insertString(doc.getLength(),Msg.substring(0,start),theStyle);
			else
				doc.insertString(doc.getLength()," ",theStyle);
			end=start+1;
			len=Msg.length();
			int fgcode=7;
			while(end<=len)
			{
				char tmp=Msg.charAt(end);

				if(tmp=='m')
				{
					String ctrl=Msg.substring(start,end+1);
//					if(ctrl.indexOf("[1;"))
					if(fgcode>=0&&fgcode<=7)
						theStyle=theFGColors[fgcode];
					Msg=Msg.substring(end+1);
				}
				if(tmp=='3')
					fgcode=Msg.charAt(end+1)-'0';
				if(tmp>='a'&&tmp<='z')
					break;
				if(tmp>='A'&&tmp<='Z')
					break;
				end++;
			}
		}
		doc.insertString(doc.getLength(),Msg,theStyle);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
/**
 * Return the ActionHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getActionHelpButton() {
	if (ivjActionHelpButton == null) {
		try {
			ivjActionHelpButton = new javax.swing.JButton();
			ivjActionHelpButton.setName("ActionHelpButton");
			ivjActionHelpButton.setText("");
			ivjActionHelpButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjActionHelpButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjActionHelpButton.setIcon(javax.swing.UIManager.getIcon("OptionPane.questionIcon"));
			ivjActionHelpButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjActionHelpButton.setToolTipText("Actions List");
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjActionHelpButton;
}
/**
 * Return the ActionsButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getActionsButton() {
	if (ivjActionsButton == null) {
		try {
			ivjActionsButton = new javax.swing.JButton();
			ivjActionsButton.setName("ActionsButton");
			ivjActionsButton.setToolTipText("Doing Actions");
			ivjActionsButton.setText("");
			ivjActionsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjActionsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjActionsButton.setIcon(new javax.swing.ImageIcon("fg.gif"));
			ivjActionsButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjActionsButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"fg.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjActionsButton;
}
/**
 * Return the HelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getHelpButton() {
	if (ivjHelpButton == null) {
		try {
			ivjHelpButton = new javax.swing.JButton();
			ivjHelpButton.setName("HelpButton");
			ivjHelpButton.setToolTipText("Chat Help");
			ivjHelpButton.setText("");
			ivjHelpButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjHelpButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjHelpButton.setIcon(javax.swing.UIManager.getIcon("OptionPane.questionIcon"));
			ivjHelpButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjHelpButton;
}
/**
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
			getJFrameContentPane().add(getJFrameContentPane1(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJFrameContentPane;
}
/**
 * Return the JFrameContentPane1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane1() {
	if (ivjJFrameContentPane1 == null) {
		try {
			ivjJFrameContentPane1 = new javax.swing.JPanel();
			ivjJFrameContentPane1.setName("JFrameContentPane1");
			ivjJFrameContentPane1.setLayout(new java.awt.BorderLayout());
			getJFrameContentPane1().add(getJToolBar1(), "North");
			getJFrameContentPane1().add(getJPanel5(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJFrameContentPane1;
}
/**
 * Return the JLabel3 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel3() {
	if (ivjJLabel3 == null) {
		try {
			ivjJLabel3 = new javax.swing.JLabel();
			ivjJLabel3.setName("JLabel3");
			ivjJLabel3.setText("Room");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJLabel3;
}
/**
 * Return the JLabel4 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel4() {
	if (ivjJLabel4 == null) {
		try {
			ivjJLabel4 = new javax.swing.JLabel();
			ivjJLabel4.setName("JLabel4");
			ivjJLabel4.setText("Topic");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJLabel4;
}
/**
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel1() {
	if (ivjJPanel1 == null) {
		try {
			ivjJPanel1 = new javax.swing.JPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setLayout(new java.awt.BorderLayout());
			getJPanel1().add(getJPanel3(), "Center");
			getJPanel1().add(getJPanel2(), "West");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJPanel1;
}
/**
 * Return the JPanel2 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel2() {
	if (ivjJPanel2 == null) {
		try {
			ivjJPanel2 = new javax.swing.JPanel();
			ivjJPanel2.setName("JPanel2");
			ivjJPanel2.setLayout(new java.awt.BorderLayout());
			ivjJPanel2.setBackground(new java.awt.Color(204,204,204));
			getJPanel2().add(getJLabel3(), "West");
			getJPanel2().add(getJTextField3(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJPanel2;
}
/**
 * Return the JPanel3 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel3() {
	if (ivjJPanel3 == null) {
		try {
			ivjJPanel3 = new javax.swing.JPanel();
			ivjJPanel3.setName("JPanel3");
			ivjJPanel3.setLayout(new java.awt.BorderLayout());
			getJPanel3().add(getJLabel4(), "West");
			getJPanel3().add(getJTextField4(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJPanel3;
}
/**
 * Return the JPanel4 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel4() {
	if (ivjJPanel4 == null) {
		try {
			ivjJPanel4 = new javax.swing.JPanel();
			ivjJPanel4.setName("JPanel4");
			ivjJPanel4.setLayout(new java.awt.BorderLayout());
			getJPanel4().add(getJTextField5(), "West");
			getJPanel4().add(getJTextField2(), "Center");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJPanel4;
}
/**
 * Return the JPanel5 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel5() {
	if (ivjJPanel5 == null) {
		try {
			ivjJPanel5 = new javax.swing.JPanel();
			ivjJPanel5.setName("JPanel5");
			ivjJPanel5.setLayout(new java.awt.BorderLayout());
			getJPanel5().add(getJPanel4(), "South");
			getJPanel5().add(getJScrollPane1(), "Center");
			getJPanel5().add(getJPanel1(), "North");
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJPanel5;
}
/**
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			getJScrollPane1().setViewportView(getJTextPane1());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJScrollPane1;
}
/**
 * Return the JTextField2 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField2() {
	if (ivjJTextField2 == null) {
		try {
			ivjJTextField2 = new javax.swing.JTextField();
			ivjJTextField2.setName("JTextField2");
			ivjJTextField2.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField2.setColumns(60);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextField2;
}
/**
 * Return the JTextField3 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField3() {
	if (ivjJTextField3 == null) {
		try {
			ivjJTextField3 = new javax.swing.JTextField();
			ivjJTextField3.setName("JTextField3");
			ivjJTextField3.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField3.setBackground(java.awt.Color.orange);
			ivjJTextField3.setColumns(8);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextField3;
}
/**
 * Return the JTextField4 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField4() {
	if (ivjJTextField4 == null) {
		try {
			ivjJTextField4 = new javax.swing.JTextField();
			ivjJTextField4.setName("JTextField4");
			ivjJTextField4.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField4.setBackground(java.awt.Color.orange);
			ivjJTextField4.setColumns(40);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextField4;
}
/**
 * Return the JTextField5 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField5() {
	if (ivjJTextField5 == null) {
		try {
			ivjJTextField5 = new javax.swing.JTextField();
			ivjJTextField5.setName("JTextField5");
			ivjJTextField5.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField5.setBackground(java.awt.Color.orange);
			ivjJTextField5.setColumns(8);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextField5;
}
/**
 * Return the JTextPane1 property value.
 * @return javax.swing.JTextPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextPane getJTextPane1() {
	if (ivjJTextPane1 == null) {
		try {
			ivjJTextPane1 = new javax.swing.JTextPane();
			ivjJTextPane1.setName("JTextPane1");
			ivjJTextPane1.setBackground(java.awt.Color.black);
			ivjJTextPane1.setForeground(java.awt.Color.lightGray);
			ivjJTextPane1.setFont(new java.awt.Font("serif", 1, 12));
			ivjJTextPane1.setBounds(0, 0, 10, 10);
			ivjJTextPane1.setEnabled(true);
			ivjJTextPane1.setEditable(false);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextPane1;
}
/**
 * Return the JToolBar1 property value.
 * @return javax.swing.JToolBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JToolBar getJToolBar1() {
	if (ivjJToolBar1 == null) {
		try {
			ivjJToolBar1 = new javax.swing.JToolBar();
			ivjJToolBar1.setName("JToolBar1");
			ivjJToolBar1.add(getActionsButton());
			getJToolBar1().add(getSendNoteButton(), getSendNoteButton().getName());
			getJToolBar1().add(getListUserButton(), getListUserButton().getName());
			getJToolBar1().add(getListRoomUserButton(), getListRoomUserButton().getName());
			getJToolBar1().add(getListRoomButton(), getListRoomButton().getName());
			ivjJToolBar1.addSeparator();
			getJToolBar1().add(getRoomOPButton(), getRoomOPButton().getName());
			ivjJToolBar1.addSeparator();
			getJToolBar1().add(getHelpButton(), getHelpButton().getName());
			getJToolBar1().add(getOPHelpButton(), getOPHelpButton().getName());
			getJToolBar1().add(getActionHelpButton(), getActionHelpButton().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJToolBar1;
}
/**
 * Return the ListRoomButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getListRoomButton() {
	if (ivjListRoomButton == null) {
		try {
			ivjListRoomButton = new javax.swing.JButton();
			ivjListRoomButton.setName("ListRoomButton");
			ivjListRoomButton.setToolTipText("List Rooms");
			ivjListRoomButton.setText("");
			ivjListRoomButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjListRoomButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjListRoomButton.setIcon(new javax.swing.ImageIcon("bullets.gif"));
			ivjListRoomButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjListRoomButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"bullets.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjListRoomButton;
}
/**
 * Return the ListRoomUserButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getListRoomUserButton() {
	if (ivjListRoomUserButton == null) {
		try {
			ivjListRoomUserButton = new javax.swing.JButton();
			ivjListRoomUserButton.setName("ListRoomUserButton");
			ivjListRoomUserButton.setToolTipText("List Room Users");
			ivjListRoomUserButton.setText("");
			ivjListRoomUserButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjListRoomUserButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjListRoomUserButton.setIcon(new javax.swing.ImageIcon("bullets.gif"));
			ivjListRoomUserButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjListRoomUserButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"bullets.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjListRoomUserButton;
}
/**
 * Return the ListUserButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getListUserButton() {
	if (ivjListUserButton == null) {
		try {
			ivjListUserButton = new javax.swing.JButton();
			ivjListUserButton.setName("ListUserButton");
			ivjListUserButton.setToolTipText("List All Users");
			ivjListUserButton.setText("");
			ivjListUserButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjListUserButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjListUserButton.setIcon(new javax.swing.ImageIcon("bullets.gif"));
			ivjListUserButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjListUserButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"bullets.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjListUserButton;
}
/**
 * Return the OPHelpButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getOPHelpButton() {
	if (ivjOPHelpButton == null) {
		try {
			ivjOPHelpButton = new javax.swing.JButton();
			ivjOPHelpButton.setName("OPHelpButton");
			ivjOPHelpButton.setToolTipText("Room Operator Help");
			ivjOPHelpButton.setText("");
			ivjOPHelpButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjOPHelpButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjOPHelpButton.setIcon(javax.swing.UIManager.getIcon("OptionPane.questionIcon"));
			ivjOPHelpButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjOPHelpButton;
}
/**
 * Return the RoomOPButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getRoomOPButton() {
	if (ivjRoomOPButton == null) {
		try {
			ivjRoomOPButton = new javax.swing.JButton();
			ivjRoomOPButton.setName("RoomOPButton");
			ivjRoomOPButton.setToolTipText("Room OP Only Function");
			ivjRoomOPButton.setText("");
			ivjRoomOPButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjRoomOPButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjRoomOPButton.setIcon(new javax.swing.ImageIcon("open.gif"));
			ivjRoomOPButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjRoomOPButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"open.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjRoomOPButton;
}
/**
 * Return the SendNoteButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getSendNoteButton() {
	if (ivjSendNoteButton == null) {
		try {
			ivjSendNoteButton = new javax.swing.JButton();
			ivjSendNoteButton.setName("SendNoteButton");
			ivjSendNoteButton.setToolTipText("Give Someone a Note");
			ivjSendNoteButton.setText("");
			ivjSendNoteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			ivjSendNoteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
			ivjSendNoteButton.setIcon(new javax.swing.ImageIcon("new.gif"));
			ivjSendNoteButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
			// user code begin {1}
			ivjSendNoteButton.setIcon(new javax.swing.ImageIcon(new URL(myCodeBase+"new.gif")));
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjSendNoteButton;
}
/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Comment
 */
public void helpButton_ActionPerformed()
{
	try
	{
		if (oos != null)
			oos.writeObject(new ChatHelps(1));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Initializes connections
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() {
	// user code begin {1}
	// user code end
	this.addWindowListener(this);
	getJTextField3().addActionListener(this);
	getActionsButton().addActionListener(this);
	getSendNoteButton().addActionListener(this);
	getListUserButton().addActionListener(this);
	getListRoomUserButton().addActionListener(this);
	getListRoomButton().addActionListener(this);
	getRoomOPButton().addActionListener(this);
	getHelpButton().addActionListener(this);
	getOPHelpButton().addActionListener(this);
	getActionHelpButton().addActionListener(this);
	getJTextField4().addActionListener(this);
	getJTextField2().addActionListener(this);
	getJTextField5().addActionListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	// user code begin {1}
	// user code end
	setName("SwingChat");
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	setSize(655, 423);
	setContentPane(getJFrameContentPane());
	initConnections();
	// user code begin {2}
	sc = new StyleContext();
	
	Style s = sc.addStyle(null, null);
	StyleConstants.setForeground(s, Color.lightGray);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	Normal=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.black);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[0]=s;

	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.red);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[1]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.green);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[2]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.yellow);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[3]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.blue);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[4]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.pink);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[5]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.cyan);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[6]=s;
	
	s = sc.addStyle(null, null);
	StyleConstants.setItalic(s, true);
	StyleConstants.setForeground(s, Color.white);
	StyleConstants.setFontFamily(s, "SansSerif");
	StyleConstants.setBold(s, true);
	theFGColors[7]=s;

	doc=new DefaultStyledDocument(sc);
	getJTextPane1().setStyledDocument(doc);
	// user code end
}
/**
 * Comment
 */
public void jTextField2_ActionPerformed() {
	try
	{
		String msg=ClientUtils.string2Byte(getJTextField2().getText());
		getJTextField2().setText("");
		if(oos!=null)
			oos.writeObject(new ChatPostMsg(msg));
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void jTextField3_ActionPerformed()
{
	try
	{
		String msg = ClientUtils.string2Byte(getJTextField3().getText());
		if (oos != null)
			oos.writeObject(new ChatEnterRoom(msg));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void jTextField4_ActionPerformed()
{
	try
	{
		String msg = ClientUtils.string2Byte(getJTextField4().getText());
		if (oos != null)
			oos.writeObject(new ChatSetTopic(msg));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void jTextField5_ActionPerformed()
{
	try
	{
		String msg = ClientUtils.string2Byte(getJTextField5().getText());
		if (oos != null)
			oos.writeObject(new ChatChangeNick(msg));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void listRoomButton_ActionPerformed()
{
	try
	{
		if (oos != null)
			oos.writeObject(new ChatListUser(3,null));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void listRoomUserButton_ActionPerformed()
{
	try
	{
		String theRoom=(new SwingInputBox(this,"Input Room","Input what room you want to list user : ")).getText();
//		String theRoom=(new SwingInputBox("Input Room","Input what room you want to list user : ")).getText();
		if (oos != null&&theRoom!=null)
			oos.writeObject(new ChatListUser(1,ClientUtils.string2Byte(theRoom)));
		else
			oos.writeObject(new ChatListUser(1,ClientUtils.string2Byte(getJTextField3().getText())));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void listUserButton_ActionPerformed()
{
	try
	{
		if (oos != null)
			oos.writeObject(new ChatListUser(1,null));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		SwingChat aSwingChat;
		aSwingChat = new SwingChat();
		try {
			Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
			Class parmTypes[] = { java.awt.Window.class };
			Object parms[] = { aSwingChat };
			java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
			aCtor.newInstance(parms);
		} catch (java.lang.Throwable exc) {};
		aSwingChat.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * Comment
 */
public void oPHelpButton_ActionPerformed()
{
	try
	{
		if (oos != null)
			oos.writeObject(new ChatHelps(2));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Comment
 */
public void roomOPButton_ActionPerformed()
{
	opFunction.setVisible(true);
	return;
}
/**
 * Comment
 */
public void sendNoteButton_ActionPerformed()
{
	try
	{
		String theUser=(new SwingInputBox(this,"Input User","Input the user nick name you want to send : ")).getText();
		String theNote=(new SwingInputBox(this,"Input Note","Input what you want to send : ")).getText();
//		String theUser=(new SwingInputBox("Input User","Input the user nick name you want to send : ")).getText();
//		String theNote=(new SwingInputBox("Input Note","Input what you want to send : ")).getText();
		if (oos != null&&theUser!=null&&theNote!=null)
			oos.writeObject(new ChatSendNote(ClientUtils.string2Byte(theUser),ClientUtils.string2Byte(theNote)));
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * This method was writen by yhwu.
 * @param cmd java.lang.Object
 */
public synchronized void serverCommand(Object cmd)
{
	try
	{
			if(cmd.getClass().getName().equals("colabbs.bbstp.chat.ChatLoginState"))
			{
				ChatLoginState cls=(ChatLoginState)cmd;

				if(cls.Result)
				{
					setVisible(true);
				}
				else
				{
					new SwingMessageBox("Connection Error",cls.Message);
				}
			}
      else if(cmd.getClass().getName().equals("colabbs.bbstp.chat.ChatAddMessage"))
			{
				ChatAddMessage cam=(ChatAddMessage)cmd;
				doAddMsg(ClientUtils.byte2String(cam.Message)+"\n");
			}
      else if(cmd.getClass().getName().equals("colabbs.bbstp.chat.ChatEnterRoom"))
			{
				ChatEnterRoom cer=(ChatEnterRoom)cmd;
				getJTextField3().setText(ClientUtils.byte2String(cer.Room));
			}
      else if(cmd.getClass().getName().equals("colabbs.bbstp.chat.ChatSetTopic"))
			{
				ChatSetTopic cst=(ChatSetTopic)cmd;
				getJTextField4().setText(ClientUtils.byte2String(cst.newTopic));
			}
      else if(cmd.getClass().getName().equals("colabbs.bbstp.chat.ChatActions"))
			{
				System.out.println("ChatActions");
				ChatActions ca=(ChatActions)cmd;
				Verb1_1=ca.Verb1_1;
				Verb1_2=ca.Verb1_2;
				Verb2=ca.Verb2;
				Verb3=ca.Verb3;
				actionMenu.setVerb(Verb1_1,Verb1_2,Verb2,Verb3);
			}
	}
	catch(ClassCastException e)
	{
		e.printStackTrace();
	}
}
/**
 * Comment
 */
public void swingChat_WindowClosed() {
	doc = new DefaultStyledDocument(sc);
	getJTextPane1().setStyledDocument(doc);
	try
	{
		if(oos!=null)
		{
			oos.writeObject(new ChatLogout());
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	return;
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowActivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosed(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == this) ) {
		connEtoC1(e);
	}
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowClosing(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeactivated(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowDeiconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowIconified(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowOpened(java.awt.event.WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
}
