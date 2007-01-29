package colabbs.bbsclient.swing;

import java.io.*;

import colabbs.bbsclient.ClientUtils;

import colabbs.bbstp.chat.*;
public class SwingChatOPFunction extends javax.swing.JFrame implements java.awt.event.ActionListener
{
	private ObjectOutputStream oos = null;
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JButton ivjJButton2 = null;
	private javax.swing.JButton ivjJButton3 = null;
	private javax.swing.JCheckBox ivjJCheckBox1 = null;
	private javax.swing.JCheckBox ivjJCheckBox2 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JLabel ivjJLabel2 = null;
	private javax.swing.JLabel ivjJLabel3 = null;
	private javax.swing.JLabel ivjJLabel4 = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JPanel ivjJPanel2 = null;
	private javax.swing.JPanel ivjJPanel3 = null;
	private javax.swing.JPanel ivjJPanel4 = null;
	private javax.swing.JPanel ivjJPanel5 = null;
	private javax.swing.JTabbedPane ivjJTabbedPane1 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
	private javax.swing.JTextField ivjJTextField2 = null;
	private javax.swing.JTextField ivjJTextField3 = null;
	private javax.swing.JTextField ivjJTextField4 = null;
	private javax.swing.JPanel ivjPage = null;
	private static java.util.ResourceBundle resChatRoom = java.util.ResourceBundle.getBundle("ChatRoom"); //$NON-NLS-1$;
public SwingChatOPFunction() {
	super();
	initialize();
}
public SwingChatOPFunction(ObjectOutputStream theoos)
{
	super("Operator Only Functions");
	initialize();
	oos=theoos;
}
public SwingChatOPFunction(String title) {
	super(title);
}
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
private javax.swing.JCheckBox getJCheckBox1() {
	if (ivjJCheckBox1 == null) {
		try {
			ivjJCheckBox1 = new javax.swing.JCheckBox();
			ivjJCheckBox1.setName("JCheckBox1");
			ivjJCheckBox1.setText("Change Room Lock State");
			ivjJCheckBox1.setBounds(47, 32, 300, 30);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJCheckBox1;
}
private javax.swing.JCheckBox getJCheckBox2() {
	if (ivjJCheckBox2 == null) {
		try {
			ivjJCheckBox2 = new javax.swing.JCheckBox();
			ivjJCheckBox2.setName("JCheckBox2");
			ivjJCheckBox2.setText("Change Room Visible State");
			ivjJCheckBox2.setBounds(46, 80, 337, 30);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJCheckBox2;
}
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
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Change OP to");
			ivjJLabel1.setBounds(20, 27, 120, 20);
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
private javax.swing.JLabel getJLabel2() {
	if (ivjJLabel2 == null) {
		try {
			ivjJLabel2 = new javax.swing.JLabel();
			ivjJLabel2.setName("JLabel2");
			ivjJLabel2.setText("Change Topic to");
			ivjJLabel2.setBounds(30, 32, 157, 20);
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
private javax.swing.JLabel getJLabel3() {
	if (ivjJLabel3 == null) {
		try {
			ivjJLabel3 = new javax.swing.JLabel();
			ivjJLabel3.setName("JLabel3");
			ivjJLabel3.setText("Input the nick name you want to invite");
			ivjJLabel3.setBounds(27, 32, 390, 28);
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
private javax.swing.JLabel getJLabel4() {
	if (ivjJLabel4 == null) {
		try {
			ivjJLabel4 = new javax.swing.JLabel();
			ivjJLabel4.setName("JLabel4");
			ivjJLabel4.setText("Input the nick name you want to kick");
			ivjJLabel4.setBounds(14, 35, 379, 20);
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
private javax.swing.JPanel getJPanel2() {
	if (ivjJPanel2 == null) {
		try {
			ivjJPanel2 = new javax.swing.JPanel();
			ivjJPanel2.setName("JPanel2");
			ivjJPanel2.setLayout(null);
			getJPanel2().add(getJLabel2(), getJLabel2().getName());
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
private javax.swing.JPanel getJPanel3() {
	if (ivjJPanel3 == null) {
		try {
			ivjJPanel3 = new javax.swing.JPanel();
			ivjJPanel3.setName("JPanel3");
			ivjJPanel3.setLayout(null);
			getJPanel3().add(getJLabel3(), getJLabel3().getName());
			getJPanel3().add(getJTextField3(), getJTextField3().getName());
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
private javax.swing.JPanel getJPanel4() {
	if (ivjJPanel4 == null) {
		try {
			ivjJPanel4 = new javax.swing.JPanel();
			ivjJPanel4.setName("JPanel4");
			ivjJPanel4.setLayout(null);
			getJPanel4().add(getJLabel4(), getJLabel4().getName());
			getJPanel4().add(getJTextField4(), getJTextField4().getName());
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
private javax.swing.JPanel getJPanel5() {
	if (ivjJPanel5 == null) {
		try {
			ivjJPanel5 = new javax.swing.JPanel();
			ivjJPanel5.setName("JPanel5");
			ivjJPanel5.setLayout(null);
			getJPanel5().add(getJCheckBox1(), getJCheckBox1().getName());
			getJPanel5().add(getJCheckBox2(), getJCheckBox2().getName());
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
private javax.swing.JTabbedPane getJTabbedPane1() {
	if (ivjJTabbedPane1 == null) {
		try {
			ivjJTabbedPane1 = new javax.swing.JTabbedPane();
			ivjJTabbedPane1.setName("JTabbedPane1");
			ivjJTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.TOP);
			ivjJTabbedPane1.insertTab("OP", null, getPage(), null, 0);
			ivjJTabbedPane1.insertTab("Topic", null, getJPanel2(), null, 1);
			ivjJTabbedPane1.insertTab("Invite", null, getJPanel3(), null, 2);
			ivjJTabbedPane1.insertTab("Kick", null, getJPanel4(), null, 3);
			ivjJTabbedPane1.insertTab("Properties", null, getJPanel5(), null, 4);
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
private javax.swing.JTextField getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setBounds(144, 26, 247, 24);
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
private javax.swing.JTextField getJTextField2() {
	if (ivjJTextField2 == null) {
		try {
			ivjJTextField2 = new javax.swing.JTextField();
			ivjJTextField2.setName("JTextField2");
			ivjJTextField2.setBounds(167, 31, 234, 24);
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
private javax.swing.JTextField getJTextField3() {
	if (ivjJTextField3 == null) {
		try {
			ivjJTextField3 = new javax.swing.JTextField();
			ivjJTextField3.setName("JTextField3");
			ivjJTextField3.setBounds(283, 64, 128, 27);
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
private javax.swing.JTextField getJTextField4() {
	if (ivjJTextField4 == null) {
		try {
			ivjJTextField4 = new javax.swing.JTextField();
			ivjJTextField4.setName("JTextField4");
			ivjJTextField4.setBounds(264, 71, 137, 27);
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
private javax.swing.JPanel getPage() {
	if (ivjPage == null) {
		try {
			ivjPage = new javax.swing.JPanel();
			ivjPage.setName("Page");
			ivjPage.setLayout(null);
			getPage().add(getJLabel1(), getJLabel1().getName());
			getPage().add(getJTextField1(), getJTextField1().getName());
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
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	// user code begin {1}
	// user code end
	setName("SwingChatOPFunction");
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	setSize(426, 240);
	setContentPane(getJFrameContentPane());
	initConnections();
	// user code begin {2}
	// user code end
}
public void jButton1_ActionPerformed()
{
	try
	{
		switch (getJTabbedPane1().getSelectedIndex())
		{
			case 0 :
				oos.writeObject(new ChatSetOP(ClientUtils.string2Byte(getJTextField1().getText())));
				break;
			case 1 :
				oos.writeObject(new ChatSetTopic(ClientUtils.string2Byte(getJTextField2().getText())));
				break;
			case 2 :
				oos.writeObject(new ChatInviteUser(ClientUtils.string2Byte(getJTextField3().getText())));
				break;
			case 3 :
				oos.writeObject(new ChatKickUser(ClientUtils.string2Byte(getJTextField4().getText())));
				break;
			case 4 :
//				System.out.println(getJCheckBox1().isSelected());
//				System.out.println(getJCheckBox2().isSelected());
				oos.writeObject(new ChatChangeRoomProperties(getJCheckBox1().isSelected(),getJCheckBox2().isSelected()));
				break;
		}
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
		SwingChatOPFunction aSwingChatOPFunction;
		aSwingChatOPFunction = new SwingChatOPFunction();
		try {
			Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
			Class parmTypes[] = { java.awt.Window.class };
			Object parms[] = { aSwingChatOPFunction };
			java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
			aCtor.newInstance(parms);
		} catch (java.lang.Throwable exc) {};
		aSwingChatOPFunction.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
}
