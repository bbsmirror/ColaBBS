package colabbs.telnet;

import colabbs.*;
import colabbs.UTILS.*;

public class TelnetUtils 
{
	public static void ReDraw(int pid)
	{
		TelnetUser User = (TelnetUser)ColaServer.BBSUsers[pid];
		if(User.CurrentEditor!=null)
		{
			User.CurrentEditor.ReDrawScreen();
			if(User.GetData!=null&&User.GetData.Busy)
				User.GetData.ReDraw();
			return;
		}

		if(User.usermode==Modes.List)
		{
			User.printendline();
			if(!User.MsgMode)
			{
				User.move(1,1);
				User.printtitle(Prompt.Msgs[82]);
				User.move(1,2);
				User.sends(Prompt.Msgs[83]);
				User.sends(Prompt.Msgs[84]);
			}
		}
		else if(User.usermode==Modes.MainMenu)
		{
			if(User.MsgMode)
				User.printendline();
			else
				User.BBSMainMenu.ReDraw();
		}
		else if((User.usermode==Modes.MailList)||(User.usermode==Modes.PostList))
		{
			User.printendline();
			if(!User.MsgMode)
				if(User.DirListBuf!=null)
					User.DirListBuf.ReDraw();
		}
		else if(User.usermode==Modes.BoardList)
		{
			User.printendline();
			if(!User.MsgMode)
			{
				User.move(1,1);
				User.printtitle(Prompt.Msgs[72]);
				User.move(1,2);
				User.sends(Prompt.Msgs[73]);
				User.sends(Prompt.Msgs[74]);
			}
		}
		else if(User.usermode.length()>=Modes.Talk.length()&&User.usermode.substring(0,Modes.Talk.length()).equals(Modes.Talk))
		{
			User.move(1,12);
			User.sends("[1;46m[35m      [32m"+STRING.Cut(User.UFD.ID,13)+"[33m([37m"+STRING.Cut(User.UFD.NickName,16)+"[33m)[35m V.S. [32m"+User.usermode.substring(Modes.Talk.length())+"      [m");
			User.printendline();
		}

		if(User.usermode==Modes.KeyMsg)
		{
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[196]);
		}
		else if(User.usermode==Modes.SendMsg)
		{
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[197]);
		}
		else if(User.usermode==Modes.ReplyMsg)
		{
			User.move(1,24);
			User.clrtoeol();
			User.sends(Prompt.Msgs[222]+ColaServer.BBSUsers[User.MsgID].UFD.ID+": ");
			//            User.sends(Prompt.Msgs[198]+User.MsgID+":",pid);
		}
		if(User.GetData!=null&&User.GetData.Busy)
			User.GetData.ReDraw();
	}
	/*    public static void ReDraw(int pid)
	{
	switch(User.ReDrawMode)
	{
	case 1:
	User.printendline();
	if(!User.MsgMode)
	{
	User.move(1,1);
	User.printtitle("¨Ï¥ÎªÌ¦Cªí",pid);
	User.sends("  ²á¤Ñ[[1;32mt[m] ±H«H[[1;32mm[m] °e°T®§[[1;32ms[m] ¥[,´îªB¤Í[[1;32mo[m,[1;32md[m] ¬Ý»¡©úÀÉ[[1;32m¡÷[m,[1;32mr[m] ¤Á´«¼Ò¦¡ [[1;32mf[m] ¨D±Ï[[1;32mh[m]  \r\n",pid);
	User.sends("[1;44m[1;33m ½s ¸¹ ¨Ï¥ÎªÌ¥N¸¹   ¨Ï¥ÎªÌ¼ÊºÙ       ¨Ó¦Û             P M °ÊºA             ®É:¤À[m\r\n",pid);
	}
	break;
	case 2:
	if(User.MsgMode)
	User.printendline();
	else
	User.BBSMainMenu.ReDraw();
	break;
	}

	if(User.CurrentEditor!=null)
	User.CurrentEditor.ReDrawScreen();

	if(User.usermode==Modes.KeyMsg)
	{
	User.move(1,24);
	User.clrtoeol();
	User.sends("°T®§:",pid);
	}
	else if(User.usermode==Modes.SendMsg)
	{
	User.move(1,24);
	User.clrtoeol();
	User.sends("°e°T®§µ¹(½s¸¹):",pid);
	}
	else if(User.usermode==Modes.ReplyMsg)
	{
	User.move(1,24);
	User.clrtoeol();
	User.sends("¦^°T®§µ¹"+User.MsgID+":",pid);
	}
	if(User.GetData!=null&&User.GetData.Busy)
	User.GetData.ReDraw();
	}
	public static void SetUserMode(int pid,String Mode)
	{
	User.usermode=Mode;
	if(User.usermode==Modes.List)
	User.ReDrawMode=1;
	else if(User.usermode==Modes.MainMenu)
	User.ReDrawMode=2;
	}*/
	public static int UserDef(int OldPerm,int pid)
	{
		TelnetUser User = (TelnetUser)ColaServer.BBSUsers[pid];
		int i;

		User.Clear();
		User.move(1,5);
		User.sends(Prompt.Msgs[176]);
		//        User.move(1,7);
		User.sends(Prompt.Msgs[177]);
		User.sends(Prompt.Msgs[178]);
		User.sends(Prompt.Msgs[179]);
		User.sends(Prompt.Msgs[180]);
		User.sends(Prompt.Msgs[181]);
		User.sends(Prompt.Msgs[182]);
		//Put User Define to 324~333
		User.sends(Prompt.Msgs[324]);

		User.move(1,23);
		User.sends(Prompt.Msgs[142]);
		//        for(i=0;i<32;i++)
		for(i=0;i<7;i++)
		{
			if(i<16)
				User.move(34,7+i);
			else
				User.move(74,i-9);
			if(((OldPerm>>>i)&1)==0)
				User.sends("[1;32mOFF");
			else
				User.sends("[1;32m ON");
		}

		while(true)
		{
			int choice;

			User.move(19,23);
			User.sends(" ");
			User.move(19,23);
			choice=(int)User.MakeSure();
			if(choice==0)
				break;
			choice-=(int)'A';
			//            if(choice>=32)
			if(choice>=7)
				continue;

			if(choice<16)
				User.move(34,7+choice);
			else
				User.move(74,choice-9);
			OldPerm^=(1<<choice);
			if(((OldPerm>>>choice)&1)==0)
				User.sends("[1;32mOFF");
			else
				User.sends("[1;32m ON");
		}

		return OldPerm;
	}
}