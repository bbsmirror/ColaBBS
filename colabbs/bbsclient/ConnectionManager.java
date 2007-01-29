
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
public class ConnectionManager implements Runnable
{
	private ObjectInputStream ois = null;
	private ObjectOutputStream oos = null;
  private Socket mySocket=null;
  public CmdTable myCmdTable=new CmdTable();
  private Hashtable dataTable=new Hashtable();
  private String address=null;
  private String userName=null;
  private String passWord=null;
  private boolean newID;
  private int clientType=1;
	private String email;
	private int signatureNumber;
	private int permission;

  public ConnectionManager()
  {
  }

  public ConnectionManager(String theAddress,String theUserName,String thePassWord,boolean theNewID,String theEmail)
  {
    address=theAddress;
    userName=theUserName;
    passWord=thePassWord;
    newID=theNewID;
    email=theEmail;
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
      StatusBar.setMessage("連線中....");
	  	mySocket=new Socket(address,5253);
//      System.out.println("Before object output stream");
	  	oos=new ObjectOutputStream(mySocket.getOutputStream());
//      System.out.println("Before object input stream");
		  ois=new ObjectInputStream(mySocket.getInputStream());
      StatusBar.setMessage("登入中....");
      sendCmd(new Login(clientType,userName,passWord,newID,email));
      StatusBar.setMessage("讀取登入結果中....");
//      System.out.println("Before start thread");
      (new Thread(this)).start();
  	}
	  catch(Exception e1)
  	{
      StatusBar.setMessage("找不到此站臺!請確定站址是對的!");
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

  public void setUserName(String newUserName)
  {
    userName = newUserName;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setPassWord(String newPassWord)
  {
    passWord = newPassWord;
  }

  public String getPassWord()
  {
    return passWord;
  }

  public void setNewID(boolean newNewID)
  {
    newID = newNewID;
  }

  public boolean isNewID()
  {
    return newID;
  }

  public void run()
  {
    //TODO: implement this java.lang.Runnable method;
  	if(mySocket==null)
	  	return;
  	try
  	{
	  	Object cmd=null;

		  while((cmd=receiveCmd())!=null)
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
  	}
	  catch(Exception e)
  	{
	  	e.printStackTrace();
  	}
  }

  public void setClientType(int newClientType)
  {
    clientType = newClientType;
  }

  public int getClientType()
  {
    return clientType;
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
    userName=null;
    passWord=null;
    newID=false;
    clientType=1;
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

	public void setEmail(String newEmail)
	{
		email = newEmail;
	}

	public String getEmail()
	{
		return email;
	}

	public void setSignatureNumber(int newSignatureNumber)
	{
		signatureNumber = newSignatureNumber;
	}

	public int getSignatureNumber()
	{
		return signatureNumber;
	}

	public void setPermission(int newPermission)
	{
		permission = newPermission;
	}

	public int getPermission()
	{
		return permission;
	}
}
