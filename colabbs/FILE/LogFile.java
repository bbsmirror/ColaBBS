package colabbs.FILE;

import colabbs.*;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

public class LogFile
{
	public final static int FileNameStyle_default = 0;
	public final static int FileNameStyle_StartUp = 1;
	public final static int FileNameStyle_PerDay = 2;
	public final static int FileNameStyle_OnlyOne = 3;
	public final static int FileNameStyle_PerMonth = 4;
	
	private String TaskName = new String();
	private String Path = new String();
	private String FileName = new String();
	private boolean InSubFolder = false;
	private int FileNameStyle = LogFile.FileNameStyle_default;
	
	private SimpleDateFormat FileNameDateFormat = null;
	private static SimpleDateFormat LogDateFormat = null;
	private int thisDay = 0;
	
	private DataOutputStream LogFileOut;
	
	/**
	 * 取得同樣設定的 LogFile 物件
	 */
	public LogFile getInstance()
	{
		return new LogFile(TaskName, InSubFolder, FileNameStyle);
	}
	
	public LogFile(String xTaskName, boolean isInSubFolder, int xFileNameStyle)
	{
		if (LogDateFormat == null)
			LogDateFormat = new SimpleDateFormat("yyyy'/'MM'/'dd HH:mm:ss", Locale.ENGLISH); 
		
		try
		{
			TaskName = xTaskName;
			InSubFolder = isInSubFolder;
			FileNameStyle = xFileNameStyle; 
			
			Path = ColaServer.INI.BBSHome + "log\\";
			if (isInSubFolder)
				Path = Path + TaskName + "\\";
			File fPath = new File(Path);
			if (!fPath.exists())
				fPath.mkdirs();
		
			Calendar Now;
			switch (FileNameStyle)
			{
			case FileNameStyle_default:
			case FileNameStyle_StartUp:
				FileName = ColaServer.StartUpTime + ".log";
				break;
			case FileNameStyle_PerDay:
				if (FileNameDateFormat == null)
				   FileNameDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
				//Now = Calendar.getInstance();
				Now = ColaServer.SysDATE.getNow();
				FileName = FileNameDateFormat.format(Now.getTime()) + ".log";
				thisDay = Now.get(Calendar.DAY_OF_MONTH);
				break;
			case FileNameStyle_OnlyOne:
				FileName = xTaskName + ".log";
				break;
			case FileNameStyle_PerMonth:
				if (FileNameDateFormat == null)
				   FileNameDateFormat = new SimpleDateFormat("yyyyMM", Locale.ENGLISH);				
				//Now = Calendar.getInstance();
				Now = ColaServer.SysDATE.getNow();
				FileName = FileNameDateFormat.format(Now.getTime()) + ".log";
				thisDay = Now.get(Calendar.MONTH);
				break;
			} 
		
			LogFileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Path + FileName, true)));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}
	
	private void CheckIt()
	{
		try
		{
			Calendar Now;
			switch (FileNameStyle)
			{
			case FileNameStyle_PerDay:
				//Now = Calendar.getInstance();
				Now = ColaServer.SysDATE.getNow();
				if (Now.get(Calendar.DAY_OF_MONTH) != thisDay)
				{
					LogFileOut.flush();
					LogFileOut.close();
					FileName = FileNameDateFormat.format(Now.getTime()) + ".log";
					thisDay = Now.get(Calendar.DAY_OF_MONTH);
					LogFileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Path + FileName)));
				}
				break;
			case FileNameStyle_PerMonth:
				//Now = Calendar.getInstance();
				Now = ColaServer.SysDATE.getNow();
				if (Now.get(Calendar.MONTH) != thisDay)
				{
					LogFileOut.flush();
					LogFileOut.close();
					FileName = FileNameDateFormat.format(Now.getTime()) + ".log";
					thisDay = Now.get(Calendar.MONTH);
					LogFileOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(Path + FileName)));
				}
				break;
			}		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}
	
	public synchronized void Write(String Msg)
	{
		CheckIt();
		
		try
		{
			//LogFileOut.write(new String(LogDateFormat.format(Calendar.getInstance().getTime()) + ", " + Msg + "\r\n").getBytes());
			LogFileOut.write(new String(LogDateFormat.format(ColaServer.SysDATE.getNow().getTime()) + ", " + Msg + "\r\n").getBytes());
			LogFileOut.flush();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}
	}

	private String GetExceptionMsg(Exception E)
	{
		String Msg = new String();
		try
		{
			throw E;
		}
		catch (UnknownHostException e)
		{
			Msg = "無法獲知的位址";
		}
		catch (UnsupportedEncodingException e)
		{
			Msg = "無法支援的字碼頁";
		}
		catch (FileNotFoundException e)
		{
			Msg = "找不到檔案";
		}
		catch (IOException e)
		{
			Msg = "IO 發生錯誤";
		}
		catch(Exception e)
		{
			Msg = "其他錯誤";
		}
		finally
		{
			Msg = Msg + " " + E.getMessage();
		}
		return Msg;
	}
	
	public void WriteException(String LMsg, Exception e)
	{
		Write(LMsg + " " + GetExceptionMsg(e));
	}

	public void WriteException(Exception e, String RMsg)
	{
		Write(GetExceptionMsg(e) + " " + RMsg);
	}
	
	public void WriteException(String LMsg, Exception e, String RMsg)
	{
		Write(LMsg + " " + GetExceptionMsg(e) + " " + RMsg);
	}
	
	public void WriteException(Exception e)
	{
		Write(GetExceptionMsg(e));
	}
	
	public void Close()
	{
		try
		{		
			LogFileOut.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println(e.toString());
		}			
	}
}
