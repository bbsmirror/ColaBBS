
//Title:        Cola Bulletin Board System
//Version:      2.0
//Copyright:    Copyright (c) 1999
//Author:       Ying-haur Wu
//Company:      infoX and NCTUCIS
//Description:  Copyright (c) 1999 Ying-haur Wu. All Rights Reserved.
package colabbs.bbstp.board;

import colabbs.DATA.BOARD.BoardItem;
import colabbs.bbstp.*;
/**
 * SaveMode = true 轉信, false 站內
 */
public class BBSTPBoardItem extends MultiModuleReplyAdapter
{
	public String Name=null;
  public String CGroup=null;
//	public byte EGroup;
	public String Title=null;
	public String BM=null;
	public int Level = 8;
	public byte State = 0;

  public boolean HaveNew = false;
	public boolean Anonymous = true;
	public boolean AnonyDefault = true;
	public boolean NoZap = false;
	public boolean SaveMode = true;
	public boolean JunkBoard = false;

  public BBSTPBoardItem(String theName,String theTitle,String theBM,
  	int theLevel,byte theState,boolean theHaveNew,
    boolean theAnonymous,boolean theAnonyDefault,boolean theNoZap,boolean theSaveMode,
    boolean theJunkBoard)
  {
  	Name=theName;
    if(theTitle.length()>12)
    {
      Title=theTitle.substring(12);
      CGroup=theTitle.substring(0,12).trim();
    }
    else
    {
      Title="";
      CGroup=theTitle.trim();
    }
    BM=theBM;
    Level=theLevel;
    State=theState;
    HaveNew=theHaveNew;
    Anonymous=theAnonymous;
    AnonyDefault=theAnonyDefault;
    NoZap=theNoZap;
    SaveMode=theSaveMode;
    JunkBoard=theJunkBoard;
  }

/*  public BBSTPBoardItem(BoardItem theItem)
  {
    Name=theItem.Name;
    EGroup=theItem.EGroup;
    if(theItem.Title.length()>12)
    {
      Title=theItem.Title.substring(12);
      CGroup=theItem.Title.substring(0,12).trim();
    }
    else
    {
      Title=theItem.Title.substring(13);
      CGroup=theItem.Title.trim();
    }
    BM=theItem.BM;
    Level=theItem.Level;
    State=theItem.State;
    Anonymous=theItem.Anonymous;
    AnonyDefault=theItem.AnonyDefault;
    SaveMode=theItem.SaveMode;
    JunkBoard=theItem.JunkBoard;
  }*/
}
