package colabbs.telnet.EHS;

import java.io.*;
import java.util.*;
import java.text.*;

import colabbs.*;
import colabbs.EVENTHANDLER.TELNET.*;
import colabbs.telnet.*;

public class LoginCounter extends EH_LOGIN
{
	public int Handle(TelnetUser who)
	{
		String Path = ColaServer.getSysTempPath() + File.separator + "LoginCounter";
		File F = new File(Path);
		if (!F.exists())
			F.mkdirs();
		
		SimpleDateFormat FileNameDateFormat = new SimpleDateFormat("yyyyMMdd.HH", Locale.ENGLISH);
		FileNameDateFormat.setTimeZone(ColaServer.SysDATE.SysTimeZone);
		
		Calendar now = ColaServer.SysDATE.getNow();
		DataOutputStream dos = null;
		try
		{
			dos = new DataOutputStream(new FileOutputStream(Path + File.separator + FileNameDateFormat.format(now.getTime()), true));
			dos.writeByte(48);
			dos.close();
			dos = null;
		}
		catch (Exception e)
		{
			try
			{
				if (dos != null)
					dos.close();
			}
			catch (Exception e1)
			{
			}
		}

		return 0;
	}
}
