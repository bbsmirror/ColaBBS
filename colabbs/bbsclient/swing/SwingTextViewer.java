
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.swing;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.board.BBSTPReadPost;
import colabbs.bbstp.board.BBSTPPostSize;
import colabbs.bbstp.mail.BBSTPReadMail;
import colabbs.bbstp.mail.BBSTPMailSize;
import colabbs.bbsclient.ClientUtils;

public class SwingTextViewer extends JPanel {
  BorderLayout borderLayout1 = new BorderLayout();
	JPanel panel1 = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel panel3 = new JPanel();
	JPanel panel4 = new JPanel();
	JPanel panel5 = new JPanel();
	JTextArea textArea1 = new JTextArea();

  public SwingTextViewer() {
    try  {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLayout(borderLayout1);
		panel1.setLayout(borderLayout2);
		textArea1.setText("傳輸中，請稍候....");
		this.add(panel1, BorderLayout.CENTER);
		panel1.add(panel4, BorderLayout.NORTH);
		panel1.add(panel3, BorderLayout.WEST);
		panel1.add(panel5, BorderLayout.EAST);
		panel1.add(textArea1, BorderLayout.CENTER);
  }

  public void ReadPost(String Addr,int theType,String UserName,String PassWord,String BoardName,int index)
  {
  	try
    {
    	textArea1.setText("資料傳輸中，請稍候....");
    	Socket mySocket=new Socket(Addr,5254);
      OutputStream os=mySocket.getOutputStream();
      ObjectOutputStream oos=new ObjectOutputStream(os);
      InputStream is=mySocket.getInputStream();
      ObjectInputStream ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadPost(BoardName,index));
				BBSTPPostSize ps=(BBSTPPostSize)ois.readObject();
        byte buffer[]=new byte[ps.size];
        is.read(buffer);
        System.out.println("File size : "+buffer.length);
//        for(int i=0;i<buffer.length;i++)
//        	System.out.println((int)buffer[i]);
        String data=new String(buffer,ClientUtils.myEncoding);
        textArea1.setEditable(false);
        textArea1.setText(ClientUtils.CutAnsiCode(data));
      }
      ois.close();
      oos.close();
      mySocket.close();
      System.gc();
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
  }

  public void ReadMail(String Addr,int theType,String UserName,String PassWord,int index)
  {
  	try
    {
    	textArea1.setText("資料傳輸中，請稍候....");
    	Socket mySocket=new Socket(Addr,5254);
      OutputStream os=mySocket.getOutputStream();
      ObjectOutputStream oos=new ObjectOutputStream(os);
      InputStream is=mySocket.getInputStream();
      ObjectInputStream ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadMail(index));
				BBSTPMailSize ms=(BBSTPMailSize)ois.readObject();
        byte buffer[]=new byte[ms.size];
        is.read(buffer);
        System.out.println("File size : "+buffer.length);
        String data=new String(buffer,ClientUtils.myEncoding);
        textArea1.setEditable(false);
        textArea1.setText(ClientUtils.CutAnsiCode(data));
      }
      ois.close();
      oos.close();
      mySocket.close();
      System.gc();
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
  }
}