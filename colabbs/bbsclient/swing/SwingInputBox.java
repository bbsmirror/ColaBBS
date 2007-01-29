package colabbs.bbsclient.swing;

import javax.swing.text.*;
public class SwingInputBox extends javax.swing.JDialog implements java.awt.event.ActionListener
{
	private String theText = null;
	private DefaultStyledDocument doc = null;
	private Style normalStyle=null;
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JButton ivjJButton2 = null;
	private javax.swing.JPanel ivjJFrameContentPane = null;
	private javax.swing.JPanel ivjJPanel1 = null;
	private javax.swing.JPanel ivjJPanel2 = null;
	private javax.swing.JPanel ivjJPanel3 = null;
	private javax.swing.JScrollPane ivjJScrollPane1 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
	private static java.util.ResourceBundle resChatRoom = java.util.ResourceBundle.getBundle("ChatRoom"); //$NON-NLS-1$;
	private javax.swing.JTextPane ivjJTextPane1 = null;
public SwingInputBox() {
	super();
	initialize();
}
public SwingInputBox(java.awt.Frame parent,String title) {
	super(parent,title,true);
//	super(parent,title,false);
	initialize();
}
public SwingInputBox(java.awt.Frame parent,String theTitle, String thePrompt)
{
	super(parent,theTitle,true);
//	super(parent,theTitle,false);
	initialize();
//	getJTextPane1().setText(thePrompt);
	try
	{
		doc.insertString(0,thePrompt,normalStyle);
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == getJTextField1()) ) {
		connEtoC1(e);
	}
	if ((e.getSource() == getJButton1()) ) {
		connEtoC2(e);
	}
	if ((e.getSource() == getJButton2()) ) {
		connEtoM1(e);
	}
	// user code begin {2}
	// user code end
}
/**
 * connEtoC1:  (JTextField1.action.actionPerformed(java.awt.event.ActionEvent) --> SwingInputBox.jTextField1_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField1_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoC2:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> SwingInputBox.jTextField1_ActionPerformed()V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC2(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.jTextField1_ActionPerformed();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * connEtoM1:  (JButton1.action.actionPerformed(java.awt.event.ActionEvent) --> SwingInputBox.visible)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoM1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.dispose();
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
			ivjJButton1.setFont(new java.awt.Font("serif", 1, 12));
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
			ivjJButton2.setFont(new java.awt.Font("serif", 1, 12));
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
 * Return the JFrameContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJFrameContentPane() {
	if (ivjJFrameContentPane == null) {
		try {
			ivjJFrameContentPane = new javax.swing.JPanel();
			ivjJFrameContentPane.setName("JFrameContentPane");
			ivjJFrameContentPane.setFont(new java.awt.Font("serif", 0, 12));
			ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
			getJFrameContentPane().add(getJPanel2(), "Center");
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
 * Return the JPanel1 property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJPanel1() {
	java.awt.GridBagConstraints constraintsJButton1 = new java.awt.GridBagConstraints();
	java.awt.GridBagConstraints constraintsJButton2 = new java.awt.GridBagConstraints();
	if (ivjJPanel1 == null) {
		try {
			ivjJPanel1 = new javax.swing.JPanel();
			ivjJPanel1.setName("JPanel1");
			ivjJPanel1.setFont(new java.awt.Font("serif", 0, 12));
			ivjJPanel1.setLayout(new java.awt.GridBagLayout());

			constraintsJButton1.gridx = 0; constraintsJButton1.gridy = 0;
			constraintsJButton1.gridwidth = 1; constraintsJButton1.gridheight = 1;
			constraintsJButton1.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsJButton1.weightx = 0.0;
			constraintsJButton1.weighty = 0.0;
			getJPanel1().add(getJButton1(), constraintsJButton1);

			constraintsJButton2.gridx = 0; constraintsJButton2.gridy = 1;
			constraintsJButton2.gridwidth = 1; constraintsJButton2.gridheight = 1;
			constraintsJButton2.anchor = java.awt.GridBagConstraints.CENTER;
			constraintsJButton2.weightx = 0.0;
			constraintsJButton2.weighty = 0.0;
			getJPanel1().add(getJButton2(), constraintsJButton2);
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
			ivjJPanel2.setFont(new java.awt.Font("serif", 0, 12));
			ivjJPanel2.setLayout(new java.awt.BorderLayout());
			getJPanel2().add(getJPanel3(), "South");
			getJPanel2().add(getJPanel1(), "East");
			getJPanel2().add(getJScrollPane1(), "Center");
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
			ivjJPanel3.setFont(new java.awt.Font("serif", 0, 12));
			ivjJPanel3.setLayout(new java.awt.FlowLayout());
			ivjJPanel3.add(getJTextField1());
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
 * Return the JScrollPane1 property value.
 * @return javax.swing.JScrollPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JScrollPane getJScrollPane1() {
	if (ivjJScrollPane1 == null) {
		try {
			ivjJScrollPane1 = new javax.swing.JScrollPane();
			ivjJScrollPane1.setName("JScrollPane1");
			ivjJScrollPane1.setFont(new java.awt.Font("serif", 0, 12));
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
 * Return the JTextField1 property value.
 * @return javax.swing.JTextField
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextField getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField1.setColumns(40);
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
 * Return the JTextPane1 property value.
 * @return javax.swing.JTextPane
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JTextPane getJTextPane1() {
	if (ivjJTextPane1 == null) {
		try {
			ivjJTextPane1 = new javax.swing.JTextPane();
			ivjJTextPane1.setName("JTextPane1");
			ivjJTextPane1.setBackground(new java.awt.Color(204,204,204));
			ivjJTextPane1.setBounds(0, 0, 10, 10);
			ivjJTextPane1.setEditable(false);
			// user code begin {1}
			StyleContext sc = new StyleContext();
	
			normalStyle = sc.addStyle(null, null);
			StyleConstants.setForeground(normalStyle, java.awt.Color.black);
			StyleConstants.setFontFamily(normalStyle, "SansSerif");
			StyleConstants.setBold(normalStyle, true);
			doc=new DefaultStyledDocument(sc);
			ivjJTextPane1.setStyledDocument(doc);
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
 * This method was writen by yhwu.
 * @return java.lang.String
 */
public String getText()
{
	setVisible(true);
	return theText;
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
	getJTextField1().addActionListener(this);
	getJButton1().addActionListener(this);
	getJButton2().addActionListener(this);
}
/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	// user code begin {1}
	// user code end
	setName("SwingInputBox");
	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
	setFont(new java.awt.Font("serif", 0, 12));
	setSize(448, 214);
	setTitle("Input Box");
	setContentPane(getJFrameContentPane());
	initConnections();
	// user code begin {2}
	// user code end
}
/**
 * Comment
 */
public void jTextField1_ActionPerformed() {
	theText=getJTextField1().getText();
	dispose();
	return;
}
/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		SwingInputBox aSwingInputBox;
		aSwingInputBox = new SwingInputBox();
		try {
			Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
			Class parmTypes[] = { java.awt.Window.class };
			Object parms[] = { aSwingInputBox };
			java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
			aCtor.newInstance(parms);
		} catch (java.lang.Throwable exc) {};
		aSwingInputBox.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JFrame");
		exception.printStackTrace(System.out);
	}
}
}
