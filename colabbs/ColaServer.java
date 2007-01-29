package colabbs;

import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

import colabbs.net.*;
import colabbs.telnet.*;
import colabbs.record.*;
import colabbs.FILE.*;
import colabbs.UTILS.*;
import colabbs.System.*;
import colabbs.WW.*;
import colabbs.chatroom.ChatRoom;

import colabbs.DATA.*;
import colabbs.DATA.ACBOARD.*;
import colabbs.DATA.APLASMID.*;
import colabbs.DATA.BOARD.*;
import colabbs.DATA.CRONTAB.*;
import colabbs.DATA.IPACCESS.*;
import colabbs.DATA.ISSUE.*;
import colabbs.DATA.MENU.*;
import colabbs.DATA.USERFILEDATA.*;
import colabbs.DATA.VOTE.*;

import colabbs.EVENTHANDLER.*;

import colabbs.telnet.EHS.*;

public final class ColaServer extends Thread
{
	public final static String VersionDate = "99112X";

	//add by yhwu.
	private static String None="";

	public static ChatRoom myChat=null;
	public static Hashtable NewRegList;
	public static Crypt Crypter=new Crypt();
	public static BBSUser BBSUsers[];
	public static int SortedUser[];
	public static int BBSIdent=-1;
	public static BBSINI INI=new BBSINI();
	
	public static byte registerbuf[];
	public static int onlineuser=0;
	public static int clientcount=100,count=1000,port=23;
	public static int MsgLen=127;
	public static Long NowNote=new Long(-1);
	public static String SYSOPS1[];
	public static String SYSOPS2[];
	public static AllMenu BBSMenus;
	public static BoardList BList;
	
	//public static ByteStrPrintWriter BBSlog;
	public static LogFile BBSlog = null;
	public static ColaServer TelnetServer;
	//public static SimpleDateFormat DateFormatter1,DateFormatter2,DateFormatter3,DateFormatter4;
	//public static CronTab myCron=new CronTab();
	public static CronTab myCron;
	public static ThreadGroup userGroup=new ThreadGroup("OnlineUser");
	public final static Class[] ArgVoidClass=new Class[0];
	public final static Object[] ArgVoidObject=new Object[0];
	private static ServerSocket BBSServer;

	/*public static IPList WW_IP_AD = new IPList();
	public static boolean IP_AD = true;		//true: Deny, false: Allow*/

	public static IPAccess_List IPAccess = new IPAccess_List(0);		//Add by WilliamWey for 擋 IP
	public static NewsFilter NFilter = new NewsFilter();				//Add by WilliamWey for NewsFilter
	public static CharsetTable CharsetMap = new CharsetTable();			//Add by WilliamWey for CharsetMap
	public static ActivePlasmid APlasmid = new ActivePlasmid();			//Add by WilliamWey for ActivePlasmid
	public static ACBoardList ACBoard = new ACBoardList();				//Add by WilliamWey for 動態看板
	public static IssueList Issues = new IssueList();					//Add by WilliamWey for 上站畫面
	public static LoginViewList LoginView = new LoginViewList();		//Add by WilliamWey for 上站後的一堆畫面
	public static long StartUpTime;										//Add by WilliamWey for LogFile
	public static LogFile logfile = null;								//Add by WilliamWey for LogFile
	public static String ErrorMsg = new String();						//Add by WilliamWey for 錯誤訊息回傳
	public static UserFileDataList UFDList;								//Add by WilliamWey for 同一使用者共用 UFD
	public static DATE SysDATE;											//Add by WilliamWey for 所有取得跟日期時間相關資料的地方
	public static EH_ListBase EHList = new EH_ListBase();				//Add by WilliamWey for EVENTHANDLER
	
	//新的
	public static ServicePlasmid_List ServicePlasmidList = new ServicePlasmid_List();
	//
	
	public ColaServer()
	{
		super(userGroup, "Telnet Server");
		this.start();
	}

	private static void initNNTP()
	{
		if(!INI.NNTP)
			return;
		//initializing dateformat time zone....
		//NNTPClient.formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		NewsNodeType nnt=new NewsNodeType();
		RecordHandler rh=null;
		BitSet min=null,hour=null,day=null,month=null,week=null;
		
		synchronized(Consts.NewsNode)
		{
			try
			{
				rh=new RecordHandler(nnt,INI.BBSHome,Consts.NewsNode,true);
				int n=(int)rh.recordNumber(nnt);
				for(int i=0;i<=n;i++)
				{
					min=new BitSet(60);
					hour=new BitSet(24);
					day=new BitSet(32);
					month=new BitSet(13);
					week=new BitSet(7);
					rh.nextElement(nnt);
					nnt.getTime(min,hour,day,month,week);
					try
					{
						myCron.add(new CronTabItem(min,hour,day,month,week,colabbs.net.NNTPClient.class.getMethod("connect",ColaServer.ArgVoidClass),new NNTPClient(nnt.getBBSName(),nnt.getAddress(),nnt.getPort(),nnt.getCommand()),ColaServer.ArgVoidObject));
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			finally
			{
				if(rh!=null)
				{
					rh.close();
					rh=null;
				}
			}
		}

		System.out.println("Starting ColaINND Server....");
		try
		{
			if(INI.ServerAddress!=null)
				new ColaINND().startServer(7777,INI.ServerAddress,"ColaINND");
			else
				new ColaINND().startServer(7777,"ColaINND");
		}
		catch(IOException ioexception)
		{
			System.out.print("ColaINND Server failed: " + ioexception + "\n");
			return;
		}
		System.out.println("ColaINND Server ready.");
	}

	private static void loadPlasmid()
	{
		try
		{
			BufferedReader in=new BufferedReader(new FileReader("Plasmid.INI"));
			System.out.println("Loading plasmid....");
			String name=null;
			do
			{
				name=in.readLine();
				try
				{
					if(name!=null)
					{
						name = name.trim();
						if (name.charAt(0) != '#')
						{
							Class myClass=Class.forName(name);
							System.out.println(name);
							myClass.getMethod("startup",ColaServer.ArgVoidClass).invoke(null,ColaServer.ArgVoidObject);
						}
					}
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}
			}while(name!=null);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static void makeSysDirectory()
	{
		File F;
		F = new File(INI.BBSHome + File.separator + "temp");
		if (!F.exists())
			F.mkdirs();
		F = new File(INI.BBSHome + File.separator + "mailtemp");
		if (!F.exists())
			F.mkdirs();
		F = new File(INI.BBSHome + File.separator + "log");
		if (!F.exists())
			F.mkdirs();
	}

	private static void makeEventHandler()
	{
		EH_List ehl = new EH_List("TELNET_LOGIN");
		ehl.add(new LoginCounter());
		EHList.add(ehl);
	}
	
	public static String getSysTempPath()
	{
		return INI.BBSHome + File.separator + "temp";
	}
	
	public static void main(String[] args)
	{
		int i;
		
		System.out.println("Cola Bulletin Board System Server, Version " + VersionDate + "SNAPSHOT 1997-1999");
		System.out.println("All rights reserved. Copyright(C) by Ying-haur Wu at NCTUCIS and infoX Studio");
		System.out.println("");
		
		System.out.print("Loading ColaBBS.INI......");
		if (INI.LoadFile(Consts.INI_ColaBBS))
			System.out.println("OK!!");
		else
		{
			System.out.println(ErrorMsg);
			System.exit(1);
		}
		
		port=INI.TelnetPort;
		count=INI.TelnetOnlineUser;
		clientcount=INI.ClientOnlineUser;
		
		System.out.println("ColaBBS Telnetd port = "+port);
		System.out.println("ColaBBS Max Telnet user = "+count);
		
		/*DateFormatter1=new SimpleDateFormat ("EEE MMM dd kk':'mm",Locale.ENGLISH);
		DateFormatter1.setTimeZone(TimeZone.getTimeZone(INI.MyTimeZone));
		DateFormatter2=new SimpleDateFormat ("EEE MMM dd kk':'mm':'ss yyyy",Locale.ENGLISH);
		DateFormatter2.setTimeZone(TimeZone.getTimeZone(INI.MyTimeZone));
		DateFormatter3=new SimpleDateFormat ("yy'/'MM'/'dd",Locale.ENGLISH);
		DateFormatter3.setTimeZone(TimeZone.getTimeZone(INI.MyTimeZone));
		DateFormatter4=new SimpleDateFormat ("kk':'mm",Locale.ENGLISH);
		DateFormatter4.setTimeZone(TimeZone.getTimeZone(INI.MyTimeZone));*/
		SysDATE = new DATE();
		myCron = new CronTab();
		
		makeSysDirectory();
		
		makeEventHandler();
		
		logfile = new LogFile("ColaBBS", true, LogFile.FileNameStyle_PerDay);
		logfile.Write("啟動");
		//try
		{
			//Changed by WilliamWey for LogFile
			//File logfile=new File(INI.BBSHome+"log"+File.separator+((new Date()).getTime()/1000)+".log");
			StartUpTime = (new Date()).getTime() / 1000;
			/*File Flogfile=new File(INI.BBSHome+"log"+File.separator+(StartUpTime)+".log");
			new File(INI.BBSHome+"log").mkdirs();
			BBSlog=new ByteStrPrintWriter(new OutputStreamWriter(fos));*/
			BBSlog = new LogFile("BBSLOG", false, LogFile.FileNameStyle_StartUp);
			
			/*Flogfile=new File(INI.BBSHome+"log"+File.separator+((new Date()).getTime()/1000)+".err");
			fos=new FileOutputStream(Flogfile);
			System.setErr(new PrintStream(fos));/** only disable for debug by yhwu 99/2/18 ***/
			System.setErr(new PrintStream(new LogOutputStream(new LogFile("ColaBBS.DEBUG", true, LogFile.FileNameStyle_StartUp))));
		}
		/*catch(IOException e)
		{
			System.out.println("Can't open log files"+e);
			System.exit(1);
			return;
		}*/
		
		//Add by WilliamWey
		System.out.print("Loading .PASSWDS......");
		UFDList = new UserFileDataList(INI.BBSHome + Consts.PassFile);
		if (UFDList.LoadFile())
			System.out.println("OK!!");
		else
			System.out.println(ErrorMsg);
		//				
		
		System.out.println("Loading Menus....");
		BBSMenus=new AllMenu();
		//      BBSMenus.Dump();

		//Add by WilliamWey for ActivePlasmid
		System.out.print("Loading ActivePlasmid File......");
		if (APlasmid.LoadFile("ActivePlasmid.INI"))
			System.out.println("OK!!");
		else
			System.out.println("No such file!!");	
		//
		
		System.out.print("Loading Boards......");
		BList = new BoardList(INI.BBSHome + Consts.Boards);
		if (BList.LoadFile())
			System.out.println("OK!!");
		else
			System.out.println(ErrorMsg);
		//

		System.out.print("Loading Vote......");
		Enumeration E = BList.getlist();
		BoardItem bi;
		while (E.hasMoreElements())
		{
			bi = (BoardItem)E.nextElement();
			bi.vote = new VoteList();
			bi.vote.Load(bi);
		}
		System.out.println("");
		
		System.out.print("Loading Messages......");
		if (Prompt.LoadFile(Consts.INI_Messages))
			System.out.println("OK!!");
		else
			System.out.println(ErrorMsg);
		//
		Modes.InitModes();

		System.out.print("Loading ServicePlasmid......");
		if (ServicePlasmidList.LoadFile(Consts.INI_ServicePlasmid))
		{
			System.out.println("OK!!");
			Enumeration e = ServicePlasmidList.list.elements();
			ServicePlasmid_Item pi;
			while (e.hasMoreElements())
			{
				pi = (ServicePlasmid_Item)e.nextElement();
				System.out.print("        [" + pi.Plasmid_Name + "]..[");
				if (pi.init())
				{
					System.out.print("OK]..[");
					if (pi.start())
						System.out.println("OK]");
					else
						System.out.println("ERROR]");
				}
				else
					System.out.println("ERROR]");
			}
		}
		else
			System.out.println(ErrorMsg);

		
		BBSUsers=new BBSUser[count+clientcount];
		SortedUser=new int[count+clientcount];
		for(i=0;i<count+clientcount;i++)
			SortedUser[i]=i;
		try
		{
			File issuefile=new File(INI.BBSHome+"etc"+File.separator+"REGISTER");
			InputStream issue=new FileInputStream(issuefile);
			registerbuf=new byte[(int)issuefile.length()];

			System.out.println("Loading REGISTER....");
			issue.read(registerbuf,0,(int)issuefile.length());
			issue.close();
			issuefile=null;
			issue=null;
		}
		catch(IOException e)
		{
			System.out.println("File loading error"+e);
			System.exit(1);
			return;
		}

		NewRegList=new Hashtable(100);

		try
		{
			File RegListFile;

			if((RegListFile=new File(INI.BBSHome,"NewRegister")).exists())
			{
				long newnum=RegListFile.length()/8,li,uidbuf;
				Long RegBuf;
				RandomAccessFile RegListBuf=new RandomAccessFile(RegListFile,"r");

				System.out.println("Loading New Regester List File....");

				for(li=0;li<newnum;li++)
				{
					RegBuf=new Long(RegListBuf.readLong());
					NewRegList.put(RegBuf,RegBuf);
				}
				RegListBuf.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			BBSlog.Write("Unable to read NewRegister file:"+e);
			System.exit(1);
		}
		
		/*MailCheck=new Hashtable(100);

		try
		{
			File RegListFile;

			if((RegListFile=new File(INI.BBSHome,"MailCheckList")).exists())
			{
				long newnum=RegListFile.length()/8,li;
				Integer Ident,Code;
				RandomAccessFile RegListBuf=new RandomAccessFile(RegListFile,"r");

				System.out.println("Loading Mail Check List File....");

				for(li=0;li<newnum;li++)
				{
					Ident=new Integer(RegListBuf.readInt());
					Code=new Integer(RegListBuf.readInt());
					MailCheck.put(Ident,Code);
				}
				RegListBuf.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
			BBSlog.Write("Unable to read Mail Check file:"+e);
			System.exit(1);
		}*/

		MsgLen=109+Prompt.Msgs[159].length();
		
/*		//Add by WilliamWey
		System.out.print("Loading IP_Allow File......");
		//if (WW_IP_AD.LoadFromFile("IP_Allow.txt"))
		if (WW_IP_AD.LoadFromFile(INI.BBSHome + "IP_Allow.txt"))
		{
			IP_AD = false;
			System.out.println("OK!!");
		}
		else
		{
			IP_AD = true;
			System.out.println("No such file!!");
			System.out.print("Loading IP_Deny File......");
			//if (WW_IP_AD.LoadFromFile("IP_Deny.txt"))
			if (WW_IP_AD.LoadFromFile(INI.BBSHome + "IP_Deny.txt"))
				System.out.println("OK!!");
			else
				System.out.println("No such file!!");
		}
		//*/
		System.out.print("Loading IPAccess File......");
		if (IPAccess.LoadFile("IPAccess.INI"))
			System.out.println("OK!!");
		else
			System.out.println("No such file!!");

		//Add by WilliamWey
		System.out.print("Loading CharsetMap File......");
		if (CharsetMap.LoadFile("CharsetMap.INI"))
			System.out.println("OK!!");
		else
			System.out.println("No such file!!");			
		//
		
		//Add by WilliamWey for NewsFilter
		System.out.print("Loading NewsFilter File......");
		if(NFilter.LoadFile("NewsFilter.INI"))
			System.out.println("OK!!");
		else
			System.out.println("No such file!!");
		//
		
		//Add by WilliamWey for LoginView
		System.out.print("Loading LoginView File......");
		if(LoginView.LoadFile("LoginView.INI"))
			System.out.println("OK!!");
		else
			System.out.println("No such file!!");
		//
		
		//if(INI.UseColaMail/*&&INI.Internet*/)
		/*{
			Mail2BBS.INI=INI;
			Mail2BBS.sdf=DateFormatter2;

			System.out.println("Starting Cola Mail Server....");
			try
			{
				if(INI.ServerAddress!=null)
					new ColaMail().startServer(25,INI.ServerAddress,"ColaMail");
				else
					new ColaMail().startServer(25,"ColaMail");
				System.out.println("Cola Mail Server ready.");
			}
			catch(IOException ioexception)
			{
				System.out.print("Cola Mail Server failed: " + ioexception + "\n");
			}
		}*/
		
		initNNTP();
		loadPlasmid();

    //以下 if 中是只有 client server 才需要的開站動作....
		if(clientcount>0)
		{
			colabbs.bbsclient.server.ClientServer clientserver1=new colabbs.bbsclient.server.ClientServer();
			clientserver1.start();
		}
    //以下 if 中是只有 telnet server 才需要的開站動作....
    if(count>0)
    {
  		System.out.println("Start Listening Telnet Port....");
	  	System.out.println("Now, your bbs service is running...");
		  System.out.println("Try to use TELNET to connect to your own computer.");

  		TelnetServer=new ColaServer();
    }
/*		System.out.println("Start Listening Telnet Port....");
		System.out.println("Now, your bbs service is running...");
		System.out.println("Try to use TELNET to connect to your own computer.");

		TelnetServer=new ColaServer();*/

		//		System.out.println("Starting chat server....");
		//myChat=new colabbs.chatroom.ChatRoom();
		
		//		System.setSecurityManager(new RMISecurityManager());

		/*		if(clientcount>0)
		{
		try
		{
		ClientServer remoteObj=new ClientServer();
		System.out.println("The Client/Server mode Start Up OK!");
		}
		catch(Exception e)
		{
		System.out.println("The Client/Server mode Start Up failure!");
		}
		}*/

		/*if(clientcount>0)
		{
			ClientServer clientserver1=new ClientServer();
			clientserver1.start();
		}*/
	}
	public static void ReSortAddUser(int pid)
	{
		synchronized(SortedUser)
		{
      fireAddOnlineUser(BBSUsers[pid]);
			int i=0,l,r,cmp=0;
			
			if(onlineuser!=0)
			{
				l=0;
				r=onlineuser-1;
				i=(onlineuser-1)/2;

				do
				{
					ColaServer.BBSlog.Write("Onlineuser = "+onlineuser+" i = "+i+" SortedUser[i] = " +SortedUser[i]);
					if(BBSUsers[SortedUser[i]]==null)
					{
						System.out.println("Error: BBSUsers[SortedUser[i]] is null!!");
						ColaServer.BBSlog.Write("Error: BBSUsers[SortedUser[i]] is null!!");
						ColaServer.BBSlog.Write("SortedUser[i] = "+SortedUser[i]);
						ColaServer.BBSlog.Write("i = "+i);
						for(int debug=0;debug<i;debug++)
							if(BBSUsers[SortedUser[i]]!=null)
								ColaServer.BBSlog.Write("SortedUser["+i+"].UFD.ID = "+BBSUsers[SortedUser[i]].UFD.ID);
						onlineuser++;  //進站後的一些初始化
						return;
					}
					if(BBSUsers[SortedUser[i]].UFD==null)
					{
						System.out.println("Error: BBSUsers[SortedUser[i]].UFD is null!!");
						ColaServer.BBSlog.Write("Error: BBSUsers[SortedUser[i]].UFD is null!!");
						ColaServer.BBSlog.Write("BBSUsers[SortedUser[i]] = "+BBSUsers[SortedUser[i]]);
						ColaServer.BBSlog.Write("SortedUser[i] = "+SortedUser[i]);
						ColaServer.BBSlog.Write("i = "+i);
						for(int debug=0;debug<i;debug++)
							if(BBSUsers[SortedUser[i]].UFD!=null)
								ColaServer.BBSlog.Write("SortedUser["+i+"].UFD.ID = "+BBSUsers[SortedUser[i]].UFD.ID);
						for(int j=i;j<onlineuser-1;j++)
							SortedUser[j]=SortedUser[j+1];
						if(onlineuser>0)
							onlineuser--;
						else
						{
							SortedUser[i]=pid;
							return;
						}
						//						onlineuser++;  //進站後的一些初始化
						//						return;
					}
					/*					if(BBSUsers[SortedUser[i]].UFD.ID==null)
					{
					System.out.println("Error: BBSUsers[SortedUser[i]].UFD.ID is null!!");
					ColaServer.BBSlog.Write("Error: BBSUsers[SortedUser[i]].UFD.ID is null!!");
					ColaServer.BBSlog.Write("BBSUsers[SortedUser[i]].UFD = "+BBSUsers[SortedUser[i]].UFD);
					ColaServer.BBSlog.Write("BBSUsers[SortedUser[i]] = "+BBSUsers[SortedUser[i]]);
					ColaServer.BBSlog.Write("SortedUser[i] = "+SortedUser[i]);
					ColaServer.BBSlog.Write("i = "+i);
					for(int debug=0;debug<i;debug++)
					if(BBSUsers[SortedUser[i]].UFD.ID!=null)
					ColaServer.BBSlog.Write("SortedUser["+i+"].UFD.ID = "+BBSUsers[SortedUser[i]].UFD.ID);
					onlineuser++;  //進站後的一些初始化
					return;
					}*/
					cmp=BBSUsers[pid].UFD.ID.toUpperCase().compareTo(BBSUsers[SortedUser[i]].UFD.ID.toUpperCase());
					if(cmp==0)
						break; /* Added by DarkSmile 98/10/13 */
					if(cmp<0)
					{
						r=i;
						i=(l+i)/2;
					}
					//					else if(cmp!=0)
					else
					{
						l=i;
						i=(i+r+1)/2;
					}
				}while(cmp!=0&&l<i&&r>i);
				cmp=BBSUsers[pid].UFD.ID.toUpperCase().compareTo(BBSUsers[SortedUser[i]].UFD.ID.toUpperCase());
				if(cmp>0)
					i++;
				for(cmp=onlineuser;cmp>i;cmp--)
					SortedUser[cmp]=SortedUser[cmp-1];
			}
			SortedUser[i]=pid;
			onlineuser++;  //進站後的一些初始化
		}
	}
	public static void ReSortRmUser(int pid)
	{
		int i,j;

		synchronized(SortedUser)
		{
			for(i=0;i<onlineuser;i++)
				if(SortedUser[i]==pid)
					break;
			for(j=i;j<onlineuser-1;j++)
				SortedUser[j]=SortedUser[j+1];
			ColaServer.onlineuser--;
      fireRemoveOnlineUser(BBSUsers[pid]);
		}
	}
	public void run()
	{

		try
		{
			if(INI.ServerAddress!=null)
				BBSServer = new ServerSocket(port,count,INI.ServerAddress);
			else
				BBSServer = new ServerSocket(port,count);
		}
		catch(IOException e)
		{
			System.out.println("Can't binding port!"+e);
			System.exit(1);
		}

		while(true)
		{
			try
			{
				int index;

				for(index=0;index<count;index++)
				{
					synchronized(BBSUsers)
					{
						if(BBSUsers[index]==null)
						{
							BBSUsers[index]=new TelnetUser(index);
							BBSUsers[index].UserLogout=false;
							BBSUsers[index].ReMsg=false;
							BBSUsers[index].MsgID=-1;
							BBSUsers[index].MsgNum=0;
							BBSUsers[index].usermode=Modes.Login;
							break;
						}
						else if(BBSUsers[index].UserLogout==true)
						{
							BBSUsers[index]=new TelnetUser(index);
							BBSUsers[index].UserLogout=false;
							BBSUsers[index].ReMsg=false;
							BBSUsers[index].MsgID=-1;
							BBSUsers[index].MsgNum=0;
							BBSUsers[index].usermode=Modes.Login;
							break;
						}
					}
				}
				if(index==count)
				{
					TIME.Delay(10000);
					continue;
				}
				BBSUsers[index].BBSSocket=BBSServer.accept();
				BBSUsers[index].from=BBSUsers[index].BBSSocket.getInetAddress();

				//if(WW_IP_AD.LookupIP(BBSUsers[index].from.getAddress()) == IP_AD)
				if (IPAccess.checkIP(BBSUsers[index].from) != 0)
				{
					synchronized(BBSUsers)
					{
						BBSUsers[index].UserLogout=true;
						BBSUsers[index].BBSSocket.close();
						BBSUsers[index]=null;
					}
					continue;
				}	

				BBSUsers[index].os=new DataOutputStream(new BufferedOutputStream(BBSUsers[index].BBSSocket.getOutputStream(),4096));
				BBSUsers[index].is=new DataInputStream(BBSUsers[index].BBSSocket.getInputStream());

				int Last1=0,Last2=0,InputBuf=0;

				BBSUsers[index].sends("\377\375\030");
				BBSUsers[index].sends("\377\372\030\001\377\360");
				BBSUsers[index].sends("\377\373\001");
				BBSUsers[index].sends("\377\373\003");

				BBSUsers[index].sends(Prompt.Msgs[194]+ColaServer.INI.BBSName+Prompt.Msgs[80]);
				try
				{
					BBSUsers[index].os.flush();
				}
				catch(Exception e){}
				BBSUsers[index].Home=null;
				//                System.out.println(BBSUsers[index].from.getHostName());
				//                BBSUsers[index].Home=BBSUsers[index].from.getHostName();
				if(BBSUsers[index].Home==null&&INI.BanUnregistIP)
				{
					synchronized(BBSUsers)
					{
						BBSUsers[index].UserLogout=true;
						BBSUsers[index].is.close();
						BBSUsers[index].os.close();
						BBSUsers[index].BBSSocket.close();
						BBSUsers[index]=null;
					}
					continue;
				}
				else if(BBSUsers[index].Home==null)
					//Changed by WilliamWey
					//BBSUsers[index].Home = BBSUsers[index].from.toString();
					BBSUsers[index].Home = NET.GetHostName(BBSUsers[index].from);

				BBSlog.Write(ColaServer.SysDATE.DateFormatter2.format(new Date())+" an user comes from "+BBSUsers[index].from.toString()+" accept in Telnet Port....");
				//				BBSlog.Write((new Date()).toString()+"User from "+BBSUsers[index].from.toString()+" accepted in Telnet Port....");
				//				BBSUsers[index].telnetmode=true;
				BBSUsers[index].start();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(Throwable e)
			{
				e.printStackTrace();
			}

		}
	}
	
	public static String GetVersionDate()
	{
		return VersionDate;
	}
  public static void fireAddOnlineUser(BBSUser theUser)
  {
		if(theUser != null)
		{
// because caller will synchronized it!
//			synchronized(ColaServer.SortedUser)
      {
      	//black
				RecordHandler rh=null;
    		Hashtable Black=null;

				try
				{
					rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theUser.UFD.ID.charAt(0))+File.separator+theUser.UFD.ID,"blacks");
					FriendType ft=new FriendType();
					int flen=(int)rh.recordNumber(ft);
					if(flen>=0)
					{
		  			Black=new Hashtable(flen);
						PassItem PassBuf=null;

						for(int i=0;i<=flen;i++)
						{
							rh.nextElement(ft);
							PassBuf=ColaServer.UFDList.getPass(ft.deleteBody());

							if(PassBuf!=null) //else delete the item?
								Black.put(new Integer(PassBuf.uid),None);
						}
					}
				}
				finally
				{
					if(rh!=null)
					{
						rh.close();
						rh=null;
					}
				}
		    //
      	int count=ColaServer.onlineuser;
				for (int i = 0; i < count; i++)
				{
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SeeCloak)&&(!theUser.Visible))
            continue;
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SYSOP)&&Black!=null&&Black.containsKey(new Integer(BBSUsers[SortedUser[i]].uid)))
            continue;
					BBSUsers[SortedUser[i]].AddOnlineUser(theUser);
				}
      }
		}
  }

  public static void fireRemoveOnlineUser(BBSUser theUser)
  {
		if(theUser != null)
		{
//			synchronized(ColaServer.SortedUser)
      {
      	//black
				RecordHandler rh=null;
    		Hashtable Black=null;

				try
				{
					rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theUser.UFD.ID.charAt(0))+File.separator+theUser.UFD.ID,"blacks");
					FriendType ft=new FriendType();
					int flen=(int)rh.recordNumber(ft);
					if(flen>=0)
					{
		  			Black=new Hashtable(flen);
						PassItem PassBuf=null;

						for(int i=0;i<=flen;i++)
						{
							rh.nextElement(ft);
							PassBuf=ColaServer.UFDList.getPass(ft.deleteBody());

							if(PassBuf!=null) //else delete the item?
								Black.put(new Integer(PassBuf.uid),None);
						}
					}
				}
				finally
				{
					if(rh!=null)
					{
						rh.close();
						rh=null;
					}
				}
		    //
      	int count=ColaServer.onlineuser;
				for (int i = 0; i < count; i++)
				{
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SeeCloak)&&(!theUser.Visible))
            continue;
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SYSOP)&&Black!=null&&Black.containsKey(new Integer(BBSUsers[SortedUser[i]].uid)))
            continue;
					BBSUsers[SortedUser[i]].RemoveOnlineUser(theUser);
				}
      }
		}
  }

  public static void fireUserStateChanged(BBSUser theUser)
  {
		if(theUser != null)
		{
//			synchronized(ColaServer.SortedUser)
      {
      	//black
				RecordHandler rh=null;
    		Hashtable Black=null;

				try
				{
					rh=new RecordHandler(ColaServer.INI.BBSHome+"home"+File.separator+Character.toUpperCase(theUser.UFD.ID.charAt(0))+File.separator+theUser.UFD.ID,"blacks");
					FriendType ft=new FriendType();
					int flen=(int)rh.recordNumber(ft);
					if(flen>=0)
					{
		  			Black=new Hashtable(flen);
						PassItem PassBuf=null;

						for(int i=0;i<=flen;i++)
						{
							rh.nextElement(ft);
							PassBuf=ColaServer.UFDList.getPass(ft.deleteBody());

							if(PassBuf!=null) //else delete the item?
								Black.put(new Integer(PassBuf.uid),None);
						}
					}
				}
				finally
				{
					if(rh!=null)
					{
						rh.close();
						rh=null;
					}
				}
		    //
      	int count=ColaServer.onlineuser;
				for (int i = 0; i < count; i++)
				{
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SeeCloak)&&(!theUser.Visible))
            continue;
          if(!colabbs.UTILS.BBS.HasOnePerm(SortedUser[i],Perm.SYSOP)&&Black!=null&&Black.containsKey(new Integer(BBSUsers[SortedUser[i]].uid)))
            continue;
					BBSUsers[SortedUser[i]].UserStateChanged(theUser);
				}
      }
		}
  }
}