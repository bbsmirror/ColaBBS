
//Title:        Cola Bulletin Board System
//Version:      
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp;

import java.io.Serializable;

public interface MultiModuleCmd extends Serializable
{
  public int getID();
  public void setID(int theID);
} 