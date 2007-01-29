package colabbs.chatroom;

import java.util.*;
public class RoomObj
{
	public boolean lock=false,hide=false,recording=false;
	public int count=0;
	public String name=null;
	public String title=null;
	public String op="";
	public Vector inviteList=new Vector(30);
public RoomObj() {
	super();
}
public RoomObj(String thename,String thetitle,String theop)
{
	name=thename;
	title=thetitle;
	op=theop;
}
}