package colabbs.telnet;

import java.text.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;
import colabbs.DATA.VOTE.*;

public class ListVote extends ListMenu
{
	public final static int MODE_VOTE = 0;
	public final static int MODE_REPORT = 1;
	public final static int MODE_MANAGE = 2;
	
	private VoteList votelist;
	
	private Vector list;
	private int listsize;
	
	private int Mode = MODE_VOTE;

	TelnetUser User = null;

	private int now, cursor;
	private int nowold, cursorold;
	private int SCR_SIZE = 20;	

	private SimpleDateFormat df;
	
	private Vector getVoteList()
	{
		Vector vl = new Vector();
		Enumeration e = votelist.list.elements();
		VoteItem vi;
		
		while (e.hasMoreElements())
		{
			vi = (VoteItem)e.nextElement();
			switch (Mode)
			{
			case MODE_VOTE:
				if (vi.isVoting() == VoteItem.VOTE_VOTING_YES && !vi.isDeleted())
					vl.addElement(vi);
				break;
			case MODE_REPORT:
				if (vi.isVoting() == VoteItem.VOTE_VOTING_RESULT && !vi.isDeleted())
					vl.addElement(vi);
				break;
			case MODE_MANAGE:
				vl.addElement(vi);
				break;
			}
		}
		listsize = vl.size();
		return vl;
	}
	
	public ListVote(int pid, int xMode, VoteList xVL)
	{
		User = (TelnetUser)ColaServer.BBSUsers[pid];
		Mode = xMode;
		votelist = xVL;
		list = getVoteList();
		
		df = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
		df.setTimeZone(TimeZone.getTimeZone(ColaServer.INI.MyTimeZone));
	}
	
	private void redraw()
	{
		int i;
		VoteItem vi;
		
		int listmax = listsize;
		if (now + SCR_SIZE <= listmax)
			listmax = now + SCR_SIZE;
		
		drawtitle();
		User.move(1, 4);
		for (i = now; i < listmax; i++)
		{
			vi = (VoteItem)list.elementAt(i);
			if (i == now + cursor)
				User.sends("!!");
			else
				User.sends("  ");
			//User.sends((i + 1) + " " + vi.title + " " + ColaServer.DateFormatter2.format(new Date(vi.deadline)) + "\r\n");
			User.sends(getOutput(i + 1, vi));
			User.newline();
		}
	}
	
	private int switchkey(int inkey)
	{
		switch (Mode)
		{
		case ListVote.MODE_VOTE:
			switch (inkey)
			{
			case Keys.Right:
			case Keys.Enter:
				inkey = 'V';
				break;
			case 'R':
			case 'N':
			case 'D':
			case 'M':				
			case 'U':
			case 'I':
				inkey = -1;
				break;
			}
			break;
		case ListVote.MODE_REPORT:
			switch (inkey)
			{
			case Keys.Right:
			case Keys.Enter:
				inkey = 'R';
				break;
			case 'V':
			case 'N':
			case 'D':
			case 'M':
			case 'U':
			case 'I':
				inkey = -1;
				break;
			}
			break;
		case ListVote.MODE_MANAGE:
			switch (inkey)
			{
			case Keys.Right:
			case Keys.Enter:
				inkey = 'M';
				break;
			case 'V':
			case 'R':
				inkey = -1;
				break;
			}
			break;
		}
		return inkey;
	}
	
	private VoteItem getCurrentVoteItem()
	{
		return (VoteItem)list.elementAt(now + cursor);
	}
	
	private void drawtitle()
	{
		User.Clear();
		User.printtitle("test");
		User.move(1, 2);
		switch (Mode)
		{
		case ListVote.MODE_VOTE:
			User.sends((new String(" 離開[←, e] 選擇[↑,↓] 投票[→, V] 求助[h]\r\n")).getBytes());
			User.sends((new String("編號 狀態 型態             標              題            主  辦  人   開票日期 \r\n")).getBytes());
//			                       "   1 投過 投票 1234567890123456789012345678901234567890 123456789012 1999/08/30"
//			                       "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
			break;
		case ListVote.MODE_REPORT:
			User.sends((new String(" 離開[←, e] 選擇[↑,↓] 觀看投票結果[→, R] 求助[h]\r\n")).getBytes());
			User.sends((new String("編號 狀態 型態             標              題            主  辦  人   開票日期 \r\n")).getBytes());
//			                       "   1 投過 投票 1234567890123456789012345678901234567890 123456789012 1999/08/30"
//			                       "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
			break;
		case ListVote.MODE_MANAGE:
			//User.sends((new String("       離開[←,e, q]  選擇[↑,↓]  投票[V] 觀看投票結果[R] 開新投票[N] 求助[h]\r\n")).getBytes());
			//User.sends((new String("       離開[←,e, q]  選擇[↑,↓]  投票[V] 結果[R] 開啟[N] 刪除[D] 求助[h]\r\n")).getBytes());
			User.sends((new String(" 離開[←, e] 選擇[↑,↓] 資訊[→, I] 開啟[N] 刪除[D] 反刪除[U] 管理[M] 求助[h]\r\n")).getBytes());
			User.sends((new String("編號 狀態 型態             標              題            主  辦  人   開票日期 \r\n")).getBytes());
//			                       "   1 投過 投票 1234567890123456789012345678901234567890 123456789012 1999/08/30"
//			                       "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
			break;
		}
		User.printendline();		
	}

	
	//還沒有完成.
	private String getOutputState(VoteItem vi)
	{
		return "";
	}
	
	private String getOutput(int n, VoteItem vi)
	{
		String State = new String("Yes");
		String Style = new String("VOTE");
		String Already = new String("--ALL--");
		switch (Mode)
		{
		case ListVote.MODE_VOTE:
			return STRING.CutLeft((new String()).valueOf(n), 2) + " " + STRING.CutLeft(State, 4) + " " + STRING.CutLeft(Style, 4) + " " + STRING.Cut(vi.title, 40) + " " + STRING.CutLeft(vi.bmid, 12) + " " + STRING.CutLeft(df.format(new Date(vi.deadline)), 10);
		case ListVote.MODE_REPORT:
			return STRING.CutLeft((new String()).valueOf(n), 2) + " " + STRING.CutLeft(State, 4) + " " + STRING.CutLeft(Style, 4) + " " + STRING.Cut(vi.title, 40) + " " + STRING.CutLeft(vi.bmid, 12) + " " + STRING.CutLeft(Already, 10);
		case ListVote.MODE_MANAGE:
			return STRING.CutLeft((new String()).valueOf(n), 2) + " " + STRING.CutLeft(State, 4) + " " + STRING.CutLeft(Style, 4) + " " + STRING.Cut(vi.title, 40) + " " + STRING.CutLeft(vi.bmid, 12) + " " + STRING.CutLeft(df.format(new Date(vi.deadline)), 10);
		}
		return "";
	}
	
	/**
	 * true: 繼續
	 * false: 離開
	 */
	private boolean noVote()
	{
		drawtitle();
		User.sends((new String("要開新的投票嗎(y/N)? ")).getBytes());
		if (User.MakeSure() == 'Y')
			newVote();
		else
			return false;
		return true;
	}
		
	public void DoList()
	{
		while (listsize == 0)
		{
			if (!noVote())
				return;
		}
		
		int i;
		VoteItem vi;		
		
		now = 0;
		cursor = 0;
		
		int inkey;
		boolean redrawflag;
		
		redrawflag = true;
exitwhile:
		while (true)
		{
			if (redrawflag)
			{
				redraw();
				redrawflag = false;
			}			
			else if (now != nowold || cursor != cursorold)
			{
				User.move(1, cursorold + 4);
				User.sends("  ");
				User.move(1, cursor + 4);
				User.sends("!!");
			}
			User.move(2, cursor + 4);
			
			nowold = now;
			cursorold = cursor;
			
			switch((inkey = switchkey(User.getch())))
			{
			case Keys.Up:
			case 'j':
				cursor--;
				if (cursor < 0)
				{
					now -= SCR_SIZE;
					cursor = SCR_SIZE - 1;
					redrawflag = true;
					if (now < 0)
					{
						now = 0;
						cursor = 0;
						redrawflag = false;
					}
				}
				break;
			case Keys.Down:
			case 'k':
				cursor++;
				if (cursor == SCR_SIZE)
				{
					now += SCR_SIZE;
					cursor = 0;
					redrawflag = true;
				}
				if (now + cursor >= listsize)
				{
					now = ((listsize - 1) / SCR_SIZE) * SCR_SIZE;
					cursor = listsize - now - 1;
					redrawflag = false;
				}
				break;
			case ((int)'b' - (int)'a' + 1):
			case Keys.PgUp:
				if (now - SCR_SIZE < 0)
				{
					cursor = 0;
					break;
				}
				now -= SCR_SIZE;
				redrawflag = true;
				break;
			case ((int)'f' - (int)'a' + 1):
			case Keys.PgDn:
			case Keys.Space:
				if (now + SCR_SIZE >= listsize)
				{
					cursor = listsize - now - 1;
					break;
				}
				now += SCR_SIZE;
				redrawflag = true;
				if (now + cursor >= listsize)
				{
					now = ((listsize - 1) / SCR_SIZE) * SCR_SIZE;
					cursor = listsize - now - 1;
				}
				break;
			case Keys.Home:
			case '#':
				now = 0;
				cursor = 0;
				redrawflag = (now != nowold);
				break;
			case Keys.End:
			case '$':
				now = ((listsize - 1) / SCR_SIZE) * SCR_SIZE;
				cursor = listsize - now - 1;
				redrawflag = (now != nowold);
				break;
			case Keys.Left:
			case 'e':
			case 'q':
				return;
			case Keys.Right:
			case Keys.Enter:
				break;
			case 'N':
				newVote();
				redrawflag = true;
				break;
			case 'D':
				vi = getCurrentVoteItem();
				redrawflag = true;
				deleteVote(vi);
				if (listsize == 0)
				{
					now = 0;
					cursor = 0;
					if (!noVote())
						return;
					break;
				}
				
				if (now + cursor >= listsize)
				{
					now = ((listsize - 1) / SCR_SIZE) * SCR_SIZE;
					cursor = listsize - now - 1;
				}
				break;
			case 'V':
				String selects = null, msg[] = null;
				vi = getCurrentVoteItem();
				if (vi.isVoted(User.UFD.ID))
				{
					if (!vi.revotable)
					{						
						User.move(1, 24);
						User.clrtoeol();
						User.sends((new String("你已經投過票了!!")).getBytes());
						User.getch();
						User.printendline();
						break;
					}
				}
				else
					vi.getVoted(User.UFD.ID, selects, msg);
				voteIt(vi, selects, msg);
				break;
			case 'R':
				vi = getCurrentVoteItem();
				if (vi.voting == VoteItem.VOTE_VOTING_RESULT)
					viewResult(vi);
				break;
			case 'M':
				vi = getCurrentVoteItem();
				break;
			default:
				if (inkey >= '0' && inkey <= '9')
				{
					StringBuffer sb = new StringBuffer((new String()).valueOf(inkey - '0'));
					User.move(1, 24);
					User.clrtoeol();
					User.sends(Prompt.Msgs[15]);
					(User.GetData = new LineEdit(sb, 2, pid, true)).DoEdit();
					User.GetData = null;
					int n;
					try
					{
						n = Integer.parseInt(sb.toString().trim());
					}
					catch (Exception e)
					{
						n = 0;
					}
					if (n > 0 && n <= listsize)
					{
						now = ((n - 1) / SCR_SIZE) * SCR_SIZE;
						cursor = n - now - 1;
						redrawflag = (now != nowold);
					}
					User.printendline();
				}
				else
					break exitwhile;
			}
		}
		
		User.PressAnyKey();
	}
	
	private void dumpVoteInfo(VoteItem vi)
	{
		User.Clear();
		User.move(1, 1);
		
		User.sends((new String("標題: ")).getBytes());
		User.sends(vi.title);
		User.newline();
		
		User.sends((new String("主辦人: ")).getBytes());
		User.sends(vi.bmid);
		User.newline();
		
		User.sends((new String("型態: ")).getBytes());
		User.sends("" + vi.style);
		User.newline();
		
		User.sends((new String("狀態: ")).getBytes());
		User.sends("yes");
		User.newline();
		
		User.sends((new String("截止日期: ")).getBytes());
		User.sends(df.format(new Date(vi.deadline)));
		User.newline();		
	}
	
	private void deleteVote(VoteItem vi)
	{
		dumpVoteInfo(vi);
		User.move(1, 7);
		User.sends((new String("是否刪除這次投票(y/N)? ")).getBytes());
		if (User.MakeSure() == 'Y')
		{
			votelist.removeVote(vi);
			list = getVoteList();
		}
	}
	
	private void newVote()
	{
		//要加當投票多過 99 個的時候就不能加了.
		
		int i;
		
		String title = null;
		int style = 0, choice = 0, select = 0, deadline = 0; // 這裡的 deadline 指的是今天後的天數
		boolean revotable = false;
		String[] choices = null;
		
		StringBuffer sb;
		boolean giveup = true;
		
		User.Clear();		
togiveup:
		while (giveup)
		{			
			sb = new StringBuffer();
			User.sends((new String("投票標題: ")).getBytes());
			(User.GetData = new LineEdit(sb, 40, pid, true)).DoEdit();
			User.GetData = null;
			title = sb.toString().trim();
			if (title.equalsIgnoreCase(""))
				break togiveup;
			User.newline();
					
			User.sends((new String("投票型態(0: 投票, 1: 心理測驗): ")).getBytes());
			switch (User.MakeSure())
			{
			case '0':
				style = VoteItem.VOTE_STYLE_VOTE;
				break;
			case '1':
				style = VoteItem.VOTE_STYLE_QUIZ;
				break;
			default:
				break togiveup;
			}
			User.newline();
		
			sb = new StringBuffer();
			User.sends((new String("投票期限(天): ")).getBytes());
			(User.GetData = new LineEdit(sb, 2, pid, true)).DoEdit();
			User.GetData = null;
			try
			{
				deadline = Integer.parseInt(sb.toString().trim());
			}
			catch (NumberFormatException e)
			{
				deadline = 0;
			}
			if (deadline < 1 && deadline > 31)
				break togiveup;
			User.newline();
			
			User.sends((new String("是否允許更改投票(y/n): ")).getBytes());
			switch (User.MakeSure())
			{
			case 'Y':
				revotable = true;
				break;
			case 'N':
				revotable = false;
				break;
			default:
				break togiveup;
			}
			User.newline();
			
			sb = new StringBuffer();
			User.sends((new String("選項數目(1 ~ 30): ")).getBytes());
			(User.GetData = new LineEdit(sb, 2, pid, true)).DoEdit();
			User.GetData = null;
			try
			{
				choice = Integer.parseInt(sb.toString().trim());
			}
			catch (NumberFormatException e)
			{
				choice = 0;
			}
			if (choice < 1 && choice > 30)
				break togiveup;
			User.newline();
			
			sb = new StringBuffer();
			User.sends((new String("可投票數(1 ~ 30): ")).getBytes());
			(User.GetData = new LineEdit(sb, 2, pid, true)).DoEdit();
			User.GetData = null;
			try
			{
				select = Integer.parseInt(sb.toString().trim());
			}
			catch (NumberFormatException e)
			{
				select = 0;
			}
			if ((select < 1 && select > 30) || select > choice)
				break togiveup;
			User.newline();

			User.Clear();
			int isize = 70;
			if (choice > 15)
				isize = 35;
			choices = new String[choice];
			for (i = 0; i < choice; i++)
			{
				if (choice % 23 == 0)
					User.Clear();
				sb = new StringBuffer();
				User.sends((new String("選項(" + (i + 1) + "): ")).getBytes());
				(User.GetData = new LineEdit(sb, isize, pid, true)).DoEdit();
				User.GetData = null;
				choices[i] = sb.toString().trim();
				if (choices[i].equalsIgnoreCase(""))
					break togiveup;
				User.newline();
			}
			
			giveup = false;
		}
		
		if (giveup)
		{
			User.move(1, 24);
			User.sends((new String("放棄開啟投票!!")).getBytes());
			User.getch();
		}	
		else
		{
			VoteItem vi = votelist.newVote(User.UFD.ID, title, style, (new Date()).getTime() + deadline * 86400 * 1000L, revotable, choice, select, choices);
			User.move(1, 24);
			User.clrtoeol();
			User.sends((new String("按任何鍵後開始編輯說明畫面......")).getBytes());
			User.getch();
			(User.CurrentEditor = new Editor(pid)).DoEdit(vi.getWelcomeFile());
			list = getVoteList();
		}
	}
	
	private void viewResult(VoteItem vi)
	{
		User.Clear();
	}
	
	private String ZeroN(int N)
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < N; i++)
			sb.append("0");
		return sb.toString();
	}
	
	private void voteIt(VoteItem vi, String selects, String msg[])
	{
		if (selects == null)
			selects = new String();
		if (selects.length() < vi.choice)
			selects = selects + ZeroN(vi.choice - selects.length());

		char sel[] = selects.toCharArray();
		
		User.ansimore(vi.getWelcomeFile());
		User.PressAnyKey();
		User.Clear();
		StringBuffer sb = new StringBuffer();
		String tmp = new String();
		String temp = new String();
		for (int i = 0; i < 15; i++)
		{
			if (vi.choice > i)
			{
				if (sel[i] == '1')
					temp = "!!";
				else
					temp = "  ";
				tmp = STRING.Cut(temp + STRING.CutLeft((new String().valueOf(i + 1)), 2) + " " + vi.choices[i], 35) + " ";
			}
			else
				tmp = STRING.Cut("", 40);
			if (vi.choice > i + 15)
			{
				if (sel[i] == '1')
					temp = "!!";
				else
					temp = "  ";
				tmp += STRING.Cut(temp + STRING.CutLeft((new String().valueOf(i + 20)), 3) + " " + vi.choices[i], 35);
			}
			else
				tmp += STRING.Cut("", 39);
			User.sends(tmp);
			User.newline();
		}

exitwhile:
		while (true)
		{
			User.move(1, 24);
			User.clrtoeol();
			User.sends((new String("要投給幾號呢(1 ~ " + vi.choice + ")? ")).getBytes());
			sb = new StringBuffer();
			(User.GetData = new LineEdit(sb, 2, pid, true)).DoEdit();
			User.GetData = null;
			int n;
			try
			{
				n = Integer.parseInt(sb.toString().trim());
			}
			catch (Exception e)
			{
				n = -1;
			}
			if (n > 0 && n <= vi.choice)
			{
				n--;
				if (sel[n] == '1')
					sel[n] = '0';
				else
					sel[n] = '1';
				if (n > 19)
					User.move(41, n - 18);
				else
					User.move(1, n + 1);
				if (sel[n] == '1')
					temp = "!!";
				else
					temp = "  ";
				User.sends(temp);
			}
			else if (n == 0)
			{
				int sels = 0;
				for (int i = 0; i < vi.choice; i++)
				{
					if (sel[i] == '1')
						sels++;
				}
				if (sels > vi.select)
				{
					User.move(1, 24);
					User.clrtoeol();
					User.sends((new String("你投太多票了!!請減少所投的票數!!")).getBytes());
					User.getch();
				}
				else
				{
					if (sels != 0)
					{
						User.Clear();
						User.move(1, 1);
						User.sends((new String("請寫下您的意見:")).getBytes());
						User.newline();

						for (int i = 0; i < 3; i++)
						{
							sb = new StringBuffer(msg[i]);
							(User.GetData = new LineEdit(sb, 60, pid, true)).DoEdit();
							User.GetData = null;
							msg[i] = sb.toString().trim();
							if (msg[i].trim().equalsIgnoreCase(""))
							{
								for (; i < 3; i++)
									msg[i] = "";
								break;
							}
						}
						
						vi.voteIt(User.UFD.ID, new String(sel), msg);
					}
					break exitwhile;
				}
			}
		}
	}
}
