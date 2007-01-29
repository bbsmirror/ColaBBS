package colabbs;

import java.net.*;
import java.io.*;
import java.util.*;

public class Prompt
{
	public static String Msgs[];

	public static boolean LoadFile(String FileName)
	{
		int lines = 0, i, seeks;
		String tmp;
		StringBuffer sb;
		RandomAccessFile MsgFile = null;

		boolean Flag = false;
		
		try
		{
			MsgFile = new RandomAccessFile(FileName, "r");
			while ((tmp = MsgFile.readLine()) != null)
				lines++;
			Msgs = new String[lines + 1];
			MsgFile.seek(0);
			for (i = 1; i <= lines; i++)
			{
				tmp = MsgFile.readLine();
				if (tmp.charAt(tmp.length() - 1) == 13)
					tmp = tmp.substring(0, tmp.length() - 1);
				sb = new StringBuffer(tmp);
				seeks = -2;
				while ((seeks = tmp.indexOf("\\n", seeks + 2)) != -1)
				{
					sb.setCharAt(seeks, (char)13);
					sb.setCharAt(seeks + 1, (char)10);
				}
				Msgs[i] = sb.toString();
			}
			
			Flag = true;
		}
		catch(IOException e)
		{
			ColaServer.ErrorMsg = "IO Error: " + e.getMessage();
		}
		finally
		{
			try
			{
				if(MsgFile != null)
				{
					MsgFile.close();
					MsgFile = null;
				}	
			}
			catch(IOException e)
			{
				ColaServer.ErrorMsg = "Error: " + e.getMessage();
				Flag = false;
			}
		}
		
		return Flag;
	}	
}