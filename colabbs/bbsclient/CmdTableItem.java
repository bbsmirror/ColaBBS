
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbsclient;

import java.lang.reflect.*;
import java.util.*;
import colabbs.bbstp.MultiModuleCmd;
import colabbs.bbstp.MultiModuleReply;

/**
 * 命令表中的元素
 */
public class CmdTableItem
{
  private Hashtable registeredCmdTable=null;
  private CmdItem registeredCmd=null;

  public CmdTableItem()
  {
  }

  public CmdItem getItem(Object theCmd)
  {
  	if(registeredCmdTable!=null)
    {
	    if(theCmd instanceof MultiModuleCmd)
  	    return (CmdItem)registeredCmdTable.get(new Integer(((MultiModuleCmd)theCmd).getID()));
    	if(theCmd instanceof MultiModuleReply)
      {
/*				for (Enumeration e = registeredCmdTable.keys() ; e.hasMoreElements() ;)
        {
        	System.out.println(e.nextElement());
     		}*/
  	    return (CmdItem)registeredCmdTable.get(new Integer(((MultiModuleReply)theCmd).getID()));
      }
    }
    return registeredCmd;
  }

  public CmdItem registerCmd(int theid,CmdItem theCmdItem)
  {
//  	System.out.println(theCmdItem.myObject+":"+theid);
    if(registeredCmdTable==null)
      registeredCmdTable=new Hashtable();
    registeredCmdTable.put(new Integer(theid),theCmdItem);
    return null;
  }

  public CmdItem registerCmd(CmdItem theCmdItem)
  {
//  	System.out.println(theCmdItem.myObject+"!?");
    CmdItem old=registeredCmd;
    registeredCmd=theCmdItem;
    return old;
  }
}
