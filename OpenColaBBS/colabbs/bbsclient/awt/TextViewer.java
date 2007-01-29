
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.

package colabbs.bbsclient.awt;

import java.awt.*;
import java.io.*;
import java.net.*;

import colabbs.bbstp.Login;
import colabbs.bbstp.LoginState;
import colabbs.bbstp.board.BBSTPReadPost;
import colabbs.bbstp.board.BBSTPPostSize;
import colabbs.bbstp.mail.BBSTPReadMail;
import colabbs.bbstp.mail.BBSTPMailSize;
import colabbs.bbsclient.ClientUtils;

public class TextViewer extends Panel
{
	BorderLayout borderLayout1 = new BorderLayout();
	Panel panel1 = new Panel();
	BorderLayout borderLayout2 = new BorderLayout();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	TextArea textArea1 = new TextArea();

	public TextViewer()
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

  public void ReadPost(String Addr,int theType,String UserName,String PassWord,String BoardName,int index)
  {
  	Socket mySocket=null;
    OutputStream os=null;
    ObjectOutputStream oos=null;
    InputStream is=null;
    ObjectInputStream ois=null;

  	try
    {
    	textArea1.setText("資料傳輸中，請稍候....\n");
    	mySocket=new Socket(Addr,5254);
      os=mySocket.getOutputStream();
      oos=new ObjectOutputStream(os);
      is=mySocket.getInputStream();
      ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadPost(BoardName,index));
				BBSTPPostSize ps=(BBSTPPostSize)ois.readObject();

        byte buffer[]=new byte[ps.size];
//        int count=ps.size,readed=0,index=0;
        for(int c=0;c<ps.size;c++)
        	buffer[c]=(byte)is.read();
//        System.out.println("File size : "+buffer.length+","+readed+" bytes readed.");
        String data=new String(buffer,ClientUtils.myEncoding);
        textArea1.setEditable(false);
        textArea1.setText(ClientUtils.CutAnsiCode(data));
      }
    }
    catch(Exception e)
    {
    	e.printStackTrace();
    }
    finally
    {
    	try
      {
	    	if(ois!=null)
        {
	  	    ois.close();
          ois=null;
        }
        if(oos!=null)
        {
	    	  oos.close();
          oos=null;
        }
        if(is!=null)
        {
        	is.close();
          is=null;
        }
        if(os!=null)
        {
        	os.close();
          os=null;
        }
        if(mySocket!=null)
        {
	      	mySocket.close();
          mySocket=null;
        }
	      System.gc();
      }
      catch(IOException e)
      {
      	e.printStackTrace();
      }
    }
  }

  public void ReadMail(String Addr,int theType,String UserName,String PassWord,int index)
  {
  	Socket mySocket=null;
    OutputStream os=null;
    ObjectOutputStream oos=null;
    InputStream is=null;
    ObjectInputStream ois=null;

  	try
    {
    	textArea1.setText("資料傳輸中，請稍候....");
    	mySocket=new Socket(Addr,5254);
      os=mySocket.getOutputStream();
      oos=new ObjectOutputStream(os);
      is=mySocket.getInputStream();
      ois=new ObjectInputStream(is);

      oos.writeObject(new Login(theType,UserName,PassWord));
      LoginState ls=(LoginState)ois.readObject();
      if(ls.State==0)
      {
      	oos.writeObject(new BBSTPReadMail(index));
				BBSTPMailSize ms=(BBSTPMailSize)ois.readObject();
        byte buffer[]=new byte[ms.size];
        for(int c=0;c<ms.size;c++)
        	buffer[c]=(byte)is.read();
//        int readed=is.read(buffer);
//        System.out.println("File size : "+buffer.length+","+readed+" bytes readed.");
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
    finally
    {
    	try
      {
	    	if(ois!=null)
        {
	  	    ois.close();
          ois=null;
        }
        if(oos!=null)
        {
	    	  oos.close();
          oos=null;
        }
        if(is!=null)
        {
        	is.close();
          is=null;
        }
        if(os!=null)
        {
        	os.close();
          os=null;
        }
        if(mySocket!=null)
        {
	      	mySocket.close();
          mySocket=null;
        }
	      System.gc();
      }
      catch(IOException e)
      {
      	e.printStackTrace();
      }
    }
  }

  public String readData()
  {
  	return textArea1.getText();
  }

	private void jbInit() throws Exception
	{
		this.setLayout(borderLayout1);
		panel1.setLayout(borderLayout2);
		textArea1.setText("傳輸中，請稍候....");
		this.add(panel1, BorderLayout.CENTER);
		panel1.add(panel4, BorderLayout.NORTH);
		panel1.add(panel3, BorderLayout.WEST);
		panel1.add(panel5, BorderLayout.EAST);
		panel1.add(textArea1, BorderLayout.CENTER);
	}

}
