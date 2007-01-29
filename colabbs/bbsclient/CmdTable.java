
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.

package colabbs.bbsclient;

import java.util.Hashtable;
import java.util.Enumeration;
import java.lang.reflect.*;

/**
 * 管理命令表的類別
 */
public class CmdTable
{
  private Hashtable myCmdTable=null;
  private Hashtable registeredObject=null;
  private int idcounter=0;
  private static Class theMultiModuleCmd=null;
  private static Class theMultiModuleReply=null;

  static
  {
    try
    {
      theMultiModuleCmd=Class.forName("colabbs.bbstp.MultiModuleCmd");
      theMultiModuleReply=Class.forName("colabbs.bbstp.MultiModuleReply");
    }
    catch(ClassNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public CmdTable()
  {
    myCmdTable=new Hashtable();
    registeredObject=new Hashtable();
    idcounter=0;
  }

  public CmdItem getItem(Object theCmd)
  {
    CmdTableItem myTableItem=(CmdTableItem)myCmdTable.get(theCmd.getClass());
//    System.out.println(myTableItem);
//    System.out.println(theCmd.getClass().getName());
    CmdItem myItem=null;
    if(myTableItem!=null)
      myItem=myTableItem.getItem(theCmd);
//    System.out.println("myItem="+myItem);
    return myItem;
  }

  public int registerObject(Object theObject)
  {
    if(theObject==null)
      return -1;
    Integer myid=null;
    if((myid=(Integer)registeredObject.get(theObject))==null)
    {
      synchronized(this)
      {
        idcounter++;
        if(idcounter<=0)
          idcounter=1;
        myid=new Integer(idcounter);
      }
      registeredObject.put(theObject,myid);
    }
    return myid.intValue();
  }

  public void registerCmd(Class theClass,CmdItem theCmdItem)
  {
//    boolean multi=theClass.isAssignableFrom(theMultiModuleCmd);

    CmdTableItem tmp=(CmdTableItem)myCmdTable.get(theClass);
    if(tmp==null)
	    tmp=new CmdTableItem();
    if((theMultiModuleCmd.isAssignableFrom(theClass)||theMultiModuleReply.isAssignableFrom(theClass))&&theCmdItem.myObject!=null)
    {
      int myid=registerObject(theCmdItem.myObject);
//      System.out.println(myid);
      tmp.registerCmd(myid,theCmdItem);
    }
    else
      tmp.registerCmd(theCmdItem);
    myCmdTable.put(theClass,tmp);
  }
}

