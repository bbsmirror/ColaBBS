package colabbs.telnet;

import java.io.*;

import colabbs.ColaServer;
import colabbs.BBSUser;

import colabbs.*;
import colabbs.record.*;

public abstract class ViewFileList extends DirList
{
	protected void enterFunction()
	{
		int InKey=0;
		
		try
		{
			while(true)
			{
				if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					break;

				User.Clear();
				
				ViewFileType vft=(ViewFileType)rt;
				
				vft.LastTitle=vft.getTitle();
				if(vft.LastTitle!=null&&vft.LastTitle.length()>3&&vft.LastTitle.substring(0,4).equals("Re: "))
					vft.LastTitle=vft.LastTitle.substring(4);

				if(!vft.isLink())
				{
					if((new File(CurrentPath,rt.deleteBody())).exists())
						User.ansimore(CurrentPath+rt.deleteBody());
					else
					{
						DelRecord(now+cursor);
						lastnow=-1;
						break;
					}
				}
				else
				{
					if((new File(rt.deleteBody())).exists())
						User.ansimore(rt.deleteBody());
					else
					{
						DelRecord(now+cursor);
						lastnow=-1;
						break;
					}
				}

				InKey=ReadEnd(rt);
				switch(InKey)
				{
				case Keys.Up:
					cursor--;
					if(cursor<0)
					{
						now-=MaxRow;
						if(now<0)
						{
							now=0;
							cursor=0;
							return;
						}
						else
							cursor=MaxRow-1;
					}
					if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
						return;
					break;
				case '[':
					if(ThreadLast()<0)
						return;
					break;
				case Keys.Down:
				case Keys.Space:
					/*					if(cursor<MaxRow-1&&(now+cursor)<ListMax)
					{
					cursor++;
					if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
					return;
					break;
					}
					cursor=0; //¤S¬G·N¤£break*/
					cursor++;
					//if(cursor>MaxRow)
					if((now + cursor) > ListMax)
					{
						cursor = ListMax % MaxRow;
						now = ListMax - cursor;
						return;
					}
					
					if(cursor == MaxRow)
					{
						now += MaxRow;
						if(now > ListMax)
						{
							cursor = ListMax % MaxRow;
							now = ListMax - cursor;
						}
						else
							cursor = 0;
					}
										
					/*if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
						return;*/
					
					break;
				case ']':
				case ((int)'x'-(int)'a'+1):
				case 'p':
					if(ThreadNext()<0)
						return;
					break;
				case 'q':
				case 'Q':
				case Keys.Left:
				default:
					return;
				}
			}
		}
		finally
		{
			DrawTitle();
			lastnow=-1;
		}
	}
	
	protected abstract int ReadEnd(RecordType t);
	
	public int ThreadLast()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;

		if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			return -1;
		
		String OrgTitle=((ViewFileType)rt).getTitle();
		String CmpTitle;

		if(OrgTitle.length()>3&&OrgTitle.substring(0,4).equals("Re: "))
			OrgTitle=OrgTitle.substring(4);

		while(true)
		{
			cursor--;
			if(cursor<0)
			{
				now-=MaxRow;
				if(now<0)
				{
					now=oldnow;
					cursor=oldcursor;
					return -1;
				}
				else
					cursor=MaxRow-1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpTitle=((ViewFileType)rt).getTitle();
			if(CmpTitle!=null&&CmpTitle.length()>3&&CmpTitle.substring(0,4).equals("Re: "))
				CmpTitle=CmpTitle.substring(4);

			if(OrgTitle.equals(CmpTitle))
				break;
		}
		return 0;
	}
	
	public int ThreadNext()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;

		if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			return -1;
		
		String OrgTitle=((ViewFileType)rt).getTitle();
		String CmpTitle;

		if(OrgTitle.length()>3&&OrgTitle.substring(0,4).equals("Re: "))
			OrgTitle=OrgTitle.substring(4);

		while(true)
		{
			cursor++;
			/*			if(cursor>=MaxRow)
			{
			now+=MaxRow;
			cursor=0;
			}*/
			if(cursor >= MaxRow)
			{
				now += MaxRow;
				if(now > ListMax)
				{
					/*cursor = ListMax % MaxRow;
					now = ListMax - cursor;*/
					now = oldnow;
					cursor = oldcursor;					
					return -1;
				}
				else
				{
					cursor = 0;
					if((now + cursor) > ListMax)
					{
						now = oldnow;
						cursor = oldcursor;
						return -1;
					}
				}
			}
			else if((now+cursor)>ListMax)
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpTitle=((ViewFileType)rt).getTitle();
			if(CmpTitle!=null&&CmpTitle.length()>3&&CmpTitle.substring(0,4).equals("Re: "))
				CmpTitle=CmpTitle.substring(4);

			if(OrgTitle.equals(CmpTitle))
				break;
		}
		return 0;
	}

	public int SearchTitleLast()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;
		
		String CmpTitle;
		String Title;		
		TelnetUser TU = (TelnetUser)User;
		
		if (TU.SearchTitle.equalsIgnoreCase(""))
		{			
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				return -1;
		
			TU.SearchTitle=((ViewFileType)rt).getTitle();		
			if(TU.SearchTitle.length()>3&&TU.SearchTitle.substring(0,4).equals("Re: "))
				TU.SearchTitle=TU.SearchTitle.substring(4);
		}
		
		StringBuffer Ans = new StringBuffer();
		
		TU.move(1, 24);
		TU.clrtoeol();
		TU.sends(Prompt.Msgs[469] + " [" + TU.SearchTitle + "] ");
		if ((TU.GetData = new LineEdit(Ans, 40, pid, true)).DoEdit() < 0)
			return -1;
		TU.GetData = null;
		
		Title = Ans.toString().trim();
		if(Title.length() == 0)
			Title = TU.SearchTitle.trim();
		else
			TU.SearchTitle = Title;
		Title = Title.toLowerCase();
		
		while(true)
		{
			cursor--;
			if(cursor<0)
			{
				now-=MaxRow;
				if(now<0)
				{
					now=oldnow;
					cursor=oldcursor;
					return -1;
				}
				else
					cursor=MaxRow-1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpTitle=((ViewFileType)rt).getTitle().toLowerCase();
			
			if(CmpTitle.indexOf(Title) != -1)
				break;
		}
		return 0;
	}	
	
	public int SearchTitleNext()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;
		
		String CmpTitle;
		String Title;
		TelnetUser TU = (TelnetUser)User;

		if (TU.SearchTitle.equalsIgnoreCase(""))
		{			
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				return -1;
		
			TU.SearchTitle=((ViewFileType)rt).getTitle();		
			if(TU.SearchTitle.length()>3&&TU.SearchTitle.substring(0,4).equals("Re: "))
				TU.SearchTitle=TU.SearchTitle.substring(4);
		}
		
		StringBuffer Ans = new StringBuffer();
		
		TU.move(1, 24);
		TU.clrtoeol();
		TU.sends(Prompt.Msgs[469] + " [" + TU.SearchTitle + "] ");
		if ((TU.GetData = new LineEdit(Ans, 40, pid, true)).DoEdit() < 0)
			return -1;
		TU.GetData = null;
		
		Title = Ans.toString().trim();
		if(Title.length() == 0)
			Title = TU.SearchTitle.trim();
		else
			TU.SearchTitle = Title;
		Title = Title.toLowerCase();
		
		while(true)
		{
			cursor++;
			if(cursor>=MaxRow)
			{
				now+=MaxRow;
				if(now>ListMax)
				{
					cursor=ListMax%MaxRow;
					now=ListMax-cursor;
				}
				else
				{
					cursor=0;
					if((now+cursor)>ListMax)
					{
						now=oldnow;
						cursor=oldcursor;
						return -1;
					}
				}
			}
			else if((now+cursor)>ListMax)
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpTitle=((ViewFileType)rt).getTitle().toLowerCase();
			
			if(CmpTitle.indexOf(Title) != -1)
				break;
		}
		return 0;
	}
	
	public int SearchAuthorLast()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;
		
		String CmpAuthor;
		String Author;		
		TelnetUser TU = (TelnetUser)User;
		
		if (TU.SearchAuthor.equalsIgnoreCase(""))
		{			
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				return -1;
		
			TU.SearchAuthor=((ViewFileType)rt).getPoster();
		}
		
		StringBuffer Ans = new StringBuffer();
		
		TU.move(1, 24);
		TU.clrtoeol();
		TU.sends(Prompt.Msgs[470] + " [" + TU.SearchAuthor + "] ");
		if ((TU.GetData = new LineEdit(Ans, 40, pid, true)).DoEdit() < 0)
			return -1;
		TU.GetData = null;
		
		Author = Ans.toString().trim();
		if(Author.length() == 0)
			Author = TU.SearchAuthor.trim();
		else
			TU.SearchAuthor = Author;
		Author = Author.toLowerCase();
		
		while(true)
		{
			cursor--;
			if(cursor<0)
			{
				now-=MaxRow;
				if(now<0)
				{
					now=oldnow;
					cursor=oldcursor;
					return -1;
				}
				else
					cursor=MaxRow-1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpAuthor=((ViewFileType)rt).getPoster().toLowerCase();
			
			if(CmpAuthor.indexOf(Author) != -1)
				break;
		}
		return 0;
	}
	
	public int SearchAuthorNext()
	{
		int oldcursor,oldnow;

		oldcursor=cursor;
		oldnow=now;
		
		String CmpAuthor;
		String Author;
		TelnetUser TU = (TelnetUser)User;

		if (TU.SearchAuthor.equalsIgnoreCase(""))
		{			
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
				return -1;
		
			TU.SearchAuthor=((ViewFileType)rt).getPoster();
		}
		
		StringBuffer Ans = new StringBuffer();
		
		TU.move(1, 24);
		TU.clrtoeol();
		TU.sends(Prompt.Msgs[470] + " [" + TU.SearchAuthor + "] ");
		if ((TU.GetData = new LineEdit(Ans, 40, pid, true)).DoEdit() < 0)
			return -1;
		TU.GetData = null;
		
		Author = Ans.toString().trim();
		if(Author.length() == 0)
			Author = TU.SearchAuthor.trim();
		else
			TU.SearchAuthor = Author;
		Author = Author.toLowerCase();
		
		while(true)
		{
			cursor++;
			if(cursor>=MaxRow)
			{
				now+=MaxRow;
				if(now>ListMax)
				{
					cursor=ListMax%MaxRow;
					now=ListMax-cursor;
				}
				else
				{
					cursor=0;
					if((now+cursor)>ListMax)
					{
						now=oldnow;
						cursor=oldcursor;
						return -1;
					}
				}
			}
			else if((now+cursor)>ListMax)
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			if(!RecordHandler.getRecord(now+cursor,lockObj,rt,CurrentPath,MyDir))
			{
				now=oldnow;
				cursor=oldcursor;
				return -1;
			}
			CmpAuthor=((ViewFileType)rt).getPoster().toLowerCase();
			
			if(CmpAuthor.indexOf(Author) != -1)
				break;
		}
		return 0;
	}
	
	protected void exitList()
	{
	}
}