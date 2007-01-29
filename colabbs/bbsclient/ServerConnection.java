
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient;

import java.io.*;
import java.net.*;
import java.util.*;

import colabbs.bbstp.*;

/**
 * 管理客戶端連結的類別
 */
public class ServerConnection implements Runnable
{
	public boolean keepRun=true;
  public CmdTable myCmdTable=new CmdTable();
//
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
  private Socket mySocket=null;
  private Hashtable dataTable=new Hashtable();
  private String address=null;
  private int port=1234;

  public ServerConnection()
  {
  }

  public ServerConnection(String theAddress,int theport)
  {
    address=theAddress;
    port=theport;
  }

  public void connect()
  {
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
  		catch(Exception e1)
	  	{
		  	e1.printStackTrace();
  		}
	  }
  	try
	  {
//      System.out.println("Before connection");
//      StatusBar.setMessage("連線中....");
	  	mySocket=new Socket(address,port);
//      System.out.println("Before object output stream");
	  	oos=new ObjectOutputStream(mySocket.getOutputStream());
//      System.out.println("Before object input stream");
		  ois=new ObjectInputStream(mySocket.getInputStream());
//      StatusBar.setMessage("登入中....");
//      sendCmd(new Login(clientType,userName,passWord,newID,email));
//      StatusBar.setMessage("讀取登入結果中....");
//      System.out.println("Before start thread");
      (new Thread(this)).start();
  	}
	  catch(Exception e1)
  	{
//      StatusBar.setMessage("找不到此站臺!請確定站址是對的!");
	  	e1.printStackTrace();
  	}
  }

  public void sendCmd(Object source,Object theCmd)
  {
    if(theCmd instanceof MultiModuleCmd)
    {
//	  	System.out.println("sendCmd:"+myCmdTable.registerObject(source));
      ((MultiModuleCmd)theCmd).setID(myCmdTable.registerObject(source));
    }
    sendCmd(theCmd);
  }

  private synchronized void sendCmd(Object theObj)
  {
    if(mySocket==null)
      connect();
    if(oos!=null)
    {
      try
      {
//        System.out.println("Before write object");
        oos.writeObject(theObj);
      }
      catch(IOException e)
      {
        e.printStackTrace();
      }
    }
  }

  private Object receiveCmd()
  {
    Object tmp=null;

//    if(mySocket==null)
//      connect();
    if(ois!=null)
    {
      try
      {
//        System.out.println("Before read object");
        tmp=ois.readObject();
        System.out.println("receive cmd:"+tmp);
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
    }
    return tmp;
  }

  public void setAddress(String newAddress)
  {
    address = newAddress;
  }

  public String getAddress()
  {
    return address;
  }

  public void setPort(int newport)
  {
    port = newport;
  }

  public int getUserName()
  {
    return port;
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
  	if(mySocket==null)
	  	return;
  	try
  	{
	  	Object cmd=null;

		  while(keepRun&&(cmd=receiveCmd())!=null)
  		{
        if(myCmdTable!=null)
        {
          CmdItem myItem=(CmdItem)myCmdTable.getItem(cmd);
          if(myItem!=null)
          {
//	          System.out.println(myItem.myObject);
            myItem.myMethod.invoke(myItem.myObject,new Object[]{/*this,*/cmd});
          }
        }
//		  	mySwingChat.serverCommand(cmd);
//			  if(cmd!=null)
//  			{
//	  		}
		  }
//      System.out.println("Closing Connection....");
  	}
	  catch(Exception e)
  	{
	  	e.printStackTrace();
  	}
    close();
  }

  public void close()
  {
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
  		catch(Exception e1)
	  	{
		  	e1.printStackTrace();
  		}
	  }
    address=null;
    port=1234;
    //reset data table....
    dataTable=new Hashtable();
 }

  public void setData(String key,Object theData)
  {
    dataTable.put(key,theData);
  }

  public Object getData(String key)
  {
    return dataTable.get(key);
  }

}
