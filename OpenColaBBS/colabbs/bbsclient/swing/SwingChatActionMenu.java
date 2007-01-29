package colabbs.bbsclient.swing;

import java.io.*;
import java.util.*;
import colabbs.bbstp.chat.*;
import colabbs.bbsclient.ClientUtils;
/**
 * This type was writen by yhwu.
 */
public class SwingChatActionMenu extends javax.swing.JFrame implements java.awt.event.ActionListener, java.awt.event.ItemListener
{
	private ObjectOutputStream oos = null;
	private Hashtable Verb1_1 = null, Verb1_2 = null, Verb2 = null, Verb3 = null;
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JButton ivjJButton2 = null;
	private javax.swing.JButton ivjJButton3 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JPanel ivjJPanel2 = null;
	private javax.swing.JPanel ivjJPanel3 = null;
	private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
	private javax.swing.JPanel ivjPage = null;
	private static java.util.ResourceBundle resChatRoom = java.util.ResourceBundle.getBundle("ChatRoom"); //$NON-NLS-1$;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JLabel ivjJLabel2 = null;
	private javax.swing.JLabel ivjJLabel3 = null;
	private javax.swing.JLabel ivjJLabel4 = null;
	private javax.swing.JLabel ivjJLabel5 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
	private javax.swing.JTextField ivjJTextField2 = null;
	private javax.swing.JComboBox ivjJComboBox1 = null;
	private javax.swing.JComboBox ivjJComboBox2 = null;
	private javax.swing.JComboBox ivjJComboBox3 = null;
/**
 * Constructor
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public SwingChatActionMenu() {
	super();
	initialize();
}
/**
 * SwingChatActionMenu constructor comment.
 * @param title java.lang.String
 */
public SwingChatActionMenu(ObjectOutputStream theoos)
{
	super("Doing Action");
	initialize();
	oos=theoos;
}
/**
 * SwingChatActionMenu constructor comment.
 * @param title java.lang.String
 */
public SwingChatActionMenu(String title)
{
	super(title);
	initialize();
}
/**
 * Method to handle events for the ActionListener interface.
 * @param e java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == getJButton1()) ) {
		connEtoC1(e);
	}
	if ((e.getSource() == getJButton1()) ) {
		connEtoM1(e);
	}
	if ((e.getSource() == getJButton2()) ) {
		connEtoM2(e);
	}
	if ((e.getSource() == getJButton3()) ) {
		connEtoC2(e);
	}
	// user code begin {2}
	// user code end
}
/**
 * connEtoC1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChatActionMenu.jButton1_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jButton1_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (JButton3.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChatActionMenu.jButton1_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jButton1_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC3:  (JComboBox1.item.itemStateChanged(java.awt.event.ItemEvent) --> SwingChatActionMenu.jComboBox1_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC3(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jComboBox1_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC4:  (JComboBox2.item.itemStateChanged(java.awt.event.ItemEvent) --> SwingChatActionMenu.jComboBox2_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC4(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jComboBox2_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC5:  (JComboBox3.item.itemStateChanged(java.awt.event.ItemEvent) --> SwingChatActionMenu.jComboBox3_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC5(java.awt.event.ItemEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jComboBox3_ItemStateChanged(arg1);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChatActionMenu.visible)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM2:  (JButton2.action.actionPerformed(java.awt.event.ActionEvent) --> SwingChatActionMenu.visible)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.setVisible(false);
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Return the JButton1 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton1() {
	if (ivjJButton1 == null) {
		try {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setName("JButton1");
			ivjJButton1.setText(resChatRoom.getString("OK"));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJButton1;
}
/**
 * Return the JButton2 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton2() {
	if (ivjJButton2 == null) {
		try {
			ivjJButton2 = new javax.swing.JButton();
			ivjJButton2.setName("JButton2");
			ivjJButton2.setText(resChatRoom.getString("Cancel"));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJButton2;
}
/**
 * Return the JButton3 property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getJButton3() {
	if (ivjJButton3 == null) {
		try {
			ivjJButton3 = new javax.swing.JButton();
			ivjJButton3.setName("JButton3");
			ivjJButton3.setText(resChatRoom.getString("Apply"));
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJButton3;
}
/**
 * Return the JComboBox1 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getJComboBox1() {
	if (ivjJComboBox1 == null) {
		try {
			ivjJComboBox1 = new javax.swing.JComboBox();
			ivjJComboBox1.setName("JComboBox1");
			ivjJComboBox1.setBounds(4, 6, 130, 27);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJComboBox1;
}
/**
 * Return the JComboBox2 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getJComboBox2() {
	if (ivjJComboBox2 == null) {
		try {
			ivjJComboBox2 = new javax.swing.JComboBox();
			ivjJComboBox2.setName("JComboBox2");
			ivjJComboBox2.setBounds(5, 6, 130, 27);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJComboBox2;
}
/**
 * Return the JComboBox3 property value.
 * @return javax.swing.JComboBox
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JComboBox getJComboBox3() {
	if (ivjJComboBox3 == null) {
		try {
			ivjJComboBox3 = new javax.swing.JComboBox();
			ivjJComboBox3.setName("JComboBox3");
			ivjJComboBox3.setBounds(5, 6, 130, 27);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJComboBox3;
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
			getJFrameContentPane().add(getJPanel1(), "South");
			getJFrameContentPane().add(getJTabbedPane1(), "Center");
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
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Verb1_1");
			ivjJLabel1.setBounds(16, 55, 712, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJLabel1;
}
/**
 * Return the JLabel2 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new javax.swing.JLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText("(Someone\'s nick name)");
			ivjJLabel2.setBounds(157, 99, 252, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJLabel2;
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
			ivjJLabel3.setText("Verb1_2");
			ivjJLabel3.setBounds(17, 145, 383, 20);
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
			ivjJLabel4.setText("Verb2");
			ivjJLabel4.setBounds(11, 47, 387, 20);
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
 * Return the JLabel5 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel5() {
	if (ivjJLabel5 == null) {
		try {
			ivjJLabel5 = new javax.swing.JLabel();
			ivjJLabel5.setName("JLabel5");
			ivjJLabel5.setText("Verb3");
			ivjJLabel5.setBounds(6, 45, 617, 20);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJLabel5;
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
			ivjJPanel1.setLayout(new java.awt.FlowLayout());
			ivjJPanel1.add(getJButton1());
			getJPanel1().add(getJButton2(), getJButton2().getName());
			getJPanel1().add(getJButton3(), getJButton3().getName());
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
			ivjJPanel2.setLayout(null);
			getJPanel2().add(getJComboBox2(), getJComboBox2().getName());
			getJPanel2().add(getJLabel4(), getJLabel4().getName());
			getJPanel2().add(getJTextField2(), getJTextField2().getName());
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
			ivjJPanel3.setLayout(null);
			getJPanel3().add(getJComboBox3(), getJComboBox3().getName());
			getJPanel3().add(getJLabel5(), getJLabel5().getName());
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
 * Return the JTabbedPane1 property value.
 * @return javax.swing.JTabbedPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTabbedPane getJTabbedPane1() {
	if (ivjJTabbedPane1 == null) {
		try {
			ivjJTabbedPane1 = new javax.swing.JTabbedPane();
			ivjJTabbedPane1.setName("JTabbedPane1");
			ivjJTabbedPane1.insertTab("Type 1", null, getPage(), null, 0);
			ivjJTabbedPane1.insertTab("Type 2", null, getJPanel2(), null, 1);
			ivjJTabbedPane1.insertTab("Type 3", null, getJPanel3(), null, 2);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTabbedPane1;
}
/**
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setBounds(16, 96, 122, 24);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJTextField1;
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
			ivjJTextField2.setBounds(9, 90, 384, 24);
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
 * Return the Page property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getPage() {
	if (ivjPage == null) {
		try {
			ivjPage = new javax.swing.JPanel();
			ivjPage.setName("Page");
			ivjPage.setLayout(null);
			getPage().add(getJComboBox1(), getJComboBox1().getName());
			getPage().add(getJTextField1(), getJTextField1().getName());
			getPage().add(getJLabel2(), getJLabel2().getName());
			getPage().add(getJLabel3(), getJLabel3().getName());
			getPage().add(getJLabel1(), getJLabel1().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjPage;
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
 * Initializes connections
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() {
	// user code begin {1}
	// user code end
	getJButton1().addActionListener(this);
	getJButton2().addActionListener(this);
	getJButton3().addActionListener(this);
	getJComboBox1().addItemListener(this);
	getJComboBox2().addItemListener(this);
	getJComboBox3().addItemListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	// user code begin {1}
	// user code end
	setName("SwingChatActionMenu");
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	setSize(639, 307);
	setContentPane(getJFrameContentPane());
	initConnections();
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the ItemListener interface.
 * @param e java.awt.event.ItemEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void itemStateChanged(java.awt.event.ItemEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == getJComboBox1()) ) {
		connEtoC3(e);
	}
	if ((e.getSource() == getJComboBox2()) ) {
		connEtoC4(e);
	}
	if ((e.getSource() == getJComboBox3()) ) {
		connEtoC5(e);
	}
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void jButton1_ActionPerformed()
{
	try
	{
		switch(getJTabbedPane1().getSelectedIndex())
		{
			case 0:
				oos.writeObject(new ChatDoAction((String)getJComboBox1().getSelectedItem(),ClientUtils.string2Byte(getJTextField1().getText())));
				break;
			case 1:
				oos.writeObject(new ChatDoAction((String)getJComboBox2().getSelectedItem(),ClientUtils.string2Byte(getJTextField2().getText())));
				break;
			case 2:
				oos.writeObject(new ChatDoAction((String)getJComboBox3().getSelectedItem(),""));
				break;
		}
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
public void jComboBox1_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	String Arg1=(String)Verb1_1.get(itemEvent.getItem()),Arg2=(String)Verb1_2.get(itemEvent.getItem());
	if(Arg1!=null)
		getJLabel1().setText(ClientUtils.byte2String(Arg1));
	else
		getJLabel1().setText("");
	if(Arg2!=null)
		getJLabel3().setText(ClientUtils.byte2String(Arg2));
	else
		getJLabel3().setText("");
	return;
}
/**
 * Comment
 */
public void jComboBox2_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	getJLabel4().setText(ClientUtils.byte2String((String)Verb2.get(itemEvent.getItem())));
	return;
}
/**
 * Comment
 */
public void jComboBox3_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
	getJLabel5().setText(ClientUtils.byte2String((String)Verb3.get(itemEvent.getItem())));
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		SwingChatActionMenu aSwingChatActionMenu;
		aSwingChatActionMenu = new SwingChatActionMenu();
		try {
			Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
			Class parmTypes[] = { java.awt.Window.class };
			Object parms[] = { aSwingChatActionMenu };
			java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
			aCtor.newInstance(parms);
		} catch (java.lang.Throwable exc) {};
		aSwingChatActionMenu.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
/**
 * This method was writen by yhwu.
 * @param theVerb1_1 java.util.Hashtable
 * @param theVerb1_2 java.util.Hashtable
 * @param theVerb2 java.util.Hashtable
 * @param theVerb3 java.util.Hashtable
 */
public void setVerb(Hashtable theVerb1_1, Hashtable theVerb1_2, Hashtable theVerb2, Hashtable theVerb3)
{
	Verb1_1=theVerb1_1;
	Verb1_2=theVerb1_2;
	Enumeration tmp=Verb1_1.keys();
	while(tmp.hasMoreElements())
		getJComboBox1().addItem(tmp.nextElement());
	
	Verb2=theVerb2;
	tmp=Verb2.keys();
	while(tmp.hasMoreElements())
		getJComboBox2().addItem(tmp.nextElement());
	
	Verb3=theVerb3;
	tmp=Verb3.keys();
	while(tmp.hasMoreElements())
		getJComboBox3().addItem(tmp.nextElement());
}
}
