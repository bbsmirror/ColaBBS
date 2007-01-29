package colabbs.DATA.USERFILEDATA;

import java.net.*;
import java.io.*;
import java.util.*;

import colabbs.*;
import colabbs.UTILS.*;
import colabbs.DATA.BOARDRC.*;

public final class UserFileData
{
	public int uid = -1;
	public int logins = 0;
	
	public String ID;
	public String PassWord;
	public String NickName = "";
	public String RealName = "";
	public String School = "";
	public String LastHost = "localhost";
	public String Tel = "";
	public String Address = "";
	public String Email = "";
	public String URL = "";
	public String UserLevel = "\u00a4\u0040\u00af\u00eb\u00ae\u0071\u00a5\u00c1";
	public boolean Cloak = false;
	public byte signature = 0;
	public byte NoteMode = 0;
	public byte Sex = 0;
	public byte BloodType = 0;
//    public byte Pager=(byte)0xf5;
	public byte Flags[] = new byte[2];
	public int NoteLine = 0;
	public int NumLogins = 0;
	public int NumPosts = 0;
	public int UserDefine = 0;
	public int Perm = (int)3;
	public int LevelNum = 0;
	public long FirstLogin = 0;
	public long Stay = 0;
	public long LastLogin = 0;
	public long Birthday = 0;

	//BoardRC
	public BoardRCList brclist = null;
	//
	
	public void loadBoardRC()
	{
		brclist = new BoardRCList(this);
		brclist.LoadFile();
	}
	
	//再 search 時用的, 現在沒用到.
	/*public boolean equals(Object obj)
	{
		return ID.equalsIgnoreCase(((UserFileData)obj).ID);
	}*/
	
	public UserFileData()
	{
		
	}
	
	public UserFileData(int xuid)
	{
		uid = xuid;
	}
	
	public UserFileData(byte buf[])
	{
		fromFileBuf(buf);
	}
	
	public UserFileData(int xuid, byte buf[])
	{
		uid = xuid;
		fromFileBuf(buf);
	}
	
	private void fromFileBuf(byte buf[])
	{
		ID = new String(buf, 0, 0, Consts.IDLen);
		if (ID.indexOf(0) != -1)
			ID = ID.substring(0, ID.indexOf(0));
		PassWord = new String(buf, 0, 14, Consts.PassLen);
		if (PassWord.indexOf(0) != -1)
			PassWord = PassWord.substring(0, PassWord.indexOf(0));
		NickName = new String(buf,0,28,Consts.NameLen);
		if (NickName.indexOf(0) != -1)
			NickName=NickName.substring(0, NickName.indexOf(0));
		RealName = new String(buf, 0, 68, Consts.NameLen);
		if (RealName.indexOf(0) != -1)
			RealName = RealName.substring(0, RealName.indexOf(0));
		//Skip ident
		School = new String(buf, 0, 148, 20);
		if (School.indexOf(0) != -1)
			School = School.substring(0, School.indexOf(0));
		LastHost = new String(buf, 0, 168, 16);
		if (LastHost.indexOf(0) != -1)
			LastHost = LastHost.substring(0, LastHost.indexOf(0));
		Tel = new String(buf, 0, 184, 20);
		if (Tel.indexOf(0) != -1)
			Tel = Tel.substring(0, Tel.indexOf(0));
		Address = new String(buf, 0, 204, Consts.StrLen);
		if (Address.indexOf(0) != -1)
			Address = Address.substring(0, Address.indexOf(0));
		Email = new String(buf, 0, 284, Consts.StrLen);
		if (Email.indexOf(0) != -1)
			Email = Email.substring(0, Email.indexOf(0));
		URL = new String(buf, 0, 364, Consts.StrLen);
		if (URL.indexOf(0) != -1)
			URL = URL.substring(0, URL.indexOf(0));
		Flags[0] = buf[444];
		Flags[1] = buf[445];
		signature = buf[446];
		NoteMode = buf[447];
		Sex = buf[448];
		BloodType = buf[449];
		FirstLogin = NUMBER.GetLong(buf, 450);
		Stay = NUMBER.GetLong(buf, 458);
		LastLogin = NUMBER.GetLong(buf, 466);
		Birthday = NUMBER.GetLong(buf, 474);
		NumLogins = NUMBER.GetInt(buf, 482);
		NumPosts = NUMBER.GetInt(buf, 486);
		UserDefine = NUMBER.GetInt(buf, 490);
		Perm = NUMBER.GetInt(buf, 494);
		LevelNum = NUMBER.GetInt(buf, 498);
		NoteLine = NUMBER.GetInt(buf, 502);		
	}
		
	public byte[] toFileBuf()
	{
		byte buf[] = new byte[Consts.UserLen];
		ID.getBytes(0, ID.length(), buf, 0);
		PassWord.getBytes(0, PassWord.length(), buf, 14);
		NickName.getBytes(0, NickName.length(), buf, 28);
		RealName.getBytes(0, RealName.length(), buf, 68);
		School.getBytes(0, School.length(), buf, 148);
		if (LastHost.length() > 16)
			LastHost = LastHost.substring(0, 16);
		LastHost.getBytes(0, LastHost.length(), buf, 168);
		Tel.getBytes(0, Tel.length(), buf, 184);
		Address.getBytes(0, Address.length(), buf, 204);
		Email.getBytes(0, Email.length(), buf, 284);
		URL.getBytes(0, URL.length(), buf, 364);
		
		buf[444] = Flags[0];
		buf[445] = Flags[1];
		buf[446] = signature;
		buf[447] = NoteMode;
		buf[448] = Sex;
		buf[449] = BloodType;
		NUMBER.PutNum(buf, 450, FirstLogin);
		NUMBER.PutNum(buf, 458, Stay);
		NUMBER.PutNum(buf, 466, LastLogin);
		NUMBER.PutNum(buf, 474, Birthday);
		NUMBER.PutNum(buf, 482, NumLogins);
		NUMBER.PutNum(buf, 486, NumPosts);
		NUMBER.PutNum(buf, 490, UserDefine);
		NUMBER.PutNum(buf, 494, Perm);
		NUMBER.PutNum(buf, 498, LevelNum);
		NUMBER.PutNum(buf, 502, NoteLine);		
		
		return buf;
	}

	public void resetPerm(int xPerm)
	{
		Perm = xPerm;
	}
}