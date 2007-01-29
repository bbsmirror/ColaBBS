package colabbs.bbsclient.swing;

import java.io.*;
import java.net.*;
public class SwingChatApplet extends javax.swing.JApplet implements java.awt.event.ActionListener, Runnable
{
	private SwingChat mySwingChat=null;
	private Socket mySocket = null;
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
	private javax.swing.JPanel ivjJAppletContentPane = null;
	private javax.swing.JButton ivjJButton1 = null;
	private javax.swing.JLabel ivjJLabel1 = null;
	private javax.swing.JTextField ivjJTextField1 = null;
	private static java.util.ResourceBundle resChatRoom = java.util.ResourceBundle.getBundle("ChatRoom"); //$NON-NLS-1$;
public SwingChatApplet() {
	super();
}
public void actionPerformed(java.awt.event.ActionEvent e) {
	// user code begin {1}
	// user code end
	if ((e.getSource() == getJButton1()) ) {
		connEtoC1(e);
	}
	if ((e.getSource() == getJTextField1()) ) {
		connEtoC4(e);
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
private void connEtoC2() {
	try {
		// user code begin {1}
		// user code end
		this.swingChatApplet_Destroy();
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}
private void connEtoC4(java.awt.event.ActionEvent arg1) {
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
public String getAppletInfo() {
	return "colabbs.bbsclient.SwingChatApplet created using VisualAge for Java.";
}
private javax.swing.JPanel getJAppletContentPane() {
	if (ivjJAppletContentPane == null) {
		try {
			ivjJAppletContentPane = new javax.swing.JPanel();
			ivjJAppletContentPane.setName("JAppletContentPane");
			ivjJAppletContentPane.setLayout(null);
			getJAppletContentPane().add(getJTextField1(), getJTextField1().getName());
			getJAppletContentPane().add(getJLabel1(), getJLabel1().getName());
			getJAppletContentPane().add(getJButton1(), getJButton1().getName());
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	};
	return ivjJAppletContentPane;
}
private javax.swing.JButton getJButton1() {
	if (ivjJButton1 == null) {
		try {
			ivjJButton1 = new javax.swing.JButton();
			ivjJButton1.setName("JButton1");
			ivjJButton1.setText("Enter Chat!");
			ivjJButton1.setBounds(15, 70, 178, 27);
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
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("Enter Your Nick Name : ");
			ivjJLabel1.setBounds(13, 12, 251, 20);
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
private javax.swing.JTextField getJTextField1() {
	if (ivjJTextField1 == null) {
		try {
			ivjJTextField1 = new javax.swing.JTextField();
			ivjJTextField1.setName("JTextField1");
			ivjJTextField1.setFont(new java.awt.Font("serif", 0, 12));
			ivjJTextField1.setBounds(15, 34, 179, 29);
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
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	// exception.printStackTrace(System.out);
}
/**
 * Handle the Applet init method.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void init() {
	try {
		setName("SwingChatApplet");
		setSize(226, 117);
		setContentPane(getJAppletContentPane());
		initConnections();
		// user code begin {1}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {2}
		// user code end
		handleException(ivjExc);
	}
}
/**
 * Initializes connections
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() {
	// user code begin {1}
	// user code end
	getJButton1().addActionListener(this);
	getJTextField1().addActionListener(this);
}
public void jButton1_ActionPerformed()
{
	if(getJTextField1().getText().length()==0)
	{
		new SwingMessageBox("System Message!","Please Input Your Nick Name!");
		return;
	}
	if(mySocket!=null)
	{
		try
		{
			if(oos!=null)
			{
				oos.close();
				oos=null;
			}
			if(ois!=null)
			{
				ois.close();
				ois=null;
			}
			mySocket.close();
			mySocket=null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	try
	{
//		mySocket=new Socket("140.113.122.2",5253); //for debug!
		mySocket=new Socket(getCodeBase().getHost(),5253);
		oos=new ObjectOutputStream(mySocket.getOutputStream());
		ois=new ObjectInputStream(mySocket.getInputStream());
		mySwingChat.myCodeBase=getCodeBase().toString();
		mySwingChat=new SwingChat(oos,getJTextField1().getText());
		mySwingChat.setVisible(true);
		(new Thread(this)).start();
	}
	catch(Exception e)
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
		java.awt.Frame frame;
		try {
			Class aFrameClass = Class.forName("com.ibm.uvm.abt.edit.TestFrame");
			frame = (java.awt.Frame)aFrameClass.newInstance();
		} catch (java.lang.Throwable ivjExc) {
			frame = new java.awt.Frame();
		}
		SwingChatApplet aSwingChatApplet;
		Class iiCls = Class.forName("colabbs.bbsclient.swing.SwingChatApplet");
		ClassLoader iiClsLoader = iiCls.getClassLoader();
		aSwingChatApplet = (SwingChatApplet)java.beans.Beans.instantiate(iiClsLoader,"colabbs.bbsclient.swing.SwingChatApplet");
		frame.add("Center", aSwingChatApplet);
		frame.setSize(aSwingChatApplet.getSize());
		frame.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JApplet");
		exception.printStackTrace(System.out);
	}
}
public void run()
{
	if(mySwingChat==null)
		return;
	try
	{
		Object cmd=null;
		
		while(ois!=null)
		{
			cmd=ois.readObject();
			mySwingChat.serverCommand(cmd);
			if(cmd!=null)
			{
			}
		}
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
public void swingChatApplet_Destroy() {
	if(mySocket!=null)
	{
		try
		{
			if(oos!=null)
			{
				oos.close();
				oos=null;
			}
			if(ois!=null)
			{
				ois.close();
				ois=null;
			}
			mySocket.close();
			mySocket=null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	return;
}
}
