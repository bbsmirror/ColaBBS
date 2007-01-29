package colabbs.plasmid;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.CRONTAB.*;
import colabbs.record.*;

public final class NoteRefresher
{
	private static Object myNote=null;
	public static void doRefresh()
	{
		if(ColaServer.NowNote.longValue()<0) // If the board notepad not create just do nothing....
			return;
			
		File NoteFile;
		// If notepad file exists, move it to notepad board....
		if((NoteFile=new File(ColaServer.INI.BBSHome+"etc"+File.separator+"notepad")).exists())
		{
			char appendChar='A'-1;
			int theTime;
			String SaveFileName;
			String tmpPath=ColaServer.INI.BBSHome+"boards"+File.separator+"notepad";
			// search the file name for create, just for safe....
			theTime=(int)((new Date()).getTime()/1000);
			do
			{
				appendChar++;
				SaveFileName="M."+theTime+"."+appendChar;
			}while((new File(tmpPath,SaveFileName)).exists());
			
			synchronized(ColaServer.NowNote) // just for safe...
			{
				// move notepad file to notepad board...
				NoteFile.renameTo(new File(tmpPath,SaveFileName));
				// add file to directory of notepad board....
				RecordHandler.append(myNote,new PostType(SaveFileName,"Anonymous",Prompt.Msgs[335]),ColaServer.INI.BBSHome+"boards"+File.separator+"notepad",Consts.DotDir);
					
				// refresh the filetime of board....
				int addnum=(int)(appendChar-'A');
				((BoardItem)myNote).filetime=theTime*1000L+addnum;
				// reset last notepad refresh time....
				ColaServer.NowNote=new Long(theTime);
			}
		}
	}
/**
 * This method was writen by yhwu.
 */
public static void startup()
{
	// Search the board item object for synchronized lock object and refresh the filetime while add new article....

	/*try
	{
		LinkType Base=ColaServer.BBSBoards.Boards.GetBase();
		LinkType ptr=Base;
		BoardItem BoardBuf=null;

		if(Base==null)
			return;

		do
		{
			BoardBuf=(BoardItem)ptr.obj;
			if(BoardBuf.Name.equalsIgnoreCase("notepad"))
				myNote=BoardBuf;
			ptr=ptr.next;
		}while(ptr!=Base);
	}
	finally
	{
		if(myNote==null)
		{
			System.err.println("Can't find the board 'notepad'!");
			return;
		}
	}*/	
	myNote = ColaServer.BList.get("notepad");
	if (myNote == null)
	{
		System.err.println("Can't find the board 'notepad'!");
		return;
	}	
	
	/*
	// Initialize the BitSets.....
	BitSet min=new BitSet(60),hour=new BitSet(24),day=new BitSet(31),month=new BitSet(12),week=new BitSet(7);
	min.set(0);
	hour.set(0);// refresh the note pad when time is 0:0
	
	for(int i=1;i<=31;i++) //everyday
		day.set(i);

	for(int i=1;i<=12;i++) //every month
		month.set(i);
		
	for(int i=0;i<7;i++) // for any week day
		week.set(i);
	// add this item to crontab....
	try
	{
		ColaServer.myCron.add(new CronTabItem(min,hour,day,month,week,Class.forName("colabbs.plasmid.NoteRefresher").getMethod("doRefresh",ColaServer.ArgVoidClass),ColaServer.ArgVoidObject));
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}*/
	try
	{
		ColaServer.myCron.add(new CronTabItem(0, 0, -1, -1, -1,Class.forName("colabbs.plasmid.NoteRefresher").getMethod("doRefresh",ColaServer.ArgVoidClass),ColaServer.ArgVoidObject));
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
}
}