package colabbs.DATA.BOARD;

import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.record.*;
import colabbs.DATA.USERFILEDATA.*;
import colabbs.DATA.VOTE.*;
import colabbs.UTILS.*;

public class BoardItem
{
	public int bid = -1;
	//public boolean Vote = false;
	//public boolean Editing = false;
	public String Name;
	public byte EGroup;
	public String Title;
	public String BM;
	public int Level = 8;
	public byte State = 0;

	public boolean Anonymous = true;	
	public boolean AnonyDefault = true;	
	public boolean NoZap = false;		
	public boolean SaveMode = true;
	public boolean JunkBoard = false;	
	
	public long filetime = 0;

	public VoteList vote;
	
	public void LoadVoteList()
	{
		vote.Load(this);
	}
	
	public String getPath()
	{
		return ColaServer.INI.BBSHome + "boards" + File.separator + Name + File.separator;
	}
	
	public boolean equals(Object O)
	{
		return Name.equalsIgnoreCase(((BoardItem)O).Name);
	}
	
	public byte DefaultSaveMode()
	{
		if (SaveMode)
			return (byte)'S';
		else
			return (byte)'L';
	}
	
	public int getPostNumber()
	{
		File F = new File(getPath() + Consts.DotDir);
		if (F.exists())
			return (int)(F.length() / 256);
		return 0;
	}
	
	public BoardItem copyOut()
	{
		BoardItem bi = new BoardItem(bid);
		
		bi.Name = Name;
		bi.EGroup = EGroup;
		bi.Title = Title;
		bi.BM = BM;
		bi.Level = Level;
		bi.State = State;
		
		bi.Anonymous = Anonymous;
		bi.AnonyDefault = AnonyDefault;
		bi.NoZap = NoZap;
		bi.SaveMode = SaveMode;
		bi.JunkBoard = JunkBoard;
		
		return bi;
	}

	public void copyIn(BoardItem bi)
	{
		Name = bi.Name;
		EGroup = bi.EGroup;
		Title = bi.Title;
		BM = bi.BM;
		Level = bi.Level;
		State = bi.State;
		
		Anonymous = bi.Anonymous;
		AnonyDefault = bi.AnonyDefault;
		NoZap = bi.NoZap;
		SaveMode = bi.SaveMode;
		JunkBoard = bi.JunkBoard;
	}
	
	public BoardItem()
	{
		
	}
	
	public BoardItem(int xbid)
	{
		bid = xbid;
	}
	
	public BoardItem(byte buf[])
	{
		fromFileBuf(buf);
	}
	
	public BoardItem(int xbid, byte buf[])
	{
		bid = xbid;
		fromFileBuf(buf);
	}
	
	public boolean visible(int userperm)
	{
		return !((Level & Consts.PostMask) == 0 && !((userperm & Level) == Level));
	}
	
	private long LoadFileTime()
	{
		int addnum=0;
		PostType pt=new PostType();
		long xfiletime = 0;
		
		Long longbuf = new Long(0);
		
		int len=(int)RecordHandler.recordNumber(pt,ColaServer.INI.BBSHome+"boards"+File.separator+Name,Consts.DotDir);
		if(len>=0)
		{
			RecordHandler.getRecord(len,this,pt,ColaServer.INI.BBSHome+"boards"+File.separator+Name,Consts.DotDir);
			
			String FNBuf=pt.deleteBody();
			if(FNBuf.lastIndexOf('.')!=-1)
			{
				addnum=(int)(FNBuf.charAt(FNBuf.lastIndexOf('.')+1)-'A');
				FNBuf=FNBuf.substring(2,FNBuf.lastIndexOf('.'));
			}
			
			try
			{
				longbuf=new Long(FNBuf);
				xfiletime=longbuf.longValue();
				xfiletime=xfiletime*1000+addnum;
			}
			catch(NumberFormatException e){}
		}
		
		if(Name.equalsIgnoreCase("notepad"))
		{
			ColaServer.NowNote=longbuf;
		}	
		
		return xfiletime;
	}
	
	private void fromFileBuf(byte buf[])
	{
		int tmp;
		Name = new String(buf, 0, Consts.BOARDS_FILENAME_P, Consts.BOARDS_FILENAME);
		if((tmp = Name.indexOf(0)) != -1)
			Name = Name.substring(0, tmp);
		BM = new String(buf, 0, Consts.BOARDS_BM_P, Consts.BOARDS_BM);
		if((tmp = BM.indexOf(0)) != -1)
			BM = BM.substring(0, tmp);
		EGroup = buf[Consts.BOARDS_EGROUP_P];
		Title = new String(buf, 0, Consts.BOARDS_TITLE_P, Consts.BOARDS_TITLE);
		if((tmp = Title.indexOf(0)) != -1)
			Title = Title.substring(0, tmp);
		Level = NUMBER.GetInt(buf, Consts.BOARDS_LEVEL_P);
		
		State = buf[Consts.BOARDS_STATE_P];
		NoZap = (State & Consts.NoZapFlag) != 0;
		Anonymous = (State & Consts.AnonyFlag) != 0;
		AnonyDefault = (State & Consts.AnonyMode) != 0;
		JunkBoard = (State & Consts.Junk) != 0;
		SaveMode = !((State & Consts.SaveMode) == 0); //有為 S
		
		filetime = LoadFileTime();
	}	
	
	public byte[] toFileBuf()
	{
		byte buf[] = new byte[Consts.BOARDS_LENGTH];
		Name.getBytes(0, Name.length(), buf, Consts.BOARDS_FILENAME_P);
		BM.getBytes(0, BM.length(), buf, Consts.BOARDS_BM_P);
		
		State = 0;
		if (Anonymous)
			State |= Consts.AnonyMode;
		if (AnonyDefault)
			State |= Consts.AnonyFlag;
		if (NoZap)
			State |= Consts.NoZapFlag;
		if (SaveMode)
			State |= Consts.SaveMode;
		if (JunkBoard)
			State |= Consts.Junk;
		buf[Consts.BOARDS_STATE_P] = State;
		buf[Consts.BOARDS_EGROUP_P] = EGroup;
		Title.getBytes(0, Title.length(), buf, Consts.BOARDS_TITLE_P);
		NUMBER.PutNum(buf, Consts.BOARDS_LEVEL_P, Level);
		return buf;		
	}

	//進版畫面
	private String getWelcomeFileName()
	{
		return ColaServer.INI.BBSHome + "boards" + File.separator + Name + File.separator + ".Welcome";
	}
	
	public File getWelcomeFile()
	{
		return new File(getWelcomeFileName());
	}
	
	public long getWelcomeFileTime()
	{
		File wf = getWelcomeFile();
		return wf.lastModified();
	}
	
	public boolean hasWelcome()
	{
		File wf = getWelcomeFile();
		return (wf.exists() && wf.length() > 2);
	}
	//
	
	//暫時使用的, 這樣做有點不太好
	public boolean postArticle(String FileName, String Author, String Title, byte tag) //tag 為 L or S
	{
		RecordHandler.append(this, new PostType(FileName, Author, Title, tag), getPath(), Consts.DotDir);
		return true;
	}

	public String getLastFileName()
	{
		return getLastFileName(ColaServer.SysDATE.getNow());
	}
	
	public synchronized String getLastFileName(Calendar whattime)
	{
		String tmpPath = getPath();
		char appendChar = 'A' - 1;
		int theTime = (int)(whattime.getTime().getTime() / 1000);
		String SaveFileName;
		do
		{
			appendChar++;
			SaveFileName = "M." + theTime + "." + appendChar;
		}while((new File(tmpPath, SaveFileName)).exists());
		
		return SaveFileName;
	}
	//
}
