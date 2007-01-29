package colabbs;

public final class Consts
{
	public final static byte ModeOut = 0x1; //文章轉入
	public final static byte ModeIn = 0x2; //文章轉出
	public final static int StrLen = 80; // 字串長度
	public final static int BmLen = 60; //版主id長度
	public final static int NameLen = 40; // 名字長度
	public final static int IDLen = 12; // ID長度
	public final static int PassLen = 14; // 密碼長度
	public final static int UserLen = 512; //使用者資料長度
	public final static int MaxGopherItems = 9999; // 目前尚未使用

	public final static int FileRead = 0x1;
	public final static int FileOwnd = 0x2;
//	public final static int FileVisit = 0x4;
	public final static int FileMarked = 0x8;
	public final static int FileDigest = 0x10;
	public final static int FileLink = 0x20; //新增for ColaInfo system!

	public final static int NoZapFlag = 0x1;  //新增
	public final static int AnonyFlag = 0x2;  //新增
	public final static int AnonyMode = 0x4;  //新增
	public final static int SaveMode = 0x8; //新增
	public final static int Junk = 0x10; //新增

	public final static int Zapped = 0x1;
	public final static int PostMask = 0x8000;  //1000 0000 0000 0000

//    final static int PagerFlag = 0x1;
//    final static int CloakFlag = 0x2;
//    final static int SigFlag = 0x8;
//    final static int BrdSortFlag = 0x20;
//    final static int CursorFlag = 0x80;
	public final static int ActiveBoard = 0x1;
	public final static int EGroupNew = 0x2;
	public final static int AllMsgPager = 0x4;
	public final static int FriendMsgPager = 0x8;
	public final static int AllPager = 0x10;
	public final static int FriendPager = 0x20;
	public final static int BackupMsg = 0x40;

	public final static int UserIDSize = 16;
	public final static int UserNameSize = 24;
	public final static int TermTypeSize = 10;

	//Add by WilliamWey, modified by yhwu
//	public final static int UserFlag_Cloak = 0; //使用者隱身狀態, 指的是第 n 個 bit
	public final static byte UserFlag_Cloak = 0x2; //使用者隱身狀態, 指的是第 n 個 bit
	//

	public final static int BOARDS_LENGTH = 256;
	
	public final static int BOARDS_FILENAME = 80;
	public final static int BOARDS_OWNER = 20;
	public final static int BOARDS_BM = 59;
	public final static int BOARDS_STATE = 1;
	public final static int BOARDS_EGROUP = 1;
	public final static int BOARDS_TITLE = 79;
	public final static int BOARDS_LEVEL = 4;
	public final static int BOARDS_ACCESSED = 12;
	
	public final static int BOARDS_FILENAME_P = 0;
	public final static int BOARDS_OWNER_P = BOARDS_FILENAME;
	public final static int BOARDS_BM_P = BOARDS_FILENAME + BOARDS_OWNER;
	public final static int BOARDS_STATE_P = BOARDS_FILENAME + BOARDS_OWNER + BOARDS_BM; 
	public final static int BOARDS_EGROUP_P = BOARDS_FILENAME + BOARDS_OWNER + BOARDS_BM + BOARDS_STATE;
	public final static int BOARDS_TITLE_P = BOARDS_FILENAME + BOARDS_OWNER + BOARDS_BM + BOARDS_STATE + BOARDS_EGROUP;
	public final static int BOARDS_LEVEL_P = BOARDS_FILENAME + BOARDS_OWNER + BOARDS_BM + BOARDS_STATE + BOARDS_EGROUP + BOARDS_TITLE;
	
/*	public final static int DoNothing = 0;
	public final static int FullUpdate = 1;
	public final static int PartUpdate = 2;
	public final static int DoQuit = 3;
	public final static int NewDirect = 4;
	public final static int ReadNext = 5;
	public final static int ReadPrev = 6;
	public final static int GotoNext = 7;
	public final static int DirChanged = 8;

	public final static int SrBMBase = 10;
	public final static int SrBMDel = 11;
	public final static int SrBMMark = 12;
	public final static int SrBMDigest = 13;
	public final static int SrBMImport = 14;
	public final static int SrBMTmp = 15;*/

	public final static String INI_ColaBBS = "ColaBBS.INI";
	public final static String INI_Messages = "Messages.INI";
	public final static String INI_Plasmid = "Plasmid.INI";
	public final static String INI_ServicePlasmid = "ServicePlasmid.INI";
	
	public final static String PassFile = ".PASSWDS"; // 使用者資料檔名
	public final static String Flush = ".PASSFLUSH"; // 不知
	public final static String Boards = ".BOARDS"; // 討論區列表檔名
	public final static String DotDir = ".DIR"; // 目錄檔檔名
	public final static String ThreadDir = ".THREAD"; // 主題檔檔名
	public final static String DigestDir = ".DIGEST"; // 文摘檔名
	public final static String Deleted = ".DELETED";
	public final static String NewsNode = ".NewsNode"; //for News Node list
	public final static String NewsFeed = ".NewsFeed"; //for News Feed list

	public final static Object AnnounceLock = new Object(); // for announce lock...
}