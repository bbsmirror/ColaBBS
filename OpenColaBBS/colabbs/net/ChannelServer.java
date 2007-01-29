
//Title:        Cola Bulletin Board System
//Version:
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1995-1999 Ying-haur Wu. All Rights Reserved.
//Bugs report to is85003@cis.nctu.edu.tw.
package colabbs.net;

import java.io.*;
import java.net.*;

import colabbs.bbstp.*;
import colabbs.bbsclient.*;

public class ChannelServer extends NetworkServer
{
	public boolean keepRun=true;
	public CmdTable myCmdTable=new CmdTable();
	private ObjectInputStream ois=null;
	private ObjectOutputStream oos=null;

	public ChannelServer(int port,InetAddress addr,String ThreadName) throws IOException
	{
  	super();
    startServer(port,addr,ThreadName);
	}

	public ChannelServer(int port,String ThreadName) throws IOException
	{
  	super();
    startServer(port,ThreadName);
	}

	public ChannelServer(int port) throws IOException
	{
  	super();
    startServer(port,"ChannelServer");
	}

/**
 * 送出一個命令到客戶端
 */
  public void sendCmd(Object theObj)
  {
    if(oos!=null)
    {
    	synchronized(oos)
      {
	      try
  	    {
    	    oos.writeObject(theObj);
      	}
	      catch(IOException e)
  	    {
    	    e.printStackTrace();
      	}
      }
    }
  }

/**
 * 回應客戶端的命令
 */
  public synchronized void replyCmd(Object source,Object theCmd)
  {
    if(source instanceof MultiModuleCmd)
    {
      int dest=((MultiModuleCmd)source).getID();
      if(theCmd instanceof MultiModuleReply)
        ((MultiModuleReply)theCmd).setID(dest);
    }
    sendCmd(theCmd);
  }

	public void doService()
  {
		try
		{
	  	ois=new ObjectInputStream(is);
			oos=new ObjectOutputStream(os);

			Object cmd=null;
			while(ois!=null&&keepRun)
			{
				cmd=ois.readObject();
				try
				{
          if(myCmdTable!=null)
          {
            CmdItem myItem=(CmdItem)myCmdTable.getItem(cmd);
            if(myItem!=null)
            {
/*              if(myItem.myObject!=null)
                myItem.myMethod.invoke(myItem.myObject,new Object[]{cmd});
              else
                myItem.myMethod.invoke(this,new Object[]{cmd});*/
              if(myItem.myObject!=null)
                myItem.myMethod.invoke(myItem.myObject,new Object[]{this,cmd});
              else
                myItem.myMethod.invoke(this,new Object[]{this,cmd});
            }
					}
				}
				catch(ClassCastException e)
				{
					e.printStackTrace();
				}
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
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
  }
}