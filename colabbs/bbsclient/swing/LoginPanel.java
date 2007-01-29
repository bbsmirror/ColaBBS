package colabbs.bbsclient.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;

public class LoginPanel
{
  //BorderLayout borderLayout1 = new BorderLayout();
  private java.awt.Image myLogo;
  private transient Vector actionListeners;

  public LoginPanel()
  {
    try
    {
      //jbInit();
      //this.setEnabled(true);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception
  {
    //this.setLayout(borderLayout1);
  }

  public void setMyLogo(java.awt.Image newMyLogo)
  {
    myLogo = newMyLogo;
  }

  public java.awt.Image getMyLogo()
  {
    return myLogo;
  }

  public synchronized void removeActionListener(ActionListener l)
  {
    if(actionListeners != null && actionListeners.contains(l))
    {
      Vector v = (Vector) actionListeners.clone();
      v.removeElement(l);
      actionListeners = v;
    }
  }

  public synchronized void addActionListener(ActionListener l)
  {
    Vector v = actionListeners == null ? new Vector(2) : (Vector) actionListeners.clone();
    if(!v.contains(l))
    {
      v.addElement(l);
      actionListeners = v;
    }
  }

  protected void fireActionPerformed(ActionEvent e)
  {
    if(actionListeners != null)
    {
      Vector listeners = actionListeners;
      int count = listeners.size();
      for (int i = 0; i < count; i++)
      {
        ((ActionListener) listeners.elementAt(i)).actionPerformed(e);
      }
    }
  }

	protected boolean ValidID(String str)
	{
		int i;
		char buf;

		if(str.length()==0)
			return false;
		for(i=0;i<str.length();i++)
		{
			buf=Character.toUpperCase(str.charAt(i));
			if((buf<'A'||buf>'Z')&&(buf<'0'||buf>'9')&&(buf!='_'))
				return false;
		}
		if(str.toString().equalsIgnoreCase("new"))
			return false;
		else
			return true;
	}

  public static void main(String args[]){
    JFrame f = new JFrame();
    //f.getContentPane().add(new LoginPanel());
    f.pack();
    f.setVisible(true);
  }
}
