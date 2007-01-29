package colabbs.bbstp;

import java.io.Serializable;

public class Login implements UniModuleCmd
{
  public int clientType=0; //1:Applet,2:JApplet,3:Java Application,4:Native Application
  public String UserName=null;
  public String PassWord=null;
  public String Email="";
  public boolean newID=false;

  public Login(int theType,String theUserName,String thePassWord)
  {
    clientType=theType;
    UserName=theUserName;
    PassWord=thePassWord;
    newID=false;
  }

  public Login(int theType,String theUserName,String thePassWord,boolean thenewID,String theEmail)
  {
    clientType=theType;
    UserName=theUserName;
    PassWord=thePassWord;
    newID=thenewID;
    Email=theEmail;
  }
}