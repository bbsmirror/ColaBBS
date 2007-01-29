package colabbs;

public final class Consts
{
	public final static byte ModeOut = 0x1; //�峹��J
	public final static byte ModeIn = 0x2; //�峹��X
	public final static int StrLen = 80; // �r�����
	public final static int BmLen = 60; //���Did����
	public final static int NameLen = 40; // �W�r����
	public final static int IDLen = 12; // ID����
	public final static int PassLen = 14; // �K�X����
	public final static int UserLen = 512; //�ϥΪ̸�ƪ���
	public final static int MaxGopherItems = 9999; // �ثe�|���ϥ�

	public final static int FileRead = 0x1;
	public final static int FileOwnd = 0x2;
//	public final static int FileVisit = 0x4;
	public final static int FileMarked = 0x8;
	public final static int FileDigest = 0x10;
	public final static int FileLink = 0x20; //�s�Wfor ColaInfo system!

	public final static int NoZapFlag = 0x1;  //�s�W
	public final static int AnonyFlag = 0x2;  //�s�W
	public final static int AnonyMode = 0x4;  //�s�W
	public final static int SaveMode = 0x8; //�s�W
	public final static int Junk = 0x10; //�s�W

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
//	public final static int UserFlag_Cloak = 0; //�ϥΪ��������A, �����O�� n �� bit
	public final static byte UserFlag_Cloak = 0x2; //�ϥΪ��������A, �����O�� n �� bit
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
	
	public final static String PassFile = ".PASSWDS"; // �ϥΪ̸���ɦW
	public final static String Flush = ".PASSFLUSH"; // ����
	public final static String Boards = ".BOARDS"; // �Q�װϦC���ɦW
	public final static String DotDir = ".DIR"; // �ؿ����ɦW
	public final static String ThreadDir = ".THREAD"; // �D�D���ɦW
	public final static String DigestDir = ".DIGEST"; // ��K�ɦW
	public final static String Deleted = ".DELETED";
	public final static String NewsNode = ".NewsNode"; //for News Node list
	public final static String NewsFeed = ".NewsFeed"; //for News Feed list

	public final static Object AnnounceLock = new Object(); // for announce lock...
}