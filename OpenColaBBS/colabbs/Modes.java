package colabbs;

import java.net.*;
import java.io.*;
import java.util.*;

public class Modes
{
	public static String List;
	public static String Talk;
	public static String Chat;
	public static String SendMsg;
	public static String KeyMsg;
	public static String ReplyMsg;
	public static String MainMenu;
	public static String Mail;
	public static String MailList;
	public static String Post;
	public static String PostList;
	public static String BoardList;
	public static String Login;
	public static String ReadNote;
	public static String SetFriend;
	public static String GroupsList;
	public static String AnnounceList;
	public static String Setup;
	public static String Logout;
	public static String WriteNote;
	public static String MailToSysop;

	public static void InitModes()
	{
		List = Prompt.Msgs[1];
		Talk = Prompt.Msgs[2];
		SendMsg = Prompt.Msgs[3];
		KeyMsg = Prompt.Msgs[4];
		ReplyMsg = Prompt.Msgs[5];
		MainMenu = Prompt.Msgs[6];
		Mail = Prompt.Msgs[7];
		MailList = Prompt.Msgs[8];
		Post = Prompt.Msgs[9];
		PostList = Prompt.Msgs[10];
		BoardList = Prompt.Msgs[11];
		Login = Prompt.Msgs[12];
		ReadNote = Prompt.Msgs[13];
		SetFriend = Prompt.Msgs[336];
		GroupsList = Prompt.Msgs[354];
		AnnounceList = Prompt.Msgs[362];
		Setup = Prompt.Msgs[378];
		Chat = Prompt.Msgs[448];
		Logout = Prompt.Msgs[435];
		WriteNote = Prompt.Msgs[436];
		MailToSysop = Prompt.Msgs[437];
	}
}