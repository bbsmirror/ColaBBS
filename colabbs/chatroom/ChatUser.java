package colabbs.chatroom;

public class ChatUser
{
	public boolean sysop=false,guest=false;
  public int mid=0;
	public String room=null,nick=null,ID=null,from=null;
	public ChatClient myClient=null;

	public ChatUser()
  {
		super();
	}

	public ChatUser(String theNick,String theID,String theFrom,String myRoom,ChatClient theClient,int themid)
	{
		super();
		nick=theNick;
		ID=theID;
		from=theFrom;
		room=myRoom;
//		room=new String(myRoom);
		myClient=theClient;
  	mid=themid;
	}
}