package colabbs;

import java.io.*;
import java.net.*;
import java.util.*;

import colabbs.DATA.PROPERTIES.*;

public class BBSINI
{
	public static InetAddress ServerAddress = null;
	public static Locale myLocale = null;
	public static String BBSHome = File.separator+"ColaSrc"+File.separator+"bbs"+File.separator;
	public static String BBSName = "插花島可樂系統 BBS 站";  // BBS 的名字
	public static String IBBSName = "TestBBS";
	public static String BBSAddress = "localhost"; // BBS 的位址
	public static String Language = "zh";
	public static String Country = "TW";
	public static String Encoding = "Big5";
	public static String MyTimeZone = "CTT";
	public static String ClientIssue = null;
	public static String DefaultBoard = "sysop"; // 使用者預設討?
	public static boolean UseColaMail = true;  // 是否使用 ColaMail
	public static boolean BMDelDecrease = false; // 版主砍文章是否會減使用者文章數
	public static boolean INetEMAIL = true; // 是否要有寄網路信件功能
	public static boolean DoTimeout = true; // 是否顯示閒置時?
	public static boolean ActBoardBar = true; //是否顯示活動看版的外括
	public static boolean DefaultAnonymous = true; //匿名版之預設之否使用匿名
	public static boolean PrintOrg = true; //是否印出發表者來源
	public static boolean BanUnregistIP = false; //是否把未註冊的ip來源ban掉
	public static boolean InternetMail = false; //是否提供InternetMail功能
	public static boolean AutoLevelUp = true; //是否自動提升?
	public static boolean NNTP = true; //是否開啟轉信功能
	public static int TelnetPort = 23; // 可以閒置多少分鐘
	public static int TelnetOnlineUser = 1000; // 可以閒置多少分鐘
	public static int ClientOnlineUser = 100; //線上Client曘大限制
	public static int IdleTimeout = 30; // 可以閒置多少分鐘
	public static int MonitorTimeout = 30; // 在監看使用者狀態可以閒置多少分鐘
	public static int MaxActBoard = 6; // 活動看板行數
	public static int MaxUsers = 20236; // 最大使用者數
	public static int OutputBuffer = 8192;
//    static int MaxBoard=256; // 最大討論區數
	public static int MaxSigLines = 6; // 簽名檔最大行數
	public static int MaxSigNum = 6; //簽名檔最大個數
	public static int MaxQueryLines = 16; // 說明檔最大行數
	public static int MaxQuoteLevel = 2; //引言最大層數
	public static int MaxBoardRC = 60;  //記錄使用者讀過文章之最大數目
	public static int RegPerm = 31;
	public static int OrgPerm = 1;
	public static int EditorMaxLines = 2048; //編輯器最大行數
	public static int EditorMaxLong = 256;  //編輯器每行最大字數
	public static long RegTimeOut = 120 * 24 * 60 * 60 * 1000; //註冊成功使用者保留天數
	public static long NoteRefreshTime = 24 * 60 * 60 * 1000; //留言版更新頻率

	public static int LoginAttempts = 3; // 淮許嘗試 login 的次數
	public static int Maxfriends = 128; // 好友名單的最大數量
	public static int Maxblacks = 32;
	public static int MaxGoodBye = 5; // 離站畫面數
	public static int MaxIssue = 5; // 進站畫面數
	public static int MaxDigest = 1000; // 最大文摘數
	public static int ActBoardMaxLine = 80; // 活動看板最大行數
	public static int MaxMailHold = 50; // 最大保留信件數
	public static int MaxSYSOPMailHold = 200; // 站長最大保留信件數
	public static int GetPostMax = 500;
	public static boolean LoginAsNew = true; // 是否予許註冊新ID
	public static boolean MultiLogins = false; // 是否予許重覆登入
	public static boolean Vote = true; // 是否有投票功能
	public static boolean NoReply = false; // 是否予許使用者回文
	public static boolean IRC = false; // 是否有 IRC 功能
	public static boolean BBSDoors = true; // 是否有 BBSNet 功能
	public static boolean PostRealName = false; // 是否使用真名貼文
	public static boolean MailRealName = false; // 是否使用真名寄信
	public static boolean QueryRealName = false; // 是否可以查詢真名
	public static boolean UseNotepad = true; // 是否使用留?
	public static boolean UseDayTop = true; // 是否使用本日十大熱門話題
	public static boolean AddEditMark = true;
	public static boolean Internet = true;
	public static boolean UseGuest = true;

	public static String SMTPAddress = "localhost";
	public static int SMTPPort = 25;
	
	public static String BoardGroups[];

	public static ColaProperties OtherProperties = new ColaProperties();

	public BBSINI()
	{
		
	}

	public BBSINI(String FileName)
	{
		LoadFile(FileName);
	}
	
	private String MakePath(String path)
	{
		path = path.trim();
		if (!path.endsWith(new String(new char[] {File.separatorChar})))
			path = path + File.separatorChar;
		return path;
	}
	
	public boolean LoadFile(String FileName)
	{
		File F = new File(FileName);
		
		if (!F.exists())
		{
			ColaServer.ErrorMsg = "there is no such file: " + F.getAbsolutePath();
			return false;
		}
		
		RandomAccessFile INIFile = null;
		
		String tmp;
		int seeks;
		String Key = null, Value = null;
		
		boolean Flag = false;
		
		try
		{
			INIFile = new RandomAccessFile(F, "r");
			
			while((tmp = INIFile.readLine()) != null)
			{
				tmp = tmp.trim();
				if (tmp.length() != 0 && (tmp.substring(0, 1)) != "#" && ((seeks = tmp.indexOf('=')) > 0))
				{
					Key = tmp.substring(0, seeks).trim();
					Value = tmp.substring(seeks + 1).trim();
					
					if (Key.equalsIgnoreCase("BBSHome"))
						BBSHome = MakePath(Value);
					else if (Key.equalsIgnoreCase("BBSName"))
						BBSName = Value;
					else if (Key.equalsIgnoreCase("BBSAddress"))
						BBSAddress = Value;
					else if (Key.equalsIgnoreCase("IBBSName"))
						IBBSName = Value;
					else if (Key.equalsIgnoreCase("IBBSName"))
						IBBSName = Value;
					else if (Key.equalsIgnoreCase("TimeZone"))
						MyTimeZone = Value;
					else if (Key.equalsIgnoreCase("Language"))
						Language = Value;
					else if (Key.equalsIgnoreCase("Country"))
						Country = Value;
					else if (Key.equalsIgnoreCase("Encoding"))
						Encoding = Value;
					else if (Key.equalsIgnoreCase("ClientIssue"))
						ClientIssue = Value;
					else if (Key.equalsIgnoreCase("DefaultBoard"))
						DefaultBoard = Value;
					else if (Key.equalsIgnoreCase("TelnetOnlineUser"))
						TelnetOnlineUser = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("ClientOnlineUser"))
						ClientOnlineUser = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("TelnetPort"))
						TelnetPort = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("IdleTimeout"))
						IdleTimeout = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxActBoard"))
						MaxActBoard = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxUsers"))
						MaxUsers = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("OutputBuffer"))
						OutputBuffer = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxSigLines"))
						MaxSigLines = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxSigNum"))
						MaxSigNum = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxQuoteLevel"))
						MaxQuoteLevel = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("MaxBoardRC"))
						MaxBoardRC = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("Maxfriends"))
						Maxfriends = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("Maxblacks"))
						Maxblacks = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("RegPerm"))
						RegPerm = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("OrgPerm"))
						OrgPerm = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("EditorMaxLines"))
						EditorMaxLines = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("EditorMaxLong"))
						EditorMaxLong = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("LoginAttempts"))
						LoginAttempts = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("ActBoardMaxLine"))
						ActBoardMaxLine = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("GetPostMax"))
						GetPostMax = Integer.parseInt(Value);
					else if (Key.equalsIgnoreCase("RegTimeOut"))
						RegTimeOut = (long)Integer.parseInt(Value) * 24 * 60 * 60 * 1000L;
					else if (Key.equalsIgnoreCase("NoteRefreshTime"))
						NoteRefreshTime = (long)Integer.parseInt(Value) * 60 * 60 * 1000L;
					else if (Key.equalsIgnoreCase("InternetMail"))
						InternetMail = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("UseColaMail"))
						UseColaMail = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("DoTimeout"))
						DoTimeout = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("ActBoardBar"))
						ActBoardBar = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("LoginAsNew"))
						LoginAsNew = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("UseNotepad"))
						UseNotepad = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("Internet"))
						Internet = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("UseGuest"))
						UseGuest = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("NNTP"))
						NNTP = Boolean.valueOf(Value).booleanValue();
					else if (Key.equalsIgnoreCase("BoardGroups"))
						BoardGroups = new String[Integer.parseInt(Value)];
					else if (Key.equalsIgnoreCase("ServerAddress"))
						ServerAddress = InetAddress.getByName(Value);
					else if (Key.substring(0, 5).equalsIgnoreCase("Group"))
					{
						if (Key.length() > 5)
							BoardGroups[Integer.parseInt(Key.substring(5, 6))] = Value;
					}
					else if (Key.equalsIgnoreCase("SMTPAddress"))
						SMTPAddress = Value;
					else if (Key.equalsIgnoreCase("SMTPPort"))
						SMTPPort = Integer.parseInt(Value);
					else
					{
						OtherProperties.set(Key.toLowerCase(), Value);
					}
				}
			}

			if(Language != null && Country != null)
				myLocale = new Locale(Language, Country);
			else
				myLocale = Locale.getDefault();

			File SYSOPs = new File(BBSHome + "etc" + File.separator + "SYSOPS");
			RandomAccessFile SYSOPSFile = new RandomAccessFile(SYSOPs, "r");
			StringTokenizer tmptok;
			int sysopnums = 0;

			while ((tmp = SYSOPSFile.readLine()) != null)
				if (tmp.length() != 0)
					sysopnums++;
			SYSOPSFile.seek(0);
			ColaServer.SYSOPS1 = new String[sysopnums];
			ColaServer.SYSOPS2 = new String[sysopnums];
			sysopnums = 0;
			while ((tmp = SYSOPSFile.readLine()) != null)
			{
				if( tmp.length() != 0)
				{
					tmptok = new StringTokenizer(tmp);
					ColaServer.SYSOPS1[sysopnums] = tmptok.nextToken();
					ColaServer.SYSOPS2[sysopnums] = tmptok.nextToken();
					sysopnums++;
				}
			}
			
			Flag = true;
		}
		catch (IOException e)
		{
			ColaServer.ErrorMsg = "IO Error: " + e.getMessage();
		}
		catch (NumberFormatException e)
		{
			ColaServer.ErrorMsg = "Number Format Error: " + Key;
		}
		finally
		{
			try
			{
				if (INIFile != null)
				{
					INIFile.close();
					INIFile = null;
				}
			}
			catch (Exception e)
			{
				ColaServer.ErrorMsg = "Error: " + e.getMessage();
				Flag = false;
			}
		}
			
		return Flag;
	}
}
