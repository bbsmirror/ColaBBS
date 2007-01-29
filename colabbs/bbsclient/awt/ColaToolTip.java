package colabbs.bbsclient.awt;

import java.awt.event.*;
import java.applet.*;
import java.awt.*;
/**
 * This type was created in VisualAge.
 */
public class ColaToolTip extends Window implements WindowListener
{
	private boolean  firstShow = true;
	protected transient java.beans.PropertyChangeSupport propertyChange;
	private String fieldMsg = new String();
/**
 * Constructor
 */
public ColaToolTip()
{
	super(new Frame());
	pack();
	initialize();
}
/**
 * Constructor
 * @param parent Symbol
 */
public ColaToolTip(java.awt.Frame parent) {
	super(parent);
	pack();
	initialize();
}
/**
 * The addPropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().addPropertyChangeListener(listener);
}
/**
 * connEtoC1:  (PopUpMsg.window.windowClosing(java.awt.event.WindowEvent) --> PopUpMsg.dispose()V)
 * @param arg1 java.awt.event.WindowEvent
 */
private void connEtoC1(WindowEvent arg1)
{
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
 * The firePropertyChange method was generated to support the propertyChange field.
 */
public void firePropertyChange(String propertyName, Object oldValue, Object newValue)
{
	getPropertyChange().firePropertyChange(propertyName, oldValue, newValue);
}

	public static Frame frameForComponent(Component component)
  {
		while (!(component instanceof Frame)) {
			component = component.getParent();
		}
		return (Frame)component;
	}

	public Rectangle getBounds()
  {
		return super.getBounds();
	}
/**
 * Gets the Msg property (java.lang.String) value.
 * @return The Msg property value.
 * @see #setMsg
 */
public String getMsg()
{
	return fieldMsg;
}
/**
 * Accessor for the propertyChange field.
 */
protected java.beans.PropertyChangeSupport getPropertyChange()
{
	if (propertyChange == null)
  {
		propertyChange = new java.beans.PropertyChangeSupport(this);
	};
	return propertyChange;
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
	public void hide() {
	  super.hide();
	  /** We need to call removeNotify() here because hide() does something only if
	   *  Component.visible is true. When the app frame is miniaturized, the parent
	   *  frame of this frame is invisible, causing AWT to believe that this frame
	   *  is invisible and causing hide() to do nothing
	   */
	  removeNotify();
	}
/**
 * Initializes connections
 */
private void initConnections()
{
	// user code begin {1}
	// user code end
	this.addWindowListener(this);
}
/**
 * Initialize the class.
 */
private void initialize()
{
	// user code begin {1}
	// user code end
	setName("PopUpMsg");
	setLayout(null);
	setBackground(java.awt.Color.yellow);
	setSize(108, 26);
	initConnections();
	// user code begin {2}
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ColaToolTip aPopUpMsg = new colabbs.bbsclient.awt.ColaToolTip(new java.awt.Frame());
		try {
			Class aCloserClass = Class.forName("com.ibm.uvm.abt.edit.WindowCloser");
			Class parmTypes[] = { java.awt.Window.class };
			Object parms[] = { aPopUpMsg };
			java.lang.reflect.Constructor aCtor = aCloserClass.getConstructor(parmTypes);
			aCtor.newInstance(parms);
		} catch (java.lang.Throwable exc) {};
		aPopUpMsg.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of java.awt.Window");
		exception.printStackTrace(System.out);
	}
}
/**
 * This method was created by a SmartGuide.
 * @param g java.awt.Graphics
 */
public void paint (Graphics g)
{
	if(fieldMsg!=null)
		g.drawString(fieldMsg,0,12);
	return;
}
/**
 * The removePropertyChangeListener method was generated to support the propertyChange field.
 */
public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
	getPropertyChange().removePropertyChangeListener(listener);
}
/**
 * Sets the Msg property (java.lang.String) value.
 * @param Msg The new value for the property.
 * @see #getMsg
 */
public void setMsg(String Msg) {
	String oldValue = fieldMsg;
	fieldMsg = Msg;
	setSize(12*fieldMsg.length(),22);
	doLayout();
	firePropertyChange("Msg", oldValue, Msg);
}

	public void show(Component invoker, int thex, int they)
  {
  	int x=0;
    int y=0;

    if(invoker!=null)
    {
    	x=invoker.getLocationOnScreen().x;
      y=invoker.getLocationOnScreen().y;
    }
	  this.setLocation(x+thex,y+they);
	  this.setVisible(true);

	  /** This hack is to workaround a bug on Solaris where the windows does not really show
	   *  the first time
	   *  It causes a side effect of MS JVM reporting IllegalArumentException: null source
	   *  fairly frequently - also happens if you use HeavyWeight JPopup, ie JComboBox
	   */
	  if(firstShow)
    {
			this.hide();
			this.setVisible(true);
			firstShow = false;
	  }
	}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
public void windowActivated(WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
public void windowClosed(WindowEvent e) {
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
public void windowClosing(WindowEvent e)
{
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
public void windowDeactivated(WindowEvent e)
{
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
public void windowDeiconified(WindowEvent e) {
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
public void windowIconified(WindowEvent e)
{
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
/**
 * Method to handle events for the WindowListener interface.
 * @param e java.awt.event.WindowEvent
 */
public void windowOpened(WindowEvent e)
{
	// user code begin {1}
	// user code end
	// user code begin {2}
	// user code end
}
}