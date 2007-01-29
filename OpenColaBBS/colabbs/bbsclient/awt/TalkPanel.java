
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.awt;

import java.io.*;
import java.awt.*;

import colabbs.bbsclient.ClientUtils;
import java.awt.event.*;
import java.util.*;

public class TalkPanel extends Panel implements Runnable
{
	private boolean running=true;
	private InputStreamReader isr=null;
  private OutputStreamWriter osw=null;
//
	GridLayout gridLayout1 = new GridLayout();
	Panel panel1 = new Panel();
	Panel panel2 = new Panel();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	TextArea textArea1 = new TextArea();
	TextArea textArea2 = new TextArea();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	Panel panel6 = new Panel();
	Panel panel7 = new Panel();
	Panel panel8 = new Panel();
	public Label labelStatus = new Label();
	private transient Vector actionListeners;

	public TalkPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

  public void doTalk(InputStream theis,OutputStream theos)
  {
  	textArea1.setText("");
  	textArea2.setText("");
  	try
    {
	  	isr=new InputStreamReader(theis,ClientUtils.myEncoding);
  	  osw=new OutputStreamWriter(theos,ClientUtils.myEncoding);
      (new Thread(this)).start();
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    	labelStatus.setText("發生錯誤無法進行聊天，請通知站長!");
    }
  }

	private void jbInit() throws Exception
	{
		gridLayout1.setRows(2);
		this.setLayout(gridLayout1);
		panel2.setLayout(borderLayout1);
		panel1.setLayout(borderLayout2);
		textArea1.setBackground(Color.black);
		textArea1.setForeground(Color.white);
		textArea1.addKeyListener(new java.awt.event.KeyAdapter()
		{


			public void keyPressed(KeyEvent e)
			{
				textArea1_keyPressed(e);
			}

			public void keyReleased(KeyEvent e)
			{
				textArea1_keyReleased(e);
			}

			public void keyTyped(KeyEvent e)
			{
				textArea1_keyTyped(e);
			}
		});
		textArea2.setBackground(Color.black);
		textArea2.setForeground(Color.white);
		textArea2.setEditable(false);
		labelStatus.setText("系統處理中請稍候...");
		this.add(panel1, null);
		panel1.add(textArea1, BorderLayout.CENTER);
		panel1.add(panel3, BorderLayout.EAST);
		panel1.add(panel4, BorderLayout.WEST);
		panel1.add(panel5, BorderLayout.NORTH);
		this.add(panel2, null);
		panel2.add(textArea2, BorderLayout.CENTER);
		panel2.add(panel6, BorderLayout.EAST);
		panel2.add(panel7, BorderLayout.WEST);
		panel2.add(panel8, BorderLayout.NORTH);
		panel8.add(labelStatus, null);
	}
  
	public void run()
	{
		//TODO: implement this java.lang.Runnable method;
  	try
    {
//	  	isr=new InputStreamReader(theis,ClientUtils.myEncoding);
//  	  osw=new OutputStreamWriter(theos,ClientUtils.myEncoding);
    	char buf=0;
      running=true;
	    while(running&&(buf=(char)isr.read())!=(char)-1)
  	  {
//      	System.out.println(""+(int)buf);
    		switch(buf)
      	{
      		case 8:
	        {
	        	String buf1=textArea2.getText();
  	      	String buf2=(new Character(buf1.charAt(buf1.length() - 1))).toString();
    	      if(buf2.getBytes(ClientUtils.myEncoding).length==2)
            {
	            String nowText=textArea2.getText();
  	  	      if(nowText.length()>0)
	  	  	    	textArea2.setText(nowText.substring(0,nowText.length()-1)+" ");
            }
            else
            {
	            String nowText=textArea2.getText();
  	  	      if(nowText.length()>0)
	  	  	    	textArea2.setText(nowText.substring(0,nowText.length()-1));
            }
        		break;
	        }
  	      default:
    	    	textArea2.append((new Character(buf)).toString());
      	  	break;
	      }
  	  }
      System.out.println(""+(int)buf);
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    	labelStatus.setText("發生錯誤無法進行聊天，請通知站長!");
    }
    fireActionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"ACTION_PERFORMED"));
	}

  public void quitTalk()
  {
  	try
    {
    	osw.close();
      isr.close();
    }
    catch(Exception e){}
    running=false;
  }

	void textArea1_keyPressed(KeyEvent e)
	{
  	try
    {
	  	switch(e.getKeyCode())
  	  {
    		case KeyEvent.VK_PAGE_UP:
    		case KeyEvent.VK_PAGE_DOWN:
	    	case KeyEvent.VK_HOME:
  	  	case KeyEvent.VK_END:
    		case KeyEvent.VK_UP:
	    	case KeyEvent.VK_DOWN:
  	  	case KeyEvent.VK_LEFT:
    		case KeyEvent.VK_RIGHT:
    		case KeyEvent.VK_DELETE:
      		e.consume();
					break;
  	  	case KeyEvent.VK_ENTER:
    	  	osw.write(13);
		      osw.flush();
      		break;
	    	case KeyEvent.VK_BACK_SPACE:
        {
        	String buf=textArea1.getText();
        	String buf1=(new Character(buf.charAt(buf.length() - 1))).toString();
          if(buf1.getBytes(ClientUtils.myEncoding).length==2)
	    	    osw.write(8);
//		      osw.flush();
  	    	break;
        }
    		default:
	      	break;
  	  }
    }
    catch(IOException e1){}
//    System.out.println("key pressed:action="+e.isActionKey()+",char="+e.getKeyChar()+",code="+e.getKeyCode());
	}

	void textArea1_keyReleased(KeyEvent e)
	{
//    System.out.println("key released:action="+e.isActionKey()+",char="+e.getKeyChar()+",code="+e.getKeyCode());
	}

	void textArea1_keyTyped(KeyEvent e)
	{
  	try
    {
	  	osw.write(e.getKeyChar());
      osw.flush();
    }
    catch(IOException e1){}
//    System.out.println("key typed:action="+e.isActionKey()+",char="+e.getKeyChar()+",code="+e.getKeyCode());
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
}
