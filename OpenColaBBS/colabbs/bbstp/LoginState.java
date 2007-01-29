package colabbs.bbstp;

import java.io.Serializable;

public class LoginState implements UniModuleReply
{
  public int State=0;
  public int Permission=0;
  public int SignatureNumber=0;

  public LoginState(int theState)
  {
    State=theState;
  }

  public LoginState(int theState,int thePermission,int theSignatureNumber)
  {
    State=theState;
    Permission=thePermission;
    SignatureNumber=theSignatureNumber;
  }
}