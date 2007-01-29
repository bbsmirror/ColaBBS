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
	public static String BBSName = "����q�i�֨t�� BBS ��";  // BBS ���W�r
	public static String IBBSName = "TestBBS";
	public static String BBSAddress = "localhost"; // BBS ����}
	public static String Language = "zh";
	public static String Country = "TW";
	public static String Encoding = "Big5";
	public static String MyTimeZone = "CTT";
	public static String ClientIssue = null;
	public static String DefaultBoard = "sysop"; // �ϥΪ̹w�]�Q?
	public static boolean UseColaMail = true;  // �O�_�ϥ� ColaMail
	public static boolean BMDelDecrease = false; // ���D��峹�O�_�|��ϥΪ̤峹��
	public static boolean INetEMAIL = true; // �O�_�n���H�����H��\��
	public static boolean DoTimeout = true; // �O�_��ܶ��m��?
	public static boolean ActBoardBar = true; //�O�_��ܬ��ʬݪ����~�A
	public static boolean DefaultAnonymous = true; //�ΦW�����w�]���_�ϥΰΦW
	public static boolean PrintOrg = true; //�O�_�L�X�o��̨ӷ�
	public static boolean BanUnregistIP = false; //�O�_�⥼���U��ip�ӷ�ban��
	public static boolean InternetMail = false; //�O�_����InternetMail�\��
	public static boolean AutoLevelUp = true; //�O�_�۰ʴ���?
	public static boolean NNTP = true; //�O�_�}����H�\��
	public static int TelnetPort = 23; // �i�H���m�h�֤���
	public static int TelnetOnlineUser = 1000; // �i�H���m�h�֤���
	public static int ClientOnlineUser = 100; //�u�WClient�ܤj����
	public static int IdleTimeout = 30; // �i�H���m�h�֤���
	public static int MonitorTimeout = 30; // �b�ʬݨϥΪ̪��A�i�H���m�h�֤���
	public static int MaxActBoard = 6; // ���ʬݪO���
	public static int MaxUsers = 20236; // �̤j�ϥΪ̼�
	public static int OutputBuffer = 8192;
//    static int MaxBoard=256; // �̤j�Q�װϼ�
	public static int MaxSigLines = 6; // ñ�W�ɳ̤j���
	public static int MaxSigNum = 6; //ñ�W�ɳ̤j�Ӽ�
	public static int MaxQueryLines = 16; // �����ɳ̤j���
	public static int MaxQuoteLevel = 2; //�ި��̤j�h��
	public static int MaxBoardRC = 60;  //�O���ϥΪ�Ū�L�峹���̤j�ƥ�
	public static int RegPerm = 31;
	public static int OrgPerm = 1;
	public static int EditorMaxLines = 2048; //�s�边�̤j���
	public static int EditorMaxLong = 256;  //�s�边�C��̤j�r��
	public static long RegTimeOut = 120 * 24 * 60 * 60 * 1000; //���U���\�ϥΪ̫O�d�Ѽ�
	public static long NoteRefreshTime = 24 * 60 * 60 * 1000; //�d������s�W�v

	public static int LoginAttempts = 3; // �a�\���� login ������
	public static int Maxfriends = 128; // �n�ͦW�檺�̤j�ƶq
	public static int Maxblacks = 32;
	public static int MaxGoodBye = 5; // �����e����
	public static int MaxIssue = 5; // �i���e����
	public static int MaxDigest = 1000; // �̤j��K��
	public static int ActBoardMaxLine = 80; // ���ʬݪO�̤j���
	public static int MaxMailHold = 50; // �̤j�O�d�H���
	public static int MaxSYSOPMailHold = 200; // �����̤j�O�d�H���
	public static int GetPostMax = 500;
	public static boolean LoginAsNew = true; // �O�_���\���U�sID
	public static boolean MultiLogins = false; // �O�_���\���еn�J
	public static boolean Vote = true; // �O�_���벼�\��
	public static boolean NoReply = false; // �O�_���\�ϥΪ̦^��
	public static boolean IRC = false; // �O�_�� IRC �\��
	public static boolean BBSDoors = true; // �O�_�� BBSNet �\��
	public static boolean PostRealName = false; // �O�_�ϥίu�W�K��
	public static boolean MailRealName = false; // �O�_�ϥίu�W�H�H
	public static boolean QueryRealName = false; // �O�_�i�H�d�߯u�W
	public static boolean UseNotepad = true; // �O�_�ϥίd?
	public static boolean UseDayTop = true; // �O�_�ϥΥ���Q�j�������D
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
